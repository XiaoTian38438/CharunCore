/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class TrialSpawnerBlock extends BaseEntityBlock {
/* 20 */   public static final MapCodec<TrialSpawnerBlock> CODEC = simpleCodec(TrialSpawnerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TrialSpawnerBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   public static final EnumProperty<TrialSpawnerState> STATE = BlockStateProperties.TRIAL_SPAWNER_STATE;
/* 28 */   public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;
/*    */   
/*    */   public TrialSpawnerBlock(BlockBehaviour.Properties paramProperties) {
/* 31 */     super(paramProperties);
/* 32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STATE, (Comparable)TrialSpawnerState.INACTIVE)).setValue((Property)OMINOUS, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 37 */     paramBuilder.add(new Property[] { (Property)STATE, (Property)OMINOUS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 42 */     return (BlockEntity)new TrialSpawnerBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 47 */     ServerLevel serverLevel = (ServerLevel)paramLevel; return (paramLevel instanceof ServerLevel) ? 
/* 48 */       createTickerHelper(paramBlockEntityType, BlockEntityType.TRIAL_SPAWNER, (paramLevel, paramBlockPos, paramBlockState, paramTrialSpawnerBlockEntity) -> paramTrialSpawnerBlockEntity.getTrialSpawner().tickServer(paramServerLevel, paramBlockPos, ((Boolean)paramBlockState.getOptionalValue((Property)BlockStateProperties.OMINOUS).orElse(Boolean.valueOf(false))).booleanValue())) : 
/* 49 */       createTickerHelper(paramBlockEntityType, BlockEntityType.TRIAL_SPAWNER, (paramLevel, paramBlockPos, paramBlockState, paramTrialSpawnerBlockEntity) -> paramTrialSpawnerBlockEntity.getTrialSpawner().tickClient(paramLevel, paramBlockPos, ((Boolean)paramBlockState.getOptionalValue((Property)BlockStateProperties.OMINOUS).orElse(Boolean.valueOf(false))).booleanValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TrialSpawnerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */