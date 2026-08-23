package net.minecraft.world.level.block;

public interface Combiner<S, T> {
  T acceptDouble(S paramS1, S paramS2);
  
  T acceptSingle(S paramS);
  
  T acceptNone();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$Combiner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */