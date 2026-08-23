/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FireworkRocketItem extends Item implements ProjectileItem {
/* 22 */   public static final byte[] CRAFTABLE_DURATIONS = new byte[] { 1, 2, 3 };
/*    */   
/*    */   public static final double ROCKET_PLACEMENT_OFFSET = 0.15D;
/*    */   
/*    */   public FireworkRocketItem(Item.Properties paramProperties) {
/* 27 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 32 */     Level level = paramUseOnContext.getLevel();
/*    */ 
/*    */     
/* 35 */     Player player = paramUseOnContext.getPlayer();
/* 36 */     if (player != null && player.isFallFlying()) {
/* 37 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/*    */     
/* 40 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 41 */       ItemStack itemStack = paramUseOnContext.getItemInHand();
/*    */       
/* 43 */       Vec3 vec3 = paramUseOnContext.getClickLocation();
/* 44 */       Direction direction = paramUseOnContext.getClickedFace();
/*    */       
/* 46 */       Projectile.spawnProjectile((Projectile)new FireworkRocketEntity(level, (Entity)paramUseOnContext
/*    */             
/* 48 */             .getPlayer(), vec3.x + direction
/* 49 */             .getStepX() * 0.15D, vec3.y + direction
/* 50 */             .getStepY() * 0.15D, vec3.z + direction
/* 51 */             .getStepZ() * 0.15D, itemStack), serverLevel, itemStack);
/*    */ 
/*    */ 
/*    */       
/* 55 */       itemStack.shrink(1); }
/*    */     
/* 57 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 62 */     if (paramPlayer.isFallFlying()) {
/* 63 */       ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 64 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 65 */         if (paramPlayer.dropAllLeashConnections(null)) {
/* 66 */           paramLevel.playSound(null, (Entity)paramPlayer, SoundEvents.LEAD_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
/*    */         }
/* 68 */         Projectile.spawnProjectile((Projectile)new FireworkRocketEntity(paramLevel, itemStack, (LivingEntity)paramPlayer), serverLevel, itemStack);
/* 69 */         itemStack.consume(1, (LivingEntity)paramPlayer);
/* 70 */         paramPlayer.awardStat(Stats.ITEM_USED.get(this)); }
/*    */ 
/*    */       
/* 73 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 75 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 81 */     return (Projectile)new FireworkRocketEntity(paramLevel, paramItemStack.copyWithCount(1), paramPosition.x(), paramPosition.y(), paramPosition.z(), true);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 86 */     return ProjectileItem.DispenseConfig.builder()
/* 87 */       .positionFunction(FireworkRocketItem::getEntityJustOutsideOfBlockPos)
/* 88 */       .uncertainty(1.0F)
/* 89 */       .power(0.5F)
/* 90 */       .overrideDispenseEvent(1004)
/* 91 */       .build();
/*    */   }
/*    */   
/*    */   private static Vec3 getEntityJustOutsideOfBlockPos(BlockSource paramBlockSource, Direction paramDirection) {
/* 95 */     return paramBlockSource.center().add(paramDirection
/* 96 */         .getStepX() * 0.5000099999997474D, paramDirection
/* 97 */         .getStepY() * 0.5000099999997474D, paramDirection
/* 98 */         .getStepZ() * 0.5000099999997474D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\FireworkRocketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */