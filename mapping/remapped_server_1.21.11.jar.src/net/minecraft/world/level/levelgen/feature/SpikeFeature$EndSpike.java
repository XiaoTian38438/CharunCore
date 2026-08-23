/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EndSpike
/*     */ {
/*     */   public static final Codec<EndSpike> CODEC;
/*     */   private final int centerX;
/*     */   private final int centerZ;
/*     */   private final int radius;
/*     */   private final int height;
/*     */   private final boolean guarded;
/*     */   private final AABB topBoundingBox;
/*     */   
/*     */   static {
/* 121 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("centerX").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("centerZ").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("radius").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("height").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.BOOL.fieldOf("guarded").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)paramInstance, EndSpike::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EndSpike(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 137 */     this.centerX = paramInt1;
/* 138 */     this.centerZ = paramInt2;
/* 139 */     this.radius = paramInt3;
/* 140 */     this.height = paramInt4;
/* 141 */     this.guarded = paramBoolean;
/*     */     
/* 143 */     this.topBoundingBox = new AABB((paramInt1 - paramInt3), DimensionType.MIN_Y, (paramInt2 - paramInt3), (paramInt1 + paramInt3), DimensionType.MAX_Y, (paramInt2 + paramInt3));
/*     */   }
/*     */   
/*     */   public boolean isCenterWithinChunk(BlockPos paramBlockPos) {
/* 147 */     return (SectionPos.blockToSectionCoord(paramBlockPos.getX()) == SectionPos.blockToSectionCoord(this.centerX) && 
/* 148 */       SectionPos.blockToSectionCoord(paramBlockPos.getZ()) == SectionPos.blockToSectionCoord(this.centerZ));
/*     */   }
/*     */   
/*     */   public int getCenterX() {
/* 152 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public int getCenterZ() {
/* 156 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public int getRadius() {
/* 160 */     return this.radius;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 164 */     return this.height;
/*     */   }
/*     */   
/*     */   public boolean isGuarded() {
/* 168 */     return this.guarded;
/*     */   }
/*     */   
/*     */   public AABB getTopBoundingBox() {
/* 172 */     return this.topBoundingBox;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SpikeFeature$EndSpike.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */