package cn.yeegro.nframe.ribbon.predicate;

import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import lombok.extern.slf4j.Slf4j;

/**
 * A default implementation of {@link DiscoveryEnabledServer} that matches the
 * instance against the attributes registered through
 *
 * @author Jakub Narloch
 * @see DiscoveryEnabledPredicate
 */
@Slf4j
public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean apply(DiscoveryEnabledServer server) {

//		final RibbonFilterContext context1 = RibbonFilterContextHolder.getContext();
//
//		String hello1 = context1.get("hello");
//		
//		System.out.println("hello1=="+hello1);
//		log.info("hello1=="+hello1);
//		
//		HystrixContextRunnable runnable = new HystrixContextRunnable(() -> {
//			// 从新的线程中获取当前用户id
//			final RibbonFilterContext context = RibbonFilterContextHolder.getContext();
//
//			String hello2 = context.get("hello");
//			System.out.println("hello2=="+hello2);
//			log.info("hello2=="+hello2);
//		});
//
//		new Thread(runnable).start();
		log.info("host=={}",server.getHost());
		return true;

	}
}
