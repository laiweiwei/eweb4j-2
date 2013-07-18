package controller;

import java.io.IOException;
import java.util.Arrays;

/**
 * Admin专用登录和注销
 * @author vivi
 *
 */
public class SessionsAction {

//	/**
//	 * 密文密码登录
//	 * @param ctx
//	 * @throws IOException 
//	 */
//	public void create(RequestContext ctx) throws IOException{
//		try {
//			String email=ctx.param("email");
//			String hash_pwd= ctx.param("pwd");
//			User user = User.hash_pwd_login(email, hash_pwd, ctx.ip());
//			if(user == null) {
//				ctx.print(DWZCallback.me().fail().msg("登录失败，请检查账号密码是否有误"));
//				return ;
//			}
//			
//			ctx.saveUserInCookie(user, "1".equals(ctx.param("save_login")));
//			ctx.print(DWZCallback.me().success().msg("登录成功!欢迎 "+user.getName()+" 回来").close());
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
//	
//	/**
//	 * 用户注销
//	 * @param req
//	 * @param res
//	 * @throws IOException 
//	 */
//	public void destroy(RequestContext ctx) throws IOException {
//		try {
//			User user = (User)ctx.user();
//			if(user != null){
//				String session = ctx.param("session");
//				if(!user.validateLogoutURL(session)){
//					ctx.print(DWZCallback.me().fail().msg("注销失败"));
//					return ;			
//				}
//				User.Logout(Arrays.asList(user.getId()));
//				OnlineUserManager.setLastActiveTime(user.getId(), -1L);			
//			}
//			ctx.deleteUserInCookie();
//			ctx.print(DWZCallback.me().success().msg("您已成功注销"));
//		} catch (Throwable e){
//			e.printStackTrace();
//			ctx.print(DWZCallback.me().fail().msg("发生错误，请检查日志"));
//		}
//	}
	
}
