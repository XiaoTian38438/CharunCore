/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EndGatewayBlock
/*     */   extends BaseEntityBlock implements Portal {
/*  29 */   public static final MapCodec<EndGatewayBlock> CODEC = simpleCodec(EndGatewayBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<EndGatewayBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */   
/*     */   protected EndGatewayBlock(BlockBehaviour.Properties paramProperties) {
/*  37 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  42 */     return (BlockEntity)new TheEndGatewayBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/*  47 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.END_GATEWAY, paramLevel.isClientSide() ? TheEndGatewayBlockEntity::beamAnimationTick : TheEndGatewayBlockEntity::portalTick);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  52 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  53 */     if (!(blockEntity instanceof TheEndGatewayBlockEntity)) {
/*     */       return;
/*     */     }
/*  56 */     int i = ((TheEndGatewayBlockEntity)blockEntity).getParticleAmount();
/*  57 */     for (byte b = 0; b < i; b++) {
/*  58 */       double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/*  59 */       double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble();
/*  60 */       double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*  61 */       double d4 = (paramRandomSource.nextDouble() - 0.5D) * 0.5D;
/*  62 */       double d5 = (paramRandomSource.nextDouble() - 0.5D) * 0.5D;
/*  63 */       double d6 = (paramRandomSource.nextDouble() - 0.5D) * 0.5D;
/*     */       
/*  65 */       int j = paramRandomSource.nextInt(2) * 2 - 1;
/*  66 */       if (paramRandomSource.nextBoolean()) {
/*  67 */         d3 = paramBlockPos.getZ() + 0.5D + 0.25D * j;
/*  68 */         d6 = (paramRandomSource.nextFloat() * 2.0F * j);
/*     */       } else {
/*  70 */         d1 = paramBlockPos.getX() + 0.5D + 0.25D * j;
/*  71 */         d4 = (paramRandomSource.nextFloat() * 2.0F * j);
/*     */       } 
/*     */       
/*  74 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.PORTAL, d1, d2, d3, d4, d5, d6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/*  80 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, Fluid paramFluid) {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  90 */     if (paramEntity.canUsePortal(false)) {
/*  91 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  92 */       if (!paramLevel.isClientSide() && blockEntity instanceof TheEndGatewayBlockEntity) { TheEndGatewayBlockEntity theEndGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity;
/*  93 */         if (!theEndGatewayBlockEntity.isCoolingDown()) {
/*  94 */           paramEntity.setAsInsidePortal(this, paramBlockPos);
/*  95 */           TheEndGatewayBlockEntity.triggerCooldown(paramLevel, paramBlockPos, paramBlockState, theEndGatewayBlockEntity);
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity, BlockPos paramBlockPos) {
/*     */     TheEndGatewayBlockEntity theEndGatewayBlockEntity;
/* 103 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos);
/* 104 */     if (blockEntity instanceof TheEndGatewayBlockEntity) { theEndGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity; }
/* 105 */     else { return null; }
/*     */ 
/*     */     
/* 108 */     Vec3 vec3 = theEndGatewayBlockEntity.getPortalPosition(paramServerLevel, paramBlockPos);
/*     */     
/* 110 */     if (vec3 == null) {
/* 111 */       return null;
/*     */     }
/*     */     
/* 114 */     if (paramEntity instanceof net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl)
/*     */     {
/*     */       
/* 117 */       return new TeleportTransition(paramServerLevel, vec3, Vec3.ZERO, 0.0F, 0.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 123 */           Set.of(), TeleportTransition.PLACE_PORTAL_TICKET);
/*     */     }
/*     */ 
/*     */     
/* 127 */     return new TeleportTransition(paramServerLevel, vec3, Vec3.ZERO, 0.0F, 0.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 133 */         Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }, ), TeleportTransition.PLACE_PORTAL_TICKET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 141 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EndGatewayBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */