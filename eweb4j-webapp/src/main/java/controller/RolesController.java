package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 角色Action处理器
 * @author vivi
 *
 */
public class RolesController {
//
//	/**
//	 * 增加、修改角色权限关联关系
//	 * @param ctx
//	 * @throws IOException
//	 */
//	public void update_role_perm_relation(RequestContext ctx) throws IOException{
//		try {
//			String[] permIds = ctx.params("permIds");
//			if (permIds == null)
//				permIds = new String[]{""};
//			System.out.println("permIds->" + Arrays.asList(permIds));
//			long roleId = ctx.param("roleId", 0);
//			System.out.println("roleId->"+roleId);
//			//删除当前角色分配的所有权限
//			AdminRolePermRelation.deleteByRoleId(roleId);
//			
//			for (String permIdStr : permIds) {
//				if (StringUtils.isBlank(permIdStr))
//					continue;
//				long permId = 0;
//				try {
//					permId = Long.parseLong(permIdStr);
//				} catch (Throwable e){
//					continue;
//				}
//				if (roleId <= 0)
//					continue;
//				
//				AdminRolePermRelation db_relation = AdminRolePermRelation.findByRoleIdAndPermId(roleId, permId);
//				if (db_relation == null) {
//					db_relation = new AdminRolePermRelation();
//					db_relation.setRole_id(roleId);
//					db_relation.setPermission_id(permId);
//					db_relation.Save();
//				}
//			}
//			
//			ctx.print(DWZCallback.me().success().reload("modules/roles").close());
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 添加角色
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void create(RequestContext ctx) throws IOException {
//		try {
//			AdminRole role = ctx.form(AdminRole.class);
//			//执行验证
//			Map<String, String> errs = role.validate_create();
//			if (!errs.isEmpty()) {
//				ctx.print(DWZCallback.me().fail().msg(errs.toString()));
//				return ;
//			}
//			
//			//获取当前登录用户
//			User loginUser = (User)ctx.user();
//			//创建新的角色记录
//			role.setCreator_id(loginUser.getId());
//			role.setCreate_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//			System.out.println("-----------role create-----------");
//			System.out.println("--- "+role+" ---");
//			long newID = role.Save();
//			role.setId(newID);
//			
//			ctx.print(DWZCallback.me().success().reload("modules/roles"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 修改角色
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void update(RequestContext ctx) throws Exception{
//		try {
//			AdminRole role = ctx.form(AdminRole.class);
//			//执行验证
//			Map<String, String> errs = role.validate_update();
//			if (!errs.isEmpty()) {
//				ctx.print(DWZCallback.me().fail().msg(errs.toString()));
//				return ;
//			}
//			//修改角色记录
//			System.out.println("-----------role update-----------");
//			System.out.println("--- "+role+" ---");
//			final String[] fields = {"name"};
//			role.update(fields, role.getName());
//			
//			ctx.print(DWZCallback.me().success().reload("modules/roles").close());
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
//			System.out.println("-----------role batch delete-----------");
//			System.out.println("--- "+Arrays.asList(ids)+" ---");
//			
//			AdminRole.batchDelete(ids);
//			
//			ctx.print(DWZCallback.me().success().reload("modules/roles"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 删除角色
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
//			System.out.println("-----------role delete-----------");
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
//			ctx.print(DWZCallback.me().success().reload("modules/roles"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
}
