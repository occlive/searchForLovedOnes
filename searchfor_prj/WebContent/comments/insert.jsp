<%@ page contentType="text/html;charset=gb2312"%>
<html>
<head>
	<title>JSP+JDBC ���Թ�����򡪡���½</title>
</head>
<body>
<center>
	<h1>���Թ����� ���� JSP + JDBCʵ��</h1>
	<hr>
	<br>
	<%
		if(session.getAttribute("uname")!=null)
		{
			// �û��ѵ�½
	%>
	<form action="insert_do.jsp" method="post">
		<table>
			<tr>
				<td colspan="2">���������</td>
			</tr>
			<tr>
				<td>���⣺</td>
				<td><input type="text" name="title"></td>
			</tr>
			<tr>
				<td>���ߣ�</td>
				<td><input type="text" name="author"></td>
			</tr>
			<tr>
				<td>���ݣ�</td>
				<td><textarea name="content" cols="30" rows="6"></textarea></td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="���">
					<input type="reset" value="����">
				</td>
			</tr>
		</table>
	</form>
	<h3><a href="list_notes.jsp">�ص������б�ҳ</a></h3>
	<%
		}
		else
		{
			// �û�δ��½����ʾ�û���½������ת
			response.setHeader("refresh","2;URL=login.jsp") ;
	%>
			����δ��½�����ȵ�½������<br>
			������Զ���ת����½���ڣ�����<br>
			���û����ת���밴<a href="login.jsp">����</a>������<br>
	<%
		}
	%>
</center>
</body>
</html>