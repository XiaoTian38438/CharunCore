/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ 
/*    */ public class BlendingDataRemoveFromNetherEndFix extends DataFix {
/*    */   public BlendingDataRemoveFromNetherEndFix(Schema paramSchema) {
/* 13 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     Type type = getOutputSchema().getType(References.CHUNK);
/*    */     
/* 20 */     return fixTypeEverywhereTyped("BlendingDataRemoveFromNetherEndFix", type, paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> updateChunkTag(Dynamic<?> paramDynamic, OptionalDynamic<?> paramOptionalDynamic) {
/* 26 */     boolean bool = "minecraft:overworld".equals(paramOptionalDynamic.get("dimension").asString().result().orElse(""));
/* 27 */     return bool ? paramDynamic : paramDynamic.remove("blending_data");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlendingDataRemoveFromNetherEndFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */