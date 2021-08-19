package works.chiri.soulus.ii.registry;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.registry.registration.Registration;
import works.chiri.soulus.ii.registry.registration.block.BlockFactory;
import works.chiri.soulus.ii.utility.function.chaining.Splat;


@Mod.EventBusSubscriber(modid = SoulusII.ID, bus = Bus.MOD)
public class BlockRegistry extends AutoRegistry<Block, Register.Block> {

	private static BlockRegistry INSTANCE = new BlockRegistry();

	@SubscribeEvent
	public static void onRegisterBlocks (final RegistryEvent.Register<Block> event) {
		INSTANCE.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void onRegisterItems (final RegistryEvent.Register<Item> event) {
		for (Map.Entry<Block, Registration.BlockItem> entry : BlockFactory.ITEMS_BY_BLOCK.entrySet()) {
			SoulusII.LOGGER.info(entry.getKey().getRegistryName() + ", " + entry.getValue().getRegistryName());
		}

		final IForgeRegistry<Item> registry = event.getRegistry();
		INSTANCE.getRegistrations()
			.map(pair -> pair.getSecond())
			.filter(block -> block instanceof Registration.Block)
			.map(block -> BlockFactory.ITEMS_BY_BLOCK.get(block)
				.setRegistryName(block.getRegistryName()))
			.filter(item -> item != null)
			.collect(Splat.intoConsumer(new Item[0], registry::registerAll));
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
