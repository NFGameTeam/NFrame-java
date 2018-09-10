package com.noahframe.nfcore.iface.functor;

public interface NET_SENDER_FUNCTOR {

	<T> void operator(int nSockIndex, T msg);
}
