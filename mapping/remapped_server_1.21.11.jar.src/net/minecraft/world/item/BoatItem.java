/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BoatItem
/*    */   extends Item {
/*    */   public BoatItem(EntityType<? extends AbstractBoat> paramEntityType, Item.Properties paramProperties) {
/* 27 */     super(paramProperties);
/* 28 */     this.entityType = paramEntityType;
/*    */   }
/*    */   private final EntityType<? extends AbstractBoat> entityType;
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 33 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*    */     
/* 35 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(paramLevel, paramPlayer, ClipContext.Fluid.ANY);
/* 36 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 37 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/*    */ 
/*    */     
/* 41 */     Vec3 vec3 = paramPlayer.getViewVector(1.0F);
/* 42 */     double d = 5.0D;
/* 43 */     List list = paramLevel.getEntities((Entity)paramPlayer, paramPlayer.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), EntitySelector.CAN_BE_PICKED);
/* 44 */     if (!list.isEmpty()) {
/* 45 */       Vec3 vec31 = paramPlayer.getEyePosition();
/* 46 */       for (Entity entity : list) {
/* 47 */         AABB aABB = entity.getBoundingBox().inflate(entity.getPickRadius());
/* 48 */         if (aABB.contains(vec31)) {
/* 49 */           return (InteractionResult)InteractionResult.PASS;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/* 55 */       AbstractBoat abstractBoat = getBoat(paramLevel, (HitResult)blockHitResult, itemStack, paramPlayer);
/* 56 */       if (abstractBoat == null) {
/* 57 */         return (InteractionResult)InteractionResult.FAIL;
/*    */       }
/* 59 */       abstractBoat.setYRot(paramPlayer.getYRot());
/* 60 */       if (!paramLevel.noCollision((Entity)abstractBoat, abstractBoat.getBoundingBox())) {
/* 61 */         return (InteractionResult)InteractionResult.FAIL;
/*    */       }
/* 63 */       if (!paramLevel.isClientSide()) {
/* 64 */         paramLevel.addFreshEntity((Entity)abstractBoat);
/* 65 */         paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.ENTITY_PLACE, blockHitResult.getLocation());
/* 66 */         itemStack.consume(1, (LivingEntity)paramPlayer);
/*    */       } 
/* 68 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*    */       
/* 70 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 73 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   private AbstractBoat getBoat(Level paramLevel, HitResult paramHitResult, ItemStack paramItemStack, Player paramPlayer) {
/* 77 */     AbstractBoat abstractBoat = (AbstractBoat)this.entityType.create(paramLevel, EntitySpawnReason.SPAWN_ITEM_USE);
/*    */     
/* 79 */     if (abstractBoat != null) {
/* 80 */       Vec3 vec3 = paramHitResult.getLocation();
/* 81 */       abstractBoat.setInitialPos(vec3.x, vec3.y, vec3.z);
/* 82 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 83 */         EntityType.createDefaultStackConfig((Level)serverLevel, paramItemStack, (LivingEntity)paramPlayer).accept(abstractBoat); }
/*    */     
/*    */     } 
/* 86 */     return abstractBoat;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BoatItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */