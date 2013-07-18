package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 管理员角色模型
 * @author vivi
 * @date 2013-7-2 上午11:01:13
 */
public class AdminRole {
	
//	public final static AdminRole INSTANCE = new AdminRole();
//	public final static Class<AdminRole> CLASS = AdminRole.class;
//	
//
//	@Override
//	public String TableName() {
//		return "qhee_admin_roles";
//	}
//
//	/**
//	 * 角色名
//	 */
//	private String name;
//
//	/**
//	 * 添加者对应的user_id
//	 */
//	private long creator_id;
//	
//	/**
//	 * 添加日期
//	 */
//	private String create_at;
//	
//	private List<Long> permIds = new ArrayList<Long>();
//
//	public static List<AdminRole> findAll(String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = "name like '%" + keyword + "%'";
//			} else {
//				//查询指定字段
//				condition =  searchField + " like '%"+keyword+"%'";
//			}
//		}
//		if (StringUtils.isNotBlank(condition)){
//			condition = " where " + condition;
//		}
//		
//		return QueryHelper.query(CLASS, "select * from " + INSTANCE.TableName() + condition);
//	}
//	
//	public static List<AdminRole> findAll(){
//		return QueryHelper.query(CLASS, "select * from " + INSTANCE.TableName());
//	}
//
//	/**
//	 * 通过ID查找记录且查询关联的权限集
//	 * @param id
//	 * @return
//	 */
//	public static AdminRole findByIdAndGetPermIds(long id) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where id = ?";
//		AdminRole role = QueryHelper.read(AdminRole.class, sql, id);
//		if (role != null) {
//			List<AdminRolePermRelation> relations = AdminRolePermRelation.findByRoleId(role.getId());
//			if (relations != null)
//				for (AdminRolePermRelation relation : relations) {
//					role.permIds.add(relation.getPermission_id());
//				}
//		}
//		
//		return role;
//	}
//	
//	public static long totalCount(String searchField, String searchKeyword){
//		System.out.println("field->" + searchField + ", key->" + searchKeyword);
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = "name like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  searchField + " like '%"+keyword+"%'";
//			}
//		}
//		
//		if (StringUtils.isNotBlank(condition))
//			return INSTANCE.TotalCount(condition);
//		return INSTANCE.TotalCount();
//	}
//	
//	public Map<String, String> validate_create(){
//		Map<String, String> err = new HashMap<String, String>(2);
//		
//		if (StringUtils.isBlank(this.name))
//			err.put("name", "角色名不能为空");
//		
//		//验证名字和URL是否重复
//		if (findByName(this.name) != null)
//			err.put("name", "角色名已存在");
//			
//		return err;
//	}
//	
//	public Map<String, String> validate_update(){
//		Map<String, String> err = new HashMap<String, String>(2);
//		
//		//检查ID是否存在
//		if (this.getId() <= 0)
//			err.put("id", "无效");
//		
//		if (StringUtils.isBlank(this.name))
//			err.put("name", "角色名不能为空");
//		
//		//验证名字和URL是否重复
//		AdminRole db_role = findOtherByName(this.getId(), this.getName());
//		if (db_role != null)
//			err.put("name", "角色名已定义");
//		
//		return err;
//	}
//	
//	/**
//	 * 批量删除角色记录
//	 * @param ids
//	 * @return
//	 */
//	public static long batchDelete(String... ids) {
//		StringBuilder idSB = new StringBuilder();
//		for (String id : ids) {
//			if (idSB.length() > 0)
//				idSB.append(",");
//			idSB.append(id);
//		}
//		final String sql = "delete from " + INSTANCE.TableName() + " where id in("+idSB.toString()+")";
//		return QueryHelper.update(sql);
//	}
//	
//	/**
//	 * 修改指定字段值
//	 * @param fields
//	 * @param values
//	 * @return
//	 */
//	public long update(String[]fields, Object... values){
//		StringBuilder fieldSB = new StringBuilder();
//		for (String field : fields) {
//			if (fieldSB.length() > 0)
//				fieldSB.append(", ");
//			fieldSB.append(field).append(" = ?");
//		}
//		
//		final String sql = "update " + INSTANCE.TableName() + " set " + fieldSB.toString() + " where id = " + this.getId();
//		return QueryHelper.update(sql, values);
//	}
//	
//	/**
//	 * 通过ID查找记录
//	 * @param id
//	 * @return
//	 */
//	public static AdminRole findById(long id) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where id = ?";
//		return QueryHelper.read(CLASS, sql, id);
//	}
//	
//	/**
//	 * 通过名字查找与给定ID不同的记录
//	 * @param id
//	 * @param name
//	 * @return
//	 */
//	public static AdminRole findOtherByName(long id, String name) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where name = ? and id <> ?";
//		return QueryHelper.read(CLASS, sql, name, id);
//	}
//	
//	/**
//	 * 通过权限名查找
//	 * @param name
//	 * @return
//	 */
//	public static AdminRole findByName(String name) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where name = ?";
//		return QueryHelper.read(CLASS, sql, name);
//	}
//	
//	/**
//	 * 分页+检索
//	 * @param page
//	 * @param size
//	 * @param orderField
//	 * @param orderDirection
//	 * @return
//	 */
//	public static List<AdminRole> getPage(int page, int size, String orderField, String orderDirection, String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = " where name like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  " where "+searchField + " like '%"+keyword+"%'";
//			}
//		}
//		final String sql = "select * from " + INSTANCE.TableName() + condition + " order by " + orderField + " " + orderDirection;
//		return QueryHelper.query_slice(CLASS, sql, page, size);
//	}
//
//	public static List<AdminPermission> findPermissionsById(long roleId) {
//		final String RPTable = AdminRolePermRelation.INSTANCE.TableName();
//		final String sql = "select p.* from " + RPTable + " rp, " + AdminPermission.INSTANCE.TableName() + " p where p.permission_id = rp.permission_id and rp.role_id = ?";
//		return QueryHelper.query(AdminPermission.class, sql, roleId);
//	}
//	
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public long getCreator_id() {
//		return creator_id;
//	}
//
//	public void setCreator_id(long creator_id) {
//		this.creator_id = creator_id;
//	}
//
//	public String getCreate_at() {
//		return create_at;
//	}
//
//	public void setCreate_at(String create_at) {
//		this.create_at = create_at;
//	}
//
//	public List<Long> GetPermIds() {
//		return permIds;
//	}

}
