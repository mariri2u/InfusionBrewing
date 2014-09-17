package mariri.infusionbrewing.misc;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import thaumcraft.api.ItemApi;

public class SpawnHelper {

    public static void spawnCreature(World world, int x, int y, int z, EntityLiving creature, double chance, int area){
    	boolean isSpawn = false;
		List<EntityCreature> list = world.getEntitiesWithinAABB(
				creature.getClass(),
				AxisAlignedBB.getBoundingBox(x - 2 * area, y - 4, z - 2 * area, x + 2 * area + 1, y + 5, z + 2 * area + 1));
		if(list.size() < 10 && world.rand.nextDouble() <= chance){
	        for(int i = 0; i <= area * 3; i++){
				int offsetX = world.rand.nextInt(2 * area + 1) - area - 1;
				int offsetY = world.rand.nextInt(5) - 3;
				int offsetZ = world.rand.nextInt(2 * area + 1) - area - 1;
				if(canSpawn(world, x + offsetX, y + offsetY, z + offsetZ, creature)){
			        creature.setLocationAndAngles(x + offsetX + 0.5, y + offsetY, z + offsetZ + 0.5, 0.0F, 0.0F);
			        isSpawn = true;
			        break;
//				}else if(i == area * 2){
//			        creature.setLocationAndAngles(x + 0.5, y + 2, z + 0.5, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
	        	}
	        }
			
	        if(isSpawn){
		        creature.rotationYawHead = creature.rotationYaw;
		        creature.renderYawOffset = creature.rotationYaw;
		        creature.onSpawnWithEgg((IEntityLivingData)null);
	
				if(creature instanceof EntityZombie && world.rand.nextDouble() < 0.7){
					((EntityZombie)creature).setChild(true);
				}else if(creature instanceof EntityCreeper && world.rand.nextDouble() < 0.7){
					creature.getDataWatcher().updateObject(17, Byte.valueOf((byte)1));
				}
				if(creature instanceof EntityZombie || creature instanceof EntitySkeleton){
					if(creature instanceof EntitySkeleton && world.rand.nextDouble() < 0.8){
						creature.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
					}else{
						creature.setCurrentItemOrArmor(0, ItemApi.getItem("itemSwordThaumium", 0));
					}
					double r = world.rand.nextDouble();
					if(r < 0.05){
						creature.setCurrentItemOrArmor(4, new ItemStack(Blocks.pumpkin));
					}else if(r < 0.6){
						creature.setCurrentItemOrArmor(4, ItemApi.getItem("itemHelmetThaumium", 0));
					}
					if(world.rand.nextDouble() < 0.6){
						creature.setCurrentItemOrArmor(3, ItemApi.getItem("itemChestThaumium", 0));
					}
					if(world.rand.nextDouble() < 0.6){
						creature.setCurrentItemOrArmor(2, ItemApi.getItem("itemLegsThaumium", 0));
					}
					if(world.rand.nextDouble() < 0.6){
						creature.setCurrentItemOrArmor(1, ItemApi.getItem("itemBootsThaumium", 0));
					}
				}
				
		        world.spawnEntityInWorld(creature);
		        creature.playLivingSound();
	        }
		}
    }
    
    private static boolean canSpawn(World world, int x, int y, int z, EntityLiving creature){
    	if(creature instanceof EntitySquid || creature instanceof EntityBat || creature instanceof EntityCaveSpider){
    		return canSpawnInBlock(world, x, y, z, 1, 1, false);
    	}else if(creature instanceof EntityEnderman){
    		return canSpawnInBlock(world, x, y, z, 1, 3, false);
    	}else if(creature instanceof EntitySpider){
    		return canSpawnInBlock(world, x, y, z, 2, 1, false);
    	}else if(creature instanceof EntitySlime){
    		return canSpawnInBlock(world, x, y, z, 3, 3, false);
    	}else{
    		return canSpawnInBlock(world, x, y, z, 1, 2, false);
    	}
    }
    
    private static boolean canSpawnInBlock(World world, int x, int y, int z, int width, int height, boolean checkNoFluid){
    	boolean flag = true;
    	for(int i = 0; i < width; i++){
        	for(int j = 0; j < width; j++){
        		flag &= isAir(world.getBlock(x - i, y, z - j));
        	}
        }
    	for(int i = 0; i < height; i++){
    		flag &= isAir(world.getBlock(x, y + i, z));
    	}
    	if(checkNoFluid){
    		flag &= !isFluid(world.getBlock(x, y - 1, z));
    		flag &= !isFluid(world.getBlock(x, y - 2, z));
    	}
    	return flag;
    }
    
    private static boolean isFluid(Block block){
		return block instanceof IFluidBlock || block instanceof BlockLiquid;
    }
    
    private static boolean isAir(Block block){
    	return block == Blocks.air;
    }
}
