package works.chiri.soulus.ii.utility.function.chaining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import works.chiri.soulus.ii.utility.function.Functions.Varargs;


public class Splat<T, R> implements Collector<T, List<T>, R> {

	public static <T> Collector<T, ?, Void> intoConsumer (final T[] arr, final Varargs.Consumer<? super T> consumer) {
		return new Splat<>(arr, value -> {
			consumer.accept(value);
			return null;
		});
	}

	public Splat (final T[] arr, final Varargs.Function<? super T, R> handler) {
		this((Supplier<List<T>>) ArrayList::new, (into, value) -> into.add(value), (left, right) -> {
			left.addAll(right);
			return left;
		}, result -> handler.apply(result.toArray(arr)), CH_NOID);
	}


	////////////////////////////////////
	// Implementation
	//

	// private static final Set<Collector.Characteristics> CH_CONCURRENT_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
	// private static final Set<Collector.Characteristics> CH_CONCURRENT_NOID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED));
	// private static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
	// private static final Set<Collector.Characteristics> CH_UNORDERED_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
	private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

	// @SuppressWarnings("unchecked")
	// private static <I, R> Function<I, R> castingIdentity () {
	// 	return i -> (R) i;
	// }

	private final Supplier<List<T>> supplier;
	private final BiConsumer<List<T>, T> accumulator;
	private final BinaryOperator<List<T>> combiner;
	private final Function<List<T>, R> finisher;
	private final Set<Characteristics> characteristics;

	private Splat (
		Supplier<List<T>> supplier, BiConsumer<List<T>, T> accumulator, BinaryOperator<List<T>> combiner,
		Function<List<T>, R> finisher, Set<Characteristics> characteristics
	) {
		this.supplier = supplier;
		this.accumulator = accumulator;
		this.combiner = combiner;
		this.finisher = finisher;
		this.characteristics = characteristics;
	}

	// private Splat (Supplier<List<T>> supplier, BiConsumer<List<T>, T> accumulator, BinaryOperator<List<T>> combiner, Set<Characteristics> characteristics) {
	// 	this(supplier, accumulator, combiner, castingIdentity(), characteristics);
	// }

	@Override
	public BiConsumer<List<T>, T> accumulator () {
		return accumulator;
	}

	@Override
	public Supplier<List<T>> supplier () {
		return supplier;
	}

	@Override
	public BinaryOperator<List<T>> combiner () {
		return combiner;
	}

	@Override
	public Function<List<T>, R> finisher () {
		return finisher;
	}

	@Override
	public Set<Characteristics> characteristics () {
		return characteristics;
	}

}
