<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>
<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <fmt:message key="layout.anonym.cmd.reg"/>
        <form action="${pageContext.request.contextPath}/" method="POST">
            <input type="hidden" value="${ApplicationConstants.REGISTER_SAVE_CMD_NAME}" name="commandName"/>
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="_user.firstName" required pattern="[A-Za-zА-Яа-яЁё]{1,100}" value="${user.firstName}"
                               name="${ApplicationConstants.FIRST_NAME_ATTRIBUTE}" >
                        <label class="mdl-textfield__label" for="_user.firstName"><fmt:message key="layout.user.firstName"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="_user.lastName" required pattern="[A-Za-zА-Яа-яЁё]{1,100}" value="${user.lastName}"
                               name="${ApplicationConstants.LAST_NAME_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="_user.lastName"><fmt:message key="layout.user.lastName"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="_user.email" pattern="(^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$)" required
                               value="${user.email}" name="${ApplicationConstants.EMAIL_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="_user.email"><fmt:message key="layout.user.email"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="_user.login" required value="${user.login}"
                               name="${ApplicationConstants.LOGIN_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="_user.login"><fmt:message key="layout.user.login"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="password" id="_user.password" pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required
                               name="${ApplicationConstants.PASSWORD_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="_user.password"><fmt:message key="layout.user.password"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                    <input class="mdl-button mdl-js-button mdl-button--raised" type="submit" value="<fmt:message key='layout.cmd.save'/>">
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