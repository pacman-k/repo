<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag import="by.epam.training.ApplicationConstants" %>
<!-- Navigation -->
<nav class="mdl-navigation">
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.CREATE_CLAIM_VIEW_CMD_NAME}"><fmt:message key="layout.users.default.createclaim"/> </a>
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.PROFILE_VIEW_CMD_NAME}"><fmt:message key="layout.users.ad_def.cmd.profile"/></a>
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.LOGOUT_USER_CMD_NAME}"><fmt:message key="layout.users.ad_def.cmd.log.out"/></a>
    <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/"><fmt:message key="layout.home"/></a>
</nav>