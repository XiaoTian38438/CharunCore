/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ class PlayerScores
/*    */ {
/* 14 */   private final Reference2ObjectOpenHashMap<Objective, Score> scores = new Reference2ObjectOpenHashMap(16, 0.5F);
/*    */   
/*    */   public Score get(Objective paramObjective) {
/* 17 */     return (Score)this.scores.get(paramObjective);
/*    */   }
/*    */   
/*    */   public Score getOrCreate(Objective paramObjective, Consumer<Score> paramConsumer) {
/* 21 */     return (Score)this.scores.computeIfAbsent(paramObjective, paramObject -> {
/*    */           Score score = new Score();
/*    */           paramConsumer.accept(score);
/*    */           return score;
/*    */         });
/*    */   }
/*    */   
/*    */   public boolean remove(Objective paramObjective) {
/* 29 */     return (this.scores.remove(paramObjective) != null);
/*    */   }
/*    */   
/*    */   public boolean hasScores() {
/* 33 */     return !this.scores.isEmpty();
/*    */   }
/*    */   
/*    */   public Object2IntMap<Objective> listScores() {
/* 37 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 38 */     this.scores.forEach((paramObjective, paramScore) -> paramObject2IntMap.put(paramObjective, paramScore.value()));
/* 39 */     return (Object2IntMap<Objective>)object2IntOpenHashMap;
/*    */   }
/*    */   
/*    */   void setScore(Objective paramObjective, Score paramScore) {
/* 43 */     this.scores.put(paramObjective, paramScore);
/*    */   }
/*    */   
/*    */   Map<Objective, Score> listRawScores() {
/* 47 */     return Collections.unmodifiableMap((Map<? extends Objective, ? extends Score>)this.scores);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\scores\PlayerScores.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */