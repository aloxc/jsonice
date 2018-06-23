package com.github.aloxc.jsonice.io;

import com.github.aloxc.jsonice.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;



public class JsonResponse{
	private String json = null;
	public String getMessage( ) {
		return message;
	}
	public int getCode( ) {
		return code;
	}
	public boolean isSuccess( ) {
		return success == 1;
	}
	
	public Map<Object, Object> getDataMap( ) {
		return dataMap;
	}

	private String message = null;
	private int code = 0;
	private int success = 0;
	private Map<Object, Object> dataMap = null;
	private JsonResponse(){
	}
	private JsonResponse(String response){
		this.json = response;
		parseResult();
	}
	public static JsonResponse newInstance(String response) {
		return new JsonResponse(response);
	}
	public enum SUCCESS{
		OK{
			public int value(){
				return 1;
			};
		},FAILURE{
			@Override
			public int value( ) {
				return 0;
			}
		};
		abstract public int value();
	};
	
	public enum CODE{
		OK{
			@Override
			public int value( ) {
				return 0;
			}

			@Override
			public String getMessage( ) {
				return "操作成功";
			}
		},
		APP_OK { //APP客户端规范要求
			@Override
			public int value( ) {
				return 1;
			}
			
			@Override
			public String getMessage( ) {
				return "操作成功";
			}
		},PARAMETER_ERROR{
			public int value(){
				return 3;
			}

			@Override
			public String getMessage( ) {
				return "参数错误,";
			};
		},NOT_LOGGED{
			@Override
			public int value( ) {
				return 120;
			}

			@Override
			public String getMessage( ) {
				return "未登录";
			}
		},NOT_ACTIVATE{

			@Override
			public int value( ) {
				return 121;
			}

			@Override
			public String getMessage( ) {
				return "未激活";
			}
			
		},WITHOUT_PERMISSION{

			@Override
			public int value( ) {
				return 122;
			}

			@Override
			public String getMessage( ) {
				return "没有权限";
			}
		},FAILURE{
			@Override
			public int value( ) {
				return -1;
			}

			@Override
			public String getMessage( ) {
				return "操作失败";
			}
		},USER_INFO_ERROR{
			@Override
			public int value( ) {
				return -303;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,获取用户信息失败";//抱歉,用户ip非法.
			}
		},USER_IP_ERROR{
			@Override
			public int value( ) {
				return -102;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,用户ip非法.";//
			}
		},USER_LOGIN_ERROR{
			@Override
			public int value( ) {
				return -103;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,请先登录再操作.";//
			}
		},PERMISSION_ERROR{
			@Override
			public int value( ) {
				return -107;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,版块类型错误.";//
			}
		},ITEM_INFO_ERROR{
			@Override
			public int value( ) {
				return -108;
			}

			@Override
			public String getMessage( ) {
				return "抱歉, 获取版块信息失败.";//
			}
		},ITEM_NAME_ERROR{
			@Override
			public int value( ) {
				return -203;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,名称长度应为4-16位字符且只能由中文,字母,数字组成!";//
			}
		},
		
		ITEM_NAME_REPICK_ERROR{
			@Override
			public int value( ) {
				return -201;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,该部落名称已经被使用.";//
			}
		},GET_ARTICLE_INFO_ERROR{
			@Override
			public int value( ) {
				return -306;
			}

			@Override
			public String getMessage( ) {
				return "抱歉,获取帖子信息失败!";//
			}
		},GET_REPLY_INFO_ERROR{
			@Override
			public int value( ) {
				return -306;
			}
			
			@Override
			public String getMessage( ) {
				return "抱歉,获取回复信息失败!";//
			}
		},USER_NOT_ACTIVE_ERROR {
			@Override public int value() {
				return -307;
			}
			@Override public String getMessage() {
				return "抱歉，您的ID尚未激活";
			}
		}, USER_NOT_IN_ITEM {
			@Override public String getMessage() {
				return "抱歉,您不是版块成员";
			}
			@Override public int value() {
				return -309;
			}
		}, REPEAT_UP_REPLY {
			@Override public String getMessage() {
				return "您已点赞了哦！";
			}
			@Override public int value() {
				return -311;
			}
		}, USER_IS_CANCEL_ERROR {
			@Override
			public int value() {
				return -312;
			}
			@Override
			public String getMessage() {
				return "抱歉，您的ID已经被注销！";
			}
		}, USER_IS_DENY_ERROR {
			@Override
			public int value() {
				return -313;
			}
			@Override
			public String getMessage() {
				return "抱歉，您的ID已经被封杀！";
			}			
		}, PROTECT_OUT_DEADLINE_ERROR {
			@Override
			public int value() {
				return -314;
			}
			@Override
			public String getMessage() {
				return "抱歉，您的帖子守护状态已过期,请续期后重新查询！";
			}			
		}, LIMIT_UP_BY_SELF {
			@Override public String getMessage() {
				return "抱歉，您不能给自己点赞！";
			}
			@Override public int value() {
				return -315;
			}
		}, LIMIT_UP_BY_COUNT {
			@Override public String getMessage() {
				return "抱歉，您每天只能点赞1000次哦！";
			}
			@Override public int value() {
				return -316;
			}
		},
		//=================涯门=====================//
		YAMEN_INTFACE_ERROR{
			@Override
			public int value( ) {
				return 404;
			}

			@Override
			public String getMessage( ) {
				return "找不到服务.";//
			}
		},YAMEN_PARAM_ERROR{
			@Override
			public int value( ) {
				return 400;
			}

			@Override
			public String getMessage( ) {
				return "请求参数错误.";//
			}
		},YAMEN_SYS_ERROR{
			@Override
			public int value( ) {
				return 500;
			}

			@Override
			public String getMessage( ) {
				return "服务器内部错误.";//
			}
		},YAMEN_OK{
			@Override
			public int value( ) {
				return 200;
			}

			@Override
			public String getMessage( ) {
				return "操作成功.";//
			}
		};
		abstract public int value();
		abstract public String getMessage();
	}
	
	
	/** 获得错误代码 */
	private static HashMap<Integer, CODE> map = new HashMap<Integer, CODE>();
	static {
		for (CODE err : CODE.values())
			map.put(err.value(), err);
	}
	
	public static CODE getByCode(int code) {
		return map.get(code);
	}
	
	@SuppressWarnings("static-access")
	public static String toResult(SUCCESS success, CODE code, String parameter, Object result) {
		LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
		ret.put("success", String.valueOf(success.value()));
		ret.put("message", code.getMessage().toString() + (parameter != null && code == code.PARAMETER_ERROR ? " parameter:" + parameter : ""));
		ret.put("data", result == null ? new HashMap<Object, Object>() : result);
		ret.put("code", String.valueOf(code.value()));
		return JsonUtil.toJson(ret);
	}
	
	/**
	 * 返回错误JSON串
	 * @param code
	 * @param errMsg
	 * @return
	 */
	public static String toErrorResult(CODE code, String errMsg){
		LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
		ret.put("success", String.valueOf(SUCCESS.FAILURE.value()));//
		ret.put("message", errMsg);
		ret.put("data", new HashMap<Object, Object>(1));
		ret.put("code", String.valueOf(code.value()));
		return JsonUtil.toJson(ret);
	}
	
    /**
     * 用于涯门V3接口返回
     * @param success
     * @param code
     * @param parameter
     * @param result
     * @return
     */
	public static String toResultToYamen(SUCCESS success,CODE code,String parameter,Object result){
		LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
		ret.put("success", String.valueOf(success.value()));//
		ret.put("message", parameter);
		ret.put("data", result == null ? new HashMap<Object, Object>() : result);
		ret.put("code", String.valueOf(code.value()));
		return JsonUtil.toJson(ret);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseResult() {

		// 判断是否为空
		if (StringUtils.isEmpty(json)) {
			return;
		}

		try {
			// 将Json字符串转成Map
			Map responseMap = JsonUtil.toBean(json,HashMap.class);

			// 获取success
			if (responseMap.containsKey("success")) {
				success = Integer.parseInt(String.valueOf(responseMap.get("success")));
			}

			// 获取code
			if (responseMap.containsKey("code")) {
				code = Integer.parseInt(String.valueOf(responseMap.get("code")));
			}

			// 获取message
			if (responseMap.containsKey("message")) {
				message = String.valueOf(responseMap.get("message"));
			}

			// 获取data
			if (responseMap.containsKey("data")) {
				dataMap = (Map) responseMap.get("data");
			}
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) {
		/*System.out.println(toResult(SUCCESS.OK, CODE.OK, null, null));
		System.out.println(toResult(SUCCESS.OK, CODE.PARAMETER_ERROR, null, new ArrayList<String>()));
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aa", "aa");
		map.put("bb", "11");
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("aa", "aa");
		map1.put("bb", "11");
		map1.put("dd", new ArrayList());
		map.put("cc", map1);
		
		list.add(map);
		System.out.println(toResult(SUCCESS.OK, CODE.OK, null, map1));*/
		// 错误列表  ==> 形成文档
//		for (CODE err : CODE.values()) {
//			System.out.println(err.value() + "\t" + err.getMessage());
//		}
		System.out.println(toErrorResult(CODE.FAILURE, "xxx"));
	}
	
}
