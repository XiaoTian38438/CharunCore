/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TurtleEggBlock
/*     */   extends Block {
/*  36 */   public static final MapCodec<TurtleEggBlock> CODEC = simpleCodec(TurtleEggBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<TurtleEggBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
/*  44 */   public static final IntegerProperty EGGS = BlockStateProperties.EGGS;
/*     */   
/*     */   public static final int MAX_HATCH_LEVEL = 2;
/*     */   
/*     */   public static final int MIN_EGGS = 1;
/*     */   public static final int MAX_EGGS = 4;
/*  50 */   private static final VoxelShape SHAPE_SINGLE = Block.box(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D);
/*  51 */   private static final VoxelShape SHAPE_MULTIPLE = Block.column(14.0D, 0.0D, 7.0D);
/*     */   
/*     */   public TurtleEggBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HATCH, Integer.valueOf(0))).setValue((Property)EGGS, Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity) {
/*  60 */     if (!paramEntity.isSteppingCarefully()) {
/*  61 */       destroyEgg(paramLevel, paramBlockState, paramBlockPos, paramEntity, 100);
/*     */     }
/*  63 */     super.stepOn(paramLevel, paramBlockPos, paramBlockState, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/*  68 */     if (!(paramEntity instanceof net.minecraft.world.entity.monster.zombie.Zombie)) {
/*  69 */       destroyEgg(paramLevel, paramBlockState, paramBlockPos, paramEntity, 3);
/*     */     }
/*     */     
/*  72 */     super.fallOn(paramLevel, paramBlockState, paramBlockPos, paramEntity, paramDouble);
/*     */   }
/*     */   
/*     */   private void destroyEgg(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, int paramInt) {
/*  76 */     if (paramBlockState.is(Blocks.TURTLE_EGG) && paramLevel instanceof ServerLevel) {
/*  77 */       ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  78 */       if (canDestroyEgg(serverLevel, paramEntity) && paramLevel.random
/*  79 */         .nextInt(paramInt) == 0)
/*     */       {
/*  81 */         decreaseEggs((Level)serverLevel, paramBlockPos, paramBlockState); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decreaseEggs(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  86 */     paramLevel.playSound(null, paramBlockPos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + paramLevel.random.nextFloat() * 0.2F);
/*  87 */     int i = ((Integer)paramBlockState.getValue((Property)EGGS)).intValue();
/*  88 */     if (i <= 1) {
/*     */       
/*  90 */       paramLevel.destroyBlock(paramBlockPos, false);
/*     */     } else {
/*     */       
/*  93 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)EGGS, Integer.valueOf(i - 1)), 2);
/*  94 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_DESTROY, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*  95 */       paramLevel.levelEvent(2001, paramBlockPos, Block.getId(paramBlockState));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 101 */     if (shouldUpdateHatchLevel((Level)paramServerLevel, paramBlockPos) && onSand((BlockGetter)paramServerLevel, paramBlockPos)) {
/* 102 */       int i = ((Integer)paramBlockState.getValue((Property)HATCH)).intValue();
/* 103 */       if (i < 2) {
/* 104 */         paramServerLevel.playSound(null, paramBlockPos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + paramRandomSource.nextFloat() * 0.2F);
/* 105 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)HATCH, Integer.valueOf(i + 1)), 2);
/* 106 */         paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*     */       } else {
/*     */         
/* 109 */         paramServerLevel.playSound(null, paramBlockPos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + paramRandomSource.nextFloat() * 0.2F);
/* 110 */         paramServerLevel.removeBlock(paramBlockPos, false);
/* 111 */         paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_DESTROY, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*     */         
/* 113 */         for (byte b = 0; b < ((Integer)paramBlockState.getValue((Property)EGGS)).intValue(); b++) {
/* 114 */           paramServerLevel.levelEvent(2001, paramBlockPos, Block.getId(paramBlockState));
/* 115 */           Turtle turtle = (Turtle)EntityType.TURTLE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/* 116 */           if (turtle != null) {
/* 117 */             turtle.setAge(-24000);
/* 118 */             turtle.setHomePos(paramBlockPos);
/* 119 */             turtle.snapTo(paramBlockPos.getX() + 0.3D + b * 0.2D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.3D, 0.0F, 0.0F);
/* 120 */             paramServerLevel.addFreshEntity((Entity)turtle);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean onSand(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 128 */     return isSand(paramBlockGetter, paramBlockPos.below());
/*     */   }
/*     */   
/*     */   public static boolean isSand(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 132 */     return paramBlockGetter.getBlockState(paramBlockPos).is(BlockTags.SAND);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 137 */     if (onSand((BlockGetter)paramLevel, paramBlockPos) && !paramLevel.isClientSide()) {
/* 138 */       paramLevel.levelEvent(2012, paramBlockPos, 15);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldUpdateHatchLevel(Level paramLevel, BlockPos paramBlockPos) {
/* 143 */     float f = ((Float)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, paramBlockPos)).floatValue();
/* 144 */     return (f > 0.0F && paramLevel.random.nextFloat() < f);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level paramLevel, Player paramPlayer, BlockPos paramBlockPos, BlockState paramBlockState, BlockEntity paramBlockEntity, ItemStack paramItemStack) {
/* 149 */     super.playerDestroy(paramLevel, paramPlayer, paramBlockPos, paramBlockState, paramBlockEntity, paramItemStack);
/*     */     
/* 151 */     decreaseEggs(paramLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/* 156 */     if (!paramBlockPlaceContext.isSecondaryUseActive() && paramBlockPlaceContext.getItemInHand().is(asItem()) && ((Integer)paramBlockState.getValue((Property)EGGS)).intValue() < 4) {
/* 157 */       return true;
/*     */     }
/* 159 */     return super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 164 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/* 165 */     if (blockState.is(this)) {
/* 166 */       return (BlockState)blockState.setValue((Property)EGGS, Integer.valueOf(Math.min(4, ((Integer)blockState.getValue((Property)EGGS)).intValue() + 1)));
/*     */     }
/*     */     
/* 169 */     return super.getStateForPlacement(paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 174 */     return (((Integer)paramBlockState.getValue((Property)EGGS)).intValue() == 1) ? SHAPE_SINGLE : SHAPE_MULTIPLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 179 */     paramBuilder.add(new Property[] { (Property)HATCH, (Property)EGGS });
/*     */   }
/*     */   
/*     */   private boolean canDestroyEgg(ServerLevel paramServerLevel, Entity paramEntity) {
/* 183 */     if (paramEntity instanceof Turtle || paramEntity instanceof net.minecraft.world.entity.ambient.Bat) {
/* 184 */       return false;
/*     */     }
/*     */     
/* 187 */     if (paramEntity instanceof net.minecraft.world.entity.LivingEntity) {
/* 188 */       return (paramEntity instanceof Player || ((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue());
/*     */     }
/*     */     
/* 191 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TurtleEggBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */