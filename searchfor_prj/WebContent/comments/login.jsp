<%@ page contentType="text/html;charset=gb2312"%>
<html>
<head>
	<title>JSP+JDBC ���Թ�����򡪡���½</title>
</head>
<body>
<center>
	<h1>���Թ���</h1>
	<hr>
	<br>
	<%
		// �ж��Ƿ��д�����Ϣ����������ӡ
		// ���û�д˶δ��룬����ʾʱ��ֱ�Ӵ�ӡnull
		if(request.getAttribute("err")!=null)
		{
	%>
			<h2><%=request.getAttribute("err")%></h2>
	<%
		}
	%>
	<form action="login_conf.jsp" method="post">
	<table width="80%">
	<tr>
		<td colspan="2">�û���½</td>
	</tr>
	<tr>
		<td>�û�����</td>
		<td><input type="text" name="id"></td>
	</tr>
	<tr>
		<td>��&nbsp;&nbsp;�룺</td>
		<td><input type="password" name="password"></td>
	</tr>
	<tr>
		<td colspan="2">
			<input type="submit" value="��½">
			<input type="reset" value="����">
		</td>
	</tr>
	</table>
	</form>
</center>
</body>
</html>