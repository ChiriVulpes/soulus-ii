package works.chiri.soulus.ii.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.utility.function.chaining.Graceful;
import works.chiri.soulus.ii.utility.function.chaining.Splat;
import works.chiri.soulus.ii.utility.translation.annotation.Annotations;


public abstract class AutoRegistry<T extends IForgeRegistryEntry<T>, A extends Annotation> {

	protected abstract Class<A> getRegistrationAnnotationClass ();

	protected abstract boolean isRegistrationInstance (final Object instance);

	protected abstract T getRegistrationInstance (final Object instance);

	protected abstract String getRegistryName (final T instance, final A annotation);

	protected abstract T[] createEmptyRegistrationArray ();

	@SuppressWarnings("unchecked")
	protected void register (final IForgeRegistry<T> registry) {
		Annotations.getAnnotatedFields(getRegistrationAnnotationClass()).filter(Graceful.filter(this::filter))
			.map(Graceful.map(this::initialize))
			.collect(Splat.intoConsumer(createEmptyRegistrationArray(), registry::registerAll));
	}

	private boolean filter (final Field field) throws IllegalAccessException {
		final Object instance = field.get(null);
		if (isRegistrationInstance(instance))
			return true;

		SoulusII.LOGGER
			.error("Non-registration field '" + field.getName() + "' in '" + field.getDeclaringClass().getName()
				+ "' annotated with @'" + getRegistrationAnnotationClass().getName() + "'");
		return false;
	}

	@SuppressWarnings("unchecked")
	private T initialize (final Field field) throws IllegalAccessException {
		final T block = getRegistrationInstance(field.get(null));

		final Annotation registration = field.getAnnotation(getRegistrationAnnotationClass());
		String registryName = getRegistryName(block, (A) registration);
		if (!registryName.contains(":"))
			registryName = SoulusII.ID + ":" + registryName;

		block.setRegistryName(new ResourceLocation(registryName));
		return block;
	}

}
