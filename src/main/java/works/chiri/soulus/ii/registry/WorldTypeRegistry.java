package works.chiri.soulus.ii.registry;

import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import works.chiri.soulus.ii.SoulusII;


@Mod.EventBusSubscriber(modid = SoulusII.ID, bus = Bus.MOD)
public class WorldTypeRegistry extends AutoRegistry<ForgeWorldType, Register.WorldType> {

	private static WorldTypeRegistry INSTANCE = new WorldTypeRegistry();

	@SubscribeEvent
	public static void onRegisterWorldType (final RegistryEvent.Register<ForgeWorldType> event) {
		INSTANCE.register(event.getRegistry());
	}

	@Override
	protected Class<Register.WorldType> getRegistrationAnnotationClass () {
		return Register.WorldType.class;
	}

	@Override
	protected boolean isRegistrationInstance (final Object instance) {
		return instance instanceof ForgeWorldType;
	}

	@Override
	protected ForgeWorldType getRegistrationInstance (final Object instance) {
		return (ForgeWorldType) instance;
	}

	@Override
	protected String getRegistryName (final ForgeWorldType instance, final Register.WorldType annotation) {
		return annotation.value();
	}

	@Override
	protected ForgeWorldType[] createEmptyRegistrationArray () {
		return new ForgeWorldType[0];
	}

}
