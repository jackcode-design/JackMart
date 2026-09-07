package com.jack.jackmart.controller;
import com.jack.jackmart.listener.DatabaseInitializer;import com.jack.jackmart.dao.jdbc.CartDAOImpl;import com.jack.jackmart.service.CartService;import com.jack.jackmart.model.User;import javax.servlet.*;import javax.servlet.http.*;import java.io.IOException;
public class CartServlet extends HttpServlet {private CartService service;public void init(){service=new CartService(new CartDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
 private long uid(HttpServletRequest r){return ((User)r.getSession().getAttribute("user")).getId();}
 protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{try{long u=uid(r);r.setAttribute("cart",service.items(u));r.setAttribute("total",service.total(u));r.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(r,s);}catch(Exception e){throw new ServletException(e);}}
 protected void doPost(HttpServletRequest r,HttpServletResponse s)throws IOException{try{service.add(uid(r),Long.parseLong(r.getParameter("productId")),1);s.sendRedirect(r.getContextPath()+"/cart");}catch(Exception e){throw new RuntimeException(e);}}
}