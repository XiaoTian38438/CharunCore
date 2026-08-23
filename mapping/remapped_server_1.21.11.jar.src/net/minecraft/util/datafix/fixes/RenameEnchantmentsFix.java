/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RenameEnchantmentsFix extends DataFix {
/*    */   final String name;
/*    */   
/*    */   public RenameEnchantmentsFix(Schema paramSchema, String paramString, Map<String, String> paramMap) {
/* 20 */     super(paramSchema, false);
/* 21 */     this.name = paramString;
/* 22 */     this.renames = paramMap;
/*    */   }
/*    */   final Map<String, String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder opticFinder = type.findField("tag");
/* 29 */     return fixTypeEverywhereTyped(this.name, type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, ()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 33 */     paramDynamic = fixEnchantmentList(paramDynamic, "Enchantments");
/* 34 */     paramDynamic = fixEnchantmentList(paramDynamic, "StoredEnchantments");
/* 35 */     return paramDynamic;
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixEnchantmentList(Dynamic<?> paramDynamic, String paramString) {
/* 39 */     return paramDynamic.update(paramString, paramDynamic -> {
/*    */           Objects.requireNonNull(paramDynamic);
/*    */           return (Dynamic)paramDynamic.asStreamOpt().map(()).map(paramDynamic::createList).mapOrElse(Function.identity(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RenameEnchantmentsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */