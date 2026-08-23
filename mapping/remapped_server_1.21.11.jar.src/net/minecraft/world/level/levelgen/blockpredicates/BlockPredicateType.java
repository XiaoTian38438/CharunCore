/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface BlockPredicateType<P extends BlockPredicate> {
/*  8 */   public static final BlockPredicateType<MatchingBlocksPredicate> MATCHING_BLOCKS = register("matching_blocks", MatchingBlocksPredicate.CODEC);
/*  9 */   public static final BlockPredicateType<MatchingBlockTagPredicate> MATCHING_BLOCK_TAG = register("matching_block_tag", MatchingBlockTagPredicate.CODEC);
/* 10 */   public static final BlockPredicateType<MatchingFluidsPredicate> MATCHING_FLUIDS = register("matching_fluids", MatchingFluidsPredicate.CODEC);
/* 11 */   public static final BlockPredicateType<HasSturdyFacePredicate> HAS_STURDY_FACE = register("has_sturdy_face", HasSturdyFacePredicate.CODEC);
/* 12 */   public static final BlockPredicateType<SolidPredicate> SOLID = register("solid", SolidPredicate.CODEC);
/* 13 */   public static final BlockPredicateType<ReplaceablePredicate> REPLACEABLE = register("replaceable", ReplaceablePredicate.CODEC);
/* 14 */   public static final BlockPredicateType<WouldSurvivePredicate> WOULD_SURVIVE = register("would_survive", WouldSurvivePredicate.CODEC);
/* 15 */   public static final BlockPredicateType<InsideWorldBoundsPredicate> INSIDE_WORLD_BOUNDS = register("inside_world_bounds", InsideWorldBoundsPredicate.CODEC);
/* 16 */   public static final BlockPredicateType<AnyOfPredicate> ANY_OF = register("any_of", AnyOfPredicate.CODEC);
/* 17 */   public static final BlockPredicateType<AllOfPredicate> ALL_OF = register("all_of", AllOfPredicate.CODEC);
/* 18 */   public static final BlockPredicateType<NotPredicate> NOT = register("not", NotPredicate.CODEC);
/* 19 */   public static final BlockPredicateType<TrueBlockPredicate> TRUE = register("true", TrueBlockPredicate.CODEC);
/* 20 */   public static final BlockPredicateType<UnobstructedPredicate> UNOBSTRUCTED = register("unobstructed", UnobstructedPredicate.CODEC);
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   private static <P extends BlockPredicate> BlockPredicateType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 25 */     return (BlockPredicateType<P>)Registry.register(BuiltInRegistries.BLOCK_PREDICATE_TYPE, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\BlockPredicateType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */