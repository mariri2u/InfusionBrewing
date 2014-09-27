package mariri.infusionbrewing.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CustomSpawnerHelper {
	private int delay;
	private int maxNearbyEntities;
	private int maxSpawnDelay;
	private int minSpawnDelay;
	private int requiredPlayerRange;
	private int spawnCount;
	private int spawnRange;
	private String entityId;
	
	private static final String id = "MobSpawner";
	
	public static final int[] MAX_NEAR_BY_ENTITIES_ARRAY = new int[] { 6, 12, 16, 20, 24 };
	public static final int[] MAX_SPAWN_DELAY_ARRAY = new int[] { 800, 320, 160, 80, 40 };
	public static final int[] MIN_SPAWN_DELAY_ARRAY = new int[] { 200, 160, 80, 40 , 20 };
	public static final int[] SPAWN_COUNT_ARRAY = new int[] { 4, 6, 8, 10, 12 };
	
	private CustomSpawnerHelper(){
		delay = 0;
		maxNearbyEntities = 6;
		maxSpawnDelay = 800;
		minSpawnDelay = 200;
		requiredPlayerRange = 16;
		spawnCount = 4;
		spawnRange = 4;
		entityId = "Pig";
	}
	
	public CustomSpawnerHelper incrementPower(){
		incrementMaxNearbyEntities();
		incrementSpawnCount();
		return this;
	}
	
	public CustomSpawnerHelper incrementSpeed(){
		incrementMaxSpawnDelay();
		incrementMinSpawnDelay();
		return this;
	}
	
	public int getSpeedCode(){
		int max = getMaxSpawnDelayCode();
		int min = getMinSpawnDelayCode();
		return (min < max) ? min : max;
	}
	
	public int getPowerCode(){
		int near = getMaxNearbyEntitiesCode();
		int spawn = getSpawnCountCode();
		return (near < spawn) ? near : spawn;
	}
	
	public CustomSpawnerHelper incrementMaxNearbyEntities(){
		if(!isCappedMaxNearbyEntities()){
			setMaxNearbyEntitiesCode(getMaxNearbyEntitiesCode() + 1);
		}
		return this;
	}
	
	public CustomSpawnerHelper incrementMaxSpawnDelay(){
		if(!isCappedMaxSpawnDelay()){
			setMaxSpawnDelayCode(getMaxSpawnDelayCode() + 1);
		}
		return this;
	}
	
	public CustomSpawnerHelper incrementMinSpawnDelay(){
		if(!isCappedMinSpawnDelay()){
			setMinSpawnDelayCode(getMinSpawnDelayCode() + 1);
		}
		return this;
	}
	
	public CustomSpawnerHelper incrementSpawnCount(){
		if(!isCappedSpawnCount()){
			setSpawnCountCode(getSpawnCountCode() + 1);
		}
		return this;
	}
	
	public boolean isCappedMaxNearbyEntities(){
		return MAX_NEAR_BY_ENTITIES_ARRAY[MAX_NEAR_BY_ENTITIES_ARRAY.length - 1] == maxNearbyEntities;
	}
	
	public boolean isCappedMaxSpawnDelay(){
		return MAX_SPAWN_DELAY_ARRAY[MAX_SPAWN_DELAY_ARRAY.length - 1] == maxSpawnDelay;
	}

	public boolean isCappedMinSpawnDelay(){
		return MIN_SPAWN_DELAY_ARRAY[MIN_SPAWN_DELAY_ARRAY.length - 1] == minSpawnDelay;
	}

	public boolean isCappedSpawnCount(){
		return SPAWN_COUNT_ARRAY[SPAWN_COUNT_ARRAY.length - 1] == spawnCount;
	}

	
	public CustomSpawnerHelper setMaxNearbyEntities(int value){
		maxNearbyEntities = value;
		return this;
	}
	
	public CustomSpawnerHelper setMaxSpawnDelay(int value){
		maxSpawnDelay = value;
		return this;
	}
	
	public CustomSpawnerHelper setMinSpawnDelay(int value){
		minSpawnDelay = value;
		return this;
	}
	
	public CustomSpawnerHelper setSpawnCount(int value){
		spawnCount = value;
		return this;
	}
	
	public int getMaxNearbyEntities(){
		return maxNearbyEntities;
	}
	
	public int getMaxSpawnDelay(){
		return maxSpawnDelay;
	}
	
	public int getMinSpawnDelay(){
		return minSpawnDelay;
	}
	
	public int getSpawnCount(){
		return spawnCount;
	}
	
	public String getEntityId(){
		return entityId;
	}
	
	public CustomSpawnerHelper setMaxNearbyEntitiesCode(int code){
		if(code >= MAX_NEAR_BY_ENTITIES_ARRAY.length){ return this; }
		return this.setMaxNearbyEntities(MAX_NEAR_BY_ENTITIES_ARRAY[code]);
	}
	
	public CustomSpawnerHelper setMaxSpawnDelayCode(int code){
		if(code >= MAX_SPAWN_DELAY_ARRAY.length){ return this; }
		return this.setMaxSpawnDelay(MAX_SPAWN_DELAY_ARRAY[code]);
	}

	public CustomSpawnerHelper setMinSpawnDelayCode(int code){
		if(code >= MIN_SPAWN_DELAY_ARRAY.length){ return this; }
		return this.setMinSpawnDelay(MIN_SPAWN_DELAY_ARRAY[code]);
	}

	public CustomSpawnerHelper setSpawnCountCode(int code){
		if(code >= SPAWN_COUNT_ARRAY.length){ return this; }
		return this.setSpawnCount(SPAWN_COUNT_ARRAY[code]);
	}
	
	public int getMaxNearbyEntitiesCode(){
		for(int i = 0; i < MAX_NEAR_BY_ENTITIES_ARRAY.length; i++){
			if(maxNearbyEntities <= MAX_NEAR_BY_ENTITIES_ARRAY[i]){
				return i;
			}
		}
		return 0;
	}
	
	public int getMaxSpawnDelayCode(){
		for(int i = 0; i < MAX_SPAWN_DELAY_ARRAY.length; i++){
			if(maxSpawnDelay >= MAX_SPAWN_DELAY_ARRAY[i]){
				return i;
			}
		}
		return 0;
	}
	
	public int getMinSpawnDelayCode(){
		for(int i = 0; i < MIN_SPAWN_DELAY_ARRAY.length; i++){
			if(minSpawnDelay >= MIN_SPAWN_DELAY_ARRAY[i]){
				return i;
			}
		}
		return 0;
	}
	
	public int getSpawnCountCode(){
		for(int i = 0; i < SPAWN_COUNT_ARRAY.length; i++){
			if(spawnCount <= SPAWN_COUNT_ARRAY[i]){
				return i;
			}
		}
		return 0;
	}
	
	public void writeToNBTTag(NBTTagCompound tag){
		tag.setShort("MaxNearbyEntities", (short)maxNearbyEntities);
		tag.setShort("MaxSpawnDelay", (short)maxSpawnDelay);
		tag.setShort("MinSpawnDelay", (short)minSpawnDelay);
//		tag.setShort("RequirePlayerRange", (short)requiredPlayerRange);
		tag.setShort("SpawnCount", (short)spawnCount);
//		tag.setShort("SpawnRange", (short)spawnRange);
//		tag.setString("EntityId", entityId);
//		tag.setString("id", this.id);
	}
	
	public void readFromNBTTag(NBTTagCompound tag){
		maxNearbyEntities = tag.getShort("MaxNearbyEntities");
		maxSpawnDelay = tag.getShort("MaxSpawnDelay");
		minSpawnDelay = tag.getShort("MinSpawnDelay");
		requiredPlayerRange = tag.getShort("RequiredPlayerRange");
		spawnCount = tag.getShort("SpawnCount");
		spawnRange = tag.getShort("SpawnRange");
		delay = tag.getShort("Delay");
		entityId = tag.getString("EntityId");
	}
	
	public static CustomSpawnerHelper getInstanceFromNBTTag(NBTTagCompound tag){
		CustomSpawnerHelper inst = new CustomSpawnerHelper();
		if(tag == null) { return inst; }
		inst.readFromNBTTag(tag);
		return inst;
	}
	

	
	public static void showSpawnerLevel(World world, int x, int y, int z, EntityPlayer player){
    	TileEntity te = world.getTileEntity(x, y, z);
    	NBTTagCompound tag = new NBTTagCompound();
    	te.writeToNBT(tag);
    	CustomSpawnerHelper helper = CustomSpawnerHelper.getInstanceFromNBTTag(tag);
    	player.addChatComponentMessage(new ChatComponentText("Spawner Info (" + helper.getEntityId() + ")"));
    	player.addChatComponentMessage(new ChatComponentText("- Current Power: " + helper.getPowerCode()));
    	player.addChatComponentMessage(new ChatComponentText("- Current Speed: " + helper.getSpeedCode()));
        world.playSoundAtEntity(player, "random.orb", 1.0F, 1.0F);
	}

	public static void showSpawnerDetails(World world, int x, int y, int z, EntityPlayer player){
    	TileEntity te = world.getTileEntity(x, y, z);
    	NBTTagCompound tag = new NBTTagCompound();
    	te.writeToNBT(tag);
    	CustomSpawnerHelper helper = CustomSpawnerHelper.getInstanceFromNBTTag(tag);
    	player.addChatComponentMessage(new ChatComponentText("Debug Info"));
    	player.addChatComponentMessage(new ChatComponentText("- EntityId: " + helper.getEntityId()));
    	player.addChatComponentMessage(new ChatComponentText("- Delay: " + helper.delay));
    	player.addChatComponentMessage(new ChatComponentText("- MaxNearbyEntities: " + helper.getMaxNearbyEntities()));
    	player.addChatComponentMessage(new ChatComponentText("- MaxSpawnDelay: " + helper.getMaxSpawnDelay()));
    	player.addChatComponentMessage(new ChatComponentText("- MinSpawnDelay: " + helper.getMinSpawnDelay()));
    	player.addChatComponentMessage(new ChatComponentText("- SpawnCount: " + helper.getSpawnCount()));
    	player.addChatComponentMessage(new ChatComponentText("- SpawnRange: " + helper.spawnRange));
    	player.addChatComponentMessage(new ChatComponentText("- RequiredPlayerRange: " + helper.requiredPlayerRange));
        world.playSoundAtEntity(player, "random.orb", 1.0F, 1.0F);
	}
}
