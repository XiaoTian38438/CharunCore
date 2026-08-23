/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ContextKey<T>
/*    */ {
/*    */   private final Identifier name;
/*    */   
/*    */   public ContextKey(Identifier paramIdentifier) {
/* 10 */     this.name = paramIdentifier;
/*    */   }
/*    */   
/*    */   public static <T> ContextKey<T> vanilla(String paramString) {
/* 14 */     return new ContextKey<>(Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public Identifier name() {
/* 18 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return "<parameter " + String.valueOf(this.name) + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\context\ContextKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */