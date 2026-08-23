/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public abstract class HangingEntity
/*     */   extends BlockAttachedEntity {
/*  28 */   private static final EntityDataAccessor<Direction> DATA_DIRECTION = SynchedEntityData.defineId(HangingEntity.class, EntityDataSerializers.DIRECTION);
/*  29 */   private static final Direction DEFAULT_DIRECTION = Direction.SOUTH;
/*     */   
/*     */   protected HangingEntity(EntityType<? extends HangingEntity> paramEntityType, Level paramLevel) {
/*  32 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   protected HangingEntity(EntityType<? extends HangingEntity> paramEntityType, Level paramLevel, BlockPos paramBlockPos) {
/*  36 */     this(paramEntityType, paramLevel);
/*  37 */     this.pos = paramBlockPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  42 */     paramBuilder.define(DATA_DIRECTION, DEFAULT_DIRECTION);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/*  47 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*  48 */     if (paramEntityDataAccessor.equals(DATA_DIRECTION)) {
/*  49 */       setDirection(getDirection());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getDirection() {
/*  55 */     return (Direction)this.entityData.get(DATA_DIRECTION);
/*     */   }
/*     */   
/*     */   protected void setDirectionRaw(Direction paramDirection) {
/*  59 */     this.entityData.set(DATA_DIRECTION, paramDirection);
/*     */   }
/*     */   
/*     */   protected void setDirection(Direction paramDirection) {
/*  63 */     Objects.requireNonNull(paramDirection);
/*  64 */     Validate.isTrue(paramDirection.getAxis().isHorizontal());
/*     */     
/*  66 */     setDirectionRaw(paramDirection);
/*  67 */     setYRot((paramDirection.get2DDataValue() * 90));
/*  68 */     this.yRotO = getYRot();
/*     */     
/*  70 */     recalculateBoundingBox();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  76 */     if (getDirection() == null) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     AABB aABB = calculateBoundingBox(this.pos, getDirection());
/*  81 */     Vec3 vec3 = aABB.getCenter();
/*  82 */     setPosRaw(vec3.x, vec3.y, vec3.z);
/*  83 */     setBoundingBox(aABB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean survives() {
/*  90 */     if (hasLevelCollision(getPopBox())) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     boolean bool = BlockPos.betweenClosedStream(calculateSupportBox()).allMatch(paramBlockPos -> {
/*     */           BlockState blockState = level().getBlockState(paramBlockPos);
/*     */ 
/*     */           
/*  98 */           return (blockState.isSolid() || DiodeBlock.isDiode(blockState));
/*     */         });
/*     */     
/* 101 */     return (bool && canCoexist(false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AABB calculateSupportBox() {
/* 107 */     return getBoundingBox().move(getDirection().step().mul(-0.5F)).deflate(1.0E-7D);
/*     */   }
/*     */   
/*     */   protected boolean canCoexist(boolean paramBoolean) {
/* 111 */     Predicate predicate = paramHangingEntity -> {
/* 112 */         boolean bool1 = (!paramBoolean && paramHangingEntity.getType() == getType()) ? true : false;
/*     */         boolean bool2 = (paramHangingEntity.getDirection() == getDirection()) ? true : false;
/* 114 */         return (paramHangingEntity != this && (bool1 || bool2));
/*     */       };
/* 116 */     return !level().hasEntities(EntityTypeTest.forClass(HangingEntity.class), getPopBox(), predicate);
/*     */   }
/*     */   
/*     */   protected boolean hasLevelCollision(AABB paramAABB) {
/* 120 */     Level level = level();
/* 121 */     return (!level.noBlockCollision(this, paramAABB) || !level.noBorderCollision(this, paramAABB));
/*     */   }
/*     */   
/*     */   protected AABB getPopBox() {
/* 125 */     return getBoundingBox();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemEntity spawnAtLocation(ServerLevel paramServerLevel, ItemStack paramItemStack, float paramFloat) {
/* 132 */     ItemEntity itemEntity = new ItemEntity(level(), getX() + (getDirection().getStepX() * 0.15F), getY() + paramFloat, getZ() + (getDirection().getStepZ() * 0.15F), paramItemStack);
/* 133 */     itemEntity.setDefaultPickUpDelay();
/* 134 */     level().addFreshEntity((Entity)itemEntity);
/* 135 */     return itemEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public float rotate(Rotation paramRotation) {
/* 140 */     Direction direction = getDirection();
/* 141 */     if (direction.getAxis() != Direction.Axis.Y) {
/* 142 */       switch (paramRotation) { case CLOCKWISE_180:
/* 143 */           direction = direction.getOpposite(); break;
/* 144 */         case COUNTERCLOCKWISE_90: direction = direction.getCounterClockWise(); break;
/* 145 */         case CLOCKWISE_90: direction = direction.getClockWise();
/*     */           break; }
/*     */ 
/*     */       
/* 149 */       setDirection(direction);
/*     */     } 
/*     */     
/* 152 */     float f = Mth.wrapDegrees(getYRot());
/* 153 */     switch (paramRotation) { case CLOCKWISE_180: case COUNTERCLOCKWISE_90: case CLOCKWISE_90:  }  return 
/*     */ 
/*     */ 
/*     */       
/* 157 */       f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float mirror(Mirror paramMirror) {
/* 163 */     return rotate(paramMirror.getRotation(getDirection()));
/*     */   }
/*     */   
/*     */   protected abstract AABB calculateBoundingBox(BlockPos paramBlockPos, Direction paramDirection);
/*     */   
/*     */   public abstract void playPlacementSound();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\HangingEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */