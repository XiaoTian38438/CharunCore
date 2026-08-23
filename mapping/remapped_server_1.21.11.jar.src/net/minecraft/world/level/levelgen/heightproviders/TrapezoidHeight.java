/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class TrapezoidHeight extends HeightProvider {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.INT.optionalFieldOf("plateau", Integer.valueOf(0)).forGetter(())).apply((Applicative)paramInstance, TrapezoidHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<TrapezoidHeight> CODEC;
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int plateau;
/*    */   
/*    */   private TrapezoidHeight(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2, int paramInt) {
/* 27 */     this.minInclusive = paramVerticalAnchor1;
/* 28 */     this.maxInclusive = paramVerticalAnchor2;
/* 29 */     this.plateau = paramInt;
/*    */   }
/*    */   
/*    */   public static TrapezoidHeight of(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2, int paramInt) {
/* 33 */     return new TrapezoidHeight(paramVerticalAnchor1, paramVerticalAnchor2, paramInt);
/*    */   }
/*    */   
/*    */   public static TrapezoidHeight of(VerticalAnchor paramVerticalAnchor1, VerticalAnchor paramVerticalAnchor2) {
/* 37 */     return of(paramVerticalAnchor1, paramVerticalAnchor2, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext) {
/* 42 */     int i = this.minInclusive.resolveY(paramWorldGenerationContext);
/* 43 */     int j = this.maxInclusive.resolveY(paramWorldGenerationContext);
/* 44 */     if (i > j) {
/* 45 */       LOGGER.warn("Empty height range: {}", this);
/* 46 */       return i;
/*    */     } 
/*    */     
/* 49 */     int k = j - i;
/* 50 */     if (this.plateau >= k) {
/* 51 */       return Mth.randomBetweenInclusive(paramRandomSource, i, j);
/*    */     }
/*    */     
/* 54 */     int m = (k - this.plateau) / 2;
/* 55 */     int n = k - m;
/*    */     
/* 57 */     return i + Mth.randomBetweenInclusive(paramRandomSource, 0, n) + Mth.randomBetweenInclusive(paramRandomSource, 0, m);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 62 */     return HeightProviderType.TRAPEZOID;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return (this.plateau == 0) ? ("triangle (" + 
/* 68 */       String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + ")") : ("trapezoid(" + 
/*    */       
/* 70 */       this.plateau + ") in [" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\heightproviders\TrapezoidHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */