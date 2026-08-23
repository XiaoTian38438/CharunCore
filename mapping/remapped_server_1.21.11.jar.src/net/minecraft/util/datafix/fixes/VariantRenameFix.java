/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class VariantRenameFix extends NamedEntityFix {
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public VariantRenameFix(Schema paramSchema, String paramString1, DSL.TypeReference paramTypeReference, String paramString2, Map<String, String> paramMap) {
/* 14 */     super(paramSchema, false, paramString1, paramTypeReference, paramString2);
/* 15 */     this.renames = paramMap;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 20 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("variant", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\VariantRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */