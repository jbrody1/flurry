package com.flurry.example.container;

import java.net.MalformedURLException;
import java.net.URL;

public class ContainerUsingLibraryTest extends ContainerUsingInterfaceTest
{
	// dependencies of the module to load
	private static final String libJar = "../lib/dist/lib.jar";

	// fully qualified class name of the module to load
	private static final String moduleClass = "com.flurry.example.module.ModuleUsingLibrary";

	@Override
	protected URL[] buildClassPath() throws MalformedURLException
	{
		return new URL[] { URLUtils.buildJarUrl(moduleJar),		// required
						   URLUtils.buildJarUrl(apiJar),		// not required, but exposes a ClassCastException with post-delegation
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
}
