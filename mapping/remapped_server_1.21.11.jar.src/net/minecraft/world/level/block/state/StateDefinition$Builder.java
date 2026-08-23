/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder<O, S extends StateHolder<O, S>>
/*     */ {
/*     */   private final O owner;
/* 121 */   private final Map<String, Property<?>> properties = Maps.newHashMap();
/*     */   
/*     */   public Builder(O paramO) {
/* 124 */     this.owner = paramO;
/*     */   }
/*     */   
/*     */   public Builder<O, S> add(Property<?>... paramVarArgs) {
/* 128 */     for (Property<?> property : paramVarArgs) {
/* 129 */       validateProperty(property);
/* 130 */       this.properties.put(property.getName(), property);
/*     */     } 
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> void validateProperty(Property<T> paramProperty) {
/* 136 */     String str = paramProperty.getName();
/* 137 */     if (!StateDefinition.NAME_PATTERN.matcher(str).matches()) {
/* 138 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + String.valueOf(this.owner));
/*     */     }
/*     */     
/* 141 */     List list = paramProperty.getPossibleValues();
/* 142 */     if (list.size() <= 1) {
/* 143 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + String.valueOf(this.owner) + " with <= 1 possible values");
/*     */     }
/*     */     
/* 146 */     for (Comparable comparable : list) {
/* 147 */       String str1 = paramProperty.getName(comparable);
/* 148 */       if (!StateDefinition.NAME_PATTERN.matcher(str1).matches()) {
/* 149 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + String.valueOf(this.owner) + " with invalidly named value: " + str);
/*     */       }
/*     */     } 
/*     */     
/* 153 */     if (this.properties.containsKey(str)) {
/* 154 */       throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + String.valueOf(this.owner));
/*     */     }
/*     */   }
/*     */   
/*     */   public StateDefinition<O, S> create(Function<O, S> paramFunction, StateDefinition.Factory<O, S> paramFactory) {
/* 159 */     return new StateDefinition<>(paramFunction, this.owner, paramFactory, this.properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\StateDefinition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */