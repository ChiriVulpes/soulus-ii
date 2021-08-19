package works.chiri.soulus.ii.client;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import works.chiri.soulus.ii.SoulusII;


public class SoulusIIItemGroup extends ItemGroup {

	public static final SoulusIIItemGroup INSTANCE = new SoulusIIItemGroup();

	public SoulusIIItemGroup () {
		super(SoulusII.ID);
	}

	@OnlyIn(Dist.CLIENT)
	public ItemStack makeIcon () {
		return new ItemStack(Items.BOOK);
	}

}
