/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LootParams
/*     */ {
/*     */   private final ServerLevel level;
/*     */   private final ContextMap params;
/*     */   private final Map<Identifier, DynamicDrop> dynamicDrops;
/*     */   private final float luck;
/*     */   
/*     */   public LootParams(ServerLevel paramServerLevel, ContextMap paramContextMap, Map<Identifier, DynamicDrop> paramMap, float paramFloat) {
/*  27 */     this.level = paramServerLevel;
/*  28 */     this.params = paramContextMap;
/*  29 */     this.dynamicDrops = paramMap;
/*  30 */     this.luck = paramFloat;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/*  34 */     return this.level;
/*     */   }
/*     */   
/*     */   public ContextMap contextMap() {
/*  38 */     return this.params;
/*     */   }
/*     */   
/*     */   public void addDynamicDrops(Identifier paramIdentifier, Consumer<ItemStack> paramConsumer) {
/*  42 */     DynamicDrop dynamicDrop = this.dynamicDrops.get(paramIdentifier);
/*  43 */     if (dynamicDrop != null) {
/*  44 */       dynamicDrop.add(paramConsumer);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getLuck() {
/*  49 */     return this.luck;
/*     */   }
/*     */   @FunctionalInterface
/*     */   public static interface DynamicDrop {
/*     */     void add(Consumer<ItemStack> param1Consumer); }
/*  54 */   public static class Builder { private final ContextMap.Builder params = new ContextMap.Builder(); private final ServerLevel level;
/*  55 */     private final Map<Identifier, LootParams.DynamicDrop> dynamicDrops = Maps.newHashMap();
/*     */     private float luck;
/*     */     
/*     */     public Builder(ServerLevel param1ServerLevel) {
/*  59 */       this.level = param1ServerLevel;
/*     */     }
/*     */     
/*     */     public ServerLevel getLevel() {
/*  63 */       return this.level;
/*     */     }
/*     */     
/*     */     public <T> Builder withParameter(ContextKey<T> param1ContextKey, T param1T) {
/*  67 */       this.params.withParameter(param1ContextKey, param1T);
/*  68 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder withOptionalParameter(ContextKey<T> param1ContextKey, T param1T) {
/*  72 */       this.params.withOptionalParameter(param1ContextKey, param1T);
/*  73 */       return this;
/*     */     }
/*     */     
/*     */     public <T> T getParameter(ContextKey<T> param1ContextKey) {
/*  77 */       return (T)this.params.getParameter(param1ContextKey);
/*     */     }
/*     */     
/*     */     public <T> T getOptionalParameter(ContextKey<T> param1ContextKey) {
/*  81 */       return (T)this.params.getOptionalParameter(param1ContextKey);
/*     */     }
/*     */     
/*     */     public Builder withDynamicDrop(Identifier param1Identifier, LootParams.DynamicDrop param1DynamicDrop) {
/*  85 */       LootParams.DynamicDrop dynamicDrop = this.dynamicDrops.put(param1Identifier, param1DynamicDrop);
/*     */       
/*  87 */       if (dynamicDrop != null) {
/*  88 */         throw new IllegalStateException("Duplicated dynamic drop '" + String.valueOf(this.dynamicDrops) + "'");
/*     */       }
/*     */       
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withLuck(float param1Float) {
/*  95 */       this.luck = param1Float;
/*  96 */       return this;
/*     */     }
/*     */     
/*     */     public LootParams create(ContextKeySet param1ContextKeySet) {
/* 100 */       ContextMap contextMap = this.params.create(param1ContextKeySet);
/* 101 */       return new LootParams(this.level, contextMap, this.dynamicDrops, this.luck);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootParams.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */