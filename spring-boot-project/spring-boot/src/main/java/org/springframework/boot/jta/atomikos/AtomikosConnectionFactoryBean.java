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

package org.springframework.boot.jta.atomikos;

import javax.annotation.Nullable;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
@ConfigurationProperties(prefix = "spring.jta.atomikos.connectionfactory")
public class AtomikosConnectionFactoryBean extends com.atomikos.jms.AtomikosConnectionFactoryBean
		implements BeanNameAware, InitializingBean, DisposableBean {

	@Nullable
	private String beanName;

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasLength(getUniqueResourceName())) {
			setUniqueResourceName(this.beanName);
		}
		init();
	}

	@Override
	public void destroy() throws Exception {
		close();
	}

}
