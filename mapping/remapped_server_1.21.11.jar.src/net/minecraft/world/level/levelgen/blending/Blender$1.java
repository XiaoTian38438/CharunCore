/*    */ package net.minecraft.world.level.levelgen.blending;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.biome.BiomeResolver;
/*    */ import net.minecraft.world.level.levelgen.DensityFunction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Blender
/*    */ {
/*    */   null(Long2ObjectOpenHashMap<BlendingData> paramLong2ObjectOpenHashMap1, Long2ObjectOpenHashMap<BlendingData> paramLong2ObjectOpenHashMap2) {
/* 39 */     super(paramLong2ObjectOpenHashMap1, paramLong2ObjectOpenHashMap2);
/*    */   }
/*    */   public Blender.BlendingOutput blendOffsetAndFactor(int paramInt1, int paramInt2) {
/* 42 */     return new Blender.BlendingOutput(1.0D, 0.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public double blendDensity(DensityFunction.FunctionContext paramFunctionContext, double paramDouble) {
/* 47 */     return paramDouble;
/*    */   }
/*    */ 
/*    */   
/*    */   public BiomeResolver getBiomeResolver(BiomeResolver paramBiomeResolver) {
/* 52 */     return paramBiomeResolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blending\Blender$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */