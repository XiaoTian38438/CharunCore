/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Context
/*    */ {
/*    */   private final LevelSimulatedReader level;
/*    */   private final BiConsumer<BlockPos, BlockState> decorationSetter;
/*    */   private final RandomSource random;
/*    */   private final ObjectArrayList<BlockPos> logs;
/*    */   private final ObjectArrayList<BlockPos> leaves;
/*    */   private final ObjectArrayList<BlockPos> roots;
/*    */   
/*    */   public Context(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, Set<BlockPos> paramSet1, Set<BlockPos> paramSet2, Set<BlockPos> paramSet3) {
/* 34 */     this.level = paramLevelSimulatedReader;
/* 35 */     this.decorationSetter = paramBiConsumer;
/* 36 */     this.random = paramRandomSource;
/*    */     
/* 38 */     this.roots = new ObjectArrayList(paramSet3);
/* 39 */     this.logs = new ObjectArrayList(paramSet1);
/* 40 */     this.leaves = new ObjectArrayList(paramSet2);
/*    */     
/* 42 */     this.logs.sort(Comparator.comparingInt(Vec3i::getY));
/* 43 */     this.leaves.sort(Comparator.comparingInt(Vec3i::getY));
/* 44 */     this.roots.sort(Comparator.comparingInt(Vec3i::getY));
/*    */   }
/*    */   
/*    */   public void placeVine(BlockPos paramBlockPos, BooleanProperty paramBooleanProperty) {
/* 48 */     setBlock(paramBlockPos, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)paramBooleanProperty, Boolean.valueOf(true)));
/*    */   }
/*    */   
/*    */   public void setBlock(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 52 */     this.decorationSetter.accept(paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   public boolean isAir(BlockPos paramBlockPos) {
/* 56 */     return this.level.isStateAtPosition(paramBlockPos, BlockBehaviour.BlockStateBase::isAir);
/*    */   }
/*    */   
/*    */   public boolean checkBlock(BlockPos paramBlockPos, Predicate<BlockState> paramPredicate) {
/* 60 */     return this.level.isStateAtPosition(paramBlockPos, paramPredicate);
/*    */   }
/*    */   
/*    */   public LevelSimulatedReader level() {
/* 64 */     return this.level;
/*    */   }
/*    */   
/*    */   public RandomSource random() {
/* 68 */     return this.random;
/*    */   }
/*    */   
/*    */   public ObjectArrayList<BlockPos> logs() {
/* 72 */     return this.logs;
/*    */   }
/*    */   
/*    */   public ObjectArrayList<BlockPos> leaves() {
/* 76 */     return this.leaves;
/*    */   }
/*    */   
/*    */   public ObjectArrayList<BlockPos> roots() {
/* 80 */     return this.roots;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TreeDecorator$Context.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */