package com.noahframe.nfweb;

import com.noahframe.api.file.PluginContextUtil;

/**
 * @Author:zoocee
 * @Date:2019/1/29 23:29
 */
public class NFSpringLoader {

  private   PluginContextUtil _ctx=null;

    public boolean Loader(String strXML)
    {
        _ctx=new PluginContextUtil();
        try {
            _ctx.initContext("classpath*:"+strXML);

            return true;
        }
        catch (Exception e)
        {
            _ctx=null;
            return false;
        }
    }

    public PluginContextUtil GetCtx()
    {
        return _ctx;
    }

}
