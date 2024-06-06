package net.potato.enchantableheads;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantableHeads implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("enchantableheads");

	@Override
	public void onInitialize() {
		LOGGER.info("Enchantable Heads has initialised!");
	}
}