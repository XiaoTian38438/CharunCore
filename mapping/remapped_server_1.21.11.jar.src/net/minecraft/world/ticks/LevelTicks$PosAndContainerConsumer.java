package net.minecraft.world.ticks;

@FunctionalInterface
interface PosAndContainerConsumer<T> {
  void accept(long paramLong, LevelChunkTicks<T> paramLevelChunkTicks);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\LevelTicks$PosAndContainerConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */