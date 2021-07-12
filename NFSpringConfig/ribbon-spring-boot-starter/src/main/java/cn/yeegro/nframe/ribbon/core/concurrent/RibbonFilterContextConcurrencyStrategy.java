package cn.yeegro.nframe.ribbon.core.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import cn.yeegro.nframe.ribbon.core.context.RibbonFilterContext;
import cn.yeegro.nframe.ribbon.core.context.RibbonFilterContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * Hystrix传播ThreadLocal对象
 * copy from RibbonFilterHystrixConcurrencyStrategy
 * http://www.itmuch.com/spring-cloud-sum/hystrix-threadlocal/
 */
@Slf4j
public class RibbonFilterContextConcurrencyStrategy extends HystrixConcurrencyStrategy {

	private HystrixConcurrencyStrategy delegate;

	public RibbonFilterContextConcurrencyStrategy() {
		try {
			this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
			if (this.delegate instanceof RibbonFilterContextConcurrencyStrategy) {
				// Welcome to singleton hell...
				return;
			}
			HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins
					.getInstance().getCommandExecutionHook();
			HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
					.getEventNotifier();
			HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
					.getMetricsPublisher();
			HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
					.getPropertiesStrategy();
			this.logCurrentStateOfHystrixPlugins(eventNotifier, metricsPublisher,
					propertiesStrategy);
			HystrixPlugins.reset();
			HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
			HystrixPlugins.getInstance()
					.registerCommandExecutionHook(commandExecutionHook);
			HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
			HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
			HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
		}
		catch (Exception e) {
			log.error("Failed to register RibbonFilter Hystrix Concurrency Strategy", e);
		}
	}

	private void logCurrentStateOfHystrixPlugins(HystrixEventNotifier eventNotifier,
			HystrixMetricsPublisher metricsPublisher,
			HystrixPropertiesStrategy propertiesStrategy) {
		if (log.isDebugEnabled()) {
			log.debug("Current Hystrix plugins configuration is ["
					+ "concurrencyStrategy [" + this.delegate + "]," + "eventNotifier ["
					+ eventNotifier + "]," + "metricPublisher [" + metricsPublisher + "],"
					+ "propertiesStrategy [" + propertiesStrategy + "]," + "]");
			log.debug("Registering RibbonFilter Hystrix Concurrency Strategy.");
		}
	}

	@Override
	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		RibbonFilterContext ribbonFilterContext = RibbonFilterContextHolder.getContext();
		return new WrappedCallable<>(callable, ribbonFilterContext);
	}

	@Override
	public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
			HystrixProperty<Integer> corePoolSize,
			HystrixProperty<Integer> maximumPoolSize,
			HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		return this.delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
				keepAliveTime, unit, workQueue);
	}

	@Override
	public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
			HystrixThreadPoolProperties threadPoolProperties) {
		return this.delegate.getThreadPool(threadPoolKey, threadPoolProperties);
	}

	@Override
	public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
		return this.delegate.getBlockingQueue(maxQueueSize);
	}

	@Override
	public <T> HystrixRequestVariable<T> getRequestVariable(
			HystrixRequestVariableLifecycle<T> rv) {
		return this.delegate.getRequestVariable(rv);
	}

	static class WrappedCallable<T> implements Callable<T> {

		private final Callable<T> target;
		private final RibbonFilterContext ribbonFilterContext;

		public WrappedCallable(Callable<T> target, RibbonFilterContext ribbonFilterContext) {
			this.target = target;
			this.ribbonFilterContext = ribbonFilterContext;
		}

		@Override
		public T call() throws Exception {
			try {
				RibbonFilterContextHolder.setContext(ribbonFilterContext); 
				return target.call();
			}
			finally {
				RibbonFilterContextHolder.clearContext();
			}
		}
	}
}