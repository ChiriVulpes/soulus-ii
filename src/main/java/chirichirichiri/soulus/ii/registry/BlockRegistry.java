package chirichirichiri.soulus.ii.registry;

import chirichirichiri.soulus.ii.SoulusII;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SoulusII.ID, bus = Bus.MOD)
public class BlockRegistry extends AutoRegistry<Block, Register.Block> {

	private static BlockRegistry INSTANCE = new BlockRegistry();

	@SubscribeEvent
	public static void onRegisterBlocks (final RegistryEvent.Register<Block> event) {
		INSTANCE.register(event.getRegistry());
	}

	@Override
	protected Class<Register.Block> getRegistrationAnnotationClass () {
		return Register.Block.class;
	}

	@Override
	protected boolean isRegistrationInstance (final Object instance) {
		return instance instanceof Block;
	}

	@Override
	protected Block getRegistrationInstance (final Object instance) {
		return (Block) instance;
	}

	@Override
	protected String getRegistryName (final Block instance, final Register.Block annotation) {
		return annotation.value();
	}

	@Override
	protected Block[] createEmptyRegistrationArray () {
		return new Block[0];
	}
}
