package works.chiri.soulus.ii.registry.registration;

import java.lang.reflect.InvocationTargetException;

import net.minecraftforge.registries.IForgeRegistryEntry;


public abstract class RegistrationFactory<T extends IForgeRegistryEntry<T>, R extends T, F extends RegistrationFactory<T, R, F>> {

	private Class<? extends T> cls = getDefaultRegistrationClass();

	public RegistrationFactory () {}

	public RegistrationFactory (final Class<? extends R> cls) {
		this.cls = cls;
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
		return (R) create(cls);
	}

	@SuppressWarnings("unchecked")
	public final <C extends T> C create (final Class<C> cls) {
		Class<? extends T> useClass = cls;
		if (useClass == null)
			useClass = this.cls;

		C registration;
		try {
			registration = (C) useClass.getDeclaredConstructor(getConstructorParameterTypes())
				.newInstance(getConstructorParameters());

		}
		catch (final NoSuchMethodException | InstantiationException | IllegalAccessException
			| InvocationTargetException e) {
			throw new IllegalStateException("Unable to call constructor on " + getClass().getName() + " class");
		}

		// if (name == null)
		// 	throw new IllegalStateException(getClass().getName() + " registration requires a name");

		// registration.setRegistryName(name);
		initialise(registration);

		return registration;
	}

	protected abstract Class<? extends T> getDefaultRegistrationClass ();

	protected void initialise (final T registration) {}

	protected Class<?>[] getConstructorParameterTypes () {
		return new Class<?>[0];
	}

	protected Object[] getConstructorParameters () {
		return new Object[0];
	}

}
