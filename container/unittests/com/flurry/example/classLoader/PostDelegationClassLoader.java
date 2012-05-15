package com.flurry.example.classLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class PostDelegationClassLoader extends URLClassLoader
{
	public PostDelegationClassLoader(URL[] urls)
	{
		super(urls);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
        Class<?> c = findLoadedClass(name);

		if (c == null)
		{
            try
            {
                c = findClass(name);

        		if (resolve)
        		{
        		    resolveClass(c);
        		}
            }
            catch (ClassNotFoundException e)
            {
            }

            if (c == null)
            {
                c = super.loadClass(name, resolve);
            }
        }

		return c;
    }
}
