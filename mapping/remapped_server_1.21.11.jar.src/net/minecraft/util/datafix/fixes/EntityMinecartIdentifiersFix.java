/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EntityMinecartIdentifiersFix extends EntityRenameFix {
/*    */   public EntityMinecartIdentifiersFix(Schema paramSchema) {
/* 12 */     super("EntityMinecartIdentifiersFix", paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped) {
/* 17 */     if (!paramString.equals("Minecart")) {
/* 18 */       return Pair.of(paramString, paramTyped);
/*    */     }
/*    */     
/* 21 */     int i = ((Dynamic)paramTyped.getOrCreate(DSL.remainderFinder())).get("Type").asInt(0);
/* 22 */     switch (i) { default: 
/*    */       case 1: 
/*    */       case 2:
/* 25 */         break; }  String str = "MinecartFurnace";
/*    */ 
/*    */     
/* 28 */     Type type = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(str);
/* 29 */     return Pair.of(str, Util.writeAndReadTypedOrThrow(paramTyped, type, paramDynamic -> paramDynamic.remove("Type")));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityMinecartIdentifiersFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */