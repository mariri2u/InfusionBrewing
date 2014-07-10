package mariri.infusionbrewing.handler;

import mariri.infusionbrewing.InfusionBrewing;
import mariri.infusionbrewing.block.BlockFluidPotion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDispenseMagicBucket extends BehaviorDefaultDispenseItem {
	public final static BehaviorDispenseMagicBucket INSTANCE = new BehaviorDispenseMagicBucket();
	
	private BehaviorDispenseMagicBucket(){}
	
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack itemstack){
		World world = source.getWorld();
		EnumFacing enumfacing = BlockDispenser.func_149937_b(source.getBlockMetadata());
		int x = source.getXInt() + enumfacing.getFrontOffsetX();
		int y = source.getYInt() + enumfacing.getFrontOffsetY();
		int z = source.getZInt() + enumfacing.getFrontOffsetZ();
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		Item item;
		
		if(block instanceof BlockFluidPotion && meta == 0){
			item = InfusionBrewing.itemPotionBuckets[((BlockFluidPotion)block).getPotionEffect() - 1];
		}else if(block.getMaterial() == Material.water){
			item = Items.water_bucket;
		}else if(block.getMaterial() == Material.lava){
			item = Items.lava_bucket;
		}else{
			return super.dispenseStack(source, itemstack);
		}
		
		world.setBlockToAir(x, y, z);
		
		if(--itemstack.stackSize == 0){
			itemstack.func_150996_a(item);
			itemstack.stackSize = 1;
		}else if(((TileEntityDispenser)source.getBlockTileEntity()).func_146019_a(new ItemStack(item)) < 0){
			super.dispenseStack(source, new ItemStack(item));
		}
		
		return itemstack;
	}
}
