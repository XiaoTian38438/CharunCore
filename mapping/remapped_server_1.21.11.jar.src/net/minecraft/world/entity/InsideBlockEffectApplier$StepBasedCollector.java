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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StepBasedCollector
/*     */   implements InsideBlockEffectApplier
/*     */ {
/*  49 */   private static final InsideBlockEffectType[] APPLY_ORDER = InsideBlockEffectType.values();
/*     */   
/*     */   private static final int NO_STEP = -1;
/*  52 */   private final Set<InsideBlockEffectType> effectsInStep = EnumSet.noneOf(InsideBlockEffectType.class);
/*  53 */   private final Map<InsideBlockEffectType, List<Consumer<Entity>>> beforeEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, paramInsideBlockEffectType -> new ArrayList());
/*  54 */   private final Map<InsideBlockEffectType, List<Consumer<Entity>>> afterEffectsInStep = Util.makeEnumMap(InsideBlockEffectType.class, paramInsideBlockEffectType -> new ArrayList());
/*     */   
/*  56 */   private final List<Consumer<Entity>> finalEffects = new ArrayList<>();
/*     */   
/*  58 */   private int lastStep = -1;
/*     */   
/*     */   public void advanceStep(int paramInt) {
/*  61 */     if (this.lastStep != paramInt) {
/*  62 */       this.lastStep = paramInt;
/*  63 */       flushStep();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applyAndClear(Entity paramEntity) {
/*  68 */     flushStep();
/*  69 */     for (Consumer<Entity> consumer : this.finalEffects) {
/*  70 */       if (!paramEntity.isAlive()) {
/*     */         break;
/*     */       }
/*  73 */       consumer.accept(paramEntity);
/*     */     } 
/*  75 */     this.finalEffects.clear();
/*  76 */     this.lastStep = -1;
/*     */   }
/*     */   
/*     */   private void flushStep() {
/*  80 */     for (InsideBlockEffectType insideBlockEffectType : APPLY_ORDER) {
/*  81 */       List<? extends Consumer<Entity>> list1 = this.beforeEffectsInStep.get(insideBlockEffectType);
/*  82 */       this.finalEffects.addAll(list1);
/*  83 */       list1.clear();
/*     */       
/*  85 */       if (this.effectsInStep.remove(insideBlockEffectType)) {
/*  86 */         this.finalEffects.add(insideBlockEffectType.effect());
/*     */       }
/*     */       
/*  89 */       List<? extends Consumer<Entity>> list2 = this.afterEffectsInStep.get(insideBlockEffectType);
/*  90 */       this.finalEffects.addAll(list2);
/*  91 */       list2.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(InsideBlockEffectType paramInsideBlockEffectType) {
/*  97 */     this.effectsInStep.add(paramInsideBlockEffectType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void runBefore(InsideBlockEffectType paramInsideBlockEffectType, Consumer<Entity> paramConsumer) {
/* 102 */     ((List<Consumer<Entity>>)this.beforeEffectsInStep.get(paramInsideBlockEffectType)).add(paramConsumer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void runAfter(InsideBlockEffectType paramInsideBlockEffectType, Consumer<Entity> paramConsumer) {
/* 107 */     ((List<Consumer<Entity>>)this.afterEffectsInStep.get(paramInsideBlockEffectType)).add(paramConsumer);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\InsideBlockEffectApplier$StepBasedCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */