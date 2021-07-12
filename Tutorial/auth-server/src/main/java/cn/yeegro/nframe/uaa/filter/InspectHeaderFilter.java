//package cn.yeegro.nframe.uaa.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
// 
//@Slf4j
//@Component
//public class InspectHeaderFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//
//
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        log.debug("得到头信息中的Authorization: " + httpServletRequest.getHeader("Authorization"));
//        
////        String header = httpServletRequest.getHeader("Authorization") ;
////        String token = StringUtils.substringAfter(header, "bearer ") ;
////
////        Claims claims =  Jwts.parser().setSigningKey("neusoft".getBytes("UTF-8")).parseClaimsJws(token).getBody() ;
////
////        logger.debug("claims: " + claims);
//        
//        filterChain.doFilter(httpServletRequest, servletResponse);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {}
//
//    @Override
//    public void destroy() {}
//}