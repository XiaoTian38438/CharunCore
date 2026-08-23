/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class WriteAndReadFix extends DataFix {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public WriteAndReadFix(Schema paramSchema, String paramString, DSL.TypeReference paramTypeReference) {
/* 13 */     super(paramSchema, true);
/* 14 */     this.name = paramString;
/* 15 */     this.type = paramTypeReference;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     return writeAndRead(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WriteAndReadFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */