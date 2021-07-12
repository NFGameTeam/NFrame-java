package cn.yeegro.nframe.comm.code.module;


import org.pf4j.ExtensionPoint;

public interface NFIModule<T> extends ExtensionPoint {

    boolean Awake();
    boolean Init();
    boolean  AfterInit();
    boolean CheckConfig();
    boolean ReadyExecute();
    boolean  Execute();
    boolean BeforeShut();
    boolean Shut();
    boolean Finalize();
    boolean OnReloadPlugin();
}
