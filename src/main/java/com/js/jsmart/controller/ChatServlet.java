package com.js.jsmart.controller;

import com.js.jsmart.dao.DAOFactory;
import com.js.jsmart.dto.ApiResponse;
import com.js.jsmart.service.chat.ChatService;
import com.js.jsmart.util.JsonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * POST /api/v1/chat : chatbot endpoint (canonical).
 * "/api/chat" is a deprecated compatibility alias and may be removed.
 */
@WebServlet({"/api/v1/chat", "/api/chat"})
public class ChatServlet extends BaseServlet {
  /** Products included in the server-side chat context. */
  public static final int TOP_PRODUCTS_LIMIT = 5;
  private ChatService chat = new ChatService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      String message = req.getParameter("message");
      if ((message == null) && req.getContentType() != null && req.getContentType().contains("json")) {
        @SuppressWarnings("unchecked")
        Map<String, String> body = JsonUtil.fromBody(req, Map.class);
        if (body != null) {
          message = body.get("message");
        }
      }
      if (message == null) {
        message = "";
      }
      HttpSession s = req.getSession(true);
      // Bridge the per-session rate-limit list and reply cache into a session map.
      // ChatService stores both back into this map; we persist them on the HttpSession
      // so limits and cached replies never leak across sessions.
      Map<String, Object> scoped = new HashMap<>();
      Object hits = s.getAttribute("chatHits");
      if (hits instanceof java.util.List) {
        scoped.put("chatHits", hits);
      }
      Object cached = s.getAttribute("chatCache");
      if (cached instanceof Map) {
        scoped.put("chatCache", cached);
      }
      String reply = chat.reply(scoped, message, buildContext());
      s.setAttribute("chatHits", scoped.get("chatHits"));
      s.setAttribute("chatCache", scoped.get("chatCache"));
      JsonUtil.write(resp, 200, ApiResponse.ok(java.util.Collections.singletonMap("reply", reply)));
    } catch (Exception e) {
      sendError(resp, e);
    }
  }

  private String buildContext() {
    try {
      var list = DAOFactory.products().findAll(TOP_PRODUCTS_LIMIT, 0);
      StringBuilder sb = new StringBuilder("Top products: ");
      for (var p : list) {
        sb.append(p.getName()).append(" (Rs.").append(p.getPrice()).append("); ");
      }
      return sb.toString();
    } catch (Exception e) {
      return "JackMart demo catalog";
    }
  }

  /** Visible for tests. */
  public void setChatService(ChatService c) {
    this.chat = c;
  }
}
