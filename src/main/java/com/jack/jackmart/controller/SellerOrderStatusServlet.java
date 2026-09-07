package com.jack.jackmart.controller;

import com.jack.jackmart.dao.jdbc.SellerDAOImpl;
import com.jack.jackmart.listener.DatabaseInitializer;
import com.jack.jackmart.model.User;
import com.jack.jackmart.service.SellerService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class SellerOrderStatusServlet extends HttpServlet {
    private SellerService service;
    public void init(){service=new SellerService(new SellerDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
    protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            User u=(User)req.getSession().getAttribute("user");
            long orderId=Long.parseLong(req.getParameter("orderId"));
            String status=req.getParameter("status");
            if(!service.updateStatus(orderId,u.getId(),status)){res.sendError(404);return;}
            res.sendRedirect(req.getContextPath()+"/seller");
        }catch(IllegalArgumentException e){res.sendError(400,e.getMessage());}catch(Exception e){throw new ServletException(e);}
    }
}
