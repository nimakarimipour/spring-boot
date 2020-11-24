/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.web.servlet;

import javax.annotation.Nullable;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.springframework.util.Assert;

public class FilterRegistrationBean<T extends Filter> extends AbstractFilterRegistrationBean<T> {

	@Nullable
	private T filter;

	/**
	 * Create a new {@link FilterRegistrationBean} instance.
	 */
	public FilterRegistrationBean() {
	}

	/**
	 * Create a new {@link FilterRegistrationBean} instance to be registered with the
	 * specified {@link ServletRegistrationBean}s.
	 * @param filter the filter to register
	 * @param servletRegistrationBeans associate {@link ServletRegistrationBean}s
	 */
	public FilterRegistrationBean(T filter, ServletRegistrationBean<?>... servletRegistrationBeans) {
		super(servletRegistrationBeans);
		Assert.notNull(filter, "Filter must not be null");
		this.filter = filter;
	}

	@Override@Nullable
	public T getFilter() {
		return this.filter;
	}

	/**
	 * Set the filter to be registered.
	 * @param filter the filter
	 */
	public void setFilter(T filter) {
		Assert.notNull(filter, "Filter must not be null");
		this.filter = filter;
	}

}
