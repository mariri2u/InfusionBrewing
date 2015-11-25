package mariri.infusionbrewing.misc;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.Constants;

public class CustomPotionHelper {

	private int id;
	private int duration;
	private int amplifier;
	
	public final static int[] DURATION_TABLE = new int[]{ 2400, 4800, 9600, 19200 };
	
	private final static int[] INSTANT_IDS = new int[] { 6, 7 };
	public final static int INSTANT_DURATION = 1;
	
	private final static int[] NO_AMPLIFIER_IDS = new int[] { 9, 12, 13, 14, 15, 16 };
	
//	public static final int MIN_SPLASH_METADATA = 16000;
	
	public static final int MAX_AMPLIFIER = 3;
	
	public final static int[] SUPPORT_IDS = new int[] { 1, 3, 5, 6, 8, 10, 11, 12, 13, 14, 16, 21, 22, 23 };
	
	public static final int SWIFTNESS = 1;
	public static final int SLOWNESS = 2;
	public static final int HASTE = 3;
	public static final int DULLNESS = 4;
	public static final int STRENGTH = 5;
	public static final int HEALING = 6;
	public static final int HARMING = 7;
	public static final int LEAPING = 8;
	public static final int NAUSEA = 9;
	public static final int REGENATION = 10;
	public static final int RESISTANCE = 11;
	public static final int FIRE_RESISTANCE = 12;
	public static final int WATER_BREATHING = 13;
	public static final int INVISIBILITY = 14;
	public static final int BLINDNESS = 15;
	public static final int NIGHT_VISION = 16;
	public static final int HUNGER = 17;
	public static final int WEAKNESS = 18;
	public static final int POISON = 19;
	public static final int DECAY = 20;
	public static final int HEALTH_BOOST = 21;
	public static final int ABSORPTION = 22;
	public static final int SATURATION = 23;
	
	public static final int EFFECT_VALUE = 23;
	
	public static final int EFFECT_SHIFT = 0;
	public static final int AMPLIFIER_SHIFT = 5;
	public static final int SPLASH_SHIFT = 14;
	
	public static final int EFFECT_MASK =		0x0000001F << EFFECT_SHIFT;
	public static final int AMPLIFIER_MASK = 	0x00000003 << AMPLIFIER_SHIFT;
	public static final int SPLASH_MASK =		0x00000001 << SPLASH_SHIFT;
	
	public CustomPotionHelper(){
		id = 1;
		duration = this.isInstant() ? INSTANT_DURATION : DURATION_TABLE[0];
		amplifier = 0;
	}
	
	public CustomPotionHelper(int id){
		this.id = id;
		duration = this.isInstant() ? INSTANT_DURATION : DURATION_TABLE[0];
		amplifier = 0;
	}
	
	public CustomPotionHelper(int id, int duration, int amplifier){
		this.id = id;
		this.duration = this.isInstant() ? INSTANT_DURATION : duration;
		this.amplifier = this.isNoAmplifier() ? 0 : amplifier;
	}
	
	public CustomPotionHelper setId(int value){
		this.id = value;
		return this;
	}
	
	public CustomPotionHelper setDuration(int value){
		this.duration = this.isInstant() ? INSTANT_DURATION : value;
		return this;
	}
	
	public CustomPotionHelper setAmplifier(int value){
		this.amplifier = this.isNoAmplifier() ? 0 : value;
		return this;
	}
	
	public int getId(){
		return id;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public int getAmplifier(){
		return amplifier;
	}
	
	public int getDurationCode(){
		for(int i = 0; i < DURATION_TABLE.length; i++){
			if(duration <= DURATION_TABLE[i]){
				return i;
			}
		}
		return 0;
	}
	
	public static int getMaxDuration(){
		return DURATION_TABLE.length - 1;
	}
	
	public CustomPotionHelper incrementDurationCode(){
		return this.setDurationCode(this.getDurationCode() + 1);
	}
	
	public CustomPotionHelper incrementAmplifier(){
		if(isMaxAmplifier()){ return this; }
		return this.setAmplifier(this.getAmplifier() + 1);
	}
	
	public boolean isSupport(){
		return isSupport(id);
	}
	
	public static boolean isSupport(int id){
		for(int i : SUPPORT_IDS){
			if(i == id){ return true; }
		}
		return false;
	}
	
	public boolean isInstant(){
		return isInstant(id);
	}
	
	public static boolean isInstant(int id){
		for(int i : INSTANT_IDS){
			if(i == id){ return true; }
		}
		return false;
	}
	
	public boolean isNoAmplifier(){
		return isNoAmplifier(id);
	}
	
	public static boolean isNoAmplifier(int id){
		for(int i : NO_AMPLIFIER_IDS){
			if(i == id){ return true; }
		}
		return false;
	}
	
	public boolean isMaxDuration(){
		if(isInstant()) { return true; }
		if(duration == DURATION_TABLE[DURATION_TABLE.length - 1]){ return true; }
		return false;
	}
	
	public boolean isMaxAmplifier(){
		if(isNoAmplifier()) { return true; }
		if(amplifier < MAX_AMPLIFIER){ return false; }
		return true;
	}
	
	public CustomPotionHelper setDurationCode(int code){
		if(code >= DURATION_TABLE.length){ return this; }
		return this.setDuration(DURATION_TABLE[code]);
	}
	
	public void writeToNBTTag(NBTTagCompound tag){
		tag.setByte("Id", (byte)id);
		tag.setByte("Amplifier", (byte)amplifier);
		tag.setInteger("Duration", duration);
	}
	
	public static NBTTagCompound createVoidNBTTag(){
		NBTTagCompound effect = new NBTTagCompound();
        NBTTagList customPotionEffect = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        customPotionEffect.appendTag(effect);
        tag.setTag("CustomPotionEffects", customPotionEffect);
        return tag;
	}
	
	public PotionEffect getPotionEffect(){
		return new PotionEffect(id, duration, amplifier);
	}
	
	public static CustomPotionHelper getInstanceFromNBTTag(NBTTagCompound tag){
		CustomPotionHelper inst = new CustomPotionHelper();
		if(tag == null) { return inst; }
		inst.setId(tag.getByte("Id")).setAmplifier(tag.getByte("Amplifier")).setDuration(tag.getInteger("Duration"));
		return inst;
	}
	
	public static NBTTagCompound findPotionNBT(ItemStack itemstack){
		try{
			return itemstack.getTagCompound().getTagList("CustomPotionEffects", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
		}catch(NullPointerException e){
			return new NBTTagCompound();
		}
	}
	
	public static NBTTagCompound findPotionNBT(NBTTagCompound tag){
		try{
			return tag.getTagList("CustomPotionEffects", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
		}catch(NullPointerException e){
			return new NBTTagCompound();
		}
	}
	
	public ItemStack getSampleItem(boolean splash){
		return getSampleItem(id, getDurationCode(), amplifier, splash);
	}
	
	public static ItemStack getSampleItem(int id, int durationCode, int amplifier, boolean splash){
		ItemStack itemstack = new ItemStack(Items.potionitem);
		CustomPotionHelper potion = new CustomPotionHelper(id, DURATION_TABLE[durationCode], amplifier);
		itemstack.setTagCompound(createVoidNBTTag());
		potion.writeToNBTTag(findPotionNBT(itemstack));
		itemstack.setItemDamage(vanillaMetadataTable[id - 1][splash ? 2 : 0]);
		return itemstack;
	}
	
	public static ItemStack getSampleItem(int id, int durationCode, int amplifier, boolean splash, int stacksize){
		ItemStack itemstack = getSampleItem(id, durationCode, amplifier, splash);
		itemstack.stackSize = stacksize;
		return itemstack;
	}
	
	public CustomPotionHelper decodeFromCustomMetadata(int meta){
		this.id = (meta & EFFECT_MASK) >> EFFECT_SHIFT;
		this.amplifier = (meta & AMPLIFIER_MASK) >> AMPLIFIER_SHIFT;
		return this;
	}
	
	public static CustomPotionHelper createFromCustomMetadata(int meta){
		CustomPotionHelper inst = new CustomPotionHelper();
		inst.decodeFromCustomMetadata(meta);
		return inst;
	}
	
	public int encodeToCustomMetadata(boolean splash){
		return encodeToCustomMetadata(this.id, this.amplifier, splash);
	}
	
	public static int encodeToCustomMetadata(int id, int amplifier, boolean splash){
		int meta = 0;
		meta |= id << EFFECT_SHIFT;
		meta |= amplifier << AMPLIFIER_SHIFT;
		meta |= splash ? SPLASH_MASK : 0;
		return meta;
	}
	
	public static final int[][] vanillaMetadataTable = new int[][]{
		new int[] {8194, 8226, 16386, 8258}, // swiftness
		new int[] {8234, 8266, 16426, 3}, // slow
		new int[] {1, 2, 16384, 3}, // haste
		new int[] {1, 2, 16384, 3}, // dullness
		new int[] {8201, 8233, 16393, 3}, // strength
		new int[] {8261, 8299, 16453, 3}, // healing
		new int[] {8268, 8236, 16460, 3}, // harming
		new int[] {1, 2, 16384, 3}, // leaping
		new int[] {1, 2, 16384, 3}, // nausea
		new int[] {8193, 8225, 16385, 3}, // regenation
		new int[] {1, 2, 16384, 3}, // resistance
		new int[] {8227, 8259, 16419, 3}, // fire resistance
		new int[] {8237, 8269, 16429, 3}, // water breathing
		new int[] {8238, 8270, 16430, 3}, // invisibility
		new int[] {1, 2, 16384, 3}, // blindness
		new int[] {8230, 8262, 16422, 3}, // night vision
		new int[] {1, 2, 16384, 3}, // hunger
		new int[] {8232, 8264, 16424, 3}, // weakness
		new int[] {8196, 8228, 16388, 3}, // poison
		new int[] {1, 2, 16384, 3}, // decay
		new int[] {1, 2, 16384, 3}, // health boost
		new int[] {1, 2, 16384, 3}, // absorption
		new int[] {1, 2, 16384, 3} }; // saturation
}
