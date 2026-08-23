/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ 
/*    */ public class LeaveVineDecorator extends TreeDecorator {
/*    */   public static final MapCodec<LeaveVineDecorator> CODEC;
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 13 */     return TreeDecoratorType.LEAVE_VINE;
/*    */   } private final float probability;
/*    */   static {
/* 16 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(LeaveVineDecorator::new, paramLeaveVineDecorator -> Float.valueOf(paramLeaveVineDecorator.probability));
/*    */   }
/*    */ 
/*    */   
/*    */   public LeaveVineDecorator(float paramFloat) {
/* 21 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 26 */     RandomSource randomSource = paramContext.random();
/* 27 */     paramContext.leaves().forEach(paramBlockPos -> {
/*    */           if (paramRandomSource.nextFloat() < this.probability) {
/*    */             BlockPos blockPos = paramBlockPos.west();
/*    */             if (paramContext.isAir(blockPos)) {
/*    */               addHangingVine(blockPos, VineBlock.EAST, paramContext);
/*    */             }
/*    */           } 
/*    */           if (paramRandomSource.nextFloat() < this.probability) {
/*    */             BlockPos blockPos = paramBlockPos.east();
/*    */             if (paramContext.isAir(blockPos)) {
/*    */               addHangingVine(blockPos, VineBlock.WEST, paramContext);
/*    */             }
/*    */           } 
/*    */           if (paramRandomSource.nextFloat() < this.probability) {
/*    */             BlockPos blockPos = paramBlockPos.north();
/*    */             if (paramContext.isAir(blockPos)) {
/*    */               addHangingVine(blockPos, VineBlock.SOUTH, paramContext);
/*    */             }
/*    */           } 
/*    */           if (paramRandomSource.nextFloat() < this.probability) {
/*    */             BlockPos blockPos = paramBlockPos.south();
/*    */             if (paramContext.isAir(blockPos)) {
/*    */               addHangingVine(blockPos, VineBlock.NORTH, paramContext);
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void addHangingVine(BlockPos paramBlockPos, BooleanProperty paramBooleanProperty, TreeDecorator.Context paramContext) {
/* 59 */     paramContext.placeVine(paramBlockPos, paramBooleanProperty);
/* 60 */     byte b = 4;
/*    */     
/* 62 */     paramBlockPos = paramBlockPos.below();
/* 63 */     while (paramContext.isAir(paramBlockPos) && b > 0) {
/* 64 */       paramContext.placeVine(paramBlockPos, paramBooleanProperty);
/* 65 */       paramBlockPos = paramBlockPos.below();
/* 66 */       b--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\LeaveVineDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */