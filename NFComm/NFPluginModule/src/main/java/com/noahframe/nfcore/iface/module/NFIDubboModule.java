package com.noahframe.nfcore.iface.module;

import com.noahframe.nfcore.iface.functor.SERVICE_ERFERENCE_FUNCTOR;

/**
 * @Author:zoocee
 * @Date:2019/9/23 21:14
 */
public abstract class NFIDubboModule  implements NFIModule  {

    public abstract int AddErferenceFunctor(String ServiceID, SERVICE_ERFERENCE_FUNCTOR functor);

    public abstract <T> T GetProviderService(String strBeanName);

    public abstract <T> T GetErferenceService(String strBeanName);

}
