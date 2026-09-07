package com.jack.jackmart.controller;

import com.jack.jackmart.dao.jdbc.SellerDAOImpl;
import com.jack.jackmart.listener.DatabaseInitializer;
import com.jack.jackmart.model.Product;
import com.jack.jackmart.model.User;
import com.jack.jackmart.service.SellerService;
import com.jack.jackmart.util.ValidationUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class SellerProductServlet extends HttpServlet {
    private SellerService service;
    public void init(){service=new SellerService(new SellerDAOImpl(DatabaseInitializer.dataSource(getServletContext())));}
    protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
        try{
            User u=(User)req.getSession().getAttribute("user");
            Product p=new Product();
            String id=req.getParameter("id");
            if(id!=null&&!id.isBlank())p.setId(Long.parseLong(id));
            p.setName(ValidationUtil.required(req.getParameter("name"),"name"));
            p.setDescription(req.getParameter("description"));
            p.setPrice(ValidationUtil.nonNegativeMoney(req.getParameter("price"),"price"));
            p.setStockQty(ValidationUtil.nonNegativeInt(req.getParameter("stockQty"),"stockQty"));
            p.setCategory(ValidationUtil.required(req.getParameter("category"),"category"));
            p.setImageUrl(req.getParameter("imageUrl"));
            if(p.getId()==null) service.create(p,u.getId()); else if(!service.update(p,u.getId())) {res.sendError(404);return;}
            res.sendRedirect(req.getContextPath()+"/seller");
        }catch(IllegalArgumentException e){res.sendError(400,e.getMessage());}catch(Exception e){throw new ServletException(e);}
    }
}
