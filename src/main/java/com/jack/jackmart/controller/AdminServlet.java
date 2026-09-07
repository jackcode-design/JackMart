package com.jack.jackmart.controller;

import com.jack.jackmart.dao.jdbc.AdminDAOImpl;
import com.jack.jackmart.listener.DatabaseInitializer;
import com.jack.jackmart.service.AdminService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AdminServlet extends HttpServlet {
    private AdminService service;
    public void init(){service=new AdminService(new AdminDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
    protected void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            req.setAttribute("users",service.users());
            req.setAttribute("orders",service.orders());
            req.setAttribute("products",service.products());
            req.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(req,res);
        }catch(Exception e){throw new ServletException(e);}
    }
}
