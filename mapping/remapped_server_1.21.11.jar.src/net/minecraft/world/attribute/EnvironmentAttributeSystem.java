/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class EnvironmentAttributeSystem
/*     */   implements EnvironmentAttributeReader
/*     */ {
/*  27 */   private final Map<EnvironmentAttribute<?>, ValueSampler<?>> attributeSamplers = (Map<EnvironmentAttribute<?>, ValueSampler<?>>)new Reference2ObjectOpenHashMap();
/*     */   
/*     */   EnvironmentAttributeSystem(Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> paramMap) {
/*  30 */     paramMap.forEach((paramEnvironmentAttribute, paramList) -> this.attributeSamplers.put(paramEnvironmentAttribute, bakeLayerSampler(paramEnvironmentAttribute, paramList)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <Value> ValueSampler<Value> bakeLayerSampler(EnvironmentAttribute<Value> paramEnvironmentAttribute, List<? extends EnvironmentAttributeLayer<?>> paramList) {
/*  37 */     ArrayList<EnvironmentAttributeLayer<?>> arrayList = new ArrayList<>(paramList);
/*     */     
/*  39 */     Value value = paramEnvironmentAttribute.defaultValue();
/*  40 */     while (!arrayList.isEmpty()) { EnvironmentAttributeLayer.Constant constant = (EnvironmentAttributeLayer.Constant)arrayList.getFirst(); if (constant instanceof EnvironmentAttributeLayer.Constant) { EnvironmentAttributeLayer.Constant<Value> constant1 = constant;
/*  41 */         value = constant1.applyConstant(value);
/*  42 */         arrayList.removeFirst(); }
/*     */        }
/*     */     
/*  45 */     boolean bool = arrayList.stream().anyMatch(paramEnvironmentAttributeLayer -> paramEnvironmentAttributeLayer instanceof EnvironmentAttributeLayer.Positional);
/*     */     
/*  47 */     return new ValueSampler<>(paramEnvironmentAttribute, value, (List)List.copyOf(arrayList), bool);
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  51 */     return new Builder();
/*     */   }
/*     */   
/*     */   static void addDefaultLayers(Builder paramBuilder, Level paramLevel) {
/*  55 */     RegistryAccess registryAccess = paramLevel.registryAccess();
/*  56 */     BiomeManager biomeManager = paramLevel.getBiomeManager();
/*  57 */     Objects.requireNonNull(paramLevel); LongSupplier longSupplier = paramLevel::getDayTime;
/*  58 */     addDimensionLayer(paramBuilder, paramLevel.dimensionType());
/*  59 */     addBiomeLayer(paramBuilder, (HolderLookup<Biome>)registryAccess.lookupOrThrow(Registries.BIOME), biomeManager);
/*  60 */     paramLevel.dimensionType().timelines().forEach(paramHolder -> paramBuilder.addTimelineLayer(paramHolder, paramLongSupplier));
/*  61 */     if (paramLevel.canHaveWeather()) {
/*  62 */       WeatherAttributes.addBuiltinLayers(paramBuilder, WeatherAttributes.WeatherAccess.from(paramLevel));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addDimensionLayer(Builder paramBuilder, DimensionType paramDimensionType) {
/*  67 */     paramBuilder.addConstantLayer(paramDimensionType.attributes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addBiomeLayer(Builder paramBuilder, HolderLookup<Biome> paramHolderLookup, BiomeManager paramBiomeManager) {
/*  75 */     Stream stream = paramHolderLookup.listElements().flatMap(paramReference -> ((Biome)paramReference.value()).getAttributes().keySet().stream()).distinct();
/*  76 */     stream.forEach(paramEnvironmentAttribute -> addBiomeLayerForAttribute(paramBuilder, paramEnvironmentAttribute, paramBiomeManager));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <Value> void addBiomeLayerForAttribute(Builder paramBuilder, EnvironmentAttribute<Value> paramEnvironmentAttribute, BiomeManager paramBiomeManager) {
/*  82 */     paramBuilder.addPositionalLayer(paramEnvironmentAttribute, (paramObject, paramVec3, paramSpatialAttributeInterpolator) -> {
/*     */           if (paramSpatialAttributeInterpolator != null && paramEnvironmentAttribute.isSpatiallyInterpolated()) {
/*     */             return paramSpatialAttributeInterpolator.applyAttributeLayer(paramEnvironmentAttribute, paramObject);
/*     */           }
/*     */           Holder holder = paramBiomeManager.getNoiseBiomeAtPosition(paramVec3.x, paramVec3.y, paramVec3.z);
/*     */           return ((Biome)holder.value()).getAttributes().applyModifier(paramEnvironmentAttribute, paramObject);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateTickCache() {
/*  93 */     this.attributeSamplers.values().forEach(ValueSampler::invalidateTickCache);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <Value> ValueSampler<Value> getValueSampler(EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/*  99 */     return (ValueSampler<Value>)this.attributeSamplers.get(paramEnvironmentAttribute);
/*     */   }
/*     */ 
/*     */   
/*     */   public <Value> Value getDimensionValue(EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/* 104 */     if (SharedConstants.IS_RUNNING_IN_IDE && paramEnvironmentAttribute.isPositional()) {
/* 105 */       throw new IllegalStateException("Position must always be provided for positional attribute " + String.valueOf(paramEnvironmentAttribute));
/*     */     }
/* 107 */     ValueSampler<Value> valueSampler = getValueSampler(paramEnvironmentAttribute);
/* 108 */     if (valueSampler == null) {
/* 109 */       return paramEnvironmentAttribute.defaultValue();
/*     */     }
/* 111 */     return valueSampler.getDimensionValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator) {
/* 116 */     ValueSampler<Value> valueSampler = getValueSampler(paramEnvironmentAttribute);
/* 117 */     if (valueSampler == null) {
/* 118 */       return paramEnvironmentAttribute.defaultValue();
/*     */     }
/* 120 */     return valueSampler.getValue(paramVec3, paramSpatialAttributeInterpolator);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   <Value> Value getConstantBaseValue(EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/* 125 */     ValueSampler<Value> valueSampler = getValueSampler(paramEnvironmentAttribute);
/* 126 */     return (valueSampler != null) ? valueSampler.baseValue : paramEnvironmentAttribute.defaultValue();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isAffectedByPosition(EnvironmentAttribute<?> paramEnvironmentAttribute) {
/* 131 */     ValueSampler<?> valueSampler = getValueSampler(paramEnvironmentAttribute);
/* 132 */     return (valueSampler != null && valueSampler.isAffectedByPosition);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 136 */     private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addDefaultLayers(Level param1Level) {
/* 142 */       EnvironmentAttributeSystem.addDefaultLayers(this, param1Level);
/* 143 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addConstantLayer(EnvironmentAttributeMap param1EnvironmentAttributeMap) {
/* 147 */       for (EnvironmentAttribute<?> environmentAttribute : param1EnvironmentAttributeMap.keySet()) {
/* 148 */         addConstantEntry(environmentAttribute, param1EnvironmentAttributeMap);
/*     */       }
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     private <Value> Builder addConstantEntry(EnvironmentAttribute<Value> param1EnvironmentAttribute, EnvironmentAttributeMap param1EnvironmentAttributeMap) {
/* 154 */       EnvironmentAttributeMap.Entry<Value, ?> entry = param1EnvironmentAttributeMap.get(param1EnvironmentAttribute);
/* 155 */       if (entry == null) {
/* 156 */         throw new IllegalArgumentException("Missing attribute " + String.valueOf(param1EnvironmentAttribute));
/*     */       }
/* 158 */       Objects.requireNonNull(entry); return addConstantLayer(param1EnvironmentAttribute, entry::applyModifier);
/*     */     }
/*     */     
/*     */     public <Value> Builder addConstantLayer(EnvironmentAttribute<Value> param1EnvironmentAttribute, EnvironmentAttributeLayer.Constant<Value> param1Constant) {
/* 162 */       return addLayer(param1EnvironmentAttribute, param1Constant);
/*     */     }
/*     */     
/*     */     public <Value> Builder addTimeBasedLayer(EnvironmentAttribute<Value> param1EnvironmentAttribute, EnvironmentAttributeLayer.TimeBased<Value> param1TimeBased) {
/* 166 */       return addLayer(param1EnvironmentAttribute, param1TimeBased);
/*     */     }
/*     */     
/*     */     public <Value> Builder addPositionalLayer(EnvironmentAttribute<Value> param1EnvironmentAttribute, EnvironmentAttributeLayer.Positional<Value> param1Positional) {
/* 170 */       return addLayer(param1EnvironmentAttribute, param1Positional);
/*     */     }
/*     */     
/*     */     private <Value> Builder addLayer(EnvironmentAttribute<Value> param1EnvironmentAttribute, EnvironmentAttributeLayer<Value> param1EnvironmentAttributeLayer) {
/* 174 */       ((List<EnvironmentAttributeLayer<Value>>)this.layersByAttribute.computeIfAbsent(param1EnvironmentAttribute, param1EnvironmentAttribute -> new ArrayList())).add(param1EnvironmentAttributeLayer);
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addTimelineLayer(Holder<Timeline> param1Holder, LongSupplier param1LongSupplier) {
/* 179 */       for (EnvironmentAttribute<?> environmentAttribute : (Iterable<EnvironmentAttribute<?>>)((Timeline)param1Holder.value()).attributes()) {
/* 180 */         addTimelineLayerForAttribute(param1Holder, environmentAttribute, param1LongSupplier);
/*     */       }
/* 182 */       return this;
/*     */     }
/*     */     
/*     */     private <Value> void addTimelineLayerForAttribute(Holder<Timeline> param1Holder, EnvironmentAttribute<Value> param1EnvironmentAttribute, LongSupplier param1LongSupplier) {
/* 186 */       addTimeBasedLayer(param1EnvironmentAttribute, (EnvironmentAttributeLayer.TimeBased<Value>)((Timeline)param1Holder.value()).createTrackSampler(param1EnvironmentAttribute, param1LongSupplier));
/*     */     }
/*     */     
/*     */     public EnvironmentAttributeSystem build() {
/* 190 */       return new EnvironmentAttributeSystem(this.layersByAttribute);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueSampler<Value>
/*     */   {
/*     */     private final EnvironmentAttribute<Value> attribute;
/*     */     final Value baseValue;
/*     */     private final List<EnvironmentAttributeLayer<Value>> layers;
/*     */     final boolean isAffectedByPosition;
/*     */     private Value cachedTickValue;
/*     */     private int cacheTickId;
/*     */     
/*     */     ValueSampler(EnvironmentAttribute<Value> param1EnvironmentAttribute, Value param1Value, List<EnvironmentAttributeLayer<Value>> param1List, boolean param1Boolean) {
/* 204 */       this.attribute = param1EnvironmentAttribute;
/* 205 */       this.baseValue = param1Value;
/* 206 */       this.layers = param1List;
/* 207 */       this.isAffectedByPosition = param1Boolean;
/*     */     }
/*     */     
/*     */     public void invalidateTickCache() {
/* 211 */       this.cachedTickValue = null;
/* 212 */       this.cacheTickId++;
/*     */     }
/*     */     
/*     */     public Value getDimensionValue() {
/* 216 */       if (this.cachedTickValue != null) {
/* 217 */         return this.cachedTickValue;
/*     */       }
/* 219 */       Value value = computeValueNotPositional();
/* 220 */       this.cachedTickValue = value;
/* 221 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Value getValue(Vec3 param1Vec3, SpatialAttributeInterpolator param1SpatialAttributeInterpolator) {
/* 226 */       if (!this.isAffectedByPosition) {
/* 227 */         return getDimensionValue();
/*     */       }
/* 229 */       return computeValuePositional(param1Vec3, param1SpatialAttributeInterpolator);
/*     */     }
/*     */     
/*     */     private Value computeValuePositional(Vec3 param1Vec3, SpatialAttributeInterpolator param1SpatialAttributeInterpolator) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield baseValue : Ljava/lang/Object;
/*     */       //   4: astore_3
/*     */       //   5: aload_0
/*     */       //   6: getfield layers : Ljava/util/List;
/*     */       //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   14: astore #4
/*     */       //   16: aload #4
/*     */       //   18: invokeinterface hasNext : ()Z
/*     */       //   23: ifeq -> 170
/*     */       //   26: aload #4
/*     */       //   28: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   33: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */       //   36: astore #5
/*     */       //   38: aload #5
/*     */       //   40: dup
/*     */       //   41: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   44: pop
/*     */       //   45: astore #6
/*     */       //   47: iconst_0
/*     */       //   48: istore #7
/*     */       //   50: aload #6
/*     */       //   52: iload #7
/*     */       //   54: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   59: tableswitch default -> 84, 0 -> 94, 1 -> 115, 2 -> 140
/*     */       //   84: new java/lang/MatchException
/*     */       //   87: dup
/*     */       //   88: aconst_null
/*     */       //   89: aconst_null
/*     */       //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   93: athrow
/*     */       //   94: aload #6
/*     */       //   96: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */       //   99: astore #8
/*     */       //   101: aload #8
/*     */       //   103: aload_3
/*     */       //   104: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   109: checkcast java/lang/Object
/*     */       //   112: goto -> 166
/*     */       //   115: aload #6
/*     */       //   117: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */       //   120: astore #9
/*     */       //   122: aload #9
/*     */       //   124: aload_3
/*     */       //   125: aload_0
/*     */       //   126: getfield cacheTickId : I
/*     */       //   129: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */       //   134: checkcast java/lang/Object
/*     */       //   137: goto -> 166
/*     */       //   140: aload #6
/*     */       //   142: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */       //   145: astore #10
/*     */       //   147: aload #10
/*     */       //   149: aload_3
/*     */       //   150: aload_1
/*     */       //   151: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   154: checkcast net/minecraft/world/phys/Vec3
/*     */       //   157: aload_2
/*     */       //   158: invokeinterface applyPositional : (Ljava/lang/Object;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;)Ljava/lang/Object;
/*     */       //   163: checkcast java/lang/Object
/*     */       //   166: astore_3
/*     */       //   167: goto -> 16
/*     */       //   170: aload_0
/*     */       //   171: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */       //   174: aload_3
/*     */       //   175: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   178: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #233	-> 0
/*     */       //   #234	-> 5
/*     */       //   #235	-> 38
/*     */       //   #236	-> 94
/*     */       //   #237	-> 115
/*     */       //   #238	-> 140
/*     */       //   #240	-> 167
/*     */       //   #241	-> 170
/*     */     }
/*     */     
/*     */     private Value computeValueNotPositional() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield baseValue : Ljava/lang/Object;
/*     */       //   4: astore_1
/*     */       //   5: aload_0
/*     */       //   6: getfield layers : Ljava/util/List;
/*     */       //   9: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   14: astore_2
/*     */       //   15: aload_2
/*     */       //   16: invokeinterface hasNext : ()Z
/*     */       //   21: ifeq -> 151
/*     */       //   24: aload_2
/*     */       //   25: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   30: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer
/*     */       //   33: astore_3
/*     */       //   34: aload_3
/*     */       //   35: dup
/*     */       //   36: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   39: pop
/*     */       //   40: astore #4
/*     */       //   42: iconst_0
/*     */       //   43: istore #5
/*     */       //   45: aload #4
/*     */       //   47: iload #5
/*     */       //   49: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   54: tableswitch default -> 80, 0 -> 90, 1 -> 111, 2 -> 136
/*     */       //   80: new java/lang/MatchException
/*     */       //   83: dup
/*     */       //   84: aconst_null
/*     */       //   85: aconst_null
/*     */       //   86: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   89: athrow
/*     */       //   90: aload #4
/*     */       //   92: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Constant
/*     */       //   95: astore #6
/*     */       //   97: aload #6
/*     */       //   99: aload_1
/*     */       //   100: invokeinterface applyConstant : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   105: checkcast java/lang/Object
/*     */       //   108: goto -> 147
/*     */       //   111: aload #4
/*     */       //   113: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$TimeBased
/*     */       //   116: astore #7
/*     */       //   118: aload #7
/*     */       //   120: aload_1
/*     */       //   121: aload_0
/*     */       //   122: getfield cacheTickId : I
/*     */       //   125: invokeinterface applyTimeBased : (Ljava/lang/Object;I)Ljava/lang/Object;
/*     */       //   130: checkcast java/lang/Object
/*     */       //   133: goto -> 147
/*     */       //   136: aload #4
/*     */       //   138: checkcast net/minecraft/world/attribute/EnvironmentAttributeLayer$Positional
/*     */       //   141: astore #8
/*     */       //   143: aload_1
/*     */       //   144: checkcast java/lang/Object
/*     */       //   147: astore_1
/*     */       //   148: goto -> 15
/*     */       //   151: aload_0
/*     */       //   152: getfield attribute : Lnet/minecraft/world/attribute/EnvironmentAttribute;
/*     */       //   155: aload_1
/*     */       //   156: invokevirtual sanitizeValue : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   159: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #245	-> 0
/*     */       //   #246	-> 5
/*     */       //   #247	-> 34
/*     */       //   #248	-> 90
/*     */       //   #249	-> 111
/*     */       //   #251	-> 136
/*     */       //   #253	-> 148
/*     */       //   #254	-> 151
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */