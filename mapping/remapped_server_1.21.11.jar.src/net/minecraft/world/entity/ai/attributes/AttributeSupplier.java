/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ public class AttributeSupplier
/*    */ {
/*    */   private final Map<Holder<Attribute>, AttributeInstance> instances;
/*    */   
/*    */   AttributeSupplier(Map<Holder<Attribute>, AttributeInstance> paramMap) {
/* 15 */     this.instances = paramMap;
/*    */   }
/*    */   
/*    */   private AttributeInstance getAttributeInstance(Holder<Attribute> paramHolder) {
/* 19 */     AttributeInstance attributeInstance = this.instances.get(paramHolder);
/* 20 */     if (attributeInstance == null) {
/* 21 */       throw new IllegalArgumentException("Can't find attribute " + paramHolder.getRegisteredName());
/*    */     }
/* 23 */     return attributeInstance;
/*    */   }
/*    */   
/*    */   public double getValue(Holder<Attribute> paramHolder) {
/* 27 */     return getAttributeInstance(paramHolder).getValue();
/*    */   }
/*    */   
/*    */   public double getBaseValue(Holder<Attribute> paramHolder) {
/* 31 */     return getAttributeInstance(paramHolder).getBaseValue();
/*    */   }
/*    */   
/*    */   public double getModifierValue(Holder<Attribute> paramHolder, Identifier paramIdentifier) {
/* 35 */     AttributeModifier attributeModifier = getAttributeInstance(paramHolder).getModifier(paramIdentifier);
/* 36 */     if (attributeModifier == null) {
/* 37 */       throw new IllegalArgumentException("Can't find modifier " + String.valueOf(paramIdentifier) + " on attribute " + paramHolder.getRegisteredName());
/*    */     }
/* 39 */     return attributeModifier.amount();
/*    */   }
/*    */   
/*    */   public AttributeInstance createInstance(Consumer<AttributeInstance> paramConsumer, Holder<Attribute> paramHolder) {
/* 43 */     AttributeInstance attributeInstance1 = this.instances.get(paramHolder);
/* 44 */     if (attributeInstance1 == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     AttributeInstance attributeInstance2 = new AttributeInstance(paramHolder, paramConsumer);
/* 48 */     attributeInstance2.replaceFrom(attributeInstance1);
/* 49 */     return attributeInstance2;
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 53 */     return new Builder();
/*    */   }
/*    */   
/*    */   public boolean hasAttribute(Holder<Attribute> paramHolder) {
/* 57 */     return this.instances.containsKey(paramHolder);
/*    */   }
/*    */   
/*    */   public boolean hasModifier(Holder<Attribute> paramHolder, Identifier paramIdentifier) {
/* 61 */     AttributeInstance attributeInstance = this.instances.get(paramHolder);
/* 62 */     return (attributeInstance != null && attributeInstance.getModifier(paramIdentifier) != null);
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 66 */     private final ImmutableMap.Builder<Holder<Attribute>, AttributeInstance> builder = ImmutableMap.builder();
/*    */     private boolean instanceFrozen;
/*    */     
/*    */     private AttributeInstance create(Holder<Attribute> param1Holder) {
/* 70 */       AttributeInstance attributeInstance = new AttributeInstance(param1Holder, param1AttributeInstance -> {
/*    */             if (this.instanceFrozen) {
/*    */               throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + param1Holder.getRegisteredName());
/*    */             }
/*    */           });
/* 75 */       this.builder.put(param1Holder, attributeInstance);
/* 76 */       return attributeInstance;
/*    */     }
/*    */     
/*    */     public Builder add(Holder<Attribute> param1Holder) {
/* 80 */       create(param1Holder);
/* 81 */       return this;
/*    */     }
/*    */     
/*    */     public Builder add(Holder<Attribute> param1Holder, double param1Double) {
/* 85 */       AttributeInstance attributeInstance = create(param1Holder);
/* 86 */       attributeInstance.setBaseValue(param1Double);
/* 87 */       return this;
/*    */     }
/*    */     
/*    */     public AttributeSupplier build() {
/* 91 */       this.instanceFrozen = true;
/* 92 */       return new AttributeSupplier((Map<Holder<Attribute>, AttributeInstance>)this.builder.buildKeepingLast());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\attributes\AttributeSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */