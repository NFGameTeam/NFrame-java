package cn.yeegro.nframe.user.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import cn.yeegro.nframe.common.web.Result;
import cn.yeegro.nframe.sentinel.flow.FlowHelper;
import cn.yeegro.nframe.sentinel.flow.FlowType;
import cn.yeegro.nframe.sentinel.flow.Flower;
import cn.yeegro.nframe.sentinel.flow.common.TimeUtil;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Sentinel测试")
public class SysSentinelContrller {

    private FlowHelper flowHelper = new FlowHelper(FlowType.Minute);

    @GetMapping("/test")
    public Result<Flower> testApi() {
        try {
            long startTime = TimeUtil.currentTimeMillis();
            // 业务逻辑
            Thread.sleep(1000);
            // 计算耗时
            long rt = TimeUtil.currentTimeMillis() - startTime;
            flowHelper.incrSuccess(rt);
            Flower flower = flowHelper.getFlow(FlowType.Minute);
            System.out.println("总请求数:" + flower.total());
            System.out.println("成功请求数:" + flower.totalSuccess());
            System.out.println("异常请求数:" + flower.totalException());
            System.out.println("平均请求耗时:" + flower.avgRt());
            System.out.println("最大请求耗时:" + flower.maxRt());
            System.out.println("最小请求耗时:" + flower.minRt());
            System.out.println("平均请求成功数(每毫秒):" + flower.successAvg());
            System.out.println("平均请求异常数(每毫秒):" + flower.exceptionAvg());


            return Result.succeed("ok");
        } catch (Exception e) {
            flowHelper.incrException();
            return Result.failed("ko");
        }
    }

    /**
     * 流控规则
     *
     * @return
     */
    @GetMapping("/test/sentinelTest")
    public String test() {
        try (Entry entry = SphU.entry("user-center")) {

            log.info("ok");
        } catch (BlockException e) {
            return "ko";
        }

        return "ok";

    }

    /**
     * 降级规则
     *
     * @return
     */
    @GetMapping("/test/sentinelResource")
    @SentinelResource(value = "user-center", blockHandler = "doErrorTest1")
    public String test1() {

        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("ok");

        return "ok";

    }

    public String doErrorTest1(BlockException e) {

        return "ko";

    }


    /**
     * 热点参数限流
     */
    @GetMapping("/test/hotParamFlow")
    @ResponseBody
    public String hotParamFlow(@RequestParam("prodId") Long prodId, @RequestParam("ip") Long ip) {
        Entry entry = null;
        String retVal;
        try {
            // 只对参数prodId进行限流，参数ip不进行限制

            if (1L == prodId) {
                entry = SphU.entry("hotParam", EntryType.IN, 1, prodId);
            } else {
                // 不传入任何参数
                entry = SphU.entry("hotParam", EntryType.IN, 1);
            }

            retVal = "passed";
        } catch (BlockException e) {
            retVal = "blocked";
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return retVal;
    }
}
