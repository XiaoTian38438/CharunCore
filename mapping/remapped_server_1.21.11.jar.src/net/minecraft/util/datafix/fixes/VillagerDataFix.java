/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class VillagerDataFix extends NamedEntityFix {
/*    */   public VillagerDataFix(Schema paramSchema, String paramString) {
/* 12 */     super(paramSchema, false, "Villager profession data fix (" + paramString + ")", References.ENTITY, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 17 */     Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/*    */     
/* 19 */     return paramTyped.set(DSL.remainderFinder(), dynamic
/* 20 */         .remove("Profession")
/* 21 */         .remove("Career")
/* 22 */         .remove("CareerLevel")
/* 23 */         .set("VillagerData", dynamic
/* 24 */           .createMap((Map)ImmutableMap.of(dynamic
/* 25 */               .createString("type"), dynamic.createString("minecraft:plains"), dynamic
/* 26 */               .createString("profession"), dynamic.createString(upgradeData(dynamic.get("Profession").asInt(0), dynamic.get("Career").asInt(0))), dynamic
/* 27 */               .createString("level"), DataFixUtils.orElse(dynamic.get("CareerLevel").result(), dynamic.createInt(1))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String upgradeData(int paramInt1, int paramInt2) {
/* 34 */     if (paramInt1 == 0) {
/* 35 */       if (paramInt2 == 2)
/* 36 */         return "minecraft:fisherman"; 
/* 37 */       if (paramInt2 == 3)
/* 38 */         return "minecraft:shepherd"; 
/* 39 */       if (paramInt2 == 4) {
/* 40 */         return "minecraft:fletcher";
/*    */       }
/* 42 */       return "minecraft:farmer";
/*    */     } 
/* 44 */     if (paramInt1 == 1) {
/* 45 */       if (paramInt2 == 2) {
/* 46 */         return "minecraft:cartographer";
/*    */       }
/* 48 */       return "minecraft:librarian";
/*    */     } 
/* 50 */     if (paramInt1 == 2)
/* 51 */       return "minecraft:cleric"; 
/* 52 */     if (paramInt1 == 3) {
/* 53 */       if (paramInt2 == 2)
/* 54 */         return "minecraft:weaponsmith"; 
/* 55 */       if (paramInt2 == 3) {
/* 56 */         return "minecraft:toolsmith";
/*    */       }
/* 58 */       return "minecraft:armorer";
/*    */     } 
/* 60 */     if (paramInt1 == 4) {
/* 61 */       if (paramInt2 == 2) {
/* 62 */         return "minecraft:leatherworker";
/*    */       }
/* 64 */       return "minecraft:butcher";
/*    */     } 
/* 66 */     if (paramInt1 == 5) {
/* 67 */       return "minecraft:nitwit";
/*    */     }
/* 69 */     return "minecraft:none";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\VillagerDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */