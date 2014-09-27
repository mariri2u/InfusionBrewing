package mariri.infusionbrewing.handler;

import mariri.infusionbrewing.InfusionBrewing;
import mariri.infusionbrewing.misc.CustomSpawnerHelper;
import mariri.infusionbrewing.misc.ThaumcraftHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerClickHandler {
	
	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent e){
		if(!e.entityPlayer.worldObj.isRemote){
			if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK){
				if(!e.entityPlayer.isSneaking()){
					onRightClickBlock(e);
				}else{
					onShiftRightClickBlock(e);
				}
			}
		}
	}
	
	private void onRightClickBlock(PlayerInteractEvent e){
		World world = e.entityPlayer.worldObj;
		Block block = world.getBlock(e.x, e.y, e.z);
		int meta = world.getBlockMetadata(e.x, e.y, e.z);
		
		if(		e.entityPlayer.inventory.getCurrentItem() != null &&
				ThaumcraftHelper.isItemWandCasting(e.entityPlayer.inventory.getCurrentItem().getItem()) &&
				block == Blocks.mob_spawner){
			CustomSpawnerHelper.showSpawnerLevel(world, e.x, e.y, e.z, e.entityPlayer);
			e.setCanceled(true);
		}
	}
	

	
	private void onShiftRightClickBlock(PlayerInteractEvent e){
		World world = e.entityPlayer.worldObj;
		Block block = world.getBlock(e.x, e.y, e.z);
		int meta = world.getBlockMetadata(e.x, e.y, e.z);
		
		if(		e.entityPlayer.inventory.getCurrentItem() != null &&
				ThaumcraftHelper.isItemWandCasting(e.entityPlayer.inventory.getCurrentItem().getItem()) &&
				block == Blocks.mob_spawner){
			if(InfusionBrewing.SHOW_SPAWNER_DETAILS){
				CustomSpawnerHelper.showSpawnerDetails(world, e.x, e.y, e.z, e.entityPlayer);
			}else{
				CustomSpawnerHelper.showSpawnerLevel(world, e.x, e.y, e.z, e.entityPlayer);
			}
        	e.setCanceled(true);
		}
	}
}
