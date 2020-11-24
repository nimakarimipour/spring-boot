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

package org.springframework.boot.context.properties.source;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;

import org.springframework.boot.context.properties.source.ConfigurationPropertyName.Form;

final class SystemEnvironmentPropertyMapper implements PropertyMapper {

	public static final PropertyMapper INSTANCE = new SystemEnvironmentPropertyMapper();

	@Override
	public List<String> map(ConfigurationPropertyName configurationPropertyName) {
		String name = convertName(configurationPropertyName);
		String legacyName = convertLegacyName(configurationPropertyName);
		if (name.equals(legacyName)) {
			return Collections.singletonList(name);
		}
		return Arrays.asList(name, legacyName);
	}

	private String convertName(ConfigurationPropertyName name) {
		return convertName(name, name.getNumberOfElements());
	}

	private String convertName(ConfigurationPropertyName name, int numberOfElements) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < numberOfElements; i++) {
			if (result.length() > 0) {
				result.append('_');
			}
			result.append(name.getElement(i, Form.UNIFORM).toUpperCase(Locale.ENGLISH));
		}
		return result.toString();
	}

	private String convertLegacyName(ConfigurationPropertyName name) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < name.getNumberOfElements(); i++) {
			if (result.length() > 0) {
				result.append('_');
			}
			result.append(convertLegacyNameElement(name.getElement(i, Form.ORIGINAL)));
		}
		return result.toString();
	}

	private Object convertLegacyNameElement(String element) {
		return element.replace('-', '_').toUpperCase(Locale.ENGLISH);
	}

	@Override
	public ConfigurationPropertyName map(String propertySourceName) {
		return convertName(propertySourceName);
	}

	private ConfigurationPropertyName convertName(String propertySourceName) {
		try {
			return ConfigurationPropertyName.adapt(propertySourceName, '_', this::processElementValue);
		}
		catch (Exception ex) {
			return ConfigurationPropertyName.EMPTY;
		}
	}

	private CharSequence processElementValue(CharSequence value) {
		String result = value.toString().toLowerCase(Locale.ENGLISH);
		return isNumber(result) ? "[" + result + "]" : result;
	}

	private static boolean isNumber(String string) {
		return string.chars().allMatch(Character::isDigit);
	}

	@Override
	public BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> getAncestorOfCheck() {
		return this::isAncestorOf;
	}

	private boolean isAncestorOf(ConfigurationPropertyName name, ConfigurationPropertyName candidate) {
		return name.isAncestorOf(candidate) || isLegacyAncestorOf(name, candidate);
	}

	private boolean isLegacyAncestorOf(ConfigurationPropertyName name, ConfigurationPropertyName candidate) {
		if (!hasDashedEntries(name)) {
			return false;
		}
		ConfigurationPropertyName legacyCompatibleName = buildLegacyCompatibleName(name);
		return legacyCompatibleName != null && legacyCompatibleName.isAncestorOf(candidate);
	}

	@Nullable
	private ConfigurationPropertyName buildLegacyCompatibleName(ConfigurationPropertyName name) {
		StringBuilder legacyCompatibleName = new StringBuilder();
		for (int i = 0; i < name.getNumberOfElements(); i++) {
			if (i != 0) {
				legacyCompatibleName.append('.');
			}
			legacyCompatibleName.append(name.getElement(i, Form.DASHED).replace('-', '.'));
		}
		return ConfigurationPropertyName.ofIfValid(legacyCompatibleName);
	}

	boolean hasDashedEntries(ConfigurationPropertyName name) {
		for (int i = 0; i < name.getNumberOfElements(); i++) {
			if (name.getElement(i, Form.DASHED).indexOf('-') != -1) {
				return true;
			}
		}
		return false;
	}

}
