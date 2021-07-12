package cn.yeegro.nframe.comm.code.functor;

public interface NET_SENDER_FUNCTOR {

	<T> void operator(int nSockIndex, T msg);
}
