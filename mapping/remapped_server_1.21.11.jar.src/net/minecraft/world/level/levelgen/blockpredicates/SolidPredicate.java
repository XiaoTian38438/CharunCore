/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ @Deprecated
/*    */ public class SolidPredicate extends StateTestingPredicate {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> stateTestingCodec(paramInstance).apply((Applicative)paramInstance, SolidPredicate::new));
/*    */   } public static final MapCodec<SolidPredicate> CODEC;
/*    */   public SolidPredicate(Vec3i paramVec3i) {
/* 13 */     super(paramVec3i);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState paramBlockState) {
/* 18 */     return paramBlockState.isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 23 */     return BlockPredicateType.SOLID;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\SolidPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */