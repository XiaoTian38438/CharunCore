/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V3938
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3938(int paramInt, Schema paramSchema) {
/* 16 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   protected static TypeTemplate abstractArrow(Schema paramSchema) {
/* 20 */     return DSL.optionalFields("inBlockState", References.BLOCK_STATE
/* 21 */         .in(paramSchema), "item", References.ITEM_STACK
/* 22 */         .in(paramSchema), "weapon", References.ITEM_STACK
/* 23 */         .in(paramSchema));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 29 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 30 */     paramSchema.register(map, "minecraft:spectral_arrow", () -> abstractArrow(paramSchema));
/* 31 */     paramSchema.register(map, "minecraft:arrow", () -> abstractArrow(paramSchema));
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3938.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */