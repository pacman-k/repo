<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 19.12.2019
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>
<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <fmt:message key="layout.news.editor"/>
        <div class="mdl-cell mdl-cell--2-col">
            <form action="${pageContext.request.contextPath}/" method="POST">
                <h6>${oldUser.login}</h6>
                <div class="mdl-textfield mdl-js-textfield">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="firstName" name="${ApplicationConstants.FIRST_NAME_ATTRIBUTE}"
                               value="${oldUser.firstName}"
                               pattern="[A-Za-zА-Яа-яЁё]{1,100}" required>
                        <label class="mdl-textfield__label" for="firstName"><fmt:message
                                key="layout.user.firstName"/></label>
                    </div>
                </div>
                <div class="mdl-textfield mdl-js-textfield">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="lastName" name="${ApplicationConstants.LAST_NAME_ATTRIBUTE}"
                               value="${oldUser.lastName}"
                               pattern="[A-Za-zА-Яа-яЁё]{1,100}" required>
                        <label class="mdl-textfield__label" for="lastName"><fmt:message
                                key="layout.user.lastName"/></label>
                    </div>
                </div>
                <div class="mdl-textfield mdl-js-textfield">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="email" name="${ApplicationConstants.EMAIL_ATTRIBUTE}"
                               value="${oldUser.email}"
                               pattern="(^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$)" required>
                        <label class="mdl-textfield__label" for="email"><fmt:message key="layout.user.email"/></label>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty error}">
                        <div class="mdl-cell mdl-cell--12-col">
                            <jsp:text>${error}</jsp:text>
                        </div>
                    </c:when>
                </c:choose>
                <input type="hidden" name="id" value="${oldUser.id}"/>
                <input type="hidden" name="commandName" value="editUser"/>
                <input class="mdl-button mdl-js-button" type="submit"
                       value="<fmt:message key='layout.working.cmd.edit'/>"/>
            </form>

        </div>
    </div>
</div>