/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.cloud;

import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;

public class CloudFoundryVcapEnvironmentPostProcessor
		implements EnvironmentPostProcessor, Ordered, ApplicationListener<ApplicationPreparedEvent> {

	private static final String VCAP_APPLICATION = "VCAP_APPLICATION";

	private static final String VCAP_SERVICES = "VCAP_SERVICES";

	private final Log logger;

	private final boolean switchableLogger;

	// Before ConfigFileApplicationListener so values there can use these ones
	private int order = ConfigDataEnvironmentPostProcessor.ORDER - 1;

	/**
	 * Create a new {@link CloudFoundryVcapEnvironmentPostProcessor} instance.
	 * @deprecated since 2.4.0 in favor of
	 * {@link #CloudFoundryVcapEnvironmentPostProcessor(Log)}
	 */
	@Deprecated
	public CloudFoundryVcapEnvironmentPostProcessor() {
		this.logger = new DeferredLog();
		this.switchableLogger = true;
	}

	/**
	 * Create a new {@link CloudFoundryVcapEnvironmentPostProcessor} instance.
	 * @param logger the logger to use
	 */
	public CloudFoundryVcapEnvironmentPostProcessor(Log logger) {
		this.logger = logger;
		this.switchableLogger = false;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (CloudPlatform.CLOUD_FOUNDRY.isActive(environment)) {
			Properties properties = new Properties();
			JsonParser jsonParser = JsonParserFactory.getJsonParser();
			addWithPrefix(properties, getPropertiesFromApplication(environment, jsonParser), "vcap.application.");
			addWithPrefix(properties, getPropertiesFromServices(environment, jsonParser), "vcap.services.");
			MutablePropertySources propertySources = environment.getPropertySources();
			if (propertySources.contains(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME)) {
				propertySources.addAfter(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME,
						new PropertiesPropertySource("vcap", properties));
			}
			else {
				propertySources.addFirst(new PropertiesPropertySource("vcap", properties));
			}
		}
	}

	/**
	 * Event listener used to switch logging.
	 * @deprecated since 2.4.0 in favor of only using {@link EnvironmentPostProcessor}
	 * callbacks
	 */
	@Deprecated
	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		if (this.switchableLogger) {
			((DeferredLog) this.logger).switchTo(CloudFoundryVcapEnvironmentPostProcessor.class);
		}
	}

	private void addWithPrefix(Properties properties, Properties other, String prefix) {
		for (String key : other.stringPropertyNames()) {
			String prefixed = prefix + key;
			properties.setProperty(prefixed, other.getProperty(key));
		}
	}

	private Properties getPropertiesFromApplication(Environment environment, JsonParser parser) {
		Properties properties = new Properties();
		try {
			String property = environment.getProperty(VCAP_APPLICATION, "{}");
			Map<String, Object> map = parser.parseMap(property);
			extractPropertiesFromApplication(properties, map);
		}
		catch (Exception ex) {
			this.logger.error("Could not parse VCAP_APPLICATION", ex);
		}
		return properties;
	}

	private Properties getPropertiesFromServices(Environment environment, JsonParser parser) {
		Properties properties = new Properties();
		try {
			String property = environment.getProperty(VCAP_SERVICES, "{}");
			Map<String, Object> map = parser.parseMap(property);
			extractPropertiesFromServices(properties, map);
		}
		catch (Exception ex) {
			this.logger.error("Could not parse VCAP_SERVICES", ex);
		}
		return properties;
	}

	private void extractPropertiesFromApplication(Properties properties, Map<String, Object> map) {
		if (map != null) {
			flatten(properties, map, "");
		}
	}

	private void extractPropertiesFromServices(Properties properties, Map<String, Object> map) {
		if (map != null) {
			for (Object services : map.values()) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) services;
				for (Object object : list) {
					@SuppressWarnings("unchecked")
					Map<String, Object> service = (Map<String, Object>) object;
					String key = (String) service.get("name");
					if (key == null) {
						key = (String) service.get("label");
					}
					flatten(properties, service, key);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void flatten(Properties properties, Map<String, Object> input, @Nullable String path) {
		input.forEach((key, value) -> {
			String name = getPropertyName(path, key);
			if (value instanceof Map) {
				// Need a compound key
				flatten(properties, (Map<String, Object>) value, name);
			}
			else if (value instanceof Collection) {
				// Need a compound key
				Collection<Object> collection = (Collection<Object>) value;
				properties.put(name, StringUtils.collectionToCommaDelimitedString(collection));
				int count = 0;
				for (Object item : collection) {
					String itemKey = "[" + (count++) + "]";
					flatten(properties, Collections.singletonMap(itemKey, item), name);
				}
			}
			else if (value instanceof String) {
				properties.put(name, value);
			}
			else if (value instanceof Number) {
				properties.put(name, value.toString());
			}
			else if (value instanceof Boolean) {
				properties.put(name, value.toString());
			}
			else {
				properties.put(name, (value != null) ? value : "");
			}
		});
	}

	private String getPropertyName(@Nullable String path, String key) {
		if (!StringUtils.hasText(path)) {
			return key;
		}
		if (key.startsWith("[")) {
			return path + key;
		}
		return path + "." + key;
	}

}
