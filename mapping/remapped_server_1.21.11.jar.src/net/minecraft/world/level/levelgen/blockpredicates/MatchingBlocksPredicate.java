/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class MatchingBlocksPredicate extends StateTestingPredicate {
/*    */   private final HolderSet<Block> blocks;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> stateTestingCodec(paramInstance).and((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(())).apply((Applicative)paramInstance, MatchingBlocksPredicate::new));
/*    */   }
/*    */   public static final MapCodec<MatchingBlocksPredicate> CODEC;
/*    */   
/*    */   public MatchingBlocksPredicate(Vec3i paramVec3i, HolderSet<Block> paramHolderSet) {
/* 20 */     super(paramVec3i);
/* 21 */     this.blocks = paramHolderSet;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState paramBlockState) {
/* 26 */     return paramBlockState.is(this.blocks);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.MATCHING_BLOCKS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBlocksPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */