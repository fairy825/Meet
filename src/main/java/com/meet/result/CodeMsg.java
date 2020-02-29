package com.meet.result;

public class CodeMsg {
	
	private int code;
	private String msg;
	
	//通用的错误码
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
	public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
	public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问太频繁！");
	public static CodeMsg NOT_LOGIN= new CodeMsg(500105, "登录信息过期，请重新登录");
	//登录注册模块 5002XX
	public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
	public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "用户名或登录密码不能为空");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
	public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
	public static CodeMsg MOBILE_EXIST = new CodeMsg(500216, "手机号已注册过");
	//用户模块 5003XX
	public static CodeMsg ORIGIN_PASSWORD_EMPTY = new CodeMsg(500301, "原密码不能为空");
	public static CodeMsg NEW_PASSWORD_EMPTY = new CodeMsg(500302, "新密码不能为空");
	
	
	//订单模块 5004XX
	public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");
	public static CodeMsg BOOK_HAS_ORDERED = new CodeMsg(500401, "已经预订过该书");
	public static CodeMsg HAS_REVIEWED = new CodeMsg(500402, "已评价过该书");
	public static CodeMsg CANNOT_REVIEWED = new CodeMsg(500403, "没有评价权限");
	public static CodeMsg WRONG_VERIFY_CODE = new CodeMsg(500404, "验证码错误");

	//秒杀模块 5005XX
	public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
	public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
	public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "预订失败");
	public static CodeMsg BOOK_EMPTY = new CodeMsg(500503, "库存不足");

	
	private CodeMsg( ) {
	}
			
	private CodeMsg( int code,String msg ) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
