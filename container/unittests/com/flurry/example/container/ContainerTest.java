package com.flurry.example.container;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class ContainerTest
{
	// class path of the module to load
	protected static final String moduleJar = "../module/dist/module.jar";
	protected static final String apiJar = "../api/dist/api.jar";

	// fully qualified class name of the module to load
	private static final String moduleClass = "com.flurry.example.module.Module";

	protected static interface IClassLoaderFactory
	{
		public ClassLoader factory() throws Exception;
	}

	protected URL[] buildClassPath() throws MalformedURLException
	{
		return new URL[] { URLUtils.buildJarUrl(moduleJar) };
	}

	@Test
	public void testDefaultClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
		{
			public ClassLoader factory() throws MalformedURLException
			{
				// load classes using the default delegation strategy
				return new URLClassLoader(buildClassPath());
			}
		});
	}

	protected String getModuleClass()
	{
		return moduleClass;
	}

	protected IContainer buildContainer()
	{
		return new Container();
	}

	protected void test(IClassLoaderFactory factory) throws Exception
	{
		String moduleClass = getModuleClass();
		IContainer container = buildContainer();

		System.gc();
		long memStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		for (int i=0; i<100; i++)
		{
			ClassLoader moduleLoader = factory.factory();

			// load the module
			container.loadModule(moduleLoader, moduleClass);

			// now free the module
			container.clearModules();
			System.gc();
		}

		System.gc();
		long memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long kbLeaked = memEnd > memStart ? (memEnd - memStart) >> 10 : 0;
		System.out.println("Leaked " + kbLeaked + "KB");
		assertTrue(kbLeaked < 10);
	}
}
