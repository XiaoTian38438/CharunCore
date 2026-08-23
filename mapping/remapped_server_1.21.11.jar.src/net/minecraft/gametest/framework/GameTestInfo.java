/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameTestInfo
/*     */ {
/*     */   private final Holder.Reference<GameTestInstance> test;
/*     */   private BlockPos testBlockPos;
/*     */   private final ServerLevel level;
/*  42 */   private final Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */   
/*     */   private final int timeoutTicks;
/*     */   
/*  46 */   private final Collection<GameTestSequence> sequences = Lists.newCopyOnWriteArrayList();
/*  47 */   private final Object2LongMap<Runnable> runAtTickTimeMap = (Object2LongMap<Runnable>)new Object2LongOpenHashMap();
/*     */   
/*     */   private boolean placedStructure;
/*     */   private boolean chunksLoaded;
/*     */   private int tickCount;
/*     */   private boolean started;
/*     */   private final RetryOptions retryOptions;
/*  54 */   private final Stopwatch timer = Stopwatch.createUnstarted();
/*     */   
/*     */   private boolean done;
/*     */   
/*     */   private final Rotation extraRotation;
/*     */   private GameTestException error;
/*     */   private TestInstanceBlockEntity testInstanceBlockEntity;
/*     */   
/*     */   public GameTestInfo(Holder.Reference<GameTestInstance> paramReference, Rotation paramRotation, ServerLevel paramServerLevel, RetryOptions paramRetryOptions) {
/*  63 */     this.test = paramReference;
/*  64 */     this.level = paramServerLevel;
/*  65 */     this.retryOptions = paramRetryOptions;
/*  66 */     this.timeoutTicks = ((GameTestInstance)paramReference.value()).maxTicks();
/*  67 */     this.extraRotation = paramRotation;
/*     */   }
/*     */   
/*     */   public void setTestBlockPos(BlockPos paramBlockPos) {
/*  71 */     this.testBlockPos = paramBlockPos;
/*     */   }
/*     */   
/*     */   public GameTestInfo startExecution(int paramInt) {
/*  75 */     this.tickCount = -(((GameTestInstance)this.test.value()).setupTicks() + paramInt + 1);
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public void placeStructure() {
/*  80 */     if (this.placedStructure) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     TestInstanceBlockEntity testInstanceBlockEntity = getTestInstanceBlockEntity();
/*  85 */     if (!testInstanceBlockEntity.placeStructure()) {
/*  86 */       fail((Component)Component.translatable("test.error.structure.failure", new Object[] { testInstanceBlockEntity.getTestName().getString() }));
/*     */     }
/*     */     
/*  89 */     this.placedStructure = true;
/*     */     
/*  91 */     testInstanceBlockEntity.encaseStructure();
/*  92 */     BoundingBox boundingBox = testInstanceBlockEntity.getStructureBoundingBox();
/*  93 */     this.level.getBlockTicks().clearArea(boundingBox);
/*  94 */     this.level.clearBlockEvents(boundingBox);
/*  95 */     this.listeners.forEach(paramGameTestListener -> paramGameTestListener.testStructureLoaded(this));
/*     */   }
/*     */   
/*     */   public void tick(GameTestRunner paramGameTestRunner) {
/*  99 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     if (!this.placedStructure) {
/* 104 */       fail((Component)Component.translatable("test.error.ticking_without_structure"));
/*     */     }
/*     */     
/* 107 */     if (this.testInstanceBlockEntity == null) {
/* 108 */       fail((Component)Component.translatable("test.error.missing_block_entity"));
/*     */     }
/*     */     
/* 111 */     if (this.error != null) {
/* 112 */       finish();
/*     */     }
/*     */ 
/*     */     
/* 116 */     Objects.requireNonNull(this.level); if (!this.chunksLoaded && !this.testInstanceBlockEntity.getStructureBoundingBox().intersectingChunks().allMatch(this.level::areEntitiesActuallyLoadedAndTicking)) {
/*     */       return;
/*     */     }
/* 119 */     this.chunksLoaded = true;
/*     */ 
/*     */     
/* 122 */     tickInternal();
/*     */     
/* 124 */     if (isDone()) {
/* 125 */       if (this.error != null) {
/* 126 */         this.listeners.forEach(paramGameTestListener -> paramGameTestListener.testFailed(this, paramGameTestRunner));
/*     */       } else {
/* 128 */         this.listeners.forEach(paramGameTestListener -> paramGameTestListener.testPassed(this, paramGameTestRunner));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void tickInternal() {
/* 134 */     this.tickCount++;
/* 135 */     if (this.tickCount < 0) {
/*     */       return;
/*     */     }
/* 138 */     if (!this.started) {
/* 139 */       startTest();
/*     */     }
/* 141 */     ObjectIterator objectIterator = this.runAtTickTimeMap.object2LongEntrySet().iterator();
/* 142 */     while (objectIterator.hasNext()) {
/* 143 */       Object2LongMap.Entry entry = (Object2LongMap.Entry)objectIterator.next();
/* 144 */       if (entry.getLongValue() <= this.tickCount) {
/*     */         try {
/* 146 */           ((Runnable)entry.getKey()).run();
/* 147 */         } catch (GameTestException gameTestException) {
/* 148 */           fail(gameTestException);
/* 149 */         } catch (Exception exception) {
/* 150 */           fail(new UnknownGameTestException(exception));
/*     */         } 
/* 152 */         objectIterator.remove();
/*     */       } 
/*     */     } 
/* 155 */     if (this.tickCount > this.timeoutTicks) {
/*     */       
/* 157 */       if (this.sequences.isEmpty()) {
/* 158 */         fail(new GameTestTimeoutException((Component)Component.translatable("test.error.timeout.no_result", new Object[] { Integer.valueOf(((GameTestInstance)this.test.value()).maxTicks()) })));
/*     */       } else {
/* 160 */         this.sequences.forEach(paramGameTestSequence -> paramGameTestSequence.tickAndFailIfNotComplete(this.tickCount));
/* 161 */         if (this.error == null) {
/* 162 */           fail(new GameTestTimeoutException((Component)Component.translatable("test.error.timeout.no_sequences_finished", new Object[] { Integer.valueOf(((GameTestInstance)this.test.value()).maxTicks()) })));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 167 */       this.sequences.forEach(paramGameTestSequence -> paramGameTestSequence.tickAndContinue(this.tickCount));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startTest() {
/* 172 */     if (this.started) {
/*     */       return;
/*     */     }
/* 175 */     this.started = true;
/* 176 */     this.timer.start();
/* 177 */     getTestInstanceBlockEntity().setRunning();
/*     */     try {
/* 179 */       ((GameTestInstance)this.test.value()).run(new GameTestHelper(this));
/* 180 */     } catch (GameTestException gameTestException) {
/* 181 */       fail(gameTestException);
/* 182 */     } catch (Exception exception) {
/* 183 */       fail(new UnknownGameTestException(exception));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRunAtTickTime(long paramLong, Runnable paramRunnable) {
/* 188 */     this.runAtTickTimeMap.put(paramRunnable, paramLong);
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 192 */     return this.test.key().identifier();
/*     */   }
/*     */   
/*     */   public BlockPos getTestBlockPos() {
/* 196 */     return this.testBlockPos;
/*     */   }
/*     */   
/*     */   public BlockPos getTestOrigin() {
/* 200 */     return this.testInstanceBlockEntity.getStartCorner();
/*     */   }
/*     */   
/*     */   public AABB getStructureBounds() {
/* 204 */     TestInstanceBlockEntity testInstanceBlockEntity = getTestInstanceBlockEntity();
/* 205 */     return testInstanceBlockEntity.getStructureBounds();
/*     */   }
/*     */   
/*     */   public TestInstanceBlockEntity getTestInstanceBlockEntity() {
/* 209 */     if (this.testInstanceBlockEntity == null) {
/* 210 */       if (this.testBlockPos == null) {
/* 211 */         throw new IllegalStateException("This GameTestInfo has no position");
/*     */       }
/*     */       
/* 214 */       BlockEntity blockEntity = this.level.getBlockEntity(this.testBlockPos); if (blockEntity instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity;
/* 215 */         this.testInstanceBlockEntity = testInstanceBlockEntity; }
/*     */       
/* 217 */       if (this.testInstanceBlockEntity == null) {
/* 218 */         throw new IllegalStateException("Could not find a test instance block entity at the given coordinate " + String.valueOf(this.testBlockPos));
/*     */       }
/*     */     } 
/*     */     
/* 222 */     return this.testInstanceBlockEntity;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/* 226 */     return this.level;
/*     */   }
/*     */   
/*     */   public boolean hasSucceeded() {
/* 230 */     return (this.done && this.error == null);
/*     */   }
/*     */   
/*     */   public boolean hasFailed() {
/* 234 */     return (this.error != null);
/*     */   }
/*     */   
/*     */   public boolean hasStarted() {
/* 238 */     return this.started;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 242 */     return this.done;
/*     */   }
/*     */   
/*     */   public long getRunTime() {
/* 246 */     return this.timer.elapsed(TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   private void finish() {
/* 250 */     if (!this.done) {
/* 251 */       this.done = true;
/* 252 */       if (this.timer.isRunning()) {
/* 253 */         this.timer.stop();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void succeed() {
/* 260 */     if (this.error == null) {
/* 261 */       finish();
/* 262 */       AABB aABB = getStructureBounds();
/* 263 */       List list = getLevel().getEntitiesOfClass(Entity.class, aABB.inflate(1.0D), paramEntity -> !(paramEntity instanceof net.minecraft.world.entity.player.Player));
/* 264 */       list.forEach(paramEntity -> paramEntity.remove(Entity.RemovalReason.DISCARDED));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fail(Component paramComponent) {
/* 269 */     fail(new GameTestAssertException(paramComponent, this.tickCount));
/*     */   }
/*     */   
/*     */   public void fail(GameTestException paramGameTestException) {
/* 273 */     this.error = paramGameTestException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GameTestException getError() {
/* 280 */     return this.error;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 285 */     return id().toString();
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener paramGameTestListener) {
/* 289 */     this.listeners.add(paramGameTestListener);
/*     */   }
/*     */   
/*     */   public GameTestInfo prepareTestStructure() {
/* 293 */     TestInstanceBlockEntity testInstanceBlockEntity = createTestInstanceBlock(Objects.<BlockPos>requireNonNull(this.testBlockPos), this.extraRotation, this.level);
/* 294 */     if (testInstanceBlockEntity != null) {
/* 295 */       this.testInstanceBlockEntity = testInstanceBlockEntity;
/* 296 */       placeStructure();
/* 297 */       return this;
/*     */     } 
/* 299 */     return null;
/*     */   }
/*     */   
/*     */   private TestInstanceBlockEntity createTestInstanceBlock(BlockPos paramBlockPos, Rotation paramRotation, ServerLevel paramServerLevel) {
/* 303 */     paramServerLevel.setBlockAndUpdate(paramBlockPos, Blocks.TEST_INSTANCE_BLOCK.defaultBlockState());
/*     */     
/* 305 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity;
/* 306 */       ResourceKey resourceKey = getTestHolder().key();
/* 307 */       Vec3i vec3i = TestInstanceBlockEntity.getStructureSize(paramServerLevel, resourceKey).orElse(new Vec3i(1, 1, 1));
/* 308 */       testInstanceBlockEntity.set(new TestInstanceBlockEntity.Data(Optional.of(resourceKey), vec3i, paramRotation, false, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/* 309 */       return testInstanceBlockEntity; }
/*     */     
/* 311 */     return null;
/*     */   }
/*     */   
/*     */   int getTick() {
/* 315 */     return this.tickCount;
/*     */   }
/*     */   
/*     */   GameTestSequence createSequence() {
/* 319 */     GameTestSequence gameTestSequence = new GameTestSequence(this);
/* 320 */     this.sequences.add(gameTestSequence);
/* 321 */     return gameTestSequence;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 325 */     return ((GameTestInstance)this.test.value()).required();
/*     */   }
/*     */   
/*     */   public boolean isOptional() {
/* 329 */     return !((GameTestInstance)this.test.value()).required();
/*     */   }
/*     */   
/*     */   public Identifier getStructure() {
/* 333 */     return ((GameTestInstance)this.test.value()).structure();
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 337 */     return ((GameTestInstance)this.test.value()).info().rotation().getRotated(this.extraRotation);
/*     */   }
/*     */   
/*     */   public GameTestInstance getTest() {
/* 341 */     return (GameTestInstance)this.test.value();
/*     */   }
/*     */   
/*     */   public Holder.Reference<GameTestInstance> getTestHolder() {
/* 345 */     return this.test;
/*     */   }
/*     */   
/*     */   public int getTimeoutTicks() {
/* 349 */     return this.timeoutTicks;
/*     */   }
/*     */   
/*     */   public boolean isFlaky() {
/* 353 */     return (((GameTestInstance)this.test.value()).maxAttempts() > 1);
/*     */   }
/*     */   
/*     */   public int maxAttempts() {
/* 357 */     return ((GameTestInstance)this.test.value()).maxAttempts();
/*     */   }
/*     */   
/*     */   public int requiredSuccesses() {
/* 361 */     return ((GameTestInstance)this.test.value()).requiredSuccesses();
/*     */   }
/*     */   
/*     */   public RetryOptions retryOptions() {
/* 365 */     return this.retryOptions;
/*     */   }
/*     */   
/*     */   public Stream<GameTestListener> getListeners() {
/* 369 */     return this.listeners.stream();
/*     */   }
/*     */   
/*     */   public GameTestInfo copyReset() {
/* 373 */     GameTestInfo gameTestInfo = new GameTestInfo(this.test, this.extraRotation, this.level, retryOptions());
/* 374 */     if (this.testBlockPos != null) {
/* 375 */       gameTestInfo.setTestBlockPos(this.testBlockPos);
/*     */     }
/* 377 */     return gameTestInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */