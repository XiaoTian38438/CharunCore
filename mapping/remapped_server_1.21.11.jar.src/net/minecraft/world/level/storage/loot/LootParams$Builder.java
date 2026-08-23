/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final ServerLevel level;
/*  54 */   private final ContextMap.Builder params = new ContextMap.Builder();
/*  55 */   private final Map<Identifier, LootParams.DynamicDrop> dynamicDrops = Maps.newHashMap();
/*     */   private float luck;
/*     */   
/*     */   public Builder(ServerLevel paramServerLevel) {
/*  59 */     this.level = paramServerLevel;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/*  63 */     return this.level;
/*     */   }
/*     */   
/*     */   public <T> Builder withParameter(ContextKey<T> paramContextKey, T paramT) {
/*  67 */     this.params.withParameter(paramContextKey, paramT);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder withOptionalParameter(ContextKey<T> paramContextKey, T paramT) {
/*  72 */     this.params.withOptionalParameter(paramContextKey, paramT);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public <T> T getParameter(ContextKey<T> paramContextKey) {
/*  77 */     return (T)this.params.getParameter(paramContextKey);
/*     */   }
/*     */   
/*     */   public <T> T getOptionalParameter(ContextKey<T> paramContextKey) {
/*  81 */     return (T)this.params.getOptionalParameter(paramContextKey);
/*     */   }
/*     */   
/*     */   public Builder withDynamicDrop(Identifier paramIdentifier, LootParams.DynamicDrop paramDynamicDrop) {
/*  85 */     LootParams.DynamicDrop dynamicDrop = this.dynamicDrops.put(paramIdentifier, paramDynamicDrop);
/*     */     
/*  87 */     if (dynamicDrop != null) {
/*  88 */       throw new IllegalStateException("Duplicated dynamic drop '" + String.valueOf(this.dynamicDrops) + "'");
/*     */     }
/*     */     
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withLuck(float paramFloat) {
/*  95 */     this.luck = paramFloat;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public LootParams create(ContextKeySet paramContextKeySet) {
/* 100 */     ContextMap contextMap = this.params.create(paramContextKeySet);
/* 101 */     return new LootParams(this.level, contextMap, this.dynamicDrops, this.luck);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootParams$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */