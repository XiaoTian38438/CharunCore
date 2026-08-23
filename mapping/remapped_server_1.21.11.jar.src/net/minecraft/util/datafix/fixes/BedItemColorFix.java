/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BedItemColorFix
/*    */   extends DataFix {
/*    */   public BedItemColorFix(Schema paramSchema, boolean paramBoolean) {
/* 18 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     OpticFinder opticFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 25 */     return fixTypeEverywhereTyped("BedItemColorFix", getInputSchema().getType(References.ITEM_STACK), paramTyped -> {
/*    */           Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder);
/*    */           if (optional.isPresent() && Objects.equals(((Pair)optional.get()).getSecond(), "minecraft:bed")) {
/*    */             Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*    */             if (dynamic.get("Damage").asInt(0) == 0)
/*    */               return paramTyped.set(DSL.remainderFinder(), dynamic.set("Damage", dynamic.createShort((short)14))); 
/*    */           } 
/*    */           return paramTyped;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BedItemColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */