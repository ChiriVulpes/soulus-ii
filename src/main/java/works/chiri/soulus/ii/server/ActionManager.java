package works.chiri.soulus.ii.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import works.chiri.soulus.ii.SoulusII;
import works.chiri.soulus.ii.server.actions.Blocks;


public class ActionManager {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Action {

		String[] value ();

	}

	private static String DECLARATIONS;

	private static void register (final StringBuilder declarationBuilder) {
		registerActions(Blocks.class, declarationBuilder);
	}

	public static String getTypeScriptDeclarations () {
		if (DECLARATIONS != null)
			return DECLARATIONS;

		return DECLARATIONS = TypeScript.generateInterface("Actions", ActionManager::register);
	}

	public static Object execute (final String name, final Object... args) {
		final List<ActionRegististration> matchingActions = new ArrayList<>();
		for (final ActionRegististration reg : REGISTRATIONS) {
			if (!reg.name.equals(name))
				continue;

			try {
				return reg.execute(args);
			}
			catch (final IllegalAccessException | IllegalArgumentException e) {
				// assume this was the incorrect method to call and continue
				matchingActions.add(reg);
			}

		}

		if (matchingActions.size() == 0)
			SoulusII.LOGGER.error("There is no action by the name '" + name + "'");
		else
			SoulusII.LOGGER.error("No '" + name + "' actions accepted the given arguments. Did you mean:\n"
				+ matchingActions.stream()
					.map(action -> "- " + action.method.toGenericString())
					.collect(Collectors.joining("\n")));

		return null;
	}

	private static class ActionRegististration {

		public final Method method;
		public final String name;
		public final int id;
		private final String[] parameterNames;

		public ActionRegististration (final Action action, final Method method, final int id) {
			this.parameterNames = action.value();
			this.method = method;
			this.name = method.getName();
			this.id = id;
		}

		public Object execute (final Object... args) throws IllegalAccessException, IllegalArgumentException {
			try {
				return method.invoke(null, args);
			}
			catch (final InvocationTargetException e) {
				SoulusII.LOGGER.error("Unable to execute action '" + name + "'", e);
				return null;
			}

		}

		public String generateTypeScriptDeclaration () {
			return TypeScript.generateMethodDeclaration(method, parameterNames);
		}

	}

	private static int ID = 0;
	private static List<ActionRegististration> REGISTRATIONS = new ArrayList<>();


	private static void registerActions (final Class<?> cls, final StringBuilder declarationBuilder) {
		declarationBuilder.append(TypeScript.generateInterfaceProperty(cls.getSimpleName(),
			propertyDeclBuilder -> propertyDeclBuilder.append(TypeScript.generateObject(objectDeclBuilder -> {
				for (final Method method : cls.getMethods()) {
					final Action action = method.getAnnotation(Action.class);
					if (action == null)
						continue;

					final ActionRegististration reg = new ActionRegististration(action, method, ID++);
					REGISTRATIONS.add(reg);
					objectDeclBuilder.append(reg.generateTypeScriptDeclaration()).append(";\n");
				}

			}))));
	}

}
