package com.weibo.userpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.weibo.common.Utils;
import com.weibo.global.Constant;
import com.weibo.model.Account;
import com.weibo.model.ObjectDB;

public class ObjectPool {
	/*
	 * @param type
	 * 
	 * @param state
	 * 
	 * @param total
	 */
	public static List<ObjectDB> getObjects(String type, int state, int total)
			throws Exception {
		List<ObjectDB> objectList = new ArrayList<ObjectDB>();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from objectpool where state="+ state + " and object_type='" + type+"'");
			rs = ps.executeQuery();
			while (rs.next() && total > 0) {
				String object_id = rs.getString("object_id");
				String object_type = rs.getString("object_type");
				ObjectDB object = new ObjectDB();
				object.setObject_id(object_id);
				object.setObject_type(object_type);
				objectList.add(object);
				total--;
			}
			if (total > 0)
				throw new Exception("too few objects to assign!");
			updateObjectStates(objectList,Constant.BUSYSTATE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		return objectList;
	}

	public static ObjectDB getObject(String type, int state) throws Exception {
		ObjectDB object = new ObjectDB();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from objectpool where object_type='"+type+"' and state="+ state );
			rs = ps.executeQuery();
			while (rs.next()) {
				String object_id = rs.getString("object_id");
				String object_type = rs.getString("object_type");
				object.setObject_id(object_id);
				object.setObject_type(object_type);
			}
			updateObjectState(object,Constant.BUSYSTATE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		return object;
	}
	
	public static void updateObjectStates(List<ObjectDB> objectList,int state){
		if (objectList == null)
			return;
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
/*		 try {
			ps = con.prepareStatement("update objectpool set state="+ state);
			ps.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			JdbcUtil.close(rs, ps, con);
		}
	     */
		try {
			for (ObjectDB object : objectList) {
			    ps = con.prepareStatement("update objectpool set state="+ state + " where object_id='" + object.getObject_id()+"' and object_type='"+object.getObject_type()+"'");
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		
	}
    public static void updateObjectState(ObjectDB object, int state){
    	if (object == null)
			return;
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update objectpool set state=" + state
						 + "  where object_id='" + object.getObject_id()+"'");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}
    
    public static void updateObjectState(){
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update objectpool set state=0 where state=1");
			System.out.println("updateObjectState---------");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}
	public static void insertObjects(List<ObjectDB> objectList) {
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			for (ObjectDB object : objectList) {
				ps = con.prepareStatement("insert objectpool(object_id,object_type,state) values(?,?,?)");
				ps.setString(1, object.getObject_id());
				ps.setString(2, object.getObject_type());
				ps.setInt(3, Constant.FREESTATE);
				ps.executeUpdate();
				count++;

				if (count == 100) {
					ps.executeBatch();
					count = 0;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}
	
	public static void deleteObjects(List<ObjectDB> objectList) {
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			for (ObjectDB object : objectList) {
				ps = con.prepareStatement("delete from objectpool where object_id='"+ object.getObject_id() +"' and object_type='"+object.getObject_type()+"'");
				ps.executeUpdate();
				count++;

				if (count == 100) {
					ps.executeBatch();
					count = 0;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}
}
