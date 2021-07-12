/*
 * Copyright 2002-2016 the original author or authors.
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
package cn.yeegro.nframe.ribbon.core.concurrent;

import java.util.concurrent.Callable;

import org.springframework.util.Assert;

import cn.yeegro.nframe.ribbon.core.context.RibbonFilterContext;
import cn.yeegro.nframe.ribbon.core.context.RibbonFilterContextHolder;

/**
 * <p>
 * Wraps a delegate {@link Callable} with logic for setting up a
 * {@link RibbonFilterContext} before invoking the delegate {@link Callable} and
 * then removing the {@link RibbonFilterContext} after the delegate has completed.
 * </p>
 * <p>
 * If there is a {@link RibbonFilterContext} that already exists, it will be
 * restored after the {@link #call()} method is invoked.
 * </p>
 *
 * @author Rob Winch
 * @since 3.2
 */
public final class DelegatingRibbonFilterContextCallable<V> implements Callable<V> {

	private final Callable<V> delegate;


	/**
	 * The {@link RibbonFilterContext} that the delegate {@link Callable} will be
	 * ran as.
	 */
	private final RibbonFilterContext delegateRibbonFilterContext;

	/**
	 * The {@link RibbonFilterContext} that was on the {@link RibbonFilterContextHolder}
	 * prior to being set to the originalRibbonFilterContext.
	 */
	private RibbonFilterContext originalRibbonFilterContext;

	/**
	 * Creates a new {@link DelegatingRibbonFilterContextCallable} with a specific
	 * {@link RibbonFilterContext}.
	 * @param delegate the delegate {@link DelegatingRibbonFilterContextCallable} to run with
	 * the specified {@link RibbonFilterContext}. Cannot be null.
	 * @param ribbonFilterContext the {@link RibbonFilterContext} to establish for the delegate
	 * {@link Callable}. Cannot be null.
	 */
	public DelegatingRibbonFilterContextCallable(Callable<V> delegate,
			RibbonFilterContext ribbonFilterContext) {
		Assert.notNull(delegate, "delegate cannot be null");
		Assert.notNull(ribbonFilterContext, "ribbonFilterContext cannot be null");
		this.delegate = delegate;
		this.delegateRibbonFilterContext = ribbonFilterContext;
	}

	/**
	 * Creates a new {@link DelegatingRibbonFilterContextCallable} with the
	 * {@link RibbonFilterContext} from the {@link RibbonFilterContextHolder}.
	 * @param delegate the delegate {@link Callable} to run under the current
	 * {@link RibbonFilterContext}. Cannot be null.
	 */
	public DelegatingRibbonFilterContextCallable(Callable<V> delegate) {
		this(delegate, RibbonFilterContextHolder.getContext());
	}

	@Override
	public V call() throws Exception {
		this.originalRibbonFilterContext = RibbonFilterContextHolder.getContext();

		try {
			RibbonFilterContextHolder.setContext(delegateRibbonFilterContext);
			return delegate.call();
		}
		finally {
			RibbonFilterContext emptyContext = RibbonFilterContextHolder.createEmptyContext();
			if(emptyContext.equals(originalRibbonFilterContext)) {
				RibbonFilterContextHolder.clearContext();
			} else {
				RibbonFilterContextHolder.setContext(originalRibbonFilterContext);
			}
			this.originalRibbonFilterContext = null;
		}
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	/**
	 * Creates a {@link DelegatingRibbonFilterContextCallable} and with the given
	 * {@link Callable} and {@link RibbonFilterContext}, but if the ribbonFilterContext is null
	 * will defaults to the current {@link RibbonFilterContext} on the
	 * {@link RibbonFilterContextHolder}
	 *
	 * @param delegate the delegate {@link DelegatingRibbonFilterContextCallable} to run with
	 * the specified {@link RibbonFilterContext}. Cannot be null.
	 * @param ribbonFilterContext the {@link RibbonFilterContext} to establish for the delegate
	 * {@link Callable}. If null, defaults to {@link RibbonFilterContextHolder#getContext()}
	 * @return
	 */
	public static <V> Callable<V> create(Callable<V> delegate,
			RibbonFilterContext ribbonFilterContext) {
		return ribbonFilterContext == null ? new DelegatingRibbonFilterContextCallable<>(
				delegate) : new DelegatingRibbonFilterContextCallable<>(delegate,
				ribbonFilterContext);
	}
}
