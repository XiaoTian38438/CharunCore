/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class InfestedRotatedPillarBlock extends InfestedBlock {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("host").forGetter(InfestedBlock::getHostBlock), (App)propertiesCodec()).apply((Applicative)paramInstance, InfestedRotatedPillarBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<InfestedRotatedPillarBlock> CODEC;
/*    */   
/*    */   public MapCodec<InfestedRotatedPillarBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public InfestedRotatedPillarBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 23 */     super(paramBlock, paramProperties);
/* 24 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 29 */     return RotatedPillarBlock.rotatePillar(paramBlockState, paramRotation);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 34 */     paramBuilder.add(new Property[] { (Property)RotatedPillarBlock.AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 39 */     return (BlockState)defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)paramBlockPlaceContext.getClickedFace().getAxis());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\InfestedRotatedPillarBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */