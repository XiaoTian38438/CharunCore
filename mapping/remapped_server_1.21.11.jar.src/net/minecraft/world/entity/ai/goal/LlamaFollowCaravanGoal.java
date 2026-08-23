/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LlamaFollowCaravanGoal
/*     */   extends Goal
/*     */ {
/*     */   public final Llama llama;
/*     */   private double speedModifier;
/*     */   private static final int CARAVAN_LIMIT = 8;
/*     */   private int distCheckCounter;
/*     */   
/*     */   public LlamaFollowCaravanGoal(Llama paramLlama, double paramDouble) {
/*  23 */     this.llama = paramLlama;
/*  24 */     this.speedModifier = paramDouble;
/*  25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  30 */     if (this.llama.isLeashed() || this.llama.inCaravan()) {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     List list = this.llama.level().getEntities((Entity)this.llama, this.llama.getBoundingBox().inflate(9.0D, 4.0D, 9.0D), paramEntity -> {
/*     */           EntityType entityType = paramEntity.getType();
/*  36 */           return (entityType == EntityType.LLAMA || entityType == EntityType.TRADER_LLAMA);
/*     */         });
/*     */     
/*  39 */     Llama llama = null;
/*  40 */     double d = Double.MAX_VALUE;
/*  41 */     for (Entity entity : list) {
/*  42 */       Llama llama1 = (Llama)entity;
/*     */       
/*  44 */       if (!llama1.inCaravan() || llama1.hasCaravanTail()) {
/*     */         continue;
/*     */       }
/*     */       
/*  48 */       double d1 = this.llama.distanceToSqr((Entity)llama1);
/*  49 */       if (d1 > d) {
/*     */         continue;
/*     */       }
/*     */       
/*  53 */       d = d1;
/*  54 */       llama = llama1;
/*     */     } 
/*     */     
/*  57 */     if (llama == null)
/*     */     {
/*  59 */       for (Entity entity : list) {
/*  60 */         Llama llama1 = (Llama)entity;
/*     */         
/*  62 */         if (!llama1.isLeashed()) {
/*     */           continue;
/*     */         }
/*     */         
/*  66 */         if (llama1.hasCaravanTail()) {
/*     */           continue;
/*     */         }
/*     */         
/*  70 */         double d1 = this.llama.distanceToSqr((Entity)llama1);
/*  71 */         if (d1 > d) {
/*     */           continue;
/*     */         }
/*     */         
/*  75 */         d = d1;
/*  76 */         llama = llama1;
/*     */       } 
/*     */     }
/*     */     
/*  80 */     if (llama == null) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (d < 4.0D) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     if (!llama.isLeashed() && !firstIsLeashed(llama, 1)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     this.llama.joinCaravan(llama);
/*     */     
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  98 */     if (!this.llama.inCaravan() || !this.llama.getCaravanHead().isAlive() || !firstIsLeashed(this.llama, 0)) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     double d = this.llama.distanceToSqr((Entity)this.llama.getCaravanHead());
/* 103 */     if (d > 676.0D) {
/* 104 */       if (this.speedModifier <= 3.0D) {
/* 105 */         this.speedModifier *= 1.2D;
/* 106 */         this.distCheckCounter = reducedTickDelay(40);
/* 107 */         return true;
/*     */       } 
/*     */       
/* 110 */       if (this.distCheckCounter == 0) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/* 114 */     if (this.distCheckCounter > 0) {
/* 115 */       this.distCheckCounter--;
/*     */     }
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 122 */     this.llama.leaveCaravan();
/* 123 */     this.speedModifier = 2.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 128 */     if (!this.llama.inCaravan()) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (this.llama.getLeashHolder() instanceof net.minecraft.world.entity.decoration.LeashFenceKnotEntity) {
/*     */       return;
/*     */     }
/*     */     
/* 136 */     Llama llama = this.llama.getCaravanHead();
/* 137 */     double d = this.llama.distanceTo((Entity)llama);
/*     */     
/* 139 */     float f = 2.0F;
/* 140 */     Vec3 vec3 = (new Vec3(llama.getX() - this.llama.getX(), llama.getY() - this.llama.getY(), llama.getZ() - this.llama.getZ())).normalize().scale(Math.max(d - 2.0D, 0.0D));
/* 141 */     this.llama.getNavigation().moveTo(this.llama.getX() + vec3.x, this.llama.getY() + vec3.y, this.llama.getZ() + vec3.z, this.speedModifier);
/*     */   }
/*     */   
/*     */   private boolean firstIsLeashed(Llama paramLlama, int paramInt) {
/* 145 */     if (paramInt > 8) {
/* 146 */       return false;
/*     */     }
/*     */     
/* 149 */     if (paramLlama.inCaravan()) {
/* 150 */       if (paramLlama.getCaravanHead().isLeashed()) {
/* 151 */         return true;
/*     */       }
/* 153 */       return firstIsLeashed(paramLlama.getCaravanHead(), ++paramInt);
/*     */     } 
/* 155 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\LlamaFollowCaravanGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */