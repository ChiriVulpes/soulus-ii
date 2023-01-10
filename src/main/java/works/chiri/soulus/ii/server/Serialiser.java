package works.chiri.soulus.ii.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import works.chiri.soulus.ii.server.serialisers.BlockStateSerialiser;
import works.chiri.soulus.ii.server.utility.Collectors2;


public abstract class Serialiser {

	private static enum PrimitiveSerialisationCode {
			Void,
			Boolean,
			Character,
			Byte,
			Short,
			Integer,
			Long,
			Float,
			Double,
			Array,
	}

	private static final Map<Class<?>, Serialiser> SERIALISERS = new HashMap<>();
	private static byte nextId = (byte) PrimitiveSerialisationCode.values().length;

	public static void register () {
		registerSerialiser(BlockState.class, new BlockStateSerialiser());
	}

	public static void registerSerialiser (final Class<?> cls, final Serialiser serialiser) {
		SERIALISERS.put(cls, serialiser);
		serialiser.ID = nextId++;
	}

	private static String DECLARATIONS;

	public static String getTypeScriptDeclarations () {
		if (DECLARATIONS != null)
			return DECLARATIONS;

		register();

		return DECLARATIONS = TypeScript.generateEnum("SerialisationCode",
			Stream.of(PrimitiveSerialisationCode.values())
				.map(primitiveCode -> primitiveCode.name())
				.collect(Collectors2.concat(SERIALISERS.entrySet().stream()
					.sorted( (a, b) -> a.getValue().ID - b.getValue().ID)
					.map(entry -> TypeScript.getName(entry.getKey()))))
				.toArray(String[]::new));
	}

	public static void write (final DataOutputStream out, final Object object) throws IOException {
		final Class<?> cls = object == null ? null : object.getClass();
		if (cls == null || cls.isPrimitive()) {
			writePrimitiveCode(out, cls);
			writePrimitive(out, object);
			return;
		}

		if (cls.isArray()) {
			writeArray(out, (Object[]) object);
			return;
		}

		final Serialiser serialiser = SERIALISERS.get(cls);
		if (serialiser == null)
			throw new RuntimeException("Cannot serialise '" + cls.getName() + "' object");

		out.writeByte(serialiser.ID);
		serialiser.handle(out, object);

	}

	private static void writeArray (final DataOutputStream out, final Object[] array) throws IOException {
		out.writeByte(PrimitiveSerialisationCode.Array.ordinal());
		writePrimitiveCode(out, array == null ? null : array.getClass().getComponentType());
		for (final Object object : array)
			writePrimitive(out, object);
	}

	private static void writePrimitive (final DataOutputStream out, final Object object) throws IOException {
		if (object instanceof Boolean)
			out.writeBoolean((boolean) object);

		else if (object instanceof Character)
			out.writeChar((char) object);

		else if (object instanceof Byte)
			out.writeByte((byte) object);

		else if (object instanceof Short)
			out.writeShort((short) object);

		else if (object instanceof Integer)
			out.writeInt((int) object);

		else if (object instanceof Long)
			out.writeLong((long) object);

		else if (object instanceof Float)
			out.writeFloat((float) object);

		else if (object instanceof Double)
			out.writeDouble((double) object);
	}

	private static void writePrimitiveCode (final DataOutputStream out, final Class<?> cls) throws IOException {
		if (cls == Void.class || cls == null)
			out.writeByte(PrimitiveSerialisationCode.Void.ordinal());
		else if (cls == Boolean.class)
			out.writeByte(PrimitiveSerialisationCode.Boolean.ordinal());
		else if (cls == Character.class)
			out.writeByte(PrimitiveSerialisationCode.Character.ordinal());
		else if (cls == Byte.class)
			out.writeByte(PrimitiveSerialisationCode.Byte.ordinal());
		else if (cls == Short.class)
			out.writeByte(PrimitiveSerialisationCode.Short.ordinal());
		else if (cls == Integer.class)
			out.writeByte(PrimitiveSerialisationCode.Integer.ordinal());
		else if (cls == Long.class)
			out.writeByte(PrimitiveSerialisationCode.Long.ordinal());
		else if (cls == Float.class)
			out.writeByte(PrimitiveSerialisationCode.Float.ordinal());
		else if (cls == Double.class)
			out.writeByte(PrimitiveSerialisationCode.Double.ordinal());
	}

	public byte ID;

	public abstract void handle (final DataOutputStream out, final Object object);

}
