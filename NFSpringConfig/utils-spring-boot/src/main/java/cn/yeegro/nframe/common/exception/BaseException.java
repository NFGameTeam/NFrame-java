package cn.yeegro.nframe.common.exception;

/**
 * 基本异常，系统定义的所有异常都需要继承这个基本类


 */
public class BaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7859712770754900356L;

	public BaseException(String msg) {
	  super(msg);
	}

	public BaseException(Exception e){
	  this(e.getMessage());
	}
}
