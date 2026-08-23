/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Strategy
/*    */ {
/*    */   @Deprecated
/*    */   public static final Strategy LEGACY_IRON_GOLEM;
/*    */   public static final Strategy ON_TOP_OF_COLLIDER;
/*    */   public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES;
/*    */   
/*    */   static {
/* 59 */     LEGACY_IRON_GOLEM = ((paramServerLevel, paramBlockPos1, paramBlockState1, paramBlockPos2, paramBlockState2) -> 
/* 60 */       (paramBlockState1.is(Blocks.COBWEB) || paramBlockState1.is(Blocks.CACTUS) || paramBlockState1.is(Blocks.GLASS_PANE) || paramBlockState1.getBlock() instanceof net.minecraft.world.level.block.StainedGlassPaneBlock || paramBlockState1.getBlock() instanceof net.minecraft.world.level.block.StainedGlassBlock || paramBlockState1.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || paramBlockState1.is(Blocks.CONDUIT) || paramBlockState1.is(Blocks.ICE) || paramBlockState1.is(Blocks.TNT) || paramBlockState1.is(Blocks.GLOWSTONE) || paramBlockState1.is(Blocks.BEACON) || paramBlockState1.is(Blocks.SEA_LANTERN) || paramBlockState1.is(Blocks.FROSTED_ICE) || paramBlockState1.is(Blocks.TINTED_GLASS) || paramBlockState1.is(Blocks.GLASS)) ? false : (
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 78 */       ((paramBlockState2.isAir() || paramBlockState2.liquid()) && (paramBlockState1.isSolid() || paramBlockState1.is(Blocks.POWDER_SNOW)))));
/*    */ 
/*    */     
/* 81 */     ON_TOP_OF_COLLIDER = ((paramServerLevel, paramBlockPos1, paramBlockState1, paramBlockPos2, paramBlockState2) -> 
/* 82 */       (paramBlockState2.getCollisionShape((BlockGetter)paramServerLevel, paramBlockPos2).isEmpty() && Block.isFaceFull(paramBlockState1.getCollisionShape((BlockGetter)paramServerLevel, paramBlockPos1), Direction.UP)));
/*    */     
/* 84 */     ON_TOP_OF_COLLIDER_NO_LEAVES = ((paramServerLevel, paramBlockPos1, paramBlockState1, paramBlockPos2, paramBlockState2) -> 
/* 85 */       (paramBlockState2.getCollisionShape((BlockGetter)paramServerLevel, paramBlockPos2).isEmpty() && !paramBlockState1.is(BlockTags.LEAVES) && Block.isFaceFull(paramBlockState1.getCollisionShape((BlockGetter)paramServerLevel, paramBlockPos1), Direction.UP)));
/*    */   }
/*    */   
/*    */   boolean canSpawnOn(ServerLevel paramServerLevel, BlockPos paramBlockPos1, BlockState paramBlockState1, BlockPos paramBlockPos2, BlockState paramBlockState2);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SpawnUtil$Strategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */