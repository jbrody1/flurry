package com.flurry.example;

public interface IContainer
{
	public void loadModule(String moduleClass) throws Exception;
	public void clearModules();
}
