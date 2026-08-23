/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface InsideBlockEffectApplier
/*     */ {
/*  13 */   public static final InsideBlockEffectApplier NOOP = new InsideBlockEffectApplier()
/*     */     {
/*     */       public void apply(InsideBlockEffectType param1InsideBlockEffectType) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void runBefore(InsideBlockEffectType param1InsideBlockEffectType, Consumer<Entity> param1Consumer) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void runAfter(InsideBlockEffectType param1InsideBlockEffectType, Consumer<Entity> param1Consumer) {}
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void apply(InsideBlockEffectType paramInsideBlockEffectType);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void runBefore(InsideBlockEffectType paramInsideBlockEffectType, Consumer<Entity> paramConsumer);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void runAfter(InsideBlockEffectType paramInsideBlockEffectType, Consumer<Entity> paramConsumer);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class StepBasedCollector
/*     */     implements InsideBlockEffectApplier
/*     */   {
/*     */     public StepBasedCollector() {
/*  52 */       this.effectsInStep = EnumSet.noneOf(InsideBlockEffectType.class);
/*  53 */       this.beforeEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, param1InsideBlockEffectType -> new ArrayList());
/*  54 */       this.afterEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, param1InsideBlockEffectType -> new ArrayList());
/*     */       
/*  56 */       this.finalEffects = new ArrayList<>();
/*     */       
/*  58 */       this.lastStep = -1;
/*     */     } private static final InsideBlockEffectType[] APPLY_ORDER = InsideBlockEffectType.values(); private static final int NO_STEP = -1; private final Set<InsideBlockEffectType> effectsInStep;
/*     */     public void advanceStep(int param1Int) {
/*  61 */       if (this.lastStep != param1Int) {
/*  62 */         this.lastStep = param1Int;
/*  63 */         flushStep();
/*     */       } 
/*     */     }
/*     */     private final Map<InsideBlockEffectType, List<Consumer<Entity>>> beforeEffectsInStep; private final Map<InsideBlockEffectType, List<Consumer<Entity>>> afterEffectsInStep; private final List<Consumer<Entity>> finalEffects; private int lastStep;
/*     */     public void applyAndClear(Entity param1Entity) {
/*  68 */       flushStep();
/*  69 */       for (Consumer<Entity> consumer : this.finalEffects) {
/*  70 */         if (!param1Entity.isAlive()) {
/*     */           break;
/*     */         }
/*  73 */         consumer.accept(param1Entity);
/*     */       } 
/*  75 */       this.finalEffects.clear();
/*  76 */       this.lastStep = -1;
/*     */     }
/*     */     
/*     */     private void flushStep() {
/*  80 */       for (InsideBlockEffectType insideBlockEffectType : APPLY_ORDER) {
/*  81 */         List<? extends Consumer<Entity>> list1 = this.beforeEffectsInStep.get(insideBlockEffectType);
/*  82 */         this.finalEffects.addAll(list1);
/*  83 */         list1.clear();
/*     */         
/*  85 */         if (this.effectsInStep.remove(insideBlockEffectType)) {
/*  86 */           this.finalEffects.add(insideBlockEffectType.effect());
/*     */         }
/*     */         
/*  89 */         List<? extends Consumer<Entity>> list2 = this.afterEffectsInStep.get(insideBlockEffectType);
/*  90 */         this.finalEffects.addAll(list2);
/*  91 */         list2.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void apply(InsideBlockEffectType param1InsideBlockEffectType) {
/*  97 */       this.effectsInStep.add(param1InsideBlockEffectType);
/*     */     }
/*     */ 
/*     */     
/*     */     public void runBefore(InsideBlockEffectType param1InsideBlockEffectType, Consumer<Entity> param1Consumer) {
/* 102 */       ((List<Consumer<Entity>>)this.beforeEffectsInStep.get(param1InsideBlockEffectType)).add(param1Consumer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void runAfter(InsideBlockEffectType param1InsideBlockEffectType, Consumer<Entity> param1Consumer) {
/* 107 */       ((List<Consumer<Entity>>)this.afterEffectsInStep.get(param1InsideBlockEffectType)).add(param1Consumer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\InsideBlockEffectApplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */