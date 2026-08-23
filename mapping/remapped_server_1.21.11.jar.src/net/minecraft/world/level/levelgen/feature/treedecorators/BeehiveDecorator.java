/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BeehiveDecorator extends TreeDecorator {
/*    */   static {
/* 20 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(BeehiveDecorator::new, paramBeehiveDecorator -> Float.valueOf(paramBeehiveDecorator.probability));
/*    */   }
/* 22 */   public static final MapCodec<BeehiveDecorator> CODEC; private static final Direction WORLDGEN_FACING = Direction.SOUTH; private static final Direction[] SPAWN_DIRECTIONS; private final float probability; static {
/* 23 */     SPAWN_DIRECTIONS = (Direction[])Direction.Plane.HORIZONTAL.stream().filter(paramDirection -> (paramDirection != WORLDGEN_FACING.getOpposite())).toArray(paramInt -> new Direction[paramInt]);
/*    */   }
/*    */ 
/*    */   
/*    */   public BeehiveDecorator(float paramFloat) {
/* 28 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 33 */     return TreeDecoratorType.BEEHIVE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 41 */     ObjectArrayList<BlockPos> objectArrayList1 = paramContext.leaves();
/* 42 */     ObjectArrayList<BlockPos> objectArrayList2 = paramContext.logs();
/*    */     
/* 44 */     if (objectArrayList2.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     RandomSource randomSource = paramContext.random();
/* 49 */     if (randomSource.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     int i = !objectArrayList1.isEmpty() ? Math.max(((BlockPos)objectArrayList1.getFirst()).getY() - 1, ((BlockPos)objectArrayList2.getFirst()).getY() + 1) : Math.min(((BlockPos)objectArrayList2.getFirst()).getY() + 1 + randomSource.nextInt(3), ((BlockPos)objectArrayList2.getLast()).getY());
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     List list = (List)objectArrayList2.stream().filter(paramBlockPos -> (paramBlockPos.getY() == paramInt)).flatMap(paramBlockPos -> { Objects.requireNonNull(paramBlockPos); return Stream.<Direction>of(SPAWN_DIRECTIONS).map(paramBlockPos::relative); }).collect(Collectors.toList());
/* 59 */     if (list.isEmpty()) {
/*    */       return;
/*    */     }
/* 62 */     Util.shuffle(list, randomSource);
/*    */ 
/*    */     
/* 65 */     Optional<BlockPos> optional = list.stream().filter(paramBlockPos -> (paramContext.isAir(paramBlockPos) && paramContext.isAir(paramBlockPos.relative(WORLDGEN_FACING)))).findFirst();
/* 66 */     if (optional.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 70 */     paramContext.setBlock(optional.get(), (BlockState)Blocks.BEE_NEST.defaultBlockState().setValue((Property)BeehiveBlock.FACING, (Comparable)WORLDGEN_FACING));
/* 71 */     paramContext.level().getBlockEntity(optional.get(), BlockEntityType.BEEHIVE).ifPresent(paramBeehiveBlockEntity -> {
/*    */           int i = 2 + paramRandomSource.nextInt(2);
/*    */           for (byte b = 0; b < i; b++)
/*    */             paramBeehiveBlockEntity.storeBee(BeehiveBlockEntity.Occupant.create(paramRandomSource.nextInt(599))); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\BeehiveDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */