/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.AnvilMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class AnvilBlock extends FallingBlock {
/*  33 */   public static final MapCodec<AnvilBlock> CODEC = simpleCodec(AnvilBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<AnvilBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  42 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or(
/*  43 */         Block.column(12.0D, 0.0D, 4.0D), new VoxelShape[] {
/*  44 */           Block.column(8.0D, 10.0D, 4.0D, 5.0D), 
/*  45 */           Block.column(4.0D, 8.0D, 5.0D, 10.0D), 
/*  46 */           Block.column(10.0D, 16.0D, 10.0D, 16.0D)
/*     */         }));
/*     */   
/*  49 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.repair");
/*     */   private static final float FALL_DAMAGE_PER_DISTANCE = 2.0F;
/*     */   private static final int FALL_DAMAGE_MAX = 40;
/*     */   
/*     */   public AnvilBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  60 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getClockWise());
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  65 */     if (!paramLevel.isClientSide()) {
/*  66 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/*  67 */       paramPlayer.awardStat(Stats.INTERACT_WITH_ANVIL);
/*     */     } 
/*  69 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  74 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new AnvilMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  79 */     return SHAPES.get(((Direction)paramBlockState.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void falling(FallingBlockEntity paramFallingBlockEntity) {
/*  84 */     paramFallingBlockEntity.setHurtsEntities(2.0F, 40);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLand(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, FallingBlockEntity paramFallingBlockEntity) {
/*  89 */     if (!paramFallingBlockEntity.isSilent()) {
/*  90 */       paramLevel.levelEvent(1031, paramBlockPos, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBrokenAfterFall(Level paramLevel, BlockPos paramBlockPos, FallingBlockEntity paramFallingBlockEntity) {
/*  96 */     if (!paramFallingBlockEntity.isSilent()) {
/*  97 */       paramLevel.levelEvent(1029, paramBlockPos, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public DamageSource getFallDamageSource(Entity paramEntity) {
/* 103 */     return paramEntity.damageSources().anvil(paramEntity);
/*     */   }
/*     */   
/*     */   public static BlockState damage(BlockState paramBlockState) {
/* 107 */     if (paramBlockState.is(Blocks.ANVIL)) {
/* 108 */       return (BlockState)Blocks.CHIPPED_ANVIL.defaultBlockState().setValue((Property)FACING, paramBlockState.getValue((Property)FACING));
/*     */     }
/* 110 */     if (paramBlockState.is(Blocks.CHIPPED_ANVIL)) {
/* 111 */       return (BlockState)Blocks.DAMAGED_ANVIL.defaultBlockState().setValue((Property)FACING, paramBlockState.getValue((Property)FACING));
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 118 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 123 */     paramBuilder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 133 */     return (paramBlockState.getMapColor(paramBlockGetter, paramBlockPos)).col;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AnvilBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */