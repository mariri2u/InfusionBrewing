use File::Copy 'copy';

for($i = 5; $i <= 23; $i++){
	copy "water_flow.png", "potion".$i."_flow.png";
	copy "water_flow.png.mcmeta", "potion".$i."_flow.png.mcmeta";
	copy "water_still.png", "potion".$i."_still.png";
	copy "water_still.png.mcmeta", "potion".$i."_still.png.mcmeta";
}