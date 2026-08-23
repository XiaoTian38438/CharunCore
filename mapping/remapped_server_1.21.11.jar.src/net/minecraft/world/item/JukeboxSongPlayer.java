/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JukeboxSongPlayer
/*     */ {
/*     */   public static final int PLAY_EVENT_INTERVAL_TICKS = 20;
/*     */   private long ticksSinceSongStarted;
/*     */   private Holder<JukeboxSong> song;
/*     */   private final BlockPos blockPos;
/*     */   private final OnSongChanged onSongChanged;
/*     */   
/*     */   public JukeboxSongPlayer(OnSongChanged paramOnSongChanged, BlockPos paramBlockPos) {
/*  30 */     this.onSongChanged = paramOnSongChanged;
/*  31 */     this.blockPos = paramBlockPos;
/*     */   }
/*     */   
/*     */   public boolean isPlaying() {
/*  35 */     return (this.song != null);
/*     */   }
/*     */   
/*     */   public JukeboxSong getSong() {
/*  39 */     if (this.song == null) {
/*  40 */       return null;
/*     */     }
/*     */     
/*  43 */     return (JukeboxSong)this.song.value();
/*     */   }
/*     */   
/*     */   public long getTicksSinceSongStarted() {
/*  47 */     return this.ticksSinceSongStarted;
/*     */   }
/*     */   
/*     */   public void setSongWithoutPlaying(Holder<JukeboxSong> paramHolder, long paramLong) {
/*  51 */     if (((JukeboxSong)paramHolder.value()).hasFinished(paramLong)) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     this.song = paramHolder;
/*  56 */     this.ticksSinceSongStarted = paramLong;
/*     */   }
/*     */   
/*     */   public void play(LevelAccessor paramLevelAccessor, Holder<JukeboxSong> paramHolder) {
/*  60 */     this.song = paramHolder;
/*  61 */     this.ticksSinceSongStarted = 0L;
/*  62 */     int i = paramLevelAccessor.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG).getId(this.song.value());
/*  63 */     paramLevelAccessor.levelEvent(null, 1010, this.blockPos, i);
/*  64 */     this.onSongChanged.notifyChange();
/*     */   }
/*     */   
/*     */   public void stop(LevelAccessor paramLevelAccessor, BlockState paramBlockState) {
/*  68 */     if (this.song == null) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     this.song = null;
/*  73 */     this.ticksSinceSongStarted = 0L;
/*  74 */     paramLevelAccessor.gameEvent((Holder)GameEvent.JUKEBOX_STOP_PLAY, this.blockPos, GameEvent.Context.of(paramBlockState));
/*  75 */     paramLevelAccessor.levelEvent(1011, this.blockPos, 0);
/*  76 */     this.onSongChanged.notifyChange();
/*     */   }
/*     */   
/*     */   public void tick(LevelAccessor paramLevelAccessor, BlockState paramBlockState) {
/*  80 */     if (this.song == null) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     if (((JukeboxSong)this.song.value()).hasFinished(this.ticksSinceSongStarted)) {
/*  85 */       stop(paramLevelAccessor, paramBlockState);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     if (shouldEmitJukeboxPlayingEvent()) {
/*  90 */       paramLevelAccessor.gameEvent((Holder)GameEvent.JUKEBOX_PLAY, this.blockPos, GameEvent.Context.of(paramBlockState));
/*  91 */       spawnMusicParticles(paramLevelAccessor, this.blockPos);
/*     */     } 
/*     */     
/*  94 */     this.ticksSinceSongStarted++;
/*     */   }
/*     */   
/*     */   private boolean shouldEmitJukeboxPlayingEvent() {
/*  98 */     return (this.ticksSinceSongStarted % 20L == 0L);
/*     */   }
/*     */   
/*     */   private static void spawnMusicParticles(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 102 */     if (paramLevelAccessor instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevelAccessor;
/* 103 */       Vec3 vec3 = Vec3.atBottomCenterOf((Vec3i)paramBlockPos).add(0.0D, 1.2000000476837158D, 0.0D);
/* 104 */       float f = paramLevelAccessor.getRandom().nextInt(4) / 24.0F;
/* 105 */       serverLevel.sendParticles((ParticleOptions)ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), 0, f, 0.0D, 0.0D, 1.0D); }
/*     */   
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OnSongChanged {
/*     */     void notifyChange();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\JukeboxSongPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */