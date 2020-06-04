<%@ page import="by.epam.training.ApplicationConstants" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="mdl-grid">
    <div class="mdl-cell mdl-cell--12-col">
        <c:choose>
            <c:when test="${not empty users}">
                <table class="mdl-data-table mdl-js-data-table">
                    <thead>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric"><fmt:message key="layout.user.firstName"/></th>
                        <th class="mdl-data-table__cell--non-numeric"><fmt:message key="layout.user.lastName"/></th>
                        <th class="mdl-data-table__cell--non-numeric"><fmt:message key="layout.user.email"/></th>
                        <th class="mdl-data-table__cell--non-numeric"><fmt:message key="layout.user.login"/></th>
                        <th class="mdl-data-table__cell--non-numeric" colspan=2><fmt:message
                                key="layout.cmd.actions"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${users}" var="u">
                            <td class="mdl-data-table__cell--non-numeric">${u.firstName}</td>
                            <td class="mdl-data-table__cell--non-numeric">${u.lastName}</td>
                            <td class="mdl-data-table__cell--non-numeric">${u.email}</td>
                            <td class="mdl-data-table__cell--non-numeric">${u.login}</td>

                            <c:if test="${u.roleDto != 'admin'}">
                            <td class="mdl-data-table__cell--non-numeric">
                                <form action="${pageContext.request.contextPath}/" method="POST">
                                    <input type="hidden" name="userId" value="${u.id}"/>
                                    <input type="hidden" name="viewName" value="viewAllUsers"/>
                                    <c:choose>
                                        <c:when test="${u.roleDto == 'locked'}">
                                            <input type="hidden" name="commandName" value="unlockUser"/>
                                            <input class="mdl-button mdl-js-button" type="submit"
                                                   value="<fmt:message key='layout.working.cmd.unlock'/>"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="commandName" value="lockUser"/>
                                            <input class="mdl-button mdl-js-button" type="submit"
                                                   value="<fmt:message key='layout.working.cmd.lock'/>"/>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </td>
                            <td class="mdl-data-table__cell--non-numeric">
                                <form action="${pageContext.request.contextPath}/" method="POST">
                                    <input type="hidden" name="userId" value="${u.id}"/>
                                    <input type="hidden" name="viewName" value="viewAllUsers"/>
                                    <input type="hidden" name="commandName" value="deleteUser"/>
                                    <input class="mdl-button mdl-js-button" type="submit"
                                           value="<fmt:message key='layout.working.cmd.delete'/>"/>
                                </form>
                            </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                no data available
            </c:otherwise>
        </c:choose>

    </div>
</div>