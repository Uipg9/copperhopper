package dev.brian.copperhopper;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Copperhopper implements ModInitializer {
	public static final String MOD_ID = "copperhopper";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	/** Translation key following the vanilla convention: {@code category.modid.path}. */
	public static String langKey(String category, String path) {
		return category + "." + MOD_ID + "." + path;
	}
}
