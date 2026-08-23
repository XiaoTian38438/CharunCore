/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ItemStackData
/*     */ {
/*     */   private final String item;
/*     */   private final int count;
/*     */   private Dynamic<?> components;
/*     */   private final Dynamic<?> remainder;
/*     */   Dynamic<?> tag;
/*     */   
/*     */   private ItemStackData(String paramString, int paramInt, Dynamic<?> paramDynamic) {
/* 707 */     this.item = NamespacedSchema.ensureNamespaced(paramString);
/* 708 */     this.count = paramInt;
/* 709 */     this.components = paramDynamic.emptyMap();
/* 710 */     this.tag = paramDynamic.get("tag").orElseEmptyMap();
/*     */     
/* 712 */     this.remainder = paramDynamic.remove("tag");
/*     */   }
/*     */   
/*     */   public static Optional<ItemStackData> read(Dynamic<?> paramDynamic) {
/* 716 */     return paramDynamic.get("id").asString().apply2stable((paramString, paramNumber) -> new ItemStackData(paramString, paramNumber.intValue(), paramDynamic.remove("id").remove("Count")), paramDynamic
/*     */         
/* 718 */         .get("Count").asNumber())
/* 719 */       .result();
/*     */   }
/*     */   
/*     */   public OptionalDynamic<?> removeTag(String paramString) {
/* 723 */     OptionalDynamic<?> optionalDynamic = this.tag.get(paramString);
/* 724 */     this.tag = this.tag.remove(paramString);
/* 725 */     return optionalDynamic;
/*     */   }
/*     */   
/*     */   public void setComponent(String paramString, Dynamic<?> paramDynamic) {
/* 729 */     this.components = this.components.set(paramString, paramDynamic);
/*     */   }
/*     */   
/*     */   public void setComponent(String paramString, OptionalDynamic<?> paramOptionalDynamic) {
/* 733 */     paramOptionalDynamic.result().ifPresent(paramDynamic -> this.components = this.components.set(paramString, paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dynamic<?> moveTagInto(String paramString1, Dynamic<?> paramDynamic, String paramString2) {
/* 739 */     Optional<Dynamic> optional = removeTag(paramString1).result();
/* 740 */     if (optional.isPresent()) {
/* 741 */       return paramDynamic.set(paramString2, optional.get());
/*     */     }
/* 743 */     return paramDynamic;
/*     */   }
/*     */   
/*     */   public void moveTagToComponent(String paramString1, String paramString2, Dynamic<?> paramDynamic) {
/* 747 */     Optional<Dynamic> optional = removeTag(paramString1).result();
/* 748 */     if (optional.isPresent() && !((Dynamic)optional.get()).equals(paramDynamic)) {
/* 749 */       setComponent(paramString2, optional.get());
/*     */     }
/*     */   }
/*     */   
/*     */   public void moveTagToComponent(String paramString1, String paramString2) {
/* 754 */     removeTag(paramString1).result().ifPresent(paramDynamic -> setComponent(paramString, paramDynamic));
/*     */   }
/*     */   
/*     */   public void fixSubTag(String paramString, boolean paramBoolean, UnaryOperator<Dynamic<?>> paramUnaryOperator) {
/* 758 */     OptionalDynamic optionalDynamic = this.tag.get(paramString);
/* 759 */     if (paramBoolean && optionalDynamic.result().isEmpty()) {
/*     */       return;
/*     */     }
/* 762 */     Dynamic<?> dynamic = optionalDynamic.orElseEmptyMap();
/* 763 */     dynamic = paramUnaryOperator.apply(dynamic);
/* 764 */     if (dynamic.equals(dynamic.emptyMap())) {
/* 765 */       this.tag = this.tag.remove(paramString);
/*     */     } else {
/* 767 */       this.tag = this.tag.set(paramString, dynamic);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dynamic<?> write() {
/* 774 */     Dynamic<?> dynamic = this.tag.emptyMap().set("id", this.tag.createString(this.item)).set("count", this.tag.createInt(this.count));
/* 775 */     if (!this.tag.equals(this.tag.emptyMap())) {
/* 776 */       this.components = this.components.set("minecraft:custom_data", this.tag);
/*     */     }
/* 778 */     if (!this.components.equals(this.tag.emptyMap())) {
/* 779 */       dynamic = dynamic.set("components", this.components);
/*     */     }
/* 781 */     return mergeRemainder(dynamic, this.remainder);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> mergeRemainder(Dynamic<T> paramDynamic, Dynamic<?> paramDynamic1) {
/* 785 */     DynamicOps dynamicOps = paramDynamic.getOps();
/* 786 */     return dynamicOps.getMap(paramDynamic.getValue())
/* 787 */       .flatMap(paramMapLike -> paramDynamicOps.mergeToMap(paramDynamic.convert(paramDynamicOps).getValue(), paramMapLike))
/* 788 */       .map(paramObject -> new Dynamic(paramDynamicOps, paramObject))
/* 789 */       .result().orElse(paramDynamic);
/*     */   }
/*     */   
/*     */   public boolean is(String paramString) {
/* 793 */     return this.item.equals(paramString);
/*     */   }
/*     */   
/*     */   public boolean is(Set<String> paramSet) {
/* 797 */     return paramSet.contains(this.item);
/*     */   }
/*     */   
/*     */   public boolean hasComponent(String paramString) {
/* 801 */     return this.components.get(paramString).result().isPresent();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackComponentizationFix$ItemStackData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */