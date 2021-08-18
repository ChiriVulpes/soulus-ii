package works.chiri.soulus.ii.utility.function;

import java.util.function.Consumer;


public class Functions {

	public static <T> java.util.function.Function<T, Void> fromConsumer (final Consumer<T> consumer) {
		return value -> {
			consumer.accept(value);
			return null;
		};
	}

	public static class Varargs {

		public static <T> Varargs.Function<T, Void> fromConsumer (final Varargs.Consumer<T> consumer) {
			return value -> {
				consumer.accept(value);
				return null;
			};
		}

		@FunctionalInterface
		public static interface Function<T, R> {

			@SuppressWarnings("unchecked")
			public R apply (final T... args);

		}

		@FunctionalInterface
		public static interface Consumer<T> {

			@SuppressWarnings("unchecked")
			public void accept (final T... args);

		}

	}

}
