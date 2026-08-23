/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class PlayerWallHeadBlock extends WallSkullBlock {
/*  6 */   public static final MapCodec<PlayerWallHeadBlock> CODEC = simpleCodec(PlayerWallHeadBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<PlayerWallHeadBlock> codec() {
/* 10 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected PlayerWallHeadBlock(BlockBehaviour.Properties paramProperties) {
/* 14 */     super(SkullBlock.Types.PLAYER, paramProperties);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PlayerWallHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */