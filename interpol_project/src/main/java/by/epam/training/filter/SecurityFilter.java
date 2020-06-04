package by.epam.training.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@WebFilter(servletNames = "app", filterName = "SecurityFilter")
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("text/html; charset=UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        String lang = request.getParameter("lang");
        if (lang != null) {
            session.setAttribute("lang", lang);
            Map<String, Object> lastReq = (Map<String, Object>) session.getAttribute("reqMap");
            if (lastReq != null) {
                lastReq.forEach(request::setAttribute);
            }
            request.getRequestDispatcher("/jsp/layout.jsp").forward(request, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return;
        }
        Map<String, Object> reqAttributes = new HashMap<>();
        Enumeration<String> keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            reqAttributes.put(key, request.getAttribute(key));
        }
        httpSession.setAttribute("reqMap", reqAttributes);

    }

    @Override
    public void destroy() {

    }
}

