<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 29.11.2019
  Time: 8:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>
<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <fmt:message key="layout.anonym.cmd.log.in"/>
        <form action="${pageContext.request.contextPath}/" method="POST">
            <input type="hidden" value="${ApplicationConstants.LOGIN_CMD_NAME}" name="commandName" />
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="_user.login" value="${login}" name="${ApplicationConstants.LOGIN_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="_user.login"><fmt:message key="layout.user.login"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="password" id="_user.password" name="${ApplicationConstants.PASSWORD_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="_user.password"><fmt:message key="layout.user.password"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                    <input class="mdl-button mdl-js-button mdl-button--raised" type="submit" value="<fmt:message key='layout.cmd.submit'/>">
                </div>
                <c:choose>
                    <c:when test="${not empty error}">
                        <jsp:text>${error}</jsp:text>
                    </c:when>
                </c:choose>
            </div>
        </form>
    </div>
</div>
