package controller;

/**
 * DWZ的callback json对象
 * 
 * @author weiwei
 * 
 */
public class DWZCallback {
	
	private String statusCode = "200";
	private String errorCode = "login_required";//错误代码
	private String message = "";
	private String navTabId = "";
	private String forwardUrl = "";
	private String callbackType = "reloadTab";//closeCurrent
	private String title = "";

	public static DWZCallback me(){
		return new DWZCallback();
	}
	
	public DWZCallback success(){
		return status("200").msg("操作成功");
	}
	
	public DWZCallback fail(){
		return status("300").msg("操作失败");
	}
	
	public DWZCallback errorCode(String code){
		this.errorCode = code;
		return this;
	}
	
	public DWZCallback login_required(String msg){
		return status("301").errorCode("login_required").msg(msg);
	}
	
	public DWZCallback status(String code){
		this.statusCode = code;
		return this;
	}
	
	public DWZCallback msg(String message){
		this.message = message;
		return this;
	}
	
	public DWZCallback tab(String tabID){
		this.navTabId = tabID;
		return this;
	}
	
	public DWZCallback reload(String url) {
		return type("reloadTab").url(url).tab(url);
	}
	
	public DWZCallback close() {
		return type("closeCurrent");
	}
	
	public DWZCallback url(String forwardUrl) {
		this.forwardUrl = forwardUrl;
		return this;
	}
	
	public DWZCallback type(String callbackType) {
		this.callbackType = callbackType;
		return this;
	}
	
	public DWZCallback title(String title) {
		this.title = title;
		return this;
	}
	
	public DWZCallback() {
	}

	public DWZCallback(String error) {
		init("300", error, "", "", "reloadTab", "");
	}

	public DWZCallback(String statusCode, String message, String navTabId,
			String forwardUrl, String callbackType, String title) {
		this.init(statusCode, message, navTabId, forwardUrl, callbackType,
				title);
	}
	
	public void init(String statusCode, String message, String navTabId,
			String forwardUrl, String callbackType, String title) {
		if (statusCode == null) {
			statusCode = "200";
		} else {
			this.statusCode = statusCode;
		}
		if (message == null) {
			if ("200".equals(statusCode)) {
				message = "操作成功";
			} else if ("300".equals(statusCode)) {
				message = "操作失败";
			}
		} else {
			this.message = message;
		}
		this.navTabId = navTabId;
		this.forwardUrl = forwardUrl;
		this.title = title;

		this.callbackType = callbackType;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}

	public String getNavTabId() {
		return navTabId;
	}

	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "{\"statusCode\":\""+statusCode + "\", \"errorCode\":\""
				+ errorCode + "\", \"message\":\"" + message + "\", \"navTabId:\"" + navTabId
				+ "\", \"forwardUrl\":\"" + forwardUrl + "\", \"callbackType\":\""
				+ callbackType + "\", \"title\":\"" + title + "\"}";
	}
	
	
}
