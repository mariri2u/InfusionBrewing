package mariri.infusionbrewing;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

public class MaterialPotion extends MaterialLiquid {
	
    public MaterialPotion()
    {
        super(MapColor.waterColor);
        this.setNoPushMobility();
    }


    public MaterialPotion(MapColor par1MapColor)
    {
        super(par1MapColor);
        this.setNoPushMobility();
    }
}
