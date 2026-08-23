/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityCatSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntityCatSplitFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super("EntityCatSplitFix", paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic) {
/* 16 */     if (Objects.equals("minecraft:ocelot", paramString)) {
/* 17 */       int i = paramDynamic.get("CatType").asInt(0);
/* 18 */       if (i == 0) {
/* 19 */         String str1 = paramDynamic.get("Owner").asString("");
/* 20 */         String str2 = paramDynamic.get("OwnerUUID").asString("");
/* 21 */         if (!str1.isEmpty() || !str2.isEmpty()) {
/* 22 */           paramDynamic.set("Trusting", paramDynamic.createBoolean(true));
/*    */         }
/* 24 */       } else if (i > 0 && i < 4) {
/* 25 */         paramDynamic = paramDynamic.set("CatType", paramDynamic.createInt(i));
/* 26 */         paramDynamic = paramDynamic.set("OwnerUUID", paramDynamic.createString(paramDynamic.get("OwnerUUID").asString("")));
/* 27 */         return Pair.of("minecraft:cat", paramDynamic);
/*    */       } 
/*    */     } 
/*    */     
/* 31 */     return Pair.of(paramString, paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityCatSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */