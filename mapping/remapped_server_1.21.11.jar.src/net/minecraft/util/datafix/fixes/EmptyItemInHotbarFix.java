/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class EmptyItemInHotbarFix extends DataFix {
/*    */   public EmptyItemInHotbarFix(Schema paramSchema) {
/* 17 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     OpticFinder opticFinder = DSL.typeFinder(getInputSchema().getType(References.ITEM_STACK));
/*    */     
/* 25 */     return fixTypeEverywhereTyped("EmptyItemInHotbarFix", getInputSchema().getType(References.HOTBAR), paramTyped -> paramTyped.update(paramOpticFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EmptyItemInHotbarFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */