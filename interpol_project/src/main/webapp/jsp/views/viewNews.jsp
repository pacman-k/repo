<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>
<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 05.12.2019
  Time: 20:28
  To change this template use File | Settings | File Templates.
--%>
<div class="mdl-grid">
    <fmt:message key='layout.users.all.cmd.news'/>
    <br>
    <c:set var="role" value='${sessionScope.role}'/>
    <c:if test="${role == 'admin'}">
        <div class="mdl-cell mdl-cell--12-col">
            <form action="${pageContext.request.contextPath}/"
                  method="POST">
                <input type="hidden" name="commandName" value="${ApplicationConstants.ADD_NEWS_VIEW_CMD}"/>
<%--                <%=ApplicationConstants.ADD_NEWS_VIEW_CMD%>--%>
                <input class="demo-button" type="submit" value="<fmt:message key='layout.news.add'/>"/>
            </form>
        </div>
    </c:if>
    <div class="mdl-cell mdl-cell--12-col">
        <c:choose>
            <c:when test="${not empty news}">
                <c:forEach items="${news}" var="n">
                    <span style="font-weight: bolder"><c:out value="${n.newsHeading}"/></span>
                    <textarea class="note" disabled>
                          <c:out value="${n.newsText}"/>
                      </textarea>
                    <div class="mdl-cell mdl-cell--4-col">
                        <c:out value="${n.dateOfPost}"/>
                    </div>
                    <c:if test="${role == 'admin'}">
            <div class="mdl-grid">
                <div class="mdl-cell mdl-cell--4-col">
                        <form action="${pageContext.request.contextPath}/" method="POST">
                            <input type="hidden" name="newsId" value="${n.newsId}"/>
                            <input type="hidden" name="commandName" value="editNewsView"/>
                            <input class="mdl-button mdl-js-button mdl-button--raised" type="submit"
                                   value="<fmt:message key='layout.working.cmd.edit'/>"/>
                        </form>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                        <form action="${pageContext.request.contextPath}/" method="POST">
                            <input type="hidden" name="newsId" value="${n.newsId}"/>
                            <input type="hidden" name="commandName" value="deleteNews"/>
                            <input class="mdl-button mdl-js-button mdl-button--raised" type="submit"
                                   value="<fmt:message key='layout.working.cmd.delete'/>"/>
                        </form>
                </div>
            </div>
                    </c:if>
                    <br>
                    <br>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <fmt:message key="layout.news.no"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
