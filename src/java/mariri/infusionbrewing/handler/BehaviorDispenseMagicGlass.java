package mariri.infusionbrewing.handler;

import mariri.infusionbrewing.block.BlockFluidPotion;
import mariri.infusionbrewing.misc.CustomPotionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDispenseMagicGlass extends BehaviorDefaultDispenseItem {
	public final static BehaviorDispenseMagicGlass INSTANCE = new BehaviorDispenseMagicGlass();
	
	private BehaviorDispenseMagicGlass(){}
	
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack itemstack){
		World world = source.getWorld();
		EnumFacing enumfacing = BlockDispenser.func_149937_b(source.getBlockMetadata());
		int x = source.getXInt() + enumfacing.getFrontOffsetX();
		int y = source.getYInt() + enumfacing.getFrontOffsetY();
		int z = source.getZInt() + enumfacing.getFrontOffsetZ();
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		
		if(block instanceof BlockFluidPotion && meta == 0){
			world.setBlockToAir(x, y, z);
			ItemStack potion = CustomPotionHelper.getSampleItem(((BlockFluidPotion)block).getPotionEffect(), 0, 0, false);
			potion.stackSize = itemstack.stackSize;
			itemstack = potion;
		}
		return itemstack;
	}

}
