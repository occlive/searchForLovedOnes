<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
    <%@include file="/layout/header.jsp"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:include page="layout/right.jsp"></jsp:include>
	<div border="1dx">
		<h4>姓名+籍贯</h4>
		左：照片<div><img src="" /></div>
		右：信息：
		<div>
			被寻人姓名：<c:out value=""></c:out>
			失散时身高：
			出生日期
			性别：
			籍贯：
			失散日期：
			寻人类别：
		</div>
	</div>

<%@include file="/layout/footer.jsp"  %>
</body>
</html>