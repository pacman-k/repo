<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <span style="margin-left: 30%"><fmt:message key="layout.user.profile"/></span>
    <div class="mdl-cell mdl-cell--12-col">
        <div class="mdl-grid">
            <div id="left" class="mdl-cell mdl-cell--4-col">
                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <img src="https://shmector.com/_ph/4/647817301.png" width="250">
                </div>
                <div id="right">
                    <div class="mdl-cell mdl-cell--4-col" style="margin-left: 150%">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <c:if test="${sessionScope.role eq admin}">
                            <span><c:out value="${sessionScope.role}"/></span>
                            </c:if>
                        </div>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.firstName"/></strong> : <c:out
                                    value="${user.firstName}"/></span>
                        </div>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.lastName"/></strong> : <c:out
                                    value="${user.lastName}"/></span>
                        </div>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.login"/></strong> : <c:out
                                    value="${user.login}"/></span>
                        </div>
                    </div>
                    <div class="mdl-cell mdl-cell--4-col">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <span><strong><fmt:message key="layout.user.email"/></strong> : <c:out
                                    value="${user.email}"/></span>
                        </div>
                    </div>
                </div>
                <c:if test="${sessionScope.userId eq user.id}">
                    <table class="mdl-data-table mdl-js-data-table">
                        <thead>
                        <tr>
                            <td>
                                <form action="${pageContext.request.contextPath}/" method="POST">
                                    <input type="hidden" name="userId" value="${user.id}"/>
                                    <input type="hidden" name="commandName" value="deleteUser"/>
                                    <input class="mdl-button mdl-js-button mdl-button--primary" type="submit"
                                           value="<fmt:message key='layout.working.cmd.delete'/>"/>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/" method="POST">
                                    <input type="hidden" name="userId" value="${user.id}"/>
                                    <input type="hidden" name="commandName" value="editUserView"/>
                                    <input class="mdl-button mdl-js-button mdl-button--primary" type="submit"
                                           value="<fmt:message key='layout.working.cmd.edit'/>"/>
                                </form>
                            </td>
                        </tr>
                        </thead>
                    </table>
                </c:if>
            </div>
        </div>
    </div>
</div>
