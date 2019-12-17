package search.user.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import search.user.service.UserService;
import search.util.AliyunSmsUtils;

/**
 * Servlet implementation class PwdChangedWebServlet
 */
@WebServlet("/PwdChangedWebServlet")
public class PwdChangedWebServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PwdChangedWebServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//获取用户输入手机号、验证码和密码
		String phonenum = request.getParameter("phone");
		
	
		//先判断手机号是否存在于数据库中，若存在，则可以发送验证码，若不存在，提示用户电话不存在
		UserService userService = new UserService();
		boolean type = userService.judgeUserTelService(phonenum);
		
		System.out.println(type);
		if(type == true) {//当返回值为true,代表数据库中存在该号码，可以发送验证码
			System.out.println();
			//获取短信验证码工具类
			AliyunSmsUtils utils = new AliyunSmsUtils();
		    utils.setNewcode();
		    String sendCode = Integer.toString(utils.getNewcode());//得到要发送的验证码
		    System.out.println("要发送的验证码为："+sendCode);
		    
		    //发送短信
			try {
				SendSmsResponse res = utils.sendSms(phonenum, sendCode);//要发送的电话和验证码
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {//返回值为false,数据库中该手机号码不存在，无法发送验证码
			response.getWriter().print("<script language='javascript'>alert('该手机号还未注册，请先注册！')</script>");
			response.setHeader("refresh", "1,URL=signup.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
