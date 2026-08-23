/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class EnderDragonPhaseManager
/*    */ {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final EnderDragon dragon;
/* 14 */   private final DragonPhaseInstance[] phases = new DragonPhaseInstance[EnderDragonPhase.getCount()];
/*    */   private DragonPhaseInstance currentPhase;
/*    */   
/*    */   public EnderDragonPhaseManager(EnderDragon paramEnderDragon) {
/* 18 */     this.dragon = paramEnderDragon;
/*    */     
/* 20 */     setPhase(EnderDragonPhase.HOVERING);
/*    */   }
/*    */   
/*    */   public void setPhase(EnderDragonPhase<?> paramEnderDragonPhase) {
/* 24 */     if (this.currentPhase != null && paramEnderDragonPhase == this.currentPhase.getPhase()) {
/*    */       return;
/*    */     }
/*    */     
/* 28 */     if (this.currentPhase != null) {
/* 29 */       this.currentPhase.end();
/*    */     }
/*    */     
/* 32 */     this.currentPhase = getPhase(paramEnderDragonPhase);
/* 33 */     if (!this.dragon.level().isClientSide()) {
/* 34 */       this.dragon.getEntityData().set(EnderDragon.DATA_PHASE, Integer.valueOf(paramEnderDragonPhase.getId()));
/*    */     }
/* 36 */     LOGGER.debug("Dragon is now in phase {} on the {}", paramEnderDragonPhase, this.dragon.level().isClientSide() ? "client" : "server");
/*    */     
/* 38 */     this.currentPhase.begin();
/*    */   }
/*    */   
/*    */   public DragonPhaseInstance getCurrentPhase() {
/* 42 */     return Objects.<DragonPhaseInstance>requireNonNull(this.currentPhase);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends DragonPhaseInstance> T getPhase(EnderDragonPhase<T> paramEnderDragonPhase) {
/* 47 */     int i = paramEnderDragonPhase.getId();
/* 48 */     DragonPhaseInstance dragonPhaseInstance = this.phases[i];
/* 49 */     if (dragonPhaseInstance == null) {
/* 50 */       dragonPhaseInstance = paramEnderDragonPhase.createInstance(this.dragon);
/* 51 */       this.phases[i] = dragonPhaseInstance;
/*    */     } 
/* 53 */     return (T)dragonPhaseInstance;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\phases\EnderDragonPhaseManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */