/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockParticleOption implements ParticleOptions {
/* 13 */   private static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.withAlternative(BlockState.CODEC, BuiltInRegistries.BLOCK
/*    */       
/* 15 */       .byNameCodec(), Block::defaultBlockState); private final ParticleType<BlockParticleOption> type;
/*    */   private final BlockState state;
/*    */   
/*    */   public static MapCodec<BlockParticleOption> codec(ParticleType<BlockParticleOption> paramParticleType) {
/* 19 */     return BLOCK_STATE_CODEC.xmap(paramBlockState -> new BlockParticleOption(paramParticleType, paramBlockState), paramBlockParticleOption -> paramBlockParticleOption.state).fieldOf("block_state");
/*    */   }
/*    */   
/*    */   public static StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> streamCodec(ParticleType<BlockParticleOption> paramParticleType) {
/* 23 */     return ByteBufCodecs.idMapper((IdMap)Block.BLOCK_STATE_REGISTRY).map(paramBlockState -> new BlockParticleOption(paramParticleType, paramBlockState), paramBlockParticleOption -> paramBlockParticleOption.state);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockParticleOption(ParticleType<BlockParticleOption> paramParticleType, BlockState paramBlockState) {
/* 30 */     this.type = paramParticleType;
/* 31 */     this.state = paramBlockState;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<BlockParticleOption> getType() {
/* 36 */     return this.type;
/*    */   }
/*    */   
/*    */   public BlockState getState() {
/* 40 */     return this.state;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\BlockParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */