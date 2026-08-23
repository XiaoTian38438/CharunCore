/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class SignTextStrictJsonFix
/*    */   extends NamedEntityFix {
/* 13 */   private static final List<String> LINE_FIELDS = List.of("Text1", "Text2", "Text3", "Text4");
/*    */   
/*    */   public SignTextStrictJsonFix(Schema paramSchema) {
/* 16 */     super(paramSchema, false, "SignTextStrictJsonFix", References.BLOCK_ENTITY, "Sign");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 21 */     for (String str : LINE_FIELDS) {
/* 22 */       OpticFinder opticFinder1 = paramTyped.getType().findField(str);
/*    */       
/* 24 */       OpticFinder opticFinder2 = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 25 */       paramTyped = paramTyped.updateTyped(opticFinder1, paramTyped -> paramTyped.update(paramOpticFinder, ()));
/*    */     } 
/*    */ 
/*    */     
/* 29 */     return paramTyped;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\SignTextStrictJsonFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */