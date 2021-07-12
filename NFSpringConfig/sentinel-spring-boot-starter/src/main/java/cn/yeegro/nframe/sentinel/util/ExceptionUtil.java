package cn.yeegro.nframe.sentinel.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class ExceptionUtil {
	public static String handleException(BlockException ex) {
        return "exception handle: " + ex.getClass().getCanonicalName() ;
    }
}
