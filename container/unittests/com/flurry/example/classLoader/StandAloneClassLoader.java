package com.flurry.example.classLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class StandAloneClassLoader extends URLClassLoader
{
	public StandAloneClassLoader(URL[] urls)
	{
		// pass null as our parent so we load all classes ourselves
		super(urls, null);
	}
}
