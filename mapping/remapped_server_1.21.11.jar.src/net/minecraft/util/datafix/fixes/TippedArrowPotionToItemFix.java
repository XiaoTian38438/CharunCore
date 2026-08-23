/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class TippedArrowPotionToItemFix
/*    */   extends NamedEntityWriteReadFix {
/*    */   public TippedArrowPotionToItemFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false, "TippedArrowPotionToItemFix", References.ENTITY, "minecraft:arrow");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 15 */     Optional optional1 = paramDynamic.get("Potion").result();
/* 16 */     Optional optional2 = paramDynamic.get("custom_potion_effects").result();
/* 17 */     Optional optional3 = paramDynamic.get("Color").result();
/* 18 */     if (optional1.isEmpty() && optional2.isEmpty() && optional3.isEmpty()) {
/* 19 */       return paramDynamic;
/*    */     }
/*    */     
/* 22 */     return paramDynamic
/* 23 */       .remove("Potion")
/* 24 */       .remove("custom_potion_effects")
/* 25 */       .remove("Color")
/* 26 */       .update("item", paramDynamic -> {
/*    */           Dynamic dynamic = paramDynamic.get("tag").orElseEmptyMap();
/*    */           if (paramOptional1.isPresent())
/*    */             dynamic = dynamic.set("Potion", paramOptional1.get()); 
/*    */           if (paramOptional2.isPresent())
/*    */             dynamic = dynamic.set("custom_potion_effects", paramOptional2.get()); 
/*    */           if (paramOptional3.isPresent())
/*    */             dynamic = dynamic.set("CustomPotionColor", paramOptional3.get()); 
/*    */           return paramDynamic.set("tag", dynamic);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TippedArrowPotionToItemFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */