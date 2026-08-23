/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.resources.HolderSetCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistryCodecs {
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> paramResourceKey, Codec<E> paramCodec) {
/* 11 */     return homogeneousList(paramResourceKey, paramCodec, false);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> paramResourceKey, Codec<E> paramCodec, boolean paramBoolean) {
/* 15 */     return HolderSetCodec.create(paramResourceKey, (Codec)RegistryFileCodec.create(paramResourceKey, paramCodec), paramBoolean);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> paramResourceKey) {
/* 19 */     return homogeneousList(paramResourceKey, false);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> paramResourceKey, boolean paramBoolean) {
/* 23 */     return HolderSetCodec.create(paramResourceKey, (Codec)RegistryFixedCodec.create(paramResourceKey), paramBoolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistryCodecs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */