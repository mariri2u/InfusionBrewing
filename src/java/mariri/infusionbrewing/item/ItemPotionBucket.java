package mariri.infusionbrewing.item;

import java.util.List;

import mariri.infusionbrewing.block.BlockFluidPotion;
import mariri.infusionbrewing.misc.CustomPotionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotionBucket extends ItemBucket {

	private String iconName;
	private BlockFluidPotion fluid;
//	public List<BlockFluidPotion> fluids = new ArrayList<BlockFluidPotion>();
	public ItemPotionBucket(BlockFluidPotion block){
		super(block);
		fluid = block;
	}
	
	public int getPotionEffect(){
		return fluid.getPotionEffect();
	}
	
//	@Override
//	public boolean tryPlaceContainedLiquid(World world, int x, int y, int z){
//		boolean result = super.tryPlaceContainedLiquid(world, x, y, z);
//		if(result){
//			for(BlockFluidPotion fluid : fluids){
////				if(fluid.getPotionEffect() == )
//			}
//		}
////		world.setBlockMetadataWithNotify(x, y, z, 3, 4);
//		return result;
//	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("mariri:bucket_potion" + fluid.getPotionEffect());
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List tooltip, boolean par4) {
		if(fluid instanceof BlockFluidPotion){
			String name = StatCollector.translateToLocal(Potion.potionTypes[fluid.getPotionEffect()].getName());
			tooltip.add(name);
			tooltip.add(CustomPotionHelper.isSupport(fluid.getPotionEffect()) ? " - Good Status" : " - Bad Status");
		}
	}
}
