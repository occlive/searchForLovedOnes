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
		// ����ת��
		request.setCharacterEncoding("GB2312") ;
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
		// ��������ݣ����޸ı���i�����û�У������i��ֵ������������ʾ
		int i = 0 ;
		String sql = null; 
		String keyword = request.getParameter("keyword") ;
		// out.println(keyword) ;
		if(keyword==null)
		{
			// û���κβ�ѯ����
			sql = "SELECT id,title,author,content FROM note" ;
		}
		else
		{
			// �в�ѯ����
			sql = "SELECT id,title,author,content FROM note WHERE title like ? or author like ? or content like ?" ;
		}
		
		try
		{
			Class.forName(DBDRIVER) ;
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD) ;
			pstmt = conn.prepareStatement(sql) ;

			// ������ڲ�ѯ���ݣ�����Ҫ���ò�ѯ����
			if(keyword!=null)
			{
				// ���ڲ�ѯ����
				pstmt.setString(1,"%"+keyword+"%") ;
				pstmt.setString(2,"%"+keyword+"%") ;
				pstmt.setString(3,"%"+keyword+"%") ;
			}

			rs = pstmt.executeQuery() ;
	%>
<form action="list_notes.jsp" method="POST">
	�������ѯ���ݣ�<input type="text" name="keyword">
	<input type="submit" value="��ѯ">
</form>
</h3><a href="insert.jsp">���������</a></h3>
<table width="80%" border="1">
	<tr>
		<td>����ID</td>
		<td>����</td>
		<td>����</td>
		<td>����</td>
		<td>ɾ��</td>
	</tr>
	<%
			while(rs.next())
			{
				i++ ;
				// ����ѭ����ӡ����ӡ�����е����ݣ��Ա����ʽ
				// �����ݿ���ȡ������
				int id = rs.getInt(1) ;
				String title = rs.getString(2) ;
				String author = rs.getString(3) ;
				String content = rs.getString(4) ;

				if(keyword!=null)
				{
					// ��Ҫ�����ݷ���
					title = title.replaceAll(keyword,"<font color=\"red\">"+keyword+"</font>") ;
					author = author.replaceAll(keyword,"<font color=\"red\">"+keyword+"</font>") ;
					content = content.replaceAll(keyword,"<font color=\"red\">"+keyword+"</font>") ;
				}
	%>
				<tr>
					<td><%=id%></td>
					<td><a href="update.jsp?id=<%=id%>"><%=title%></a></td>
					<td><%=author%></td>
					<td><%=content%></td>
					<td><a href="delete_do.jsp?id=<%=id%>">ɾ��</a></td>
				</tr>
	<%
			}
			// �ж�i��ֵ�Ƿ�ı䣬����ı䣬���ʾ�����ݣ���֮��������
			if(i==0)
			{
				// ������ʾ
	%>
				<tr>
					<td colspan="5">û���κ����ݣ�����</td>
				</tr>
	<%
			}
	%>
</table>
	<%
			rs.close() ;
			pstmt.close() ;
			conn.close() ;
		}
		catch(Exception e)
		{}
	%>
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