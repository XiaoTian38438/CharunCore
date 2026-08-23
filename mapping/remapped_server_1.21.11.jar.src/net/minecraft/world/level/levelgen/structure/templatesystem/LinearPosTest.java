/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class LinearPosTest extends PosRuleTest {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.FLOAT.fieldOf("max_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.fieldOf("min_dist").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("max_dist").orElse(Integer.valueOf(0)).forGetter(())).apply((Applicative)paramInstance, LinearPosTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<LinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   
/*    */   public LinearPosTest(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
/* 24 */     if (paramInt1 >= paramInt2) {
/* 25 */       throw new IllegalArgumentException("Invalid range: [" + paramInt1 + "," + paramInt2 + "]");
/*    */     }
/*    */     
/* 28 */     this.minChance = paramFloat1;
/* 29 */     this.maxChance = paramFloat2;
/* 30 */     this.minDist = paramInt1;
/* 31 */     this.maxDist = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockPos paramBlockPos3, RandomSource paramRandomSource) {
/* 36 */     int i = paramBlockPos2.distManhattan((Vec3i)paramBlockPos3);
/*    */     
/* 38 */     float f = paramRandomSource.nextFloat();
/* 39 */     return (f <= Mth.clampedLerp(Mth.inverseLerp(i, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 44 */     return PosRuleTestType.LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LinearPosTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */