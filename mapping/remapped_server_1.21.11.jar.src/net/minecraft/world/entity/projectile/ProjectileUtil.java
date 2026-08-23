/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ArrowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ProjectileUtil
/*     */ {
/*     */   public static final float DEFAULT_ENTITY_HIT_RESULT_MARGIN = 0.3F;
/*     */   
/*     */   public static HitResult getHitResultOnMoveVector(Entity paramEntity, Predicate<Entity> paramPredicate) {
/*  37 */     Vec3 vec31 = paramEntity.getDeltaMovement();
/*  38 */     Level level = paramEntity.level();
/*     */     
/*  40 */     Vec3 vec32 = paramEntity.position();
/*  41 */     return getHitResult(vec32, paramEntity, paramPredicate, vec31, level, computeMargin(paramEntity), ClipContext.Block.COLLIDER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity paramEntity, AttackRange paramAttackRange, Predicate<Entity> paramPredicate, ClipContext.Block paramBlock) {
/*  48 */     Vec3 vec31 = paramEntity.getHeadLookAngle();
/*  49 */     Vec3 vec32 = paramEntity.getEyePosition();
/*  50 */     Vec3 vec33 = vec32.add(vec31.scale(paramAttackRange.effectiveMinRange(paramEntity)));
/*  51 */     double d = paramEntity.getKnownMovement().dot(vec31);
/*  52 */     Vec3 vec34 = vec32.add(vec31.scale(paramAttackRange.effectiveMaxRange(paramEntity) + Math.max(0.0D, d)));
/*  53 */     return getHitEntitiesAlong(paramEntity, vec32, vec33, paramPredicate, vec34, paramAttackRange.hitboxMargin(), paramBlock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HitResult getHitResultOnMoveVector(Entity paramEntity, Predicate<Entity> paramPredicate, ClipContext.Block paramBlock) {
/*  60 */     Vec3 vec31 = paramEntity.getDeltaMovement();
/*  61 */     Level level = paramEntity.level();
/*     */     
/*  63 */     Vec3 vec32 = paramEntity.position();
/*  64 */     return getHitResult(vec32, paramEntity, paramPredicate, vec31, level, computeMargin(paramEntity), paramBlock);
/*     */   }
/*     */   
/*     */   public static HitResult getHitResultOnViewVector(Entity paramEntity, Predicate<Entity> paramPredicate, double paramDouble) {
/*  68 */     Vec3 vec31 = paramEntity.getViewVector(0.0F).scale(paramDouble);
/*  69 */     Level level = paramEntity.level();
/*     */     
/*  71 */     Vec3 vec32 = paramEntity.getEyePosition();
/*  72 */     return getHitResult(vec32, paramEntity, paramPredicate, vec31, level, 0.0F, ClipContext.Block.COLLIDER);
/*     */   }
/*     */   private static HitResult getHitResult(Vec3 paramVec31, Entity paramEntity, Predicate<Entity> paramPredicate, Vec3 paramVec32, Level paramLevel, float paramFloat, ClipContext.Block paramBlock) {
/*     */     EntityHitResult entityHitResult1;
/*  76 */     Vec3 vec3 = paramVec31.add(paramVec32);
/*  77 */     BlockHitResult blockHitResult = paramLevel.clipIncludingBorder(new ClipContext(paramVec31, vec3, paramBlock, ClipContext.Fluid.NONE, paramEntity));
/*     */     
/*  79 */     if (blockHitResult.getType() != HitResult.Type.MISS) {
/*  80 */       vec3 = blockHitResult.getLocation();
/*     */     }
/*  82 */     EntityHitResult entityHitResult2 = getEntityHitResult(paramLevel, paramEntity, paramVec31, vec3, paramEntity.getBoundingBox().expandTowards(paramVec32).inflate(1.0D), paramPredicate, paramFloat);
/*     */     
/*  84 */     if (entityHitResult2 != null) {
/*  85 */       entityHitResult1 = entityHitResult2;
/*     */     }
/*     */     
/*  88 */     return (HitResult)entityHitResult1;
/*     */   }
/*     */   
/*     */   private static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity paramEntity, Vec3 paramVec31, Vec3 paramVec32, Predicate<Entity> paramPredicate, Vec3 paramVec33, float paramFloat, ClipContext.Block paramBlock) {
/*  92 */     Level level = paramEntity.level();
/*     */     
/*  94 */     BlockHitResult blockHitResult = level.clipIncludingBorder(new ClipContext(paramVec31, paramVec33, paramBlock, ClipContext.Fluid.NONE, paramEntity));
/*     */     
/*  96 */     if (blockHitResult.getType() != HitResult.Type.MISS) {
/*  97 */       paramVec33 = blockHitResult.getLocation();
/*  98 */       if (paramVec31.distanceToSqr(paramVec33) < paramVec31.distanceToSqr(paramVec32))
/*     */       {
/* 100 */         return Either.left(blockHitResult);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 105 */     AABB aABB = AABB.ofSize(paramVec32, paramFloat, paramFloat, paramFloat).expandTowards(paramVec33.subtract(paramVec32)).inflate(1.0D);
/* 106 */     Collection<EntityHitResult> collection = getManyEntityHitResult(level, paramEntity, paramVec32, paramVec33, aABB, paramPredicate, paramFloat, paramBlock, true);
/*     */     
/* 108 */     if (!collection.isEmpty()) {
/* 109 */       return Either.right(collection);
/*     */     }
/*     */     
/* 112 */     return Either.left(blockHitResult);
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Entity paramEntity, Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB, Predicate<Entity> paramPredicate, double paramDouble) {
/* 116 */     Level level = paramEntity.level();
/* 117 */     double d = paramDouble;
/* 118 */     Entity entity = null;
/* 119 */     Vec3 vec3 = null;
/*     */     
/* 121 */     for (Entity entity1 : level.getEntities(paramEntity, paramAABB, paramPredicate)) {
/* 122 */       AABB aABB = entity1.getBoundingBox().inflate(entity1.getPickRadius());
/* 123 */       Optional<Vec3> optional = aABB.clip(paramVec31, paramVec32);
/* 124 */       if (aABB.contains(paramVec31)) {
/* 125 */         if (d >= 0.0D) {
/* 126 */           entity = entity1;
/* 127 */           vec3 = optional.orElse(paramVec31);
/* 128 */           d = 0.0D;
/*     */         }  continue;
/*     */       } 
/* 131 */       if (optional.isPresent()) {
/* 132 */         Vec3 vec31 = optional.get();
/* 133 */         double d1 = paramVec31.distanceToSqr(vec31);
/* 134 */         if (d1 < d || d == 0.0D) {
/* 135 */           if (entity1.getRootVehicle() == paramEntity.getRootVehicle()) {
/* 136 */             if (d == 0.0D) {
/* 137 */               entity = entity1;
/* 138 */               vec3 = vec31;
/*     */             }  continue;
/*     */           } 
/* 141 */           entity = entity1;
/* 142 */           vec3 = vec31;
/* 143 */           d = d1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (entity == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     return new EntityHitResult(entity, vec3);
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Level paramLevel, Projectile paramProjectile, Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB, Predicate<Entity> paramPredicate) {
/* 157 */     return getEntityHitResult(paramLevel, paramProjectile, paramVec31, paramVec32, paramAABB, paramPredicate, computeMargin(paramProjectile));
/*     */   }
/*     */   
/*     */   public static float computeMargin(Entity paramEntity) {
/* 161 */     return Math.max(0.0F, Math.min(0.3F, (paramEntity.tickCount - 2) / 20.0F));
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Level paramLevel, Entity paramEntity, Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB, Predicate<Entity> paramPredicate, float paramFloat) {
/* 165 */     double d = Double.MAX_VALUE;
/* 166 */     Optional<?> optional = Optional.empty();
/* 167 */     Entity entity = null;
/*     */     
/* 169 */     for (Entity entity1 : paramLevel.getEntities(paramEntity, paramAABB, paramPredicate)) {
/* 170 */       AABB aABB = entity1.getBoundingBox().inflate(paramFloat);
/* 171 */       Optional<Vec3> optional1 = aABB.clip(paramVec31, paramVec32);
/* 172 */       if (optional1.isPresent()) {
/* 173 */         double d1 = paramVec31.distanceToSqr(optional1.get());
/* 174 */         if (d1 < d) {
/* 175 */           entity = entity1;
/* 176 */           d = d1;
/* 177 */           optional = optional1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     if (entity == null) {
/* 183 */       return null;
/*     */     }
/* 185 */     return new EntityHitResult(entity, (Vec3)optional.get());
/*     */   }
/*     */   
/*     */   public static Collection<EntityHitResult> getManyEntityHitResult(Level paramLevel, Entity paramEntity, Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB, Predicate<Entity> paramPredicate, boolean paramBoolean) {
/* 189 */     return getManyEntityHitResult(paramLevel, paramEntity, paramVec31, paramVec32, paramAABB, paramPredicate, computeMargin(paramEntity), ClipContext.Block.COLLIDER, paramBoolean);
/*     */   }
/*     */   
/*     */   public static Collection<EntityHitResult> getManyEntityHitResult(Level paramLevel, Entity paramEntity, Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB, Predicate<Entity> paramPredicate, float paramFloat, ClipContext.Block paramBlock, boolean paramBoolean) {
/* 193 */     ArrayList<EntityHitResult> arrayList = new ArrayList();
/*     */     
/* 195 */     for (Entity entity : paramLevel.getEntities(paramEntity, paramAABB, paramPredicate)) {
/* 196 */       AABB aABB = entity.getBoundingBox();
/*     */ 
/*     */       
/* 199 */       if (paramBoolean && aABB.contains(paramVec31)) {
/* 200 */         arrayList.add(new EntityHitResult(entity, paramVec31));
/*     */         continue;
/*     */       } 
/* 203 */       Optional<Vec3> optional1 = aABB.clip(paramVec31, paramVec32);
/* 204 */       if (optional1.isPresent()) {
/* 205 */         arrayList.add(new EntityHitResult(entity, optional1.get()));
/*     */         continue;
/*     */       } 
/* 208 */       if (paramFloat <= 0.0D) {
/*     */         continue;
/*     */       }
/* 211 */       Optional<Vec3> optional2 = aABB.inflate(paramFloat).clip(paramVec31, paramVec32);
/* 212 */       if (optional2.isEmpty()) {
/*     */         continue;
/*     */       }
/* 215 */       Vec3 vec31 = optional2.get();
/* 216 */       Vec3 vec32 = aABB.getCenter();
/* 217 */       BlockHitResult blockHitResult = paramLevel.clipIncludingBorder(new ClipContext(vec31, vec32, paramBlock, ClipContext.Fluid.NONE, paramEntity));
/*     */       
/* 219 */       if (blockHitResult.getType() != HitResult.Type.MISS) {
/* 220 */         vec32 = blockHitResult.getLocation();
/*     */       }
/* 222 */       Optional<Vec3> optional3 = entity.getBoundingBox().clip(vec31, vec32);
/* 223 */       if (optional3.isPresent()) {
/* 224 */         arrayList.add(new EntityHitResult(entity, optional3.get()));
/*     */       }
/*     */     } 
/*     */     
/* 228 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rotateTowardsMovement(Entity paramEntity, float paramFloat) {
/* 235 */     Vec3 vec3 = paramEntity.getDeltaMovement();
/*     */     
/* 237 */     if (vec3.lengthSqr() == 0.0D) {
/*     */       return;
/*     */     }
/*     */     
/* 241 */     double d = vec3.horizontalDistance();
/* 242 */     paramEntity.setYRot((float)(Mth.atan2(vec3.z, vec3.x) * 57.2957763671875D) + 90.0F);
/* 243 */     paramEntity.setXRot((float)(Mth.atan2(d, vec3.y) * 57.2957763671875D) - 90.0F);
/*     */     
/* 245 */     while (paramEntity.getXRot() - paramEntity.xRotO < -180.0F) {
/* 246 */       paramEntity.xRotO -= 360.0F;
/*     */     }
/* 248 */     while (paramEntity.getXRot() - paramEntity.xRotO >= 180.0F) {
/* 249 */       paramEntity.xRotO += 360.0F;
/*     */     }
/*     */     
/* 252 */     while (paramEntity.getYRot() - paramEntity.yRotO < -180.0F) {
/* 253 */       paramEntity.yRotO -= 360.0F;
/*     */     }
/* 255 */     while (paramEntity.getYRot() - paramEntity.yRotO >= 180.0F) {
/* 256 */       paramEntity.yRotO += 360.0F;
/*     */     }
/*     */     
/* 259 */     paramEntity.setXRot(Mth.lerp(paramFloat, paramEntity.xRotO, paramEntity.getXRot()));
/* 260 */     paramEntity.setYRot(Mth.lerp(paramFloat, paramEntity.yRotO, paramEntity.getYRot()));
/*     */   }
/*     */   
/*     */   public static InteractionHand getWeaponHoldingHand(LivingEntity paramLivingEntity, Item paramItem) {
/* 264 */     return paramLivingEntity.getMainHandItem().is(paramItem) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
/*     */   }
/*     */   
/*     */   public static AbstractArrow getMobArrow(LivingEntity paramLivingEntity, ItemStack paramItemStack1, float paramFloat, ItemStack paramItemStack2) {
/* 268 */     ArrowItem arrowItem = (paramItemStack1.getItem() instanceof ArrowItem) ? (ArrowItem)paramItemStack1.getItem() : (ArrowItem)Items.ARROW;
/* 269 */     AbstractArrow abstractArrow = arrowItem.createArrow(paramLivingEntity.level(), paramItemStack1, paramLivingEntity, paramItemStack2);
/* 270 */     abstractArrow.setBaseDamageFromMob(paramFloat);
/*     */     
/* 272 */     return abstractArrow;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\ProjectileUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */