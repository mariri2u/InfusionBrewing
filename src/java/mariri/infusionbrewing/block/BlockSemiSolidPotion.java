package mariri.infusionbrewing.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockSemiSolidPotion extends BlockFalling{
	
	@SideOnly(Side.CLIENT)
	protected IIcon icon;
	
	public BlockSemiSolidPotion(){
		super();
	}
	
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
//    	checkExplosion(world, x, y, z);
    	react(world, x, y, z);
    	super.onNeighborBlockChange(world, x, y, z, block);
    }

	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand){
		
//		List<EntityCreature> list = world.getEntitiesWithinAABB(
//				EntityItem.class,
//				AxisAlignedBB.getBoundingBox(x - 10, y - 5, z - 10, x + 11, y + 5, z + 11));
//		if(list.size() < 10){
//		    EntityItem entityitem = new EntityItem(world, x, y, z, new ItemStack(this));
//		    entityitem.delayBeforeCanPickup = 10;
//		    world.spawnEntityInWorld(entityitem);
//	
//		    world.setBlockToAir(x, y, z);
//		}
		react(world, x, y, z);
		super.updateTick(world, x, y, z, rand);
	}
	
	private boolean react(World world, int x, int y, int z){
		int count = 0;
		List<EntityCreature> list = world.getEntitiesWithinAABB(
				EntityItem.class,
				AxisAlignedBB.getBoundingBox(x - 2, y - 1, z - 2, x + 3, y + 1, z + 3));
		if(list.size() < 10){
			for(int xi = -1; xi <= 1; xi++){
				for(int yi = -1; yi <= 1; yi++){
					for(int zi = -1; zi <= 1; zi++){
						if(!(xi == 0 && yi == 0 && zi == 0) && world.getBlock(x + xi, y + yi, z + zi) == this){
							world.setBlockToAir(x + xi, y + yi, z + zi);
							count++;
						}
					}
				}
			}
				
			if(count > 0){
				count++;
				world.setBlockToAir(x, y, z);
				EntityItem entityitem = new EntityItem(world, x, y, z, new ItemStack(this, count));
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
			}
		}
		return count > 0;
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return icon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.icon = iconRegister.registerIcon("mariri:semi_solid_potion");
	}
}
