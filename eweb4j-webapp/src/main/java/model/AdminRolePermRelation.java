package model;

import java.util.List;

/**
 * 管理员角色-权限关联
 * @author vivi
 * @date 2013-7-2 下午12:01:16
 */
public class AdminRolePermRelation {

	public final static AdminRolePermRelation INSTANCE = new AdminRolePermRelation();
	
//	@Override
//	public String TableName(){
//		return "qhee_admin_power";
//	}
//	
//	/**
//	 * 角色ID
//	 */
//	private long role_id;
//	
//	/**
//	 * 权限ID
//	 */
//	private long permission_id;
//	
//	public static AdminRolePermRelation findByRoleIdAndPermId(long roleId, long permId){
//		final String sql = "select * from " + INSTANCE.TableName() + " where role_id = ? and permission_id = ?";
//		return QueryHelper.read(AdminRolePermRelation.class, sql, roleId, permId);
//	}
//	public static List<AdminRolePermRelation> findByRoleId(long roleId){
//		final String sql = "select * from " + INSTANCE.TableName() + " where role_id = ?";
//		return QueryHelper.query(AdminRolePermRelation.class, sql, roleId);
//	}
//	
//	public static void deleteByRoleId(long roleId){
//		final String sql = "delete from " + INSTANCE.TableName() + " where role_id = ?";
//		QueryHelper.update(sql, roleId);
//	}
//	
//	/**
//	 * 获取给定角色所拥有的所有权限
//	 * @param roleId 给定角色ID
//	 * @return
//	 */
//	public static List<AdminPermission> getPermsByRoleID(long roleId) {
//		//select p.* from {rp} rp, {AdminPermission} p where rp.perm_id = p.id and rp.role_id = ?
//		final String RPTable = INSTANCE.TableName();
//		final String PTable = AdminPermission.INSTANCE.TableName();
//		final String fmt = "select p.* from %s rp, %s p where rp.perm_id = p.id and rp.role_id = ?";
//		final String sql = String.format(fmt, RPTable, PTable);
//		return QueryHelper.query(AdminPermission.class, sql, roleId);
//	}
//	
//
//	public long getRole_id() {
//		return role_id;
//	}
//
//	public void setRole_id(long role_id) {
//		this.role_id = role_id;
//	}
//
//	public long getPermission_id() {
//		return permission_id;
//	}
//
//	public void setPermission_id(long permission_id) {
//		this.permission_id = permission_id;
//	}
//	
}
