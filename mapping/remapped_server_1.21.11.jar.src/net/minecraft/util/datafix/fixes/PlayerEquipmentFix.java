/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class PlayerEquipmentFix
/*    */   extends DataFix {
/*    */   public PlayerEquipmentFix(Schema paramSchema) {
/* 14 */     super(paramSchema, true);
/*    */   }
/*    */   
/* 17 */   private static final Map<Integer, String> SLOT_TRANSLATIONS = Map.of(
/* 18 */       Integer.valueOf(100), "feet", 
/* 19 */       Integer.valueOf(101), "legs", 
/* 20 */       Integer.valueOf(102), "chest", 
/* 21 */       Integer.valueOf(103), "head", 
/* 22 */       Integer.valueOf(-106), "offhand");
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type type1 = getInputSchema().getTypeRaw(References.PLAYER);
/* 28 */     Type type2 = getOutputSchema().getTypeRaw(References.PLAYER);
/*    */     
/* 30 */     return writeFixAndRead("Player Equipment Fix", type1, type2, paramDynamic -> {
/*    */           HashMap<Object, Object> hashMap = new HashMap<>();
/*    */           paramDynamic = paramDynamic.update("Inventory", ());
/*    */           return paramDynamic.set("equipment", paramDynamic.createMap(hashMap));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\PlayerEquipmentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */