/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class RedStoneOreBlock extends Block {
/*  24 */   public static final MapCodec<RedStoneOreBlock> CODEC = simpleCodec(RedStoneOreBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<RedStoneOreBlock> codec() {
/*  28 */     return CODEC;
/*     */   }
/*     */   
/*  31 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*     */   
/*     */   public RedStoneOreBlock(BlockBehaviour.Properties paramProperties) {
/*  34 */     super(paramProperties);
/*  35 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void attack(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/*  40 */     interact(paramBlockState, paramLevel, paramBlockPos);
/*  41 */     super.attack(paramBlockState, paramLevel, paramBlockPos, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity) {
/*  46 */     if (!paramEntity.isSteppingCarefully()) {
/*  47 */       interact(paramBlockState, paramLevel, paramBlockPos);
/*     */     }
/*  49 */     super.stepOn(paramLevel, paramBlockPos, paramBlockState, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  54 */     if (paramLevel.isClientSide()) {
/*  55 */       spawnParticles(paramLevel, paramBlockPos);
/*     */     } else {
/*  57 */       interact(paramBlockState, paramLevel, paramBlockPos);
/*     */     } 
/*     */ 
/*     */     
/*  61 */     if (paramItemStack.getItem() instanceof net.minecraft.world.item.BlockItem && (new BlockPlaceContext(paramPlayer, paramInteractionHand, paramItemStack, paramBlockHitResult)).canPlace()) {
/*  62 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  65 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static void interact(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  69 */     spawnParticles(paramLevel, paramBlockPos);
/*  70 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*  71 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)LIT, Boolean.valueOf(true)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  77 */     return ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  82 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*  83 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)LIT, Boolean.valueOf(false)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/*  89 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/*  90 */     if (paramBoolean) {
/*  91 */       tryDropExperience(paramServerLevel, paramBlockPos, paramItemStack, (IntProvider)UniformInt.of(1, 5));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  97 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*  98 */       spawnParticles(paramLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void spawnParticles(Level paramLevel, BlockPos paramBlockPos) {
/* 103 */     double d = 0.5625D;
/* 104 */     RandomSource randomSource = paramLevel.random;
/* 105 */     for (Direction direction : Direction.values()) {
/* 106 */       BlockPos blockPos = paramBlockPos.relative(direction);
/* 107 */       if (!paramLevel.getBlockState(blockPos).isSolidRender()) {
/*     */ 
/*     */ 
/*     */         
/* 111 */         Direction.Axis axis = direction.getAxis();
/* 112 */         double d1 = (axis == Direction.Axis.X) ? (0.5D + 0.5625D * direction.getStepX()) : randomSource.nextFloat();
/* 113 */         double d2 = (axis == Direction.Axis.Y) ? (0.5D + 0.5625D * direction.getStepY()) : randomSource.nextFloat();
/* 114 */         double d3 = (axis == Direction.Axis.Z) ? (0.5D + 0.5625D * direction.getStepZ()) : randomSource.nextFloat();
/*     */         
/* 116 */         paramLevel.addParticle((ParticleOptions)DustParticleOptions.REDSTONE, paramBlockPos.getX() + d1, paramBlockPos.getY() + d2, paramBlockPos.getZ() + d3, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 122 */     paramBuilder.add(new Property[] { (Property)LIT });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RedStoneOreBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */