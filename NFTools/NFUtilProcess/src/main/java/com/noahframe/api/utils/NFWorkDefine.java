package com.noahframe.api.utils;

/**
 * @Author:zoocee
 * @Date:2019/1/7 11:42
 */
public enum NFWorkDefine {

    NF_WORK_ACTOR(1,"角色工作ID")
    ;
    private int _value;
    private String _url;
    private String _hint=null;

    NFWorkDefine(int value, String... strs) {
        _value = value;
        _url=strs[0];
    }

    public int value() {
        return _value;
    }

    public  String url(){
        return _url;
    }

    public static NFWorkDefine get(int nType) {
        for (int i = 0; i < NFWorkDefine.values().length; i++) {
            NFWorkDefine val = NFWorkDefine.values()[i];
            if (val.value()-nType==0) {
                return val;
            }
        }
        return null;
    }
}
