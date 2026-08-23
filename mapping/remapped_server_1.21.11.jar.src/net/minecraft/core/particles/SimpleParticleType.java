/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class SimpleParticleType extends ParticleType<SimpleParticleType> implements ParticleOptions {
/*  8 */   private final MapCodec<SimpleParticleType> codec = MapCodec.unit(this::getType);
/*    */   
/* 10 */   private final StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec = StreamCodec.unit(this);
/*    */   
/*    */   protected SimpleParticleType(boolean paramBoolean) {
/* 13 */     super(paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public SimpleParticleType getType() {
/* 18 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<SimpleParticleType> codec() {
/* 23 */     return this.codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec() {
/* 28 */     return this.streamCodec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\SimpleParticleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */