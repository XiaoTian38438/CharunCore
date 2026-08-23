/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface EntityDataSerializer<T> {
/*    */   StreamCodec<? super RegistryFriendlyByteBuf, T> codec();
/*    */   
/*    */   default EntityDataAccessor<T> createAccessor(int paramInt) {
/* 10 */     return new EntityDataAccessor<>(paramInt, this);
/*    */   }
/*    */   
/*    */   T copy(T paramT);
/*    */   
/*    */   public static interface ForValueType<T>
/*    */     extends EntityDataSerializer<T> {
/*    */     default T copy(T param1T) {
/* 18 */       return param1T;
/*    */     }
/*    */   }
/*    */   
/*    */   static <T> EntityDataSerializer<T> forValueType(StreamCodec<? super RegistryFriendlyByteBuf, T> paramStreamCodec) {
/* 23 */     return () -> paramStreamCodec;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\syncher\EntityDataSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */