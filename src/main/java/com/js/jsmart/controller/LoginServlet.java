package com.js.jsmart.controller;

import com.js.jsmart.dao.DAOFactory;
import com.js.jsmart.dto.ApiResponse;
import com.js.jsmart.dto.UserResponseDTO;
import com.js.jsmart.model.User;
import com.js.jsmart.service.AuthService;
import com.js.jsmart.util.ConfigUtil;
import com.js.jsmart.util.JsonUtil;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** POST /api/v1/auth/login : session login with ID regeneration + timeout. */
@WebServlet("/api/v1/auth/login")
public class LoginServlet extends BaseServlet {
  /** Default session timeout in minutes when config is absent or invalid. */
  public static final int DEFAULT_TIMEOUT_MINUTES = 30;
  /** Seconds per minute for HttpSession interval conversion. */
  public static final int SECONDS_PER_MINUTE = 60;
  private AuthService auth = new AuthService(DAOFactory.users());

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      String email = req.getParameter("email");
      String password = req.getParameter("password");
      if (email == null && req.getContentType() != null && req.getContentType().contains("json")) {
        @SuppressWarnings("unchecked")
        Map<String, String> body = JsonUtil.fromBody(req, Map.class);
        if (body != null) {
          email = body.get("email");
          password = body.get("password");
        }
      }
      User u = auth.login(email, password);
      HttpSession old = req.getSession(false);
      if (old != null) {
        old.invalidate();
      }
      HttpSession s = req.getSession(true);
      s.setAttribute("user", u);
      int timeoutMin = DEFAULT_TIMEOUT_MINUTES;
      try {
        timeoutMin = Integer.parseInt(
            ConfigUtil.get("session.timeoutMinutes", String.valueOf(DEFAULT_TIMEOUT_MINUTES)));
      } catch (NumberFormatException e) {
        timeoutMin = DEFAULT_TIMEOUT_MINUTES;
      }
      s.setMaxInactiveInterval(timeoutMin * SECONDS_PER_MINUTE);
      boolean isForm = req.getParameter("email") != null
          && (req.getContentType() == null || !req.getContentType().contains("json"));
      if (isForm) {
        String role = u.getRole().name();
        String home = "/index.jsp";
        if ("ADMIN".equals(role)) {
          home = "/admin/dashboard";
        } else if ("SELLER".equals(role)) {
          home = "/seller/dashboard";
        }
        com.js.jsmart.util.Flash.successPage(req, "Login Successful! \uD83C\uDF89",
            "Welcome back to JackMart", "Continue Shopping", home);
        resp.sendRedirect(req.getContextPath() + "/success.jsp");
        return;
      }
      JsonUtil.write(resp, 200, ApiResponse.ok(UserResponseDTO.from(u)));
    } catch (IOException | RuntimeException e) {
      // Centralized mapping: known failures become JSON/redirect responses via sendError,
      // never container error pages. Errors (not Exceptions) still propagate.
      boolean isForm = req.getParameter("email") != null
          && (req.getContentType() == null || !req.getContentType().contains("json"));
      if (isForm) {
        com.js.jsmart.util.Flash.error(req, "Invalid email or password.");
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
        return;
      }
      sendError(resp, e);
    }
  }

  /** Visible for tests. */
  public void setAuthService(AuthService authService) {
    this.auth = authService;
  }
}
