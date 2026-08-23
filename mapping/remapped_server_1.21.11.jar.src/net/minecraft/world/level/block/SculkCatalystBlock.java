/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SculkCatalystBlock extends BaseEntityBlock {
/* 22 */   public static final MapCodec<SculkCatalystBlock> CODEC = simpleCodec(SculkCatalystBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SculkCatalystBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/* 29 */   public static final BooleanProperty PULSE = BlockStateProperties.BLOOM;
/* 30 */   private final IntProvider xpRange = (IntProvider)ConstantInt.of(5);
/*    */   
/*    */   public SculkCatalystBlock(BlockBehaviour.Properties paramProperties) {
/* 33 */     super(paramProperties);
/*    */     
/* 35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PULSE, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 40 */     paramBuilder.add(new Property[] { (Property)PULSE });
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 45 */     if (((Boolean)paramBlockState.getValue((Property)PULSE)).booleanValue()) {
/* 46 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)PULSE, Boolean.valueOf(false)), 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 52 */     return (BlockEntity)new SculkCatalystBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 57 */     if (paramLevel.isClientSide()) {
/* 58 */       return null;
/*    */     }
/* 60 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.SCULK_CATALYST, SculkCatalystBlockEntity::serverTick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 65 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/* 66 */     if (paramBoolean)
/* 67 */       tryDropExperience(paramServerLevel, paramBlockPos, paramItemStack, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkCatalystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */