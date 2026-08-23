/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackUUIDFix
/*    */   extends AbstractUUIDFix {
/*    */   public ItemStackUUIDFix(Schema paramSchema) {
/* 15 */     super(paramSchema, References.ITEM_STACK);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 20 */     OpticFinder opticFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 22 */     return fixTypeEverywhereTyped("ItemStackUUIDFix", getInputSchema().getType(this.typeReference), paramTyped -> {
/*    */           OpticFinder opticFinder = paramTyped.getType().findField("tag");
/*    */           return paramTyped.updateTyped(opticFinder, ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateAttributeModifiers(Dynamic<?> paramDynamic) {
/* 38 */     return paramDynamic.update("AttributeModifiers", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkullOwner(Dynamic<?> paramDynamic) {
/* 46 */     return paramDynamic.update("SkullOwner", paramDynamic -> (Dynamic)replaceUUIDString(paramDynamic, "Id", "Id").orElse(paramDynamic));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */