<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/nav" %>
<%@ taglib prefix="lang" tagdir="/WEB-INF/tags" %>
<c:choose>
    <c:when test="${not empty sessionScope.lang}">
        <fmt:setLocale value="${sessionScope.lang}"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en" scope="session"/>
    </c:otherwise>
</c:choose>
<html>
<meta charset="utf-8"/>
<head>
    <title>Interpole</title>
    <link rel="stylesheet" href="static/style/material.min.css">
    <link rel="stylesheet" href="static/style/icons.css">
    <link rel="stylesheet" href="static/style/ownconfig.css">
    <script defer src="static/style/material.min.js"></script>
</head>
<body>
<fmt:setBundle basename="/pagelang" scope="session"/>
<div class="mdl-layout mdl-js-layout">
    <header class="demo-layout-transparent mdl-layout__header mdl-layout__header--transparent">
        <div align="right"><lang:lang/></div>
        <div class="mdl-layout__header-row">
            <!-- Title -->
            <span class="mdl-layout-title">Interpole</span>
            <!-- Add spacer, to align navigation to the right -->
            <div class="mdl-layout-spacer"></div>
            <!-- Navigation -->
            <nav class="mdl-navigation">
                <c:set var="role" value="${sessionScope.role}"/>
<%--                value='<%=request.getSession().getAttribute("role")%>'--%>
                <c:choose>
                    <c:when test="${role == 'admin'}">
                        <nav:nav-links-admin/>
                    </c:when>
                    <c:when test="${role == 'default'}">
                        <nav:nav-links-user/>
                    </c:when>
                    <c:otherwise>
                        <nav:nav-links-anonym/>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
        <br>
        <br>
    </header>
    <div class="mdl-layout__drawer">
        <span class="mdl-layout-title"><fmt:message key="layout.menu"/></span>
        <nav class="mdl-navigation">
            <c:choose>
            <c:when test="${role == 'admin'}">
                <nav:nav-links-admin_menu/>
            </c:when>
            <c:otherwise>
                <nav:nav-links-us_an/>
            </c:otherwise>
            </c:choose>
    </div>
    <main class="mdl-layout__content">
        <div class="page-content">
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--2-col">
                </div>
                <div class="mdl-cell mdl-cell--8-col">
                    <c:choose>
                        <c:when test="${not empty viewName}">
                            <jsp:include page="views/${viewName}.jsp"/>
                        </c:when>
                        <c:otherwise>
                            <div class="mdl-grid">
                                <div class="mdl-cell mdl-cell--12-col">
                                    <fmt:message key="layout.welcome"/>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="mdl-cell mdl-cell--2-col">
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>