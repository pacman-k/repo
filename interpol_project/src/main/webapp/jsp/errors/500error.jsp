<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 20.12.2019
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <link rel="stylesheet" type="text/css" href="static/style/material.min.css"/>
    <link rel="stylesheet" type="text/css" href="static/style/ownconfig.css">
</head>

<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--10-col">
        <div class="centered-text">
            <h1>
                <fmt:message key="errors.500"/>
            </h1>
            <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/">
                <fmt:message key="layout.home"/>
            </a>
        </div>
    </div>
</div>
