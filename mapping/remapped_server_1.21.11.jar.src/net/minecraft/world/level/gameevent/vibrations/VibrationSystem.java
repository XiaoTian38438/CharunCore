/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ClipBlockStateContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public interface VibrationSystem
/*     */ {
/*  51 */   public static final List<ResourceKey<GameEvent>> RESONANCE_EVENTS = List.of((ResourceKey<GameEvent>[])new ResourceKey[] { GameEvent.RESONATE_1
/*  52 */         .key(), GameEvent.RESONATE_2.key(), GameEvent.RESONATE_3.key(), GameEvent.RESONATE_4.key(), GameEvent.RESONATE_5.key(), GameEvent.RESONATE_6
/*  53 */         .key(), GameEvent.RESONATE_7.key(), GameEvent.RESONATE_8.key(), GameEvent.RESONATE_9.key(), GameEvent.RESONATE_10.key(), GameEvent.RESONATE_11
/*  54 */         .key(), GameEvent.RESONATE_12.key(), GameEvent.RESONATE_13.key(), GameEvent.RESONATE_14.key(), GameEvent.RESONATE_15.key() });
/*     */   public static final int NO_VIBRATION_FREQUENCY = 0;
/*     */   public static final ToIntFunction<ResourceKey<GameEvent>> VIBRATION_FREQUENCY_FOR_EVENT;
/*     */   
/*     */   static {
/*  59 */     VIBRATION_FREQUENCY_FOR_EVENT = (ToIntFunction<ResourceKey<GameEvent>>)Util.make(new Reference2IntOpenHashMap(), paramReference2IntOpenHashMap -> {
/*     */           paramReference2IntOpenHashMap.defaultReturnValue(0);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.STEP.key(), 1);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.SWIM.key(), 1);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.FLAP.key(), 1);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.PROJECTILE_LAND.key(), 2);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.HIT_GROUND.key(), 2);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.SPLASH.key(), 2);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ITEM_INTERACT_FINISH.key(), 3);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.PROJECTILE_SHOOT.key(), 3);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.INSTRUMENT_PLAY.key(), 3);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_ACTION.key(), 4);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ELYTRA_GLIDE.key(), 4);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.UNEQUIP.key(), 4);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_DISMOUNT.key(), 5);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.EQUIP.key(), 5);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_INTERACT.key(), 6);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.SHEAR.key(), 6);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_MOUNT.key(), 6);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_DAMAGE.key(), 7);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.DRINK.key(), 8);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.EAT.key(), 8);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.CONTAINER_CLOSE.key(), 9);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_CLOSE.key(), 9);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_DEACTIVATE.key(), 9);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_DETACH.key(), 9);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.CONTAINER_OPEN.key(), 10);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_OPEN.key(), 10);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_ACTIVATE.key(), 10);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_ATTACH.key(), 10);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.PRIME_FUSE.key(), 10);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.NOTE_BLOCK_PLAY.key(), 10);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_CHANGE.key(), 11);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_DESTROY.key(), 12);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.FLUID_PICKUP.key(), 12);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.BLOCK_PLACE.key(), 13);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.FLUID_PLACE.key(), 13);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_PLACE.key(), 14);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.LIGHTNING_STRIKE.key(), 14);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.TELEPORT.key(), 14);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.ENTITY_DIE.key(), 15);
/*     */           paramReference2IntOpenHashMap.put(GameEvent.EXPLODE.key(), 15);
/*     */           for (byte b = 1; b <= 15; b++) {
/*     */             paramReference2IntOpenHashMap.put(getResonanceEventByFrequency(b), b);
/*     */           }
/*     */         });
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
/*     */   static int getGameEventFrequency(Holder<GameEvent> paramHolder) {
/* 138 */     return ((Integer)paramHolder.unwrapKey().map(VibrationSystem::getGameEventFrequency).orElse(Integer.valueOf(0))).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   static int getGameEventFrequency(ResourceKey<GameEvent> paramResourceKey) {
/* 143 */     return VIBRATION_FREQUENCY_FOR_EVENT.applyAsInt(paramResourceKey);
/*     */   }
/*     */   
/*     */   static ResourceKey<GameEvent> getResonanceEventByFrequency(int paramInt) {
/* 147 */     return RESONANCE_EVENTS.get(paramInt - 1);
/*     */   }
/*     */   
/*     */   static int getRedstoneStrengthForDistance(float paramFloat, int paramInt) {
/* 151 */     double d = 15.0D / paramInt;
/* 152 */     return Math.max(1, 15 - Mth.floor(d * paramFloat));
/*     */   }
/*     */   
/*     */   Data getVibrationData();
/*     */   
/*     */   User getVibrationUser();
/*     */   
/*     */   public static final class Data {
/*     */     static {
/* 161 */       CODEC = RecordCodecBuilder.create(param1Instance -> param1Instance.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)VibrationSelector.CODEC.fieldOf("selector").forGetter(Data::getSelectionStrategy), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(Integer.valueOf(0)).forGetter(Data::getTravelTimeInTicks)).apply((Applicative)param1Instance, ()));
/*     */     }
/*     */ 
/*     */     
/*     */     public static Codec<Data> CODEC;
/*     */     
/*     */     public static final String NBT_TAG_KEY = "listener";
/*     */     
/*     */     VibrationInfo currentVibration;
/*     */     private int travelTimeInTicks;
/*     */     final VibrationSelector selectionStrategy;
/*     */     private boolean reloadVibrationParticle;
/*     */     
/*     */     private Data(VibrationInfo param1VibrationInfo, VibrationSelector param1VibrationSelector, int param1Int, boolean param1Boolean) {
/* 175 */       this.currentVibration = param1VibrationInfo;
/* 176 */       this.travelTimeInTicks = param1Int;
/* 177 */       this.selectionStrategy = param1VibrationSelector;
/* 178 */       this.reloadVibrationParticle = param1Boolean;
/*     */     }
/*     */     
/*     */     public Data() {
/* 182 */       this(null, new VibrationSelector(), 0, false);
/*     */     }
/*     */     
/*     */     public VibrationSelector getSelectionStrategy() {
/* 186 */       return this.selectionStrategy;
/*     */     }
/*     */     
/*     */     public VibrationInfo getCurrentVibration() {
/* 190 */       return this.currentVibration;
/*     */     }
/*     */     
/*     */     public void setCurrentVibration(VibrationInfo param1VibrationInfo) {
/* 194 */       this.currentVibration = param1VibrationInfo;
/*     */     }
/*     */     
/*     */     public int getTravelTimeInTicks() {
/* 198 */       return this.travelTimeInTicks;
/*     */     }
/*     */     
/*     */     public void setTravelTimeInTicks(int param1Int) {
/* 202 */       this.travelTimeInTicks = param1Int;
/*     */     }
/*     */     
/*     */     public void decrementTravelTime() {
/* 206 */       this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1);
/*     */     }
/*     */     
/*     */     public boolean shouldReloadVibrationParticle() {
/* 210 */       return this.reloadVibrationParticle;
/*     */     }
/*     */     
/*     */     public void setReloadVibrationParticle(boolean param1Boolean) {
/* 214 */       this.reloadVibrationParticle = param1Boolean;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Listener
/*     */     implements GameEventListener
/*     */   {
/*     */     private final VibrationSystem system;
/*     */ 
/*     */     
/*     */     public Listener(VibrationSystem param1VibrationSystem) {
/* 227 */       this.system = param1VibrationSystem;
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionSource getListenerSource() {
/* 232 */       return this.system.getVibrationUser().getPositionSource();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getListenerRadius() {
/* 237 */       return this.system.getVibrationUser().getListenerRadius();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel param1ServerLevel, Holder<GameEvent> param1Holder, GameEvent.Context param1Context, Vec3 param1Vec3) {
/* 242 */       VibrationSystem.Data data = this.system.getVibrationData();
/* 243 */       VibrationSystem.User user = this.system.getVibrationUser();
/*     */ 
/*     */       
/* 246 */       if (data.getCurrentVibration() != null) {
/* 247 */         return false;
/*     */       }
/*     */       
/* 250 */       if (!user.isValidVibration(param1Holder, param1Context)) {
/* 251 */         return false;
/*     */       }
/*     */       
/* 254 */       Optional<Vec3> optional = user.getPositionSource().getPosition((Level)param1ServerLevel);
/*     */       
/* 256 */       if (optional.isEmpty()) {
/* 257 */         return false;
/*     */       }
/*     */       
/* 260 */       Vec3 vec3 = optional.get();
/*     */ 
/*     */       
/* 263 */       if (!user.canReceiveVibration(param1ServerLevel, BlockPos.containing((Position)param1Vec3), param1Holder, param1Context)) {
/* 264 */         return false;
/*     */       }
/*     */       
/* 267 */       if (isOccluded((Level)param1ServerLevel, param1Vec3, vec3)) {
/* 268 */         return false;
/*     */       }
/*     */       
/* 271 */       scheduleVibration(param1ServerLevel, data, param1Holder, param1Context, param1Vec3, vec3);
/*     */       
/* 273 */       return true;
/*     */     }
/*     */     
/*     */     public void forceScheduleVibration(ServerLevel param1ServerLevel, Holder<GameEvent> param1Holder, GameEvent.Context param1Context, Vec3 param1Vec3) {
/* 277 */       this.system.getVibrationUser().getPositionSource().getPosition((Level)param1ServerLevel).ifPresent(param1Vec32 -> scheduleVibration(param1ServerLevel, this.system.getVibrationData(), param1Holder, param1Context, param1Vec31, param1Vec32));
/*     */     }
/*     */     
/*     */     private void scheduleVibration(ServerLevel param1ServerLevel, VibrationSystem.Data param1Data, Holder<GameEvent> param1Holder, GameEvent.Context param1Context, Vec3 param1Vec31, Vec3 param1Vec32) {
/* 281 */       param1Data.selectionStrategy.addCandidate(new VibrationInfo(param1Holder, (float)param1Vec31.distanceTo(param1Vec32), param1Vec31, param1Context.sourceEntity()), param1ServerLevel.getGameTime());
/*     */     }
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
/*     */     public static float distanceBetweenInBlocks(BlockPos param1BlockPos1, BlockPos param1BlockPos2) {
/* 297 */       return (float)Math.sqrt(param1BlockPos1.distSqr((Vec3i)param1BlockPos2));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static boolean isOccluded(Level param1Level, Vec3 param1Vec31, Vec3 param1Vec32) {
/* 306 */       Vec3 vec31 = new Vec3(Mth.floor(param1Vec31.x) + 0.5D, Mth.floor(param1Vec31.y) + 0.5D, Mth.floor(param1Vec31.z) + 0.5D);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       Vec3 vec32 = new Vec3(Mth.floor(param1Vec32.x) + 0.5D, Mth.floor(param1Vec32.y) + 0.5D, Mth.floor(param1Vec32.z) + 0.5D);
/*     */ 
/*     */       
/* 314 */       for (Direction direction : Direction.values()) {
/* 315 */         Vec3 vec3 = vec31.relative(direction, 9.999999747378752E-6D);
/* 316 */         if (param1Level.isBlockInLine(new ClipBlockStateContext(vec3, vec32, param1BlockState -> param1BlockState.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
/* 317 */           return false;
/*     */         }
/*     */       } 
/* 320 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Ticker
/*     */   {
/*     */     static void tick(Level param1Level, VibrationSystem.Data param1Data, VibrationSystem.User param1User) {
/*     */       ServerLevel serverLevel;
/* 333 */       if (param1Level instanceof ServerLevel) { serverLevel = (ServerLevel)param1Level; }
/*     */       else
/*     */       { return; }
/*     */       
/* 337 */       if (param1Data.currentVibration == null) {
/* 338 */         trySelectAndScheduleVibration(serverLevel, param1Data, param1User);
/*     */       }
/*     */       
/* 341 */       if (param1Data.currentVibration == null) {
/*     */         return;
/*     */       }
/*     */       
/* 345 */       boolean bool = (param1Data.getTravelTimeInTicks() > 0);
/* 346 */       tryReloadVibrationParticle(serverLevel, param1Data, param1User);
/* 347 */       param1Data.decrementTravelTime();
/*     */       
/* 349 */       if (param1Data.getTravelTimeInTicks() <= 0) {
/* 350 */         bool = receiveVibration(serverLevel, param1Data, param1User, param1Data.currentVibration);
/*     */       }
/*     */       
/* 353 */       if (bool) {
/* 354 */         param1User.onDataChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     private static void trySelectAndScheduleVibration(ServerLevel param1ServerLevel, VibrationSystem.Data param1Data, VibrationSystem.User param1User) {
/* 359 */       param1Data.getSelectionStrategy().chosenCandidate(param1ServerLevel.getGameTime()).ifPresent(param1VibrationInfo -> {
/*     */             param1Data.setCurrentVibration(param1VibrationInfo);
/*     */             Vec3 vec3 = param1VibrationInfo.pos();
/*     */             param1Data.setTravelTimeInTicks(param1User.calculateTravelTimeInTicks(param1VibrationInfo.distance()));
/*     */             param1ServerLevel.sendParticles((ParticleOptions)new VibrationParticleOption(param1User.getPositionSource(), param1Data.getTravelTimeInTicks()), vec3.x, vec3.y, vec3.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */             param1User.onDataChanged();
/*     */             param1Data.getSelectionStrategy().startOver();
/*     */           });
/*     */     }
/*     */     
/*     */     private static void tryReloadVibrationParticle(ServerLevel param1ServerLevel, VibrationSystem.Data param1Data, VibrationSystem.User param1User) {
/* 370 */       if (!param1Data.shouldReloadVibrationParticle()) {
/*     */         return;
/*     */       }
/*     */       
/* 374 */       if (param1Data.currentVibration == null) {
/* 375 */         param1Data.setReloadVibrationParticle(false);
/*     */         
/*     */         return;
/*     */       } 
/* 379 */       Vec3 vec31 = param1Data.currentVibration.pos();
/* 380 */       PositionSource positionSource = param1User.getPositionSource();
/* 381 */       Vec3 vec32 = positionSource.getPosition((Level)param1ServerLevel).orElse(vec31);
/* 382 */       int i = param1Data.getTravelTimeInTicks();
/*     */       
/* 384 */       int j = param1User.calculateTravelTimeInTicks(param1Data.currentVibration.distance());
/* 385 */       double d1 = 1.0D - i / j;
/*     */       
/* 387 */       double d2 = Mth.lerp(d1, vec31.x, vec32.x);
/* 388 */       double d3 = Mth.lerp(d1, vec31.y, vec32.y);
/* 389 */       double d4 = Mth.lerp(d1, vec31.z, vec32.z);
/*     */       
/* 391 */       boolean bool = (param1ServerLevel.sendParticles((ParticleOptions)new VibrationParticleOption(positionSource, i), d2, d3, d4, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0) ? true : false;
/*     */       
/* 393 */       if (bool) {
/* 394 */         param1Data.setReloadVibrationParticle(false);
/*     */       }
/*     */     }
/*     */     
/*     */     private static boolean receiveVibration(ServerLevel param1ServerLevel, VibrationSystem.Data param1Data, VibrationSystem.User param1User, VibrationInfo param1VibrationInfo) {
/* 399 */       BlockPos blockPos1 = BlockPos.containing((Position)param1VibrationInfo.pos());
/* 400 */       BlockPos blockPos2 = param1User.getPositionSource().getPosition((Level)param1ServerLevel).map(BlockPos::containing).orElse(blockPos1);
/*     */ 
/*     */ 
/*     */       
/* 404 */       if (param1User.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking((Level)param1ServerLevel, blockPos2)) {
/* 405 */         return false;
/*     */       }
/*     */       
/* 408 */       param1User.onReceiveVibration(param1ServerLevel, blockPos1, param1VibrationInfo
/*     */ 
/*     */           
/* 411 */           .gameEvent(), param1VibrationInfo
/* 412 */           .getEntity(param1ServerLevel).orElse(null), param1VibrationInfo
/* 413 */           .getProjectileOwner(param1ServerLevel).orElse(null), 
/* 414 */           VibrationSystem.Listener.distanceBetweenInBlocks(blockPos1, blockPos2));
/*     */ 
/*     */ 
/*     */       
/* 418 */       param1Data.setCurrentVibration(null);
/* 419 */       return true;
/*     */     }
/*     */     
/*     */     private static boolean areAdjacentChunksTicking(Level param1Level, BlockPos param1BlockPos) {
/* 423 */       ChunkPos chunkPos = new ChunkPos(param1BlockPos);
/*     */       
/* 425 */       for (int i = chunkPos.x - 1; i <= chunkPos.x + 1; i++) {
/* 426 */         for (int j = chunkPos.z - 1; j <= chunkPos.z + 1; j++) {
/* 427 */           if (!param1Level.shouldTickBlocksAt(ChunkPos.asLong(i, j)) || param1Level.getChunkSource().getChunkNow(i, j) == null) {
/* 428 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 433 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface User
/*     */   {
/*     */     int getListenerRadius();
/*     */ 
/*     */ 
/*     */     
/*     */     PositionSource getPositionSource();
/*     */ 
/*     */ 
/*     */     
/*     */     boolean canReceiveVibration(ServerLevel param1ServerLevel, BlockPos param1BlockPos, Holder<GameEvent> param1Holder, GameEvent.Context param1Context);
/*     */ 
/*     */ 
/*     */     
/*     */     void onReceiveVibration(ServerLevel param1ServerLevel, BlockPos param1BlockPos, Holder<GameEvent> param1Holder, Entity param1Entity1, Entity param1Entity2, float param1Float);
/*     */ 
/*     */ 
/*     */     
/*     */     default TagKey<GameEvent> getListenableEvents() {
/* 459 */       return GameEventTags.VIBRATIONS;
/*     */     }
/*     */     
/*     */     default boolean canTriggerAvoidVibration() {
/* 463 */       return false;
/*     */     }
/*     */     
/*     */     default boolean requiresAdjacentChunksToBeTicking() {
/* 467 */       return false;
/*     */     }
/*     */     
/*     */     default int calculateTravelTimeInTicks(float param1Float) {
/* 471 */       return Mth.floor(param1Float);
/*     */     }
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
/*     */     default boolean isValidVibration(Holder<GameEvent> param1Holder, GameEvent.Context param1Context) {
/* 487 */       if (!param1Holder.is(getListenableEvents())) {
/* 488 */         return false;
/*     */       }
/*     */       
/* 491 */       Entity entity = param1Context.sourceEntity();
/*     */       
/* 493 */       if (entity != null) {
/* 494 */         if (entity.isSpectator()) {
/* 495 */           return false;
/*     */         }
/*     */         
/* 498 */         if (entity.isSteppingCarefully() && param1Holder.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
/* 499 */           if (canTriggerAvoidVibration() && entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 500 */             CriteriaTriggers.AVOID_VIBRATION.trigger(serverPlayer); }
/*     */ 
/*     */           
/* 503 */           return false;
/*     */         } 
/*     */         
/* 506 */         if (entity.dampensVibrations()) {
/* 507 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 511 */       if (param1Context.affectedState() != null) {
/* 512 */         return !param1Context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
/*     */       }
/*     */       
/* 515 */       return true;
/*     */     }
/*     */     
/*     */     default void onDataChanged() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */