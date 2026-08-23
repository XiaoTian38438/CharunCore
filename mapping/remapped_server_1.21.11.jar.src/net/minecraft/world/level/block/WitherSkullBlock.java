/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ 
/*     */ public class WitherSkullBlock extends SkullBlock {
/*  26 */   public static final MapCodec<WitherSkullBlock> CODEC = simpleCodec(WitherSkullBlock::new); private static BlockPattern witherPatternFull;
/*     */   private static BlockPattern witherPatternBase;
/*     */   
/*     */   public MapCodec<WitherSkullBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WitherSkullBlock(BlockBehaviour.Properties paramProperties) {
/*  38 */     super(SkullBlock.Types.WITHER_SKELETON, paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  43 */     checkSpawn(paramLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static void checkSpawn(Level paramLevel, BlockPos paramBlockPos) {
/*  47 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof SkullBlockEntity) { SkullBlockEntity skullBlockEntity = (SkullBlockEntity)blockEntity;
/*  48 */       checkSpawn(paramLevel, paramBlockPos, skullBlockEntity); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void checkSpawn(Level paramLevel, BlockPos paramBlockPos, SkullBlockEntity paramSkullBlockEntity) {
/*  53 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*  56 */     BlockState blockState = paramSkullBlockEntity.getBlockState();
/*  57 */     boolean bool = (blockState.is(Blocks.WITHER_SKELETON_SKULL) || blockState.is(Blocks.WITHER_SKELETON_WALL_SKULL)) ? true : false;
/*  58 */     if (!bool || paramBlockPos.getY() < paramLevel.getMinY() || paramLevel.getDifficulty() == Difficulty.PEACEFUL) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     BlockPattern.BlockPatternMatch blockPatternMatch = getOrCreateWitherFull().find((LevelReader)paramLevel, paramBlockPos);
/*  63 */     if (blockPatternMatch == null) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     WitherBoss witherBoss = (WitherBoss)EntityType.WITHER.create(paramLevel, EntitySpawnReason.TRIGGERED);
/*  68 */     if (witherBoss != null) {
/*  69 */       CarvedPumpkinBlock.clearPatternBlocks(paramLevel, blockPatternMatch);
/*     */       
/*  71 */       BlockPos blockPos = blockPatternMatch.getBlock(1, 2, 0).getPos();
/*  72 */       witherBoss.snapTo(blockPos.getX() + 0.5D, blockPos.getY() + 0.55D, blockPos.getZ() + 0.5D, (blockPatternMatch.getForwards().getAxis() == Direction.Axis.X) ? 0.0F : 90.0F, 0.0F);
/*  73 */       witherBoss.yBodyRot = (blockPatternMatch.getForwards().getAxis() == Direction.Axis.X) ? 0.0F : 90.0F;
/*  74 */       witherBoss.makeInvulnerable();
/*     */       
/*  76 */       for (ServerPlayer serverPlayer : paramLevel.getEntitiesOfClass(ServerPlayer.class, witherBoss.getBoundingBox().inflate(50.0D))) {
/*  77 */         CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, (Entity)witherBoss);
/*     */       }
/*     */       
/*  80 */       paramLevel.addFreshEntity((Entity)witherBoss);
/*     */       
/*  82 */       CarvedPumpkinBlock.updatePatternBlocks(paramLevel, blockPatternMatch);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean canSpawnMob(Level paramLevel, BlockPos paramBlockPos, ItemStack paramItemStack) {
/*  87 */     if (paramItemStack.is(Items.WITHER_SKELETON_SKULL) && paramBlockPos.getY() >= paramLevel.getMinY() + 2 && paramLevel.getDifficulty() != Difficulty.PEACEFUL && !paramLevel.isClientSide()) {
/*  88 */       return (getOrCreateWitherBase().find((LevelReader)paramLevel, paramBlockPos) != null);
/*     */     }
/*     */     
/*  91 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockPattern getOrCreateWitherFull() {
/*  95 */     if (witherPatternFull == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       witherPatternFull = BlockPatternBuilder.start().aisle(new String[] { "^^^", "###", "~#~" }).where('#', paramBlockInWorld -> paramBlockInWorld.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or((Predicate)BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', paramBlockInWorld -> paramBlockInWorld.getState().isAir()).build();
/*     */     }
/*     */     
/* 110 */     return witherPatternFull;
/*     */   }
/*     */   
/*     */   private static BlockPattern getOrCreateWitherBase() {
/* 114 */     if (witherPatternBase == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       witherPatternBase = BlockPatternBuilder.start().aisle(new String[] { "   ", "###", "~#~" }).where('#', paramBlockInWorld -> paramBlockInWorld.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('~', paramBlockInWorld -> paramBlockInWorld.getState().isAir()).build();
/*     */     }
/*     */     
/* 126 */     return witherPatternBase;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WitherSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */