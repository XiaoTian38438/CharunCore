/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ public class PoweredRailBlock extends BaseRailBlock {
/*  15 */   public static final MapCodec<PoweredRailBlock> CODEC = simpleCodec(PoweredRailBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<PoweredRailBlock> codec() {
/*  19 */     return CODEC;
/*     */   }
/*     */   
/*  22 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
/*  23 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   protected PoweredRailBlock(BlockBehaviour.Properties paramProperties) {
/*  26 */     super(true, paramProperties);
/*  27 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected boolean findPoweredRailSignal(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean, int paramInt) {
/*  31 */     if (paramInt >= 8) {
/*  32 */       return false;
/*     */     }
/*     */     
/*  35 */     int i = paramBlockPos.getX();
/*  36 */     int j = paramBlockPos.getY();
/*  37 */     int k = paramBlockPos.getZ();
/*     */     
/*  39 */     boolean bool = true;
/*  40 */     RailShape railShape = (RailShape)paramBlockState.getValue((Property)SHAPE);
/*  41 */     switch (railShape) {
/*     */       case NORTH_SOUTH:
/*  43 */         if (paramBoolean) {
/*  44 */           k++; break;
/*     */         } 
/*  46 */         k--;
/*     */         break;
/*     */       
/*     */       case EAST_WEST:
/*  50 */         if (paramBoolean) {
/*  51 */           i--; break;
/*     */         } 
/*  53 */         i++;
/*     */         break;
/*     */       
/*     */       case ASCENDING_EAST:
/*  57 */         if (paramBoolean) {
/*  58 */           i--;
/*     */         } else {
/*  60 */           i++;
/*  61 */           j++;
/*  62 */           bool = false;
/*     */         } 
/*  64 */         railShape = RailShape.EAST_WEST;
/*     */         break;
/*     */       case ASCENDING_WEST:
/*  67 */         if (paramBoolean) {
/*  68 */           i--;
/*  69 */           j++;
/*  70 */           bool = false;
/*     */         } else {
/*  72 */           i++;
/*     */         } 
/*  74 */         railShape = RailShape.EAST_WEST;
/*     */         break;
/*     */       case ASCENDING_NORTH:
/*  77 */         if (paramBoolean) {
/*  78 */           k++;
/*     */         } else {
/*  80 */           k--;
/*  81 */           j++;
/*  82 */           bool = false;
/*     */         } 
/*  84 */         railShape = RailShape.NORTH_SOUTH;
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/*  87 */         if (paramBoolean) {
/*  88 */           k++;
/*  89 */           j++;
/*  90 */           bool = false;
/*     */         } else {
/*  92 */           k--;
/*     */         } 
/*  94 */         railShape = RailShape.NORTH_SOUTH;
/*     */         break;
/*     */     } 
/*     */     
/*  98 */     if (isSameRailWithPower(paramLevel, new BlockPos(i, j, k), paramBoolean, paramInt, railShape)) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (bool && isSameRailWithPower(paramLevel, new BlockPos(i, j - 1, k), paramBoolean, paramInt, railShape)) {
/* 102 */       return true;
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isSameRailWithPower(Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean, int paramInt, RailShape paramRailShape) {
/* 108 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */     
/* 110 */     if (!blockState.is(this)) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     RailShape railShape = (RailShape)blockState.getValue((Property)SHAPE);
/* 115 */     if (paramRailShape == RailShape.EAST_WEST && (railShape == RailShape.NORTH_SOUTH || railShape == RailShape.ASCENDING_NORTH || railShape == RailShape.ASCENDING_SOUTH)) {
/* 116 */       return false;
/*     */     }
/* 118 */     if (paramRailShape == RailShape.NORTH_SOUTH && (railShape == RailShape.EAST_WEST || railShape == RailShape.ASCENDING_EAST || railShape == RailShape.ASCENDING_WEST)) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if (((Boolean)blockState.getValue((Property)POWERED)).booleanValue()) {
/* 123 */       if (paramLevel.hasNeighborSignal(paramBlockPos)) {
/* 124 */         return true;
/*     */       }
/* 126 */       return findPoweredRailSignal(paramLevel, paramBlockPos, blockState, paramBoolean, paramInt + 1);
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock) {
/* 134 */     boolean bool1 = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/* 135 */     boolean bool2 = (paramLevel.hasNeighborSignal(paramBlockPos) || findPoweredRailSignal(paramLevel, paramBlockPos, paramBlockState, true, 0) || findPoweredRailSignal(paramLevel, paramBlockPos, paramBlockState, false, 0));
/*     */     
/* 137 */     if (bool2 != bool1) {
/* 138 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(bool2)), 3);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       paramLevel.updateNeighborsAt(paramBlockPos.below(), this);
/* 144 */       if (((RailShape)paramBlockState.getValue((Property)SHAPE)).isSlope()) {
/* 145 */         paramLevel.updateNeighborsAt(paramBlockPos.above(), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Property<RailShape> getShapeProperty() {
/* 152 */     return (Property<RailShape>)SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 157 */     RailShape railShape1 = (RailShape)paramBlockState.getValue((Property)SHAPE);
/* 158 */     RailShape railShape2 = rotate(railShape1, paramRotation);
/* 159 */     return (BlockState)paramBlockState.setValue((Property)SHAPE, (Comparable)railShape2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 164 */     RailShape railShape1 = (RailShape)paramBlockState.getValue((Property)SHAPE);
/* 165 */     RailShape railShape2 = mirror(railShape1, paramMirror);
/* 166 */     return (BlockState)paramBlockState.setValue((Property)SHAPE, (Comparable)railShape2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 171 */     paramBuilder.add(new Property[] { (Property)SHAPE, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PoweredRailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */