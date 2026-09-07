package com.jack.jackmart.filter;
import com.jack.jackmart.model.User;import javax.servlet.*;import javax.servlet.http.*;import java.io.IOException;
public class AuthFilter implements Filter {public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{HttpServletRequest r=(HttpServletRequest)req;HttpServletResponse s=(HttpServletResponse)res;User u=(User)r.getSession().getAttribute("user");if(u==null){s.sendRedirect(r.getContextPath()+"/login");return;}chain.doFilter(req,res);}}
