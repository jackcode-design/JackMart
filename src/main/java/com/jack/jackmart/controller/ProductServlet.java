package com.jack.jackmart.controller;
import com.jack.jackmart.listener.DatabaseInitializer;import com.jack.jackmart.dao.jdbc.ProductDAOImpl;import com.jack.jackmart.service.ProductService;import javax.servlet.*;import javax.servlet.http.*;import java.io.IOException;
public class ProductServlet extends HttpServlet {private ProductService service;public void init(){service=new ProductService(new ProductDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
 protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{try{r.setAttribute("products",service.search(r.getParameter("q"),r.getParameter("category")));r.getRequestDispatcher("/WEB-INF/jsp/products.jsp").forward(r,s);}catch(Exception e){throw new ServletException(e);}}
}