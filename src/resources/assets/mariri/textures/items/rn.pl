use File::Copy 'copy';

for($i = 5; $i <= 23; $i++){
	copy "bucket_water.png", "bucket_water".$i.".png";
}