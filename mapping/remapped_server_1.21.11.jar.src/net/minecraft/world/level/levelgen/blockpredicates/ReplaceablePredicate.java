/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class ReplaceablePredicate extends StateTestingPredicate {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> stateTestingCodec(paramInstance).apply((Applicative)paramInstance, ReplaceablePredicate::new));
/*    */   } public static final MapCodec<ReplaceablePredicate> CODEC;
/*    */   public ReplaceablePredicate(Vec3i paramVec3i) {
/* 12 */     super(paramVec3i);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState paramBlockState) {
/* 17 */     return paramBlockState.canBeReplaced();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 22 */     return BlockPredicateType.REPLACEABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\ReplaceablePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */