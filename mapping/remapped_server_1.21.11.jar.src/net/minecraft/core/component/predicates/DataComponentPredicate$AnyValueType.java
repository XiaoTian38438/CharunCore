/*     */ package net.minecraft.core.component.predicates;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnyValueType
/*     */   extends DataComponentPredicate.TypeBase<AnyValue>
/*     */ {
/*     */   private final AnyValue predicate;
/*     */   
/*     */   public AnyValueType(AnyValue paramAnyValue) {
/* 110 */     super(MapCodec.unitCodec(paramAnyValue));
/* 111 */     this.predicate = paramAnyValue;
/*     */   }
/*     */   
/*     */   public AnyValue predicate() {
/* 115 */     return this.predicate;
/*     */   }
/*     */   
/*     */   public DataComponentType<?> componentType() {
/* 119 */     return this.predicate.type();
/*     */   }
/*     */   
/*     */   public static AnyValueType create(DataComponentType<?> paramDataComponentType) {
/* 123 */     return new AnyValueType(new AnyValue(paramDataComponentType));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$AnyValueType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */