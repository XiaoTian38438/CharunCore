package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;

abstract class FloorRoomCollection {
  public abstract String get1x1(RandomSource paramRandomSource);
  
  public abstract String get1x1Secret(RandomSource paramRandomSource);
  
  public abstract String get1x2SideEntrance(RandomSource paramRandomSource, boolean paramBoolean);
  
  public abstract String get1x2FrontEntrance(RandomSource paramRandomSource, boolean paramBoolean);
  
  public abstract String get1x2Secret(RandomSource paramRandomSource);
  
  public abstract String get2x2(RandomSource paramRandomSource);
  
  public abstract String get2x2Secret(RandomSource paramRandomSource);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$FloorRoomCollection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */