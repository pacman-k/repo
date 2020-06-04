<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 14.12.2019
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>
<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <fmt:message key="layout.claims.create"/>
        <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/" method="POST">
            <input type="hidden" value="${ApplicationConstants.CREATE_CLAIM_CMD_NAME}" name="commandName"/>
            <input type="hidden" value="${sessionScope.userId}" name="userId"/>
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="pers.firstName" required value="${pers.firstName}"
                               name="${ApplicationConstants.FIRST_NAME_ATTRIBUTE}" >
                        <label class="mdl-textfield__label" for="pers.firstName"><fmt:message key="layout.user.firstName"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="pers.lastName" required value="${pers.lastName}"
                               name="${ApplicationConstants.LAST_NAME_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="pers.lastName"><fmt:message key="layout.user.lastName"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--10-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="pers.country" required value="${pers.country}"
                               name="${ApplicationConstants.COUNTRY_ATTRIBUTE}">
                        <label class="mdl-textfield__label" for="pers.country"><fmt:message key="layout.user.country"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--10-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <fmt:message key="layout.user.birthday"/>: <input class="mdl-textfield__input" type="date" required
                               name="${ApplicationConstants.BIRTHDAY_ATTRIBUTE}">
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--10-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <fmt:message key="layout.user.photo"/>: <input class="is-small-screen" accept="image/jpeg" type="file"
                               name="${ApplicationConstants.PHOTO_ATTRIBUTE}">
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--12-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <span><fmt:message key="layout.user.description"/>:</span>
                        <div aria-required="true">
                        <textarea cols="40" style="resize: none" name="${ApplicationConstants.DESCRIPTION_ATTRIBUTE}" required tabindex="3" rows="10"
                                  maxlength="1000">${pers.description}
                        </textarea>
                        </div>
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
