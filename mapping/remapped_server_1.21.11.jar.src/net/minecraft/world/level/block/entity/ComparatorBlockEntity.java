/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class ComparatorBlockEntity
/*    */   extends BlockEntity {
/*    */   private static final int DEFAULT_OUTPUT = 0;
/* 11 */   private int output = 0;
/*    */   
/*    */   public ComparatorBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 14 */     super(BlockEntityType.COMPARATOR, paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 19 */     super.saveAdditional(paramValueOutput);
/* 20 */     paramValueOutput.putInt("OutputSignal", this.output);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loadAdditional(ValueInput paramValueInput) {
/* 25 */     super.loadAdditional(paramValueInput);
/* 26 */     this.output = paramValueInput.getIntOr("OutputSignal", 0);
/*    */   }
/*    */   
/*    */   public int getOutputSignal() {
/* 30 */     return this.output;
/*    */   }
/*    */   
/*    */   public void setOutputSignal(int paramInt) {
/* 34 */     this.output = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\ComparatorBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */