/*    */ package net.minecraft.core.dispenser;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ShulkerBoxDispenseBehavior extends OptionalDispenseItemBehavior {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/*    */   protected ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 18 */     setSuccess(false);
/*    */     
/* 20 */     Item item = paramItemStack.getItem();
/* 21 */     if (item instanceof BlockItem) {
/* 22 */       Direction direction1 = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 23 */       BlockPos blockPos = paramBlockSource.pos().relative(direction1);
/*    */       
/* 25 */       Direction direction2 = paramBlockSource.level().isEmptyBlock(blockPos.below()) ? direction1 : Direction.UP;
/*    */       try {
/* 27 */         setSuccess(((BlockItem)item).place((BlockPlaceContext)new DirectionalPlaceContext((Level)paramBlockSource.level(), blockPos, direction1, paramItemStack, direction2)).consumesAction());
/* 28 */       } catch (Exception exception) {
/* 29 */         LOGGER.error("Error trying to place shulker box at {}", blockPos, exception);
/*    */       } 
/*    */     } 
/* 32 */     return paramItemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\ShulkerBoxDispenseBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */