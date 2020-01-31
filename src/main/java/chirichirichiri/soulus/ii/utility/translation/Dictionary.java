package chirichirichiri.soulus.ii.utility.translation;

public enum Dictionary {

	/**
	 * @param registryName — IE <code>minecraft:bat</code>
	 */
	ENTITY ("entity.%1$s.name"),
	/**
	 * @param registryName — IE <code>soulus:fossil_dirt</code>
	 */
	DESCRIPTION ("jei.description.%1$s"),
	/**
	 * @param registryName — IE <code>soulus:teleport_multiple</code>
	 */
	ADVANCEMENT_TITLE ("advancement.%1$s.title"),
	/**
	 * @param registryName — IE <code>soulus:teleport_multiple</code>
	 */
	ADVANCEMENT_DESCRIPTION ("advancement.%1$s.description");

	private final String key;

	private Dictionary (final String key) {
		this.key = key;
	}

	public Translation translation (final Object... args) {
		return new Translation(String.format(key, args));
	}
}
