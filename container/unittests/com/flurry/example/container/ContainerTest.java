package com.flurry.example.container;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Before;
import org.junit.Test;

public class ContainerTest
{
	// class path of the module to load
	private static final String moduleJar = "../module/dist/module.jar";
	private static final String apiJar = "../api/dist/api.jar";
	private static final String libJar = "../lib/dist/lib.jar";

	// fully qualified class name of the module to load
	private static final String moduleClass = "com.flurry.example.module.Module";

	private static final int bytesPerMb = 1024 * 1024;

	protected static interface IClassLoaderFactory
	{
		public ClassLoader factory();
	}

	protected URL[] urls;

	@Before
	public void setupClassPath() throws MalformedURLException
	{
		urls = new URL[] { URLUtils.buildJarUrl(moduleJar),		// required
		                   URLUtils.buildJarUrl(apiJar),		// not required, but exposes a problem with post-delegation
		                   URLUtils.buildJarUrl(libJar) };		// not required, but exposes a problem with default delegation 
	}

	@Test
	public void testDefaultClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
		{
			public ClassLoader factory()
			{
				// load classes using the default delegation strategy
				return new URLClassLoader(urls);
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

		for (int i=0; i<4; i++)
		{
			ClassLoader moduleLoader = factory.factory();

			// load the module
			container.loadModule(moduleLoader, moduleClass);

			// now free the module
			container.clearModules();
		}

		System.gc();
		long memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long memLeaked = (memEnd - memStart) / bytesPerMb;
		System.out.println("Leaked " + memLeaked + "MB");
		assertEquals(0, memLeaked);
	}
}
