package com.jack.jackmart.filter;

import com.jack.jackmart.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

public class RoleFilter implements Filter {
    private Set<String> allowed = Set.of();
    public void init(FilterConfig c){
        String roles=c.getInitParameter("roles");
        if(roles!=null) allowed=Set.of(roles.split(","));
    }
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
        HttpServletRequest r=(HttpServletRequest)req;
        HttpServletResponse s=(HttpServletResponse)res;
        User u=(User)r.getSession().getAttribute("user");
        if(u==null){s.sendError(HttpServletResponse.SC_UNAUTHORIZED);return;}
        if(!allowed.contains(u.getRole())){s.sendError(HttpServletResponse.SC_FORBIDDEN);return;}
        chain.doFilter(req,res);
    }
}
