<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag import="by.epam.training.ApplicationConstants" %>
<!-- Navigation -->
<nav class="mdl-navigation">
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.REGISTER_VIEW_CMD_NAME}"><fmt:message key="layout.anonym.cmd.reg"/></a>
    <a class="mdl-navigation__link" href="?${ApplicationConstants.CMD_REQ_PARAMETER}=${ApplicationConstants.LOGIN_VIEW_CMD_NAME}"><fmt:message key="layout.anonym.cmd.log.in"/></a>
    <a class="mdl-navigation__link" href="${pageContext.request.contextPath}/"><fmt:message key="layout.home"/></a>
</nav>