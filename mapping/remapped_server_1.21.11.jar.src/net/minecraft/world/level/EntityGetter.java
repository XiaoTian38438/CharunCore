/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public interface EntityGetter
/*     */ {
/*     */   List<Entity> getEntities(Entity paramEntity, AABB paramAABB, Predicate<? super Entity> paramPredicate);
/*     */   
/*     */   <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate);
/*     */   
/*     */   default <T extends Entity> List<T> getEntitiesOfClass(Class<T> paramClass, AABB paramAABB, Predicate<? super T> paramPredicate) {
/*  24 */     return getEntities(EntityTypeTest.forClass(paramClass), paramAABB, paramPredicate);
/*     */   }
/*     */   
/*     */   List<? extends Player> players();
/*     */   
/*     */   default List<Entity> getEntities(Entity paramEntity, AABB paramAABB) {
/*  30 */     return getEntities(paramEntity, paramAABB, EntitySelector.NO_SPECTATORS);
/*     */   }
/*     */   
/*     */   default boolean isUnobstructed(Entity paramEntity, VoxelShape paramVoxelShape) {
/*  34 */     if (paramVoxelShape.isEmpty()) {
/*  35 */       return true;
/*     */     }
/*     */     
/*  38 */     for (Entity entity : getEntities(paramEntity, paramVoxelShape.bounds())) {
/*  39 */       if (!entity.isRemoved() && entity.blocksBuilding && (paramEntity == null || !entity.isPassengerOfSameVehicle(paramEntity)) && 
/*  40 */         Shapes.joinIsNotEmpty(paramVoxelShape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND)) {
/*  41 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   default <T extends Entity> List<T> getEntitiesOfClass(Class<T> paramClass, AABB paramAABB) {
/*  49 */     return getEntitiesOfClass(paramClass, paramAABB, EntitySelector.NO_SPECTATORS);
/*     */   }
/*     */ 
/*     */   
/*     */   default List<VoxelShape> getEntityCollisions(Entity paramEntity, AABB paramAABB) {
/*  54 */     if (paramAABB.getSize() < 1.0E-7D) {
/*  55 */       return List.of();
/*     */     }
/*     */     
/*  58 */     Objects.requireNonNull(paramEntity); Predicate<? super Entity> predicate = (paramEntity == null) ? EntitySelector.CAN_BE_COLLIDED_WITH : EntitySelector.NO_SPECTATORS.and(paramEntity::canCollideWith);
/*  59 */     List<Entity> list = getEntities(paramEntity, paramAABB.inflate(1.0E-7D), predicate);
/*     */     
/*  61 */     if (list.isEmpty()) {
/*  62 */       return List.of();
/*     */     }
/*     */     
/*  65 */     ImmutableList.Builder builder = ImmutableList.builderWithExpectedSize(list.size());
/*  66 */     for (Entity entity : list) {
/*  67 */       builder.add(Shapes.create(entity.getBoundingBox()));
/*     */     }
/*     */     
/*  70 */     return (List<VoxelShape>)builder.build();
/*     */   }
/*     */   
/*     */   default Player getNearestPlayer(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, Predicate<Entity> paramPredicate) {
/*  74 */     double d = -1.0D;
/*  75 */     Player player = null;
/*     */     
/*  77 */     for (Player player1 : players()) {
/*  78 */       if (paramPredicate != null && !paramPredicate.test(player1)) {
/*     */         continue;
/*     */       }
/*     */       
/*  82 */       double d1 = player1.distanceToSqr(paramDouble1, paramDouble2, paramDouble3);
/*  83 */       if ((paramDouble4 < 0.0D || d1 < paramDouble4 * paramDouble4) && (d == -1.0D || d1 < d)) {
/*  84 */         d = d1;
/*  85 */         player = player1;
/*     */       } 
/*     */     } 
/*  88 */     return player;
/*     */   }
/*     */   
/*     */   default Player getNearestPlayer(Entity paramEntity, double paramDouble) {
/*  92 */     return getNearestPlayer(paramEntity.getX(), paramEntity.getY(), paramEntity.getZ(), paramDouble, false);
/*     */   }
/*     */   
/*     */   default Player getNearestPlayer(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, boolean paramBoolean) {
/*  96 */     Predicate<Entity> predicate = paramBoolean ? EntitySelector.NO_CREATIVE_OR_SPECTATOR : EntitySelector.NO_SPECTATORS;
/*  97 */     return getNearestPlayer(paramDouble1, paramDouble2, paramDouble3, paramDouble4, predicate);
/*     */   }
/*     */   
/*     */   default boolean hasNearbyAlivePlayer(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 101 */     for (Player player : players()) {
/* 102 */       if (!EntitySelector.NO_SPECTATORS.test(player) || !EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player)) {
/*     */         continue;
/*     */       }
/* 105 */       double d = player.distanceToSqr(paramDouble1, paramDouble2, paramDouble3);
/* 106 */       if (paramDouble4 < 0.0D || d < paramDouble4 * paramDouble4) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   default Player getPlayerByUUID(UUID paramUUID) {
/* 114 */     for (byte b = 0; b < players().size(); b++) {
/* 115 */       Player player = players().get(b);
/* 116 */       if (paramUUID.equals(player.getUUID())) {
/* 117 */         return player;
/*     */       }
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\EntityGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */