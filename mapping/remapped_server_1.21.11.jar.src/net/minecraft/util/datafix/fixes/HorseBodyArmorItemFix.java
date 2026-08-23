/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class HorseBodyArmorItemFix
/*    */   extends NamedEntityWriteReadFix {
/*    */   private final String previousBodyArmorTag;
/*    */   private final boolean clearArmorItems;
/*    */   
/*    */   public HorseBodyArmorItemFix(Schema paramSchema, String paramString1, String paramString2, boolean paramBoolean) {
/* 14 */     super(paramSchema, true, "Horse armor fix for " + paramString1, References.ENTITY, paramString1);
/* 15 */     this.previousBodyArmorTag = paramString2;
/* 16 */     this.clearArmorItems = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 21 */     Optional<Dynamic> optional = paramDynamic.get(this.previousBodyArmorTag).result();
/* 22 */     if (optional.isPresent()) {
/* 23 */       Dynamic dynamic = optional.get();
/* 24 */       Dynamic<T> dynamic1 = paramDynamic.remove(this.previousBodyArmorTag);
/* 25 */       if (this.clearArmorItems) {
/* 26 */         dynamic1 = dynamic1.update("ArmorItems", paramDynamic -> paramDynamic.createList(Streams.mapWithIndex(paramDynamic.asStream(), ())));
/*    */ 
/*    */         
/* 29 */         dynamic1 = dynamic1.update("ArmorDropChances", paramDynamic -> paramDynamic.createList(Streams.mapWithIndex(paramDynamic.asStream(), ())));
/*    */       } 
/*    */ 
/*    */       
/* 33 */       dynamic1 = dynamic1.set("body_armor_item", dynamic);
/* 34 */       dynamic1 = dynamic1.set("body_armor_drop_chance", paramDynamic.createFloat(2.0F));
/* 35 */       return dynamic1;
/*    */     } 
/* 37 */     return paramDynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\HorseBodyArmorItemFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */