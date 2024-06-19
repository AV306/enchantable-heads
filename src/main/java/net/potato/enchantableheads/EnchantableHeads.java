package net.potato.enchantableheads;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantableHeads implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("enchantableheads");

	public static boolean ALLOW_HELMET_ENCHANTMENTS = false;
	public static boolean ALLOW_CHESTPLATE_ENCHANTMENTS = false;
	public static boolean ALLOW_LEGGINGS_ENCHANTMENTS = false;
	public static boolean ALLOW_BOOTS_ENCHANTMENTS = false;
	public static boolean ALLOW_GENERAL_ARMOR_ENCHANTMENTS = false;
	public static boolean ALLOW_NON_ARMOR_ENCHANTMENTS = false;

	public static ConfigManager CONFIG_MANAGER = null;

//	public static KeyBinding CONFIG_RELOAD_KEYBIND = null;

	@Override
	public void onInitialize() {
		CONFIG_MANAGER = new ConfigManager( FabricLoader.getInstance().getConfigDir().resolve("enchantableheads_config.properties").toString() );

		/*LOGGER.info("{}", ALLOW_HELMET_ENCHANTMENTS);
		LOGGER.info("{}", ALLOW_CHESTPLATE_ENCHANTMENTS);
		LOGGER.info("{}", ALLOW_LEGGINGS_ENCHANTMENTS);
		LOGGER.info("{}", ALLOW_GENERAL_ARMOR_ENCHANTMENTS);
		LOGGER.info("{}", ALLOW_NON_ARMOR_ENCHANTMENTS);*/

		LOGGER.info("Enchantable Heads has initialised!");

		CommandRegistrationCallback.EVENT.register( (dispatcher, registryAccess, environment) -> dispatcher.register(
				literal( "enchantableheads" )
						.executes( context -> {
							context.getSource().sendMessage(Text.translatable("text.enchantableheads.info"));
							return 0;
						})
						.then( literal( "reloadconfig").requires(source -> source.hasPermissionLevel(4)).executes( context -> {
							CONFIG_MANAGER.readConfigFile();
							context.getSource().sendMessage(Text.translatable("text.enchantableheads.configreloaded"));
							return 0;
						}) )
		));
	}
}