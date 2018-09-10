package com.noahframe.nfcore.iface.module;

public interface NFIModule extends NFIExtensionPoint {

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
