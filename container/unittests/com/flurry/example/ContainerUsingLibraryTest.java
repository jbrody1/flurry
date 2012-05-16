package com.flurry.example;

import com.flurry.example.Container.IClassLoaderFactory;

public class ContainerUsingLibraryTest extends ContainerTest
{
	// fully qualified class name of the module to load
	private static final String moduleClass = flurryPrefix + "ModuleUsingLibrary";

	@Override
	protected String getModuleClass()
	{
		return moduleClass;
	}

	@Override
	protected Container buildContainer(IClassLoaderFactory factory)
	{
		return new ContainerUsingLibrary(factory);
	}
}
