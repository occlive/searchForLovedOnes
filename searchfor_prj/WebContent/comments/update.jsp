<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.sql.*"%>
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
	<%!
		String DBDRIVER			= "oracle.jdbc.driver.OracleDriver" ;
		String DBURL			= "jdbc:oracle:thin:@localhost:1521:MLDN" ;
		String DBUSER			= "scott" ;
		String DBPASSWORD		= "tiger" ;
		Connection conn			= null ;
		PreparedStatement pstmt	= null ;
		ResultSet rs			= null ;
	%>
	<%
		// ���ղ���
		int id = 0 ;
		try
		{
			id = Integer.parseInt(request.getParameter("id")) ;
		}
		catch(Exception e)
		{}
	%>
	<%
		// ��������ݣ����޸ı���i�����û�У������i��ֵ������������ʾ
		int i = 0 ;
		String sql = "SELECT id,title,author,content FROM note WHERE id=?" ;
		try
		{
			Class.forName(DBDRIVER) ;
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD) ;
			pstmt = conn.prepareStatement(sql) ;
			// ���ò�ѯ����
			pstmt.setInt(1,id) ;
			rs = pstmt.executeQuery() ;
	%>
	<%
			if(rs.next())
			{
				i++ ;
				// ����ѭ����ӡ����ӡ�����е����ݣ��Ա����ʽ
				// �����ݿ���ȡ������
				id = rs.getInt(1) ;
				String title = rs.getString(2) ;
				String author = rs.getString(3) ;
				String content = rs.getString(4) ;
	%>
	<form action="update_do.jsp" method="post">
		<table>
			<tr>
				<td colspan="2">���������</td>
			</tr>
			<tr>
				<td>���⣺</td>
				<td><input type="text" name="title" value="<%=title%>"></td>
			</tr>
			<tr>
				<td>���ߣ�</td>
				<td><input type="text" name="author" value="<%=author%>"></td>
			</tr>
			<tr>
				<td>���ݣ�</td>
				<td><textarea name="content" cols="30" rows="6"><%=content%></textarea></td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="hidden" name="id" value="<%=id%>">
					<input type="submit" value="����">
					<input type="reset" value="����">
				</td>
			</tr>
		</table>
	</form>
	<%
			}
			else
			{
	%>
				û�з��֣�Ҫ���µ����ݣ���<br>
				��ȷ��Ҫ���µ������Ƿ���ڣ���<br>
	<%
			}
	%>
	<%
			rs.close() ;
			pstmt.close() ;
			conn.close() ;
		}
		catch(Exception e)
		{}
	%>
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