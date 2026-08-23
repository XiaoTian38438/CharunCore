/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.StringTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements Action.ValueGetter
/*    */ {
/*    */   public String asTemplateSubstitution() {
/* 51 */     return value.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public Tag asTag() {
/* 56 */     return (Tag)StringTag.valueOf(value.get());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\action\Action$ValueGetter$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */