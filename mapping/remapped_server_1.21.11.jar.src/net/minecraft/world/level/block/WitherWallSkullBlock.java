/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WitherWallSkullBlock extends WallSkullBlock {
/* 12 */   public static final MapCodec<WitherWallSkullBlock> CODEC = simpleCodec(WitherWallSkullBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WitherWallSkullBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected WitherWallSkullBlock(BlockBehaviour.Properties paramProperties) {
/* 20 */     super(SkullBlock.Types.WITHER_SKELETON, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 25 */     WitherSkullBlock.checkSpawn(paramLevel, paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WitherWallSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */