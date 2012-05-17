package com.flurry.example.module;

import com.external.library.ResourceLibrary;
import com.external.library.ResourceLibrary.Resource;

@SuppressWarnings("unused")
public class ModuleUsingLibrary extends Module
{
	private final Resource resource = ResourceLibrary.getResource(ModuleUsingLibrary.class);
}
