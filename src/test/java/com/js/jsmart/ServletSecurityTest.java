package com.js.jsmart;

import com.js.jsmart.controller.HealthServlet;
import com.js.jsmart.controller.LoginServlet;
import com.js.jsmart.dto.ApiResponse;
import com.js.jsmart.filter.AuthFilter;
import com.js.jsmart.model.Role;
import com.js.jsmart.model.User;
import com.js.jsmart.service.AuthService;
import com.js.jsmart.service.chat.ChatProvider;
import com.js.jsmart.service.chat.ChatService;
import com.js.jsmart.service.chat.MockChatProvider;
import com.zaxxer.hikari.HikariDataSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Servlet + filter + chatbot + envelope tests.
 */
class ServletSecurityTest {

  @Test
  void loginSetsSessionAndStatus() throws Exception {
    User u = new User();
    u.setId(1);
    u.setEmail("b@x.com");
    u.setRole(Role.BUYER);
    u.setFullName("B");
    AuthService auth = mock(AuthService.class);
    when(auth.login("b@x.com", "pw")).thenReturn(u);
    LoginServlet servlet = new LoginServlet();
    servlet.setAuthService(auth);

    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    when(req.getParameter("email")).thenReturn("b@x.com");
    when(req.getParameter("password")).thenReturn("pw");
    when(req.getContentType()).thenReturn("application/json");
    when(req.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(
        "{\"email\":\"b@x.com\",\"password\":\"pw\"}")));
    when(req.getSession(true)).thenReturn(session);
    when(req.getSession(false)).thenReturn(null);
    java.io.StringWriter sw = new java.io.StringWriter();
    when(resp.getWriter()).thenReturn(new java.io.PrintWriter(sw));

    servlet.doPost(req, resp);
    verify(session).setAttribute(eq("user"), eq(u));
    verify(resp).setStatus(200);
    assertTrue(sw.toString().contains("\"success\":true"));
    assertFalse(sw.toString().contains("passwordHash"));
  }

  @Test
  void authFilterBlocksAnonymousApi() throws Exception {
    AuthFilter f = new AuthFilter();
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);
    when(req.getRequestURI()).thenReturn("/jackmart/api/v1/cart");
    when(req.getContextPath()).thenReturn("/jackmart");
    when(req.getSession(false)).thenReturn(null);
    java.io.StringWriter sw = new java.io.StringWriter();
    when(resp.getWriter()).thenReturn(new java.io.PrintWriter(sw));
    f.doFilter(req, resp, chain);
    verify(resp).setStatus(401);
    verify(chain, never()).doFilter(req, resp);
  }

  @Test
  void authFilterBlocksBuyerOnAdmin() throws Exception {
    AuthFilter f = new AuthFilter();
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);
    HttpSession s = mock(HttpSession.class);
    User buyer = new User();
    buyer.setRole(Role.BUYER);
    when(s.getAttribute("user")).thenReturn(buyer);
    when(req.getSession(false)).thenReturn(s);
    when(req.getRequestURI()).thenReturn("/jackmart/admin/dashboard");
    when(req.getContextPath()).thenReturn("/jackmart");
    f.doFilter(req, resp, chain);
    verify(resp).sendError(403, "Forbidden");
    verify(chain, never()).doFilter(req, resp);
  }

  @Test
  void userControlledFieldsRenderThroughCOut() throws Exception {
    assertEscaped("src/main/webapp/WEB-INF/views/product-list.jsp",
        "p.name", "p.category", "p.price", "param.q", "param.category");
    assertEscaped("src/main/webapp/WEB-INF/views/product-detail.jsp",
        "product.name", "product.description", "product.category", "r.comment", "r.userName");
    assertEscaped("src/main/webapp/WEB-INF/views/orders.jsp",
        "o.status", "i.productName", "param.success");
    assertEscaped("src/main/webapp/WEB-INF/views/cart.jsp",
        "i.productName", "i.unitPrice");
  }

  @Test
  void chatWidgetEscapesAngleBrackets() throws Exception {
    java.nio.file.Path js = java.nio.file.Path.of("src/main/webapp/assets/js/chat.js");
    assertTrue(java.nio.file.Files.exists(js), "chat widget source must exist");
    String content = java.nio.file.Files.readString(js);
    assertTrue(content.contains(".replace(/</g, '&lt;')"),
        "chat widget must escape < before injecting messages into the page");
  }

  private static void assertEscaped(String path, String... fields) throws Exception {
    java.nio.file.Path file = java.nio.file.Path.of(path);
    assertTrue(java.nio.file.Files.exists(file), "view must exist: " + path);
    String content = java.nio.file.Files.readString(file);
    for (String field : fields) {
      String expr = "${" + field + "}";
      boolean wrapped = content.contains("<c:out value=\"" + expr + "\"")
          || content.contains("<c:out value='" + expr + "'");
      assertTrue(wrapped, path + " must render " + expr + " through <c:out>");
    }
  }

  @Test
  void apiEnvelopeShape() {
    ApiResponse ok = ApiResponse.ok(Map.of("a", 1));
    assertTrue(ok.isSuccess());
    assertNull(ok.getError());
    ApiResponse err = ApiResponse.fail("VALIDATION_ERROR", "bad");
    assertFalse(err.isSuccess());
    assertNull(err.getData());
    assertEquals("VALIDATION_ERROR", err.getError().get("code"));
  }

  @Test
  void chatRateLimitAndCache() {
    ChatService chat = new ChatService(new MockChatProvider());
    Map<String, Object> sess = new HashMap<>();
    String first = chat.reply(sess, "shipping time?", "ctx");
    String second = chat.reply(sess, "shipping time?", "ctx");
    assertEquals(first, second); // cached
    for (int i = 0; i < 10; i++) {
      chat.reply(sess, "q" + i, "ctx");
    }
    assertTrue(chat.reply(sess, "one more", "ctx").contains("Rate limit"));
  }

  @Test
  void chatCacheIsPerSession() {
    final int[] calls = {0};
    ChatProvider counting = (msg, ctx) -> "reply-" + (calls[0]++);
    ChatService chat = new ChatService(counting);
    Map<String, Object> first = new HashMap<>();
    Map<String, Object> second = new HashMap<>();
    String r1 = chat.reply(first, "hello", "ctx");
    String r2 = chat.reply(second, "hello", "ctx");
    assertNotEquals(r1, r2, "different sessions must not share cached replies");
    assertEquals(2, calls[0], "provider must be called once per session");
    assertEquals(r1, chat.reply(first, "hello", "ctx"), "same session must hit its own cache");
    assertEquals(2, calls[0], "cached reply must not call provider again");
  }

  @Test
  void healthReturnsUpWhenDbHealthy() throws Exception {
    HikariDataSource ds = TestDb.init();
    try {
      var servlet = new HealthServlet() {
        void invoke(HttpServletRequest req, HttpServletResponse resp) throws java.io.IOException {
          doGet(req, resp);
        }
      };
      HttpServletRequest req = mock(HttpServletRequest.class);
      HttpServletResponse resp = mock(HttpServletResponse.class);
      StringWriter sw = new StringWriter();
      when(resp.getWriter()).thenReturn(new PrintWriter(sw));

      servlet.invoke(req, resp);

      verify(resp).setStatus(200);
      verify(resp).setContentType("application/json;charset=UTF-8");
      String body = sw.toString();
      assertTrue(body.contains("\"status\":\"UP\""), "health body must report status UP: " + body);
      assertTrue(body.contains("\"db\":\"UP\""), "health body must report db UP: " + body);
    } finally {
      ds.close();
    }
  }
}
