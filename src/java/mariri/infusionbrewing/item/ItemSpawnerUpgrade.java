package mariri.infusionbrewing.item;

import java.util.List;

import mariri.infusionbrewing.misc.CustomSpawnerHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpawnerUpgrade extends Item{

	private IIcon[] icons;
	
	public ItemSpawnerUpgrade(){
		super();
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
            int x = movingobjectposition.blockX;
            int y = movingobjectposition.blockY;
            int z = movingobjectposition.blockZ;

            if (world.getBlock(x, y, z) == Blocks.mob_spawner){
            	boolean result = false;
            	int meta = itemstack.getItemDamage();
            	TileEntity tile = world.getTileEntity(x, y, z);
            	NBTTagCompound tag = new NBTTagCompound();
            	tile.writeToNBT(tag);
            	CustomSpawnerHelper helper = CustomSpawnerHelper.getInstanceFromNBTTag(tag);
            	
            	if(		(meta == 0 && helper.getPowerCode() == 0) ||
            			(meta == 1 && helper.getPowerCode() == 1) ||
            			(meta == 2 && helper.getPowerCode() == 2) ||
            			(meta == 3 && helper.getPowerCode() == 3)){
            		helper.incrementPower();
                	player.addChatComponentMessage(new ChatComponentText("Power Upgrade Successfull"));
                	player.addChatComponentMessage(new ChatComponentText("- Current Power: " + helper.getPowerCode()));
                	result = true;
            	}else if(	(meta == 4 && helper.getSpeedCode() == 0) ||
            				(meta == 5 && helper.getSpeedCode() == 1) ||
            				(meta == 6 && helper.getSpeedCode() == 2) ||
            				(meta == 7 && helper.getSpeedCode() == 3)){
            		helper.incrementSpeed();
                	player.addChatComponentMessage(new ChatComponentText("Speed Upgrade Successfull"));
                	player.addChatComponentMessage(new ChatComponentText("- Current Speed: " + helper.getSpeedCode()));
                	result = true;
            	}
            	
            	if(result){
	            	helper.writeToNBTTag(tag);
	            	tile.readFromNBT(tag);
	        		world.spawnEntityInWorld(new EntityLightningBolt(world, x, y + 1, z));
	            	--itemstack.stackSize;
            	}
            }
        }
		return itemstack;
	}
	
    public String getUnlocalizedName(ItemStack itemstack)
    {
    	String name = "item.upgradePower_i";	
    	switch(itemstack.getItemDamage()){
    		case 0:
    			name = "item.upgradePowerI";
    			break;
    		case 1:
    			name = "item.upgradePowerII";
    			break;
    		case 2:
    			name = "item.upgradePowerIII";
    			break;
    		case 3:
    			name = "item.upgradePowerIV";
    			break;
    		case 4:
    			name = "item.upgradeSpeedI";
    			break;
    		case 5:
    			name = "item.upgradeSpeedII";
    			break;
    		case 6:
    			name = "item.upgradeSpeedIII";
    			break;
    		case 7:
    			name = "item.upgradeSpeedIV";
    			break;
    	}
        return name;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list)
    {
    	for(int i = 0; i < 8; i++){
    		list.add(new ItemStack(item, 1, i));
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        return meta < 8 ? this.icons[meta] : this.icons[0];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("mariri:upgrade_power_i");
		this.icons = new IIcon[8];
		this.icons[0] = par1IconRegister.registerIcon("mariri:upgrade_power_i");
		this.icons[1] = par1IconRegister.registerIcon("mariri:upgrade_power_ii");
		this.icons[2] = par1IconRegister.registerIcon("mariri:upgrade_power_iii");
		this.icons[3] = par1IconRegister.registerIcon("mariri:upgrade_power_iv");
		this.icons[4] = par1IconRegister.registerIcon("mariri:upgrade_speed_i");
		this.icons[5] = par1IconRegister.registerIcon("mariri:upgrade_speed_ii");
		this.icons[6] = par1IconRegister.registerIcon("mariri:upgrade_speed_iii");
		this.icons[7] = par1IconRegister.registerIcon("mariri:upgrade_speed_iv");
	}
}
