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
/*    */ public class V3689
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3689(int paramInt, Schema paramSchema) {
/* 16 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 22 */     paramSchema.registerSimple(map, "minecraft:breeze");
/* 23 */     paramSchema.registerSimple(map, "minecraft:wind_charge");
/* 24 */     paramSchema.registerSimple(map, "minecraft:breeze_wind_charge");
/* 25 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 30 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 31 */     paramSchema.register(map, "minecraft:trial_spawner", () -> DSL.optionalFields("spawn_potentials", DSL.list(DSL.fields("data", DSL.fields("entity", References.ENTITY_TREE.in(paramSchema)))), "spawn_data", DSL.fields("entity", References.ENTITY_TREE.in(paramSchema))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3689.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */