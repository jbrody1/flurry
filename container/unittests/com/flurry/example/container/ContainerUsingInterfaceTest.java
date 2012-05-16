package com.flurry.example.container;

import com.flurry.example.container.ContainerUsingInterface;
import com.flurry.example.container.IContainer;


public class ContainerUsingInterfaceTest extends ContainerTest
{
	@Override
	protected IContainer buildContainer()
	{
		return new ContainerUsingInterface();
	}
}
