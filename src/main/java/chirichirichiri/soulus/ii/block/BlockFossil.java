package chirichirichiri.soulus.ii.block;

import chirichirichiri.soulus.ii.registry.Register;
import chirichirichiri.soulus.ii.registry.registration.Registration;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ToolType;

public class BlockFossil {

	@Register.Block("banna") public static Registration.Block INSTANCE = Registration.Block.Factory()
		.mapColor(DyeColor.BLACK)
		.material(Material.BAMBOO)
		.properties(properties -> properties
			.harvestTool(ToolType.AXE)
			.lightValue(1))
		.create();
}
