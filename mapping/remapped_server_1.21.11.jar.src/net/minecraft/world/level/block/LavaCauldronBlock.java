/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.cauldron.CauldronInteraction;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.entity.InsideBlockEffectType;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class LavaCauldronBlock extends AbstractCauldronBlock {
/* 18 */   public static final MapCodec<LavaCauldronBlock> CODEC = simpleCodec(LavaCauldronBlock::new);
/* 19 */   private static final VoxelShape SHAPE_INSIDE = Block.column(12.0D, 4.0D, 15.0D);
/* 20 */   private static final VoxelShape FILLED_SHAPE = Shapes.or(AbstractCauldronBlock.SHAPE, SHAPE_INSIDE);
/*    */ 
/*    */   
/*    */   public MapCodec<LavaCauldronBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public LavaCauldronBlock(BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramProperties, CauldronInteraction.LAVA);
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getContentHeight(BlockState paramBlockState) {
/* 33 */     return 0.9375D;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull(BlockState paramBlockState) {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getEntityInsideCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/* 43 */     return FILLED_SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 48 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 49 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
/* 50 */     paramInsideBlockEffectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 55 */     return 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LavaCauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */