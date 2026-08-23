/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class BlockPropertyRenameAndFix
/*    */   extends AbstractBlockPropertyFix {
/*    */   private final String blockId;
/*    */   private final String oldPropertyName;
/*    */   private final String newPropertyName;
/*    */   private final UnaryOperator<String> valueFixer;
/*    */   
/*    */   public BlockPropertyRenameAndFix(Schema paramSchema, String paramString1, String paramString2, String paramString3, String paramString4, UnaryOperator<String> paramUnaryOperator) {
/* 15 */     super(paramSchema, paramString1);
/* 16 */     this.blockId = paramString2;
/* 17 */     this.oldPropertyName = paramString3;
/* 18 */     this.newPropertyName = paramString4;
/* 19 */     this.valueFixer = paramUnaryOperator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldFix(String paramString) {
/* 24 */     return paramString.equals(this.blockId);
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixProperties(String paramString, Dynamic<T> paramDynamic) {
/* 29 */     return paramDynamic.renameAndFixField(this.oldPropertyName, this.newPropertyName, paramDynamic -> paramDynamic.createString(this.valueFixer.apply(paramDynamic.asString(""))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockPropertyRenameAndFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */