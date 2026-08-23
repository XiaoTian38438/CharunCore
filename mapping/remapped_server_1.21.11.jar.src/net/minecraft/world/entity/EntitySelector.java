/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ import net.minecraft.world.scores.Team;
/*    */ 
/*    */ public final class EntitySelector
/*    */ {
/*    */   public static final Predicate<Entity> LIVING_ENTITY_STILL_ALIVE;
/*    */   public static final Predicate<Entity> ENTITY_NOT_BEING_RIDDEN;
/*    */   public static final Predicate<Entity> CONTAINER_ENTITY_SELECTOR;
/* 14 */   public static final Predicate<Entity> ENTITY_STILL_ALIVE = Entity::isAlive; public static final Predicate<Entity> NO_CREATIVE_OR_SPECTATOR; public static final Predicate<Entity> NO_SPECTATORS; public static final Predicate<Entity> CAN_BE_COLLIDED_WITH; static {
/* 15 */     LIVING_ENTITY_STILL_ALIVE = (paramEntity -> (paramEntity.isAlive() && paramEntity instanceof LivingEntity));
/* 16 */     ENTITY_NOT_BEING_RIDDEN = (paramEntity -> (paramEntity.isAlive() && !paramEntity.isVehicle() && !paramEntity.isPassenger()));
/* 17 */     CONTAINER_ENTITY_SELECTOR = (paramEntity -> (paramEntity instanceof net.minecraft.world.Container && paramEntity.isAlive()));
/* 18 */     NO_CREATIVE_OR_SPECTATOR = (paramEntity -> { if (paramEntity instanceof Player) { Player player = (Player)paramEntity; if (!paramEntity.isSpectator() && !player.isCreative()); return false; } 
/* 19 */       }); NO_SPECTATORS = (paramEntity -> !paramEntity.isSpectator());
/* 20 */     CAN_BE_COLLIDED_WITH = NO_SPECTATORS.and(paramEntity -> paramEntity.canBeCollidedWith(null));
/* 21 */   } public static final Predicate<Entity> CAN_BE_PICKED = NO_SPECTATORS.and(Entity::isPickable);
/*    */   
/*    */   public static Predicate<Entity> withinDistance(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 24 */     double d = paramDouble4 * paramDouble4;
/* 25 */     return paramEntity -> (paramEntity.distanceToSqr(paramDouble1, paramDouble2, paramDouble3) <= paramDouble4);
/*    */   }
/*    */   
/*    */   public static Predicate<Entity> pushableBy(Entity paramEntity) {
/* 29 */     PlayerTeam playerTeam = paramEntity.getTeam();
/* 30 */     Team.CollisionRule collisionRule = (playerTeam == null) ? Team.CollisionRule.ALWAYS : playerTeam.getCollisionRule();
/* 31 */     if (collisionRule == Team.CollisionRule.NEVER) {
/* 32 */       return (Predicate<Entity>)Predicates.alwaysFalse();
/*    */     }
/* 34 */     return NO_SPECTATORS.and(paramEntity2 -> {
/*    */           if (!paramEntity2.isPushable())
/*    */             return false;  if (paramEntity1.level().isClientSide())
/*    */             if (paramEntity2 instanceof Player) {
/*    */               Player player = (Player)paramEntity2; if (!player.isLocalPlayer())
/*    */                 return false; 
/*    */             } else {
/*    */               return false;
/*    */             }   PlayerTeam playerTeam = paramEntity2.getTeam();
/*    */           Team.CollisionRule collisionRule = (playerTeam == null) ? Team.CollisionRule.ALWAYS : playerTeam.getCollisionRule();
/*    */           if (collisionRule == Team.CollisionRule.NEVER)
/*    */             return false; 
/* 46 */           boolean bool = (paramTeam != null && paramTeam.isAlliedTo((Team)playerTeam)) ? true : false;
/* 47 */           return ((paramCollisionRule == Team.CollisionRule.PUSH_OWN_TEAM || collisionRule == Team.CollisionRule.PUSH_OWN_TEAM) && bool) ? false : (
/*    */ 
/*    */             
/* 50 */             !((paramCollisionRule == Team.CollisionRule.PUSH_OTHER_TEAMS || collisionRule == Team.CollisionRule.PUSH_OTHER_TEAMS) && !bool));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Predicate<Entity> notRiding(Entity paramEntity) {
/* 58 */     return paramEntity2 -> {
/*    */         while (paramEntity2.isPassenger()) {
/*    */           paramEntity2 = paramEntity2.getVehicle();
/*    */           if (paramEntity2 == paramEntity1)
/*    */             return false; 
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntitySelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */