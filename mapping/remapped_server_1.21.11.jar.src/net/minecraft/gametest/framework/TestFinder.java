/*     */ package net.minecraft.gametest.framework;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ 
/*     */ public class TestFinder implements TestInstanceFinder, TestPosFinder {
/*  16 */   static final TestInstanceFinder NO_FUNCTIONS = Stream::empty;
/*  17 */   static final TestPosFinder NO_STRUCTURES = Stream::empty;
/*     */   
/*     */   private final TestInstanceFinder testInstanceFinder;
/*     */   private final TestPosFinder testPosFinder;
/*     */   private final CommandSourceStack source;
/*     */   
/*     */   public Stream<BlockPos> findTestPos() {
/*  24 */     return this.testPosFinder.findTestPos();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> testFinderWrapper;
/*     */     private final UnaryOperator<Supplier<Stream<BlockPos>>> structureBlockPosFinderWrapper;
/*     */     
/*     */     public Builder() {
/*  32 */       this.testFinderWrapper = (param1Supplier -> param1Supplier);
/*  33 */       this.structureBlockPosFinderWrapper = (param1Supplier -> param1Supplier);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Builder(UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> param1UnaryOperator, UnaryOperator<Supplier<Stream<BlockPos>>> param1UnaryOperator1) {
/*  40 */       this.testFinderWrapper = param1UnaryOperator;
/*  41 */       this.structureBlockPosFinderWrapper = param1UnaryOperator1;
/*     */     }
/*     */     
/*     */     public Builder createMultipleCopies(int param1Int) {
/*  45 */       return new Builder(createCopies(param1Int), createCopies(param1Int));
/*     */     }
/*     */     
/*     */     private static <Q> UnaryOperator<Supplier<Stream<Q>>> createCopies(int param1Int) {
/*  49 */       return param1Supplier -> {
/*     */           LinkedList linkedList = new LinkedList();
/*     */           List list = ((Stream)param1Supplier.get()).toList();
/*     */           for (byte b = 0; b < param1Int; b++) {
/*     */             linkedList.addAll(list);
/*     */           }
/*     */           Objects.requireNonNull(linkedList);
/*     */           return linkedList::stream;
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     private TestFinder build(CommandSourceStack param1CommandSourceStack, TestInstanceFinder param1TestInstanceFinder, TestPosFinder param1TestPosFinder) {
/*  62 */       Objects.requireNonNull(param1TestInstanceFinder); Objects.requireNonNull(this.testFinderWrapper.apply(param1TestInstanceFinder::findTests));
/*  63 */       Objects.requireNonNull(param1TestPosFinder); Objects.requireNonNull(this.structureBlockPosFinderWrapper.apply(param1TestPosFinder::findTestPos)); return new TestFinder(param1CommandSourceStack, (Supplier)this.testFinderWrapper.apply(param1TestInstanceFinder::findTests)::get, (Supplier)this.structureBlockPosFinderWrapper.apply(param1TestPosFinder::findTestPos)::get);
/*     */     }
/*     */ 
/*     */     
/*     */     public TestFinder radius(CommandContext<CommandSourceStack> param1CommandContext, int param1Int) {
/*  68 */       CommandSourceStack commandSourceStack = (CommandSourceStack)param1CommandContext.getSource();
/*  69 */       BlockPos blockPos = BlockPos.containing((Position)commandSourceStack.getPosition());
/*  70 */       return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(param1BlockPos, param1Int, param1CommandSourceStack.getLevel()));
/*     */     }
/*     */     
/*     */     public TestFinder nearest(CommandContext<CommandSourceStack> param1CommandContext) {
/*  74 */       CommandSourceStack commandSourceStack = (CommandSourceStack)param1CommandContext.getSource();
/*  75 */       BlockPos blockPos = BlockPos.containing((Position)commandSourceStack.getPosition());
/*  76 */       return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findNearestTest(param1BlockPos, 15, param1CommandSourceStack.getLevel()).stream());
/*     */     }
/*     */     
/*     */     public TestFinder allNearby(CommandContext<CommandSourceStack> param1CommandContext) {
/*  80 */       CommandSourceStack commandSourceStack = (CommandSourceStack)param1CommandContext.getSource();
/*  81 */       BlockPos blockPos = BlockPos.containing((Position)commandSourceStack.getPosition());
/*  82 */       return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(param1BlockPos, 250, param1CommandSourceStack.getLevel()));
/*     */     }
/*     */     
/*     */     public TestFinder lookedAt(CommandContext<CommandSourceStack> param1CommandContext) {
/*  86 */       CommandSourceStack commandSourceStack = (CommandSourceStack)param1CommandContext.getSource();
/*  87 */       return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.lookedAtTestPos(BlockPos.containing((Position)param1CommandSourceStack.getPosition()), param1CommandSourceStack.getPlayer().getCamera(), param1CommandSourceStack.getLevel()));
/*     */     }
/*     */     
/*     */     public TestFinder failedTests(CommandContext<CommandSourceStack> param1CommandContext, boolean param1Boolean) {
/*  91 */       return build((CommandSourceStack)param1CommandContext.getSource(), () -> FailedTestTracker.getLastFailedTests().filter(()), TestFinder.NO_STRUCTURES);
/*     */     }
/*     */     
/*     */     public TestFinder byResourceSelection(CommandContext<CommandSourceStack> param1CommandContext, Collection<Holder.Reference<GameTestInstance>> param1Collection) {
/*  95 */       Objects.requireNonNull(param1Collection); return build((CommandSourceStack)param1CommandContext.getSource(), param1Collection::stream, TestFinder.NO_STRUCTURES);
/*     */     }
/*     */     
/*     */     public TestFinder failedTests(CommandContext<CommandSourceStack> param1CommandContext) {
/*  99 */       return failedTests(param1CommandContext, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 104 */     return new Builder();
/*     */   }
/*     */   
/*     */   TestFinder(CommandSourceStack paramCommandSourceStack, TestInstanceFinder paramTestInstanceFinder, TestPosFinder paramTestPosFinder) {
/* 108 */     this.source = paramCommandSourceStack;
/* 109 */     this.testInstanceFinder = paramTestInstanceFinder;
/* 110 */     this.testPosFinder = paramTestPosFinder;
/*     */   }
/*     */   
/*     */   public CommandSourceStack source() {
/* 114 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<Holder.Reference<GameTestInstance>> findTests() {
/* 119 */     return this.testInstanceFinder.findTests();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\TestFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */