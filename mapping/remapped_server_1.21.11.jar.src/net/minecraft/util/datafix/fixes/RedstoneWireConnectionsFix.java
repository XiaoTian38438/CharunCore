/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RedstoneWireConnectionsFix extends DataFix {
/*    */   public RedstoneWireConnectionsFix(Schema paramSchema) {
/* 11 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     Schema schema = getInputSchema();
/* 17 */     return fixTypeEverywhereTyped("RedstoneConnectionsFix", schema.getType(References.BLOCK_STATE), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::updateRedstoneConnections));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> updateRedstoneConnections(Dynamic<T> paramDynamic) {
/* 21 */     boolean bool = paramDynamic.get("Name").asString().result().filter("minecraft:redstone_wire"::equals).isPresent();
/* 22 */     if (!bool) {
/* 23 */       return paramDynamic;
/*    */     }
/*    */     
/* 26 */     return paramDynamic.update("Properties", paramDynamic -> {
/*    */           String str1 = paramDynamic.get("east").asString("none");
/*    */           String str2 = paramDynamic.get("west").asString("none");
/*    */           String str3 = paramDynamic.get("north").asString("none");
/*    */           String str4 = paramDynamic.get("south").asString("none");
/* 31 */           boolean bool1 = (isConnected(str1) || isConnected(str2)) ? true : false;
/* 32 */           boolean bool2 = (isConnected(str3) || isConnected(str4)) ? true : false;
/*    */           
/* 34 */           String str5 = (!isConnected(str1) && !bool2) ? "side" : str1;
/* 35 */           String str6 = (!isConnected(str2) && !bool2) ? "side" : str2;
/* 36 */           String str7 = (!isConnected(str3) && !bool1) ? "side" : str3;
/* 37 */           String str8 = (!isConnected(str4) && !bool1) ? "side" : str4;
/*    */           return paramDynamic.update("east", ()).update("west", ()).update("north", ()).update("south", ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isConnected(String paramString) {
/* 48 */     return !"none".equals(paramString);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RedstoneWireConnectionsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */