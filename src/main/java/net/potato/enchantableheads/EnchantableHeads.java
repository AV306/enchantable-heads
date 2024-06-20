package net.potato.enchantableheads;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.minecraft.server.command.CommandManager.*;

public class EnchantableHeads implements ModInitializer {
	public static final String MOD_ID = "enchantableheads";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Configuration variables, set by ConfigManager
	public static boolean ALLOW_HELMET_ENCHANTMENTS = false;
	public static boolean ALLOW_CHESTPLATE_ENCHANTMENTS = false;
	public static boolean ALLOW_LEGGINGS_ENCHANTMENTS = false;
	public static boolean ALLOW_BOOTS_ENCHANTMENTS = false;
	public static boolean ALLOW_GENERAL_ARMOR_ENCHANTMENTS = false;
	public static boolean ALLOW_NON_ARMOR_ENCHANTMENTS = false;

	public static ConfigManager CONFIG_MANAGER = null;

	@Override
	public void onInitialize() {
		// Setup config manager & read config file
		CONFIG_MANAGER = new ConfigManager( FabricLoader.getInstance().getConfigDir().resolve("enchantableheads_config.properties").toString() );

		LOGGER.info("Enchantable Heads has initialised!");

		ModMetadata modMetadata = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata();
		String name = modMetadata.getName();
		String version = modMetadata.getVersion().getFriendlyString();

		// Config reload command
		CommandRegistrationCallback.EVENT.register( (dispatcher, registryAccess, environment) -> dispatcher.register(
				literal("enchantableheads")
						.executes(context -> {
							context.getSource().sendMessage(Text.translatable("text.enchantableheads.info", name, version));
							return 0;
						})
						.then(literal("reloadconfig").requires(source -> source.hasPermissionLevel(4)).executes( context -> {
							CONFIG_MANAGER.readConfigFile();
							context.getSource().sendMessage(Text.translatable("text.enchantableheads.configreloaded"));
							return 0;
						}))
		));
	}
}