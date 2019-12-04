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
		// ����һ��boolean���������ڱ����û��Ƿ�Ϸ���״̬
		boolean flag = false ;

		// ���ղ���
		String id = request.getParameter("id") ;
		String password = request.getParameter("password") ;
	%>
	<%
		String sql = "SELECT name FROM person WHERE id=? and password=?" ;
		try
		{
			Class.forName(DBDRIVER) ;
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD) ;
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setString(1,id) ;
			pstmt.setString(2,password) ;
			rs = pstmt.executeQuery() ;
			if(rs.next())
			{
				// �û��Ϸ�
				flag = true ;
				// ���û���������session֮��
				session.setAttribute("uname",rs.getString(1)) ;
			}
			else
			{
				// ���������Ϣ
				request.setAttribute("err","������û��������룡����") ;
			}
			rs.close() ;
			pstmt.close() ;
			conn.close() ;
		}
		catch(Exception e)
		{}
	%>
	<%
		// ��ת
		if(flag)
		{
			// �û��Ϸ�
	%>
			<jsp:forward page="login_success.jsp"/>
	<%
		}
		else
		{
			// �û��Ƿ�
	%>
			<jsp:forward page="login.jsp"/>
	<%
		}
	%>
</center>
</body>
</html>