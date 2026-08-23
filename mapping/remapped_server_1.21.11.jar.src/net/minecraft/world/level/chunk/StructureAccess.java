package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public interface StructureAccess {
  StructureStart getStartForStructure(Structure paramStructure);
  
  void setStartForStructure(Structure paramStructure, StructureStart paramStructureStart);
  
  LongSet getReferencesForStructure(Structure paramStructure);
  
  void addReferenceForStructure(Structure paramStructure, long paramLong);
  
  Map<Structure, LongSet> getAllReferences();
  
  void setAllReferences(Map<Structure, LongSet> paramMap);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\StructureAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */