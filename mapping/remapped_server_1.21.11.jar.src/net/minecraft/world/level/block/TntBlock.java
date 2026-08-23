/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class TntBlock extends Block {
/*  33 */   public static final MapCodec<TntBlock> CODEC = simpleCodec(TntBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<TntBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
/*     */   
/*     */   public TntBlock(BlockBehaviour.Properties paramProperties) {
/*  43 */     super(paramProperties);
/*  44 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)UNSTABLE, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  49 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/*  52 */     if (paramLevel.hasNeighborSignal(paramBlockPos) && 
/*  53 */       prime(paramLevel, paramBlockPos)) {
/*  54 */       paramLevel.removeBlock(paramBlockPos, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  61 */     if (paramLevel.hasNeighborSignal(paramBlockPos) && 
/*  62 */       prime(paramLevel, paramBlockPos)) {
/*  63 */       paramLevel.removeBlock(paramBlockPos, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/*  70 */     if (!paramLevel.isClientSide() && !(paramPlayer.getAbilities()).instabuild && ((Boolean)paramBlockState.getValue((Property)UNSTABLE)).booleanValue()) {
/*  71 */       prime(paramLevel, paramBlockPos);
/*     */     }
/*     */     
/*  74 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void wasExploded(ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion) {
/*  79 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     PrimedTnt primedTnt = new PrimedTnt((Level)paramServerLevel, paramBlockPos.getX() + 0.5D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.5D, paramExplosion.getIndirectSourceEntity());
/*  84 */     int i = primedTnt.getFuse();
/*  85 */     primedTnt.setFuse((short)(paramServerLevel.random.nextInt(i / 4) + i / 8));
/*  86 */     paramServerLevel.addFreshEntity((Entity)primedTnt);
/*     */   }
/*     */   
/*     */   public static boolean prime(Level paramLevel, BlockPos paramBlockPos) {
/*  90 */     return prime(paramLevel, paramBlockPos, (LivingEntity)null);
/*     */   }
/*     */   
/*     */   private static boolean prime(Level paramLevel, BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/*  94 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*     */ 
/*     */ 
/*     */         
/*  98 */         PrimedTnt primedTnt = new PrimedTnt(paramLevel, paramBlockPos.getX() + 0.5D, paramBlockPos.getY(), paramBlockPos.getZ() + 0.5D, paramLivingEntity);
/*  99 */         paramLevel.addFreshEntity((Entity)primedTnt);
/* 100 */         paramLevel.playSound(null, primedTnt.getX(), primedTnt.getY(), primedTnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 101 */         paramLevel.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.PRIME_FUSE, paramBlockPos);
/* 102 */         return true;
/*     */       }  }
/*     */     
/*     */     return false;
/*     */   } protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 107 */     if (!paramItemStack.is(Items.FLINT_AND_STEEL) && !paramItemStack.is(Items.FIRE_CHARGE)) {
/* 108 */       return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*     */     }
/*     */     
/* 111 */     if (prime(paramLevel, paramBlockPos, (LivingEntity)paramPlayer))
/* 112 */     { paramLevel.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 11);
/*     */       
/* 114 */       Item item = paramItemStack.getItem();
/* 115 */       if (paramItemStack.is(Items.FLINT_AND_STEEL)) {
/* 116 */         paramItemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/*     */       } else {
/* 118 */         paramItemStack.consume(1, (LivingEntity)paramPlayer);
/*     */       } 
/* 120 */       paramPlayer.awardStat(Stats.ITEM_USED.get(item)); }
/* 121 */     else if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 122 */         paramPlayer.displayClientMessage((Component)Component.translatable("block.minecraft.tnt.disabled"), true);
/* 123 */         return (InteractionResult)InteractionResult.PASS;
/*     */       }  }
/* 125 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 130 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 131 */       BlockPos blockPos = paramBlockHitResult.getBlockPos();
/* 132 */       Entity entity = paramProjectile.getOwner();
/* 133 */       if (paramProjectile.isOnFire() && paramProjectile.mayInteract(serverLevel, blockPos) && 
/* 134 */         prime(paramLevel, blockPos, (entity instanceof LivingEntity) ? (LivingEntity)entity : null)) {
/* 135 */         paramLevel.removeBlock(blockPos, false);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean dropFromExplosion(Explosion paramExplosion) {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 148 */     paramBuilder.add(new Property[] { (Property)UNSTABLE });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TntBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */