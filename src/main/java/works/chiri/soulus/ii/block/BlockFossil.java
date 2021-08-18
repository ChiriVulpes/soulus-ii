package works.chiri.soulus.ii.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ToolType;
import works.chiri.soulus.ii.registry.Register;
import works.chiri.soulus.ii.registry.registration.Registration;


public class BlockFossil {

	@Register.Block("banna")
	public static Registration.Block INSTANCE = Registration.Block.Factory()
		.mapColor(DyeColor.BLACK)
		.material(Material.BAMBOO)
		.properties(properties -> properties
			.harvestTool(ToolType.AXE)
			.lightLevel(light -> 1))
		.create();

}
