/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CatTypeFix extends NamedEntityFix {
/*    */   public CatTypeFix(Schema paramSchema, boolean paramBoolean) {
/* 10 */     super(paramSchema, paramBoolean, "CatTypeFix", References.ENTITY, "minecraft:cat");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     if (paramDynamic.get("CatType").asInt(0) == 9) {
/* 15 */       return paramDynamic.set("CatType", paramDynamic.createInt(10));
/*    */     }
/* 17 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 22 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\CatTypeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */