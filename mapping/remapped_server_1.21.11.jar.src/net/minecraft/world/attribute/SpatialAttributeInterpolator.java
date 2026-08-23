/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2DoubleMaps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpatialAttributeInterpolator
/*    */ {
/* 16 */   private final Reference2DoubleArrayMap<EnvironmentAttributeMap> weightsBySource = new Reference2DoubleArrayMap();
/*    */   
/*    */   public void clear() {
/* 19 */     this.weightsBySource.clear();
/*    */   }
/*    */   
/*    */   public SpatialAttributeInterpolator accumulate(double paramDouble, EnvironmentAttributeMap paramEnvironmentAttributeMap) {
/* 23 */     this.weightsBySource.mergeDouble(paramEnvironmentAttributeMap, paramDouble, Double::sum);
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public <Value> Value applyAttributeLayer(EnvironmentAttribute<Value> paramEnvironmentAttribute, Value paramValue) {
/* 28 */     if (this.weightsBySource.isEmpty())
/* 29 */       return paramValue; 
/* 30 */     if (this.weightsBySource.size() == 1) {
/* 31 */       EnvironmentAttributeMap environmentAttributeMap = (EnvironmentAttributeMap)this.weightsBySource.keySet().iterator().next();
/* 32 */       return environmentAttributeMap.applyModifier(paramEnvironmentAttribute, paramValue);
/*    */     } 
/*    */     
/* 35 */     LerpFunction<Object> lerpFunction = paramEnvironmentAttribute.type().spatialLerp();
/* 36 */     Value value = null;
/*    */ 
/*    */     
/* 39 */     double d = 0.0D;
/* 40 */     for (ObjectIterator<Reference2DoubleMap.Entry> objectIterator = Reference2DoubleMaps.fastIterable((Reference2DoubleMap)this.weightsBySource).iterator(); objectIterator.hasNext(); ) { Reference2DoubleMap.Entry entry = objectIterator.next();
/* 41 */       EnvironmentAttributeMap environmentAttributeMap = (EnvironmentAttributeMap)entry.getKey();
/* 42 */       double d1 = entry.getDoubleValue();
/* 43 */       Value value1 = (Value)environmentAttributeMap.applyModifier((EnvironmentAttribute)paramEnvironmentAttribute, (Object)paramValue);
/* 44 */       d += d1;
/* 45 */       if (value == null) {
/* 46 */         value = value1; continue;
/*    */       } 
/* 48 */       float f = (float)(d1 / d);
/* 49 */       value = (Value)lerpFunction.apply(f, (Object)value, (Object)value1); }
/*    */ 
/*    */ 
/*    */     
/* 53 */     return Objects.requireNonNull(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\SpatialAttributeInterpolator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */