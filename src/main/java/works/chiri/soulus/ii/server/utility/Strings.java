package works.chiri.soulus.ii.server.utility;

public class Strings {

	public static StringBuilder tabbify (final StringBuilder builder) {
		return tabbify(builder, 1);
	}

	public static StringBuilder tabbify (final StringBuilder builder, int amount) {
		final String insert = "\t".repeat(amount);

		if (builder.length() > 0)
			// indent start
			builder.insert(0, insert);

		for (int i = 0; i < builder.length(); i++)
			// indent newlines
			if (builder.charAt(i) == '\n' && i < builder.length() - 1)
				builder.insert(++i, insert);

		return builder;
	}

	public static String tabbify (final String string) {
		return tabbify(string, 1);
	}

	public static String tabbify (final String string, int amount) {
		final StringBuilder builder = new StringBuilder(string);
		return tabbify(builder, amount).toString();
	}

}
