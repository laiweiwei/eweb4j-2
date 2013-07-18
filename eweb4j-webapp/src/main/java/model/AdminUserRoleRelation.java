package model;

import java.util.List;

public class AdminUserRoleRelation {
	
//	public final static AdminUserRoleRelation INSTANCE = new AdminUserRoleRelation();
//
//	public String TableName(){
//		return "qhee_admin_user_role_relations";
//	}
//	
//	private long user_id;
//	
//	private long role_id;
//
//	public static AdminUserRoleRelation findByUserIdAndRoleId(long userId, long roleId){
//		final String sql = "select * from " + INSTANCE.TableName() + " where user_id = ? and role_id = ?";
//		return QueryHelper.read(AdminUserRoleRelation.class, sql, userId, roleId);
//	}
//	public static List<AdminUserRoleRelation> findByUserId(long userId){
//		final String sql = "select * from " + INSTANCE.TableName() + " where user_id = ?";
//		return QueryHelper.query(AdminUserRoleRelation.class, sql, userId);
//	}
//	
//	public static void deleteByUserId(long userId){
//		final String sql = "delete from " + INSTANCE.TableName() + " where user_id = ?";
//		QueryHelper.update(sql, userId);
//	}
//	
//	/**
//	 * 获取给定用户所拥有的所有角色
//	 * @param userId 给定用户ID
//	 * @return
//	 */
//	public static List<AdminRole> getRolesByUserID(long userId) {
//		//select r.* from {ur} ur, {AdminRole} r where ur.role_id = r.id and ur.user_id = ?
//		final String URTable = INSTANCE.TableName();
//		final String RTable = AdminRole.INSTANCE.TableName();
//		final String fmt = "select r.* from %s ur, %s r where ur.role_id = r.id and ur.user_id = ?";
//		final String sql = String.format(fmt, URTable, RTable);
//		return QueryHelper.query(AdminRole.class, sql, userId);
//	}
//	
//	public long getUser_id() {
//		return user_id;
//	}
//	public void setUser_id(long user_id) {
//		this.user_id = user_id;
//	}
//
//	public long getRole_id() {
//		return role_id;
//	}
//
//	public void setRole_id(long role_id) {
//		this.role_id = role_id;
//	}
//	
}
