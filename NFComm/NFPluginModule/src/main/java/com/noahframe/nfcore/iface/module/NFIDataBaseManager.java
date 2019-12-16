package com.noahframe.nfcore.iface.module;

/**
 * @Author:zoocee
 * @Date:2019/1/22 11:07
 */
public abstract class NFIDataBaseManager implements NFIModule  {

    public abstract void LoadDataBase();
    public abstract void LoadDataBase(String strDataBase);

    public abstract <T> T FindDataBase(String strDataBase);

}
