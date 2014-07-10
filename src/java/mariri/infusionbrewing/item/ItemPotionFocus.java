package mariri.infusionbrewing.item;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

import mariri.infusionbrewing.misc.CustomPotionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.IWandFocus;

public class ItemPotionFocus extends Item implements IWandFocus {

	private IIcon ornament, depth;


	public ItemPotionFocus() {
		super();
//		setMaxDamage(1);
		setNoRepair();
		setMaxStackSize(1);
	}
	
	@Override
	public int getFocusColor(){
		return 0xFF60FF;
	}
	
	/**
	 * @return An icon that will be drawn as a block inside the focus "block".
	 */
	@Override
	public IIcon getFocusDepthLayerIcon(){
		return depth;
	}
	
	@Override
	public IIcon getOrnament(){
		return ornament;
	}
	
	@Override
	public WandFocusAnimation getAnimation(){
		return WandFocusAnimation.WAVE;
	}
	
	
	/**
	 * Gets the amount of vis used per aspect per click or tick. This cost is actually listed as
	 * a hundredth of a single point of vis, so a cost of 100 will equal one vis per tick/click.
	 * It is returned as an AspectList to allow for multiple vis types in different ratios.
	 */
	@Override
	public AspectList getVisCost(){
		return new AspectList().add(Aspect.FIRE, 100).add(Aspect.ENTROPY, 50);
	}
	
	protected AspectList getVisCost(CustomPotionHelper potion){
		AspectList cost = getVisCost();
		for(Aspect vis : Aspect.getPrimalAspects()){
			if(cost.getAmount(vis) > 0){
//				int value = cost.getAmount(vis);
//				value = value * (potion.getAmplifier() + 1) * (potion.isInstant() ? 1 : 4);
//				cost.merge(vis, value);
				cost.add(vis, cost.getAmount(vis) * potion.getAmplifier());
			}
		}
		return cost;
	}
	
	@Override
	public boolean isVisCostPerTick(){
		return false;
	}

	@Override
	public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition movingobjectposition){

        if (!world.isRemote){
//                NBTTagCompound nbt = itemstack.stackTagCompound.getCompoundTag("focus");
//                ItemStack focus = ItemStack.loadItemStackFromNBT(nbt);
//            	System.out.println(focus.getItem().getUnlocalizedName());

        	ItemStack focus = getFocusItem(itemstack);
            NBTTagCompound tag = CustomPotionHelper.findPotionNBT(focus);
            CustomPotionHelper effect = new CustomPotionHelper(1, 0, 0);
            if(tag != null){
	        	effect = CustomPotionHelper.getInstanceFromNBTTag(tag);
            }
        	effect.setDurationCode(0);
//	        	System.out.println("Foci: " + effect.getId() + ", " + effect.getAmplifier() + ", " + effect.getDuration());
        	if(ThaumcraftApiHelper.consumeVisFromWand(itemstack, player, getVisCost(effect), true, false)){
                world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                ItemStack potion = effect.getSampleItem(true);
        		world.spawnEntityInWorld(new EntityPotion(world, player, potion));
        	}
        }
        return itemstack;
	}
	
	@Override
	public void onUsingFocusTick(ItemStack itemstack, EntityPlayer player, int count){
		
	}
	
	@Override
	public void onPlayerStoppedUsingFocus(ItemStack itemstack, World world, EntityPlayer player, int count){
		
	}
		
	/**
	 * Helper method to determine in what order foci should be iterated through when 
	 * the user presses the 'change focus' keybinding.
	 * @return a string of characters that foci will be sorted against. 
	 * For example AA00 will be placed before FG12
	 * <br>As a guide build the sort string from two alphanumeric characters followed by 
	 * two numeric characters based on... whatever. 
	 */
	@Override
	public String getSortingHelper(ItemStack itemstack){
		String str = "POTION";
//    	ItemStack focus = getFocusItem(itemstack);
        NBTTagCompound tag = CustomPotionHelper.findPotionNBT(itemstack);
        if(tag != null){
        	CustomPotionHelper effect = CustomPotionHelper.getInstanceFromNBTTag(tag);
        	str = str + ":" + effect.getId() + ":" + effect.getAmplifier();
        }
//        System.out.println(str);
		return str;
	}

	@Override
	public boolean onFocusBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player){
		return false;
	}

	public boolean acceptsEnchant(int id){
		return false;
	}
	
	protected boolean hasOrnament() {
		return false;
	}

	protected boolean hasDepth() {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List tooltip, boolean par4) {
		NBTTagCompound tag = CustomPotionHelper.findPotionNBT(itemstack);
		CustomPotionHelper potion = new CustomPotionHelper(1, 0, 0);
		if(tag != null){
			potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
		}
		String name = StatCollector.translateToLocal(Potion.potionTypes[potion.getId()].getName());
		String lv = StatCollector.translateToLocal("potion.potency." + potion.getAmplifier());
		tooltip.add(name + " " + lv);
		AspectList al = this.getVisCost(potion);
		if (al!=null && al.size()>0) {
			tooltip.add(StatCollector.translateToLocal(isVisCostPerTick() ? "item.Focus.cost2" : "item.Focus.cost1"));
			for (Aspect aspect : al.getAspectsSorted()) {
				DecimalFormat myFormatter = new DecimalFormat("#####.##");
				String amount = myFormatter.format(al.getAmount(aspect) / 100f);
				tooltip.add(" \u00A7" + aspect.getChatcolor() + aspect.getName() + "\u00A7r x " + amount);
				
			}
		}
	}
	
	@Override
	public void registerIcons(IIconRegister iconregister){
		this.itemIcon = iconregister.registerIcon("mariri:focus_potion");

		if (hasOrnament()){
//			ornament = IconHelper.forItem(iconregister, this, "Orn");
		}
		if (hasDepth()){
//			depth = IconHelper.forItem(iconregister, this, "Depth");
		}

	}
	
	private ItemStack getFocusItem(ItemStack stack){
		ItemStack focus = new ItemStack(Blocks.air);
		try {
			Item item = stack.getItem();
			Class clazz = item.getClass();
			Method method = clazz.getMethod("getFocusItem", new Class[]{ ItemStack.class });
			focus = (ItemStack)method.invoke(item, new Object[]{ stack });
		} catch (Exception ex) {
//			FMLLog.warning("[Thaumcraft] Could not retrieve item identified by: " + itemString);
		}
		return focus;
	}
	
//	private boolean consumeAllVis(ItemStack itemstack, EntityPlayer player, AspectList aspects, boolean doit, boolean crafting){
//		boolean result = false;
//		try {
//			Item item = itemstack.getItem();
//			Class clazz = item.getClass();
////			System.out.println("BBBB");
//			Method method = clazz.getMethod(
//					"consumeAllVis",
//					new Class[]{ ItemStack.class, EntityPlayer.class, AspectList.class, boolean.class, boolean.class });
////			System.out.println("AAAA");
//			result = (Boolean)method.invoke(
//					item,
//					new Object[]{ itemstack, player, aspects, doit, crafting });
////			System.out.println("Bool: " + result);
//		} catch (Exception ex) {
////			System.out.println("EXX");
////			FMLLog.warning("[Thaumcraft] Could not retrieve item identified by: " + itemString);
//		}
//		return result;
//		
//	}
}
