package com.flurry.example;


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
	protected IContainer buildContainer()
	{
		return new ContainerUsingLibrary();
	}
}
