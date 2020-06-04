<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag import="by.epam.training.ApplicationConstants" %>
<!-- Navigation -->
<nav class="mdl-navigation">
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.VIEW_NEWS_CMD_NAME}"><fmt:message key="layout.users.all.cmd.news"/></a>
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.VIEW_CLAIMS_CMD_NAME}"><fmt:message key="layout.users.all.cmd.claims"/></a>
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.VIEW_ALL_USERS_CMD_NAME}"><fmt:message key="layout.users.admin.allusers"/></a>
</nav>