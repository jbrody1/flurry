package com.flurry.example.classLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class FilterClassLoader extends URLClassLoader
{
	private final String parentPrefix;

	public FilterClassLoader(URL[] urls, ClassLoader parent, String parentPrefix)
	{
		super(urls, parent);

		this.parentPrefix = parentPrefix;
	}

	@Override
	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		Class<?> c = findLoadedClass(name);

		if (c == null)
		{
			boolean parentFirst = name.startsWith(parentPrefix);

			if (parentFirst)
			{
				try
				{
					c = super.loadClass(name, resolve);
				}
				catch (ClassNotFoundException e)
				{
				}
			}
			
			if (c == null)
			{
				try
				{
					c = findClass(name);
				}
				catch (ClassNotFoundException e)
				{
					if (parentFirst)
					{
						throw e;
					}
					else
					{
						c = super.loadClass(name, resolve);
					}
				}

				if (resolve)
				{
				    resolveClass(c);
				}
			}
		}
		
		return c;
	}
}
