package com.flurry.example.container;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.flurry.example.container.classLoader.StandAloneClassLoader;

public class ContainerUsingLibraryTest extends ContainerTest
{
	// dependencies of the module to load
	private static final String libJar = "../lib/dist/lib.jar";

	// fully qualified class name of the module to load
	private static final String moduleClass = "com.flurry.example.module.ModuleUsingLibrary";

	@Override
	protected URL[] buildClassPath() throws MalformedURLException
	{
		return new URL[] { URLUtils.buildJarUrl(moduleJar),		// required
						   URLUtils.buildJarUrl(apiJar),		// required for stand-alone class loader
						   URLUtils.buildJarUrl(libJar) };		// not required, but exposes a memory leak with default delegation
	}

	@Override
	protected String getModuleClass()
	{
		return moduleClass;
	}

	@Override
	protected IContainer buildContainer()
	{
		return new ContainerUsingLibrary();
	}

	@Test
	public void testStandAloneClassLoader() throws Exception
	{
		test(new IClassLoaderFactory()
    	{
			public ClassLoader factory() throws MalformedURLException
			{
				// don't load any classes from the parent
				return new StandAloneClassLoader(buildClassPath());
			}
		});
	}
}
