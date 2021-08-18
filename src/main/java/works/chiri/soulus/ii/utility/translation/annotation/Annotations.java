package works.chiri.soulus.ii.utility.translation.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.objectweb.asm.Type;

import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.utility.function.chaining.Graceful;


public class Annotations {

	private static ModFileScanData scanData = null;

	private static ModFileScanData getScanData () {
		if (scanData == null)
			scanData = SoulusII.INFO.getOwningFile().getFile().getScanResult();

		return scanData;
	}

	public static Stream<AnnotationData> getAnnotations (final Class<? extends Annotation> annotationClass) {
		return getScanData().getAnnotations().stream()
			.filter(annotation -> annotation.getAnnotationType().equals(Type.getType(annotationClass)));
	}

	/**
	 * @return A stream of all classes annotated with the given annotation
	 */
	@SuppressWarnings("unchecked")
	public static Stream<Class<?>> getAnnotatedClasses (final Class<? extends Annotation> annotationClass) {
		return getAnnotations(annotationClass).filter(a -> a.getTargetType() == ElementType.TYPE)
			.map(annotation -> annotation.getClassType().getClassName()).map(Graceful.map(Class::forName));
	}

	/**
	 * @return A map of all fields annotated with the given annotation, indexed by their class
	 */
	@SuppressWarnings("unchecked")
	public static Map<Class<?>, Stream<Field>> getAnnotatedFieldsByClass (
		final Class<? extends Annotation> annotationClass
	) {
		return getAnnotations(annotationClass).filter(a -> a.getTargetType() == ElementType.FIELD)
			.collect(Collectors
				.groupingBy(Graceful.map(annotation -> Class.forName(annotation.getClassType().getClassName()))))
			.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
				.map(Graceful.map(annotation -> entry.getKey().getDeclaredField(annotation.getMemberName())))
				.filter(field -> Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()))));
	}

	/**
	 * @return A stream of all fields annotated with the given annotation
	 */
	public static Stream<Field> getAnnotatedFields (final Class<? extends Annotation> annotationClass) {
		return getAnnotatedFieldsByClass(annotationClass).values().stream().flatMap(Function.identity());
	}

	/**
	 * @return A map of all methods annotated with the given annotation, indexed by their class
	 */
	@SuppressWarnings("unchecked")
	public static Map<Class<?>, Stream<Method>> getAnnotatedMethods (
		final Class<? extends Annotation> annotationClass
	) {
		return getAnnotations(annotationClass).filter(a -> a.getTargetType() == ElementType.METHOD)
			.map(annotation -> annotation.getClassType().getClassName()).map(Graceful.map(Class::forName)).distinct()
			.collect(
				Collectors.toMap(Function.identity(),
					cls -> Arrays.stream(cls.getDeclaredMethods())
						.filter(method -> Modifier.isPublic(method.getModifiers())
							&& Modifier.isStatic(method.getModifiers()) //
							&& method.isAnnotationPresent(annotationClass))));
	}

}
