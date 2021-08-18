package works.chiri.soulus.ii.utility.translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.text.TranslationTextComponent;


public class Translation {

	public static String localise (final String id, final Object... args) {
		return new TranslationTextComponent(id, args).getUnformattedComponentText();
	}

	public static boolean hasLocalization (final String id) {
		return localise(id).equalsIgnoreCase(id);
	}

	private final String id;
	private final List<Object> args = new ArrayList<>();

	public Translation (final String id, final Object... args) {
		this.id = id;
		args(args);
	}

	public Translation args (final Object... args) {
		Arrays.stream(args).forEach(this.args::add);
		return this;
	}

	public String localise (final Object... args) {
		args(args);
		return localise(id, this.args.toArray(new Object[0]));
	}

	public boolean exists () {
		return !localise().equalsIgnoreCase(id);
	}

}
