package by.epam.training.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MultipartReqParser {
    public static Map<String, Object> parseReq(HttpServletRequest request) throws FileUploadException {
        Map<String, Object> resultMap = new HashMap<>();
        DiskFileItemFactory disk = new DiskFileItemFactory();
        ServletContext servletContext = request.getServletContext();
        File rep = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

        disk.setRepository(rep);
        ServletFileUpload upload = new ServletFileUpload(disk);
        List<FileItem> items = upload.parseRequest(request);

        Iterator<FileItem> iterator = items.iterator();
        while (iterator.hasNext()){
            FileItem item = iterator.next();
            if (item.isFormField()){
                resultMap.put(item.getFieldName(), item.getString());
            } else {
                resultMap.put(item.getFieldName(),item.get());
            }
        }
        return resultMap;
    }
}
