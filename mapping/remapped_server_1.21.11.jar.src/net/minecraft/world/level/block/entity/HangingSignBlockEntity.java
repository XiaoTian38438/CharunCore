/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HangingSignBlockEntity extends SignBlockEntity {
/*    */   private static final int MAX_TEXT_LINE_WIDTH = 60;
/*    */   private static final int TEXT_LINE_HEIGHT = 9;
/*    */   
/*    */   public HangingSignBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 13 */     super(BlockEntityType.HANGING_SIGN, paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTextLineHeight() {
/* 18 */     return 9;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxTextLineWidth() {
/* 23 */     return 60;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getSignInteractionFailedSoundEvent() {
/* 28 */     return SoundEvents.WAXED_HANGING_SIGN_INTERACT_FAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\HangingSignBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */