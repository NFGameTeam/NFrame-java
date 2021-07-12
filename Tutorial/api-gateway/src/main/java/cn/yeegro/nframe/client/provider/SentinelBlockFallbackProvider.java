package cn.yeegro.nframe.client.provider;

import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.BlockResponse;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.ZuulBlockFallbackProvider;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author Administrator
 * @version V1.0.0
 * @desc 限流后调用这里的逻辑
 * @date 2019/12/26 0026 23:56
 */

public class SentinelBlockFallbackProvider implements ZuulBlockFallbackProvider {

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public BlockResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof BlockException) {
            return new BlockResponse(429, "open capacity platform tip : Sentinel block exception", route);
        } else {
            return new BlockResponse(500, "open capacity platform tip : System Error", route);
        }
    }
}
