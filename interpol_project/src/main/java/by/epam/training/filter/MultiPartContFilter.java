package by.epam.training.filter;

import by.epam.training.util.MultipartReqParser;
import lombok.extern.log4j.Log4j;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


@Log4j
@WebFilter(servletNames = "app", filterName = " MultiPartContFilter")
public class MultiPartContFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                Map<String, Object> requestMap = MultipartReqParser.parseReq(request);
                request.setAttribute("requestMap", requestMap);
                request.setAttribute("commandName", requestMap.get("commandName"));
            }catch (FileUploadException e){
                log.error("cant parse multi req", e);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
