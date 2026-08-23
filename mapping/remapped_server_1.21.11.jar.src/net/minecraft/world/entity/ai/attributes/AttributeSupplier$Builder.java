/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 66 */   private final ImmutableMap.Builder<Holder<Attribute>, AttributeInstance> builder = ImmutableMap.builder();
/*    */   private boolean instanceFrozen;
/*    */   
/*    */   private AttributeInstance create(Holder<Attribute> paramHolder) {
/* 70 */     AttributeInstance attributeInstance = new AttributeInstance(paramHolder, paramAttributeInstance -> {
/*    */           if (this.instanceFrozen) {
/*    */             throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + paramHolder.getRegisteredName());
/*    */           }
/*    */         });
/* 75 */     this.builder.put(paramHolder, attributeInstance);
/* 76 */     return attributeInstance;
/*    */   }
/*    */   
/*    */   public Builder add(Holder<Attribute> paramHolder) {
/* 80 */     create(paramHolder);
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public Builder add(Holder<Attribute> paramHolder, double paramDouble) {
/* 85 */     AttributeInstance attributeInstance = create(paramHolder);
/* 86 */     attributeInstance.setBaseValue(paramDouble);
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public AttributeSupplier build() {
/* 91 */     this.instanceFrozen = true;
/* 92 */     return new AttributeSupplier((Map<Holder<Attribute>, AttributeInstance>)this.builder.buildKeepingLast());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\attributes\AttributeSupplier$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */