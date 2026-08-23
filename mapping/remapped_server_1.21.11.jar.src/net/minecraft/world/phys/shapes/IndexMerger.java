package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

interface IndexMerger {
  DoubleList getList();
  
  boolean forMergedIndexes(IndexConsumer paramIndexConsumer);
  
  int size();
  
  public static interface IndexConsumer {
    boolean merge(int param1Int1, int param1Int2, int param1Int3);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\IndexMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */