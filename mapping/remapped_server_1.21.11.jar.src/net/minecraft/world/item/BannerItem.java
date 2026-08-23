/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class BannerItem extends StandingAndWallBlockItem {
/*    */   public BannerItem(Block paramBlock1, Block paramBlock2, Item.Properties paramProperties) {
/* 10 */     super(paramBlock1, paramBlock2, Direction.DOWN, paramProperties);
/*    */     
/* 12 */     Validate.isInstanceOf(AbstractBannerBlock.class, paramBlock1);
/* 13 */     Validate.isInstanceOf(AbstractBannerBlock.class, paramBlock2);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 17 */     return ((AbstractBannerBlock)getBlock()).getColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BannerItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */