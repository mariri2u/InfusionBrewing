package mariri.infusionbrewing.item;

import java.util.List;

import mariri.infusionbrewing.misc.CustomPotionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionBaubles extends Item implements IBauble{
	
	protected BaubleType type;
	protected String icon;
	
	protected AspectList cost;
	
	private static final int UPDATE_TICK = 300;
		
	public ItemPotionBaubles(String name, BaubleType type){
		this.icon = name;
		this.setUnlocalizedName(name);
		this.type = type;
		this.cost = new AspectList();
		this.maxStackSize = 1;
	}
	
	public ItemPotionBaubles setCost(AspectList cost){
		if(cost != null){
			this.cost = cost;
		}
		return this;
	}
	
	public BaubleType getBaubleType(ItemStack itemstack){
		  return type;
	}
	
	private int getInterval(int amplifier){
		return (1200 / (amplifier + 1));
	}
	
	public void onWornTick(ItemStack itemstack, EntityLivingBase player){
		if(player instanceof EntityPlayer && !player.worldObj.isRemote) {
			NBTTagCompound tag = CustomPotionHelper.findPotionNBT(itemstack);
			CustomPotionHelper potion = new CustomPotionHelper(1, 0, 0);
			int meta = itemstack.getItemDamage();
			if(tag.hasKey("Id")){
				potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
			}else{
				potion.decodeFromCustomMetadata(itemstack.getItemDamage());
			}
			PotionEffect effect = player.getActivePotionEffect(Potion.potionTypes[potion.getId()]);
			if(effect == null || effect.getAmplifier() < potion.getAmplifier() || effect.getDuration() < UPDATE_TICK) {
				if(ThaumcraftApiHelper.consumeVisFromInventory((EntityPlayer)player, cost)){
					if(effect != null && effect.getAmplifier() < potion.getAmplifier()){
						player.removePotionEffect(potion.getId());
					}
					player.addPotionEffect(new PotionEffect(potion.getId(), getInterval(potion.getAmplifier()) + UPDATE_TICK, potion.getAmplifier(), true));
				}
			}
		}
	}
	
	public void onEquipped(ItemStack itemstack, EntityLivingBase player){
	}
	
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player){
	}
	
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player){
		  return true;
	}
	
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player){
		  return true;
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List tooltip, boolean par4) {
		NBTTagCompound tag = CustomPotionHelper.findPotionNBT(itemstack);
		CustomPotionHelper potion = new CustomPotionHelper(1, 0, 0);
		int meta = itemstack.getItemDamage();
		if(tag.hasKey("Id")){
			potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
		}else{
			potion.decodeFromCustomMetadata(itemstack.getItemDamage());
		}
		String name = StatCollector.translateToLocal(Potion.potionTypes[potion.getId()].getName());
		String lv = StatCollector.translateToLocal("potion.potency." + potion.getAmplifier());
		tooltip.add(name + " " + lv);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list)
    {
    	int amp_bits = 3 << CustomPotionHelper.AMPLIFIER_SHIFT; 
    	for(int effect : CustomPotionHelper.SUPPORT_IDS){
    		if(!CustomPotionHelper.isInstant(effect)){
        		list.add(new ItemStack(item, 1, CustomPotionHelper.isNoAmplifier(effect) ? effect : effect | amp_bits));
    		}
    	}
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {
		this.itemIcon = iconregister.registerIcon("mariri:" + icon);
	}
}
