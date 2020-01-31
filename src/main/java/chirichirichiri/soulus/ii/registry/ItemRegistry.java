package chirichirichiri.soulus.ii.registry;

import chirichirichiri.soulus.ii.SoulusII;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SoulusII.ID, bus = Bus.MOD)
public class ItemRegistry extends AutoRegistry<Item, Register.Item> {

	private static ItemRegistry INSTANCE = new ItemRegistry();

	@SubscribeEvent
	public static void onRegisterItems (final RegistryEvent.Register<Item> event) {
		INSTANCE.register(event.getRegistry());
	}

	@Override
	protected Class<Register.Item> getRegistrationAnnotationClass () {
		return Register.Item.class;
	}

	@Override
	protected boolean isRegistrationInstance (final Object instance) {
		return instance instanceof Item;
	}

	@Override
	protected Item getRegistrationInstance (final Object instance) {
		return (Item) instance;
	}

	@Override
	protected String getRegistryName (final Item instance, final Register.Item annotation) {
		return annotation.value();
	}

	@Override
	protected Item[] createEmptyRegistrationArray () {
		return new Item[0];
	}
}
