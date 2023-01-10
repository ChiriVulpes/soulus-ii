package works.chiri.soulus.ii.server;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import works.chiri.soulus.ii.server.utility.Strings;


public class TypeScript {

	private static Map<Class<?>, String> NAMES = new HashMap<>();

	public static void registerName (final Class<?> cls, final String name) {
		NAMES.put(cls, name);
	}

	public static String generateMethodDeclaration (final Method method, String[] paramNames) {
		final StringBuilder builder = new StringBuilder();

		builder.append(method.getName()).append(" (");

		final Parameter[] params = method.getParameters();
		for (int i = 0; i < params.length; i++) {
			builder.append(paramNames != null && paramNames.length > i ? paramNames[i] : params[i].getName())
				.append(": ")
				.append(getName(params[i].getType()));

			if (i != params.length - 1)
				builder.append(", ");
		}

		builder.append("): Promise<")
			.append(getName(method.getReturnType()))
			.append(">");

		return builder.toString();
	}

	public static String getName (final Class<?> type) {
		if (type.isArray())
			return new StringBuilder()
				.append(getName(type.getComponentType()))
				.append("[]")
				.toString();

		final String name = type.getName();
		switch (name) {
			case "byte":
			case "short":
			case "int":
			case "long":
			case "float":
			case "double":
				return "number";

			case "char":
			case "java.lang.String":
				return "string";

			case "java.lang.Object":
				return "any";

			default:
				final String registeredName = NAMES.get(type);
				if (registeredName != null)
					return registeredName;

				return name;
		}

	}

	public static String generateEnum (final String enumName, final String... names) {
		return new StringBuilder()
			.append("export const enum ")
			.append(enumName)
			.append(" {\n")
			.append(Strings.tabbify(Stream.of(names)
				.collect(Collectors.joining(",\n"))))
			.append(",\n}")
			.toString();
	}

	public static String generateInterface (final String interfaceName, final Consumer<StringBuilder> generator) {
		final StringBuilder builder = new StringBuilder()
			.append("export interface ")
			.append(interfaceName)
			.append(" {\n");

		final StringBuilder insides = new StringBuilder();
		generator.accept(insides);

		return builder
			.append(Strings.tabbify(insides))
			.append("}")
			.toString();
	}

	public static String generateObject (final Consumer<StringBuilder> generator) {
		final StringBuilder builder = new StringBuilder()
			.append("{\n");

		final StringBuilder insides = new StringBuilder();
		generator.accept(insides);

		return builder
			.append(Strings.tabbify(insides))
			.append("}")
			.toString();
	}

	public static String generateObjectProperty (final String propertyName, final Consumer<StringBuilder> generator) {
		return generateProperty(propertyName, generator, ",\n");
	}

	public static String generateInterfaceProperty (final String propName, final Consumer<StringBuilder> generator) {
		return generateProperty(propName, generator, ";\n");
	}

	public static String generateProperty (final String name, final Consumer<StringBuilder> gen, final String end) {
		final StringBuilder builder = new StringBuilder()
			.append(name)
			.append(": ");

		gen.accept(builder);

		return builder
			.append(end)
			.toString();
	}

}
