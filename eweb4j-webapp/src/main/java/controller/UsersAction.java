package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 管理员账号Action处理器
 * @author vivi
 *
 */
public class UsersAction {

//	/**
//	 * 添加管理员账号
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void create(RequestContext ctx) throws IOException {
//		try {
//			User user = ctx.form(User.class);
//			user.setEmail(StringUtils.trim(user.getEmail()));
//			user.setName(StringUtils.trim(user.getName()));
//			
//			//执行验证
//			Map<String, String> errs = user.validate_create(ctx);
//			if (!errs.isEmpty()) {
//				ctx.print(DWZCallback.me().fail().msg(errs.toString()));
//				return ;
//			}
//			
//			//创建新的管理员账号记录
//			
//			user.setThis_login_ip(ctx.ip());
//			
//			System.out.println("--- "+user+" ---");
//			long newID = user.Save();;
//			user.setId(newID);
//			
//			ctx.print(DWZCallback.me().success().reload("modules/users"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 修改密码
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void change_pwd(RequestContext ctx) throws Exception{
//		try {
//			String oldpwd = ctx.param("oldpwd", "");
//			String pwd = ctx.param("pwd", "");
//			//执行验证
//			User user = (User)ctx.user();
//			if (!StringUtils.equals(oldpwd, user.getPwd())) {
//				ctx.print(DWZCallback.me().fail().msg("旧密码错误"));
//				return ;
//			}
//			
//			if (StringUtils.isBlank(pwd)){
//				ctx.print(DWZCallback.me().fail().msg("请输入新密码"));
//				return;
//			}
//			
//			//修改密码
//			System.out.println("-----------user changepwd-----------");
//			System.out.println("--- "+user+" ---");
//			final String[] fields = {"pwd"};
//			user.update(fields, pwd);
//			
//			ctx.print(DWZCallback.me().success().close());
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 修改管理员帐号
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void update(RequestContext ctx) throws Exception{
//		try {
//			User user = ctx.form(User.class);
//			//执行验证
//			Map<String, String> errs = user.validate_update(ctx);
//			if (!errs.isEmpty()) {
//				ctx.print(DWZCallback.me().fail().msg(errs.toString()));
//				return ;
//			}
//			//修改角色记录
//			System.out.println("-----------user update-----------");
//			System.out.println("--- "+user+" ---");
//			final String[] fields = {"email", "name", "nick_name", "pwd"};
//			user.update(fields, user.getEmail(), user.getName(), user.getNick_name(), user.getPwd());
//			
//			ctx.print(DWZCallback.me().success().reload("modules/users").close());
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 批量删除角色
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void batch_delete(RequestContext ctx) throws Exception{
//		try {
//			String[] ids = ctx.params("ids");
//			if (ids == null || ids.length == 0){
//				ctx.print(DWZCallback.me().fail().msg("请至少选择一条记录来删除"));
//				return ;
//			}
//				
//			System.out.println("-----------user batch delete-----------");
//			System.out.println("--- "+Arrays.asList(ids)+" ---");
//			
//			User.batchDelete(ids);
//			
//			ctx.print(DWZCallback.me().success().reload("modules/users"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 删除管理员账号
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void delete(RequestContext ctx) throws Exception{
//		try {
//			long id = ctx.param("id", 0L);
//			if (id == 0){
//				ctx.print(DWZCallback.me().fail().msg("id 值无效"));
//				return ;
//			}
//				
//			System.out.println("-----------user delete-----------");
//			System.out.println("--- "+id+" ---");
//			
//			AdminRole db_role = AdminRole.findById(id);
//			if (db_role == null){
//				ctx.print(DWZCallback.me().fail().msg("id 值无效"));
//				return ;
//			}
//			
//			AdminRole.batchDelete(id+"");
//			
//			ctx.print(DWZCallback.me().success().reload("modules/users"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 增加、修改用户角色关联关系
//	 * @param ctx
//	 * @throws IOException
//	 */
//	public void update_user_role_relation(RequestContext ctx) throws IOException{
//		try {
//			String[] roleIds = ctx.params("roleIds");
//			if (roleIds == null)
//				roleIds = new String[]{""};
//			System.out.println("roleIds->" + Arrays.asList(roleIds));
//			long userId = ctx.param("userId", 0);
//			System.out.println("userId->"+userId);
//			//删除当前用户分配的所有角色
//			AdminUserRoleRelation.deleteByUserId(userId);
//			
//			for (String roleIdStr : roleIds) {
//				if (StringUtils.isBlank(roleIdStr))
//					continue;
//				long roleId = 0;
//				try {
//					roleId = Long.parseLong(roleIdStr);
//				} catch (Throwable e){
//					continue;
//				}
//				if (roleId <= 0)
//					continue;
//				
//				AdminUserRoleRelation db_relation = AdminUserRoleRelation.findByUserIdAndRoleId(userId, roleId);
//				if (db_relation == null) {
//					db_relation = new AdminUserRoleRelation();
//					db_relation.setUser_id(userId);
//					db_relation.setRole_id(roleId);
//					db_relation.Save();
//				}
//			}
//			
//			ctx.print(DWZCallback.me().success().reload("modules/users").close());
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
	
}
