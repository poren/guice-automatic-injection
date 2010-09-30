/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devsurf.injection.guice.integrations.test.commons.configuration.url;

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.configuration.Configuration.PathType;
import de.devsurf.injection.guice.integrations.commons.configuration.CommonsConfigurationFeature;
import de.devsurf.injection.guice.scanner.StartupModule;
import de.devsurf.injection.guice.scanner.annotations.AutoBind;
import de.devsurf.injection.guice.scanner.asm.ASMClasspathScanner;

public class URLConfigTests {
    @Test
    public void createDynamicModule() {
	StartupModule startup = StartupModule.create(ASMClasspathScanner.class,
	    URLConfigTests.class.getPackage().getName());
	startup.addFeature(CommonsConfigurationFeature.class);

	Injector injector = Guice.createInjector(startup);
	assertNotNull(injector);
    }

    @Test
    public void createPListConfiguration() {
	StartupModule startup = StartupModule.create(ASMClasspathScanner.class,
	    URLConfigTests.class.getPackage().getName());
	startup.addFeature(CommonsConfigurationFeature.class);

	Injector injector = Guice.createInjector(startup);
	assertNotNull(injector);

	TestInterface instance = injector.getInstance(TestInterface.class);
	Assert.assertTrue("sayHello() - yeahhh out of the package :)".equals(instance.sayHello()));
    }

    @de.devsurf.injection.guice.configuration.Configuration(name = "config", bind = PropertyListConfiguration.class, path = "http://devsurf.de/guice/configuration.plist", pathType = PathType.URL)
    public interface TestConfiguration {
    }

    public static interface TestInterface {
	String sayHello();
    }

    @AutoBind
    public static class TestImplementations implements TestInterface {
	@Inject
	@Named("config")
	private Configuration config;

	@Override
	public String sayHello() {
	    return "sayHello() - " + config.getString("de.devsurf.configuration.message");
	}
    }
}
