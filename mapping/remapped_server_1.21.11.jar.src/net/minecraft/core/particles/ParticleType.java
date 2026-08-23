/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public abstract class ParticleType<T extends ParticleOptions> {
/*    */   private final boolean overrideLimiter;
/*    */   
/*    */   protected ParticleType(boolean paramBoolean) {
/* 11 */     this.overrideLimiter = paramBoolean;
/*    */   }
/*    */   
/*    */   public boolean getOverrideLimiter() {
/* 15 */     return this.overrideLimiter;
/*    */   }
/*    */   
/*    */   public abstract MapCodec<T> codec();
/*    */   
/*    */   public abstract StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\ParticleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */