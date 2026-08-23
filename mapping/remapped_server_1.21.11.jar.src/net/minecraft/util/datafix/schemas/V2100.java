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
/*    */ public class V2100
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2100(int paramInt, Schema paramSchema) {
/* 15 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 19 */     paramSchema.registerSimple(paramMap, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 25 */     registerMob(paramSchema, map, "minecraft:bee");
/* 26 */     registerMob(paramSchema, map, "minecraft:bee_stinger");
/* 27 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 32 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/*    */     
/* 34 */     paramSchema.register(map, "minecraft:beehive", () -> DSL.optionalFields("Bees", DSL.list(DSL.optionalFields("EntityData", References.ENTITY_TREE.in(paramSchema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V2100.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */