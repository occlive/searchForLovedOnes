package search.hall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import search.entity.Basic_information;
import search.entity.User;
import search.util.DBUtil;

public class HallDao {
	//模糊查询然后返回一个List，其中里面包含这id，图片（图片地址），名字
	/**
	 * 根据id进行搜索
	 * @param type
	 * @return return 一个list
	 */
	public List<Basic_information> findBasicByID(int type) {
		String sqls=" ";
		String table_name;
		String middle=type+"";
		int first=Integer.parseInt(middle.substring(0,1));
		 
		Connection con = null;
		PreparedStatement pstm = null;
		List<Basic_information> basics = new ArrayList<>();
		//根据type选择表名
		if(first==1)
			sqls="select * from search_home where id like ?";
		else if(first==2)
			sqls="select * from search_person where id like ?";
		else if(first==3)
			sqls="select * from search_vagrancy where id like ?";
		else if(first==4)
			sqls="select * from other_search where id like ?";
		
		try {
			String sql="select * from ? where id ? ?";
			con = DBUtil.getCon();
			
			String typelenth=type+"";
			int length=typelenth.length();
			System.out.println("获取到的字符串长度是"+length);
//			int ok=new Integer(type);
//			String sql = "select * from search_home";
			
			pstm = con.prepareStatement(sqls);
//			pstm.setInt(1, type);
			
			//判断一下type的长度（最小为1，最大为4）,如果长度为1或小于4的话（暂定id最大长度为4）的话说明是单选框事件，应该用模糊查询
			//如果长度为4（暂定id最大长度为4）的话，就采用精确查询
			 
				//符合长度要求可以检索
				if(length<4&&length>0) {//启用模糊查询
					System.out.println("启用模糊查询");
					pstm.setString(1, type+"%");
				}
				else if(4==length){//启用精确查询
					System.out.println("启用精确查询");
					pstm.setInt(1, type);
				}
			
			
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				String basic_id = rs.getString("id");
				String basic_name = rs.getString("name");
				String basic_sex = rs.getString("gender");//性别QAQ
				String basic_photo=rs.getString("photo");
				
				
				Basic_information basic=new Basic_information();
				basic.setId(basic_id);
				basic.setName(basic_name);
				basic.setSex(basic_sex);
				basic.setPhoto(basic_photo);
				basics.add(basic);//将从数据库中查找的所有用户信息放进users列表中
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(con);
		}
		return basics;
		
	}
	/**
	 * 根据人名进行搜索
	 * @param name
	 * @return return 一个数组
	 */
	public List<Basic_information> findBasicByName(String name) {

		 
		Connection con = null;
		PreparedStatement pstm = null;
		List<Basic_information> basics = new ArrayList<>();
	
		try {
			String sql="select * from ? where id ? ?";
			con = DBUtil.getCon();
			
			

			
			pstm = con.prepareStatement(sql);
			
			
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				String basic_id = rs.getString("id");
				String basic_name = rs.getString("name");
				String basic_sex = rs.getString("gender");//性别QAQ
				String basic_photo=rs.getString("photo");
				
				
				Basic_information basic=new Basic_information();
				basic.setId(basic_id);
				basic.setName(basic_name);
				basic.setSex(basic_sex);
				basic.setPhoto(basic_photo);
				basics.add(basic);//将从数据库中查找的所有用户信息放进users列表中
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(con);
		}
		return basics;
		
	}
}