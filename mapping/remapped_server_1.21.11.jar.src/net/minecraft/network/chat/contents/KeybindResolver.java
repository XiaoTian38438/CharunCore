/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class KeybindResolver
/*    */ {
/*    */   static Function<String, Supplier<Component>> keyResolver = paramString -> ();
/*    */   
/*    */   public static void setKeyResolver(Function<String, Supplier<Component>> paramFunction) {
/* 12 */     keyResolver = paramFunction;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\KeybindResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */