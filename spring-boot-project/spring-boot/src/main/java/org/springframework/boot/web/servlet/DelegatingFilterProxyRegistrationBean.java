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
import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

public class DelegatingFilterProxyRegistrationBean extends AbstractFilterRegistrationBean<DelegatingFilterProxy>
		implements ApplicationContextAware {

	@Nullable
	private ApplicationContext applicationContext;

	private final String targetBeanName;

	/**
	 * Create a new {@link DelegatingFilterProxyRegistrationBean} instance to be
	 * registered with the specified {@link ServletRegistrationBean}s.
	 * @param targetBeanName name of the target filter bean to look up in the Spring
	 * application context (must not be {@code null}).
	 * @param servletRegistrationBeans associate {@link ServletRegistrationBean}s
	 */
	public DelegatingFilterProxyRegistrationBean(String targetBeanName,
			ServletRegistrationBean<?>... servletRegistrationBeans) {
		super(servletRegistrationBeans);
		Assert.hasLength(targetBeanName, "TargetBeanName must not be null or empty");
		this.targetBeanName = targetBeanName;
		setName(targetBeanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected String getTargetBeanName() {
		return this.targetBeanName;
	}

	@Override
	public DelegatingFilterProxy getFilter() {
		return new DelegatingFilterProxy(this.targetBeanName, getWebApplicationContext()) {

			@Override
			protected void initFilterBean() throws ServletException {
				// Don't initialize filter bean on init()
			}

		};
	}

	@Nullable
	private WebApplicationContext getWebApplicationContext() {
		Assert.notNull(this.applicationContext, "ApplicationContext be injected");
		Assert.isInstanceOf(WebApplicationContext.class, this.applicationContext);
		return (WebApplicationContext) this.applicationContext;
	}

}
