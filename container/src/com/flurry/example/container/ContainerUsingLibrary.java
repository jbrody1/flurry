package com.flurry.example.container;

import com.external.library.ResourceLibrary;
import com.external.library.ResourceLibrary.Resource;

@SuppressWarnings("unused")
public class ContainerUsingLibrary extends Container
{
	private final Resource resource = ResourceLibrary.getResource(ContainerUsingLibrary.class);
}
