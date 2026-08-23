/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.Codec;
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
/*    */ public abstract class TreeDecorator {
/* 19 */   public static final Codec<TreeDecorator> CODEC = BuiltInRegistries.TREE_DECORATOR_TYPE.byNameCodec().dispatch(TreeDecorator::type, TreeDecoratorType::codec);
/*    */   
/*    */   protected abstract TreeDecoratorType<?> type();
/*    */   
/*    */   public abstract void place(Context paramContext);
/*    */   
/*    */   public static final class Context {
/*    */     private final LevelSimulatedReader level;
/*    */     private final BiConsumer<BlockPos, BlockState> decorationSetter;
/*    */     private final RandomSource random;
/*    */     private final ObjectArrayList<BlockPos> logs;
/*    */     private final ObjectArrayList<BlockPos> leaves;
/*    */     private final ObjectArrayList<BlockPos> roots;
/*    */     
/*    */     public Context(LevelSimulatedReader param1LevelSimulatedReader, BiConsumer<BlockPos, BlockState> param1BiConsumer, RandomSource param1RandomSource, Set<BlockPos> param1Set1, Set<BlockPos> param1Set2, Set<BlockPos> param1Set3) {
/* 34 */       this.level = param1LevelSimulatedReader;
/* 35 */       this.decorationSetter = param1BiConsumer;
/* 36 */       this.random = param1RandomSource;
/*    */       
/* 38 */       this.roots = new ObjectArrayList(param1Set3);
/* 39 */       this.logs = new ObjectArrayList(param1Set1);
/* 40 */       this.leaves = new ObjectArrayList(param1Set2);
/*    */       
/* 42 */       this.logs.sort(Comparator.comparingInt(Vec3i::getY));
/* 43 */       this.leaves.sort(Comparator.comparingInt(Vec3i::getY));
/* 44 */       this.roots.sort(Comparator.comparingInt(Vec3i::getY));
/*    */     }
/*    */     
/*    */     public void placeVine(BlockPos param1BlockPos, BooleanProperty param1BooleanProperty) {
/* 48 */       setBlock(param1BlockPos, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)param1BooleanProperty, Boolean.valueOf(true)));
/*    */     }
/*    */     
/*    */     public void setBlock(BlockPos param1BlockPos, BlockState param1BlockState) {
/* 52 */       this.decorationSetter.accept(param1BlockPos, param1BlockState);
/*    */     }
/*    */     
/*    */     public boolean isAir(BlockPos param1BlockPos) {
/* 56 */       return this.level.isStateAtPosition(param1BlockPos, BlockBehaviour.BlockStateBase::isAir);
/*    */     }
/*    */     
/*    */     public boolean checkBlock(BlockPos param1BlockPos, Predicate<BlockState> param1Predicate) {
/* 60 */       return this.level.isStateAtPosition(param1BlockPos, param1Predicate);
/*    */     }
/*    */     
/*    */     public LevelSimulatedReader level() {
/* 64 */       return this.level;
/*    */     }
/*    */     
/*    */     public RandomSource random() {
/* 68 */       return this.random;
/*    */     }
/*    */     
/*    */     public ObjectArrayList<BlockPos> logs() {
/* 72 */       return this.logs;
/*    */     }
/*    */     
/*    */     public ObjectArrayList<BlockPos> leaves() {
/* 76 */       return this.leaves;
/*    */     }
/*    */     
/*    */     public ObjectArrayList<BlockPos> roots() {
/* 80 */       return this.roots;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TreeDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */