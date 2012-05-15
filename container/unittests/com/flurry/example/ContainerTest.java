package com.flurry.example;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Before;
import org.junit.Test;

import com.flurry.example.classLoader.ConditionalDelegationClassLoader;
import com.flurry.example.classLoader.PostDelegationClassLoader;

public class ContainerTest
{
	private static final int bytesPerMb = 1024 * 1024;

	// class path of the module to load
	private static final String moduleJar = "../module/dist/module.jar";
	private static final String apiJar = "../api/dist/api.jar";
	private static final String libJar = "../lib/dist/lib.jar";

	// package prefix under which all internal code can be found
	private static final String flurryPrefix = "com.flurry.example.";
	
	// fully qualified class name of the module to load
	private static final String moduleClass = flurryPrefix + "Module";

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
				return new URLClassLoader(urls, null);
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

	private void test(IClassLoaderFactory factory) throws Exception
	{
		System.gc();
		Container container = new Container(factory);
		long memUsed = Runtime.getRuntime().totalMemory();

		for (int i=0; i<4; i++)
		{
			// load the module
			container.loadModule(moduleClass);

			// now free the module
			container.clearModules();

			System.gc();
		}

		// this is not scientific, but seems to work pretty well
		long memLeaked = (Runtime.getRuntime().totalMemory() - memUsed) / bytesPerMb;
		System.out.println("Leaked " + memLeaked + "MB");
		assertTrue(memLeaked < 100);
	}
}
