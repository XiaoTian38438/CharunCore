/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VehicleEntity
/*     */   extends Entity
/*     */ {
/*  23 */   protected static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.INT);
/*  24 */   protected static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.INT);
/*  25 */   protected static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.FLOAT);
/*     */   
/*     */   public VehicleEntity(EntityType<?> paramEntityType, Level paramLevel) {
/*  28 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource paramDamageSource) {
/*  33 */     return true;
/*     */   }
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
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isRemoved : ()Z
/*     */     //   4: ifeq -> 9
/*     */     //   7: iconst_1
/*     */     //   8: ireturn
/*     */     //   9: aload_0
/*     */     //   10: aload_2
/*     */     //   11: invokevirtual isInvulnerableToBase : (Lnet/minecraft/world/damagesource/DamageSource;)Z
/*     */     //   14: ifeq -> 19
/*     */     //   17: iconst_0
/*     */     //   18: ireturn
/*     */     //   19: aload_0
/*     */     //   20: aload_0
/*     */     //   21: invokevirtual getHurtDir : ()I
/*     */     //   24: ineg
/*     */     //   25: invokevirtual setHurtDir : (I)V
/*     */     //   28: aload_0
/*     */     //   29: bipush #10
/*     */     //   31: invokevirtual setHurtTime : (I)V
/*     */     //   34: aload_0
/*     */     //   35: invokevirtual markHurt : ()V
/*     */     //   38: aload_0
/*     */     //   39: aload_0
/*     */     //   40: invokevirtual getDamage : ()F
/*     */     //   43: fload_3
/*     */     //   44: ldc 10.0
/*     */     //   46: fmul
/*     */     //   47: fadd
/*     */     //   48: invokevirtual setDamage : (F)V
/*     */     //   51: aload_0
/*     */     //   52: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_DAMAGE : Lnet/minecraft/core/Holder$Reference;
/*     */     //   55: aload_2
/*     */     //   56: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   59: invokevirtual gameEvent : (Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;)V
/*     */     //   62: aload_2
/*     */     //   63: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   66: astore #6
/*     */     //   68: aload #6
/*     */     //   70: instanceof net/minecraft/world/entity/player/Player
/*     */     //   73: ifeq -> 98
/*     */     //   76: aload #6
/*     */     //   78: checkcast net/minecraft/world/entity/player/Player
/*     */     //   81: astore #5
/*     */     //   83: aload #5
/*     */     //   85: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */     //   88: getfield instabuild : Z
/*     */     //   91: ifeq -> 98
/*     */     //   94: iconst_1
/*     */     //   95: goto -> 99
/*     */     //   98: iconst_0
/*     */     //   99: istore #4
/*     */     //   101: iload #4
/*     */     //   103: ifne -> 116
/*     */     //   106: aload_0
/*     */     //   107: invokevirtual getDamage : ()F
/*     */     //   110: ldc 40.0
/*     */     //   112: fcmpl
/*     */     //   113: ifgt -> 124
/*     */     //   116: aload_0
/*     */     //   117: aload_2
/*     */     //   118: invokevirtual shouldSourceDestroy : (Lnet/minecraft/world/damagesource/DamageSource;)Z
/*     */     //   121: ifeq -> 133
/*     */     //   124: aload_0
/*     */     //   125: aload_1
/*     */     //   126: aload_2
/*     */     //   127: invokevirtual destroy : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V
/*     */     //   130: goto -> 142
/*     */     //   133: iload #4
/*     */     //   135: ifeq -> 142
/*     */     //   138: aload_0
/*     */     //   139: invokevirtual discard : ()V
/*     */     //   142: iconst_1
/*     */     //   143: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     //   #39	-> 7
/*     */     //   #41	-> 9
/*     */     //   #42	-> 17
/*     */     //   #44	-> 19
/*     */     //   #45	-> 28
/*     */     //   #46	-> 34
/*     */     //   #47	-> 38
/*     */     //   #48	-> 51
/*     */     //   #49	-> 62
/*     */     //   #51	-> 101
/*     */     //   #52	-> 124
/*     */     //   #53	-> 133
/*     */     //   #54	-> 138
/*     */     //   #56	-> 142
/*     */   }
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
/*     */   protected boolean shouldSourceDestroy(DamageSource paramDamageSource) {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion paramExplosion) {
/*  65 */     return (paramExplosion.getIndirectSourceEntity() instanceof net.minecraft.world.entity.Mob && 
/*  66 */       !((Boolean)paramExplosion.level().getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue());
/*     */   }
/*     */   
/*     */   public void destroy(ServerLevel paramServerLevel, Item paramItem) {
/*  70 */     kill(paramServerLevel);
/*     */     
/*  72 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     ItemStack itemStack = new ItemStack((ItemLike)paramItem);
/*  77 */     itemStack.set(DataComponents.CUSTOM_NAME, getCustomName());
/*  78 */     spawnAtLocation(paramServerLevel, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  83 */     paramBuilder.define(DATA_ID_HURT, Integer.valueOf(0));
/*  84 */     paramBuilder.define(DATA_ID_HURTDIR, Integer.valueOf(1));
/*  85 */     paramBuilder.define(DATA_ID_DAMAGE, Float.valueOf(0.0F));
/*     */   }
/*     */   
/*     */   public void setHurtTime(int paramInt) {
/*  89 */     this.entityData.set(DATA_ID_HURT, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public void setHurtDir(int paramInt) {
/*  93 */     this.entityData.set(DATA_ID_HURTDIR, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public void setDamage(float paramFloat) {
/*  97 */     this.entityData.set(DATA_ID_DAMAGE, Float.valueOf(paramFloat));
/*     */   }
/*     */   
/*     */   public float getDamage() {
/* 101 */     return ((Float)this.entityData.get(DATA_ID_DAMAGE)).floatValue();
/*     */   }
/*     */   
/*     */   public int getHurtTime() {
/* 105 */     return ((Integer)this.entityData.get(DATA_ID_HURT)).intValue();
/*     */   }
/*     */   
/*     */   public int getHurtDir() {
/* 109 */     return ((Integer)this.entityData.get(DATA_ID_HURTDIR)).intValue();
/*     */   }
/*     */   
/*     */   protected void destroy(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/* 113 */     destroy(paramServerLevel, getDropItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDimensionChangingDelay() {
/* 118 */     return 10;
/*     */   }
/*     */   
/*     */   protected abstract Item getDropItem();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\VehicleEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */