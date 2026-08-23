/*     */ package net.minecraft.commands.execution;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ExecutionContext<T>
/*     */   implements AutoCloseable
/*     */ {
/*     */   private static final int MAX_QUEUE_DEPTH = 10000000;
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final int commandLimit;
/*     */   
/*     */   private final int forkLimit;
/*     */   private final ProfilerFiller profiler;
/*     */   private TraceCallbacks tracer;
/*     */   private int commandQuota;
/*     */   private boolean queueOverflow;
/*  31 */   private final Deque<CommandQueueEntry<T>> commandQueue = Queues.newArrayDeque();
/*  32 */   private final List<CommandQueueEntry<T>> newTopCommands = (List<CommandQueueEntry<T>>)new ObjectArrayList();
/*     */   private int currentFrameDepth;
/*     */   
/*     */   public ExecutionContext(int paramInt1, int paramInt2, ProfilerFiller paramProfilerFiller) {
/*  36 */     this.commandLimit = paramInt1;
/*  37 */     this.forkLimit = paramInt2;
/*  38 */     this.profiler = paramProfilerFiller;
/*     */     
/*  40 */     this.commandQuota = paramInt1;
/*     */   }
/*     */   
/*     */   private static <T extends ExecutionCommandSource<T>> Frame createTopFrame(ExecutionContext<T> paramExecutionContext, CommandResultCallback paramCommandResultCallback) {
/*  44 */     if (paramExecutionContext.currentFrameDepth == 0) {
/*  45 */       Objects.requireNonNull(paramExecutionContext.commandQueue); return new Frame(0, paramCommandResultCallback, paramExecutionContext.commandQueue::clear);
/*     */     } 
/*  47 */     int i = paramExecutionContext.currentFrameDepth + 1;
/*  48 */     return new Frame(i, paramCommandResultCallback, paramExecutionContext.frameControlForDepth(i));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends ExecutionCommandSource<T>> void queueInitialFunctionCall(ExecutionContext<T> paramExecutionContext, InstantiatedFunction<T> paramInstantiatedFunction, T paramT, CommandResultCallback paramCommandResultCallback) {
/*  53 */     paramExecutionContext.queueNext(new CommandQueueEntry<>(createTopFrame(paramExecutionContext, paramCommandResultCallback), (new CallFunction(paramInstantiatedFunction, paramT.callback(), false)).bind(paramT)));
/*     */   }
/*     */   
/*     */   public static <T extends ExecutionCommandSource<T>> void queueInitialCommandExecution(ExecutionContext<T> paramExecutionContext, String paramString, ContextChain<T> paramContextChain, T paramT, CommandResultCallback paramCommandResultCallback) {
/*  57 */     paramExecutionContext.queueNext(new CommandQueueEntry<>(createTopFrame(paramExecutionContext, paramCommandResultCallback), (EntryAction<T>)new BuildContexts.TopLevel(paramString, paramContextChain, (ExecutionCommandSource)paramT)));
/*     */   }
/*     */   
/*     */   private void handleQueueOverflow() {
/*  61 */     this.queueOverflow = true;
/*     */     
/*  63 */     this.newTopCommands.clear();
/*  64 */     this.commandQueue.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueNext(CommandQueueEntry<T> paramCommandQueueEntry) {
/*  70 */     if (this.newTopCommands.size() + this.commandQueue.size() > 10000000) {
/*  71 */       handleQueueOverflow();
/*     */     }
/*     */     
/*  74 */     if (!this.queueOverflow) {
/*  75 */       this.newTopCommands.add(paramCommandQueueEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardAtDepthOrHigher(int paramInt) {
/*  81 */     while (!this.commandQueue.isEmpty() && ((CommandQueueEntry)this.commandQueue.peek()).frame().depth() >= paramInt) {
/*  82 */       this.commandQueue.removeFirst();
/*     */     }
/*     */   }
/*     */   
/*     */   public Frame.FrameControl frameControlForDepth(int paramInt) {
/*  87 */     return () -> discardAtDepthOrHigher(paramInt);
/*     */   }
/*     */   
/*     */   public void runCommandQueue() {
/*  91 */     pushNewCommands();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  96 */       if (this.commandQuota <= 0) {
/*  97 */         LOGGER.info("Command execution stopped due to limit (executed {} commands)", Integer.valueOf(this.commandLimit));
/*     */         
/*     */         break;
/*     */       } 
/* 101 */       CommandQueueEntry commandQueueEntry = this.commandQueue.pollFirst();
/* 102 */       if (commandQueueEntry == null) {
/*     */         return;
/*     */       }
/* 105 */       this.currentFrameDepth = commandQueueEntry.frame().depth();
/* 106 */       commandQueueEntry.execute(this);
/*     */       
/* 108 */       if (this.queueOverflow) {
/* 109 */         LOGGER.error("Command execution stopped due to command queue overflow (max {})", Integer.valueOf(10000000));
/*     */         
/*     */         break;
/*     */       } 
/* 113 */       pushNewCommands();
/*     */     } 
/* 115 */     this.currentFrameDepth = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void pushNewCommands() {
/* 120 */     for (int i = this.newTopCommands.size() - 1; i >= 0; i--) {
/* 121 */       this.commandQueue.addFirst(this.newTopCommands.get(i));
/*     */     }
/* 123 */     this.newTopCommands.clear();
/*     */   }
/*     */   
/*     */   public void tracer(TraceCallbacks paramTraceCallbacks) {
/* 127 */     this.tracer = paramTraceCallbacks;
/*     */   }
/*     */   
/*     */   public TraceCallbacks tracer() {
/* 131 */     return this.tracer;
/*     */   }
/*     */   
/*     */   public ProfilerFiller profiler() {
/* 135 */     return this.profiler;
/*     */   }
/*     */   
/*     */   public int forkLimit() {
/* 139 */     return this.forkLimit;
/*     */   }
/*     */   
/*     */   public void incrementCost() {
/* 143 */     this.commandQuota--;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 148 */     if (this.tracer != null)
/* 149 */       this.tracer.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\ExecutionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */