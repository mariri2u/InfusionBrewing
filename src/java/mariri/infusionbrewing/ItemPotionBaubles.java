package mariri.infusionbrewing;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
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
			if(tag != null){
				CustomPotionHelper potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
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
	}
	
	public void onEquipped(ItemStack itemstack, EntityLivingBase player){
//		if(player instanceof EntityPlayer && !player.worldObj.isRemote) {
//			NBTTagCompound tag = CustomPotionHelper.findPotionNBT(itemstack);
//			if(tag != null){
//				CustomPotionHelper potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
//				if(ThaumcraftApiHelper.consumeVisFromInventory((EntityPlayer)player, cost)){
//					if(player.getActivePotionEffect(Potion.potionTypes[potion.getId()]) != null) {
//						player.removePotionEffect(potion.getId());
//					}
//					player.addPotionEffect(new PotionEffect(potion.getId(), getInterval(potion.getAmplifier()) + UPDATE_TICK, potion.getAmplifier(), true));
//				}
//			}
//		}
	}
	
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player){
//		if(player instanceof EntityPlayer && !player.worldObj.isRemote) {
//			NBTTagCompound tag = CustomPotionHelper.findPotionNBT(itemstack);
//			if(tag != null){
//				CustomPotionHelper potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
//				
//				PotionEffect effect = player.getActivePotionEffect(Potion.potionTypes[potion.getId()]);
//				if(effect != null && effect.getAmplifier() == potion.getAmplifier())
//					player.removePotionEffect(potion.getId());
//			}
//		}
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
		CustomPotionHelper potion = new CustomPotionHelper();
		if(tag != null){
			potion = CustomPotionHelper.getInstanceFromNBTTag(tag);
			String name = StatCollector.translateToLocal(Potion.potionTypes[potion.getId()].getName());
			String lv = StatCollector.translateToLocal("potion.potency." + potion.getAmplifier());
			tooltip.add(name + " " + lv);
		}
	}
	  
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {
		this.itemIcon = iconregister.registerIcon("mariri:" + icon);
	}
}
