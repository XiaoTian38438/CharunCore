/*    */ package net.minecraft.world.entity.decoration;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class GlowItemFrame extends ItemFrame {
/*    */   public GlowItemFrame(EntityType<? extends ItemFrame> paramEntityType, Level paramLevel) {
/* 14 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public GlowItemFrame(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 18 */     super(EntityType.GLOW_ITEM_FRAME, paramLevel, paramBlockPos, paramDirection);
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getRemoveItemSound() {
/* 23 */     return SoundEvents.GLOW_ITEM_FRAME_REMOVE_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getBreakSound() {
/* 28 */     return SoundEvents.GLOW_ITEM_FRAME_BREAK;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getPlaceSound() {
/* 33 */     return SoundEvents.GLOW_ITEM_FRAME_PLACE;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getAddItemSound() {
/* 38 */     return SoundEvents.GLOW_ITEM_FRAME_ADD_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getRotateItemSound() {
/* 43 */     return SoundEvents.GLOW_ITEM_FRAME_ROTATE_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getFrameItemStack() {
/* 48 */     return new ItemStack((ItemLike)Items.GLOW_ITEM_FRAME);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\GlowItemFrame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */