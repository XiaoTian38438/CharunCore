package net.minecraft.util.profiling.metrics.profiling;

import net.minecraft.util.profiling.ProfilerFiller;

public interface MetricsRecorder {
  void end();
  
  void cancel();
  
  void startTick();
  
  boolean isRecording();
  
  ProfilerFiller getProfiler();
  
  void endTick();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\profiling\MetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */