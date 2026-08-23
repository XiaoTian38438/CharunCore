/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EmptyItemInVillagerTradeFix extends DataFix {
/*    */   public EmptyItemInVillagerTradeFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 17 */     Type type = getInputSchema().getType(References.VILLAGER_TRADE);
/* 18 */     return writeFixAndRead("EmptyItemInVillagerTradeFix", type, type, paramDynamic -> {
/*    */           Dynamic dynamic = paramDynamic.get("buyB").orElseEmptyMap();
/*    */           String str = NamespacedSchema.ensureNamespaced(dynamic.get("id").asString("minecraft:air"));
/*    */           int i = dynamic.get("count").asInt(0);
/* 22 */           return (str.equals("minecraft:air") || i == 0) ? paramDynamic.remove("buyB") : paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EmptyItemInVillagerTradeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */