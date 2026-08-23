/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public class EnderDragonPhase<T extends DragonPhaseInstance>
/*    */ {
/*  9 */   private static EnderDragonPhase<?>[] phases = (EnderDragonPhase<?>[])new EnderDragonPhase[0];
/* 10 */   public static final EnderDragonPhase<DragonHoldingPatternPhase> HOLDING_PATTERN = create(DragonHoldingPatternPhase.class, "HoldingPattern");
/* 11 */   public static final EnderDragonPhase<DragonStrafePlayerPhase> STRAFE_PLAYER = create(DragonStrafePlayerPhase.class, "StrafePlayer");
/* 12 */   public static final EnderDragonPhase<DragonLandingApproachPhase> LANDING_APPROACH = create(DragonLandingApproachPhase.class, "LandingApproach");
/* 13 */   public static final EnderDragonPhase<DragonLandingPhase> LANDING = create(DragonLandingPhase.class, "Landing");
/* 14 */   public static final EnderDragonPhase<DragonTakeoffPhase> TAKEOFF = create(DragonTakeoffPhase.class, "Takeoff");
/* 15 */   public static final EnderDragonPhase<DragonSittingFlamingPhase> SITTING_FLAMING = create(DragonSittingFlamingPhase.class, "SittingFlaming");
/* 16 */   public static final EnderDragonPhase<DragonSittingScanningPhase> SITTING_SCANNING = create(DragonSittingScanningPhase.class, "SittingScanning");
/* 17 */   public static final EnderDragonPhase<DragonSittingAttackingPhase> SITTING_ATTACKING = create(DragonSittingAttackingPhase.class, "SittingAttacking");
/* 18 */   public static final EnderDragonPhase<DragonChargePlayerPhase> CHARGING_PLAYER = create(DragonChargePlayerPhase.class, "ChargingPlayer");
/* 19 */   public static final EnderDragonPhase<DragonDeathPhase> DYING = create(DragonDeathPhase.class, "Dying");
/* 20 */   public static final EnderDragonPhase<DragonHoverPhase> HOVERING = create(DragonHoverPhase.class, "Hover");
/*    */   
/*    */   private final Class<? extends DragonPhaseInstance> instanceClass;
/*    */   private final int id;
/*    */   private final String name;
/*    */   
/*    */   private EnderDragonPhase(int paramInt, Class<? extends DragonPhaseInstance> paramClass, String paramString) {
/* 27 */     this.id = paramInt;
/* 28 */     this.instanceClass = paramClass;
/* 29 */     this.name = paramString;
/*    */   }
/*    */   
/*    */   public DragonPhaseInstance createInstance(EnderDragon paramEnderDragon) {
/*    */     try {
/* 34 */       Constructor<? extends DragonPhaseInstance> constructor = getConstructor();
/* 35 */       return constructor.newInstance(new Object[] { paramEnderDragon });
/* 36 */     } catch (Exception exception) {
/* 37 */       throw new Error(exception);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected Constructor<? extends DragonPhaseInstance> getConstructor() throws NoSuchMethodException {
/* 42 */     return this.instanceClass.getConstructor(new Class[] { EnderDragon.class });
/*    */   }
/*    */   
/*    */   public int getId() {
/* 46 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return this.name + " (#" + this.name + ")";
/*    */   }
/*    */   
/*    */   public static EnderDragonPhase<?> getById(int paramInt) {
/* 55 */     if (paramInt < 0 || paramInt >= phases.length) {
/* 56 */       return HOLDING_PATTERN;
/*    */     }
/* 58 */     return phases[paramInt];
/*    */   }
/*    */   
/*    */   public static int getCount() {
/* 62 */     return phases.length;
/*    */   }
/*    */   
/*    */   private static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(Class<T> paramClass, String paramString) {
/* 66 */     EnderDragonPhase<DragonPhaseInstance> enderDragonPhase = new EnderDragonPhase<>(phases.length, paramClass, paramString);
/* 67 */     phases = (EnderDragonPhase<?>[])Arrays.<EnderDragonPhase>copyOf((EnderDragonPhase[])phases, phases.length + 1);
/* 68 */     phases[enderDragonPhase.getId()] = enderDragonPhase;
/* 69 */     return (EnderDragonPhase)enderDragonPhase;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\EnderDragonPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */