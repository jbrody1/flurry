package com.flurry.example;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Before;
import org.junit.Test;

import com.flurry.example.classLoader.ConditionalDelegationClassLoader;
import com.flurry.example.classLoader.IClassLoaderFactory;
import com.flurry.example.classLoader.PostDelegationClassLoader;
import com.flurry.example.classLoader.StandAloneClassLoader;

public class ContainerTest
{
	// package prefix under which all internal code can be found
	static final String flurryPrefix = "com.flurry.example.";
	
	// class path of the module to load
	private static final String moduleJar = "../module/dist/module.jar";
	private static final String apiJar = "../api/dist/api.jar";
	private static final String libJar = "../lib/dist/lib.jar";

	// fully qualified class name of the module to load
	private static final String moduleClass = flurryPrefix + "Module";

	private static final int bytesPerMb = 1024 * 1024;

	private URL[] urls;

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
				// load classes from the parent before the child
				return new URLClassLoader(urls);
			}
		});
	}

	@Test
	public void testStandAloneClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory()
			{
				// don't load any classes from the parent
				return new StandAloneClassLoader(urls);
			}
		});
	}

	@Test
	public void testPostDelegationClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory()
			{
				// load classes from the child before the parent
				return new PostDelegationClassLoader(urls);
			}
		});
	}

	@Test
	public void testConditionalDelegationClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory()
			{
				// load our classes from the parent before the child,
				// all other from the child before the parent
				return new ConditionalDelegationClassLoader(urls, flurryPrefix);
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

	private void test(IClassLoaderFactory factory) throws Exception
	{
		System.gc();
		String moduleClass = getModuleClass();
		IContainer container = buildContainer();
		long memStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		for (int i=0; i<4; i++)
		{
			ClassLoader moduleLoader = factory.factory();

			// load the module
			container.loadModule(moduleLoader, moduleClass);

			// now free the module
			container.clearModules();

			System.gc();
		}

		long memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long memLeaked = (memEnd - memStart) / bytesPerMb;
		System.out.println("Leaked " + memLeaked + "MB");
		assertEquals(0, memLeaked);
	}
}
