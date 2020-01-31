package chirichirichiri.soulus.ii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

@Mod(SoulusII.ID)
public class SoulusII {

	public static final String ID = "soulus-ii";
	public static String VERSION;
	public static String NAME;
	public static ModInfo INFO;

	public static final Logger LOGGER = LogManager.getLogger(ID);

	public SoulusII () {
		INFO = ModList.get().getMods().stream().filter(i -> i.getModId().equals(ID)).findFirst().orElse(null);
		VERSION = INFO.getVersion().toString();
		NAME = INFO.getDisplayName();
	}

}
