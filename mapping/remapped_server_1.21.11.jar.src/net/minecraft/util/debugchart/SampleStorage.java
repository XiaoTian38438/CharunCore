package net.minecraft.util.debugchart;

public interface SampleStorage {
  int capacity();
  
  int size();
  
  long get(int paramInt);
  
  long get(int paramInt1, int paramInt2);
  
  void reset();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debugchart\SampleStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */