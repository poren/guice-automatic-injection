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
package de.devsurf.injection.guice.scanner.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Qualifier;

import com.google.inject.Module;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.scanner.GuiceAnnotationListener;

/**
 * Annotate a Module with the GuiceModule-Annotation and it will be installed
 * automatically.
 * 
 * @author Daniel Manzke
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Target({ElementType.TYPE})
public @interface GuiceModule {
    BindingStage stage() default BindingStage.OBJECT;
    
    public static enum BindingStage{
	TOOL,
	OBJECT
    }
    
    @Singleton
    public class ToolModuleListener extends GuiceAnnotationListener {
	private Logger _logger = Logger.getLogger(ToolModuleListener.class.getName());
	@Override
	public void found(Class<Object> annotatedClass, Map<String, Annotation> annotations) {
	    if (annotations.containsKey(GuiceModule.class.getName()) && ((GuiceModule)annotations.get(GuiceModule.class.getName())).stage() == BindingStage.TOOL) {
		try {
		    if(_logger.isLoggable(Level.FINE)){
			_logger.fine("Installing Module: "+annotatedClass.getName());
		    }
		    synchronized (_binder) {
			_binder.install((Module) annotatedClass.newInstance());
		    }
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
    
    @Singleton
    public class ObjectModuleListener extends GuiceAnnotationListener {
	private Logger _logger = Logger.getLogger(ObjectModuleListener.class.getName());
	@Override
	public void found(Class<Object> annotatedClass, Map<String, Annotation> annotations) {
	    if (annotations.containsKey(GuiceModule.class.getName()) && ((GuiceModule)annotations.get(GuiceModule.class.getName())).stage() == BindingStage.OBJECT) {
		try {
		    if(_logger.isLoggable(Level.FINE)){
			_logger.fine("Installing Module: "+annotatedClass.getName());
		    }
		    synchronized (_binder) {
			_binder.install((Module) annotatedClass.newInstance());
		    }
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		}
	    }
	}
    }    
}
