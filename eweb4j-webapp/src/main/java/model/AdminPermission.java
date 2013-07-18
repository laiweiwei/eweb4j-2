package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 管理员权限模型 
 * @author vivi
 * @date 2013-7-2 上午11:27:50
 */
@Entity
@Table(name="qhee_admin_permission")
public class AdminPermission {

//	public Map<String, String> validate_create(){
//		Map<String, String> err = new HashMap<String, String>(2);
//		
//		if (this.permission_name == null || this.permission_desc.trim().length() == 0)
//			err.put("permission_name", "权限名不能为空");
//		
//		if (this.uri == null || this.uri.trim().length() == 0)
//			err.put("uri", "URI不能为空");
//		
//		//验证名字和URL是否重复
//		if (findByName(this.permission_name) != null)
//			err.put("permission_name", "权限名已定义");
//			
//		if (findByUri(this.uri) != null)
//			err.put("uri", "URI已定义");
//		
//		return err;
//	}
//	
//	public Map<String, String> validate_update(){
//		Map<String, String> err = new HashMap<String, String>(2);
//		
//		//检查ID是否存在
//		if (this.permission_id <= 0)
//			err.put("permission_id", "无效");
//		
//		if (this.permission_name == null || this.permission_name.trim().length() == 0)
//			err.put("permission_name", "权限名不能为空");
//		
//		if (this.uri == null || this.uri.trim().length() == 0)
//			err.put("uri", "URI不能为空");
//		
//		//验证名字和URL是否重复
//		AdminPermission db_perm = findOtherByName(this.permission_id, this.permission_name);
//		if (db_perm != null)
//			err.put("permission_name", "权限名已定义");
//		db_perm = findOtherByUri(this.permission_id, this.uri);
//		if (db_perm != null)
//			err.put("uri", "URI已定义");
//		
//		return err;
//	}
//	
//	public static List<PermissionGroup> findAllSplitByGroup(){
//		return findAllSplitByGroup("", "");
//	}
	
//	public static List<PermissionGroup> findAllSplitByGroup(String searchField, String searchKeyword){
//		List<PermissionGroup> groups = new ArrayList<PermissionGroup>();
//		
//		String sql2 = "select DISTINCT p.permission_group from #table p where p.permission_group is not null and p.permission_group <> ''";
//		List<String> groupNames = Db.model().query(String.class, sql2);
//		if (groupNames != null) {
//			for (String groupName : groupNames){
//				PermissionGroup normalGroup = new PermissionGroup();
//				List<AdminPermission> perms = findByGroup(groupName, searchField, searchKeyword);
//				if (perms == null || perms.isEmpty())
//					continue;
//				
//				normalGroup.group_name = groupName;
//				normalGroup.items.addAll(perms);
//				groups.add(normalGroup);
//			}
//		}
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = "permission_name like '%"+keyword+"%' or uri like '%"+keyword+"%' or permission_desc like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  searchField + " like '%"+keyword+"%'";
//			}
//		}
//		if (StringUtils.isNotBlank(condition)){
//			condition = " and ( " + condition + " )";
//		}
//		
//		String sql = "select p.* from "+INSTANCE.TableName()+" p where ( p.permission_group is null or p.permission_group = '' )" + condition;
//		List<AdminPermission> permsNoGroup = QueryHelper.query(AdminPermission.class, sql);
//		if (permsNoGroup != null && !permsNoGroup.isEmpty()) {
//			PermissionGroup defaultGroup = new PermissionGroup();
//			defaultGroup.group_name = "待分组权限";
//			defaultGroup.items.addAll(permsNoGroup);
//			groups.add(defaultGroup);
//		}
//		
//		return groups;
//	}
//
//	public static List<AdminPermission> findByGroup(String group, String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = "permission_name like '%"+keyword+"%' or uri like '%"+keyword+"%' or permission_desc like '%"+keyword+"%' ";
//			} else {
//				//查询指定字段
//				condition =  searchField + " like '%"+keyword+"%'";
//			}
//		}
//		if (StringUtils.isNotBlank(condition)){
//			condition = " and ( " + condition + " )";
//		}
//		
//		final String sql = "select * from " + INSTANCE.TableName() + " where permission_group = ?"+condition;
//		return QueryHelper.query(CLASS, sql, group);
//	}
//	
//	public static List<AdminPermission> findAll(){
//		return QueryHelper.query(CLASS, "select * from " + INSTANCE.TableName());
//	}
//
//	public static long totalCount(String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = "permission_name like '%"+keyword+"%' or uri like '%"+keyword+"%' or permission_desc like '%"+keyword+"%' or permission_group like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  searchField + " like '%"+keyword+"%'";
//			}
//		}
//		if (StringUtils.isNotBlank(condition))
//			return INSTANCE.TotalCount(condition);
//		return INSTANCE.TotalCount();
//	}
//	
//	/**
//	 * 批量删除权限记录
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
//		final String sql = "delete from " + INSTANCE.TableName() + " where permission_id in("+idSB.toString()+")";
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
//		final String sql = "update " + INSTANCE.TableName() + " set " + fieldSB.toString() + " where permission_id = " + this.permission_id;
//		return QueryHelper.update(sql, values);
//	}
//	
//	/**
//	 * 通过ID查找权限记录
//	 * @param id
//	 * @return
//	 */
//	public static AdminPermission findById(long id) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where permission_id = ?";
//		return QueryHelper.read(CLASS, sql, id);
//	}
//	
//	/**
//	 * 通过名字查找与给定ID不同的记录
//	 * @param id
//	 * @param name
//	 * @return
//	 */
//	public static AdminPermission findOtherByName(long id, String name) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where permission_name = ? and permission_id <> ?";
//		return QueryHelper.read(CLASS, sql, name, id);
//	}
//	
//	/**
//	 * 通过权限名查找
//	 * @param name
//	 * @return
//	 */
//	public static AdminPermission findByName(String name) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where permission_name = ?";
//		return QueryHelper.read(CLASS, sql, name);
//	}
//	
//	/**
//	 * 通过URI查找与给定ID不同的记录
//	 * @param id
//	 * @param uri
//	 * @return
//	 */
//	public static AdminPermission findOtherByUri(long id, String uri) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where uri = ? and permission_id <> ?";
//		return QueryHelper.read(CLASS, sql, uri, id);
//	}
//	
//	public static AdminPermission findByUri(String uri) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where uri = ?";
//		return QueryHelper.read(CLASS, sql, uri);
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
//	public static List<AdminPermission> getPage(int page, int size, String orderField, String orderDirection, String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = " where permission_name like '%"+keyword+"%' or uri like '%"+keyword+"%' or permission_desc like '%"+keyword+"%' or permission_group like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  " where "+searchField + " like '%"+keyword+"%'";
//			}
//		}
//		final String sql = "select * from " + INSTANCE.TableName() + condition + " order by " + orderField + " " + orderDirection;
//		return QueryHelper.query_slice(CLASS, sql, page, size);
//	}
//	
//	/**
//	 * 自增长ID
//	 */
//	private long permission_id;
//	
//	/**
//	 * 权限名
//	 */
//	private String permission_name;
//	
//	private String permission_group;//分组
//	
//	/**
//	 * 权限描述
//	 */
//	private String permission_desc;
//	
//	/**
//	 * 权限对应的Action URI
//	 */
//	private String uri;
//	
//	public long getId(){
//		return permission_id;
//	}
//	
//	public void setId(long id) {
//		this.permission_id = id;
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
//	public String getPermission_name() {
//		return permission_name;
//	}
//
//	public void setPermission_name(String permission_name) {
//		this.permission_name = permission_name;
//	}
//
//	public String getPermission_desc() {
//		return permission_desc;
//	}
//
//	public void setPermission_desc(String permission_desc) {
//		this.permission_desc = permission_desc;
//	}
//
//	public String getUri() {
//		return uri;
//	}
//
//	public void setUri(String uri) {
//		this.uri = uri;
//	}
//
//	public String getPermission_group() {
//		return permission_group;
//	}
//
//	public void setPermission_group(String permission_group) {
//		this.permission_group = permission_group;
//	}
//
//	@Override
//	public String toString() {
//		return "AdminPermission [permission_id=" + permission_id
//				+ ", permission_name=" + permission_name
//				+ ", permission_group=" + permission_group
//				+ ", permission_desc=" + permission_desc + ", uri=" + uri + "]";
//	}
//	
//	public static class PermissionGroup {
//		private String group_name = "待分组权限";
//		private List<AdminPermission> items = new ArrayList<AdminPermission>();
//		
//		public String getGroup_name() {
//			return group_name;
//		}
//		public void setGroup_name(String group_name) {
//			this.group_name = group_name;
//		}
//		public List<AdminPermission> getItems() {
//			return items;
//		}
//		public void setItems(List<AdminPermission> items) {
//			this.items = items;
//		}
//	}

}
