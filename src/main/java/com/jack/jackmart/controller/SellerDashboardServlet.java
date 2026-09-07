package com.jack.jackmart.controller;

import com.jack.jackmart.dao.jdbc.SellerDAOImpl;
import com.jack.jackmart.listener.DatabaseInitializer;
import com.jack.jackmart.model.User;
import com.jack.jackmart.service.SellerService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class SellerDashboardServlet extends HttpServlet {
    private SellerService service;
    public void init(){service=new SellerService(new SellerDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
    protected void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            User u=(User)req.getSession().getAttribute("user");
            req.setAttribute("products",service.products(u.getId()));
            req.setAttribute("orders",service.incomingOrders(u.getId()));
            req.getRequestDispatcher("/WEB-INF/jsp/seller.jsp").forward(req,res);
        }catch(Exception e){throw new ServletException(e);}
    }
}
