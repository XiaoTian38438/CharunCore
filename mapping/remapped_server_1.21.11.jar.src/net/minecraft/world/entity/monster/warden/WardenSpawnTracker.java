/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WardenSpawnTracker {
/*     */   static {
/*  20 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks_since_last_warning").orElse(Integer.valueOf(0)).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("warning_level").orElse(Integer.valueOf(0)).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("cooldown_ticks").orElse(Integer.valueOf(0)).forGetter(())).apply((Applicative)paramInstance, WardenSpawnTracker::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<WardenSpawnTracker> CODEC;
/*     */   
/*     */   public static final int MAX_WARNING_LEVEL = 4;
/*     */   
/*     */   private static final double PLAYER_SEARCH_RADIUS = 16.0D;
/*     */   
/*     */   private static final int WARNING_CHECK_DIAMETER = 48;
/*     */   private static final int DECREASE_WARNING_LEVEL_EVERY_INTERVAL = 12000;
/*     */   private static final int WARNING_LEVEL_INCREASE_COOLDOWN = 200;
/*     */   private int ticksSinceLastWarning;
/*     */   private int warningLevel;
/*     */   private int cooldownTicks;
/*     */   
/*     */   public WardenSpawnTracker(int paramInt1, int paramInt2, int paramInt3) {
/*  38 */     this.ticksSinceLastWarning = paramInt1;
/*  39 */     this.warningLevel = paramInt2;
/*  40 */     this.cooldownTicks = paramInt3;
/*     */   }
/*     */   
/*     */   public WardenSpawnTracker() {
/*  44 */     this(0, 0, 0);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  48 */     if (this.ticksSinceLastWarning >= 12000) {
/*  49 */       decreaseWarningLevel();
/*  50 */       this.ticksSinceLastWarning = 0;
/*     */     } else {
/*  52 */       this.ticksSinceLastWarning++;
/*     */     } 
/*     */     
/*  55 */     if (this.cooldownTicks > 0) {
/*  56 */       this.cooldownTicks--;
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/*  61 */     this.ticksSinceLastWarning = 0;
/*  62 */     this.warningLevel = 0;
/*  63 */     this.cooldownTicks = 0;
/*     */   }
/*     */   
/*     */   public static OptionalInt tryWarn(ServerLevel paramServerLevel, BlockPos paramBlockPos, ServerPlayer paramServerPlayer) {
/*  67 */     if (hasNearbyWarden(paramServerLevel, paramBlockPos)) {
/*  68 */       return OptionalInt.empty();
/*     */     }
/*     */     
/*  71 */     List<ServerPlayer> list = getNearbyPlayers(paramServerLevel, paramBlockPos);
/*     */     
/*  73 */     if (!list.contains(paramServerPlayer)) {
/*  74 */       list.add(paramServerPlayer);
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (list.stream().anyMatch(paramServerPlayer -> ((Boolean)paramServerPlayer.getWardenSpawnTracker().map(WardenSpawnTracker::onCooldown).orElse(Boolean.valueOf(false))).booleanValue())) {
/*  79 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     Optional<WardenSpawnTracker> optional = list.stream().flatMap(paramServerPlayer -> paramServerPlayer.getWardenSpawnTracker().stream()).max(Comparator.comparingInt(WardenSpawnTracker::getWarningLevel));
/*     */     
/*  87 */     if (optional.isPresent()) {
/*  88 */       WardenSpawnTracker wardenSpawnTracker = optional.get();
/*     */       
/*  90 */       wardenSpawnTracker.increaseWarningLevel();
/*     */ 
/*     */       
/*  93 */       list.forEach(paramServerPlayer -> paramServerPlayer.getWardenSpawnTracker().ifPresent(()));
/*     */       
/*  95 */       return OptionalInt.of(wardenSpawnTracker.warningLevel);
/*     */     } 
/*  97 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean onCooldown() {
/* 102 */     return (this.cooldownTicks > 0);
/*     */   }
/*     */   
/*     */   private static boolean hasNearbyWarden(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 106 */     AABB aABB = AABB.ofSize(Vec3.atCenterOf((Vec3i)paramBlockPos), 48.0D, 48.0D, 48.0D);
/* 107 */     return !paramServerLevel.getEntitiesOfClass(Warden.class, aABB).isEmpty();
/*     */   }
/*     */   
/*     */   private static List<ServerPlayer> getNearbyPlayers(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 111 */     Vec3 vec3 = Vec3.atCenterOf((Vec3i)paramBlockPos);
/*     */     
/* 113 */     return paramServerLevel.getPlayers(paramServerPlayer -> 
/* 114 */         (!paramServerPlayer.isSpectator() && paramServerPlayer.position().closerThan((Position)paramVec3, 16.0D) && paramServerPlayer.isAlive()));
/*     */   }
/*     */ 
/*     */   
/*     */   private void increaseWarningLevel() {
/* 119 */     if (!onCooldown()) {
/* 120 */       this.ticksSinceLastWarning = 0;
/* 121 */       this.cooldownTicks = 200;
/* 122 */       setWarningLevel(getWarningLevel() + 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decreaseWarningLevel() {
/* 127 */     setWarningLevel(getWarningLevel() - 1);
/*     */   }
/*     */   
/*     */   public void setWarningLevel(int paramInt) {
/* 131 */     this.warningLevel = Mth.clamp(paramInt, 0, 4);
/*     */   }
/*     */   
/*     */   public int getWarningLevel() {
/* 135 */     return this.warningLevel;
/*     */   }
/*     */   
/*     */   private void copyData(WardenSpawnTracker paramWardenSpawnTracker) {
/* 139 */     this.warningLevel = paramWardenSpawnTracker.warningLevel;
/* 140 */     this.cooldownTicks = paramWardenSpawnTracker.cooldownTicks;
/* 141 */     this.ticksSinceLastWarning = paramWardenSpawnTracker.ticksSinceLastWarning;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\warden\WardenSpawnTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */