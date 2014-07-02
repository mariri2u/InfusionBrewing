package mariri.infusionbrewing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;

public class CrucibleBrewingRecipe extends CrucibleRecipe {
	
	public enum MODE { AMPLIFIER, DURATION, SPLASH };
	
	private MODE mode = MODE.AMPLIFIER;
	
	public CrucibleBrewingRecipe(String researchKey, ItemStack result, Object cat, AspectList tags) {
		super(researchKey, result, cat, tags);
	}
	
	public CrucibleBrewingRecipe setMode(MODE mode){
		this.mode = mode;
		return this;
	}
	
	@Override
	public boolean matches(AspectList itags, ItemStack cat) {
		if (catalyst instanceof ItemStack &&
				!itemMatches((ItemStack) catalyst, cat)) {
			return false;
		} else 
		if (itags==null) return false;
		for (Aspect tag:aspects.getAspects()) {
			if (itags.getAmount(tag)<aspects.getAmount(tag)) return false;
		}
//		NBTTagCompound tag = cat.getTagCompound().getTagList("CustomPotionEffects", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
//		if(mode == MODE.AMPLIFIER){
//			tag.setByte("Amplifier", (byte)(tag.getByte("Amplifier") + 1));
//		}else if(mode == MODE.DURATION){
//			tag.setInteger("Duration", tag.getByte("Duration") + 600);
//		}else if(mode == MODE.SPLASH){
//			tag.setBoolean("Ambient", true);
//		}
//		getRecipeOutput().setTagCompound(tag);
//		System.out.println("AAA");
		catalyst = cat;
		return true;
	}
	
	@Override
	public boolean catalystMatches(ItemStack cat) {
		if (catalyst instanceof ItemStack && itemMatches((ItemStack) catalyst,cat)) {
//			NBTTagCompound tag = cat.getTagCompound().getTagList("CustomPotionEffects", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
//			if(mode == MODE.AMPLIFIER){
//				tag.setByte("Amplifier", (byte)(tag.getByte("Amplifier") + 1));
//			}else if(mode == MODE.DURATION){
//				tag.setInteger("Duration", tag.getByte("Duration") + 600);
//			}else if(mode == MODE.SPLASH){
//				tag.setBoolean("Ambient", true);
//			}
//			getRecipeOutput().setTagCompound(tag);
//			System.out.println("BBB");
			catalyst = cat;
			return true;
		} else 
		return false;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		if(!(catalyst instanceof ItemStack)) { return super.getRecipeOutput(); }
		ItemStack output = ((ItemStack)catalyst).copy();
		if(output.getTagCompound() == null) { return super.getRecipeOutput(); }
//		NBTTagCompound tag = output.getTagCompound().getTagList("CustomPotionEffects", 0).getCompoundTagAt(0);
		NBTTagCompound tag = output.getTagCompound().getTagList("CustomPotionEffects", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
		if(mode == MODE.AMPLIFIER){
			byte a = tag.getByte("Amplifier");
			if(a < 3){
				tag.setByte("Amplifier", (byte)(a + 1) );
			}else{
				tag.setByte("Amplifier", (byte)3 );
			}
			output.setItemDamage(CustomPotionHelper.metadataTable[tag.getByte("Id") - 1][0]);
		}else if(mode == MODE.DURATION){
			int d = tag.getInteger("Duration");
			if(d <= 20 * 30){
				tag.setInteger("Duration", 20 * 60 * 2);
			}else if(d <= 20 * 60 * 2){
				tag.setInteger("Duration", 20 * 60 * 4);
			}else if(d <= 20 * 60 * 4){
				tag.setInteger("Duration", 20 * 60 * 8);
			}else if(d <= 20 * 60 * 8){
				tag.setInteger("Duration", 20 * 60 * 12);
			}else if(d <= 20 * 60 * 12){
				tag.setInteger("Duration", 20 * 60 * 16);
			}else if(d <= 20 * 60 * 16){
				tag.setInteger("Duration", 20 * 60 * 16);
			}else {
				tag.setInteger("Duration", 20 * 60 * 2);
			}
			output.setItemDamage(CustomPotionHelper.metadataTable[tag.getByte("Id") - 1][1]);
		}else if(mode == MODE.SPLASH){
			output.setItemDamage(CustomPotionHelper.metadataTable[tag.getByte("Id") - 1][2]);
		}
		return output;
	}
	
	private boolean itemMatches(ItemStack item1, ItemStack item2){
        if (item1 == null && item2 != null || item1 != null && item2 == null)
        {
            return false;
        }
        return item1.getItem() == item2.getItem();
	}
	

}
