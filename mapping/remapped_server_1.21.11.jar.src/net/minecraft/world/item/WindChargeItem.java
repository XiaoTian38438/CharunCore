/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WindChargeItem extends Item implements ProjectileItem {
/* 22 */   public static float PROJECTILE_SHOOT_POWER = 1.5F;
/*    */   
/*    */   public WindChargeItem(Item.Properties paramProperties) {
/* 25 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 30 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*    */     
/* 32 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 33 */       Projectile.spawnProjectileFromRotation((paramServerLevel, paramLivingEntity, paramItemStack) -> new WindCharge(paramPlayer, paramLevel, paramPlayer.position().x(), paramPlayer.getEyePosition().y(), paramPlayer.position().z()), serverLevel, itemStack, (LivingEntity)paramPlayer, 0.0F, PROJECTILE_SHOOT_POWER, 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.WIND_CHARGE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */     
/* 43 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 44 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/*    */     
/* 46 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Projectile asProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack, Direction paramDirection) {
/* 51 */     RandomSource randomSource = paramLevel.getRandom();
/* 52 */     double d1 = randomSource.triangle(paramDirection.getStepX(), 0.11485000000000001D);
/* 53 */     double d2 = randomSource.triangle(paramDirection.getStepY(), 0.11485000000000001D);
/* 54 */     double d3 = randomSource.triangle(paramDirection.getStepZ(), 0.11485000000000001D);
/* 55 */     Vec3 vec3 = new Vec3(d1, d2, d3);
/* 56 */     WindCharge windCharge = new WindCharge(paramLevel, paramPosition.x(), paramPosition.y(), paramPosition.z(), vec3);
/* 57 */     windCharge.setDeltaMovement(vec3);
/* 58 */     return (Projectile)windCharge;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void shoot(Projectile paramProjectile, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 68 */     return ProjectileItem.DispenseConfig.builder()
/* 69 */       .positionFunction((paramBlockSource, paramDirection) -> DispenserBlock.getDispensePosition(paramBlockSource, 1.0D, Vec3.ZERO))
/* 70 */       .uncertainty(6.6666665F)
/* 71 */       .power(1.0F)
/* 72 */       .overrideDispenseEvent(1051)
/* 73 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\WindChargeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */