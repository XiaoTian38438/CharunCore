/*    */ package net.minecraft.server.dialog.action;
/*    */ 
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
/*    */ class null
/*    */   implements Action.ValueGetter
/*    */ {
/*    */   public String asTemplateSubstitution() {
/* 37 */     return value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Tag asTag() {
/* 42 */     return (Tag)StringTag.valueOf(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\dialog\action\Action$ValueGetter$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */