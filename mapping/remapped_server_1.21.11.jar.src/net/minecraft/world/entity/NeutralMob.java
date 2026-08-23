/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface NeutralMob
/*     */ {
/*     */   public static final String TAG_ANGER_END_TIME = "anger_end_time";
/*     */   public static final String TAG_ANGRY_AT = "angry_at";
/*     */   public static final long NO_ANGER_END_TIME = -1L;
/*     */   
/*     */   long getPersistentAngerEndTime();
/*     */   
/*     */   default void setTimeToRemainAngry(long paramLong) {
/*  41 */     setPersistentAngerEndTime(level().getGameTime() + paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setPersistentAngerEndTime(long paramLong);
/*     */ 
/*     */ 
/*     */   
/*     */   EntityReference<LivingEntity> getPersistentAngerTarget();
/*     */ 
/*     */   
/*     */   void setPersistentAngerTarget(EntityReference<LivingEntity> paramEntityReference);
/*     */ 
/*     */   
/*     */   void startPersistentAngerTimer();
/*     */ 
/*     */   
/*     */   Level level();
/*     */ 
/*     */   
/*     */   default void addPersistentAngerSaveData(ValueOutput paramValueOutput) {
/*  63 */     paramValueOutput.putLong("anger_end_time", getPersistentAngerEndTime());
/*  64 */     paramValueOutput.storeNullable("angry_at", EntityReference.codec(), getPersistentAngerTarget());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void readPersistentAngerSaveData(Level paramLevel, ValueInput paramValueInput) {
/*  71 */     Optional<Long> optional = paramValueInput.getLong("anger_end_time");
/*  72 */     if (optional.isPresent()) {
/*  73 */       setPersistentAngerEndTime(((Long)optional.get()).longValue());
/*     */     }
/*     */     else {
/*     */       
/*  77 */       Optional<Integer> optional1 = paramValueInput.getInt("AngerTime");
/*  78 */       if (optional1.isPresent()) {
/*  79 */         setTimeToRemainAngry(((Integer)optional1.get()).intValue());
/*     */       } else {
/*  81 */         setPersistentAngerEndTime(-1L);
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     if (!(paramLevel instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     setPersistentAngerTarget(EntityReference.read(paramValueInput, "angry_at"));
/*  90 */     setTarget(EntityReference.getLivingEntity(getPersistentAngerTarget(), paramLevel));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void updatePersistentAnger(ServerLevel paramServerLevel, boolean paramBoolean) {
/*  97 */     LivingEntity livingEntity = getTarget();
/*     */     
/*  99 */     EntityReference<LivingEntity> entityReference = getPersistentAngerTarget();
/*     */     
/* 101 */     if (livingEntity != null && livingEntity.isDeadOrDying() && entityReference != null && entityReference.matches(livingEntity) && livingEntity instanceof Mob) {
/*     */ 
/*     */ 
/*     */       
/* 105 */       stopBeingAngry();
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     if (livingEntity != null) {
/* 110 */       if (entityReference == null || !entityReference.matches(livingEntity))
/*     */       {
/* 112 */         setPersistentAngerTarget(EntityReference.of(livingEntity));
/*     */       }
/*     */       
/* 115 */       startPersistentAngerTimer();
/*     */     } 
/*     */     
/* 118 */     if (entityReference != null && !isAngry() && (
/* 119 */       livingEntity == null || !isValidPlayerTarget(livingEntity) || !paramBoolean)) {
/* 120 */       stopBeingAngry();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidPlayerTarget(LivingEntity paramLivingEntity) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: instanceof net/minecraft/world/entity/player/Player
/*     */     //   4: ifeq -> 30
/*     */     //   7: aload_0
/*     */     //   8: checkcast net/minecraft/world/entity/player/Player
/*     */     //   11: astore_1
/*     */     //   12: aload_1
/*     */     //   13: invokevirtual isCreative : ()Z
/*     */     //   16: ifne -> 30
/*     */     //   19: aload_1
/*     */     //   20: invokevirtual isSpectator : ()Z
/*     */     //   23: ifne -> 30
/*     */     //   26: iconst_1
/*     */     //   27: goto -> 31
/*     */     //   30: iconst_0
/*     */     //   31: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #128	-> 0
/*     */     //   #126	-> 7
/*     */     //   #127	-> 13
/*     */     //   #128	-> 20
/*     */     //   #126	-> 31
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isAngryAt(LivingEntity paramLivingEntity, ServerLevel paramServerLevel) {
/* 135 */     if (!canAttack(paramLivingEntity)) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     if (isValidPlayerTarget(paramLivingEntity) && isAngryAtAllPlayers(paramServerLevel)) {
/* 140 */       return true;
/*     */     }
/*     */     
/* 143 */     EntityReference<LivingEntity> entityReference = getPersistentAngerTarget();
/* 144 */     return (entityReference != null && entityReference.matches(paramLivingEntity));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isAngryAtAllPlayers(ServerLevel paramServerLevel) {
/* 150 */     return (((Boolean)paramServerLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue() && isAngry() && getPersistentAngerTarget() == null);
/*     */   }
/*     */   
/*     */   default boolean isAngry() {
/* 154 */     long l = getPersistentAngerEndTime();
/* 155 */     if (l > 0L) {
/* 156 */       long l1 = l - level().getGameTime();
/* 157 */       return (l1 > 0L);
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */   
/*     */   default void playerDied(ServerLevel paramServerLevel, Player paramPlayer) {
/* 163 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.FORGIVE_DEAD_PLAYERS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     EntityReference<LivingEntity> entityReference = getPersistentAngerTarget();
/* 168 */     if (entityReference == null || !entityReference.matches(paramPlayer)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 173 */     stopBeingAngry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void forgetCurrentTargetAndRefreshUniversalAnger() {
/* 180 */     stopBeingAngry();
/* 181 */     startPersistentAngerTimer();
/*     */   }
/*     */   
/*     */   default void stopBeingAngry() {
/* 185 */     setLastHurtByMob(null);
/* 186 */     setPersistentAngerTarget(null);
/* 187 */     setTarget(null);
/* 188 */     setPersistentAngerEndTime(-1L);
/*     */   }
/*     */   
/*     */   LivingEntity getLastHurtByMob();
/*     */   
/*     */   void setLastHurtByMob(LivingEntity paramLivingEntity);
/*     */   
/*     */   void setTarget(LivingEntity paramLivingEntity);
/*     */   
/*     */   boolean canAttack(LivingEntity paramLivingEntity);
/*     */   
/*     */   LivingEntity getTarget();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\NeutralMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */