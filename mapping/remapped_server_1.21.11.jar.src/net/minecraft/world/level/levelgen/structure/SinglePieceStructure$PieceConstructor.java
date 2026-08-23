package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.WorldgenRandom;

@FunctionalInterface
public interface PieceConstructor {
  StructurePiece construct(WorldgenRandom paramWorldgenRandom, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\SinglePieceStructure$PieceConstructor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */