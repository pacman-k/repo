<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 05.12.2019
  Time: 21:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>

<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <fmt:message key="layout.news.creation"/>
        <form action="${pageContext.request.contextPath}/" method="POST">
            <input type="hidden" name="commandName" value="${ApplicationConstants.REGISTER_NEWS_CMD}" />
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="news.topic" name="${ApplicationConstants.NEWS_TOPIC_ATTRIBUTE}" maxlength="45" value="${news.newsTopic}">
                        <label class="mdl-textfield__label" for="news.topic"><fmt:message
                                key="layout.news.topic"/></label>
                    </div>
                </div>
                <br>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" id="news.header" name="${ApplicationConstants.NEWS_HEADER_ATTRIBUTE}" maxlength="100" value="${news.newsHeading}">
                        <label class="mdl-textfield__label" for="news.header"><fmt:message
                                key="layout.news.header"/></label>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <input class="mdl-button mdl-js-button mdl-button--raised" type="submit"
                           value="<fmt:message key='layout.news.post'/>">
                </div>
                <c:choose>
                    <c:when test="${not empty error}">
                        <div class="mdl-cell mdl-cell--4-col">
                            <jsp:text>${error}</jsp:text>
                        </div>
                    </c:when>
                </c:choose>
                <div class="mdl-cell mdl-cell--10-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <span><fmt:message key="layout.news.text"/></span>
                            <textarea cols="80" style="resize: none" name="${ApplicationConstants.NEWS_TEXT_ATTRIBUTE}" rows="30" tabindex="3" maxlength="2000" >${news.newsText}</textarea>
                    </div>
                </div>

            </div>
        </form>
    </div>
</div>
