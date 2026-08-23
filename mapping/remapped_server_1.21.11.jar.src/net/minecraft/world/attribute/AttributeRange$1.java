/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements AttributeRange<Value>
/*    */ {
/*    */   public DataResult<Value> validate(Value paramValue) {
/* 14 */     return DataResult.success(paramValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public Value sanitize(Value paramValue) {
/* 19 */     return paramValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\AttributeRange$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */