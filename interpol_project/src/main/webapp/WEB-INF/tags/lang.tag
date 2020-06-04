<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<nav>
    <span>
        <a href="${pageContext.request.contextPath}/?lang=en&viewName=${viewName}" style="text-decoration: none">
            <img src="http://2d.by/wallpapers/f/flag_velikobritanii.jpg" alt="<fmt:message key="links.lang.en"/>" style="width: 20px; height: 15px">
        </a>
            </span>
    <span>
        <a href="${pageContext.request.contextPath}/?lang=ru&viewName=${viewName}" style="text-decoration: none">
             <img src="https://www.roi.ru/tmp/attachments/593855/eto-nyneshnii-flag-rossiiskoi-federatcii1514276527.jpg" alt="<fmt:message key="links.lang.ru"/>" style="width: 20px; height: 15px">
        </a>
    </span>
</nav>
