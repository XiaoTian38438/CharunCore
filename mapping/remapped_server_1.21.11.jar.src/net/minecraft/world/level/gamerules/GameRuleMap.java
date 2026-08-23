/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ 
/*     */ 
/*     */ public final class GameRuleMap
/*     */ {
/*  16 */   public static final Codec<GameRuleMap> CODEC = Codec.dispatchedMap(BuiltInRegistries.GAME_RULE.byNameCodec(), GameRule::valueCodec).xmap(GameRuleMap::ofTrusted, GameRuleMap::map);
/*     */   
/*     */   private final Reference2ObjectMap<GameRule<?>, Object> map;
/*     */   
/*     */   GameRuleMap(Reference2ObjectMap<GameRule<?>, Object> paramReference2ObjectMap) {
/*  21 */     this.map = paramReference2ObjectMap;
/*     */   }
/*     */   
/*     */   private static GameRuleMap ofTrusted(Map<GameRule<?>, Object> paramMap) {
/*  25 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap(paramMap));
/*     */   }
/*     */   
/*     */   public static GameRuleMap of() {
/*  29 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   public static GameRuleMap of(Stream<GameRule<?>> paramStream) {
/*  33 */     Reference2ObjectOpenHashMap reference2ObjectOpenHashMap = new Reference2ObjectOpenHashMap();
/*  34 */     paramStream.forEach(paramGameRule -> paramReference2ObjectOpenHashMap.put(paramGameRule, paramGameRule.defaultValue()));
/*  35 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)reference2ObjectOpenHashMap);
/*     */   }
/*     */   
/*     */   public static GameRuleMap copyOf(GameRuleMap paramGameRuleMap) {
/*  39 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap(paramGameRuleMap.map));
/*     */   }
/*     */   
/*     */   public boolean has(GameRule<?> paramGameRule) {
/*  43 */     return this.map.containsKey(paramGameRule);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(GameRule<T> paramGameRule) {
/*  48 */     return (T)this.map.get(paramGameRule);
/*     */   }
/*     */   
/*     */   public <T> void set(GameRule<T> paramGameRule, T paramT) {
/*  52 */     this.map.put(paramGameRule, paramT);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T remove(GameRule<T> paramGameRule) {
/*  57 */     return (T)this.map.remove(paramGameRule);
/*     */   }
/*     */   
/*     */   public Set<GameRule<?>> keySet() {
/*  61 */     return (Set<GameRule<?>>)this.map.keySet();
/*     */   }
/*     */   
/*     */   public int size() {
/*  65 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  70 */     return this.map.toString();
/*     */   }
/*     */   
/*     */   public GameRuleMap withOther(GameRuleMap paramGameRuleMap) {
/*  74 */     GameRuleMap gameRuleMap = copyOf(this);
/*  75 */     gameRuleMap.setFromIf(paramGameRuleMap, paramGameRule -> true);
/*  76 */     return gameRuleMap;
/*     */   }
/*     */   
/*     */   public void setFromIf(GameRuleMap paramGameRuleMap, Predicate<GameRule<?>> paramPredicate) {
/*  80 */     for (GameRule<?> gameRule : paramGameRuleMap.keySet()) {
/*  81 */       if (paramPredicate.test(gameRule)) {
/*  82 */         setGameRule(paramGameRuleMap, gameRule, this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> void setGameRule(GameRuleMap paramGameRuleMap1, GameRule<T> paramGameRule, GameRuleMap paramGameRuleMap2) {
/*  88 */     paramGameRuleMap2.set(paramGameRule, Objects.requireNonNull(paramGameRuleMap1.get(paramGameRule)));
/*     */   }
/*     */   
/*     */   private Reference2ObjectMap<GameRule<?>, Object> map() {
/*  92 */     return this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  97 */     if (paramObject == this) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (paramObject == null || paramObject.getClass() != getClass()) {
/* 101 */       return false;
/*     */     }
/* 103 */     GameRuleMap gameRuleMap = (GameRuleMap)paramObject;
/* 104 */     return Objects.equals(this.map, gameRuleMap.map);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     return Objects.hash(new Object[] { this.map });
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 113 */     final Reference2ObjectMap<GameRule<?>, Object> map = (Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap();
/*     */     
/*     */     public <T> Builder set(GameRule<T> param1GameRule, T param1T) {
/* 116 */       this.map.put(param1GameRule, param1T);
/* 117 */       return this;
/*     */     }
/*     */     
/*     */     public GameRuleMap build() {
/* 121 */       return new GameRuleMap(this.map);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gamerules\GameRuleMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */