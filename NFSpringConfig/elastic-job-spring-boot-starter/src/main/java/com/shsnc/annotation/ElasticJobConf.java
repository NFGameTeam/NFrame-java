package com.shsnc.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用于类上 Class
 *   任务的配置只需要在Job类上加上此注解即可
 *
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJobConf {

    /*******************  JobCoreConfiguration 作业核心配置 START  *******************/

    /**
     * cron表达式，用于控制作业触发时间
     * @return
     */
    @AliasFor("cron")
    String value() default "";

    @AliasFor("value")
    String cron() default "";

    /**
     *  作业名称 作业名称唯一性 保证全局只有一个作业名称
     * @return
     */
    String jobName() default "";

    /**
     * 作业分片总数
     * @return
     */
    int shardingTotalCount() default 1;


    /**
     * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     * <p>分片序列号从0开始，不可大于或等于作业分片总数<p>
     * <p>如：<p>
     * <p>0=a,1=b,2=c<p>
     * @return
     */
    String shardingItemParameters() default "";

    /**
     * 作业自定义参数
     * <p>作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业<p>
     * <p>例：每次获取的数据量、作业实例从数据库读取的主键等<p>
     * @return
     */
    String jobParameter() default "";

    /**
     * 是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
     * @return
     */
    boolean failover() default false;

    /**
     * 是否开启错过任务重新执行
     * @return
     */
    boolean misfire() default false;

    /**
     * 作业描述信息
     * @return
     */
    String description() default "";



    /*******************  JobCoreConfiguration 作业核心配置 END    *******************/



    /*******************  DataflowJobConfiguration 数据流作业配置信息 START    *******************/

    /**
     * 是否流式处理数据
     * <p>如果流式处理数据, 则fetchData不返回空结果将持续执行作业<p>
     * <p>如果非流式处理数据, 则处理数据完成后作业结束<p>
     * @return
     */
    boolean streamingProcess() default false;


    /*******************  DataflowJobConfiguration 数据流作业配置信息 END      *******************/



    /*******************  ScriptJobConfiguration 脚本作业配置 START    *******************/

    /**
     * 脚本型作业执行命令行
     * @return
     */
    String scriptCommandLine() default "";

    /*******************  ScriptJobConfiguration 脚本作业配置 END      *******************/





    /*******************  LiteJobConfiguration Lite作业配置 START      *******************/

    /**
     * 监控作业运行时状态
     * <p>每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。<p>
     * <p>因为是瞬时状态，所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。<p>
     * <p>每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。<p>
     * @return
     */
    boolean monitorExecution() default true;

    /**
     * 大允许的本机与注册中心的时间误差秒数
     * <p>如果时间误差超过配置秒数则作业启动时将抛异常<p>
     * <p>配置为-1表示不校验时间误差<p>
     * @return
     */
    int maxTimeDiffSeconds() default -1;

    /**
     * 作业监控端口
     * <p>建议配置作业监控端口, 方便开发者dump作业信息。<p>
     * <p>使用方法: echo “dump” | nc 127.0.0.1 9888<p>
     * @return
     */
    int monitorPort() default -1;

    /**
     * 作业分片策略实现类全路径,默认使用平均分配策略
     *      默认策略 com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy 基于平均分配算法的分片策略
     *      com.dangdang.ddframe.job.lite.api.strategy.impl.RotateServerByNameJobShardingStrategy
     * @return
     */
    String jobShardingStrategyClass() default "com.shsnc.strategy.RotateServerByJobIndexShardingStrategy";

    /**
     * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复,单位：分钟
     *  默认修复30分钟
     * @return
     */
    int reconcileIntervalMinutes() default 30;

    /**
     * 作业是否禁止启动,可用于部署作业时，先禁止启动，部署结束后统一启动
     * @return
     */
    boolean disabled() default false;

    /**
     * 本地配置是否可覆盖注册中心配置
     * 如果可覆盖，每次启动作业都以本地配置为准
     * @return
     */
    boolean overwrite() default true;

    /*******************  LiteJobConfiguration Lite作业配置 END        *******************/

    /**
     * 作业事件追踪的数据源Bean引用
     * @return
     */
    String eventTraceRdbDataSource() default "";

    /**
     * 前置后置任务监听实现类，需实现ElasticJobListener接口
     * @return
     */
    String listener() default "";


    /**
     * 前置后置任务分布式监听实现类，需继承AbstractDistributeOnceElasticJobListener类
     * @return
     */
    String distributedListener() default "";

    /**
     * 最后一个作业执行前的执行方法的超时时间,单位：毫秒
     * @return
     */
    long startedTimeoutMilliseconds() default Long.MAX_VALUE;

    /**
     * 最后一个作业执行后的执行方法的超时时间,单位：毫秒
     * @return
     */
    long completedTimeoutMilliseconds() default Long.MAX_VALUE;

    /**
     * 自定义异常处理类
     * @return
     */
    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    /**
     * 自定义业务处理线程池
     * @return
     */
    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";


}
