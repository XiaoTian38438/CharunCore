/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Listener
/*     */   implements GameEventListener
/*     */ {
/*     */   private final VibrationSystem system;
/*     */   
/*     */   public Listener(VibrationSystem paramVibrationSystem) {
/* 227 */     this.system = paramVibrationSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionSource getListenerSource() {
/* 232 */     return this.system.getVibrationUser().getPositionSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getListenerRadius() {
/* 237 */     return this.system.getVibrationUser().getListenerRadius();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleGameEvent(ServerLevel paramServerLevel, Holder<GameEvent> paramHolder, GameEvent.Context paramContext, Vec3 paramVec3) {
/* 242 */     VibrationSystem.Data data = this.system.getVibrationData();
/* 243 */     VibrationSystem.User user = this.system.getVibrationUser();
/*     */ 
/*     */     
/* 246 */     if (data.getCurrentVibration() != null) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     if (!user.isValidVibration(paramHolder, paramContext)) {
/* 251 */       return false;
/*     */     }
/*     */     
/* 254 */     Optional<Vec3> optional = user.getPositionSource().getPosition((Level)paramServerLevel);
/*     */     
/* 256 */     if (optional.isEmpty()) {
/* 257 */       return false;
/*     */     }
/*     */     
/* 260 */     Vec3 vec3 = optional.get();
/*     */ 
/*     */     
/* 263 */     if (!user.canReceiveVibration(paramServerLevel, BlockPos.containing((Position)paramVec3), paramHolder, paramContext)) {
/* 264 */       return false;
/*     */     }
/*     */     
/* 267 */     if (isOccluded((Level)paramServerLevel, paramVec3, vec3)) {
/* 268 */       return false;
/*     */     }
/*     */     
/* 271 */     scheduleVibration(paramServerLevel, data, paramHolder, paramContext, paramVec3, vec3);
/*     */     
/* 273 */     return true;
/*     */   }
/*     */   
/*     */   public void forceScheduleVibration(ServerLevel paramServerLevel, Holder<GameEvent> paramHolder, GameEvent.Context paramContext, Vec3 paramVec3) {
/* 277 */     this.system.getVibrationUser().getPositionSource().getPosition((Level)paramServerLevel).ifPresent(paramVec32 -> scheduleVibration(paramServerLevel, this.system.getVibrationData(), paramHolder, paramContext, paramVec31, paramVec32));
/*     */   }
/*     */   
/*     */   private void scheduleVibration(ServerLevel paramServerLevel, VibrationSystem.Data paramData, Holder<GameEvent> paramHolder, GameEvent.Context paramContext, Vec3 paramVec31, Vec3 paramVec32) {
/* 281 */     paramData.selectionStrategy.addCandidate(new VibrationInfo(paramHolder, (float)paramVec31.distanceTo(paramVec32), paramVec31, paramContext.sourceEntity()), paramServerLevel.getGameTime());
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
/*     */   public static float distanceBetweenInBlocks(BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/* 297 */     return (float)Math.sqrt(paramBlockPos1.distSqr((Vec3i)paramBlockPos2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOccluded(Level paramLevel, Vec3 paramVec31, Vec3 paramVec32) {
/* 306 */     Vec3 vec31 = new Vec3(Mth.floor(paramVec31.x) + 0.5D, Mth.floor(paramVec31.y) + 0.5D, Mth.floor(paramVec31.z) + 0.5D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     Vec3 vec32 = new Vec3(Mth.floor(paramVec32.x) + 0.5D, Mth.floor(paramVec32.y) + 0.5D, Mth.floor(paramVec32.z) + 0.5D);
/*     */ 
/*     */     
/* 314 */     for (Direction direction : Direction.values()) {
/* 315 */       Vec3 vec3 = vec31.relative(direction, 9.999999747378752E-6D);
/* 316 */       if (paramLevel.isBlockInLine(new ClipBlockStateContext(vec3, vec32, paramBlockState -> paramBlockState.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
/* 317 */         return false;
/*     */       }
/*     */     } 
/* 320 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Listener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */