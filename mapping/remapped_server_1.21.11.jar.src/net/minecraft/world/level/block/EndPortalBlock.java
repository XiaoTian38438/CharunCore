/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPlatformFeature;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EndPortalBlock extends BaseEntityBlock implements Portal {
/*  34 */   public static final MapCodec<EndPortalBlock> CODEC = simpleCodec(EndPortalBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<EndPortalBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  41 */   private static final VoxelShape SHAPE = Block.column(16.0D, 6.0D, 12.0D);
/*     */   
/*     */   protected EndPortalBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  49 */     return (BlockEntity)new TheEndPortalBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  54 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/*  59 */     return paramBlockState.getShape(paramBlockGetter, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  64 */     if (paramEntity.canUsePortal(false)) {
/*  65 */       if (!paramLevel.isClientSide() && paramLevel.dimension() == Level.END && paramEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramEntity; if (!serverPlayer.seenCredits) {
/*  66 */           serverPlayer.showEndCredits(); return;
/*     */         }  }
/*  68 */        paramEntity.setAsInsidePortal(this, paramBlockPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos) {
/*     */     float f1, f2;
/*     */     Set set;
/*  75 */     LevelData.RespawnData respawnData = paramServerLevel.getRespawnData();
/*  76 */     ResourceKey resourceKey1 = paramServerLevel.dimension();
/*  77 */     boolean bool = (resourceKey1 == Level.END) ? true : false;
/*  78 */     ResourceKey resourceKey2 = bool ? respawnData.dimension() : Level.END;
/*  79 */     BlockPos blockPos = bool ? respawnData.pos() : ServerLevel.END_SPAWN_POINT;
/*  80 */     ServerLevel serverLevel = paramServerLevel.getServer().getLevel(resourceKey2);
/*  81 */     if (serverLevel == null) {
/*  82 */       return null;
/*     */     }
/*     */     
/*  85 */     Vec3 vec3 = blockPos.getBottomCenter();
/*     */ 
/*     */ 
/*     */     
/*  89 */     if (!bool) {
/*  90 */       EndPlatformFeature.createEndPlatform((ServerLevelAccessor)serverLevel, BlockPos.containing((Position)vec3).below(), true);
/*  91 */       f1 = Direction.WEST.toYRot();
/*  92 */       f2 = 0.0F;
/*  93 */       set = Relative.union(new Set[] { Relative.DELTA, Set.of(Relative.X_ROT) });
/*  94 */       if (paramEntity instanceof ServerPlayer) {
/*  95 */         vec3 = vec3.subtract(0.0D, 1.0D, 0.0D);
/*     */       }
/*     */     } else {
/*  98 */       f1 = respawnData.yaw();
/*  99 */       f2 = respawnData.pitch();
/* 100 */       set = Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION });
/* 101 */       if (paramEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramEntity;
/* 102 */         return serverPlayer.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING); }
/*     */       
/* 104 */       vec3 = paramEntity.adjustSpawnLocation(serverLevel, blockPos).getBottomCenter();
/*     */     } 
/*     */ 
/*     */     
/* 108 */     return new TeleportTransition(serverLevel, vec3, Vec3.ZERO, f1, f2, set, TeleportTransition.PLAY_PORTAL_SOUND
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 115 */         .then(TeleportTransition.PLACE_PORTAL_TICKET));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 121 */     double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/* 122 */     double d2 = paramBlockPos.getY() + 0.8D;
/* 123 */     double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*     */     
/* 125 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 130 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, Fluid paramFluid) {
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 140 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EndPortalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */