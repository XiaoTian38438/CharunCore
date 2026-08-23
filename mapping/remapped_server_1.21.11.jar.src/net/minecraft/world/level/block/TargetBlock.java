/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TargetBlock extends Block {
/*  28 */   public static final MapCodec<TargetBlock> CODEC = simpleCodec(TargetBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<TargetBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */   
/*  35 */   private static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;
/*     */   
/*     */   private static final int ACTIVATION_TICKS_ARROWS = 20;
/*     */   private static final int ACTIVATION_TICKS_OTHER = 8;
/*     */   
/*     */   public TargetBlock(BlockBehaviour.Properties paramProperties) {
/*  41 */     super(paramProperties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)OUTPUT_POWER, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/*  47 */     int i = updateRedstoneOutput((LevelAccessor)paramLevel, paramBlockState, paramBlockHitResult, (Entity)paramProjectile);
/*     */     
/*  49 */     Entity entity = paramProjectile.getOwner();
/*  50 */     if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  51 */       serverPlayer.awardStat(Stats.TARGET_HIT);
/*  52 */       CriteriaTriggers.TARGET_BLOCK_HIT.trigger(serverPlayer, (Entity)paramProjectile, paramBlockHitResult.getLocation(), i); }
/*     */   
/*     */   }
/*     */   
/*     */   private static int updateRedstoneOutput(LevelAccessor paramLevelAccessor, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Entity paramEntity) {
/*  57 */     int i = getRedstoneStrength(paramBlockHitResult, paramBlockHitResult.getLocation());
/*  58 */     byte b = (paramEntity instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow) ? 20 : 8;
/*     */     
/*  60 */     if (!paramLevelAccessor.getBlockTicks().hasScheduledTick(paramBlockHitResult.getBlockPos(), paramBlockState.getBlock())) {
/*  61 */       setOutputPower(paramLevelAccessor, paramBlockState, i, paramBlockHitResult.getBlockPos(), b);
/*     */     }
/*     */     
/*  64 */     return i;
/*     */   }
/*     */   private static int getRedstoneStrength(BlockHitResult paramBlockHitResult, Vec3 paramVec3) {
/*     */     double d4;
/*  68 */     Direction direction = paramBlockHitResult.getDirection();
/*  69 */     double d1 = Math.abs(Mth.frac(paramVec3.x) - 0.5D);
/*  70 */     double d2 = Math.abs(Mth.frac(paramVec3.y) - 0.5D);
/*  71 */     double d3 = Math.abs(Mth.frac(paramVec3.z) - 0.5D);
/*     */ 
/*     */     
/*  74 */     Direction.Axis axis = direction.getAxis();
/*  75 */     if (axis == Direction.Axis.Y) {
/*  76 */       d4 = Math.max(d1, d3);
/*  77 */     } else if (axis == Direction.Axis.Z) {
/*  78 */       d4 = Math.max(d1, d2);
/*     */     } else {
/*  80 */       d4 = Math.max(d2, d3);
/*     */     } 
/*     */     
/*  83 */     return Math.max(1, Mth.ceil(15.0D * Mth.clamp((0.5D - d4) / 0.5D, 0.0D, 1.0D)));
/*     */   }
/*     */   
/*     */   private static void setOutputPower(LevelAccessor paramLevelAccessor, BlockState paramBlockState, int paramInt1, BlockPos paramBlockPos, int paramInt2) {
/*  87 */     paramLevelAccessor.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)OUTPUT_POWER, Integer.valueOf(paramInt1)), 3);
/*  88 */     paramLevelAccessor.scheduleTick(paramBlockPos, paramBlockState.getBlock(), paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  93 */     if (((Integer)paramBlockState.getValue((Property)OUTPUT_POWER)).intValue() != 0) {
/*  94 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)OUTPUT_POWER, Integer.valueOf(0)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 100 */     return ((Integer)paramBlockState.getValue((Property)OUTPUT_POWER)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 110 */     paramBuilder.add(new Property[] { (Property)OUTPUT_POWER });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 115 */     if (paramLevel.isClientSide() || paramBlockState1.is(paramBlockState2.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 119 */     if (((Integer)paramBlockState1.getValue((Property)OUTPUT_POWER)).intValue() > 0 && !paramLevel.getBlockTicks().hasScheduledTick(paramBlockPos, this))
/* 120 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState1.setValue((Property)OUTPUT_POWER, Integer.valueOf(0)), 18); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TargetBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */