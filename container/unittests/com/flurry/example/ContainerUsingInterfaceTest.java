package com.flurry.example;

import com.flurry.example.Container.IClassLoaderFactory;

public class ContainerUsingInterfaceTest extends ContainerTest
{
	@Override
	protected Container buildContainer(IClassLoaderFactory factory)
	{
		return new ContainerUsingInterface(factory);
	}
}
