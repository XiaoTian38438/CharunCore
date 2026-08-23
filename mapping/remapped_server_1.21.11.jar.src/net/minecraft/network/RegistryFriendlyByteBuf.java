/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ 
/*    */ public class RegistryFriendlyByteBuf
/*    */   extends FriendlyByteBuf {
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   public RegistryFriendlyByteBuf(ByteBuf paramByteBuf, RegistryAccess paramRegistryAccess) {
/* 12 */     super(paramByteBuf);
/* 13 */     this.registryAccess = paramRegistryAccess;
/*    */   }
/*    */   
/*    */   public RegistryAccess registryAccess() {
/* 17 */     return this.registryAccess;
/*    */   }
/*    */   
/*    */   public static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess paramRegistryAccess) {
/* 21 */     return paramByteBuf -> new RegistryFriendlyByteBuf(paramByteBuf, paramRegistryAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\RegistryFriendlyByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */