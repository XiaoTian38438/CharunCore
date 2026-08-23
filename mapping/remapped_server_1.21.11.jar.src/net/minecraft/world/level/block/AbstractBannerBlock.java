/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class AbstractBannerBlock
/*    */   extends BaseEntityBlock {
/*    */   protected AbstractBannerBlock(DyeColor paramDyeColor, BlockBehaviour.Properties paramProperties) {
/* 16 */     super(paramProperties);
/* 17 */     this.color = paramDyeColor;
/*    */   }
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractBannerBlock> codec();
/*    */   
/*    */   public boolean isPossibleToRespawnInThis(BlockState paramBlockState) {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 30 */     return (BlockEntity)new BannerBlockEntity(paramBlockPos, paramBlockState, this.color);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 35 */     BlockEntity blockEntity = paramLevelReader.getBlockEntity(paramBlockPos); if (blockEntity instanceof BannerBlockEntity) { BannerBlockEntity bannerBlockEntity = (BannerBlockEntity)blockEntity;
/* 36 */       return bannerBlockEntity.getItem(); }
/*    */ 
/*    */     
/* 39 */     return super.getCloneItemStack(paramLevelReader, paramBlockPos, paramBlockState, paramBoolean);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 43 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AbstractBannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */