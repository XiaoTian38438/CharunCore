package net.minecraft.world.level.levelgen.structure;

public interface StructurePieceAccessor {
  void addPiece(StructurePiece paramStructurePiece);
  
  StructurePiece findCollisionPiece(BoundingBox paramBoundingBox);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\StructurePieceAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */