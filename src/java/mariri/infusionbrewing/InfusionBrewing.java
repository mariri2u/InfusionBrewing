package mariri.infusionbrewing;

import java.util.ArrayList;

import mariri.infusionbrewing.block.BlockFluidPotion;
import mariri.infusionbrewing.block.BlockSemiSolidPotion;
import mariri.infusionbrewing.block.MaterialPotion;
import mariri.infusionbrewing.handler.BehaviorDispencePotionBucket;
import mariri.infusionbrewing.handler.BehaviorDispenseMagicBucket;
import mariri.infusionbrewing.handler.BehaviorDispenseMagicGlass;
import mariri.infusionbrewing.handler.FillBucketHandler;
import mariri.infusionbrewing.handler.PlayerClickHandler;
import mariri.infusionbrewing.item.ItemMagicBottle;
import mariri.infusionbrewing.item.ItemMagicBucket;
import mariri.infusionbrewing.item.ItemPotionBaubles;
import mariri.infusionbrewing.item.ItemPotionBucket;
import mariri.infusionbrewing.item.ItemPotionFocus;
import mariri.infusionbrewing.item.ItemSpawnerUpgrade;
import mariri.infusionbrewing.misc.CustomPotionHelper;
import mariri.infusionbrewing.misc.ResearchHelper;
import mariri.infusionbrewing.recipe.InfusionBrewingRecipeAmplifier;
import mariri.infusionbrewing.recipe.InfusionBrewingRecipeBaubles;
import mariri.infusionbrewing.recipe.InfusionBrewingRecipeDuration;
import mariri.infusionbrewing.recipe.InfusionBrewingRecipeFocus;
import mariri.infusionbrewing.recipe.InfusionBrewingRecipeSplash;
import mariri.infusionbrewing.recipe.InfusionBrewingRecipeUpgrade;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import baubles.api.BaubleType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = InfusionBrewing.MODID, version = InfusionBrewing.VERSION, dependencies = InfusionBrewing.DEPENDENCIES )
public class InfusionBrewing {
    public static final String MODID = "InfusionBrewing";
    public static final String VERSION = "1.7.10-1.3a";
    public static final String DEPENDENCIES = "required-after:Thaumcraft";
//    public static final String DEPENDENCIES = "after:Thaumcraft";
    
    public static BlockFluidPotion[] blockFluidPotions;
    public static BlockSemiSolidPotion blockSemiSolidPotion;
    public static Fluid[] fluidPotions;
    public static ItemPotionBucket[] itemPotionBuckets;
    public static ItemMagicBucket itemMagicBucket;
//    public static BlockPotionCauldron blockPotionCauldron;
    public static ItemMagicBottle itemMagicBottle;
    public static ItemPotionFocus itemPotionFocus;
    public static ItemSpawnerUpgrade itemSpawnerUpgrade;
    public static int POTION_COUNT = 23;
    
    public static CreativeTabs creativeTab;
    
    public static Material potionMaterial;
    
    public static final String RESEARCH_CATEGORY = "INFUSION_BREWING";
    
    public static final String RESEARCH_FLUID_POTION = "FLUID_POTION";
    
//    public static final String RESEARCH_BREWING = "Brewing";
    
    public static final String RESEARCH_SPLASH = "SPLASH";
    public static final String RESEARCH_AMPLIFIER = "AMPLIFIER";
    public static final String RESEARCH_DURATION = "DURATION";
    
    public static final String RESEARCH_MAGIC_BOTTLE = "MAGIC_BOTTLE";
    public static final String RESEARCH_MAGIC_BUCKET = "MAGIC_BUCKET";
    
//    public static final String RESEARCH_BAUBLES = "Baubles";
    
    public static final String RESEARCH_AMULET = "POTION_AMULET";
    public static final String RESEARCH_RING = "POTION_RING";
    public static final String RESEARCH_BELT = "POTION_BELT";
    
    public static final String RESEARCH_FOCUS = "POTION_FOCUS";
    
    public static final String RESEARCH_SPAWNER_POWER = "SPAWNER_POWER";
    public static final String RESEARCH_SPAWNER_SPEED = "SPAWNER_SPEED";
    
//    public static final String INFUSION_BREWING_RESEARCH = "Brewing";
    
    public static boolean ENABLE_REACT_EXPLOSION;
    public static int REACT_EXPLOSION_POWER;
    public static boolean ENABLE_REACT_SPAWN;
    public static boolean ENABLE_INFINITY_SOURCE;
    public static boolean DISPENSE_MAGIC_BUCKET;
    public static int POTION_STACK_SIZE;
    
    public static boolean SHOW_SPAWNER_DETAILS;
   
    public static ItemPotionBaubles[] itemPotionBaubles;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        
        ENABLE_REACT_EXPLOSION = config.get(Configuration.CATEGORY_GENERAL, "enableReactExplosion", true).getBoolean(true);
        REACT_EXPLOSION_POWER = config.get(Configuration.CATEGORY_GENERAL, "reactExplosionPower", 3).getInt();
        ENABLE_REACT_SPAWN = config.get(Configuration.CATEGORY_GENERAL, "enableReactSpawn", true).getBoolean(true);
        ENABLE_INFINITY_SOURCE = config.get(Configuration.CATEGORY_GENERAL, "enableInfinitySource", true).getBoolean(true); 
        DISPENSE_MAGIC_BUCKET = config.get(Configuration.CATEGORY_GENERAL, "dispenseMagicBucket", false).getBoolean(false); 
        POTION_STACK_SIZE = config.get(Configuration.CATEGORY_GENERAL, "potionStackSize", 8).getInt(); 
      
        SHOW_SPAWNER_DETAILS = config.get(Configuration.CATEGORY_GENERAL, "showSpawnerDetails", false).getBoolean(false);
        
        config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	Items.potionitem.setMaxStackSize(POTION_STACK_SIZE);
    	
    	creativeTab = new CreativeTabBrewing("Infusion Brewing");
    	
    	itemMagicBottle = 
    			(ItemMagicBottle)new ItemMagicBottle()
    			.setMaxStackSize(POTION_STACK_SIZE)
    			.setCreativeTab(creativeTab).setUnlocalizedName("magicBottle");
    	GameRegistry.registerItem(itemMagicBottle, "magicBottle");
    	

        potionMaterial = new MaterialPotion();
        fluidPotions = new Fluid[POTION_COUNT];
        blockFluidPotions = new BlockFluidPotion[POTION_COUNT];
        itemPotionBuckets = new ItemPotionBucket[POTION_COUNT];
        
        itemSpawnerUpgrade = 
        		(ItemSpawnerUpgrade)new ItemSpawnerUpgrade()
        		.setMaxStackSize(1)
        		.setCreativeTab(creativeTab).setUnlocalizedName("spawnerUpgrade");
        GameRegistry.registerItem(itemSpawnerUpgrade, "spawnerUpgrade");
        
        blockSemiSolidPotion = 
        		(BlockSemiSolidPotion)new BlockSemiSolidPotion()
        		.setCreativeTab(creativeTab)
        		.setBlockName("semiSolidPotion");
        GameRegistry.registerBlock(blockSemiSolidPotion, "semiSolidPotion");
          	
        for(int i = 0; i < POTION_COUNT; i++){
        	fluidPotions[i] = new Fluid("potion" + i).setDensity(800).setViscosity(1000);
        	FluidRegistry.registerFluid(fluidPotions[i]);
        	
        	blockFluidPotions[i] =
        			(BlockFluidPotion)new BlockFluidPotion(fluidPotions[i], Material.water)
        			.setPotionEffect(i + 1)
        			.setExplode(ENABLE_REACT_EXPLOSION)
        			.setExplosionPower(REACT_EXPLOSION_POWER)
        			.setSpawn(ENABLE_REACT_SPAWN)
        			.setInfinity(ENABLE_INFINITY_SOURCE)
        			.setBlockName("fluidPotion" + i);
        	GameRegistry.registerBlock(blockFluidPotions[i], "fluidPotion" + i);
        	
        	itemPotionBuckets[i] =
        			(ItemPotionBucket)new ItemPotionBucket(blockFluidPotions[i])
        			.setCreativeTab(creativeTab)
        			.setUnlocalizedName("potionBucket" + i)
        			.setContainerItem(Items.bucket);
        	GameRegistry.registerItem(itemPotionBuckets[i], "potionBucket" + i);
        	
        	FillBucketHandler.INSTANCE.buckets.put(blockFluidPotions[i], itemPotionBuckets[i]);
        	FluidContainerRegistry.registerFluidContainer(fluidPotions[i], new ItemStack(itemPotionBuckets[i]), FluidContainerRegistry.EMPTY_BUCKET);
        }
    	itemMagicBucket =
    			(ItemMagicBucket)new ItemMagicBucket(Blocks.air)
    			.setCreativeTab(creativeTab)
    			.setMaxStackSize(1)
    			.setUnlocalizedName("magicBucket");
        GameRegistry.registerItem(itemMagicBucket, "magicBucket");
   
        itemPotionFocus = 
        		(ItemPotionFocus)new ItemPotionFocus()
        		.setCreativeTab(creativeTab)
        		.setUnlocalizedName("potionFocus");
        GameRegistry.registerItem(itemPotionFocus, "potionFocus");
        
        itemPotionBaubles = new ItemPotionBaubles[3];
        
        itemPotionBaubles[0] =
    	        (ItemPotionBaubles)new ItemPotionBaubles("potionInfusedAmulet", BaubleType.AMULET)
        		.setCost(new AspectList().add(Aspect.AIR, 100).add(Aspect.ORDER, 50))
    			.setCreativeTab(creativeTab);
        GameRegistry.registerItem(itemPotionBaubles[0], "potionInfusedAmulet");
        
        itemPotionBaubles[1] =
        		(ItemPotionBaubles)new ItemPotionBaubles("potionInfusedRing", BaubleType.RING)
        		.setCost(new AspectList().add(Aspect.WATER, 100).add(Aspect.ORDER, 50))
        		.setCreativeTab(creativeTab);
        GameRegistry.registerItem(itemPotionBaubles[1], "potionInfusedRing");
            
        itemPotionBaubles[2] =
    	        (ItemPotionBaubles)new ItemPotionBaubles("potionInfusedBelt", BaubleType.BELT)
        		.setCost(new AspectList().add(Aspect.EARTH, 100).add(Aspect.ORDER, 50))
        		.setCreativeTab(creativeTab);
        GameRegistry.registerItem(itemPotionBaubles[2], "potionInfusedBelt");
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	initResearch();

    	MinecraftForge.EVENT_BUS.register(FillBucketHandler.INSTANCE);
		BlockDispenser.dispenseBehaviorRegistry.putObject(itemMagicBottle, BehaviorDispenseMagicGlass.INSTANCE);
		BlockDispenser.dispenseBehaviorRegistry.putObject(itemMagicBucket, BehaviorDispenseMagicBucket.INSTANCE);
		if(!DISPENSE_MAGIC_BUCKET){
			BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, BehaviorDispenseMagicBucket.INSTANCE);
		}
		BehaviorDispencePotionBucket.DISPENSE_MAGIC_BUCKET = DISPENSE_MAGIC_BUCKET;
		for(int i = 0; i < POTION_COUNT; i++){
    		BlockDispenser.dispenseBehaviorRegistry.putObject(itemPotionBuckets[i], BehaviorDispencePotionBucket.INSTANCE);
    	}
		
		MinecraftForge.EVENT_BUS.register(new PlayerClickHandler());
    }
    
    private void initResearch(){
		ResourceLocation background = new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png");
		ResourceLocation icon = new ResourceLocation("mariri", "textures/items/magic_bottle.png");
		ResearchCategories.registerCategory(RESEARCH_CATEGORY, icon, background);
    	ResearchItem research;
    	ResearchPage[] pages;
    	InfusionRecipe iRecipe;
    	CrucibleRecipe cRecipe;
    	ShapelessArcaneRecipe aRecipe;
    	ArrayList<ItemStack> input;
    	ShapelessRecipes wRecipe;
    	
    	// Magic Item
    	
    	research = new ResearchItem(
    			RESEARCH_MAGIC_BOTTLE, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.CRYSTAL, 1).add(Aspect.MAGIC, 1),
    			0, 0, 0, new ItemStack(itemMagicBottle)
    			).setStub().setAutoUnlock().setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_MAGIC_BOTTLE + ".0");
    	research.setPages(pages);
    	cRecipe = ThaumcraftApi.addCrucibleRecipe(
    			RESEARCH_MAGIC_BOTTLE,
    			new ItemStack(itemMagicBottle),
    			new ItemStack(Items.glass_bottle),
    			new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.MAGIC, 1));
    	pages[1] = new ResearchPage(cRecipe);
    	
    	research = new ResearchItem(
    			RESEARCH_MAGIC_BUCKET, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.CRYSTAL, 1).add(Aspect.MAGIC, 1),
    			0, 2, 0, new ItemStack(itemMagicBucket)
    			).setStub().setAutoUnlock().setRound().registerResearchItem();
    	pages = new ResearchPage[3];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_MAGIC_BUCKET + ".0");
    	research.setPages(pages);
    	aRecipe = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
    			RESEARCH_MAGIC_BUCKET,
    			new ItemStack(itemMagicBucket),
    			new AspectList().add(Aspect.WATER, 1),
    			new Object[]{ new ItemStack(Items.bucket) });
    	pages[1] = new ResearchPage(aRecipe);
    	input = new ArrayList<ItemStack>();
    	input.add(new ItemStack(itemMagicBucket));
    	wRecipe = new ShapelessRecipes(new ItemStack(Items.bucket), input);
    	GameRegistry.addRecipe(wRecipe);
    	pages[2] = new ResearchPage(wRecipe);
//    	GameRegistry.addShapelessRecipe(
//    			new ItemStack(Items.bucket),
//    			new Object[]{ new ItemStack(itemMagicBucket) });
    	
    	// Fluid Potion
    	
    	research = new ResearchItem(
    			RESEARCH_FLUID_POTION, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1),
    			2, 0, 1, new ItemStack(itemPotionBuckets[0])
    			).setStub().setParents(new String[]{"INFUSION", RESEARCH_MAGIC_BOTTLE}).setRound().registerResearchItem();
    	pages = new ResearchPage[POTION_COUNT + 1];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_FLUID_POTION + ".0");
    	research.setPages(pages);
        for(int i = 0; i < POTION_COUNT; i++){
        	iRecipe = ThaumcraftApi.addInfusionCraftingRecipe(
        			RESEARCH_FLUID_POTION, 
        			new ItemStack(itemPotionBuckets[i]),
        			5, 
        			inputAspects[i],
        			new ItemStack(Items.water_bucket),
        			inputItems[i]);
        	pages[i + 1] = new ResearchPage(iRecipe);
        }
        
        // Brewing

    	research = new ResearchItem(
    			RESEARCH_DURATION, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.MOTION, 4).add(Aspect.MAGIC, 4),
    			4, -2, 0, new ItemStack(Items.redstone)
    			).setStub().setParents(new String[]{RESEARCH_FLUID_POTION}).setSecondary().setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_DURATION + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeDuration(
    			RESEARCH_DURATION,
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][3]),
    			CustomPotionHelper.getSampleItem(1, 1, 0, false),
    			1,
    			new AspectList().add(Aspect.MOTION, 4).add(Aspect.MAGIC, 2),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][0]),
    			CustomPotionHelper.getSampleItem(1, 0, 0, false),
    			new ItemStack[]{ new ItemStack(Items.redstone) });
//    			.setMode(InfusionBrewingRecipe.MODE.DURATION);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);
    	
    	research = new ResearchItem(
    			RESEARCH_AMPLIFIER, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.ENERGY, 4).add(Aspect.MAGIC, 4),
    			4, 2, 0, new ItemStack(Items.glowstone_dust)
    			).setStub().setParents(new String[]{RESEARCH_FLUID_POTION}).setSecondary().setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_AMPLIFIER + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeAmplifier(
    			RESEARCH_AMPLIFIER,
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][1]),
    			CustomPotionHelper.getSampleItem(1, 0, 1, false),
    			1,
    			new AspectList().add(Aspect.ENERGY, 4).add(Aspect.MAGIC, 2),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][0]),
    			CustomPotionHelper.getSampleItem(1, 0, 0, false),
    			new ItemStack[]{ new ItemStack(Items.glowstone_dust) });
//    			.setMode(InfusionBrewingRecipe.MODE.AMPLIFIER);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);
    	
    	research = new ResearchItem(
    			RESEARCH_SPLASH, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.FIRE, 4).add(Aspect.MAGIC, 4),
    			4, 0, 0, new ItemStack(Items.gunpowder)
    			).setStub().setParents(new String[]{RESEARCH_FLUID_POTION}).setSecondary().setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_SPLASH + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeSplash(
    			RESEARCH_SPLASH,
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][2]),
    			CustomPotionHelper.getSampleItem(1, 0, 0, true),
    			1,
    			new AspectList().add(Aspect.FIRE, 4).add(Aspect.MAGIC, 2),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][0]),
    			CustomPotionHelper.getSampleItem(1, 0, 0, false),
    			new ItemStack[]{ new ItemStack(Items.gunpowder) });
//    			.setMode(InfusionBrewingRecipe.MODE.SPLASH);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);
    	
    	// Baubles
    	
    	research = new ResearchItem(
    			RESEARCH_AMULET, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.AIR, 1).add(Aspect.ARMOR, 1).add(Aspect.SENSES, 1),
    			2, -4, 2, new ItemStack(itemPotionBaubles[0])
    			).setStub().setParents(new String[]{RESEARCH_DURATION, "ENCHFABRIC", "TALLOW"})
    			.setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_AMULET + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeBaubles(
    			RESEARCH_AMULET,
    			new ItemStack(itemPotionBaubles[0]),
    			8,
    			new AspectList().add(Aspect.AIR, 32).add(Aspect.ARMOR, 16).add(Aspect.CRYSTAL, 16).add(Aspect.SENSES, 24),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][3]),
    			CustomPotionHelper.getSampleItem(1, CustomPotionHelper.getMaxDuration(), 0, false),
    			new ItemStack[]{
    				ItemApi.getItem("itemShard", 0), // Air Shard
    				ItemApi.getItem("itemResource", 4), // Magic Tallow
    				ItemApi.getItem("itemResource", 7), // Enchanted Fabric
    				ItemApi.getItem("itemBaubleBlanks", 0) 
    			});
//    			.setMode(InfusionBrewingRecipe.MODE.BAUBLES);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);
    
    	research = new ResearchItem(
    			RESEARCH_RING, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1).add(Aspect.MAN, 1),
    			4, -4, 2, new ItemStack(itemPotionBaubles[1])
    			).setStub().setParents(new String[]{RESEARCH_DURATION, "ENCHFABRIC", "TALLOW"})
    			.setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_RING + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeBaubles(
    			RESEARCH_RING,
    			new ItemStack(itemPotionBaubles[1]),
    			8,
    			new AspectList().add(Aspect.WATER, 32).add(Aspect.ARMOR, 16).add(Aspect.CRYSTAL, 16).add(Aspect.MAN, 24),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][3]),
    			CustomPotionHelper.getSampleItem(1, CustomPotionHelper.getMaxDuration(), 0, false),
    			new ItemStack[]{
    				ItemApi.getItem("itemShard", 2), // Water Shard
    				ItemApi.getItem("itemResource", 4), // Magic Tallow
    				ItemApi.getItem("itemResource", 7), // Enchanted Fabric
    				ItemApi.getItem("itemBaubleBlanks", 1)
    			});
//    			.setMode(InfusionBrewingRecipe.MODE.BAUBLES);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);

    	research = new ResearchItem(
    			RESEARCH_BELT, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.EARTH, 1).add(Aspect.ARMOR, 1).add(Aspect.BEAST, 1),
    			6, -4, 2, new ItemStack(itemPotionBaubles[2])
    			).setStub().setParents(new String[]{RESEARCH_DURATION, "ENCHFABRIC", "TALLOW"})
    			.setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_BELT + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeBaubles(
    			RESEARCH_BELT,
    			new ItemStack(itemPotionBaubles[2]),
    			8,
    			new AspectList().add(Aspect.EARTH, 32).add(Aspect.ARMOR, 16).add(Aspect.CRYSTAL, 16).add(Aspect.BEAST, 24),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][3]),
    			CustomPotionHelper.getSampleItem(1, CustomPotionHelper.getMaxDuration(), 0, false),
    			new ItemStack[]{
    				ItemApi.getItem("itemShard", 3), // Earth Shard
    				ItemApi.getItem("itemResource", 4), // Magic Tallow
    				ItemApi.getItem("itemResource", 7), // Enchanted Fabric
    				ItemApi.getItem("itemBaubleBlanks", 2)
    			});
//    			.setMode(InfusionBrewingRecipe.MODE.BAUBLES);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);
    	
    	// Focus
    	
    	research = new ResearchItem(
    			RESEARCH_FOCUS, RESEARCH_CATEGORY,
    			new AspectList().add(Aspect.FIRE, 1).add(Aspect.ENERGY, 1).add(Aspect.ENTROPY, 1),
    			6, -1, 2, new ItemStack(itemPotionFocus)
    			).setStub().setParents(new String[]{RESEARCH_DURATION, RESEARCH_SPLASH, "NITOR", "ALUMENTUM"})
    			.setRound().registerResearchItem();
    	pages = new ResearchPage[2];
    	pages[0] = new ResearchPage("tc.research_page." + RESEARCH_FOCUS + ".0");
    	research.setPages(pages);
    	iRecipe = new InfusionBrewingRecipeFocus(
    			RESEARCH_FOCUS,
    			new ItemStack(itemPotionFocus),
    			8,
    			new AspectList().add(Aspect.FIRE, 24).add(Aspect.MAGIC, 16).add(Aspect.ENERGY, 32).add(Aspect.ENTROPY, 24),
//    			new ItemStack(Items.potionitem, 1, CustomPotionHelper.metadataTable[0][3]),
    			CustomPotionHelper.getSampleItem(1, CustomPotionHelper.getMaxDuration(), 0, true),
    			new ItemStack[]{
	    				ItemApi.getItem("itemShard", 1), // Fire Shard
    				ItemApi.getItem("itemResource", 0), // Almentum
    				ItemApi.getItem("itemResource", 1), // Nitor
    				ItemApi.getItem("itemResource", 15), // Primal Charm
    			});
//    			.setMode(InfusionBrewingRecipe.MODE.FOCUS);
    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
    	pages[1] = new ResearchPage(iRecipe);
    	
    	// Upgrade
    	
    	pages = new ResearchPage[5];
    	research = ResearchHelper.makeResearch(RESEARCH_SPAWNER_POWER,
    			new AspectList().add(Aspect.SOUL, 1).add(Aspect.UNDEAD, 1).add(Aspect.DARKNESS, 1).add(Aspect.POISON, 1),
    			6, 1, 3, new String[]{ RESEARCH_AMPLIFIER }, false,
    			new ItemStack(itemSpawnerUpgrade, 1, 3), pages);
    	for(int i = 0; i < 4; i++){
	    	iRecipe = new InfusionBrewingRecipeUpgrade(RESEARCH_SPAWNER_POWER,
	    			new ItemStack(itemSpawnerUpgrade, 1, i), 2 + i * 3,
	    			new AspectList()
	    				.add(Aspect.SOUL, 12 * (i + 1))
	    				.add(Aspect.UNDEAD, 12 * (i + 1))
	    				.add(Aspect.DARKNESS, 16 * (i + 1))
	    				.add(Aspect.POISON, 12 * (i + 1))
	    				.add(Aspect.FIRE, 16 * (i + 1)),
	    			(i == 0) ? new ItemStack(Items.paper) : new ItemStack(itemSpawnerUpgrade, 1, i - 1),
	    			new ItemStack[]{
	    				CustomPotionHelper.getSampleItem(CustomPotionHelper.STRENGTH, 0, i, false),
	    				CustomPotionHelper.getSampleItem(CustomPotionHelper.POISON, 0, i, false),
	    				CustomPotionHelper.getSampleItem(CustomPotionHelper.DECAY, 0, i, false),
	    				(i < 2) ?	ItemApi.getItem("itemShard", 1) : // Fire Shard
	    							ItemApi.getBlock("blockCustomPlant", 3), // Cinder Pearl
	    				(i < 3) ?	ItemApi.getItem("itemResource", 1) : // Nitor
	    							new ItemStack(Blocks.quartz_ore),	
	    				(i == 0) ? new ItemStack(Items.blaze_powder) : new ItemStack(Items.magma_cream),
	    				(i < 2) ? new ItemStack(Items.redstone) : new ItemStack(Blocks.redstone_block),
	    				(i < 3) ? new ItemStack(Items.dye) : new ItemStack(Blocks.coal_ore)
	    			});
//	    			.setMode(InfusionBrewingRecipe.MODE.UPGRADE);
	    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
	    	pages[i + 1] = new ResearchPage(iRecipe);
    	}

    	
    	pages = new ResearchPage[5];
    	research = ResearchHelper.makeResearch(RESEARCH_SPAWNER_SPEED,
    			new AspectList().add(Aspect.MOTION, 1).add(Aspect.UNDEAD, 1).add(Aspect.BEAST, 1).add(Aspect.TRAVEL, 1),
    			6, 3, 3, new String[]{ RESEARCH_AMPLIFIER }, false,
    			new ItemStack(itemSpawnerUpgrade, 1, 7), pages);
    	for(int i = 0; i < 4; i++){
	    	iRecipe = new InfusionBrewingRecipeUpgrade(RESEARCH_SPAWNER_SPEED,
	    			new ItemStack(itemSpawnerUpgrade, 1, i + 4), 2 + i * 3,
	    			new AspectList()
	    				.add(Aspect.MOTION, 12 * (i + 1))
	    				.add(Aspect.UNDEAD, 8 * (i + 1))
	    				.add(Aspect.BEAST, 16 * (i + 1))
	    				.add(Aspect.TRAVEL, 12 * (i + 1))
	    				.add(Aspect.AIR, 16 * (i + 1)),
	    			(i == 0) ? new ItemStack(Items.paper) : new ItemStack(itemSpawnerUpgrade, 1, i + 3),
	    			new ItemStack[]{
	    				CustomPotionHelper.getSampleItem(CustomPotionHelper.SWIFTNESS, 0, i, false),
	    				CustomPotionHelper.getSampleItem(CustomPotionHelper.NAUSEA, 0, i, false),
	    				CustomPotionHelper.getSampleItem(CustomPotionHelper.HUNGER, 0, i, false),
	    				(i < 2) ?	ItemApi.getItem("itemShard", 0) : // Air Shard
	    							ItemApi.getBlock("blockCustomPlant", 2), // Shimmer Leaf
	    				(i < 3) ?	ItemApi.getItem("itemResource", 0) : // Almentum
    							new ItemStack(Items.nether_star),
    					(i < 1) ? new ItemStack(Items.ender_pearl) : new ItemStack(Items.ender_eye),
	    				(i < 2) ? new ItemStack(Items.glowstone_dust) : new ItemStack(Blocks.glowstone),
	    				(i < 3) ? new ItemStack(Items.emerald) : new ItemStack(Blocks.emerald_ore)
	    			});
//	    			.setMode(InfusionBrewingRecipe.MODE.UPGRADE);
	    	ThaumcraftApi.getCraftingRecipes().add(iRecipe);
	    	pages[i + 1] = new ResearchPage(iRecipe);
    	}
    }
    
    private final ItemStack[][] inputItems = new ItemStack[][]{
			new ItemStack[]{ new ItemStack(Items.sugar), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.sugar), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.gold_ingot), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.gold_ingot), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.blaze_powder), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.speckled_melon), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.speckled_melon), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.feather), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.fish, 1, 3), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.ghast_tear), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.iron_ingot), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.magma_cream), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.fish, 1, 3), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.golden_carrot), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.dye, 1, 0), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.golden_carrot), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.rotten_flesh), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.blaze_powder), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Items.poisonous_potato), new ItemStack(Items.nether_wart), new ItemStack(Items.spider_eye) },
			new ItemStack[]{ new ItemStack(Blocks.coal_block), new ItemStack(Items.nether_wart), new ItemStack(Items.fermented_spider_eye) },
			new ItemStack[]{ new ItemStack(Blocks.pumpkin), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.golden_apple), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) },
			new ItemStack[]{ new ItemStack(Items.porkchop), new ItemStack(Items.nether_wart), new ItemStack(Items.cookie) } };
    
    private final AspectList[] inputAspects = new AspectList[]{
    		new AspectList().add(Aspect.MOTION, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.MOTION, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.TOOL, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.TOOL, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.ENERGY, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.HEAL, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.HEAL, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.FLIGHT, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.TRAP, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.HEAL, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.ARMOR, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.FIRE, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.AIR, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.DARKNESS, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.DARKNESS, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.DARKNESS, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.HUNGER, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.ENERGY, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.POISON, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.VOID, 16).add(Aspect.ENTROPY, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.MAN, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.MAN, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8),
			new AspectList().add(Aspect.FLESH, 16).add(Aspect.ORDER, 12).add(Aspect.PLANT, 8) };
    
}
