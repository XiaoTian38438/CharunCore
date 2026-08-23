/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ 
/*     */ public class GoalSelector
/*     */ {
/*  14 */   private static final WrappedGoal NO_GOAL = new WrappedGoal(2147483647, new Goal()
/*     */       {
/*     */         public boolean canUse() {
/*  17 */           return false;
/*     */         }
/*     */       })
/*     */     {
/*     */       public boolean isRunning() {
/*  22 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  26 */   private final Map<Goal.Flag, WrappedGoal> lockedFlags = new EnumMap<>(Goal.Flag.class);
/*  27 */   private final Set<WrappedGoal> availableGoals = (Set<WrappedGoal>)new ObjectLinkedOpenHashSet();
/*  28 */   private final EnumSet<Goal.Flag> disabledFlags = EnumSet.noneOf(Goal.Flag.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGoal(int paramInt, Goal paramGoal) {
/*  35 */     this.availableGoals.add(new WrappedGoal(paramInt, paramGoal));
/*     */   }
/*     */   
/*     */   public void removeAllGoals(Predicate<Goal> paramPredicate) {
/*  39 */     this.availableGoals.removeIf(paramWrappedGoal -> paramPredicate.test(paramWrappedGoal.getGoal()));
/*     */   }
/*     */   
/*     */   public void removeGoal(Goal paramGoal) {
/*  43 */     for (WrappedGoal wrappedGoal : this.availableGoals) {
/*  44 */       if (wrappedGoal.getGoal() == paramGoal && wrappedGoal.isRunning()) {
/*  45 */         wrappedGoal.stop();
/*     */       }
/*     */     } 
/*  48 */     this.availableGoals.removeIf(paramWrappedGoal -> (paramWrappedGoal.getGoal() == paramGoal));
/*     */   }
/*     */   
/*     */   private static boolean goalContainsAnyFlags(WrappedGoal paramWrappedGoal, EnumSet<Goal.Flag> paramEnumSet) {
/*  52 */     for (Goal.Flag flag : paramWrappedGoal.getFlags()) {
/*  53 */       if (paramEnumSet.contains(flag)) {
/*  54 */         return true;
/*     */       }
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean goalCanBeReplacedForAllFlags(WrappedGoal paramWrappedGoal, Map<Goal.Flag, WrappedGoal> paramMap) {
/*  61 */     for (Goal.Flag flag : paramWrappedGoal.getFlags()) {
/*  62 */       if (!((WrappedGoal)paramMap.getOrDefault(flag, NO_GOAL)).canBeReplacedBy(paramWrappedGoal)) {
/*  63 */         return false;
/*     */       }
/*     */     } 
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  71 */     ProfilerFiller profilerFiller = Profiler.get();
/*     */     
/*  73 */     profilerFiller.push("goalCleanup");
/*  74 */     for (WrappedGoal wrappedGoal : this.availableGoals) {
/*  75 */       if (wrappedGoal.isRunning() && (goalContainsAnyFlags(wrappedGoal, this.disabledFlags) || !wrappedGoal.canContinueToUse())) {
/*  76 */         wrappedGoal.stop();
/*     */       }
/*     */     } 
/*  79 */     this.lockedFlags.entrySet().removeIf(paramEntry -> !((WrappedGoal)paramEntry.getValue()).isRunning());
/*  80 */     profilerFiller.pop();
/*     */     
/*  82 */     profilerFiller.push("goalUpdate");
/*  83 */     for (WrappedGoal wrappedGoal : this.availableGoals) {
/*  84 */       if (wrappedGoal.isRunning() || goalContainsAnyFlags(wrappedGoal, this.disabledFlags) || !goalCanBeReplacedForAllFlags(wrappedGoal, this.lockedFlags) || !wrappedGoal.canUse()) {
/*     */         continue;
/*     */       }
/*  87 */       for (Goal.Flag flag : wrappedGoal.getFlags()) {
/*  88 */         WrappedGoal wrappedGoal1 = this.lockedFlags.getOrDefault(flag, NO_GOAL);
/*  89 */         wrappedGoal1.stop();
/*  90 */         this.lockedFlags.put(flag, wrappedGoal);
/*     */       } 
/*  92 */       wrappedGoal.start();
/*     */     } 
/*  94 */     profilerFiller.pop();
/*     */     
/*  96 */     tickRunningGoals(true);
/*     */   }
/*     */   
/*     */   public void tickRunningGoals(boolean paramBoolean) {
/* 100 */     ProfilerFiller profilerFiller = Profiler.get();
/*     */     
/* 102 */     profilerFiller.push("goalTick");
/* 103 */     for (WrappedGoal wrappedGoal : this.availableGoals) {
/* 104 */       if (wrappedGoal.isRunning() && (paramBoolean || wrappedGoal.requiresUpdateEveryTick())) {
/* 105 */         wrappedGoal.tick();
/*     */       }
/*     */     } 
/* 108 */     profilerFiller.pop();
/*     */   }
/*     */   
/*     */   public Set<WrappedGoal> getAvailableGoals() {
/* 112 */     return this.availableGoals;
/*     */   }
/*     */   
/*     */   public void disableControlFlag(Goal.Flag paramFlag) {
/* 116 */     this.disabledFlags.add(paramFlag);
/*     */   }
/*     */   
/*     */   public void enableControlFlag(Goal.Flag paramFlag) {
/* 120 */     this.disabledFlags.remove(paramFlag);
/*     */   }
/*     */   
/*     */   public void setControlFlag(Goal.Flag paramFlag, boolean paramBoolean) {
/* 124 */     if (paramBoolean) {
/* 125 */       enableControlFlag(paramFlag);
/*     */     } else {
/* 127 */       disableControlFlag(paramFlag);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\GoalSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */