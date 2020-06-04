<%--
  Created by IntelliJ IDEA.
  User: Dany
  Date: 15.12.2019
  Time: 6:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="by.epam.training.ApplicationConstants" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="mdl-grid">
    <div class="mdl-layout-spacer"></div>
    <fmt:message key="layout.claims.status.${type}"/>
    <c:set var="role" value='${sessionScope.role}'/>
    <c:if test="${role == 'admin'}">
    <div class="mdl-cell mdl-cell--12-col">
        <form action="${pageContext.request.contextPath}/"
              method="POST">
            <input type="hidden" name="commandName" value="${ApplicationConstants.CREATE_CLAIM_VIEW_CMD_NAME}"/>
            <input class="demo-button" type="submit" value="<fmt:message key='layout.claims.add'/>"/>
        </form>
    </div>
    <div align="right" class="mdl-cell mdl-cell--12-col">
        <c:choose>
            <c:when test="${type == 'actual'}">
                <a href="${pageContext.request.contextPath}/?commandName=viewUnderConsClaims"><fmt:message
                        key="layout.claims.status.prep"/>
                </a>
            </c:when>
            <c:when test="${type == 'prep'}">
                <a href="${pageContext.request.contextPath}/?commandName=viewClaims"><fmt:message
                        key="layout.claims.status.actual"/>
                </a>
                <c:set value="${true}" var="prep"/>
            </c:when>
        </c:choose>
        </form>
    </div>
    </c:if>
    <div class="mdl-cell mdl-cell--12-col">
        <c:choose>
            <c:when test="${not empty persons}">
                <div class="mdl-grid" style="column-count: 4">
                    <c:forEach items="${persons}" var="p">
                        <div class="mdl-cell--4-col" style="border: 1px solid black;padding: 0 2px">
                            <figure>
                                <c:choose>
                                    <c:when test="${p.hasPhoto()}">
                                        <img src="data:image/jpg;base64,${p.getBase64String()}"
                                             width="170" height="200">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="http://pluspng.com/img-png/logo-anonymous-png-file-logo-anonymous-png-1024.png"
                                             width="200">
                                    </c:otherwise>
                                </c:choose>
                            </figure>
                            <div>
                                <form action="${pageContext.request.contextPath}/" method="POST">
                                    <input type="hidden" name="commandName" value="viewClaimDetails"/>
                                    <input type="hidden" name="persId" value="${p.id}"/>
                                    <input class="mdl-icon-toggle__input" type="submit" value="${p.firstName}"/>
                                </form>
                                <c:if test="${prep}">
                                    <table>
                                        <tr>
                                    <form action="${pageContext.request.contextPath}/" method="POST">
                                        <input type="hidden" name="personId" value="${p.id}"/>
                                        <input type="hidden" name="commandName" value="deleteClaim"/>
                                        <input type="hidden" name="viewName" value="viewUnderConsClaims"/>
                                        <input class="mdl-button mdl-js-button" type="submit"
                                               value="<fmt:message key='layout.working.cmd.delete'/>"/>
                                    </form>
                                        </tr>
                                        <tr>
                                    <form action="${pageContext.request.contextPath}/" method="POST">
                                        <input type="hidden" name="personId" value="${p.id}"/>
                                        <input type="hidden" name="commandName" value="confirmClaim"/>
                                        <input type="hidden" name="viewName" value="viewUnderConsClaims"/>
                                        <input class="mdl-button mdl-js-button" type="submit"
                                               value="<fmt:message key='layout.working.cmd.confirm'/>"/>
                                    </form>
                                        </tr>
                                    </table>
                                </c:if>
                                <c:out value="${p.country}"/>
                                <br>
                                <c:out value="${p.getBirthdayStr()}"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
               <fmt:message key="layout.claims.no"/>
            </c:otherwise>
        </c:choose>
    </div>