package cn.yeegro.nframe.ribbon.core.context;

/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import cn.yeegro.nframe.ribbon.core.strategy.GlobalRibbonFilterContextHolderStrategy;
import cn.yeegro.nframe.ribbon.core.strategy.InheritableThreadLocalRibbonFilterContextHolderStrategy;
import cn.yeegro.nframe.ribbon.core.strategy.RibbonFilterContextHolderStrategy;
import cn.yeegro.nframe.ribbon.core.strategy.ThreadLocalRibbonFilterContextHolderStrategy;

import java.lang.reflect.Constructor;

/**
 * Associates a given {@link RibbonFilterContex} with the current execution thread.
 * <p>
 * This class provides a series of static methods that delegate to an instance of
 * {@link org.springframework.security.core.context.RibbonFilterContexHolderStrategy}. The
 * purpose of the class is to provide a convenient way to specify the strategy that should
 * be used for a given JVM. This is a JVM-wide setting, since everything in this class is
 * <code>static</code> to facilitate ease of use in calling code.
 * <p>
 * To specify which strategy should be used, you must provide a mode setting. A mode
 * setting is one of the three valid <code>MODE_</code> settings defined as
 * <code>static final</code> fields, or a fully qualified classname to a concrete
 * implementation of
 * {@link org.springframework.security.core.context.RibbonFilterContexHolderStrategy} that
 * provides a public no-argument constructor.
 * <p>
 * There are two ways to specify the desired strategy mode <code>String</code>. The first
 * is to specify it via the system property keyed on {@link #SYSTEM_PROPERTY}. The second
 * is to call {@link #setStrategyName(String)} before using the class. If neither approach
 * is used, the class will default to using {@link #MODE_THREADLOCAL}, which is backwards
 * compatible, has fewer JVM incompatibilities and is appropriate on servers (whereas
 * {@link #MODE_GLOBAL} is definitely inappropriate for server use).
 *
 * @author Ben Alex
 *
 */
public class RibbonFilterContextHolder {
	// ~ Static fields/initializers
	// =====================================================================================

	public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
	public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
	public static final String MODE_GLOBAL = "MODE_GLOBAL";
	public static final String SYSTEM_PROPERTY = "ribbon.strategy";
	private static String strategyName = System.getProperty(SYSTEM_PROPERTY);
	private static RibbonFilterContextHolderStrategy strategy;
	private static int initializeCount = 0;

	static {
		initialize();
	}

	// ~ Methods
	// ========================================================================================================

	/**
	 * Explicitly clears the context value from the current thread.
	 */
	public static void clearContext() {
		strategy.clearContext();
	}

	/**
	 * Obtain the current <code>RibbonFilterContex</code>.
	 *
	 * @return the security context (never <code>null</code>)
	 */
	public static RibbonFilterContext getContext() {
		return strategy.getContext();
	}

	/**
	 * Primarily for troubleshooting purposes, this method shows how many times the class
	 * has re-initialized its <code>RibbonFilterContexHolderStrategy</code>.
	 *
	 * @return the count (should be one unless you've called
	 * {@link #setStrategyName(String)} to switch to an alternate strategy.
	 */
	public static int getInitializeCount() {
		return initializeCount;
	}

	private static void initialize() {
		if (!StringUtils.hasText(strategyName)) {
			// Set default
			strategyName = MODE_INHERITABLETHREADLOCAL;
		}
		if (MODE_THREADLOCAL.equals(strategyName)) {
			strategy = new ThreadLocalRibbonFilterContextHolderStrategy();
		}
		else if (MODE_INHERITABLETHREADLOCAL.equals(strategyName)) {
			strategy = new InheritableThreadLocalRibbonFilterContextHolderStrategy();
		}
		else if (MODE_GLOBAL.equals(strategyName)) {
			strategy = new GlobalRibbonFilterContextHolderStrategy();
		}
		else {
			// Try to load a custom strategy
			try {
				Class<?> clazz = Class.forName(strategyName);
				Constructor<?> customStrategy = clazz.getConstructor();
				strategy = (RibbonFilterContextHolderStrategy) customStrategy.newInstance();
			}
			catch (Exception ex) {
				ReflectionUtils.handleReflectionException(ex);
			}
		}

		initializeCount++;
	}

	/**
	 * Associates a new <code>RibbonFilterContex</code> with the current thread of execution.
	 *
	 * @param context the new <code>RibbonFilterContex</code> (may not be <code>null</code>)
	 */
	public static void setContext(RibbonFilterContext context) {
		strategy.setContext(context);
	}

	/**
	 * Changes the preferred strategy. Do <em>NOT</em> call this method more than once for
	 * a given JVM, as it will re-initialize the strategy and adversely affect any
	 * existing threads using the old strategy.
	 *
	 * @param strategyName the fully qualified class name of the strategy that should be
	 * used.
	 */
	public static void setStrategyName(String strategyName) {
		RibbonFilterContextHolder.strategyName = strategyName;
		initialize();
	}

	/**
	 * Allows retrieval of the context strategy. See SEC-1188.
	 *
	 * @return the configured strategy for storing the security context.
	 */
	public static RibbonFilterContextHolderStrategy getContextHolderStrategy() {
		return strategy;
	}

	/**
	 * Delegates the creation of a new, empty context to the configured strategy.
	 */
	public static RibbonFilterContext createEmptyContext() {
		return strategy.createEmptyContext();
	}

	@Override
	public String toString() {
		return "RibbonFilterContexHolder[strategy='" + strategyName + "'; initializeCount="
				+ initializeCount + "]";
	}
}
