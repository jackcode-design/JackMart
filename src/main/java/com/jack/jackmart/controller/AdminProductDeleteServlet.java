package com.jack.jackmart.controller;

import com.jack.jackmart.dao.jdbc.AdminDAOImpl;
import com.jack.jackmart.listener.DatabaseInitializer;
import com.jack.jackmart.service.AdminService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AdminProductDeleteServlet extends HttpServlet {
    private AdminService service;
    public void init(){service=new AdminService(new AdminDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
    protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            long id=Long.parseLong(req.getParameter("id"));
            if(!service.removeProduct(id)){res.sendError(404);return;}
            res.sendRedirect(req.getContextPath()+"/admin");
        }catch(Exception e){throw new ServletException(e);}
    }
}
