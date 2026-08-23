package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

public interface ProfilerPathEntry {
  long getDuration();
  
  long getMaxDuration();
  
  long getCount();
  
  Object2LongMap<String> getCounters();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ProfilerPathEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */