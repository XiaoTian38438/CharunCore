/*    */ package net.minecraft.world.item;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.decoration.ArmorStand;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ArmorStandItem extends Item {
/*    */   public ArmorStandItem(Item.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 29 */     Direction direction = paramUseOnContext.getClickedFace();
/* 30 */     if (direction == Direction.DOWN) {
/* 31 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 34 */     Level level = paramUseOnContext.getLevel();
/* 35 */     BlockPlaceContext blockPlaceContext = new BlockPlaceContext(paramUseOnContext);
/* 36 */     BlockPos blockPos = blockPlaceContext.getClickedPos();
/*    */     
/* 38 */     ItemStack itemStack = paramUseOnContext.getItemInHand();
/* 39 */     Vec3 vec3 = Vec3.atBottomCenterOf((Vec3i)blockPos);
/* 40 */     AABB aABB = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
/*    */     
/* 42 */     if (!level.noCollision(null, aABB) || !level.getEntities(null, aABB).isEmpty()) {
/* 43 */       return (InteractionResult)InteractionResult.FAIL;
/*    */     }
/*    */     
/* 46 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 47 */       Consumer consumer = EntityType.createDefaultStackConfig((Level)serverLevel, itemStack, (LivingEntity)paramUseOnContext.getPlayer());
/* 48 */       ArmorStand armorStand = (ArmorStand)EntityType.ARMOR_STAND.create(serverLevel, consumer, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
/*    */       
/* 50 */       if (armorStand == null) {
/* 51 */         return (InteractionResult)InteractionResult.FAIL;
/*    */       }
/*    */       
/* 54 */       float f = Mth.floor((Mth.wrapDegrees(paramUseOnContext.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
/* 55 */       armorStand.snapTo(armorStand.getX(), armorStand.getY(), armorStand.getZ(), f, 0.0F);
/*    */       
/* 57 */       serverLevel.addFreshEntityWithPassengers((Entity)armorStand);
/*    */       
/* 59 */       level.playSound(null, armorStand.getX(), armorStand.getY(), armorStand.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
/* 60 */       armorStand.gameEvent((Holder)GameEvent.ENTITY_PLACE, (Entity)paramUseOnContext.getPlayer()); }
/*    */ 
/*    */     
/* 63 */     itemStack.shrink(1);
/* 64 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ArmorStandItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */