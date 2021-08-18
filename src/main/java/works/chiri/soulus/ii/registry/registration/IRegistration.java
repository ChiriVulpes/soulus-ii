package works.chiri.soulus.ii.registry.registration;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import works.chiri.soulus.ii.SoulusII;


public interface IRegistration<T extends IForgeRegistryEntry<T>> extends IForgeRegistryEntry<T> {

	public abstract T setRegistryName (final String mod, final String name);

	public abstract T setRegistryName (final String name);

	public abstract T setRegistryName (final ResourceLocation name);

	public abstract ResourceLocation getRegistryName ();

	@SuppressWarnings("unchecked")
	public default T setName (final String name) {
		setRegistryName(SoulusII.ID, name);
		// setUnlocalizedName(getRegistryName().toString());
		return (T) this;
	}

}
