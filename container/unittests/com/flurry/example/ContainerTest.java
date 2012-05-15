package com.flurry.example;

import static org.junit.Assert.assertTrue;

import java.io.File;
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
	private static final String modulePath = "../module/bin/";
	private static final String jarPath = "../api/dist/api.jar";
	
	// package prefix under which all user code can be found
	private static final String flurryPrefix = "com.flurry.example.";
	
	// fully qualified class name of the module to load
	private static final String moduleClass = flurryPrefix + "Module";

	private static URL buildFileUrl(String path) throws MalformedURLException
	{
		return new File(modulePath).toURI().toURL();
	}

	private static URL buildJarUrl(String path) throws MalformedURLException
	{
		return new URL("jar", "", "file:" + new File(path).getAbsolutePath() + "!/");
	}

	private URL[] urls;

	@Before
	public void setupClassPath() throws MalformedURLException
	{
		urls = new URL[] { buildFileUrl(modulePath),
		                   buildJarUrl(jarPath)};
	}

	@Test
	public void testDefaultClassLoader() throws Exception
	{
		test(new ClassLoaderFactory()
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
		test(new ClassLoaderFactory()
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
		test(new ClassLoaderFactory()
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
		test(new ClassLoaderFactory()
    	{
			public ClassLoader factory()
			{
				// load our classes from the parent before the child,
				// all other from the child before the parent
				return new ConditionalDelegationClassLoader(urls, flurryPrefix);
			}
		});
	}

	private void test(ClassLoaderFactory factory) throws Exception
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

		System.gc();
		long memLeaked = (Runtime.getRuntime().totalMemory() - memUsed) / bytesPerMb;
		System.out.println("Leaked " + memLeaked + "MB");
		assertTrue(memLeaked < 100);
	}
}
