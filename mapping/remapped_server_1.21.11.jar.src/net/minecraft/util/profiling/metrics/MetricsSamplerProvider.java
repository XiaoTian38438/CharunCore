package net.minecraft.util.profiling.metrics;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.util.profiling.ProfileCollector;

public interface MetricsSamplerProvider {
  Set<MetricSampler> samplers(Supplier<ProfileCollector> paramSupplier);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\MetricsSamplerProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */