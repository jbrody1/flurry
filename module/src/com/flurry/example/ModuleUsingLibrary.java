package com.flurry.example;

import com.external.library.Resource;
import com.external.library.ResourceLibrary;

@SuppressWarnings("unused")
public class ModuleUsingLibrary extends Module
{
	private final Resource resource = ResourceLibrary.getResource(Module.class);
}
