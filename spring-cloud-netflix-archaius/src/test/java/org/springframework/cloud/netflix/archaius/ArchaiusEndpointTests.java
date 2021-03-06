/*
 * Copyright 2013-2020 the original author or authors.
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

package org.springframework.cloud.netflix.archaius;

import java.util.Map;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import org.junit.Test;

import org.springframework.core.env.StandardEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
public class ArchaiusEndpointTests {

	private ArchaiusEndpoint endpoint = new ArchaiusEndpoint();

	@Test
	public void detectsPropertiesWhenSet() {
		ConfigurationManager.getConfigInstance().setProperty("foo", "bar");
		assertThat(this.endpoint.invoke().containsKey("foo")).isTrue();
	}

	@Test
	public void doesNotIncludeSpringEnvironment() {
		ConcurrentCompositeConfiguration composite = new ConcurrentCompositeConfiguration(
				ConfigurationManager.getConfigInstance());
		ConfigurableEnvironmentConfiguration config = new ConfigurableEnvironmentConfiguration(
				new StandardEnvironment());
		assertThat(config.containsKey("user.dir")).isTrue();
		composite.addConfiguration(config);
		ConfigurationManager.getConfigInstance().setProperty("foo", "bar");
		Map<String, Object> map = this.endpoint.invoke();
		assertThat(map.containsKey("foo")).isTrue();
		assertThat(map.containsKey("user.dir")).isFalse();
	}

}
