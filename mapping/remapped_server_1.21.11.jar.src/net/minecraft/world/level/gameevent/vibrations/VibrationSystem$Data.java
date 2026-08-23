/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.ExtraCodecs;
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
/*     */ public final class Data
/*     */ {
/*     */   public static Codec<Data> CODEC;
/*     */   public static final String NBT_TAG_KEY = "listener";
/*     */   VibrationInfo currentVibration;
/*     */   private int travelTimeInTicks;
/*     */   final VibrationSelector selectionStrategy;
/*     */   private boolean reloadVibrationParticle;
/*     */   
/*     */   static {
/* 161 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)VibrationSelector.CODEC.fieldOf("selector").forGetter(Data::getSelectionStrategy), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(Integer.valueOf(0)).forGetter(Data::getTravelTimeInTicks)).apply((Applicative)paramInstance, ()));
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
/*     */   private Data(VibrationInfo paramVibrationInfo, VibrationSelector paramVibrationSelector, int paramInt, boolean paramBoolean) {
/* 175 */     this.currentVibration = paramVibrationInfo;
/* 176 */     this.travelTimeInTicks = paramInt;
/* 177 */     this.selectionStrategy = paramVibrationSelector;
/* 178 */     this.reloadVibrationParticle = paramBoolean;
/*     */   }
/*     */   
/*     */   public Data() {
/* 182 */     this(null, new VibrationSelector(), 0, false);
/*     */   }
/*     */   
/*     */   public VibrationSelector getSelectionStrategy() {
/* 186 */     return this.selectionStrategy;
/*     */   }
/*     */   
/*     */   public VibrationInfo getCurrentVibration() {
/* 190 */     return this.currentVibration;
/*     */   }
/*     */   
/*     */   public void setCurrentVibration(VibrationInfo paramVibrationInfo) {
/* 194 */     this.currentVibration = paramVibrationInfo;
/*     */   }
/*     */   
/*     */   public int getTravelTimeInTicks() {
/* 198 */     return this.travelTimeInTicks;
/*     */   }
/*     */   
/*     */   public void setTravelTimeInTicks(int paramInt) {
/* 202 */     this.travelTimeInTicks = paramInt;
/*     */   }
/*     */   
/*     */   public void decrementTravelTime() {
/* 206 */     this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1);
/*     */   }
/*     */   
/*     */   public boolean shouldReloadVibrationParticle() {
/* 210 */     return this.reloadVibrationParticle;
/*     */   }
/*     */   
/*     */   public void setReloadVibrationParticle(boolean paramBoolean) {
/* 214 */     this.reloadVibrationParticle = paramBoolean;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Data.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */