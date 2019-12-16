package com.noahframe.plugins.log;

import com.noahframe.loader.NFPluginManager;
import com.noahframe.nfcore.api.plugin.Extension;
import com.noahframe.nfcore.iface.NFIPluginManager;
import com.noahframe.nfcore.iface.module.NFGUID;
import com.noahframe.nfcore.iface.module.NFILogModule;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;





@Extension
public class NFLogModule extends NFILogModule {

	public static int idx = 0;
	public long mnLogCountTotal;
	private NFIPluginManager pPluginManager;

	private Logger LOG = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	
	private static NFLogModule SingletonPtr=null;
	
	public static NFLogModule GetSingletonPtr()
	{
		if (null==SingletonPtr) {
			 SingletonPtr=new NFLogModule();
			 return SingletonPtr;
		}
		else {
			return SingletonPtr;
		}
	}

	public boolean CheckLogFileExist(String filename) {
		String fn = filename + "." + (++idx);
		File file = new File(fn);
		if (file.exists()) {
			return CheckLogFileExist(filename);
		}

		return false;
	}

	public void rolloutHandler(String filename, Long size) {
		if (!CheckLogFileExist(filename)) {
			String fn = filename + "." + idx;
			rename(filename, fn);
		}
	}

	protected static void rename(String oldname, String newname) {
		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(oldname);
			File newfile = new File(newname);
			if (!oldfile.exists()) {
				return;// 重命名文件不存在
			}

			if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				System.out.println(newname + "已经存在！");
			else {
				oldfile.renameTo(newfile);
			}
		} else {
			System.out.println("新文件名和旧文件名相同...");
		}
	}

	public NFLogModule() {
		pPluginManager = NFPluginManager.GetSingletonPtr();
	}

	@Override
	public boolean Awake() {
		mnLogCountTotal = 0;

		String strLogConfigName = pPluginManager.GetLogConfigName();
		if (strLogConfigName.isEmpty()) {
			strLogConfigName = pPluginManager.GetAppName();
		}

		String strAppLogName = "";
		strAppLogName = "logconfig" + File.separator + strLogConfigName
				+ ".conf";

		System.out.println("LogConfig: " + strAppLogName);

		return true;
	}

	@Override
	public boolean Init() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean AfterInit() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean CheckConfig() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ReadyExecute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Execute() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean BeforeShut() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Shut() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Finalize() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean OnReloadPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean LogElement(NF_LOG_LEVEL nll, NFGUID ident,
			String strElement, String strDesc, String func, int line) {
		if (line > 0) {
			Log(nll, "[ELEMENT] Indent[%s] Element[%s] %s %s %d",
					ident.ToString(), strElement, strDesc, func, line);
		} else {
			Log(nll, "[ELEMENT] Indent[%s] Element[%s] %s", ident.ToString(),
					strElement, strDesc);
		}

		return true;
	}

	@Override
	public boolean LogProperty(NF_LOG_LEVEL nll, NFGUID ident,
			String strProperty, String strDesc, String func, int line) {
		if (line > 0) {
			Log(nll, "[PROPERTY] Indent[%s] Property[%s] %s %s %d",
					ident.ToString(), strProperty, strDesc, func, line);
		} else {
			Log(nll, "[PROPERTY] Indent[%s] Property[%s] %s", ident.ToString(),
					strProperty, strDesc);
		}

		return true;
	}

	@Override
	public boolean LogObject(NF_LOG_LEVEL nll, NFGUID ident, String strDesc,
			String func, int line) {
		if (line > 0) {
			Log(nll, "[OBJECT] Indent[%s] %s %s %d", ident.ToString(), strDesc,
					func, line);
		} else {
			Log(nll, "[OBJECT] Indent[%s] %s", ident.ToString(), strDesc);
		}

		return true;
	}

	@Override
	public boolean LogRecord(NF_LOG_LEVEL nll, NFGUID ident,
			String strRecord, String strDesc, int nRow, int nCol, String func,
			int line) {
		if (line > 0) {
			Log(nll, "[RECORD] Indent[%s] Record[%s] Row[%d] Col[%d] %s %s %d",
					ident.ToString(), strRecord, nRow, nCol, strDesc, func,
					line);
		} else {
			Log(nll, "[RECORD] Indent[%s] Record[%s] Row[%d] Col[%d] %s",
					ident.ToString(), strRecord, nRow, nCol, strDesc);
		}

		return true;
	}

	@Override
	public boolean LogRecord(NF_LOG_LEVEL nll, NFGUID ident,
			String strRecord, String strDesc, String func, int line) {
		if (line > 0) {
			Log(nll, "[RECORD] Indent[%s] Record[%s] %s %s %d",
					ident.ToString(), strRecord, strDesc, func, line);
		} else {
			Log(nll, "[RECORD] Indent[%s] Record[%s] %s", ident.ToString(),
					strRecord, strDesc);
		}

		return true;
	}

	@Override
	public boolean LogNormal(NF_LOG_LEVEL nll, NFGUID ident, String strInfo,
			int nDesc, String func, int line) {

		if (line > 0) {
			Log(nll, "Indent[%s] %s %d %s %d", ident.ToString(), strInfo,
					nDesc, func, line);
		} else {
			Log(nll, "Indent[%s] %s %d", ident.ToString(), strInfo, nDesc);
		}

		return true;
	}

	@Override
	public boolean LogNormal(NF_LOG_LEVEL nll, NFGUID ident, String strInfo,
			String strDesc, String func, int line) {

		if (line > 0) {
			Log(nll, "Indent[%s] %s %s %s %d", ident.ToString(), strInfo,
					strDesc, func, line);
		} else {
			Log(nll, "Indent[%s] %s %s", ident.ToString(), strInfo, strDesc);
		}

		return true;
	}

	@Override
	public boolean LogNormal(NF_LOG_LEVEL nll, NFGUID ident,
			String stream, String func, int line) {
		if (line > 0) {
			Log(nll, "Indent[%s] %s %s %d", ident.ToString(),
					stream.toString(), func, line);
		} else {
			Log(nll, "Indent[%s] %s", ident.ToString(), stream.toString());
		}

		return true;
	}

	protected boolean Log(NF_LOG_LEVEL nll, String format, Object... args) {

		mnLogCountTotal++;
		String szBuffer = String.format(format, args);

		String log = mnLogCountTotal + " | " + pPluginManager.GetAppID()
				+ " | " + szBuffer;

		switch (nll) {
		case NLL_DEBUG_NORMAL: {
			LOG.debug(log);
		}
			break;
		case NLL_INFO_NORMAL: {
			LOG.info(log);
		}
			break;
		case NLL_WARING_NORMAL: {
			LOG.warn(log);
		}
			break;
		case NLL_ERROR_NORMAL: {
			LOG.error(log);
		}
			break;
		case NLL_FATAL_NORMAL: {
			LOG.fatal(log);
		}
			break;
		default: {
			LOG.info(log);
		}
			break;
		}

		return true;
	}

	public boolean LogDebugFunctionDump(NFGUID ident, int nMsg, String strArg,
			String func, int line) {
		// #ifdef NF_DEBUG_MODE
		LogNormal(NF_LOG_LEVEL.NLL_WARING_NORMAL, ident, strArg + "MsgID:",
				nMsg, func, line);
		// #endif
		return true;
	}

	public boolean ChangeLogLevel(String strLevel) {
		Level level = Level.toLevel(strLevel);

		return true;
	}

	@Override
	public StackTraceElement CurrTrace() {
		StackTraceElement[] trace = new Throwable().getStackTrace();
		// 下标为0的元素是上一行语句的信息, 下标为1的才是调用LineCurr的地方的信息
		StackTraceElement tmp = trace[1];

		return tmp;
	}

}
