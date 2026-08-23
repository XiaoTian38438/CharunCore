/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class UniformHeight extends HeightProvider {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(())).apply((Applicative)paramInstance, UniformHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<UniformHeight> CODEC;
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   
/*    */   private final VerticalAnchor maxInclusive;
/* 25 */   private final LongSet warnedFor = (LongSet)new LongOpenHashSet();
/*    */   
/*    */   private UniformHeight(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2) {
/* 28 */     this.minInclusive = paramVerticalAnchor1;
/* 29 */     this.maxInclusive = paramVerticalAnchor2;
/*    */   }
/*    */   
/*    */   public static UniformHeight of(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2) {
/* 33 */     return new UniformHeight(paramVerticalAnchor1, paramVerticalAnchor2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext) {
/* 38 */     int i = this.minInclusive.resolveY(paramWorldGenerationContext);
/* 39 */     int j = this.maxInclusive.resolveY(paramWorldGenerationContext);
/* 40 */     if (i > j) {
/* 41 */       if (this.warnedFor.add(i << 32L | j)) {
/* 42 */         LOGGER.warn("Empty height range: {}", this);
/*    */       }
/* 44 */       return i;
/*    */     } 
/*    */     
/* 47 */     return Mth.randomBetweenInclusive(paramRandomSource, i, j);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 52 */     return HeightProviderType.UNIFORM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\UniformHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */