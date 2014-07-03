package mariri.infusionbrewing;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import thaumcraft.api.ItemApi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidPotion extends BlockFluidClassic {
	
	@SideOnly(Side.CLIENT)
	protected IIcon[] theIcon;
	protected int potionEffect;
	protected boolean explode;
	protected boolean spawn;
	
	public BlockFluidPotion(Fluid fluid, Material material) {
		super(fluid, material);
	}
	
	public BlockFluidPotion setPotionEffect(int potionEffect){
		this.potionEffect = potionEffect;
		return this;
	}
	
	public int getPotionEffect(){
		return potionEffect;
	}
	
	public BlockFluidPotion setExplode(boolean value){
		this.explode = value;
		return this;
	}
	
	public BlockFluidPotion setSpawn(boolean value){
		this.spawn = value;
		return this;
	}
	
//    protected void flowIntoBlock(World world, int x, int y, int z, int meta)
//    {
//    	Block block = world.getBlock(x, y, z);
//    	if(block == Blocks.water){
//    		
//    	}else if(block == Blocks.lava){
//    		
//    	}
//    	world.setBlockToAir(x, y, z);
//    	super.flowIntoBlock(world, x, y, z, meta);
//    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
//    	checkExplosion(world, x, y, z);
    	reactBlock(world, x, y, z, world.getBlock(x, y, z - 1)); 
    	reactBlock(world, x, y, z, world.getBlock(x, y, z + 1)); 
    	reactBlock(world, x, y, z, world.getBlock(x - 1, y, z)); 
    	reactBlock(world, x, y, z, world.getBlock(x + 1, y, z)); 
    	reactBlock(world, x, y, z, world.getBlock(x, y + 1, z)); 
    	super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
//    	checkExplosion(world, x, y, z);
    	reactBlock(world, x, y, z, world.getBlock(x, y, z - 1)); 
    	reactBlock(world, x, y, z, world.getBlock(x, y, z + 1)); 
    	reactBlock(world, x, y, z, world.getBlock(x - 1, y, z)); 
    	reactBlock(world, x, y, z, world.getBlock(x + 1, y, z)); 
    	reactBlock(world, x, y, z, world.getBlock(x, y + 1, z)); 
    	super.onNeighborBlockChange(world, x, y, z, block);
    }
    
//    private boolean checkExplosion(World world, int x, int y, int z){
//        boolean flag = false;
//        if(!CustomPotionHelper.isSupport(potionEffect)) { return false; }
//        if(world.getBlock(x, y, z) == this){
//            flag |= world.getBlock(x, y, z - 1).getMaterial() == Material.lava;
//            flag |= world.getBlock(x, y, z + 1).getMaterial() == Material.lava;
//            flag |= world.getBlock(x - 1, y, z).getMaterial() == Material.lava;
//            flag |= world.getBlock(x + 1, y, z).getMaterial() == Material.lava;
//            flag |= world.getBlock(x, y + 1, z).getMaterial() == Material.lava;
//            if(world.getBlockMetadata(x, y, z) == 0){
//	           	flag |= world.canBlockSeeTheSky(x, y, z) && world.getBlockLightValue(x, y, z) == 15;
//	           	flag |= world.provider.isHellWorld;
//            }
//            if(flag){
//            	world.setBlockToAir(x, y, z);
//            	world.newExplosion(null, x, y, z, 4, true, false);
////            	world.createExplosion(null, x, y, z, 4, true);
//            }
//        }
//        return flag;
//    }
    
//    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
//    	if(world.getBlockMetadata(x, y, z) == 0){
//    		world.createExplosion(null, x, y, z, 4, true);
//    	}
//    }
    
    private void reactBlock(World world, int x, int y, int z, Block infusion){
    	boolean support = CustomPotionHelper.isSupport(this.potionEffect);
    	boolean instant = CustomPotionHelper.isInstant(this.potionEffect);
    	Fluid fluid = FluidRegistry.lookupFluidForBlock(infusion);
//    	if(world.getBlockMetadata(x, y, z) != 0){ return; }
    	if(this == infusion){ return; }
    	if(infusion instanceof BlockFluidPotion){
    		int effect = ((BlockFluidPotion)infusion).getPotionEffect();
    		if(support != CustomPotionHelper.isSupport(effect)){
    			world.setBlockToAir(x, y, z);
            	createExplosion(world, x, y, z);
    		}else if(support){
    			if(instant == CustomPotionHelper.isInstant(effect)){
        			world.setBlock(x, y, z, Blocks.gravel);
    			}else{
        			world.setBlock(x, y, z, Blocks.sand);
    			}
				if(this.potionEffect == CustomPotionHelper.WATER_BREATHING || effect == CustomPotionHelper.WATER_BREATHING){
					spawnCreature(world, x, y + 2, z, new EntitySquid(world), 0.1);
				}
    		}else{
				if(instant == CustomPotionHelper.isInstant(effect)){
	    			world.setBlock(x, y, z, Blocks.netherrack);
				}else{
	    			world.setBlock(x, y, z, Blocks.soul_sand);
				}
				if(this.potionEffect == CustomPotionHelper.DECAY || effect == CustomPotionHelper.DECAY){
					spawnCreature(world, x, y + 2, z, new EntitySkeleton(world), 1.0);
				}
				if(this.potionEffect == CustomPotionHelper.HUNGER || effect == CustomPotionHelper.HUNGER){
					if(world.provider.isHellWorld){
						spawnCreature(world, x, y + 2, z, new EntityPigZombie(world), 1.0);
					}else{
						spawnCreature(world, x, y + 2, z, new EntityZombie(world), 1.0);
					}
				}
				if(this.potionEffect == CustomPotionHelper.POISON || effect == CustomPotionHelper.POISON){
					spawnCreature(world, x, y + 2, z, new EntityCaveSpider(world), 1.0);
				}
				if(this.potionEffect == CustomPotionHelper.HARMING || effect == CustomPotionHelper.HARMING){
					spawnCreature(world, x, y + 2, z, new EntityWitch(world), 1.0);
				}
				if(this.potionEffect == CustomPotionHelper.WEAKNESS || effect == CustomPotionHelper.WEAKNESS){
					spawnCreature(world, x, y + 2, z, new EntityVillager(world), 0.2);
				}
    		}
    	}else if(infusion.getMaterial() == Material.lava){
    		if(support){
    			world.setBlockToAir(x, y, z);
            	createExplosion(world, x, y, z);
    		}else{
    			world.setBlock(x, y, z, Blocks.sandstone);
    		}
    	}else if(fluid != null && fluid.getLuminosity() >= 9){
    		if(support){
            	world.setBlock(x, y, z, Blocks.glass);
    		}else{
    			world.setBlock(x, y, z, Blocks.glowstone);
    			spawnCreature(world, x, y + 2, z, new EntityCreeper(world), 1.0);
    		}
    	}else if(fluid != null && infusion.isFlammable(world, x, y, z, ForgeDirection.UP)){
    		if(support){
    			world.setBlockToAir(x, y, z);
            	createExplosion(world, x, y, z);
    		}else{
    			world.setBlock(x, y, z, Blocks.coal_block);
    			spawnCreature(world, x, y + 2, z, new EntityBlaze(world), 1.0);
    		}
    	}else if(infusion.getMaterial() == Material.water){
    		if(support){
    			world.setBlock(x, y, z, Blocks.mycelium);
    		}else{
    			world.setBlock(x, y, z, Blocks.clay);
    		}
    	}else if(infusion == Blocks.fire){
    		if(support){
    			world.setBlockToAir(x, y, z);
            	createExplosion(world, x, y, z);
    		}
    	}
    }
    
    private void spawnCreature(World world, int x, int y, int z, EntityLiving creature, double chance){
		List<EntityCreature> list = world.getEntitiesWithinAABB(
				creature.getClass(),
				AxisAlignedBB.getBoundingBox(x -5, y - 5, z - 5, x + 6, y + 6, z + 6));
		if(spawn && list.size() < 10 && world.rand.nextDouble() < chance){
	        creature.setLocationAndAngles(x + 0.5, y, z + 0.5, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
	        creature.rotationYawHead = creature.rotationYaw;
	        creature.renderYawOffset = creature.rotationYaw;
	        creature.onSpawnWithEgg((IEntityLivingData)null);

			if(creature instanceof EntityZombie){
				((EntityZombie)creature).setChild(true);
			}else if(creature instanceof EntityCreeper){
				creature.getDataWatcher().updateObject(17, Byte.valueOf((byte)1));
			}
			if(creature instanceof EntityZombie || creature instanceof EntitySkeleton){
				if(creature instanceof EntityZombie){
					creature.setCurrentItemOrArmor(0, ItemApi.getItem("itemSwordThaumium", 0));
				}else if(creature instanceof EntitySkeleton){
					creature.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				}
				creature.setCurrentItemOrArmor(1, ItemApi.getItem("itemHelmetThaumium", 0));
				creature.setCurrentItemOrArmor(2, ItemApi.getItem("itemChestThaumium", 0));
				creature.setCurrentItemOrArmor(3, ItemApi.getItem("itemLegsThaumium", 0));
				creature.setCurrentItemOrArmor(4, ItemApi.getItem("itemBootsThaumium", 0));
			}
			
	        world.spawnEntityInWorld(creature);
	        creature.playLivingSound();
		}
    }
    
    private boolean reactSunlight(World world, int x, int y, int z){
    	boolean flag = false;
        if(world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == 0 && CustomPotionHelper.isSupport(this.potionEffect)){
           	flag |= world.canBlockSeeTheSky(x, y, z) && world.getBlockLightValue(x, y, z) == 15;
//           	flag |= world.canBlockSeeTheSky(x, y, z);
           	flag |= world.provider.isHellWorld;
            if(flag){
            	world.setBlockToAir(x, y, z);
            	createExplosion(world, x, y, z);
            }
        }
        return flag;
    }
    
    private void createExplosion(World world, int x, int y ,int z){
    	if(explode){
    		world.createExplosion(null, x, y, z, 4, true);
    	}
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand){
		super.updateTick(world, x, y, z, rand);
		if(reactSunlight(world, x, y, z)) { return; }
//		Block aboveBlock = world.getBlock(x, y + 1, z);
		int count = 0;
//		int waterCount = 0;
//		int lavaCount = 0;
		for(int i = 0; i < 4; i++){
			int offsetX = 0;
			int offsetZ = 0;
			switch(i){
			case 0:
				offsetX = -1;
				break;
			case 1:
				offsetX = 1;
				break;
			case 2:
				offsetZ = -1;
				break;
			case 3:
				offsetZ = 1;
				break;
			}
			Block block = world.getBlock(x + offsetX, y, z + offsetZ);
			if(block == this && world.getBlockMetadata(x + offsetX, y, z + offsetZ) == 0){
				count++;
			}
//			if(block == Blocks.water){
//				waterCount++;
//			}
//			if(block == Blocks.lava){
//				lavaCount++;
//			}
		}
//		block = world.getBlock(x - 1, y, z);
//		if(block == this && world.getBlockMetadata(x - 1, y, z) == 0){
//			count++;
//		}
//		block = world.getBlock(x, y, z + 1);
//		if(block == this && world.getBlockMetadata(x, y, z + 1) == 0){
//			count++;
//		}
//		block = world.getBlock(x, y, z - 1);
//		if(block == this && world.getBlockMetadata(x, y, z - 1) == 0){
//			count++;
//		}
//		if(count >= 2){
		
//		if(aboveBlock == Blocks.water || waterCount > 0){
//			world.setBlock(x, y, z, Blocks.water, 1, 2);
//            world.scheduleBlockUpdate(x, y, z, Blocks.water, 0);
//            world.notifyBlocksOfNeighborChange(x, y, z, Blocks.water);
//		}else if(aboveBlock == Blocks.lava || lavaCount > 0){
//			if(world.getBlockMetadata(x, y, z) == 0){
//				world.createExplosion(null, x, y, z, 4, true);
//				world.setBlockToAir(x, y, z);
//	            world.scheduleBlockUpdate(x, y, z, Blocks.air, 0);
//	            world.notifyBlocksOfNeighborChange(x, y, z, Blocks.air);
//			}else{
//				world.setBlock(x, y, z, Blocks.lava, 1, 2);
//	            world.scheduleBlockUpdate(x, y, z, Blocks.lava, 0);
//	            world.notifyBlocksOfNeighborChange(x, y, z, Blocks.lava);
//			}
//		}else if(potCount >= 2 && world.getBlock(x, y - 1, z).isNormalCube()){
		if(count >= 2 && world.getBlock(x, y - 1, z).isNormalCube()){
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            world.scheduleBlockUpdate(x, y, z, this, 0);
            world.notifyBlocksOfNeighborChange(x, y, z, this);
		}
		
//		if(potCount >= 2 && world.getBlock(x, y - 1, z).isNormalCube()){
//			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
//            world.scheduleBlockUpdate(x, y, z, this, 0);
//            world.notifyBlocksOfNeighborChange(x, y, z, this);
//		}
	}
	
	@Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(!world.isRemote && entity instanceof EntityLivingBase){
			if(CustomPotionHelper.isInstant(potionEffect)){
				if(world.getWorldTime() % 60 == 0){
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(potionEffect, CustomPotionHelper.INSTANT_DURATION, 0));
					if(potionEffect == CustomPotionHelper.HARMING && entity instanceof EntityVillager){
						if(world.rand.nextDouble() < 0.05){
							spawnItem(world, x, y, z, new ItemStack(Items.emerald));
						}
					}
				}
			}else{
				if(world.getWorldTime() % 20 == 0){
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(potionEffect, 100, 0));
				}
			}
		}
	}
	
	private void spawnItem(World world, double x, double y, double z, ItemStack itemstack){
	    float f = 0.7F;
	    double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	    double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	    double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	    EntityItem entityitem = 
	    		new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, itemstack);
	    entityitem.delayBeforeCanPickup = 10;
	    world.spawnEntityInWorld(entityitem);
	}
//	
//	@Override
//    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float p_149746_6_) {
//		if(entity instanceof EntityLiving && !CustomPotionHelper.isInstant(potionEffect)){
//			((EntityLiving)entity).addPotionEffect(new PotionEffect(potionEffect, 100, 0));
//		}
//		
//	}
	

	
	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.canDisplace(world, x, y, z);
	}
	
	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
			return false;
		}
		return super.displaceIfPossible(world, x, y, z);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.theIcon[1] : this.theIcon[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.theIcon = new IIcon[] {
				iconRegister.registerIcon("mariri:potion" + potionEffect + "_still"),
				iconRegister.registerIcon("mariri:potion" + potionEffect + "_flow")};
	}
}
