package com.jack.jackmart.controller;

import com.jack.jackmart.dao.jdbc.ReviewDAOImpl;
import com.jack.jackmart.listener.DatabaseInitializer;
import com.jack.jackmart.model.User;
import com.jack.jackmart.service.ReviewService;
import com.jack.jackmart.util.ValidationUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ReviewServlet extends HttpServlet {
    private ReviewService service;
    public void init(){service=new ReviewService(new ReviewDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
    protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            User u=(User)req.getSession().getAttribute("user");
            long productId=Long.parseLong(req.getParameter("productId"));
            int rating=ValidationUtil.rating(req.getParameter("rating"));
            service.add(u.getId(),productId,rating,req.getParameter("comment"));
            res.sendRedirect(req.getContextPath()+"/orders");
        }catch(IllegalArgumentException e){res.sendError(400,e.getMessage());}catch(Exception e){throw new ServletException(e);}
    }
}
