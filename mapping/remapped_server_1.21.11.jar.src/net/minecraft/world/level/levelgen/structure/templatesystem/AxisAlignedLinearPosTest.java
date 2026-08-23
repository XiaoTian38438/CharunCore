/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class AxisAlignedLinearPosTest extends PosRuleTest {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.FLOAT.fieldOf("max_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.fieldOf("min_dist").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("max_dist").orElse(Integer.valueOf(0)).forGetter(()), (App)Direction.Axis.CODEC.fieldOf("axis").orElse(Direction.Axis.Y).forGetter(())).apply((Applicative)paramInstance, AxisAlignedLinearPosTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<AxisAlignedLinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   private final Direction.Axis axis;
/*    */   
/*    */   public AxisAlignedLinearPosTest(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, Direction.Axis paramAxis) {
/* 27 */     if (paramInt1 >= paramInt2) {
/* 28 */       throw new IllegalArgumentException("Invalid range: [" + paramInt1 + "," + paramInt2 + "]");
/*    */     }
/* 30 */     this.minChance = paramFloat1;
/* 31 */     this.maxChance = paramFloat2;
/* 32 */     this.minDist = paramInt1;
/* 33 */     this.maxDist = paramInt2;
/* 34 */     this.axis = paramAxis;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockPos paramBlockPos3, RandomSource paramRandomSource) {
/* 39 */     Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
/* 40 */     float f1 = Math.abs((paramBlockPos2.getX() - paramBlockPos3.getX()) * direction.getStepX());
/* 41 */     float f2 = Math.abs((paramBlockPos2.getY() - paramBlockPos3.getY()) * direction.getStepY());
/* 42 */     float f3 = Math.abs((paramBlockPos2.getZ() - paramBlockPos3.getZ()) * direction.getStepZ());
/* 43 */     int i = (int)(f1 + f2 + f3);
/*    */     
/* 45 */     float f4 = paramRandomSource.nextFloat();
/* 46 */     return (f4 <= Mth.clampedLerp(Mth.inverseLerp(i, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 51 */     return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AxisAlignedLinearPosTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */