package works.chiri.soulus.ii.registry.registration;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import net.minecraftforge.registries.IForgeRegistryEntry;


public abstract class RegistrationFactory<T extends IForgeRegistryEntry<T>, R extends T, F extends RegistrationFactory<T, R, F>> {

	protected Supplier<? extends T> supplier = this::construct;
	protected Class<? extends T> cls = getDefaultRegistrationClass();

	public RegistrationFactory () {}

	public RegistrationFactory (final Class<? extends R> cls) {
		this.cls = cls;
	}

	public RegistrationFactory (final Supplier<? extends T> supplier) {
		this.supplier = supplier;
	}

	////////////////////////////////////
	// Name
	//

	// private ResourceLocation name = null;

	// @SuppressWarnings("unchecked")
	// public R name (final String name) {
	// 	this.name = new ResourceLocation(name);
	// 	return (R) this;
	// }

	// @SuppressWarnings("unchecked")
	// public R name (final ResourceLocation name) {
	// 	this.name = name;
	// 	return (R) this;
	// }


	////////////////////////////////////
	// Initialisation
	//

	@SuppressWarnings("unchecked")
	public final R create () {
		final T registration = supplier.get();
		initialise(registration);
		return (R) registration;
	}

	protected abstract Class<? extends T> getDefaultRegistrationClass ();

	protected void initialise (final T registration) {}

	protected Class<?>[] getConstructorParameterTypes () {
		return new Class<?>[0];
	}

	protected Object[] getConstructorParameters () {
		return new Object[0];
	}

	private T construct () {
		Class<? extends T> useClass = cls;
		if (useClass == null)
			useClass = this.cls;

		T registration;
		try {
			registration = (T) useClass.getDeclaredConstructor(getConstructorParameterTypes())
				.newInstance(getConstructorParameters());

		}
		catch (final NoSuchMethodException | InstantiationException | IllegalAccessException
			| InvocationTargetException e) {
			throw new IllegalStateException("Unable to call constructor on " + getClass().getName() + " class", e);
		}

		return registration;
	}

}
