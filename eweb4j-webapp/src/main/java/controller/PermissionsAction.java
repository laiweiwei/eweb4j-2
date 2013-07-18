package controller;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 * 权限Action处理器
 * @author vivi
 * @date 2013-7-3 下午01:42:04
 * @version V1.0
 */
public class PermissionsAction {

//	/**
//	 * 添加权限
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	public void create(RequestContext ctx) throws Exception{
//		try {
//			AdminPermission perm = ctx.form(AdminPermission.class);
//			//执行验证
//			Map<String, String> errs = perm.validate_create();
//			if (!errs.isEmpty()) {
//				ctx.print(DWZCallback.me().fail().msg(errs.toString()));
//				return ;
//			}
//			//创建新的权限记录
//			System.out.println("-----------permission create-----------");
//			System.out.println("--- "+perm+" ---");
//			long newID = perm.Save();
//			perm.setId(newID);
//			
//			ctx.print(DWZCallback.me().success().reload("modules/permissions"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 修改权限
//	 * @param ctx   
//	 * @return void  
//	 * @throws
//	 */
//	@PostMethod
//	@JSONOutputEnabled
//	public void update(RequestContext ctx) throws Exception{
//		try {
//			AdminPermission perm = ctx.form(AdminPermission.class);
//			//执行验证
//			Map<String, String> errs = perm.validate_update();
//			if (!errs.isEmpty()) {
//				ctx.print(DWZCallback.me().fail().msg(errs.toString()));
//				return ;
//			}
//			//创建新的权限记录
//			System.out.println("-----------permission update-----------");
//			System.out.println("--- "+perm+" ---");
//			final String[] fields = {"permission_name","permission_desc","uri","permission_group"};
//			perm.update(fields, perm.getPermission_name(), perm.getPermission_desc(), perm.getUri(), perm.getPermission_group());
//			
//			ctx.print(DWZCallback.me().success().reload("modules/permissions").close());
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 批量删除权限
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
//			//创建新的权限记录
//			System.out.println("-----------permission batch delete-----------");
//			System.out.println("--- "+Arrays.asList(ids)+" ---");
//			
//			AdminPermission.batchDelete(ids);
//			
//			ctx.print(DWZCallback.me().success().reload("modules/permissions"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 删除权限
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
//			//创建新的权限记录
//			System.out.println("-----------permission delete-----------");
//			System.out.println("--- "+id+" ---");
//			
//			AdminPermission db_perm = AdminPermission.findById(id);
//			if (db_perm == null){
//				ctx.print(DWZCallback.me().fail().msg("id 值无效"));
//				return ;
//			}
//			
//			AdminPermission.batchDelete(id+"");
//			
//			ctx.print(DWZCallback.me().success().reload("modules/permissions"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
}
