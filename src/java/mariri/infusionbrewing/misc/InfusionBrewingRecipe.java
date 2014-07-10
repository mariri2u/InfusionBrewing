package mariri.infusionbrewing.misc;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

public class InfusionBrewingRecipe extends InfusionRecipe {
	
	public enum MODE { AMPLIFIER, DURATION, SPLASH, BAUBLES, FOCUS };
	
	private MODE mode = MODE.AMPLIFIER;
	
//	private CustomPotionHelper compomentEffect;
	
	public InfusionBrewingRecipe(String research, Object output, int inst,
			AspectList aspects2, ItemStack input, ItemStack[] recipe) {
		super(research, output, inst, aspects2, input, recipe);
//		if(input.getItem() == Items.potionitem && CustomPotionHelper.findPotionNBT(input) == null){
//	        input.setTagCompound(CustomPotionHelper.createVoidNBTTag());
//		}
	}
	
	public InfusionBrewingRecipe setMode(MODE mode){
		this.mode = mode;
		return this;
	}
	
	@Override
	public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
		if (getRecipeInput() == null) return false;
		
		if (research.length() > 0 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research)) {
    		return false;
    	}
		
		ItemStack i2 = central.copy();
		
		if(central.getItem() == Items.potionitem && central.getTagCompound() == null) { return false; }
		
		if (!areItemStacksEqual(i2, getRecipeInput(), true)) return false;
		
		if(i2.getItem() == Items.potionitem){
			NBTTagCompound tag = CustomPotionHelper.findPotionNBT(i2);
			CustomPotionHelper potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
			if(mode == MODE.AMPLIFIER && potion.isMaxAmplifier()){ return false; }
			else if(mode == MODE.DURATION && potion.isMaxDuration()){ return false; }
			else if(mode == MODE.SPLASH && ItemPotion.isSplash(i2.getItemDamage())) { return false; }
			else if(mode == MODE.BAUBLES){
				 if(potion.isInstant()){ return false; }
				 else if(!potion.isMaxDuration()) { return false; }
			}else if(mode == MODE.FOCUS) {
				if(!ItemPotion.isSplash(i2.getItemDamage())){ return false; }
				else if(!potion.isMaxDuration()){ return false; }
			}
		}
		
		ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
		for (ItemStack is : input) {
			ii.add(is.copy());
		}
		
		for (ItemStack comp : getComponents()) {
			boolean b = false;
			for (int a = 0; a < ii.size(); a++) {
				 i2 = ii.get(a).copy();
//				 
//				 if(mode == MODE.BAUBLES && comp.getItem() == Items.potionitem){
//					 NBTTagCompound tag = CustomPotionHelper.findPotionNBT(i2);
//					 if(tag == null){ System.out.println("Notag"); return false; }
//					 CustomPotionHelper potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
//					 System.out.println(potion.getId() + " : " + potion.getAmplifier() + " : " + potion.getDuration());
//					 if(potion.isInstant()){ System.out.println("Instant"); return false; }
//					 else if(!potion.isMaxDuration()) { System.out.println("No duration"); return false; }
//					 compomentEffect = potion;
//				 }

				if (areItemStacksEqual(i2, comp,true)) {
					ii.remove(a);
					b = true;
					break;
				}
			}
			if (!b) return false;
		}
		return ii.size() == 0 ? true : false;
    }
	
	@Override
	protected boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean fuzzy)
    {
		if (stack0 == null && stack1 != null) return false;
		if (stack0 != null && stack1 == null) return false;
		if (stack0 == null && stack1 == null) return true;
		boolean t1 = false;
		t1 = stack0.getItem() == stack1.getItem();
//		if(stack0.getItem() == Items.potionitem){
//			t1 &= (CustomPotionHelper.findPotionNBT(stack0) == null && CustomPotionHelper.findPotionNBT(stack1) == null) || (CustomPotionHelper.findPotionNBT(stack0) != null && CustomPotionHelper.findPotionNBT(stack1) != null);
//		}else{
//			t1 &= stack0.getItemDamage() == stack1.getItemDamage();
//		}
		if(stack0.getItem() != Items.potionitem){
			t1 &= stack0.getItemDamage() == stack1.getItemDamage();
		}

        return t1;
    }
	
	@Override
	public Object getRecipeOutput(ItemStack input) {
		ItemStack output = input.copy();
		NBTTagCompound tag = CustomPotionHelper.findPotionNBT(output);
		if(tag == null) { return super.getRecipeOutput(input); }
		CustomPotionHelper potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
		if(potion.getId() <= 0){ return super.getRecipeOutput(input); }
		if(mode == MODE.AMPLIFIER){
			potion.incrementAmplifier();
			if(ItemPotion.isSplash(input.getItemDamage())){
				output.setItemDamage(CustomPotionHelper.metadataTable[potion.getId() - 1][2]);
			}else{
				output.setItemDamage(CustomPotionHelper.metadataTable[potion.getId() - 1][0]);
			}
			potion.writeNBTTag(tag);
		}else if(mode == MODE.DURATION){
			potion.incrementDurationCode();
			if(ItemPotion.isSplash(input.getItemDamage())){
				output.setItemDamage(CustomPotionHelper.metadataTable[potion.getId() - 1][2]);
			}else{
				output.setItemDamage(CustomPotionHelper.metadataTable[potion.getId() - 1][1]);
			}
			potion.writeNBTTag(tag);
		}else if(mode == MODE.SPLASH){
			output.setItemDamage(CustomPotionHelper.metadataTable[potion.getId() - 1][2]);
		}else if(mode == MODE.BAUBLES || mode == MODE.FOCUS){
			output = ((ItemStack)super.getRecipeOutput(input)).copy();
			output.setTagCompound(input.getTagCompound());
//			NBTTagCompound t = CustomPotionHelper.createVoidNBTTag();
//			output.setTagCompound(t);
//			NBTTagCompound tt = CustomPotionHelper.findPotionNBT(t);
//			compomentEffect.writeNBTTag(tt);
		}
		return output;
    }
	
//	private NBTTagCompound getPotionNBT(ItemStack itemstack){
//		try{
//			return itemstack.getTagCompound().getTagList("CustomPotionEffects", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
//		}catch(NullPointerException e){
//			return null;
//		}
//	}

}
