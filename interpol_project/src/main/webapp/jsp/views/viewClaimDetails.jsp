<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="by.epam.training.ApplicationConstants" %>

<style type="text/css">
    * {
        margin: 0;
        padding: 0;
    }

    p {
        padding: 10px
    }

    #left {
        position: absolute;
        left: 18%;
        top: 0;
        width: 50%;
    }

    #right {
        position: absolute;
        right: 0;
        top: 0;
        width: 50%;
    }
</style>
<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <div class="mdl-grid">
            <div id="left" class="mdl-cell mdl-cell--4-col">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <figure>
                        <c:choose>
                            <c:when test="${pers.hasPhoto()}">
                                <img src="data:image/jpg;base64,${pers.getBase64String()}"
                                     width="200">
                            </c:when>
                            <c:otherwise>
                                <img src="http://pluspng.com/img-png/logo-anonymous-png-file-logo-anonymous-png-1024.png"
                                     width="200">
                            </c:otherwise>
                        </c:choose>
                    </figure>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.claims.dateofpost"/></strong> :
                                <c:out value="${claim.dateOfClaim}"/></span>
                    </div>
                </div>
                <c:if test="${(sessionScope.userId eq customerId) or (sessionScope.role eq 'admin')}">
                    <div class="mdl-button--primary">
                        <form action="${pageContext.request.contextPath}/" method="POST">
                            <input type="hidden" name="personId" value="${pers.id}"/>
                            <input type="hidden" name="viewName" value="viewClaims"/>
                            <input type="hidden" name="commandName" value="deleteClaim"/>
                            <input class="mdl-button mdl-js-button mdl-button--primary" type="submit"
                                   value="<fmt:message key='layout.working.cmd.delete'/>"/>
                        </form>
                    </div>
                </c:if>
                <c:if test="${(claim.claimStatus eq 'under_consideration') and (sessionScope.role eq 'admin')}">
                    <div class="mdl-button--primary">
                        <form action="${pageContext.request.contextPath}/" method="POST">
                            <input type="hidden" name="personId" value="${p.id}"/>
                            <input type="hidden" name="commandName" value="confirmClaim"/>
                            <input type="hidden" name="viewName" value="${ApplicationConstants.VIEW_CLAIMS_CMD_NAME}"/>
                            <input class="mdl-button mdl-js-button" type="submit"
                                   value="<fmt:message key='layout.working.cmd.confirm'/>"/>
                        </form>
                        <form action="${pageContext.request.contextPath}/" method="POST">
                            <input type="hidden" name="personId" value="${p.id}"/>
                            <input type="hidden" name="commandName" value="confirmClaim"/>
                            <input type="hidden" name="viewName" value="viewUnderConsClaims"/>
                            <input class="mdl-button mdl-js-button" type="submit"
                                   value="<fmt:message key='layout.working.cmd.confirm'/>"/>
                        </form>
                    </div>
                </c:if>
            </div>
            <div id="right">
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.firstName"/></strong> : <c:out
                                    value="${pers.firstName}"/></span>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.lastName"/></strong> : <c:out
                                    value="${pers.lastName}"/></span>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.country"/></strong> : <c:out
                                    value="${pers.country}"/></span>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.birthday"/></strong>:
                                <br>
                                <c:out value="${pers.getBirthdayStr()}"/></span>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--4-col">
                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <fmt:message key="layout.user.description"/>:
                        <textarea class="description" disabled>
                          <c:out value="${pers.description}"/>
                      </textarea>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>