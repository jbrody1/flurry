package com.flurry.example.classLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class ConditionalDelegationClassLoader extends URLClassLoader
{
	private final String parentPrefix;
	private final String childPrefix;

	public ConditionalDelegationClassLoader(URL[] urls, String parentPrefix)
	{
		this(urls, parentPrefix, null);
	}

	public ConditionalDelegationClassLoader(URL[] urls, String parentPrefix, String childPrefix)
	{
		super(urls);

		this.parentPrefix = parentPrefix;
		this.childPrefix = childPrefix;
	}

	@Override
	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		Class<?> c = findLoadedClass(name);

		if (c == null)
		{
			boolean parentFirst = name.startsWith(parentPrefix) && (childPrefix == null || !name.startsWith(childPrefix));

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
