/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ public class AttributeMap
/*     */ {
/*  18 */   private final Map<Holder<Attribute>, AttributeInstance> attributes = (Map<Holder<Attribute>, AttributeInstance>)new Object2ObjectOpenHashMap();
/*  19 */   private final Set<AttributeInstance> attributesToSync = (Set<AttributeInstance>)new ObjectOpenHashSet();
/*  20 */   private final Set<AttributeInstance> attributesToUpdate = (Set<AttributeInstance>)new ObjectOpenHashSet();
/*     */   private final AttributeSupplier supplier;
/*     */   
/*     */   public AttributeMap(AttributeSupplier paramAttributeSupplier) {
/*  24 */     this.supplier = paramAttributeSupplier;
/*     */   }
/*     */   
/*     */   private void onAttributeModified(AttributeInstance paramAttributeInstance) {
/*  28 */     this.attributesToUpdate.add(paramAttributeInstance);
/*  29 */     if (((Attribute)paramAttributeInstance.getAttribute().value()).isClientSyncable()) {
/*  30 */       this.attributesToSync.add(paramAttributeInstance);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<AttributeInstance> getAttributesToSync() {
/*  35 */     return this.attributesToSync;
/*     */   }
/*     */   
/*     */   public Set<AttributeInstance> getAttributesToUpdate() {
/*  39 */     return this.attributesToUpdate;
/*     */   }
/*     */   
/*     */   public Collection<AttributeInstance> getSyncableAttributes() {
/*  43 */     return (Collection<AttributeInstance>)this.attributes.values().stream().filter(paramAttributeInstance -> ((Attribute)paramAttributeInstance.getAttribute().value()).isClientSyncable()).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public AttributeInstance getInstance(Holder<Attribute> paramHolder) {
/*  47 */     return this.attributes.computeIfAbsent(paramHolder, paramHolder -> this.supplier.createInstance(this::onAttributeModified, paramHolder));
/*     */   }
/*     */   
/*     */   public boolean hasAttribute(Holder<Attribute> paramHolder) {
/*  51 */     return (this.attributes.get(paramHolder) != null || this.supplier.hasAttribute(paramHolder));
/*     */   }
/*     */   
/*     */   public boolean hasModifier(Holder<Attribute> paramHolder, Identifier paramIdentifier) {
/*  55 */     AttributeInstance attributeInstance = this.attributes.get(paramHolder);
/*  56 */     return (attributeInstance != null) ? ((attributeInstance.getModifier(paramIdentifier) != null)) : this.supplier.hasModifier(paramHolder, paramIdentifier);
/*     */   }
/*     */   
/*     */   public double getValue(Holder<Attribute> paramHolder) {
/*  60 */     AttributeInstance attributeInstance = this.attributes.get(paramHolder);
/*  61 */     return (attributeInstance != null) ? attributeInstance.getValue() : this.supplier.getValue(paramHolder);
/*     */   }
/*     */   
/*     */   public double getBaseValue(Holder<Attribute> paramHolder) {
/*  65 */     AttributeInstance attributeInstance = this.attributes.get(paramHolder);
/*  66 */     return (attributeInstance != null) ? attributeInstance.getBaseValue() : this.supplier.getBaseValue(paramHolder);
/*     */   }
/*     */   
/*     */   public double getModifierValue(Holder<Attribute> paramHolder, Identifier paramIdentifier) {
/*  70 */     AttributeInstance attributeInstance = this.attributes.get(paramHolder);
/*  71 */     return (attributeInstance != null) ? attributeInstance.getModifier(paramIdentifier).amount() : this.supplier.getModifierValue(paramHolder, paramIdentifier);
/*     */   }
/*     */   
/*     */   public void addTransientAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> paramMultimap) {
/*  75 */     paramMultimap.forEach((paramHolder, paramAttributeModifier) -> {
/*     */           AttributeInstance attributeInstance = getInstance(paramHolder);
/*     */           if (attributeInstance != null) {
/*     */             attributeInstance.removeModifier(paramAttributeModifier.id());
/*     */             attributeInstance.addTransientModifier(paramAttributeModifier);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> paramMultimap) {
/*  86 */     paramMultimap.asMap().forEach((paramHolder, paramCollection) -> {
/*     */           AttributeInstance attributeInstance = this.attributes.get(paramHolder);
/*     */           if (attributeInstance != null) {
/*     */             paramCollection.forEach(());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void assignAllValues(AttributeMap paramAttributeMap) {
/*  96 */     paramAttributeMap.attributes.values().forEach(paramAttributeInstance -> {
/*     */           AttributeInstance attributeInstance = getInstance(paramAttributeInstance.getAttribute());
/*     */           if (attributeInstance != null) {
/*     */             attributeInstance.replaceFrom(paramAttributeInstance);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignBaseValues(AttributeMap paramAttributeMap) {
/* 105 */     paramAttributeMap.attributes.values().forEach(paramAttributeInstance -> {
/*     */           AttributeInstance attributeInstance = getInstance(paramAttributeInstance.getAttribute());
/*     */           if (attributeInstance != null) {
/*     */             attributeInstance.setBaseValue(paramAttributeInstance.getBaseValue());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignPermanentModifiers(AttributeMap paramAttributeMap) {
/* 114 */     paramAttributeMap.attributes.values().forEach(paramAttributeInstance -> {
/*     */           AttributeInstance attributeInstance = getInstance(paramAttributeInstance.getAttribute());
/*     */           if (attributeInstance != null) {
/*     */             attributeInstance.addPermanentModifiers(paramAttributeInstance.getPermanentModifiers());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean resetBaseValue(Holder<Attribute> paramHolder) {
/* 123 */     if (!this.supplier.hasAttribute(paramHolder)) {
/* 124 */       return false;
/*     */     }
/* 126 */     AttributeInstance attributeInstance = this.attributes.get(paramHolder);
/* 127 */     if (attributeInstance != null) {
/* 128 */       attributeInstance.setBaseValue(this.supplier.getBaseValue(paramHolder));
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public List<AttributeInstance.Packed> pack() {
/* 134 */     ArrayList<AttributeInstance.Packed> arrayList = new ArrayList(this.attributes.values().size());
/* 135 */     for (AttributeInstance attributeInstance : this.attributes.values()) {
/* 136 */       arrayList.add(attributeInstance.pack());
/*     */     }
/* 138 */     return arrayList;
/*     */   }
/*     */   
/*     */   public void apply(List<AttributeInstance.Packed> paramList) {
/* 142 */     for (AttributeInstance.Packed packed : paramList) {
/* 143 */       AttributeInstance attributeInstance = getInstance(packed.attribute());
/* 144 */       if (attributeInstance != null)
/* 145 */         attributeInstance.apply(packed); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\attributes\AttributeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */