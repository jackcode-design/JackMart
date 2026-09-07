package com.jack.jackmart.filter;
import javax.servlet.*;import java.io.IOException;
public class EncodingFilter implements Filter {public void doFilter(ServletRequest r,ServletResponse s,FilterChain chain)throws IOException,ServletException{r.setCharacterEncoding("UTF-8");s.setCharacterEncoding("UTF-8");chain.doFilter(r,s);}}
