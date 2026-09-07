package com.jack.jackmart.controller;

import com.jack.jackmart.listener.DatabaseInitializer;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import javax.sql.DataSource;

public class HealthServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException{
        res.setContentType("application/json");
        DataSource ds=DatabaseInitializer.dataSource(getServletContext());
        try(Connection c=ds.getConnection()){
            res.getWriter().print("{\"status\":\"UP\",\"db\":\"UP\"}");
        }catch(Exception e){
            res.setStatus(500);
            res.getWriter().print("{\"status\":\"DOWN\",\"db\":\"DOWN\"}");
        }
    }
}
