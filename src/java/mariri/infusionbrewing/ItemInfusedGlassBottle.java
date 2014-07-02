package mariri.infusionbrewing;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInfusedGlassBottle extends Item {
	
	public ItemInfusedGlassBottle(){
		
	}

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (movingobjectposition == null) { return itemStack; }
        else{
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, x, y, z)){ return itemStack; }
                if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, itemStack)){ return itemStack; }
                if (world.getBlock(x, y, z) instanceof BlockFluidPotion){    
                	--itemStack.stackSize;

                	BlockFluidPotion fluid = (BlockFluidPotion)world.getBlock(x, y, z);
                	
                	ItemStack potion = CustomPotionHelper.getSampleItem(fluid.getPotionEffect(), 0, 0, false);
                	
//                	CustomPotionHelper helper = new CustomPotionHelper(fluid.getPotionEffect());
//                    ItemStack potion = new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[fluid.getPotionEffect() - 1][0]);
//                    
//                    NBTTagCompound tag = CustomPotionHelper.createVoidNBTTag();
//                    helper.writeNBTTag(CustomPotionHelper.findPotionNBT(tag));
//                    potion.setTagCompound(tag);
                    
//                    NBTTagCompound effect = new NBTTagCompound();
//                    NBTTagList customPotionEffect = new NBTTagList();
//                    NBTTagCompound tag = new NBTTagCompound();
//                    new PotionEffect(fluid.getPotionEffect(), 1200, 0, false).writeCustomPotionEffectToNBT(effect);
//                    customPotionEffect.appendTag(effect);
//                    tag.setTag("CustomPotionEffects", customPotionEffect);
//                    potion.setTagCompound(tag);

                    world.setBlockToAir(x, y, z);
                    
                    if (itemStack.stackSize <= 0){ return potion; }

                    if (!player.inventory.addItemStackToInventory(potion)){
                        player.dropPlayerItemWithRandomChoice(potion, false);
                    }
                }
            }
            return itemStack;
        }
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {
		this.itemIcon = iconregister.registerIcon("mariri:infused_glass_bottle");
	}
}
