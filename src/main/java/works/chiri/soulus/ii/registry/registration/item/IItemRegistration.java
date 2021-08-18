package works.chiri.soulus.ii.registry.registration.item;

import net.minecraft.item.Item;
import works.chiri.soulus.ii.registry.registration.IRegistrationHasItem;


public interface IItemRegistration<I extends Item> extends IRegistrationHasItem<Item, I> {}
