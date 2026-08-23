/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class WrittenBookPagesStrictJsonFix extends ItemStackTagFix {
/*    */   public WrittenBookPagesStrictJsonFix(Schema paramSchema) {
/* 13 */     super(paramSchema, "WrittenBookPagesStrictJsonFix", paramString -> paramString.equals("minecraft:written_book"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> paramTyped) {
/* 19 */     Type type1 = getInputSchema().getType(References.TEXT_COMPONENT);
/*    */     
/* 21 */     Type type2 = getInputSchema().getType(References.ITEM_STACK);
/* 22 */     OpticFinder opticFinder1 = type2.findField("tag");
/* 23 */     OpticFinder opticFinder2 = opticFinder1.type().findField("pages");
/* 24 */     OpticFinder opticFinder3 = DSL.typeFinder(type1);
/*    */     
/* 26 */     return paramTyped.updateTyped(opticFinder2, paramTyped -> paramTyped.update(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\WrittenBookPagesStrictJsonFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */