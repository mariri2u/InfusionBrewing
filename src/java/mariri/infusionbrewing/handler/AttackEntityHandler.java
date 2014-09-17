package mariri.infusionbrewing.handler;

import java.util.List;

import mariri.infusionbrewing.misc.ThaumcraftHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AttackEntityHandler {

	
	@SubscribeEvent
	public void onPlayerATtack(AttackEntityEvent e){
		World world = e.entityPlayer.worldObj;

		if(ThaumcraftHelper.isItemWandCasting(e.entityPlayer.inventory.getCurrentItem().getItem())){
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText(e.target.getClass().getCanonicalName()));

			if(e.target instanceof EntityMinecartMobSpawner){
				int x = e.target.chunkCoordX;
				int y = e.target.chunkCoordY;
				int z = e.target.chunkCoordZ;
				
				EntityMinecartMobSpawner mobcart = (EntityMinecartMobSpawner)e.target;
	        	NBTTagCompound tag = new NBTTagCompound();
	        	mobcart.writeToNBT(tag);
	        	
	        	world.setBlock(mobcart.chunkCoordX, mobcart.chunkCoordY, mobcart.chunkCoordZ, Blocks.mob_spawner);
	        	TileEntity tileentity = new TileEntity();
	        	tileentity.readFromNBT(tag);
	        	world.setTileEntity(mobcart.chunkCoordX, mobcart.chunkCoordY, mobcart.chunkCoordZ, tileentity);
				
	        	mobcart.setDead();
	        	
	    		List<EntityLiving> list = world.getEntitiesWithinAABB(
	    				EntityLiving.class,
	    				AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1 , x + 2, y + 2, z + 2));
	    		for(EntityLiving entity : list){
	    			entity.setDead();
	    		}
	        	
	        	e.setCanceled(true);
			}
		}
	}
}
