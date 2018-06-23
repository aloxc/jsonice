package com.github.aloxc.jsonice.io;

/**
 * ice请求状态枚举类
 * @author leerohwa@gmail.com
 * @date 2016年8月5日
 */
public enum IceCode {
	OK{
		@Override
		public int value( ) {
			return 0;
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
			return "参数错误.";
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

	},TYPE_ERROR{
		@Override
		public int value( ) {
			return -106;
		}

		@Override
		public String getMessage( ) {
			return "抱歉,请选择分类.";//
		}


	};
	abstract public int value();
	abstract public String getMessage();
}
