package works.chiri.soulus.ii.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.registry.registration.IRegistration;
import works.chiri.soulus.ii.utility.function.chaining.Graceful;
import works.chiri.soulus.ii.utility.function.chaining.Splat;
import works.chiri.soulus.ii.utility.translation.annotation.Annotations;


@SuppressWarnings("unchecked")
public abstract class AutoRegistry<T extends IForgeRegistryEntry<T>, A extends Annotation> {

	protected abstract Class<A> getRegistrationAnnotationClass ();

	protected abstract boolean isRegistrationInstance (final Object instance);

	protected abstract T getRegistrationInstance (final Object instance);

	protected abstract String getRegistryName (final T instance, final A annotation);

	protected abstract T[] createEmptyRegistrationArray ();

	protected void register (final IForgeRegistry<T> registry) {
		getRegistrations()
			.map(Graceful.map(this::initialize))
			.collect(Splat.intoConsumer(createEmptyRegistrationArray(), registry::registerAll));
	}

	protected Stream<Pair<Field, T>> getRegistrations () {
		return Annotations.getAnnotatedFields(getRegistrationAnnotationClass())
			.map(Graceful.map(this::get))
			.filter(Graceful.filter(this::filter));
	}

	private Pair<Field, T> get (final Field field) throws IllegalAccessException {
		return new Pair<>(field, getRegistrationInstance(field.get(null)));
	}

	private boolean filter (final Pair<Field, T> pair) throws IllegalAccessException {
		if (isRegistrationInstance(pair.getSecond()))
			return true;

		final Field field = pair.getFirst();
		SoulusII.LOGGER
			.error("Non-registration field '" + field.getName() + "' in '" + field.getDeclaringClass().getName()
				+ "' annotated with @'" + getRegistrationAnnotationClass().getName() + "'");
		return false;
	}

	private T initialize (final Pair<Field, T> pair) throws IllegalAccessException {
		final Field field = pair.getFirst();
		final T registration = pair.getSecond();

		final Annotation annotation = field.getAnnotation(getRegistrationAnnotationClass());
		String registryName = getRegistryName(registration, (A) annotation);
		if (!registryName.contains(":"))
			registryName = SoulusII.ID + ":" + registryName;

		registration.setRegistryName(new ResourceLocation(registryName));

		if (registration instanceof IRegistration)
			((IRegistration<?>) registration).onRegistration();

		return registration;
	}

}
