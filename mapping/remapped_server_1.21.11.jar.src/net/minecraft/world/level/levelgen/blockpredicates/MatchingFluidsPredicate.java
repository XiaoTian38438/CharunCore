/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class MatchingFluidsPredicate extends StateTestingPredicate {
/*    */   private final HolderSet<Fluid> fluids;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> stateTestingCodec(paramInstance).and((App)RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(())).apply((Applicative)paramInstance, MatchingFluidsPredicate::new));
/*    */   }
/*    */   public static final MapCodec<MatchingFluidsPredicate> CODEC;
/*    */   
/*    */   public MatchingFluidsPredicate(Vec3i paramVec3i, HolderSet<Fluid> paramHolderSet) {
/* 20 */     super(paramVec3i);
/* 21 */     this.fluids = paramHolderSet;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState paramBlockState) {
/* 26 */     return paramBlockState.getFluidState().is(this.fluids);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.MATCHING_FLUIDS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingFluidsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */