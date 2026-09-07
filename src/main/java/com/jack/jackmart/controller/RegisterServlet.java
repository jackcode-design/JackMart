package com.jack.jackmart.controller;
import com.jack.jackmart.listener.DatabaseInitializer;import com.jack.jackmart.dao.jdbc.UserDAOImpl;import com.jack.jackmart.service.UserService;import javax.servlet.*;import javax.servlet.http.*;import java.io.IOException;
public class RegisterServlet extends HttpServlet {private UserService service;public void init(){service=new UserService(new UserDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
 protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{r.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(r,s);}
 protected void doPost(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{try{service.register(r.getParameter("name"),r.getParameter("email"),r.getParameter("password"));s.sendRedirect(r.getContextPath()+"/login");}catch(Exception e){r.setAttribute("error",e.getMessage());doGet(r,s);}}
}