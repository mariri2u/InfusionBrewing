package mariri.infusionbrewing.misc;

import net.minecraft.item.Item;

public class ThaumcraftHelper {
	public static boolean isItemWandCasting(Item item){
		Class clazz;
		boolean result = false;
		try{
			clazz = Class.forName("thaumcraft.common.items.wands.ItemWandCasting");
			result = clazz.isInstance(item);
		}catch(Exception e){}
		return result;
	}
}
