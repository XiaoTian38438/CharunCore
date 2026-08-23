/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultState;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class VaultBlock extends BaseEntityBlock {
/* 28 */   public static final MapCodec<VaultBlock> CODEC = simpleCodec(VaultBlock::new);
/* 29 */   public static final Property<VaultState> STATE = (Property<VaultState>)BlockStateProperties.VAULT_STATE;
/* 30 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/* 31 */   public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;
/*    */ 
/*    */   
/*    */   public MapCodec<VaultBlock> codec() {
/* 35 */     return CODEC;
/*    */   }
/*    */   
/*    */   public VaultBlock(BlockBehaviour.Properties paramProperties) {
/* 39 */     super(paramProperties);
/* 40 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue(STATE, (Comparable)VaultState.INACTIVE)).setValue((Property)OMINOUS, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 45 */     if (paramItemStack.isEmpty() || paramBlockState.getValue(STATE) != VaultState.ACTIVE) {
/* 46 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*    */     }
/*    */     
/* 49 */     if (paramLevel instanceof ServerLevel) { VaultBlockEntity vaultBlockEntity; ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 50 */       BlockEntity blockEntity = serverLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof VaultBlockEntity) { vaultBlockEntity = (VaultBlockEntity)blockEntity; }
/* 51 */       else { return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; }
/*    */       
/* 53 */       VaultBlockEntity.Server.tryInsertKey(serverLevel, paramBlockPos, paramBlockState, vaultBlockEntity.getConfig(), vaultBlockEntity.getServerData(), vaultBlockEntity.getSharedData(), paramPlayer, paramItemStack); }
/*    */     
/* 55 */     return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 60 */     return (BlockEntity)new VaultBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 65 */     paramBuilder.add(new Property[] { (Property)FACING, STATE, (Property)OMINOUS });
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 70 */     ServerLevel serverLevel = (ServerLevel)paramLevel; return (paramLevel instanceof ServerLevel) ? 
/* 71 */       createTickerHelper(paramBlockEntityType, BlockEntityType.VAULT, (paramLevel, paramBlockPos, paramBlockState, paramVaultBlockEntity) -> VaultBlockEntity.Server.tick(paramServerLevel, paramBlockPos, paramBlockState, paramVaultBlockEntity.getConfig(), paramVaultBlockEntity.getServerData(), paramVaultBlockEntity.getSharedData())) : 
/* 72 */       createTickerHelper(paramBlockEntityType, BlockEntityType.VAULT, (paramLevel, paramBlockPos, paramBlockState, paramVaultBlockEntity) -> VaultBlockEntity.Client.tick(paramLevel, paramBlockPos, paramBlockState, paramVaultBlockEntity.getClientData(), paramVaultBlockEntity.getSharedData()));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 77 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 82 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 87 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\VaultBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */