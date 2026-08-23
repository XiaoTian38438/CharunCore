/*     */ package net.minecraft.world.timeline;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Timeline {
/*  26 */   public static final Codec<Holder<Timeline>> CODEC = (Codec<Holder<Timeline>>)RegistryFixedCodec.create(Registries.TIMELINE);
/*     */   
/*  28 */   private static final Codec<Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>> TRACKS_CODEC = Codec.dispatchedMap(EnvironmentAttributes.CODEC, Util.memoize(AttributeTrack::createCodec));
/*     */   
/*     */   public static final Codec<Timeline> DIRECT_CODEC;
/*     */   
/*     */   static {
/*  33 */     DIRECT_CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("period_ticks").forGetter(()), (App)TRACKS_CODEC.optionalFieldOf("tracks", Map.of()).forGetter(())).apply((Applicative)paramInstance, Timeline::new)).validate(Timeline::validateInternal);
/*     */   }
/*  35 */   public static final Codec<Timeline> NETWORK_CODEC = DIRECT_CODEC.xmap(Timeline::filterSyncableTracks, Timeline::filterSyncableTracks); private final Optional<Integer> periodTicks; private final Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks;
/*     */   
/*     */   private static Timeline filterSyncableTracks(Timeline paramTimeline) {
/*  38 */     Map<?, ?> map = Map.copyOf(Maps.filterKeys(paramTimeline.tracks, EnvironmentAttribute::isSyncable));
/*  39 */     return new Timeline(paramTimeline.periodTicks, (Map)map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Timeline(Optional<Integer> paramOptional, Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> paramMap) {
/*  49 */     this.periodTicks = paramOptional;
/*  50 */     this.tracks = paramMap;
/*     */   }
/*     */   
/*     */   private static DataResult<Timeline> validateInternal(Timeline paramTimeline) {
/*  54 */     if (paramTimeline.periodTicks.isEmpty()) {
/*  55 */       return DataResult.success(paramTimeline);
/*     */     }
/*  57 */     int i = ((Integer)paramTimeline.periodTicks.get()).intValue();
/*  58 */     DataResult<Timeline> dataResult = DataResult.success(paramTimeline);
/*  59 */     for (AttributeTrack<?, ?> attributeTrack : paramTimeline.tracks.values()) {
/*  60 */       dataResult = dataResult.apply2stable((paramTimeline, paramAttributeTrack) -> paramTimeline, 
/*     */           
/*  62 */           AttributeTrack.validatePeriod(attributeTrack, i));
/*     */     }
/*     */     
/*  65 */     return dataResult;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  69 */     return new Builder();
/*     */   }
/*     */   
/*     */   public long getCurrentTicks(Level paramLevel) {
/*  73 */     long l = getTotalTicks(paramLevel);
/*  74 */     if (this.periodTicks.isEmpty()) {
/*  75 */       return l;
/*     */     }
/*  77 */     return l % ((Integer)this.periodTicks.get()).intValue();
/*     */   }
/*     */   
/*     */   public long getTotalTicks(Level paramLevel) {
/*  81 */     return paramLevel.getDayTime();
/*     */   }
/*     */   
/*     */   public Optional<Integer> periodTicks() {
/*  85 */     return this.periodTicks;
/*     */   }
/*     */   
/*     */   public Set<EnvironmentAttribute<?>> attributes() {
/*  89 */     return this.tracks.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public <Value> AttributeTrackSampler<Value, ?> createTrackSampler(EnvironmentAttribute<Value> paramEnvironmentAttribute, LongSupplier paramLongSupplier) {
/*  94 */     AttributeTrack<Value, ?> attributeTrack = (AttributeTrack)this.tracks.get(paramEnvironmentAttribute);
/*  95 */     if (attributeTrack == null) {
/*  96 */       throw new IllegalStateException("Timeline has no track for " + String.valueOf(paramEnvironmentAttribute));
/*     */     }
/*  98 */     return attributeTrack.bakeSampler(paramEnvironmentAttribute, this.periodTicks, paramLongSupplier);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 102 */     private Optional<Integer> periodTicks = Optional.empty();
/* 103 */     private final ImmutableMap.Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPeriodTicks(int param1Int) {
/* 109 */       this.periodTicks = Optional.of(Integer.valueOf(param1Int));
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public <Value, Argument> Builder addModifierTrack(EnvironmentAttribute<Value> param1EnvironmentAttribute, AttributeModifier<Value, Argument> param1AttributeModifier, Consumer<KeyframeTrack.Builder<Argument>> param1Consumer) {
/* 114 */       param1EnvironmentAttribute.type().checkAllowedModifier(param1AttributeModifier);
/* 115 */       KeyframeTrack.Builder<Argument> builder = new KeyframeTrack.Builder();
/* 116 */       param1Consumer.accept(builder);
/* 117 */       this.tracks.put(param1EnvironmentAttribute, new AttributeTrack<>(param1AttributeModifier, builder.build()));
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public <Value> Builder addTrack(EnvironmentAttribute<Value> param1EnvironmentAttribute, Consumer<KeyframeTrack.Builder<Value>> param1Consumer) {
/* 122 */       return addModifierTrack(param1EnvironmentAttribute, AttributeModifier.override(), param1Consumer);
/*     */     }
/*     */     
/*     */     public Timeline build() {
/* 126 */       return new Timeline(this.periodTicks, (Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>)this.tracks.build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\timeline\Timeline.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */