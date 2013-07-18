package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册用户
 * @author liudong
 */
public class User {
//
//	private final static Log LOG = LogFactory.getLog(User.class);
//	
//	private final static transient String CACHE_USER_EMAILS = "UserEmails";
//	public final static transient User INSTANCE = new User();
//	public final static transient long WEBMASTER = 1;
//	
//	private List<Long> roleIds = new ArrayList<Long>();
//	
//	public Map<String, String> validate_create(RequestContext ctx){
//		User user = this;
//		Map<String, String> err = new HashMap<String, String>();
//		if(!FormatTool.is_email(user.getEmail())){
//			err.put("email", "邮箱格式不合法");
//			return err;
//		}
//		
//		if(user.getEmail().toLowerCase().endsWith("@prtnx.com")) {
//			err.put("email", "该邮箱服务商不被允许");
//			return err;
//		}
//
//		if(StringUtils.isBlank(user.getName())) {
//			err.put("name", "用户名不能为空");
//			return err;
//		}
//		
//		if(!user.getName().matches("^[A-Za-z0-9\\u4e00-\\u9fa5_-]+$"))
//			err.put("name", "用户名不合法");
//		
//		if(user.getName().getBytes().length < ActionChecker.MIN_NAME_LENGTH || user.getName().length() > ActionChecker.MAX_NAME_LENGTH)
//			err.put("name", "用户名长度限制["+ActionChecker.MIN_NAME_LENGTH+", "+ActionChecker.MAX_NAME_LENGTH+"]");
//		
////		boolean isRegFromMobile = ctx.param("from_mobile", 0) == 1;
//		if(StringUtils.isBlank(user.getProvince()) || StringUtils.isBlank(user.getCity())){
//			IPAddr ip = IPAddr.getRangeOfIp(ctx.ip());
//			if(ip != null){
//				user.setProvince(ip.getProvince());
//				user.setCity(ip.getCity());
//			} 
////			else if(!isRegFromMobile)
////				err.put("user", "无法识别用户所在地");
//		}
//		
//		if(User.GetUserByEmail(user.getEmail())!=null)
//			err.put("email", "邮箱已被注册");
//		
//		if(User.ExistUsername(user.getName()))
//			err.put("name", "用户名已被注册");
//		
//		//检查密码
//		if(StringUtils.isBlank(user.getPwd()) || user.getPwd().length() < 4)
//			err.put("pwd", "密码不合法");
//		
//		return err;
//	}
//	
//	public Map<String, String> validate_update(RequestContext ctx){
//		User user = this;
//		Map<String, String> err = new HashMap<String, String>();
//		if(!FormatTool.is_email(user.getEmail())) { 
//			err.put("email", "邮箱格式不合法");
//			return err;
//		}
//		
//		if(user.getEmail().toLowerCase().endsWith("@prtnx.com")) 
//			err.put("email", "该邮箱服务商不被允许");
//
//		if(StringUtils.isBlank(user.getName())) {
//			err.put("name", "用户名不能为空");
//			return err;
//		}
//
//		if(!user.getName().matches("^[A-Za-z0-9\\u4e00-\\u9fa5_-]+$"))
//			err.put("name", "用户名不合法");
//		
//		if(user.getName().getBytes().length < ActionChecker.MIN_NAME_LENGTH || user.getName().length() > ActionChecker.MAX_NAME_LENGTH)
//			err.put("name", "用户名长度限制["+ActionChecker.MIN_NAME_LENGTH+", "+ActionChecker.MAX_NAME_LENGTH+"]");
//		
////		boolean isRegFromMobile = ctx.param("from_mobile", 0) == 1;
//		if(StringUtils.isBlank(user.getProvince()) || StringUtils.isBlank(user.getCity())){
//			IPAddr ip = IPAddr.getRangeOfIp(ctx.ip());
//			if(ip != null){
//				user.setProvince(ip.getProvince());
//				user.setCity(ip.getCity());
//			} 
////			else if(!isRegFromMobile)
////				err.put("user", "无法识别用户所在地");
//		}
//		
//		if(User.findOtherByEmail(user.getId(), user.getEmail())!=null)
//			err.put("email", "邮箱已被注册");
//		
//		if(User.findOtherByName(user.getId(), user.getName()) != null)
//			err.put("name", "用户名已被注册");
//		
//		//检查密码
//		if(StringUtils.isBlank(user.getPwd()) || user.getPwd().length() < 4)
//			err.put("pwd", "密码不合法");
//		
//		return err;
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
//	public static List<User> getPage(int page, int size, String orderField, String orderDirection, String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = " where name like '%"+keyword+"%' or nick_name like '%"+keyword+"%' or email like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  " where "+searchField + " like '%"+keyword+"%'";
//			}
//		}
//		final String sql = "select * from " + INSTANCE.TableName() + condition + " order by " + orderField + " " + orderDirection;
//		return QueryHelper.query_slice(User.class, sql, page, size);
//	}
//	
//	/**
//	 * 批量删除记录
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
//	 * 通过ID查找记录
//	 * @param id
//	 * @return
//	 */
//	public static User findById(long id) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where id = ?";
//		return QueryHelper.read(User.class, sql, id);
//	}
//	
//	/**
//	 * 通过ID查找记录且查询关联的角色
//	 * @param id
//	 * @return
//	 */
//	public static User findByIdAndGetRoleIds(long id) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where id = ?";
//		User user = QueryHelper.read(User.class, sql, id);
//		if (user != null) {
//			List<AdminUserRoleRelation> relations = AdminUserRoleRelation.findByUserId(user.getId());
//			if (relations != null)
//				for (AdminUserRoleRelation relation : relations) {
//					user.roleIds.add(relation.getRole_id());
//				}
//		}
//		
//		return user;
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
//	public String logout_url(){
//		return LinkTool.action("user/logout?session="+StringUtils.left(pwd, 10)+Math.abs(email.hashCode()));
//	}
//	
//	public static User findOtherByEmail(long id, String email) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where email = ? and id <> ?";
//		return QueryHelper.read(User.class, sql, email, id);
//	}
//	
//	public static User findOtherByName(long id, String name) {
//		final String sql = "select * from " + INSTANCE.TableName() + " where name = ? and id <> ?";
//		return QueryHelper.read(User.class, sql, name, id);
//	}
//	
//	public boolean validateLogoutURL(String session) {
//		String old = StringUtils.left(pwd, 10)+Math.abs(email.hashCode());
//		return StringUtils.equals(old, session);
//	}
//
//	public boolean IsInvestor(){
//		return this.getRole_type() == User.ROLE_TYPE_PER_INVESTER || this.getRole_type() == User.ROLE_TYPE_INS_INVESTER;
//	}
//	
//	public List<Long> ListMyProjects() {
//		return Project.ListMyProjects(getId());
//	}
//	
//	public static List<LoginBinding> ListLoginBindings(long id) {
//		return LoginBinding.list(id);
//	}
//	
//	public boolean hasMoreInfo(){
//		return getMoreInfo(getId())!=null;
//	}
//	
//	public static boolean hasMoreInfo(long id){
//		return getMoreInfo(id)!=null;
//	}
//		
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static Pojo getMoreInfo(long id){
//		User user = User.INSTANCE.Get(id);
//		if(user==null)
//			return null;
//		String sql = "";
//		Class clazz = null;
//		byte role_type = user.getRole_type();
//		if(role_type == ROLE_TYPE_PER_INVESTER){
//			sql = "SELECT * FROM qhee_investor_personal WHERE user_id = ?";
//			clazz = InvestorPersonal.class;
//		}else if(role_type == ROLE_TYPE_INS_INVESTER){
//			sql = "SELECT * FROM qhee_investor_institution WHERE user_id = ?";
//			clazz = InvestorInstitution.class;
//		}else if(role_type == ROLE_TYPE_ENT){
//			sql = "SELECT * FROM osc_projects WHERE user = ?";
//			clazz = Project.class;
//		}
//		String key = "mi#"+role_type+"#"+id;
//		return (Pojo) QueryHelper.read_cache(clazz, INSTANCE.CacheRegion(), key, sql, id);
//	}
//	
//	/**
//	 * 注销用户
//	 * @param id
//	 */
//	public static boolean Logout(List<Long> ids) {
//		if(ids == null || ids.size()==0)
//			return false;
//		String sql = "UPDATE osc_users SET ONLINE = ? WHERE id=?";
//		Object[][] args = new Object[ids.size()][2];
//		for(int i=0;i<ids.size();i++){
//			args[i][0] = User.OFFLINE;
//			args[i][1] = ids.get(i);
//			User u = (User)CacheManager.get(INSTANCE.CacheRegion(), ids.get(i));
//			if(u != null){
//				u.setOnline(User.OFFLINE);
//				CacheManager.set(INSTANCE.CacheRegion(), ids.get(i), u);
//			}
//		}
//		QueryHelper.batch(sql, args);
//		return true;
//	}
//	
//	public static void IncScore(long user, int score) {
//		String sql = "UPDATE osc_users SET score = score + ? WHERE id = ?";
//		if(QueryHelper.update(sql, score, user)==1){
//			User obj = (User)getCache("User", user);
//			if(obj != null){
//				obj.setScore(obj.getScore() + score);
//				setCache("User", user, obj);
//			}
//		}
//	}
//	
//	/**
//	 * 获取一些相关的统计数据
//	 * @return
//	 */
//	public TweetCount GetTweetCounts(){
//		return TweetCount.get(getId());
//	}
//	
//	/**
//	 * 列出所有在线用户id
//	 * @return
//	 */
//	public static List<Long> ListOnlineUsers() {
//		String sql = "SELECT id FROM osc_users WHERE online=?";
//		return QueryHelper.query(long.class, sql, ONLINE);
//	}
//	
//	/**
//	 * 修改登录的邮箱地址
//	 * @param new_email
//	 */
//	public void ChangeEmail(String new_email) {
//		//从缓存中清除老邮箱
//		evictCache(CACHE_USER_EMAILS, getEmail().toLowerCase());
//		String sql = "UPDATE osc_users SET email=? WHERE id=?";
//		Evict(QueryHelper.update(sql, new_email, getId())==1);
//		evictCache(CACHE_USER_EMAILS, new_email.toLowerCase());
//	}
//	
//	public void ChangeNotifySetting() {
//		String sql = "UPDATE osc_users SET options=?,notify_email=?,notify_email_validated=? WHERE id=?";
//		Evict(QueryHelper.update(sql, options,notify_email,notify_email_validated,getId())==1);		
//	}
//
//	public void ChangeIdent() {
//		String sql = "UPDATE osc_users SET ident = ? WHERE id = ?";
//		Evict(QueryHelper.update(sql, ident, getId())==1);		
//	}
//
//	public static User GetByIdent(String ident) {
//		if(StringUtils.isBlank(ident))
//			return null;
//		Long id = QueryHelper.read_cache(Long.class, INSTANCE.CacheRegion(), ident, "SELECT id FROM osc_users WHERE ident=?", ident);
//		return (id!=null)?(User)INSTANCE.Get(id):null;
//	}
//	public static User GetByName(String name) {
//		if(StringUtils.isBlank(name))
//			return null;
//		Long id = QueryHelper.read_cache(Long.class, INSTANCE.CacheRegion(), "#N" + name, "SELECT id FROM osc_users WHERE name=?", name);
//		return (id!=null)?(User)INSTANCE.Get(id):null;
//	}
//	
//	/**
//	 * 使用OpenID的自动注册账号
//	 * @param email 邮箱地址
//	 * @param fn 名
//	 * @param ln 姓
//	 * @param gender 性别
//	 * @param client_ip IP地址
//	 * @return
//	 */
//	public static User AutoCreateViaOpenID(String email, String fn, String ln, byte gender, String client_ip) {
//		User user = new User();
//		user.setGender(gender);
//		user.setEmail(email);
//		String name = "";
//		if(StringUtils.isNotBlank(fn) && StringUtils.isNotBlank(ln)){
//			if(StringUtils.equals(fn, ln))
//				name = fn;
//			else{
//				if(fn.length() < ln.length())
//					name = fn + ln;
//				else
//					name = ln + fn;
//			}
//		}
//		else{
//			if(StringUtils.isNotBlank(ln))
//				name = ln;
//			if(StringUtils.isNotBlank(fn))
//				name += fn;
//		}
//		if(StringUtils.isBlank(name))
//			name = email.substring(0, email.indexOf('@'));
//		user.setName(StringUtils.abbreviate(name,20));
//		user.setType((byte)3);//OpenID的同步用户类型
//		user.setRole(User.ROLE_GENERAL);
//		user.setScore((short)0);
//		user.setRank((byte)1);
//		user.setOptions(0);
//		user.setActivate_code(RandomStringUtils.randomAlphanumeric(40));
//		user.setStatus(User.STATUS_NORMAL);
//		String pwd = RandomStringUtils.randomNumeric(4);
//		user.setPwd(DigestUtils.shaHex(pwd));
//		
//		try{
//			IPAddr ip = IPAddr.getRangeOfIp(client_ip);
//			if(ip != null){
//				user.setProvince(ip.getProvince());
//				user.setCity(ip.getCity());
//			}
//		}catch(Exception e){
//			e.printStackTrace(System.err);
//		}
//		
//		user.JustSave();
//
//		OptLog.newUser(user);
//		
//		//发送站内留言
//		Msg.SendViaKey(1, user.getId(), "openid.autoreg.msg", Msg.TYPE_SYSTEM, user.getEmail(), user.getName(), pwd);
//		
//		//发送email
//		try{
//			String html = TemplateExecutor.callOpenIDTemplate(user.getName(), user.getEmail(), pwd);
//			SmtpHelper.send(user.getEmail(), ResourceUtils.ui("activate_mail_subject"), html);
//		}catch(Exception e){
//			LOG.error("Unabled to send openid email to " + user.getEmail(), e);
//			SmtpHelper.reportError(RequestContext.get().request(), e);
//		}
//		
//		return user;
//	}
//	/**
//	 * 使用OpenID的自动注册账号
//	 * @param email 邮箱地址
//	 * @param fn 名
//	 * @param ln 姓
//	 * @param gender 性别
//	 * @param client_ip IP地址
//	 * @return
//	 */
//	public static User AutoCreateViaOpenID(String email, String fn, String ln, byte gender, String client_ip, boolean validate_email) {
//		User user = new User();
//		user.setGender(gender);
//		user.setEmail(email);
//		String name = "";
//		if(StringUtils.isNotBlank(fn) && StringUtils.isNotBlank(ln)){
//			if(StringUtils.equals(fn, ln))
//				name = fn;
//			else{
//				if(fn.length() < ln.length())
//					name = fn + ln;
//				else
//					name = ln + fn;
//			}
//		}
//		else{
//			if(StringUtils.isNotBlank(ln))
//				name = ln;
//			if(StringUtils.isNotBlank(fn))
//				name += fn;
//		}
//		if(StringUtils.isBlank(name))
//			name = email.substring(0, email.indexOf('@'));
//		user.setName(StringUtils.abbreviate(name,20));
//		user.setType((byte)3);//OpenID的同步用户类型
//		user.setRole(User.ROLE_GENERAL);
//		user.setScore((short)0);
//		user.setRank((byte)1);
//		user.setOptions(0);
//		user.setActivate_code(RandomStringUtils.randomAlphanumeric(40));
//		if(validate_email)
//			user.setStatus(User.STATUS_PENDING);
//		else
//			user.setStatus(User.STATUS_NORMAL);
//		String pwd = RandomStringUtils.randomNumeric(4);
//		user.setPwd(DigestUtils.shaHex(pwd));
//		
//		try{
//			IPAddr ip = IPAddr.getRangeOfIp(client_ip);
//			if(ip != null){
//				user.setProvince(ip.getProvince());
//				user.setCity(ip.getCity());
//			}
//		}catch(Exception e){
//			e.printStackTrace(System.err);
//		}
//		
//		user.JustSave();
//		//激活账号时会调添加log
//		if(!validate_email)
//			OptLog.newUser(user);
//		
//		//发送站内留言
//		Msg.SendViaKey(1, user.getId(), "openid.autoreg.msg", Msg.TYPE_SYSTEM, user.getEmail(), user.getName(), pwd);
//		
//		//发送email
//		try{
//			String html = null;
//			if(validate_email)
//				html = TemplateExecutor.callOpenIDActivateTemplate(user.getId(), user.getName(), user.getEmail(), pwd, user.getActivate_code());
//			else
//				html = TemplateExecutor.callOpenIDTemplate(user.getName(), user.getEmail(), pwd);
//			SmtpHelper.send(user.getEmail(), ResourceUtils.ui("activate_mail_subject"), html);
//		}catch(Exception e){
//			LOG.error("Unabled to send openid email to " + user.getEmail(), e);
//			SmtpHelper.reportError(RequestContext.get().request(), e);
//		}
//		
//		return user;
//	}
//	
//	/**
//	 * 后台查找用户
//	 * @param q
//	 * @return
//	 */
//	public static List<User> FindUsers(String q) {
//		String sql = "SELECT * FROM osc_users WHERE id=? OR name LIKE ? OR email LIKE ? ORDER BY id DESC";
//		String p = q + "%";
//		return QueryHelper.query(User.class, sql, NumberUtils.toLong(q, 0), p, p);
//	}
//
//	/**
//	 * 屏蔽或解除屏蔽用户
//	 * @param uid
//	 * @param block
//	 * @return
//	 */
//	public boolean Block(boolean block) {
//		String sql = "UPDATE osc_users SET status=? WHERE id=?";
//		return Evict(QueryHelper.update(sql, block?STATUS_DISABLED:STATUS_NORMAL, getId())==1);
//	}	
//	
//	/**
//	 * 删除用户
//	 */
//	@Override
//	public boolean Delete() {
//		if(StringUtils.isNotBlank(ident))
//			evictCache(CacheRegion(), ident);
//		
//		if(this.status == STATUS_PENDING) {
//			Friend.DeleteFriend(getId(), getId());
//			//删除账号绑定
//			LoginBinding.Delete(getId());
//			return super.Delete();
//		}
//		else{			
//			//删贴
//			List<Answer> posts = Answer.ListOfUser(getId());
//			if(posts != null)
//				for(Answer post : posts)
//					post.Delete();
//			List<Question> questions = QuestionTool.list(QuestionTool.list_by_user(getId()));
//			if(questions != null)
//			for(Question q : questions)
//				q.Delete();
//			
//			List<Code> codes = CodeTool.list_codes_of_user(getId(), 1, 999999);
//			if(codes != null)
//			for(Code code : codes)
//				code.Delete();
//					
//			//删除账号绑定
//			LoginBinding.Delete(getId());
//			
//			Space sps = Space.INSTANCE.Get(getId());
//			if(sps != null)
//				sps.Delete();
//			QueryHelper.update("CALL DELETE_USER(?)", getId());		
//			Evict(true);
//			evictCache(CACHE_USER_EMAILS, getEmail());
//		}
//		return true;
//	}
//
//	/**
//	 * 只列出好友动态，不包括我自己的
//	 * @param page
//	 * @param count
//	 * @return
//	 */
//	public List<Long> ListFriendOptLogs(int page, int count) {
//		return OptLog.ListLogsOfFriend(getId(), page, count);
//	}
//
//	/**
//	 * 列出所有定制了每周精粹的邮箱地址
//	 * @param scope
//	 * @return
//	 */
//	public static List<String> ListWeeklyReportEmails(byte scope) {
//		switch(scope){
//		case 1:
//			String sql = "SELECT LOWER(email) FROM osc_users WHERE (type=1 OR type=3) AND status=1 AND options & ? = ? ORDER BY rank DESC, score DESC, this_login_time DESC";
//			return QueryHelper.query(String.class, sql, SERVICE_WEEKLY_REPORT, SERVICE_WEEKLY_REPORT);
//		case 2:
//			sql = "SELECT LOWER(email) FROM osc_users WHERE (type=1 OR type=3) AND status=1 ORDER BY rank DESC, score DESC, this_login_time DESC";
//			return QueryHelper.query(String.class, sql);
//		case 3:
//			List<String> emails = new ArrayList<String>();
//			emails.addAll(ListWeeklyReportEmails((byte)1));
//			emails.addAll(ListWeeklyReportEmails((byte)4));
//			return emails;
//		case 4:
//			return QueryHelper.query(String.class, "SELECT LOWER(email) FROM misc_emails WHERE status=1");
//		}
//		return null;
//	}
//	
//	/**
//	 * 增、减用户积分
//	 * @param id
//	 * @param score
//	 * @return
//	 */
//	public static boolean AddScore(long id, int score){
//		return INSTANCE.Evict(QueryHelper.update("UPDATE osc_users SET score=score+? WHERE id=?", score, id)==1);
//	}
//	
//	public boolean AssignAdmin(byte role) {
//		return Evict(QueryHelper.update("UPDATE osc_users SET role=? WHERE id=?", role, getId()) == 1);
//	}
//	
//	public boolean DismissAdmin() {
//		return Evict(QueryHelper.update("UPDATE osc_users SET role=? WHERE id=?", ROLE_GENERAL, getId()) == 1);
//	}
//
//	/**
//	 * 注册账号
//	 */
//	public long Save() {
//		this.role = ROLE_GENERAL;
//		this.score = 0;
//		this.rank = 1;
//		this.options |= SERVICE_WEEKLY_REPORT;
//		this.status = STATUS_PENDING;
//		this.online = OFFLINE;
//		this.pwd = DigestUtils.shaHex(pwd);
//		this.activate_code = RandomStringUtils.randomAlphanumeric(40);
//		long userid = JustSave();
//		//发送账号激活邮件
//		SmtpHelper.SendActivateMail(this);
//		return userid;
//	}
//	/**
//	 * 注册账号,密文密码方式
//	 */
//	public long hash_pwd_save() {
//		this.role_id = 0;
//		this.role = ROLE_GENERAL;
//		this.score = 0;
//		this.rank = 1;
//		this.options |= SERVICE_WEEKLY_REPORT;
//		this.status = STATUS_PENDING;
//		this.online = OFFLINE;
//		this.activate_code = RandomStringUtils.randomAlphanumeric(40);
//		long userid = JustSave();
//		//发送账号激活邮件
//		SmtpHelper.SendActivateMail(this);
//		return userid;
//	}
//	/**
//	 * 注册账号
//	 */
//	public long JustSave() {
//		long id = super.Save();
//		//往好友表增加自己为好友的记录（用于空间首页的动态获取）
//		Friend.AddFriend(this, this.getId(), null, (byte)0, (byte)0);
//		return id;
//	}
//	
//	/**
//	 * 激活账号
//	 * @param code
//	 * @return
//	 */
//	public boolean Activate(String code) {
//		if(this.status != STATUS_PENDING)
//			return true;
//		if(!StringUtils.equalsIgnoreCase(this.getActivate_code(), code))
//			return false;
//
//		String sql = "UPDATE osc_users SET status=?,options=? WHERE id=? AND status=?";
//		if(Evict(QueryHelper.update(sql, STATUS_NORMAL, SERVICE_WEEKLY_REPORT, getId(), STATUS_PENDING)==1)){
//			//发送欢迎留言
//			Msg.SendViaKey(User.WEBMASTER, getId(), "reg.msg", Msg.TYPE_SYSTEM);
//			//操作日志
//			OptLog.newUser(this);
//		}
//		return true;
//	}
//	
//	public boolean UpdateActivateCode() {
//		String sql = "UPDATE osc_users SET activate_code=? WHERE id=?";	
//		setActivate_code(RandomStringUtils.randomAlphanumeric(40));
//		return Evict(QueryHelper.update(sql, getActivate_code() ,getId())==1);
//	}
//	
//	public boolean UpdateBasicInfo() {
//		String sql = "UPDATE osc_users SET name=?,gender=?,province=?,city=?,options=?,nick_name=? WHERE id=?";		
//		return Evict(QueryHelper.update(sql, name,gender,province,city,options,nick_name,getId())==1);
//	}
//
//	public boolean UpdateBasicInfoFromAdmin() {
//		String sql = "UPDATE osc_users SET name=?,gender=?,province=?,city=?,score=?,rank=?,options=?,nick_name=? WHERE id=?";		
//		return Evict(QueryHelper.update(sql, name,gender,province,city,score,rank,options,nick_name,getId())==1);
//	}
//	
//	public boolean UpdateBasicInfoOfMember(){
//		String sql = "UPDATE osc_users SET name=?,gender=?,province=?,city=?,lportrait=? WHERE id=?";
//		return Evict(QueryHelper.update(sql,name,gender,province,city,lportrait,getId())==1);
//	}
//	
//	public boolean UpdatePortrait(String url) {
//		Timestamp curTime = new Timestamp(System.currentTimeMillis());
//		String sql = "UPDATE osc_users SET portrait=?,portrait_update=? WHERE id=?";
//		return Evict(QueryHelper.update(sql, url, curTime, getId())==1);
//	}
//	
//	public boolean UpdateHDPortrait(String url) {
//		Timestamp curTime = new Timestamp(System.currentTimeMillis());
//		String sql = "UPDATE osc_users SET lportrait=?,portrait_update=? WHERE id=?";
//		return Evict(QueryHelper.update(sql, url, curTime, getId())==1);
//	}
//	
//	public boolean UpdateArea(String province, String city) {
//		String sql = "UPDATE osc_users SET province=?,city=? WHERE id=?";
//		return Evict(QueryHelper.update(sql, province, city, getId())==1);
//	}
//	
//	public static List<User> ListAdmins() {
//		return QueryHelper.query(User.class, "SELECT * FROM osc_users WHERE role>100");
//	}
//	
//	/**
//	 * 列出我的好友
//	 * @return
//	 */
//	public List<Long> ListFriends() { return Friend.ListFriendsOfUser(getId()); }
//	public List<Long> ListFans() { return Friend.ListFansOfUser(getId()); }
//	public List<Friend.UserLog> ListFriendStatus(String sort) { return Friend.ListFriendLogOfUser(getId(), sort); }
//	public List<Friend.UserLog> ListFansStatus(String sort) { return Friend.ListFansLogOfUser(getId(), sort); }
//	public static void LoadUserStatus(List<Friend.UserLog> users) { Friend._LoadList(users); }
//	public boolean IsFansOfMe(long user){ return ListFans().contains(user); }
//	public boolean IsFriendOfMe(long user){ return ListFriends().contains(user); }
//	public Friend GetFriend(long friend) { return Friend.GetFriend(getId(), friend); }
//	
//	public static User GetUserByEmail(String email) {
//		if(StringUtils.isBlank(email))
//			return null;
//		email = email.toLowerCase();
//		User user = null;
//		Long uid = CacheManager.get(Long.class, CACHE_USER_EMAILS, email);
//		if(uid == null || uid <= 0){
//			user = QueryHelper.read(User.class, "SELECT * FROM osc_users WHERE email=?", email);
//			if(user != null)
//				CacheManager.set(CACHE_USER_EMAILS, email, user.getId());
//		}
//		else
//			user = Read(uid);
//		return user;
//	}
//	
//	public static List<Long> CheckUsername(String name) {
//		return QueryHelper.query(long.class, "SELECT id FROM osc_users WHERE name=? AND status<>? ORDER BY rank DESC,score DESC", name, User.STATUS_PENDING);
//	}
//
//	public static boolean ExistUsername(String name) {
//		List<Long> uids = CheckUsername(name);
//		return uids!=null&&uids.size()>0;
//	}
//	
//	public static User Login(String email, String pwd, String ip) {
//		if(StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) return null;
//		User user = GetUserByEmail(email);
//		if((user!=null && StringUtils.equalsIgnoreCase(user.pwd, DigestUtils.shaHex(pwd)))){
//			if(user.getStatus() == User.STATUS_PENDING){
//				User duser = User.ReadNoCache(user.getId());
//				if(duser.getStatus() == User.STATUS_PENDING){
//					String tip = ResourceUtils.getString("error", "user_not_activated", user.getEmail());
//					throw new ActionException(tip);
//				}
//				user.Evict(true);
//			}
//			if(ip.length()>16)
//				ip = "127.0.0.1";
//			Timestamp curTime = new Timestamp(System.currentTimeMillis());
//			QueryHelper.update(ResourceUtils.sql("U_POST_LOGIN"), ONLINE, curTime, ip, user.getId());
//			user.setOnline(ONLINE);
//			user.setThis_login_ip(ip);
//			user.setThis_login_time(curTime);
//			User.setCache(user.CacheRegion(), user.getId(), user);			
//			return user;
//		}
//		return null;
//	}
//	/**
//	 * 密文密码登录
//	 * @param email
//	 * @param pwd
//	 * @param ip
//	 * @return
//	 */
//	public static User hash_pwd_login(String email, String pwd, String ip) {
//		if(StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) return null;
//		User user = GetUserByEmail(email);
//		if((user!=null && StringUtils.equalsIgnoreCase(user.pwd, pwd))){
//			if(user.getStatus() == User.STATUS_PENDING){
//				String tip = ResourceUtils.getString("error", "user_not_activated", user.getEmail());
//				throw new ActionException(tip);
//			}
//			if(ip.length()>16)
//				ip = "127.0.0.1";
//			Timestamp curTime = new Timestamp(System.currentTimeMillis());
//			QueryHelper.update(ResourceUtils.sql("U_POST_LOGIN"), ONLINE, curTime, ip, user.getId());
//			user.setOnline(ONLINE);
//			user.setThis_login_ip(ip);
//			user.setThis_login_time(curTime);
//			return user;
//		}
//		return null;
//	}
//	
//	/**
//	 * 返回当前登录用户的资料
//	 * @param req
//	 * @return
//	 */
//	public static User GetLoginUser(HttpServletRequest req) {
//		User loginUser = (User)req.getAttribute(G_USER);
//		if(loginUser == null){
//			IUser cookie_user = RequestContext.get().getUserFromCookie();
//			if(cookie_user == null) return null;
//			User user = User.Read(cookie_user.getId());
//			if(user != null && StringUtils.equalsIgnoreCase(user.getPwd(), cookie_user.getPwd()) && !user.IsBlocked()) {
//				user.PostLogin(req);
//				return user;
//			}
//		}
//		return loginUser;
//	}
//	
//	public void PostLogin(HttpServletRequest req) {
//		boolean is_today = (getThis_login_time()==null)?false:DateTimeTool.is_today(getThis_login_time());
//		//超过半个小时
//		if(online != ONLINE || getThis_login_time()==null || !is_today || (System.currentTimeMillis()-getThis_login_time().getTime())>3600000){
//			String ip = RequestUtils.getRemoteAddr(req);
//			Timestamp curTime = new Timestamp(System.currentTimeMillis());
//			QueryHelper.update(ResourceUtils.sql("U_POST_LOGIN"), ONLINE, curTime, ip, getId());
//			this.setOnline(ONLINE);
//			this.setThis_login_ip(ip);
//			this.setThis_login_time(curTime);
//			setCache(CacheRegion(), getId(), this);
//			//Evict(true);
//		}
//		req.setAttribute(G_USER, this);
//	}
//	
//	/**
//	 * 修改密码
//	 * @param userid
//	 * @param newPwd
//	 * @return
//	 */
//	public static boolean ChangePassword(long userid, String newPwd) {
//		String new_code = RandomStringUtils.randomAlphanumeric(40);
//		String sql = "UPDATE osc_users SET pwd=?,activate_code=? WHERE id=?";
//		return new User(userid).Evict(QueryHelper.update(sql, DigestUtils.shaHex(newPwd),new_code,userid) == 1);
//	}
//	
//	/**
//	 * 修改校验码
//	 */
//	public void ChangeActivateCode() {
//		String new_code = RandomStringUtils.randomAlphanumeric(40);
//		String sql = "UPDATE osc_users SET activate_code=? WHERE id=?";
//		Evict(QueryHelper.update(sql, new_code, getId())==1);
//		this.activate_code = new_code;
//	}
//	
//	public List<Long> ListMyFavorites(int type) {
//		return Favorite.ListMyFavorites(getId(), type);
//	}
//	
//	public boolean HasFavorite(long obj_id, int type) {
//		return ListMyFavorites(type).contains(obj_id);
//	}
//	
//	public List<String> ListFavorTags(int type){
//		return Favorite.ListFavorTags(getId(), type);
//	}
//	
//	public List<Favorite> ListFavoritesWithTag(int type,String tag) {
//		return Favorite.ListMyFavorites(getId(), type, tag);
//	}
//	
//	public List<String> ListTags(long obj_id,int type){
//		return Favorite.ListSourceTags(getId(), obj_id, type);
//	}
//	
//	public ProjectExp ExpOfProject(long prj_id) {
//		return ProjectExp.ExpOfProject(getId(), prj_id);
//	}
//	
//	public List<Long> ListUsedProjects() {
//		return ProjectExp.ListProjects(getId());
//	}
//	
//	public List<Long> ListUsedProjects(int level) {
//		return ProjectExp.ListProjects(getId(), (byte)level);
//	}
//	
//	public List<ProjectExp> ListMyExps() {
//		return ProjectExp.ListExps(getId());
//	}
//
//	public User(){}
//	public User(long id){super.setId(id);}
//	
//	public static User Read(long id) { return (User)INSTANCE.Get(id); }
//	public static User ReadNoCache(long id) {
//		INSTANCE.Evict(id);
//		return Read(id);
//	}
//
//	public List<Long> GetRoleIds() {
//		return roleIds;
//	}
//	
//	public static long totalCount(String searchField, String searchKeyword){
//		String condition = "";
//		if (StringUtils.isNotBlank(searchKeyword)) {
//			final String keyword = searchKeyword.trim();
//			if (StringUtils.isBlank(searchField)){
//				//查询所有字段
//				condition = "name like '%"+keyword+"%' or nick_name like '%"+keyword+"%' or email like '%"+keyword+"%'";
//			} else {
//				//查询指定字段
//				condition =  searchField + " like '%"+keyword+"%'";
//			}
//		}
//		if (StringUtils.isNotBlank(condition))
//			return INSTANCE.TotalCount(condition);
//		return INSTANCE.TotalCount();
//	}

}
