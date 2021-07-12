/**   
* @Title: ${name} 
* @Package ${package_name} 
* @Description: 略
* @author zoecee yideal_formula@126.com  
* @date 2017.7.6 
* @version V1.0   
*/
package cn.yeegro.nframe.comm.module;


import cn.yeegro.nframe.comm.code.api.NFGUID;
import cn.yeegro.nframe.comm.code.module.NFIModule;
import lombok.extern.slf4j.Slf4j;

public abstract class NFILogModule implements NFIModule {

	public enum NF_LOG_LEVEL {
		NLL_DEBUG_NORMAL(0), NLL_INFO_NORMAL(1), NLL_WARING_NORMAL(2), NLL_ERROR_NORMAL(
				3), NLL_FATAL_NORMAL(4);
		private int _value;

		private NF_LOG_LEVEL(int value) {
			_value = value;
		}

		public int value() {
			return _value;
		}

		public static NF_LOG_LEVEL get(int ntype) {
			for (int i = 0; i < NF_LOG_LEVEL.values().length; i++) {
				NF_LOG_LEVEL val = NF_LOG_LEVEL.values()[i];
				if (val.value() == ntype) {
					return val;
				}
			}
			return null;
		}
	};

	public abstract boolean LogElement(NF_LOG_LEVEL nll, NFGUID ident,
                                       String strElement, String strDesc, String func, int line);

	public abstract boolean LogProperty(NF_LOG_LEVEL nll, NFGUID ident,
                                        String strProperty, String strDesc, String func, int line);

	public abstract boolean LogObject(NF_LOG_LEVEL nll, NFGUID ident,
                                      String strDesc, String func, int line);

	public abstract boolean LogRecord(NF_LOG_LEVEL nll, NFGUID ident,
                                      String strRecord, String strDesc, int nRow, int nCol, String func,
                                      int line);

	public abstract boolean LogRecord(NF_LOG_LEVEL nll, NFGUID ident,
                                      String strRecord, String strDesc, String func, int line);

	public abstract boolean LogNormal(NF_LOG_LEVEL nll, NFGUID ident,
                                      String strInfo, int nDesc, String func, int line);

	public abstract boolean LogNormal(NF_LOG_LEVEL nll, NFGUID ident,
                                      String strInfo, String strDesc, String func, int line);

	public abstract boolean LogNormal(NF_LOG_LEVEL nll, NFGUID ident,
                                      String stream, String func, int line);

	public abstract StackTraceElement CurrTrace();


}
