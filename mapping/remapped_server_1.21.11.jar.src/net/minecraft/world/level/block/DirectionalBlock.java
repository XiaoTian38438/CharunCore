/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ 
/*    */ public abstract class DirectionalBlock extends Block {
/*  9 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/*    */   
/*    */   protected DirectionalBlock(BlockBehaviour.Properties paramProperties) {
/* 12 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends DirectionalBlock> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */