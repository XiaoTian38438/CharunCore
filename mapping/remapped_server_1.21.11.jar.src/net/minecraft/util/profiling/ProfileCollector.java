package net.minecraft.util.profiling;

import java.util.Set;
import net.minecraft.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;

public interface ProfileCollector extends ProfilerFiller {
  ProfileResults getResults();
  
  ActiveProfiler.PathEntry getEntry(String paramString);
  
  Set<Pair<String, MetricCategory>> getChartedPaths();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ProfileCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */