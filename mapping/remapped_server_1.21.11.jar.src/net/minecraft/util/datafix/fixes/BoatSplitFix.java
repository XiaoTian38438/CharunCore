/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BoatSplitFix
/*    */   extends DataFix {
/*    */   public BoatSplitFix(Schema paramSchema) {
/* 18 */     super(paramSchema, true);
/*    */   }
/*    */   
/*    */   private static boolean isNormalBoat(String paramString) {
/* 22 */     return paramString.equals("minecraft:boat");
/*    */   }
/*    */   
/*    */   private static boolean isChestBoat(String paramString) {
/* 26 */     return paramString.equals("minecraft:chest_boat");
/*    */   }
/*    */   
/*    */   private static boolean isAnyBoat(String paramString) {
/* 30 */     return (isNormalBoat(paramString) || isChestBoat(paramString));
/*    */   }
/*    */   
/*    */   private static String mapVariantToNormalBoat(String paramString) {
/* 34 */     switch (paramString) { default: case "spruce": case "birch": case "jungle": case "acacia": case "cherry": case "dark_oak": case "mangrove": case "bamboo": break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 43 */       "minecraft:bamboo_raft";
/*    */   }
/*    */ 
/*    */   
/*    */   private static String mapVariantToChestBoat(String paramString) {
/* 48 */     switch (paramString) { default: case "spruce": case "birch": case "jungle": case "acacia": case "cherry": case "dark_oak": case "mangrove": case "bamboo": break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       "minecraft:bamboo_chest_raft";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 63 */     OpticFinder opticFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 65 */     Type type1 = getInputSchema().getType(References.ENTITY);
/* 66 */     Type type2 = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 68 */     return fixTypeEverywhereTyped("BoatSplitFix", type1, type2, paramTyped -> {
/*    */           Optional<String> optional = paramTyped.getOptional(paramOpticFinder);
/*    */           if (optional.isPresent() && isAnyBoat(optional.get())) {
/*    */             String str;
/*    */             Dynamic dynamic = (Dynamic)paramTyped.getOrCreate(DSL.remainderFinder());
/*    */             Optional optional1 = dynamic.get("Type").asString().result();
/*    */             if (isChestBoat(optional.get())) {
/*    */               str = optional1.map(BoatSplitFix::mapVariantToChestBoat).orElse("minecraft:oak_chest_boat");
/*    */             } else {
/*    */               str = optional1.map(BoatSplitFix::mapVariantToNormalBoat).orElse("minecraft:oak_boat");
/*    */             } 
/*    */             return ExtraDataFixUtils.cast(paramType, paramTyped).update(DSL.remainderFinder(), ()).set(paramOpticFinder, str);
/*    */           } 
/*    */           return ExtraDataFixUtils.cast(paramType, paramTyped);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BoatSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */