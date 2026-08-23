/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MatchingBlockTagPredicate extends StateTestingPredicate {
/*    */   final TagKey<Block> tag;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> stateTestingCodec(paramInstance).and((App)TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(())).apply((Applicative)paramInstance, MatchingBlockTagPredicate::new));
/*    */   }
/*    */   public static final MapCodec<MatchingBlockTagPredicate> CODEC;
/*    */   
/*    */   protected MatchingBlockTagPredicate(Vec3i paramVec3i, TagKey<Block> paramTagKey) {
/* 19 */     super(paramVec3i);
/* 20 */     this.tag = paramTagKey;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState paramBlockState) {
/* 25 */     return paramBlockState.is(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 30 */     return BlockPredicateType.MATCHING_BLOCK_TAG;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBlockTagPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */