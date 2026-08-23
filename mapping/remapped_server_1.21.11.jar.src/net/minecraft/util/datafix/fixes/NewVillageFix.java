/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.CompoundList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class NewVillageFix extends DataFix {
/*    */   public NewVillageFix(Schema paramSchema, boolean paramBoolean) {
/* 22 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     CompoundList.CompoundListType<String, ?> compoundListType = DSL.compoundList(DSL.string(), getInputSchema().getType(References.STRUCTURE_FEATURE));
/* 28 */     OpticFinder opticFinder = compoundListType.finder();
/*    */     
/* 30 */     return cap(compoundListType);
/*    */   }
/*    */   
/*    */   private <SF> TypeRewriteRule cap(CompoundList.CompoundListType<String, SF> paramCompoundListType) {
/* 34 */     Type type1 = getInputSchema().getType(References.CHUNK);
/* 35 */     Type type2 = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 36 */     OpticFinder opticFinder1 = type1.findField("Level");
/* 37 */     OpticFinder opticFinder2 = opticFinder1.type().findField("Structures");
/* 38 */     OpticFinder opticFinder3 = opticFinder2.type().findField("Starts");
/* 39 */     OpticFinder opticFinder4 = paramCompoundListType.finder();
/* 40 */     return TypeRewriteRule.seq(
/* 41 */         fixTypeEverywhereTyped("NewVillageFix", type1, paramTyped -> paramTyped.updateTyped(paramOpticFinder1, ())), 
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
/* 57 */         fixTypeEverywhereTyped("NewVillageStartFix", type2, paramTyped -> paramTyped.update(DSL.remainderFinder(), ())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\NewVillageFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */