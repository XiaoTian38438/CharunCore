/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class JigsawPropertiesFix extends NamedEntityFix {
/*    */   public JigsawPropertiesFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "JigsawPropertiesFix", References.BLOCK_ENTITY, "minecraft:jigsaw");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     String str1 = paramDynamic.get("attachement_type").asString("minecraft:empty");
/* 15 */     String str2 = paramDynamic.get("target_pool").asString("minecraft:empty");
/* 16 */     return paramDynamic
/* 17 */       .set("name", paramDynamic.createString(str1))
/* 18 */       .set("target", paramDynamic.createString(str1))
/* 19 */       .remove("attachement_type")
/* 20 */       .set("pool", paramDynamic.createString(str2))
/* 21 */       .remove("target_pool");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 26 */     return paramTyped.update(DSL.remainderFinder(), JigsawPropertiesFix::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\JigsawPropertiesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */