/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class FoodToConsumableFix
/*    */   extends DataFix {
/*    */   public FoodToConsumableFix(Schema paramSchema) {
/* 13 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     return writeFixAndRead("Food to consumable fix", getInputSchema().getType(References.DATA_COMPONENTS), getOutputSchema().getType(References.DATA_COMPONENTS), paramDynamic -> {
/*    */           Optional<Dynamic> optional = paramDynamic.get("minecraft:food").result();
/*    */           if (optional.isPresent()) {
/*    */             float f = ((Dynamic)optional.get()).get("eat_seconds").asFloat(1.6F);
/*    */             Stream stream1 = ((Dynamic)optional.get()).get("effects").asStream();
/*    */             Stream stream2 = stream1.map(());
/*    */             paramDynamic = Dynamic.copyField(optional.get(), "using_converts_to", paramDynamic, "minecraft:use_remainder");
/*    */             paramDynamic = paramDynamic.set("minecraft:food", ((Dynamic)optional.get()).remove("eat_seconds").remove("effects").remove("using_converts_to"));
/*    */             return paramDynamic.set("minecraft:consumable", paramDynamic.emptyMap().set("consume_seconds", paramDynamic.createFloat(f)).set("on_consume_effects", paramDynamic.createList(stream2)));
/*    */           } 
/*    */           return paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FoodToConsumableFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */