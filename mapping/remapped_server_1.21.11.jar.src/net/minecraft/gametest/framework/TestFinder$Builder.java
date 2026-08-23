/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/*    */   private final UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> testFinderWrapper;
/*    */   private final UnaryOperator<Supplier<Stream<BlockPos>>> structureBlockPosFinderWrapper;
/*    */   
/*    */   public Builder() {
/* 32 */     this.testFinderWrapper = (paramSupplier -> paramSupplier);
/* 33 */     this.structureBlockPosFinderWrapper = (paramSupplier -> paramSupplier);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Builder(UnaryOperator<Supplier<Stream<Holder.Reference<GameTestInstance>>>> paramUnaryOperator, UnaryOperator<Supplier<Stream<BlockPos>>> paramUnaryOperator1) {
/* 40 */     this.testFinderWrapper = paramUnaryOperator;
/* 41 */     this.structureBlockPosFinderWrapper = paramUnaryOperator1;
/*    */   }
/*    */   
/*    */   public Builder createMultipleCopies(int paramInt) {
/* 45 */     return new Builder(createCopies(paramInt), createCopies(paramInt));
/*    */   }
/*    */   
/*    */   private static <Q> UnaryOperator<Supplier<Stream<Q>>> createCopies(int paramInt) {
/* 49 */     return paramSupplier -> {
/*    */         LinkedList linkedList = new LinkedList();
/*    */         List list = ((Stream)paramSupplier.get()).toList();
/*    */         for (byte b = 0; b < paramInt; b++) {
/*    */           linkedList.addAll(list);
/*    */         }
/*    */         Objects.requireNonNull(linkedList);
/*    */         return linkedList::stream;
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   private TestFinder build(CommandSourceStack paramCommandSourceStack, TestInstanceFinder paramTestInstanceFinder, TestPosFinder paramTestPosFinder) {
/* 62 */     Objects.requireNonNull(paramTestInstanceFinder); Objects.requireNonNull(this.testFinderWrapper.apply(paramTestInstanceFinder::findTests));
/* 63 */     Objects.requireNonNull(paramTestPosFinder); Objects.requireNonNull(this.structureBlockPosFinderWrapper.apply(paramTestPosFinder::findTestPos)); return new TestFinder(paramCommandSourceStack, (Supplier)this.testFinderWrapper.apply(paramTestInstanceFinder::findTests)::get, (Supplier)this.structureBlockPosFinderWrapper.apply(paramTestPosFinder::findTestPos)::get);
/*    */   }
/*    */ 
/*    */   
/*    */   public TestFinder radius(CommandContext<CommandSourceStack> paramCommandContext, int paramInt) {
/* 68 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 69 */     BlockPos blockPos = BlockPos.containing((Position)commandSourceStack.getPosition());
/* 70 */     return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(paramBlockPos, paramInt, paramCommandSourceStack.getLevel()));
/*    */   }
/*    */   
/*    */   public TestFinder nearest(CommandContext<CommandSourceStack> paramCommandContext) {
/* 74 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 75 */     BlockPos blockPos = BlockPos.containing((Position)commandSourceStack.getPosition());
/* 76 */     return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findNearestTest(paramBlockPos, 15, paramCommandSourceStack.getLevel()).stream());
/*    */   }
/*    */   
/*    */   public TestFinder allNearby(CommandContext<CommandSourceStack> paramCommandContext) {
/* 80 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 81 */     BlockPos blockPos = BlockPos.containing((Position)commandSourceStack.getPosition());
/* 82 */     return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.findTestBlocks(paramBlockPos, 250, paramCommandSourceStack.getLevel()));
/*    */   }
/*    */   
/*    */   public TestFinder lookedAt(CommandContext<CommandSourceStack> paramCommandContext) {
/* 86 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 87 */     return build(commandSourceStack, TestFinder.NO_FUNCTIONS, () -> StructureUtils.lookedAtTestPos(BlockPos.containing((Position)paramCommandSourceStack.getPosition()), paramCommandSourceStack.getPlayer().getCamera(), paramCommandSourceStack.getLevel()));
/*    */   }
/*    */   
/*    */   public TestFinder failedTests(CommandContext<CommandSourceStack> paramCommandContext, boolean paramBoolean) {
/* 91 */     return build((CommandSourceStack)paramCommandContext.getSource(), () -> FailedTestTracker.getLastFailedTests().filter(()), TestFinder.NO_STRUCTURES);
/*    */   }
/*    */   
/*    */   public TestFinder byResourceSelection(CommandContext<CommandSourceStack> paramCommandContext, Collection<Holder.Reference<GameTestInstance>> paramCollection) {
/* 95 */     Objects.requireNonNull(paramCollection); return build((CommandSourceStack)paramCommandContext.getSource(), paramCollection::stream, TestFinder.NO_STRUCTURES);
/*    */   }
/*    */   
/*    */   public TestFinder failedTests(CommandContext<CommandSourceStack> paramCommandContext) {
/* 99 */     return failedTests(paramCommandContext, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\TestFinder$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */