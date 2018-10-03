package com.example.toutiao.intercept;

import com.example.toutiao.dao.LoginTicketDAO;
import com.example.toutiao.dao.UserDAO;
import com.example.toutiao.model.HostHolder;
import com.example.toutiao.model.LoginTicket;
import com.example.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
//每次访问前，拦截器插进来，找这个用户
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HostHolder hostHolder;
    @Override
    //boolean preHandle() 该方法在处理请求之前进行调用，就是在执行Controller的任务之前。
    // 如果返回true就继续往下执行，返回false就放弃执行。
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception{


        String ticket=null;
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie :httpServletRequest.getCookies()){
               if( cookie.getName().equals("ticket")){
                   ticket=cookie.getValue();
                   break;
               }
            }
        }
        if(ticket!=null){
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            User user=userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    //void postHandle()该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，
    // 可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
           if(modelAndView!=null&&hostHolder.getUser()!=null){
                modelAndView.addObject("user",hostHolder.getUser());//后端代码和前端交互的地方，登陆后显示用户名
           }
    }

    @Override
    //void afterCompletion()该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，该方法将在整个请求结束之后，
    // 也就是在DispatcherServlet 渲染了对应的视图之后执行。用于进行资源清理。
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
            hostHolder.clear();

    }
}
