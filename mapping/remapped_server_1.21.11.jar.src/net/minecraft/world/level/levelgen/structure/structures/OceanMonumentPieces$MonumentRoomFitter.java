package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

interface MonumentRoomFitter {
  boolean fits(OceanMonumentPieces.RoomDefinition paramRoomDefinition);
  
  OceanMonumentPieces.OceanMonumentPiece create(Direction paramDirection, OceanMonumentPieces.RoomDefinition paramRoomDefinition, RandomSource paramRandomSource);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$MonumentRoomFitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */