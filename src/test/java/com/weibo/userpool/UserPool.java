package com.weibo.userpool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.weibo.common.Utils;
import com.weibo.global.Constant;
import com.weibo.model.Account;

public class UserPool {
	// 取所有账号，只为清用户使用
	public static List<Account> getAllAccounts(int state, int total)
			throws Exception {
		List<Account> accountsList = new ArrayList();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from account_copy where state="
					+ state);
			rs = ps.executeQuery();
			while (rs.next()) {
				String uid = rs.getString("uid");
				String screen_name = rs.getString("screen_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				int id = rs.getInt("id");
				Account account = new Account();
				account.setEmail(email);
				account.setScreen_name(screen_name);
				account.setId(id);
				account.setPassword(password);
				account.setState(state);
				account.setUid(Long.parseLong(uid));
				accountsList.add(account);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		List retList = Utils.randomAccountList(accountsList, total);
		updateAccountState(retList, Constant.BUSYSTATE);
		return retList;

	}
	
	/**
	 * 
	 * @param state
	 *            :0:free;1:busy
	 * @param total
	 *            ：需要的帐号数目
	 * @return
	 */
	// 只拿普通账号
	public static List<Account> getAccounts(int state, int total)
			throws Exception {
		List<Account> accountsList = new ArrayList();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from account_copy where state="
					+ state + " AND type='ORDINARY' ORDER BY uid DESC");
			rs = ps.executeQuery();
			while (rs.next()) {
				String uid = rs.getString("uid");
				String screen_name = rs.getString("screen_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				int id = rs.getInt("id");
				Account account = new Account();
				account.setEmail(email);
				account.setScreen_name(screen_name);
				account.setId(id);
				account.setPassword(password);
				account.setState(state);
				account.setUid(Long.parseLong(uid));
				accountsList.add(account);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		List retList = Utils.randomAccountList(accountsList, total);
		updateAccountState(retList, Constant.BUSYSTATE);
		return retList;

	}

	/*
	 * 支持不同用户 
	 * type : 
	 * 0 ORDINARY 普通账号 ; 
	 * 1 CONTENT 内容账号 id:2668-2728, 50个;
	 * 2 BLUE 蓝V id:2729-2786, 50个;
	 * 3 ORANGE 橙V id:2787-2844, 50个;
	 * 4 VIP 会员 id:2845-2906, 50个;
	 * 5 TRUST 可信用户 id:2917-2966, 50个
	 * 5 TRUSTC1 可信用户，并且为C1用户, 15个
	 */
	public static List<Account> getAccounts(int state, int total, int type)
			throws Exception {
		List<Account> accountsList = new ArrayList();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			switch (type) {
			case 0: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='ORDINARY'  ORDER BY uid DESC");
				break;
			}
			case 1: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='CONTENT' ORDER BY uid DESC");
				break;
			}
			case 2: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='BLUE'  ORDER BY uid DESC");
				break;
			}
			case 3: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='ORANGE'  ORDER BY uid DESC");
				break;
			}
			case 4: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='VIP'  ORDER BY uid DESC");
				break;
			}
			case 5: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='TRUST'  ORDER BY uid DESC");
				break;
			}
			case 6: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='TRUSTC1'  ORDER BY uid DESC");
				break;
			}
			default: {
				ps = con.prepareStatement("select * from account_copy where state="
						+ state + " AND type='ORDINARY'  ORDER BY uid DESC");
				break;
			}
		}
//			ps = con.prepareStatement("select * from account where state="
//					+ state + " ORDER BY uid DESC");
			rs = ps.executeQuery();
			while (rs.next()) {
				String uid = rs.getString("uid");
				String screen_name = rs.getString("screen_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				int id = rs.getInt("id");
				Account account = new Account();
				account.setEmail(email);
				account.setScreen_name(screen_name);
				account.setId(id);
				account.setPassword(password);
				account.setState(state);
				account.setUid(Long.parseLong(uid));
				accountsList.add(account);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		List retList = Utils.randomAccountList(accountsList, total);
		updateAccountState(retList, Constant.BUSYSTATE);
		return retList;

	}
	
	public static List<Account> getAccountsStrategy(int state, int total)
			throws Exception {
		List<Account> accountsList = new ArrayList();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from account_content_strategy where state="
					+ state + " ORDER BY uid DESC");
			rs = ps.executeQuery();
			while (rs.next()) {
				String uid = rs.getString("uid");
				String screen_name = rs.getString("screen_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String groupID = rs.getString("groupID");
				int id = rs.getInt("id");
				Account account = new Account();
				account.setEmail(email);
				account.setScreen_name(screen_name);
				account.setId(id);
				account.setPassword(password);
				account.setState(state);
				account.setUid(Long.parseLong(uid));
				account.setGroupID(groupID);
				accountsList.add(account);
			}

		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		List retList = Utils.randomAccountList(accountsList, total);
		updateAccountState(retList, Constant.BUSYSTATE);
		return retList;

	}

	/**
	 * 
	 * @param state
	 *            :0:free;1:busy
	 * @return
	 */
	public static Account getAccount(int state) {
		List<Account> accountList = new ArrayList();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from account_copy where state="
					+ state + " limit 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				Account account = new Account();
				String uid = rs.getString("uid");
				String screen_name = rs.getString("screen_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				int id = rs.getInt("id");
				account.setEmail(email);
				account.setScreen_name(screen_name);
				account.setId(id);
				account.setPassword(password);
				account.setState(state);
				account.setUid(Long.parseLong(uid));
				accountList.add(account);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		if (accountList.size() <= 0)
			return null;
		else if (accountList.size() == 1)
			return accountList.get(0);
		double random = Math.random() * accountList.size() + accountList.size()
				- 1;
		Account account = accountList.get(Integer.parseInt(String
				.valueOf(random)));
		updateAccountState(account, Constant.BUSYSTATE);
		return account;
	}

	/**
	 * 
	 * @param uid
	 *            :根据uid查询库中是否存在该用户
	 * @return
	 */
	public static Account getAccount(String uid) {
		Account account = new Account();
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from account_copy where uid=" + uid);
			rs = ps.executeQuery();
			while (rs.next()) {
				String screen_name = rs.getString("screen_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				int id = rs.getInt("id");
				int state = rs.getInt("state");
				account.setEmail(email);
				account.setScreen_name(screen_name);
				account.setId(id);
				account.setPassword(password);
				account.setState(state);
				account.setUid(Long.parseLong(uid));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}

		return account;

	}

	/**
	 * 
	 * @param accountList
	 *            :需要改变状态的用户帐号列表
	 * @param state
	 *            ：需要改变的状态
	 */
	public static void updateAccountState(List<Account> accountList, int state) {
		if (accountList == null)
			return;
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String updatetime = Utils.getCurrentTime();
		try {
			for (Account account : accountList) {
				if (state == Constant.BUSYSTATE)
					ps = con.prepareStatement("update account_copy set state="
							+ state + ",updatetime='" + updatetime
							+ "'  where id=" + account.getId());
				else
					ps = con.prepareStatement("update account_copy set state="
							+ state + ",updatetime=''" + "  where id="
							+ account.getId());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}

	/**
	 * 
	 * @param state
	 *            ：需要改变的状态
	 */
	public static void updateAccountState(Account account, int state) {
		if (account == null)
			return;
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String updatetime = Utils.getCurrentTime();
		try {
			if (state == Constant.BUSYSTATE)
				ps = con.prepareStatement("update account_copy set state=" + state
						+ ",updatetime=" + updatetime + "  where id="
						+ account.getId());
			else
				ps = con.prepareStatement("update account_copy set state=" + state
						+ ",updatetime=''" + "  where id=" + account.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}

	/**
	 * 
	 * @param state
	 *            ：需要改变的状态
	 */
	public static void updateAccountContentStrategyState(List<Account> accountList, int state) {
		if (accountList == null)
			return;
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String updatetime = Utils.getCurrentTime();
		try {
			for (Account account : accountList) {
				if (state == Constant.BUSYSTATE)
					ps = con.prepareStatement("update account_content_strategy set state="
							+ state + ",updatetime='" + updatetime
							+ "'  where id=" + account.getId());
				else
					ps = con.prepareStatement("update account_content_strategy set state="
							+ state + ",updatetime=''" + "  where id="
							+ account.getId());
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
	}
	public static void insertAccounts(List<Account> accountsList) {
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			for (Account account : accountsList) {
				ps = con.prepareStatement("insert account_copy(uid,screen_name,email,password,state) values(?,?,?,?,?)");
				ps.setLong(1, account.getUid());
				ps.setString(2, account.getScreen_name());
				ps.setString(3, account.getEmail());
				ps.setString(4, account.getPassword());
				ps.setInt(5, Constant.FREESTATE);
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

	public static boolean isExistedAccount(String screen_name) throws Exception {
		Connection con = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("select * from account_copy where screen_name='"
					+ screen_name + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} finally {
			JdbcUtil.close(rs, ps, con);
		}
		return false;

	}

}
