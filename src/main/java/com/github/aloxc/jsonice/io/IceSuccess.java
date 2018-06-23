package com.github.aloxc.jsonice.io;

/**
 * ice 请求成功否的枚举类，
 * @author leerohwa@gmail.com
 * @date 2016年8月5日
 */
public enum IceSuccess {
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
}
