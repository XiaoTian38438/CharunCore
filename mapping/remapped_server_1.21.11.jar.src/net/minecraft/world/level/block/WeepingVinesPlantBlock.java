/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WeepingVinesPlantBlock extends GrowingPlantBodyBlock {
/*  8 */   public static final MapCodec<WeepingVinesPlantBlock> CODEC = simpleCodec(WeepingVinesPlantBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WeepingVinesPlantBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   
/* 15 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/*    */   public WeepingVinesPlantBlock(BlockBehaviour.Properties paramProperties) {
/* 18 */     super(paramProperties, Direction.DOWN, SHAPE, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 23 */     return (GrowingPlantHeadBlock)Blocks.WEEPING_VINES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeepingVinesPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */