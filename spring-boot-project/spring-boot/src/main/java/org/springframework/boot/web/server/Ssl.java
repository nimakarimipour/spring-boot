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

package org.springframework.boot.web.server;

import javax.annotation.Nullable;

public class Ssl {

	private boolean enabled = true;

	@Nullable
	private ClientAuth clientAuth;

	@Nullable
	private String[] ciphers;

	@Nullable
	private String[] enabledProtocols;

	@Nullable
	private String keyAlias;

	@Nullable
	private String keyPassword;

	@Nullable
	private String keyStore;

	@Nullable
	private String keyStorePassword;

	@Nullable
	private String keyStoreType;

	@Nullable
	private String keyStoreProvider;

	@Nullable
	private String trustStore;

	@Nullable
	private String trustStorePassword;

	@Nullable
	private String trustStoreType;

	@Nullable
	private String trustStoreProvider;

	private String protocol = "TLS";

	/**
	 * Return whether to enable SSL support.
	 * @return whether to enable SSL support
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Return Whether client authentication is not wanted ("none"), wanted ("want") or
	 * needed ("need"). Requires a trust store.
	 * @return the {@link ClientAuth} to use
	 */
	@Nullable
	public ClientAuth getClientAuth() {
		return this.clientAuth;
	}

	public void setClientAuth(ClientAuth clientAuth) {
		this.clientAuth = clientAuth;
	}

	/**
	 * Return the supported SSL ciphers.
	 * @return the supported SSL ciphers
	 */
	@Nullable
	public String[] getCiphers() {
		return this.ciphers;
	}

	public void setCiphers(String[] ciphers) {
		this.ciphers = ciphers;
	}

	/**
	 * Return the enabled SSL protocols.
	 * @return the enabled SSL protocols.
	 */
	@Nullable
	public String[] getEnabledProtocols() {
		return this.enabledProtocols;
	}

	public void setEnabledProtocols(String[] enabledProtocols) {
		this.enabledProtocols = enabledProtocols;
	}

	/**
	 * Return the alias that identifies the key in the key store.
	 * @return the key alias
	 */
	@Nullable
	public String getKeyAlias() {
		return this.keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	/**
	 * Return the password used to access the key in the key store.
	 * @return the key password
	 */
	@Nullable
	public String getKeyPassword() {
		return this.keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	/**
	 * Return the path to the key store that holds the SSL certificate (typically a jks
	 * file).
	 * @return the path to the key store
	 */
	@Nullable
	public String getKeyStore() {
		return this.keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	/**
	 * Return the password used to access the key store.
	 * @return the key store password
	 */
	@Nullable
	public String getKeyStorePassword() {
		return this.keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * Return the type of the key store.
	 * @return the key store type
	 */
	@Nullable
	public String getKeyStoreType() {
		return this.keyStoreType;
	}

	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	/**
	 * Return the provider for the key store.
	 * @return the key store provider
	 */
	@Nullable
	public String getKeyStoreProvider() {
		return this.keyStoreProvider;
	}

	public void setKeyStoreProvider(String keyStoreProvider) {
		this.keyStoreProvider = keyStoreProvider;
	}

	/**
	 * Return the trust store that holds SSL certificates.
	 * @return the trust store
	 */
	@Nullable
	public String getTrustStore() {
		return this.trustStore;
	}

	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}

	/**
	 * Return the password used to access the trust store.
	 * @return the trust store password
	 */
	@Nullable
	public String getTrustStorePassword() {
		return this.trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	/**
	 * Return the type of the trust store.
	 * @return the trust store type
	 */
	@Nullable
	public String getTrustStoreType() {
		return this.trustStoreType;
	}

	public void setTrustStoreType(String trustStoreType) {
		this.trustStoreType = trustStoreType;
	}

	/**
	 * Return the provider for the trust store.
	 * @return the trust store provider
	 */
	@Nullable
	public String getTrustStoreProvider() {
		return this.trustStoreProvider;
	}

	public void setTrustStoreProvider(String trustStoreProvider) {
		this.trustStoreProvider = trustStoreProvider;
	}

	/**
	 * Return the SSL protocol to use.
	 * @return the SSL protocol
	 */
	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * Client authentication types.
	 */
	public enum ClientAuth {

		/**
		 * Client authentication is not wanted.
		 */
		NONE,

		/**
		 * Client authentication is wanted but not mandatory.
		 */
		WANT,

		/**
		 * Client authentication is needed and mandatory.
		 */
		NEED

	}

}
