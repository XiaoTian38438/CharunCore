/*     */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ThrownEnderpearl
/*     */   extends ThrowableItemProjectile
/*     */ {
/*  34 */   private long ticketTimer = 0L;
/*     */   
/*     */   public ThrownEnderpearl(EntityType<? extends ThrownEnderpearl> paramEntityType, Level paramLevel) {
/*  37 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public ThrownEnderpearl(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  41 */     super(EntityType.ENDER_PEARL, paramLivingEntity, paramLevel, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDefaultItem() {
/*  46 */     return Items.ENDER_PEARL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setOwner(EntityReference<Entity> paramEntityReference) {
/*  51 */     deregisterFromCurrentOwner();
/*  52 */     super.setOwner(paramEntityReference);
/*  53 */     registerToCurrentOwner();
/*     */   }
/*     */   
/*     */   private void deregisterFromCurrentOwner() {
/*  57 */     Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  58 */       serverPlayer.deregisterEnderPearl(this); }
/*     */   
/*     */   }
/*     */   
/*     */   private void registerToCurrentOwner() {
/*  63 */     Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  64 */       serverPlayer.registerEnderPearl(this); }
/*     */   
/*     */   }
/*     */   
/*     */   public Entity getOwner() {
/*     */     ServerLevel serverLevel;
/*  70 */     if (this.owner != null) { Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  71 */       else { return super.getOwner(); }  } else { return super.getOwner(); }
/*     */     
/*  73 */     return (Entity)this.owner.getEntity((Level)serverLevel, Entity.class);
/*     */   }
/*     */   
/*     */   private static Entity findOwnerIncludingDeadPlayer(ServerLevel paramServerLevel, UUID paramUUID) {
/*  77 */     Entity entity = paramServerLevel.getEntityInAnyDimension(paramUUID);
/*  78 */     if (entity != null) {
/*  79 */       return entity;
/*     */     }
/*     */ 
/*     */     
/*  83 */     return (Entity)paramServerLevel.getServer().getPlayerList().getPlayer(paramUUID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/*  88 */     super.onHitEntity(paramEntityHitResult);
/*  89 */     paramEntityHitResult.getEntity().hurt(damageSources().thrown((Entity)this, getOwner()), 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult paramHitResult) {
/*  94 */     super.onHit(paramHitResult);
/*     */     
/*  96 */     for (byte b = 0; b < 32; b++) {
/*  97 */       level().addParticle((ParticleOptions)ParticleTypes.PORTAL, getX(), getY() + this.random.nextDouble() * 2.0D, getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
/*     */     }
/*     */     
/* 100 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!isRemoved()) {
/*     */ 
/*     */ 
/*     */         
/* 104 */         Entity entity = getOwner();
/* 105 */         if (entity == null || !isAllowedToTeleportOwner(entity, (Level)serverLevel)) {
/* 106 */           discard();
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 112 */         Vec3 vec3 = oldPosition();
/*     */         
/* 114 */         if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 115 */           if (serverPlayer.connection.isAcceptingMessages()) {
/* 116 */             if (this.random.nextFloat() < 0.05F && serverLevel.isSpawningMonsters()) {
/* 117 */               Endermite endermite = (Endermite)EntityType.ENDERMITE.create((Level)serverLevel, EntitySpawnReason.TRIGGERED);
/* 118 */               if (endermite != null) {
/* 119 */                 endermite.snapTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
/* 120 */                 serverLevel.addFreshEntity((Entity)endermite);
/*     */               } 
/*     */             } 
/*     */             
/* 124 */             if (isOnPortalCooldown())
/*     */             {
/*     */               
/* 127 */               entity.setPortalCooldown();
/*     */             }
/*     */             
/* 130 */             ServerPlayer serverPlayer1 = serverPlayer.teleport(new TeleportTransition(serverLevel, vec3, Vec3.ZERO, 0.0F, 0.0F, Relative.union(new Set[] { Relative.ROTATION, Relative.DELTA }, ), TeleportTransition.DO_NOTHING));
/* 131 */             if (serverPlayer1 != null) {
/* 132 */               serverPlayer1.resetFallDistance();
/* 133 */               serverPlayer1.resetCurrentImpulseContext();
/* 134 */               serverPlayer1.hurtServer(serverPlayer.level(), damageSources().enderPearl(), 5.0F);
/*     */             } 
/*     */             
/* 137 */             playSound((Level)serverLevel, vec3);
/*     */           }  }
/*     */         else
/* 140 */         { Entity entity1 = entity.teleport(new TeleportTransition(serverLevel, vec3, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), TeleportTransition.DO_NOTHING));
/* 141 */           if (entity1 != null) {
/* 142 */             entity1.resetFallDistance();
/*     */           }
/* 144 */           playSound((Level)serverLevel, vec3); }
/*     */ 
/*     */         
/* 147 */         discard();
/*     */         return;
/*     */       }  }
/*     */      } private static boolean isAllowedToTeleportOwner(Entity paramEntity, Level paramLevel) {
/* 151 */     if (paramEntity.level().dimension() == paramLevel.dimension()) {
/* 152 */       if (paramEntity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)paramEntity;
/* 153 */         return (livingEntity.isAlive() && !livingEntity.isSleeping()); }
/*     */       
/* 155 */       return paramEntity.isAlive();
/*     */     } 
/* 157 */     return paramEntity.canUsePortal(true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   4: astore_2
/*     */     //   5: aload_2
/*     */     //   6: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   9: ifeq -> 20
/*     */     //   12: aload_2
/*     */     //   13: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   16: astore_1
/*     */     //   17: goto -> 25
/*     */     //   20: aload_0
/*     */     //   21: invokespecial tick : ()V
/*     */     //   24: return
/*     */     //   25: aload_0
/*     */     //   26: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   29: invokevirtual x : ()D
/*     */     //   32: invokestatic blockToSectionCoord : (D)I
/*     */     //   35: istore_2
/*     */     //   36: aload_0
/*     */     //   37: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   40: invokevirtual z : ()D
/*     */     //   43: invokestatic blockToSectionCoord : (D)I
/*     */     //   46: istore_3
/*     */     //   47: aload_0
/*     */     //   48: getfield owner : Lnet/minecraft/world/entity/EntityReference;
/*     */     //   51: ifnull -> 68
/*     */     //   54: aload_1
/*     */     //   55: aload_0
/*     */     //   56: getfield owner : Lnet/minecraft/world/entity/EntityReference;
/*     */     //   59: invokevirtual getUUID : ()Ljava/util/UUID;
/*     */     //   62: invokestatic findOwnerIncludingDeadPlayer : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/UUID;)Lnet/minecraft/world/entity/Entity;
/*     */     //   65: goto -> 69
/*     */     //   68: aconst_null
/*     */     //   69: astore #4
/*     */     //   71: aload #4
/*     */     //   73: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   76: ifeq -> 132
/*     */     //   79: aload #4
/*     */     //   81: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   84: astore #5
/*     */     //   86: aload #4
/*     */     //   88: invokevirtual isAlive : ()Z
/*     */     //   91: ifne -> 132
/*     */     //   94: aload #5
/*     */     //   96: getfield wonGame : Z
/*     */     //   99: ifne -> 132
/*     */     //   102: aload #5
/*     */     //   104: invokevirtual level : ()Lnet/minecraft/server/level/ServerLevel;
/*     */     //   107: invokevirtual getGameRules : ()Lnet/minecraft/world/level/gamerules/GameRules;
/*     */     //   110: getstatic net/minecraft/world/level/gamerules/GameRules.ENDER_PEARLS_VANISH_ON_DEATH : Lnet/minecraft/world/level/gamerules/GameRule;
/*     */     //   113: invokevirtual get : (Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;
/*     */     //   116: checkcast java/lang/Boolean
/*     */     //   119: invokevirtual booleanValue : ()Z
/*     */     //   122: ifeq -> 132
/*     */     //   125: aload_0
/*     */     //   126: invokevirtual discard : ()V
/*     */     //   129: goto -> 136
/*     */     //   132: aload_0
/*     */     //   133: invokespecial tick : ()V
/*     */     //   136: aload_0
/*     */     //   137: invokevirtual isAlive : ()Z
/*     */     //   140: ifne -> 144
/*     */     //   143: return
/*     */     //   144: aload_0
/*     */     //   145: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   148: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   151: astore #5
/*     */     //   153: aload_0
/*     */     //   154: dup
/*     */     //   155: getfield ticketTimer : J
/*     */     //   158: lconst_1
/*     */     //   159: lsub
/*     */     //   160: dup2_x1
/*     */     //   161: putfield ticketTimer : J
/*     */     //   164: lconst_0
/*     */     //   165: lcmp
/*     */     //   166: ifle -> 193
/*     */     //   169: iload_2
/*     */     //   170: aload #5
/*     */     //   172: invokevirtual getX : ()I
/*     */     //   175: invokestatic blockToSectionCoord : (I)I
/*     */     //   178: if_icmpne -> 193
/*     */     //   181: iload_3
/*     */     //   182: aload #5
/*     */     //   184: invokevirtual getZ : ()I
/*     */     //   187: invokestatic blockToSectionCoord : (I)I
/*     */     //   190: if_icmpeq -> 218
/*     */     //   193: aload #4
/*     */     //   195: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   198: ifeq -> 218
/*     */     //   201: aload #4
/*     */     //   203: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   206: astore #6
/*     */     //   208: aload_0
/*     */     //   209: aload #6
/*     */     //   211: aload_0
/*     */     //   212: invokevirtual registerAndUpdateEnderPearlTicket : (Lnet/minecraft/world/entity/projectile/throwableitemprojectile/ThrownEnderpearl;)J
/*     */     //   215: putfield ticketTimer : J
/*     */     //   218: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #162	-> 0
/*     */     //   #163	-> 20
/*     */     //   #164	-> 24
/*     */     //   #167	-> 25
/*     */     //   #168	-> 36
/*     */     //   #171	-> 47
/*     */     //   #173	-> 71
/*     */     //   #174	-> 125
/*     */     //   #176	-> 132
/*     */     //   #179	-> 136
/*     */     //   #180	-> 143
/*     */     //   #183	-> 144
/*     */     //   #184	-> 153
/*     */     //   #185	-> 184
/*     */     //   #186	-> 193
/*     */     //   #187	-> 208
/*     */     //   #190	-> 218
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void playSound(Level paramLevel, Vec3 paramVec3) {
/* 193 */     paramLevel.playSound(null, paramVec3.x, paramVec3.y, paramVec3.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition paramTeleportTransition) {
/* 198 */     Entity entity = super.teleport(paramTeleportTransition);
/* 199 */     if (entity != null)
/*     */     {
/*     */ 
/*     */       
/* 203 */       entity.placePortalTicket(BlockPos.containing((Position)entity.position()));
/*     */     }
/* 205 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTeleport(Level paramLevel1, Level paramLevel2) {
/* 210 */     if (paramLevel1.dimension() == Level.END && paramLevel2.dimension() == Level.OVERWORLD) { Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 211 */         return (super.canTeleport(paramLevel1, paramLevel2) && serverPlayer.seenCredits); }
/*     */        }
/* 213 */      return super.canTeleport(paramLevel1, paramLevel2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onInsideBlock(BlockState paramBlockState) {
/* 218 */     super.onInsideBlock(paramBlockState);
/* 219 */     if (paramBlockState.is(Blocks.END_GATEWAY)) { Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 220 */         serverPlayer.onInsideBlock(paramBlockState); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public void onRemoval(Entity.RemovalReason paramRemovalReason) {
/* 226 */     if (paramRemovalReason != Entity.RemovalReason.UNLOADED_WITH_PLAYER) {
/* 227 */       deregisterFromCurrentOwner();
/*     */     }
/* 229 */     super.onRemoval(paramRemovalReason);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAboveBubbleColumn(boolean paramBoolean, BlockPos paramBlockPos) {
/* 234 */     Entity.handleOnAboveBubbleColumn((Entity)this, paramBoolean, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInsideBubbleColumn(boolean paramBoolean) {
/* 239 */     Entity.handleOnInsideBubbleColumn((Entity)this, paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownEnderpearl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */