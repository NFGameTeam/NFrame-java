package cn.yeegro.nframe.sentinel.slot.degrade;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * 熔断预警slot
 * @author chenhao
 */
@Slf4j
public class DegradeEarlyWarningSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

  
    /**
     * 与流控基本一致 就是取原规则的方式不一样
     * @param resource
     * @return
     */
    private List<DegradeRule> getRuleProvider(String resource) {
        // Flow rule map should not be null.
        List<DegradeRule> rules = DegradeRuleManager.getRules();
        List<DegradeRule> earlyWarningRuleList = Lists.newArrayList();
        for (DegradeRule rule : rules) {
            DegradeRule earlyWarningRule = new DegradeRule();
            BeanUtils.copyProperties(rule, earlyWarningRule);
            earlyWarningRule.setCount(rule.getCount() * 0.8);
            earlyWarningRuleList.add(earlyWarningRule);
        }
        return earlyWarningRuleList.stream().filter(rule -> resource.equals(rule.getResource())).collect(Collectors.toList());
    }

    /**
     * get origin rule
     *
     * @param resource
     * @return
     */
    private DegradeRule getOriginRule(String resource) {
        List<DegradeRule> originRule = DegradeRuleManager.getRules().stream().filter(rule -> rule.getResource().equals(resource)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(originRule)) {
            return null;
        }
        return originRule.get(0);
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode node, int count, boolean prioritized, Object... args)
            throws Throwable {
        String resource = context.getCurEntry().getResourceWrapper().getName();
        List<DegradeRule> rules = getRuleProvider(resource);
        if (rules != null) {
            for (DegradeRule rule : rules) {
                if (!rule.passCheck(context, node, count)) {
                    DegradeRule originRule = getOriginRule(resource);
                    String originRuleCount = originRule == null ? "未知" : String.valueOf(originRule.getCount());
                    log.info("DegradeEarlyWarning:服务{}目前的熔断指标已经超过{}，接近配置的熔断阈值:{},", resource, rule.getCount(), originRuleCount);
                    break;
                }
            }
        }
        fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }
}