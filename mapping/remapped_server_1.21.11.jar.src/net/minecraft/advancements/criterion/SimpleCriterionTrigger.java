/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.CriterionTrigger;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public abstract class SimpleCriterionTrigger<T extends SimpleCriterionTrigger.SimpleInstance> implements CriterionTrigger<T> {
/* 19 */   private final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<T>>> players = Maps.newIdentityHashMap();
/*    */ 
/*    */   
/*    */   public final void addPlayerListener(PlayerAdvancements paramPlayerAdvancements, CriterionTrigger.Listener<T> paramListener) {
/* 23 */     ((Set<CriterionTrigger.Listener<T>>)this.players.computeIfAbsent(paramPlayerAdvancements, paramPlayerAdvancements -> Sets.newHashSet())).add(paramListener);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void removePlayerListener(PlayerAdvancements paramPlayerAdvancements, CriterionTrigger.Listener<T> paramListener) {
/* 28 */     Set set = this.players.get(paramPlayerAdvancements);
/* 29 */     if (set != null) {
/* 30 */       set.remove(paramListener);
/* 31 */       if (set.isEmpty()) {
/* 32 */         this.players.remove(paramPlayerAdvancements);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final void removePlayerListeners(PlayerAdvancements paramPlayerAdvancements) {
/* 39 */     this.players.remove(paramPlayerAdvancements);
/*    */   }
/*    */   
/*    */   protected void trigger(ServerPlayer paramServerPlayer, Predicate<T> paramPredicate) {
/* 43 */     PlayerAdvancements playerAdvancements = paramServerPlayer.getAdvancements();
/* 44 */     Set set = this.players.get(playerAdvancements);
/*    */     
/* 46 */     if (set == null || set.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 50 */     LootContext lootContext = EntityPredicate.createContext(paramServerPlayer, (Entity)paramServerPlayer);
/*    */     
/* 52 */     ArrayList<CriterionTrigger.Listener> arrayList = null;
/* 53 */     for (CriterionTrigger.Listener listener : set) {
/* 54 */       SimpleInstance simpleInstance = (SimpleInstance)listener.trigger();
/*    */ 
/*    */       
/* 57 */       if (!paramPredicate.test((T)simpleInstance)) {
/*    */         continue;
/*    */       }
/* 60 */       Optional<ContextAwarePredicate> optional = simpleInstance.player();
/* 61 */       if (optional.isEmpty() || ((ContextAwarePredicate)optional.get()).matches(lootContext)) {
/* 62 */         if (arrayList == null) {
/* 63 */           arrayList = Lists.newArrayList();
/*    */         }
/* 65 */         arrayList.add(listener);
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     if (arrayList != null)
/* 70 */       for (CriterionTrigger.Listener listener : arrayList) {
/* 71 */         listener.run(playerAdvancements);
/*    */       } 
/*    */   }
/*    */   
/*    */   public static interface SimpleInstance
/*    */     extends CriterionTriggerInstance
/*    */   {
/*    */     default void validate(CriterionValidator param1CriterionValidator) {
/* 79 */       param1CriterionValidator.validateEntity(player(), "player");
/*    */     }
/*    */     
/*    */     Optional<ContextAwarePredicate> player();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\SimpleCriterionTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */