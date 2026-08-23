/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class AreaEffectCloudPotionFix
/*    */   extends NamedEntityFix {
/*    */   public AreaEffectCloudPotionFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false, "AreaEffectCloudPotionFix", References.ENTITY, "minecraft:area_effect_cloud");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 17 */     return paramTyped.update(DSL.remainderFinder(), this::fix);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 21 */     Optional<Dynamic> optional1 = paramDynamic.get("Color").result();
/* 22 */     Optional<Dynamic> optional2 = paramDynamic.get("effects").result();
/* 23 */     Optional<Dynamic> optional3 = paramDynamic.get("Potion").result();
/* 24 */     paramDynamic = paramDynamic.remove("Color").remove("effects").remove("Potion");
/*    */     
/* 26 */     if (optional1.isEmpty() && optional2.isEmpty() && optional3.isEmpty()) {
/* 27 */       return paramDynamic;
/*    */     }
/*    */     
/* 30 */     Dynamic dynamic = paramDynamic.emptyMap();
/* 31 */     if (optional1.isPresent()) {
/* 32 */       dynamic = dynamic.set("custom_color", optional1.get());
/*    */     }
/* 34 */     if (optional2.isPresent()) {
/* 35 */       dynamic = dynamic.set("custom_effects", optional2.get());
/*    */     }
/* 37 */     if (optional3.isPresent()) {
/* 38 */       dynamic = dynamic.set("potion", optional3.get());
/*    */     }
/*    */     
/* 41 */     return paramDynamic.set("potion_contents", dynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AreaEffectCloudPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */