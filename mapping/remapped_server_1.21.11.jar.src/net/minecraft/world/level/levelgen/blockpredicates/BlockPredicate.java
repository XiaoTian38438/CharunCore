/*     */ package net.minecraft.world.level.levelgen.blockpredicates;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public interface BlockPredicate
/*     */   extends BiPredicate<WorldGenLevel, BlockPos> {
/*  21 */   public static final Codec<BlockPredicate> CODEC = BuiltInRegistries.BLOCK_PREDICATE_TYPE.byNameCodec().dispatch(BlockPredicate::type, BlockPredicateType::codec);
/*     */ 
/*     */ 
/*     */   
/*  25 */   public static final BlockPredicate ONLY_IN_AIR_PREDICATE = matchesBlocks(new Block[] { Blocks.AIR });
/*  26 */   public static final BlockPredicate ONLY_IN_AIR_OR_WATER_PREDICATE = matchesBlocks(new Block[] { Blocks.AIR, Blocks.WATER });
/*     */   
/*     */   BlockPredicateType<?> type();
/*     */   
/*     */   static BlockPredicate allOf(List<BlockPredicate> paramList) {
/*  31 */     return new AllOfPredicate(paramList);
/*     */   }
/*     */   
/*     */   static BlockPredicate allOf(BlockPredicate... paramVarArgs) {
/*  35 */     return allOf(List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   static BlockPredicate allOf(BlockPredicate paramBlockPredicate1, BlockPredicate paramBlockPredicate2) {
/*  39 */     return allOf(List.of(paramBlockPredicate1, paramBlockPredicate2));
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(List<BlockPredicate> paramList) {
/*  43 */     return new AnyOfPredicate(paramList);
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(BlockPredicate... paramVarArgs) {
/*  47 */     return anyOf(List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(BlockPredicate paramBlockPredicate1, BlockPredicate paramBlockPredicate2) {
/*  51 */     return anyOf(List.of(paramBlockPredicate1, paramBlockPredicate2));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Vec3i paramVec3i, List<Block> paramList) {
/*  55 */     return new MatchingBlocksPredicate(paramVec3i, (HolderSet<Block>)HolderSet.direct(Block::builtInRegistryHolder, paramList));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(List<Block> paramList) {
/*  59 */     return matchesBlocks(Vec3i.ZERO, paramList);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Vec3i paramVec3i, Block... paramVarArgs) {
/*  63 */     return matchesBlocks(paramVec3i, List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Block... paramVarArgs) {
/*  67 */     return matchesBlocks(Vec3i.ZERO, paramVarArgs);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesTag(Vec3i paramVec3i, TagKey<Block> paramTagKey) {
/*  71 */     return new MatchingBlockTagPredicate(paramVec3i, paramTagKey);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesTag(TagKey<Block> paramTagKey) {
/*  75 */     return matchesTag(Vec3i.ZERO, paramTagKey);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Vec3i paramVec3i, List<Fluid> paramList) {
/*  79 */     return new MatchingFluidsPredicate(paramVec3i, (HolderSet<Fluid>)HolderSet.direct(Fluid::builtInRegistryHolder, paramList));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Vec3i paramVec3i, Fluid... paramVarArgs) {
/*  83 */     return matchesFluids(paramVec3i, List.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Fluid... paramVarArgs) {
/*  87 */     return matchesFluids(Vec3i.ZERO, paramVarArgs);
/*     */   }
/*     */   
/*     */   static BlockPredicate not(BlockPredicate paramBlockPredicate) {
/*  91 */     return new NotPredicate(paramBlockPredicate);
/*     */   }
/*     */   
/*     */   static BlockPredicate replaceable(Vec3i paramVec3i) {
/*  95 */     return new ReplaceablePredicate(paramVec3i);
/*     */   }
/*     */   
/*     */   static BlockPredicate replaceable() {
/*  99 */     return replaceable(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate wouldSurvive(BlockState paramBlockState, Vec3i paramVec3i) {
/* 103 */     return new WouldSurvivePredicate(paramVec3i, paramBlockState);
/*     */   }
/*     */   
/*     */   static BlockPredicate hasSturdyFace(Vec3i paramVec3i, Direction paramDirection) {
/* 107 */     return new HasSturdyFacePredicate(paramVec3i, paramDirection);
/*     */   }
/*     */   
/*     */   static BlockPredicate hasSturdyFace(Direction paramDirection) {
/* 111 */     return hasSturdyFace(Vec3i.ZERO, paramDirection);
/*     */   }
/*     */   
/*     */   static BlockPredicate solid(Vec3i paramVec3i) {
/* 115 */     return new SolidPredicate(paramVec3i);
/*     */   }
/*     */   
/*     */   static BlockPredicate solid() {
/* 119 */     return solid(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate noFluid() {
/* 123 */     return noFluid(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate noFluid(Vec3i paramVec3i) {
/* 127 */     return matchesFluids(paramVec3i, new Fluid[] { Fluids.EMPTY });
/*     */   }
/*     */   
/*     */   static BlockPredicate insideWorld(Vec3i paramVec3i) {
/* 131 */     return new InsideWorldBoundsPredicate(paramVec3i);
/*     */   }
/*     */   
/*     */   static BlockPredicate alwaysTrue() {
/* 135 */     return TrueBlockPredicate.INSTANCE;
/*     */   }
/*     */   
/*     */   static BlockPredicate unobstructed(Vec3i paramVec3i) {
/* 139 */     return new UnobstructedPredicate(paramVec3i);
/*     */   }
/*     */   
/*     */   static BlockPredicate unobstructed() {
/* 143 */     return unobstructed(Vec3i.ZERO);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\BlockPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */