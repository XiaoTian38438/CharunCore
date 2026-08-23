/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder<Value>
/*     */ {
/*     */   private final AttributeType<Value> type;
/*     */   private Value defaultValue;
/*  74 */   private AttributeRange<Value> valueRange = AttributeRange.any();
/*     */   private boolean isSyncable = false;
/*     */   private boolean isPositional = true;
/*     */   private boolean isSpatiallyInterpolated = false;
/*     */   
/*     */   public Builder(AttributeType<Value> paramAttributeType) {
/*  80 */     this.type = paramAttributeType;
/*     */   }
/*     */   
/*     */   public Builder<Value> defaultValue(Value paramValue) {
/*  84 */     this.defaultValue = paramValue;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> valueRange(AttributeRange<Value> paramAttributeRange) {
/*  89 */     this.valueRange = paramAttributeRange;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> syncable() {
/*  94 */     this.isSyncable = true;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> notPositional() {
/*  99 */     this.isPositional = false;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<Value> spatiallyInterpolated() {
/* 104 */     this.isSpatiallyInterpolated = true;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public EnvironmentAttribute<Value> build() {
/* 109 */     return new EnvironmentAttribute<>(this.type, 
/*     */         
/* 111 */         Objects.requireNonNull(this.defaultValue, "Missing default value"), this.valueRange, this.isSyncable, this.isPositional, this.isSpatiallyInterpolated);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttribute$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */