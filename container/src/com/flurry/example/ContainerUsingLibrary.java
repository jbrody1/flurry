package com.flurry.example;

import com.external.library.Resource;
import com.external.library.ResourceLibrary;

@SuppressWarnings("unused")
public class ContainerUsingLibrary extends ContainerUsingInterface
{
	// simulate a 3rd party library that holds references to its clients
	private final Resource resource = ResourceLibrary.getResource(getClass());
}
