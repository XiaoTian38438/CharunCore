/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class JigsawBlock extends Block implements EntityBlock, GameMasterBlock {
/*  21 */   public static final MapCodec<JigsawBlock> CODEC = simpleCodec(JigsawBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<JigsawBlock> codec() {
/*  25 */     return CODEC;
/*     */   }
/*     */   
/*  28 */   public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
/*     */   
/*     */   protected JigsawBlock(BlockBehaviour.Properties paramProperties) {
/*  31 */     super(paramProperties);
/*  32 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ORIENTATION, (Comparable)FrontAndTop.NORTH_UP));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  37 */     paramBuilder.add(new Property[] { (Property)ORIENTATION });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  42 */     return (BlockState)paramBlockState.setValue((Property)ORIENTATION, (Comparable)paramRotation.rotation().rotate((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/*  47 */     return (BlockState)paramBlockState.setValue((Property)ORIENTATION, (Comparable)paramMirror.rotation().rotate((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  52 */     Direction direction2, direction1 = paramBlockPlaceContext.getClickedFace();
/*     */     
/*  54 */     if (direction1.getAxis() == Direction.Axis.Y) {
/*  55 */       direction2 = paramBlockPlaceContext.getHorizontalDirection().getOpposite();
/*     */     } else {
/*  57 */       direction2 = Direction.UP;
/*     */     } 
/*     */     
/*  60 */     return (BlockState)defaultBlockState().setValue((Property)ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(direction1, direction2));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  65 */     return (BlockEntity)new JigsawBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/*  70 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  71 */     if (blockEntity instanceof JigsawBlockEntity && paramPlayer.canUseGameMasterBlocks()) {
/*  72 */       paramPlayer.openJigsawBlock((JigsawBlockEntity)blockEntity);
/*  73 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/*  77 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static boolean canAttach(StructureTemplate.JigsawBlockInfo paramJigsawBlockInfo1, StructureTemplate.JigsawBlockInfo paramJigsawBlockInfo2) {
/*  81 */     Direction direction1 = getFrontFacing(paramJigsawBlockInfo1.info().state());
/*  82 */     Direction direction2 = getFrontFacing(paramJigsawBlockInfo2.info().state());
/*  83 */     Direction direction3 = getTopFacing(paramJigsawBlockInfo1.info().state());
/*  84 */     Direction direction4 = getTopFacing(paramJigsawBlockInfo2.info().state());
/*     */ 
/*     */     
/*  87 */     JigsawBlockEntity.JointType jointType = paramJigsawBlockInfo1.jointType();
/*  88 */     boolean bool = (jointType == JigsawBlockEntity.JointType.ROLLABLE) ? true : false;
/*     */     
/*  90 */     return (direction1 == direction2.getOpposite() && (bool || direction3 == direction4) && paramJigsawBlockInfo1
/*     */       
/*  92 */       .target().equals(paramJigsawBlockInfo2.name()));
/*     */   }
/*     */   
/*     */   public static Direction getFrontFacing(BlockState paramBlockState) {
/*  96 */     return ((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)).front();
/*     */   }
/*     */   
/*     */   public static Direction getTopFacing(BlockState paramBlockState) {
/* 100 */     return ((FrontAndTop)paramBlockState.getValue((Property)ORIENTATION)).top();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\JigsawBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */