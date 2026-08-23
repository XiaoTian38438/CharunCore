/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class TestInstanceBlock extends BaseEntityBlock implements GameMasterBlock {
/* 15 */   public static final MapCodec<TestInstanceBlock> CODEC = simpleCodec(TestInstanceBlock::new);
/*    */   
/*    */   public TestInstanceBlock(BlockBehaviour.Properties paramProperties) {
/* 18 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 23 */     return (BlockEntity)new TestInstanceBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*    */     TestInstanceBlockEntity testInstanceBlockEntity;
/* 28 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 29 */     if (blockEntity instanceof TestInstanceBlockEntity) { testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity; }
/* 30 */     else { return (InteractionResult)InteractionResult.PASS; }
/*    */     
/* 32 */     if (!paramPlayer.canUseGameMasterBlocks())
/*    */     {
/* 34 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/* 36 */     if (paramPlayer.level().isClientSide()) {
/* 37 */       paramPlayer.openTestInstanceBlock(testInstanceBlockEntity);
/*    */     }
/* 39 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<TestInstanceBlock> codec() {
/* 44 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TestInstanceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */