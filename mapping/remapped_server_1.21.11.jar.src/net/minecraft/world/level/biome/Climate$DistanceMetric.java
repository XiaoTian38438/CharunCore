package net.minecraft.world.level.biome;

interface DistanceMetric<T> {
  long distance(Climate.RTree.Node<T> paramNode, long[] paramArrayOflong);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\Climate$DistanceMetric.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */