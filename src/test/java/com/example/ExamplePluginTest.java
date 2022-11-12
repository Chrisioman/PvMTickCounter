package com.example;

import com.PvMTickCounter.TickCounterPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TickCounterPlugin.class);
		RuneLite.main(args);
	}
}