/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class NamedEntityConvertUncheckedFix extends NamedEntityFix {
/*    */   public NamedEntityConvertUncheckedFix(Schema paramSchema, String paramString1, DSL.TypeReference paramTypeReference, String paramString2) {
/* 11 */     super(paramSchema, true, paramString1, paramTypeReference, paramString2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 17 */     Type type = getOutputSchema().getChoiceType(this.type, this.entityName);
/* 18 */     return ExtraDataFixUtils.cast(type, paramTyped);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\NamedEntityConvertUncheckedFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */