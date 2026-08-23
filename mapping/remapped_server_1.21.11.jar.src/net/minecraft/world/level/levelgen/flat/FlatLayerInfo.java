/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class FlatLayerInfo {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(FlatLayerInfo::getHeight), (App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").orElse(Blocks.AIR).forGetter(())).apply((Applicative)paramInstance, FlatLayerInfo::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FlatLayerInfo> CODEC;
/*    */   private final Block block;
/*    */   private final int height;
/*    */   
/*    */   public FlatLayerInfo(int paramInt, Block paramBlock) {
/* 21 */     this.height = paramInt;
/* 22 */     this.block = paramBlock;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 26 */     return this.height;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState() {
/* 30 */     return this.block.defaultBlockState();
/*    */   }
/*    */   
/*    */   public FlatLayerInfo heightLimited(int paramInt) {
/* 34 */     if (this.height > paramInt) {
/* 35 */       return new FlatLayerInfo(paramInt, this.block);
/*    */     }
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return ((this.height != 1) ? ("" + this.height + "*") : "") + ((this.height != 1) ? ("" + this.height + "*") : "");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\flat\FlatLayerInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */