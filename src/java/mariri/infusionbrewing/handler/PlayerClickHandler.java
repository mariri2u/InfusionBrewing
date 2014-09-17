package mariri.infusionbrewing.handler;

import mariri.infusionbrewing.misc.CustomSpawnerHelper;
import mariri.infusionbrewing.misc.ThaumcraftHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerClickHandler {
	
//	public static String BEDTWEAKS_SET_RESPAWN_MESSAGE;
//	public static String BEDTWEAKS_NO_SLEEP_MESSAGE;

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent e){
		if(!e.entityPlayer.worldObj.isRemote){
			if(e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK){
				if(!e.entityPlayer.isSneaking()){
//					onRightClickBlock(e);
				}else{
					onShiftRightClickBlock(e);
				}
			}
		}
	}
	
	private void onRightClickBlock(PlayerInteractEvent e){
		World world = e.entityPlayer.worldObj;
		Block block = world.getBlock(e.x, e.y, e.z);// Block.blocksList[world.getBlockId(e.x, e.y, e.z)];
		int meta = world.getBlockMetadata(e.x, e.y, e.z);
		
		if(		e.entityPlayer.inventory.getCurrentItem() != null &&
				ThaumcraftHelper.isItemWandCasting(e.entityPlayer.inventory.getCurrentItem().getItem()) &&
				block == Blocks.mob_spawner){
        	TileEntity te = world.getTileEntity(e.x, e.y, e.z);
        	NBTTagCompound tag = new NBTTagCompound();
        	te.writeToNBT(tag);
        	CustomSpawnerHelper helper = CustomSpawnerHelper.getInstanceFromNBTTag(tag);
        	
        	helper.incrementMaxNearbyEntities();
        	helper.incrementMaxSpawnDelay();
        	helper.incrementMinSpawnDelay();
        	helper.incrementSpawnCount();
        	
        	helper.writeToNBTTag(tag);
        	
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("Debug Info"));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- EntityId: " + helper.getEntityId()));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- MaxNearbyEntities: " + helper.getMaxNearbyEntities()));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- MaxSpawnDelay: " + helper.getMaxSpawnDelay()));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- MinSpawnDelay: " + helper.getMinSpawnDelay()));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- SpawnCount: " + helper.getSpawnCount()));
        	
        	te.readFromNBT(tag);
        	
//        	EntityMinecartMobSpawner mobcart = (EntityMinecartMobSpawner)EntityMinecart.createMinecart(world, e.x, e.y + 1, e.z - 1, 4);
//        	mobcart.writeToNBT(tag);
//        	
//        	helper.writeNBTTag(tag);
//        	
//        	world.spawnEntityInWorld(mobcart);
//        	e.entityPlayer.addChatComponentMessage(new ChatComponentText(tag.getString("EntityId")));
//        	mobcart.readFromNBT(tag);
//        	e.entityPlayer.addChatComponentMessage(new ChatComponentText(mobcart.func_98039_d().getEntityNameToSpawn()));
//        	mobcart.onUpdate();
		}
	}
	

	
	private void onShiftRightClickBlock(PlayerInteractEvent e){
		World world = e.entityPlayer.worldObj;
		Block block = world.getBlock(e.x, e.y, e.z);// Block.blocksList[world.getBlockId(e.x, e.y, e.z)];
		int meta = world.getBlockMetadata(e.x, e.y, e.z);
		
		if(ThaumcraftHelper.isItemWandCasting(e.entityPlayer.inventory.getCurrentItem().getItem()) && block == Blocks.mob_spawner){
        	TileEntity te = world.getTileEntity(e.x, e.y, e.z);
        	NBTTagCompound tag = new NBTTagCompound();
        	te.writeToNBT(tag);
        	CustomSpawnerHelper helper = CustomSpawnerHelper.getInstanceFromNBTTag(tag);
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("Spawner Info (" + helper.getEntityId() + ")"));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- Current Power: " + helper.getPowerCode()));
        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- Current Speed: " + helper.getSpeedCode()));
//        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- MaxNearbyEntities: " + helper.getMaxNearbyEntities()));
//        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- MaxSpawnDelay: " + helper.getMaxSpawnDelay()));
//        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- MinSpawnDelay: " + helper.getMinSpawnDelay()));
//        	e.entityPlayer.addChatComponentMessage(new ChatComponentText("- SpawnCount: " + helper.getSpawnCount()));
        	e.setCanceled(true);
		}
	}
	

}
