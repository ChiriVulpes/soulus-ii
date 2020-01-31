package chirichirichiri.soulus.ii.registry;

import java.lang.annotation.*;

public class Register {

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Block {

		String value ();
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Item {

		String value ();
	}

}

