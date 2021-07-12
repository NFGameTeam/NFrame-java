package cn.yeegro.nframe.common.exception.controller;

import cn.yeegro.nframe.common.exception.BaseException;

/**


 */

public class ControllerException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1412104290896291466L;

	public ControllerException(String msg) {
		super(msg);
	}

	public ControllerException(Exception e) {
		this(e.getMessage());
	}

}
