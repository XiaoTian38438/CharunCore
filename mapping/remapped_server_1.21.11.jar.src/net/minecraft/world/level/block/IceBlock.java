/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class IceBlock extends HalfTransparentBlock {
/* 19 */   public static final MapCodec<IceBlock> CODEC = simpleCodec(IceBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends IceBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/*    */   public IceBlock(BlockBehaviour.Properties paramProperties) {
/* 27 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   public static BlockState meltsInto() {
/* 31 */     return Blocks.WATER.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void playerDestroy(Level paramLevel, Player paramPlayer, BlockPos paramBlockPos, BlockState paramBlockState, BlockEntity paramBlockEntity, ItemStack paramItemStack) {
/* 36 */     super.playerDestroy(paramLevel, paramPlayer, paramBlockPos, paramBlockState, paramBlockEntity, paramItemStack);
/*    */     
/* 38 */     if (!EnchantmentHelper.hasTag(paramItemStack, EnchantmentTags.PREVENTS_ICE_MELTING)) {
/* 39 */       if (((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, paramBlockPos)).booleanValue()) {
/* 40 */         paramLevel.removeBlock(paramBlockPos, false);
/*    */         
/*    */         return;
/*    */       } 
/* 44 */       BlockState blockState = paramLevel.getBlockState(paramBlockPos.below());
/* 45 */       if (blockState.blocksMotion() || blockState.liquid()) {
/* 46 */         paramLevel.setBlockAndUpdate(paramBlockPos, meltsInto());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 53 */     if (paramServerLevel.getBrightness(LightLayer.BLOCK, paramBlockPos) > 11 - paramBlockState.getLightBlock()) {
/* 54 */       melt(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void melt(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 59 */     if (((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, paramBlockPos)).booleanValue()) {
/* 60 */       paramLevel.removeBlock(paramBlockPos, false);
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     paramLevel.setBlockAndUpdate(paramBlockPos, meltsInto());
/* 65 */     paramLevel.neighborChanged(paramBlockPos, meltsInto().getBlock(), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\IceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */