/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SweetBerryBushBlock extends VegetationBlock implements BonemealableBlock {
/*  34 */   public static final MapCodec<SweetBerryBushBlock> CODEC = simpleCodec(SweetBerryBushBlock::new); private static final float HURT_SPEED_THRESHOLD = 0.003F;
/*     */   public static final int MAX_AGE = 3;
/*     */   
/*     */   public MapCodec<SweetBerryBushBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*     */   
/*  45 */   private static final VoxelShape SHAPE_SAPLING = Block.column(10.0D, 0.0D, 8.0D);
/*  46 */   private static final VoxelShape SHAPE_GROWING = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*     */   public SweetBerryBushBlock(BlockBehaviour.Properties paramProperties) {
/*  49 */     super(paramProperties);
/*  50 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/*  55 */     return new ItemStack((ItemLike)Items.SWEET_BERRIES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  60 */     switch (((Integer)paramBlockState.getValue((Property)AGE)).intValue()) { case 0: case 3:  }  return 
/*     */ 
/*     */       
/*  63 */       SHAPE_GROWING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  69 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  74 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  75 */     if (i < 3 && paramRandomSource.nextInt(5) == 0 && paramServerLevel.getRawBrightness(paramBlockPos.above(), 0) >= 9) {
/*  76 */       BlockState blockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1));
/*  77 */       paramServerLevel.setBlock(paramBlockPos, blockState, 2);
/*  78 */       paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(blockState));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  84 */     if (!(paramEntity instanceof net.minecraft.world.entity.LivingEntity) || paramEntity.getType() == EntityType.FOX || paramEntity.getType() == EntityType.BEE) {
/*     */       return;
/*     */     }
/*  87 */     paramEntity.makeStuckInBlock(paramBlockState, new Vec3(0.800000011920929D, 0.75D, 0.800000011920929D));
/*     */     
/*  89 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (((Integer)paramBlockState.getValue((Property)AGE)).intValue() != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  96 */         Vec3 vec3 = paramEntity.isClientAuthoritative() ? paramEntity.getKnownMovement() : paramEntity.oldPosition().subtract(paramEntity.position());
/*     */         
/*  98 */         if (vec3.horizontalDistanceSqr() > 0.0D) {
/*  99 */           double d1 = Math.abs(vec3.x());
/* 100 */           double d2 = Math.abs(vec3.z());
/*     */           
/* 102 */           if (d1 >= 0.003000000026077032D || d2 >= 0.003000000026077032D)
/* 103 */             paramEntity.hurtServer(serverLevel, paramLevel.damageSources().sweetBerryBush(), 1.0F); 
/*     */         } 
/*     */         return;
/*     */       }  }
/*     */   
/*     */   }
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 110 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/* 111 */     boolean bool = (i == 3) ? true : false;
/*     */     
/* 113 */     if (!bool && paramItemStack.is(Items.BONE_MEAL)) {
/* 114 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 116 */     return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 121 */     if (((Integer)paramBlockState.getValue((Property)AGE)).intValue() > 1) {
/* 122 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 123 */         Block.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.HARVEST_SWEET_BERRY_BUSH, paramBlockState, paramLevel
/*     */ 
/*     */ 
/*     */             
/* 127 */             .getBlockEntity(paramBlockPos), (ItemStack)null, (Entity)paramPlayer, (paramServerLevel, paramItemStack) -> Block.popResource((Level)paramServerLevel, paramBlockPos, paramItemStack));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 132 */         serverLevel.playSound(null, paramBlockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + serverLevel.random.nextFloat() * 0.4F);
/* 133 */         BlockState blockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(1));
/* 134 */         serverLevel.setBlock(paramBlockPos, blockState, 2);
/* 135 */         serverLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of((Entity)paramPlayer, blockState)); }
/*     */       
/* 137 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 140 */     return super.useWithoutItem(paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramBlockHitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 145 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 150 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 155 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 160 */     int i = Math.min(3, ((Integer)paramBlockState.getValue((Property)AGE)).intValue() + 1);
/* 161 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i)), 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SweetBerryBushBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */