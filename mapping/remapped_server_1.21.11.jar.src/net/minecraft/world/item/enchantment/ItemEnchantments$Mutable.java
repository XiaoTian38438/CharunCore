/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mutable
/*     */ {
/* 128 */   private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments = new Object2IntOpenHashMap();
/*     */   
/*     */   public Mutable(ItemEnchantments paramItemEnchantments) {
/* 131 */     this.enchantments.putAll((Map)paramItemEnchantments.enchantments);
/*     */   }
/*     */   
/*     */   public void set(Holder<Enchantment> paramHolder, int paramInt) {
/* 135 */     if (paramInt <= 0) {
/* 136 */       this.enchantments.removeInt(paramHolder);
/*     */     } else {
/* 138 */       this.enchantments.put(paramHolder, Math.min(paramInt, 255));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void upgrade(Holder<Enchantment> paramHolder, int paramInt) {
/* 143 */     if (paramInt > 0) {
/* 144 */       this.enchantments.merge(paramHolder, Math.min(paramInt, 255), Integer::max);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeIf(Predicate<Holder<Enchantment>> paramPredicate) {
/* 149 */     this.enchantments.keySet().removeIf(paramPredicate);
/*     */   }
/*     */   
/*     */   public int getLevel(Holder<Enchantment> paramHolder) {
/* 153 */     return this.enchantments.getOrDefault(paramHolder, 0);
/*     */   }
/*     */   
/*     */   public Set<Holder<Enchantment>> keySet() {
/* 157 */     return (Set<Holder<Enchantment>>)this.enchantments.keySet();
/*     */   }
/*     */   
/*     */   public ItemEnchantments toImmutable() {
/* 161 */     return new ItemEnchantments(this.enchantments);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\ItemEnchantments$Mutable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */