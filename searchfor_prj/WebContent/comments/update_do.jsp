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
		// �������봦��
		request.setCharacterEncoding("GB2312") ;
	%>
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
	%>
	<%
		// ����һ��boolean����
		boolean flag = false ;

		// ���ղ���
		String title = request.getParameter("title") ;
		String author = request.getParameter("author") ;
		String content = request.getParameter("content") ;
		int id = 0 ;
		try
		{
			id = Integer.parseInt(request.getParameter("id")) ;
		}
		catch(Exception e)
		{}
	%>
	<%
		// ����note���е�����
		String sql = "UPDATE note set title=?,author=?,content=? WHERE id=?" ;
		try
		{
			Class.forName(DBDRIVER) ;
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD) ;
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setString(1,title) ;
			pstmt.setString(2,author) ;
			pstmt.setString(3,content) ;
			pstmt.setInt(4,id);
			pstmt.executeUpdate() ;
			pstmt.close() ;
			conn.close() ;
			// ����޸ĳɹ�����϶���ִ�е��˶δ���
			flag = true ;
		}
		catch(Exception e)
		{}
	%>
	<%
			response.setHeader("refresh","2;URL=list_notes.jsp") ;
			if(flag)
			{
	%>
				�����޸ĳɹ����������ת�������б�ҳ������<br>
				���û����ת���밴<a href="list_notes.jsp">����</a>������
	<%
			}
			else
			{
	%>
				�����޸�ʧ�ܣ��������ת�������б�ҳ������<br>
				���û����ת���밴<a href="list_notes.jsp">����</a>������
	<%
			}
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