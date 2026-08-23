/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class TestBlock extends BaseEntityBlock implements GameMasterBlock {
/*  30 */   public static final MapCodec<TestBlock> CODEC = simpleCodec(TestBlock::new);
/*  31 */   public static final EnumProperty<TestBlockMode> MODE = BlockStateProperties.TEST_BLOCK_MODE;
/*     */   
/*     */   public TestBlock(BlockBehaviour.Properties paramProperties) {
/*  34 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  39 */     return (BlockEntity)new TestBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  44 */     BlockItemStateProperties blockItemStateProperties = (BlockItemStateProperties)paramBlockPlaceContext.getItemInHand().get(DataComponents.BLOCK_STATE);
/*  45 */     BlockState blockState = defaultBlockState();
/*  46 */     if (blockItemStateProperties != null) {
/*  47 */       TestBlockMode testBlockMode = (TestBlockMode)blockItemStateProperties.get((Property)MODE);
/*  48 */       if (testBlockMode != null) {
/*  49 */         blockState = (BlockState)blockState.setValue((Property)MODE, (Comparable)testBlockMode);
/*     */       }
/*     */     } 
/*  52 */     return blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  57 */     paramBuilder.add(new Property[] { (Property)MODE });
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*     */     TestBlockEntity testBlockEntity;
/*  62 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  63 */     if (blockEntity instanceof TestBlockEntity) { testBlockEntity = (TestBlockEntity)blockEntity; }
/*  64 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */     
/*  66 */     if (!paramPlayer.canUseGameMasterBlocks())
/*     */     {
/*  68 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*  70 */     if (paramLevel.isClientSide()) {
/*  71 */       paramPlayer.openTestBlock(testBlockEntity);
/*     */     }
/*  73 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  78 */     TestBlockEntity testBlockEntity = getServerTestBlockEntity((Level)paramServerLevel, paramBlockPos);
/*  79 */     if (testBlockEntity == null) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     testBlockEntity.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  88 */     TestBlockEntity testBlockEntity = getServerTestBlockEntity(paramLevel, paramBlockPos);
/*  89 */     if (testBlockEntity == null) {
/*     */       return;
/*     */     }
/*  92 */     if (testBlockEntity.getMode() == TestBlockMode.START) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     boolean bool1 = paramLevel.hasNeighborSignal(paramBlockPos);
/*  97 */     boolean bool2 = testBlockEntity.isPowered();
/*     */     
/*  99 */     if (bool1 && !bool2) {
/* 100 */       testBlockEntity.setPowered(true);
/* 101 */       testBlockEntity.trigger();
/* 102 */     } else if (!bool1 && bool2) {
/* 103 */       testBlockEntity.setPowered(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static TestBlockEntity getServerTestBlockEntity(Level paramLevel, BlockPos paramBlockPos) {
/* 108 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; BlockEntity blockEntity = serverLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof TestBlockEntity) return (TestBlockEntity)blockEntity;
/*     */        }
/*     */     
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 116 */     if (paramBlockState.getValue((Property)MODE) != TestBlockMode.START) {
/* 117 */       return 0;
/*     */     }
/* 119 */     BlockEntity blockEntity = paramBlockGetter.getBlockEntity(paramBlockPos);
/* 120 */     if (blockEntity instanceof TestBlockEntity) { TestBlockEntity testBlockEntity = (TestBlockEntity)blockEntity;
/* 121 */       return testBlockEntity.isPowered() ? 15 : 0; }
/*     */     
/* 123 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 128 */     ItemStack itemStack = super.getCloneItemStack(paramLevelReader, paramBlockPos, paramBlockState, paramBoolean);
/* 129 */     return setModeOnStack(itemStack, (TestBlockMode)paramBlockState.getValue((Property)MODE));
/*     */   }
/*     */   
/*     */   public static ItemStack setModeOnStack(ItemStack paramItemStack, TestBlockMode paramTestBlockMode) {
/* 133 */     paramItemStack.set(DataComponents.BLOCK_STATE, ((BlockItemStateProperties)paramItemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY)).with((Property)MODE, (Comparable)paramTestBlockMode));
/* 134 */     return paramItemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<TestBlock> codec() {
/* 139 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */