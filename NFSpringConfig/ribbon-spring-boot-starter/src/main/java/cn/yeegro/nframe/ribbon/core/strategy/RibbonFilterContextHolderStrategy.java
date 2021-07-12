package cn.yeegro.nframe.ribbon.core.strategy;

import cn.yeegro.nframe.ribbon.core.context.RibbonFilterContext;

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


/**
 * A strategy for storing security context information against a thread.
 *
 * <p>
 * The preferred strategy is loaded by {@link RibbonFilterContexHolder}.
 *
 * @author Ben Alex
 */
public interface RibbonFilterContextHolderStrategy {
	// ~ Methods
	// ========================================================================================================

	/**
	 * Clears the current context.
	 */
	void clearContext();

	/**
	 * Obtains the current context.
	 *
	 * @return a context (never <code>null</code> - create a default implementation if
	 * necessary)
	 */
	RibbonFilterContext getContext();

	/**
	 * Sets the current context.
	 *
	 * @param context to the new argument (should never be <code>null</code>, although
	 * implementations must check if <code>null</code> has been passed and throw an
	 * <code>IllegalArgumentException</code> in such cases)
	 */
	void setContext(RibbonFilterContext context);

	/**
	 * Creates a new, empty context implementation, for use by
	 * <tt>RibbonFilterContexRepository</tt> implementations, when creating a new context for
	 * the first time.
	 *
	 * @return the empty context.
	 */
	RibbonFilterContext createEmptyContext();
}
