package works.chiri.soulus.ii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import works.chiri.soulus.ii.server.ActionManager;
import works.chiri.soulus.ii.server.Serialiser;
import works.chiri.soulus.ii.server.Server;


@Mod(SoulusII.ID)
public class SoulusII {

	public static final String ID = "soulus-ii";
	public static String VERSION;
	public static String NAME;
	public static ModInfo INFO;

	public static final Logger LOGGER = LogManager.getLogger(ID);

	public static ResourceLocation location (final String location) {
		return new ResourceLocation(ID, location);
	}

	public SoulusII () {
		INFO = ModList.get()
			.getMods()
			.stream()
			.filter(i -> i.getModId().equals(ID))
			.findFirst()
			.orElse(null);
		NAME = INFO.getDisplayName();
		VERSION = INFO.getVersion().toString();
		VERSION = INFO.getVersion().toString();
		MinecraftForge.EVENT_BUS.addListener(SoulusII::onServerStart);
		MinecraftForge.EVENT_BUS.addListener(SoulusII::onServerStop);
		LOGGER.info(Serialiser.getTypeScriptDeclarations());
		LOGGER.info(ActionManager.getTypeScriptDeclarations());
	}

	public static Server server;

	public static void onServerStart (FMLServerStartedEvent event) {
		LOGGER.info("server started");
		server = new Server(6666);
		server.start();
	}

	public static void onServerStop (FMLServerStoppingEvent event) {
		LOGGER.info("server stopped");
		server.interrupt();
	}

}
