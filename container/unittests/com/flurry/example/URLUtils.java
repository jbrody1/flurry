package com.flurry.example;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

class URLUtils
{
	static URL buildJarUrl(String path) throws MalformedURLException
	{
		return new URL("jar", "", "file:" + new File(path).getAbsolutePath() + "!/");
	}
}
