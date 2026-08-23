/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class VibrationSelector {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)Codec.LONG.fieldOf("tick").forGetter(())).apply((Applicative)paramInstance, VibrationSelector::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<VibrationSelector> CODEC;
/*    */   private Optional<Pair<VibrationInfo, Long>> currentVibrationData;
/*    */   
/*    */   public VibrationSelector(Optional<VibrationInfo> paramOptional, long paramLong) {
/* 18 */     this.currentVibrationData = paramOptional.map(paramVibrationInfo -> Pair.of(paramVibrationInfo, Long.valueOf(paramLong)));
/*    */   }
/*    */   
/*    */   public VibrationSelector() {
/* 22 */     this.currentVibrationData = Optional.empty();
/*    */   }
/*    */   
/*    */   public void addCandidate(VibrationInfo paramVibrationInfo, long paramLong) {
/* 26 */     if (shouldReplaceVibration(paramVibrationInfo, paramLong)) {
/* 27 */       this.currentVibrationData = Optional.of(Pair.of(paramVibrationInfo, Long.valueOf(paramLong)));
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean shouldReplaceVibration(VibrationInfo paramVibrationInfo, long paramLong) {
/* 32 */     if (this.currentVibrationData.isEmpty()) {
/* 33 */       return true;
/*    */     }
/* 35 */     Pair pair = this.currentVibrationData.get();
/* 36 */     long l = ((Long)pair.getRight()).longValue();
/* 37 */     if (paramLong != l)
/*    */     {
/* 39 */       return false;
/*    */     }
/* 41 */     VibrationInfo vibrationInfo = (VibrationInfo)pair.getLeft();
/* 42 */     if (paramVibrationInfo.distance() < vibrationInfo.distance())
/* 43 */       return true; 
/* 44 */     if (paramVibrationInfo.distance() > vibrationInfo.distance()) {
/* 45 */       return false;
/*    */     }
/* 47 */     return (VibrationSystem.getGameEventFrequency(paramVibrationInfo.gameEvent()) > VibrationSystem.getGameEventFrequency(vibrationInfo.gameEvent()));
/*    */   }
/*    */   
/*    */   public Optional<VibrationInfo> chosenCandidate(long paramLong) {
/* 51 */     if (this.currentVibrationData.isEmpty()) {
/* 52 */       return Optional.empty();
/*    */     }
/* 54 */     if (((Long)((Pair)this.currentVibrationData.get()).getRight()).longValue() < paramLong) {
/* 55 */       return Optional.of((VibrationInfo)((Pair)this.currentVibrationData.get()).getLeft());
/*    */     }
/* 57 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public void startOver() {
/* 61 */     this.currentVibrationData = Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */