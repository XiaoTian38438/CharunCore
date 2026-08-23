/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.HashSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DefaultRedstoneWireEvaluator
/*    */   extends RedstoneWireEvaluator {
/*    */   public DefaultRedstoneWireEvaluator(RedStoneWireBlock paramRedStoneWireBlock) {
/* 16 */     super(paramRedStoneWireBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updatePowerStrength(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Orientation paramOrientation, boolean paramBoolean) {
/* 21 */     int i = calculateTargetStrength(paramLevel, paramBlockPos);
/*    */     
/* 23 */     if (((Integer)paramBlockState.getValue((Property)RedStoneWireBlock.POWER)).intValue() != i) {
/* 24 */       if (paramLevel.getBlockState(paramBlockPos) == paramBlockState) {
/* 25 */         paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)RedStoneWireBlock.POWER, Integer.valueOf(i)), 2);
/*    */       }
/*    */ 
/*    */       
/* 29 */       HashSet<BlockPos> hashSet = Sets.newHashSet();
/* 30 */       hashSet.add(paramBlockPos);
/* 31 */       for (Direction direction : Direction.values()) {
/* 32 */         hashSet.add(paramBlockPos.relative(direction));
/*    */       }
/* 34 */       for (BlockPos blockPos : hashSet) {
/* 35 */         paramLevel.updateNeighborsAt(blockPos, (Block)this.wireBlock);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private int calculateTargetStrength(Level paramLevel, BlockPos paramBlockPos) {
/* 41 */     int i = getBlockSignal(paramLevel, paramBlockPos);
/* 42 */     if (i == 15) {
/* 43 */       return i;
/*    */     }
/*    */     
/* 46 */     return Math.max(i, getIncomingWireSignal(paramLevel, paramBlockPos));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\redstone\DefaultRedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */