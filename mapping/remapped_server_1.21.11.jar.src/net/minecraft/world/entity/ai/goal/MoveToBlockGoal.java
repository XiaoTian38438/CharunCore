/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ public abstract class MoveToBlockGoal
/*     */   extends Goal {
/*     */   private static final int GIVE_UP_TICKS = 1200;
/*     */   private static final int STAY_TICKS = 1200;
/*     */   private static final int INTERVAL_TICKS = 200;
/*     */   protected final PathfinderMob mob;
/*     */   public final double speedModifier;
/*     */   protected int nextStartTick;
/*     */   protected int tryTicks;
/*     */   private int maxStayTicks;
/*  20 */   protected BlockPos blockPos = BlockPos.ZERO;
/*     */   
/*     */   private boolean reachedTarget;
/*     */   private final int searchRange;
/*     */   private final int verticalSearchRange;
/*     */   protected int verticalSearchStart;
/*     */   
/*     */   public MoveToBlockGoal(PathfinderMob paramPathfinderMob, double paramDouble, int paramInt) {
/*  28 */     this(paramPathfinderMob, paramDouble, paramInt, 1);
/*     */   }
/*     */   
/*     */   public MoveToBlockGoal(PathfinderMob paramPathfinderMob, double paramDouble, int paramInt1, int paramInt2) {
/*  32 */     this.mob = paramPathfinderMob;
/*  33 */     this.speedModifier = paramDouble;
/*  34 */     this.searchRange = paramInt1;
/*  35 */     this.verticalSearchStart = 0;
/*  36 */     this.verticalSearchRange = paramInt2;
/*  37 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  42 */     if (this.nextStartTick > 0) {
/*  43 */       this.nextStartTick--;
/*  44 */       return false;
/*     */     } 
/*  46 */     this.nextStartTick = nextStartTick(this.mob);
/*  47 */     return findNearestBlock();
/*     */   }
/*     */   
/*     */   protected int nextStartTick(PathfinderMob paramPathfinderMob) {
/*  51 */     return reducedTickDelay(200 + paramPathfinderMob.getRandom().nextInt(200));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  56 */     return (this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.mob.level(), this.blockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  61 */     moveMobToBlock();
/*  62 */     this.tryTicks = 0;
/*  63 */     this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(1200) + 1200) + 1200;
/*     */   }
/*     */   
/*     */   protected void moveMobToBlock() {
/*  67 */     this.mob.getNavigation().moveTo(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, this.speedModifier);
/*     */   }
/*     */   
/*     */   public double acceptedDistance() {
/*  71 */     return 1.0D;
/*     */   }
/*     */   
/*     */   protected BlockPos getMoveToTarget() {
/*  75 */     return this.blockPos.above();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  85 */     BlockPos blockPos = getMoveToTarget();
/*  86 */     if (!blockPos.closerToCenterThan((Position)this.mob.position(), acceptedDistance())) {
/*  87 */       this.reachedTarget = false;
/*  88 */       this.tryTicks++;
/*  89 */       if (shouldRecalculatePath()) {
/*  90 */         this.mob.getNavigation().moveTo(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, this.speedModifier);
/*     */       }
/*     */     } else {
/*  93 */       this.reachedTarget = true;
/*  94 */       this.tryTicks--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldRecalculatePath() {
/*  99 */     return (this.tryTicks % 40 == 0);
/*     */   }
/*     */   
/*     */   protected boolean isReachedTarget() {
/* 103 */     return this.reachedTarget;
/*     */   }
/*     */   
/*     */   protected boolean findNearestBlock() {
/* 107 */     int i = this.searchRange;
/* 108 */     int j = this.verticalSearchRange;
/* 109 */     BlockPos blockPos = this.mob.blockPosition();
/*     */     
/* 111 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(); int k;
/* 112 */     for (k = this.verticalSearchStart; k <= j; k = (k > 0) ? -k : (1 - k)) {
/* 113 */       for (byte b = 0; b < i; b++) {
/* 114 */         byte b1; for (b1 = 0; b1 <= b; b1 = (b1 > 0) ? -b1 : (1 - b1)) {
/*     */           
/* 116 */           byte b2 = (b1 < b && b1 > -b) ? b : 0;
/* 117 */           for (; b2 <= b; b2 = (b2 > 0) ? -b2 : (1 - b2)) {
/* 118 */             mutableBlockPos.setWithOffset((Vec3i)blockPos, b1, k - 1, b2);
/* 119 */             if (this.mob.isWithinHome((BlockPos)mutableBlockPos) && isValidTarget((LevelReader)this.mob.level(), (BlockPos)mutableBlockPos)) {
/* 120 */               this.blockPos = (BlockPos)mutableBlockPos;
/* 121 */               return true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract boolean isValidTarget(LevelReader paramLevelReader, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\MoveToBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */