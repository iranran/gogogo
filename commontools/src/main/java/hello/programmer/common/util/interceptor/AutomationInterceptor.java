package hello.programmer.common.util.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * change this template use File | Settings | File Templates.
 * 于2015年2月5日 由 xusong 创建 
 * @author  当前负责人 xusong     
 * @since 项目版本号1.0
 */
public class AutomationInterceptor implements HandlerInterceptor {

    //private static Logger logger = Logger.getLogger(AutomationInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
            final HttpServletResponse response, final Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
            final HttpServletResponse response, final Object o,
            final ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
            final HttpServletResponse response, final Object o, final Exception e)
            throws Exception {
    }
}
