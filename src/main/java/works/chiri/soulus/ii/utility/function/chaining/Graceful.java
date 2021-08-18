package works.chiri.soulus.ii.utility.function.chaining;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


public class Graceful {

	@SuppressWarnings("unchecked")
	public static <E extends Exception> void ignoring (
		final Runnable runnable, final Consumer<E> handler, final Class<E>... exceptionClasses
	) {
		try {
			runnable.run();
		}
		catch (final Exception e) {
			if (Arrays.stream(exceptionClasses).noneMatch(cls -> cls.isInstance(e)))
				throw new RuntimeException("Unhandled exception", e);
			handler.accept((E) e);
		}

	}

	@SuppressWarnings("unchecked")
	public static <E extends Exception> void ignoring (final Runnable runnable, final Class<E>... exceptionClasses) {
		ignoring(runnable, Graceful::handleDefault, exceptionClasses);
	}

	@SafeVarargs
	public static <T, R, E extends Exception> Function<T, R> map (
		final FunctionThrows<? super T, ? extends R, E> mapper, final Function<E, R> handler,
		final Class<E>... exceptionClasses
	) {
		return new Function<T, R>() {

			@SuppressWarnings("unchecked")
			@Override
			public R apply (final T t) {
				try {
					return mapper.apply(t);
				}
				catch (final Exception e) {
					if (Arrays.stream(exceptionClasses).noneMatch(cls -> cls.isInstance(e)))
						throw new RuntimeException("Unhandled exception", e);
					return handler.apply((E) e);
				}

			}

		};
	}

	@SuppressWarnings("unchecked")
	public static <T, R, E extends Exception> Function<T, R> map (
		final FunctionThrows<? super T, ? extends R, E> mapper, final Class<E>... exceptionClasses
	) {
		return map(mapper, exception -> (R) handleDefault(exception), exceptionClasses);
	}

	@FunctionalInterface
	public static interface FunctionThrows<T, R, E extends Exception> {

		public R apply (T t) throws E;

	}

	@SafeVarargs
	public static <T, E extends Exception> Predicate<T> filter (
		final PredicateThrows<? super T, E> mapper, final Consumer<E> handler, final Class<E>... exceptionClasses
	) {
		return new Predicate<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean test (final T t) {
				try {
					return mapper.test(t);
				}
				catch (final Exception e) {
					if (Arrays.stream(exceptionClasses).noneMatch(cls -> cls.isInstance(e)))
						throw new RuntimeException("Unhandled exception", e);
					handler.accept((E) e);
					return false;
				}

			}

		};
	}

	@SuppressWarnings("unchecked")
	public static <T, R, E extends Exception> Predicate<T> filter (
		final PredicateThrows<? super T, E> predicate, final Class<E>... exceptionClasses
	) {
		return filter(predicate, Graceful::handleDefault, exceptionClasses);
	}

	@FunctionalInterface
	public static interface PredicateThrows<T, E extends Exception> {

		public boolean test (final T t) throws E;

	}


	private static Object handleDefault (final Exception e) {
		// SoulusII.LOGGER.error("Encountered");
		return null;
	}

	final Map<Class<? extends Exception>, Consumer<Exception>> handlers = new HashMap<>();

	@SuppressWarnings("unchecked")
	public Graceful (final Class<? extends Exception>... exceptionClasses) {
		for (final Class<? extends Exception> exceptionClass : exceptionClasses)
			handlers.put(exceptionClass, Graceful::handleDefault);
	}

	@SuppressWarnings("unchecked")
	public Graceful handle (final Consumer<Exception> handler, final Class<? extends Exception>... exceptionClasses) {
		for (final Class<? extends Exception> exceptionClass : exceptionClasses)
			handlers.put(exceptionClass, handler);

		return this;
	}

	public Graceful run (final Runnable runnable) {
		try {
			runnable.run();
		}
		catch (final Exception e) {
			final Consumer<Exception> handler = handlers.entrySet().stream().filter(h -> h.getKey().isInstance(e))
				.findFirst().map(Map.Entry::getValue).orElse(null);

			if (handler == null)
				throw new RuntimeException("Unhandled exception", e);

			handler.accept(e);
		}

		return this;
	}

}
