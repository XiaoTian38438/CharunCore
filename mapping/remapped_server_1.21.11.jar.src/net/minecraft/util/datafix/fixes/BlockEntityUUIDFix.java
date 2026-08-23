/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityUUIDFix extends AbstractUUIDFix {
/*    */   public BlockEntityUUIDFix(Schema paramSchema) {
/*  9 */     super(paramSchema, References.BLOCK_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 14 */     return fixTypeEverywhereTyped("BlockEntityUUIDFix", getInputSchema().getType(this.typeReference), paramTyped -> {
/*    */           paramTyped = updateNamedChoice(paramTyped, "minecraft:conduit", this::updateConduit);
/*    */           return updateNamedChoice(paramTyped, "minecraft:skull", this::updateSkull);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkull(Dynamic<?> paramDynamic) {
/* 22 */     return paramDynamic.get("Owner").get().map(paramDynamic -> (Dynamic)replaceUUIDString(paramDynamic, "Id", "Id").orElse(paramDynamic))
/*    */       
/* 24 */       .map(paramDynamic2 -> paramDynamic1.remove("Owner").set("SkullOwner", paramDynamic2))
/*    */       
/* 26 */       .result().orElse(paramDynamic);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateConduit(Dynamic<?> paramDynamic) {
/* 30 */     return replaceUUIDMLTag(paramDynamic, "target_uuid", "Target").orElse(paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */