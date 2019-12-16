package com.noahframe.nfcore.iface.module;

import com.noahframe.api.file.PluginContextUtil;

/**
 * @Author:zoocee
 * @Date:2019/1/22 11:11
 */
public class NFDataBase<T> {

    private String mDataBaseName;
    private T mDatabase=null;

    public NFDataBase(String strXML, String strName)
    {
        PluginContextUtil _ctx=new PluginContextUtil();
        _ctx.initContext("classpath*:"+strXML);

        if (_ctx.getBean(strName)!=null) {
            T m_DataBase=(T)_ctx.getBean(strName);
            mDataBaseName=strName;
            setDatabase((T) _ctx.getBean(strName));
        }
    }

    public T getDatabase() {
        return mDatabase;
    }

    public void setDatabase(T mDatabase) {
        this.mDatabase = mDatabase;
    }
}
