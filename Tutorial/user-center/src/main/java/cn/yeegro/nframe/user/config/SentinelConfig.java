package cn.yeegro.nframe.user.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;

@Component
public class SentinelConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        // 初始化流控规则
        FlowRule rule = new FlowRule();
        rule.setResource("user-center");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(10);
        List<FlowRule> rules = new ArrayList<>();
        rules.add(rule);
        FlowRuleManager.loadRules(Collections.singletonList(rule));

        // 初始化降级规则
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("user-center");
        //响应时间
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        //超过100ms
        degradeRule.setCount(10);
        //熔断打开后，经过多少时间进入半打开
        degradeRule.setTimeWindow(10);
        DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));


        // 定义热点限流的规则，对第一个参数设置 qps 限流模式，阈值为1
        ParamFlowRule paramFlowRule = new ParamFlowRule("hotParam")
                .setParamIdx(0)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setCount(1);
        ParamFlowRuleManager.loadRules(Collections.singletonList(paramFlowRule));
    }

}
