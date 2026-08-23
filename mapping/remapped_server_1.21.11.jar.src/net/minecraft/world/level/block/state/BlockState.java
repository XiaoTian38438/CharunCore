/*    */ package net.minecraft.world.level.block.state;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ public class BlockState
/*    */   extends BlockBehaviour.BlockStateBase
/*    */ {
/* 14 */   public static final Codec<BlockState> CODEC = codec(BuiltInRegistries.BLOCK.byNameCodec(), Block::defaultBlockState).stable();
/*    */   
/*    */   public BlockState(Block paramBlock, Reference2ObjectArrayMap<Property<?>, Comparable<?>> paramReference2ObjectArrayMap, MapCodec<BlockState> paramMapCodec) {
/* 17 */     super(paramBlock, paramReference2ObjectArrayMap, paramMapCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState asState() {
/* 22 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\BlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */