package works.chiri.soulus.ii.server.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;


public class Collectors2 {

	@SafeVarargs
	public static <T> Collector<T, List<T>, Stream<T>> concat (final Stream<T>... streams) {
		return Collector.of( () -> new ArrayList<>(),
			(list, item) -> list.add(item),
			(list1, list2) -> {
				final List<T> newList = new ArrayList<>();
				newList.addAll(list1);
				newList.addAll(list2);
				return newList;
			},
			list -> {
				Stream<T> stream = list.stream();
				for (final Stream<T> nextStream : streams)
					stream = Stream.concat(stream, nextStream);
				return stream;
			});
	}

}
