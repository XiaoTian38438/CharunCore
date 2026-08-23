/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3078
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3078(int paramInt, Schema paramSchema) {
/* 14 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 18 */     paramSchema.registerSimple(paramMap, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 23 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 24 */     registerMob(paramSchema, map, "minecraft:frog");
/* 25 */     registerMob(paramSchema, map, "minecraft:tadpole");
/* 26 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 31 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(paramSchema);
/* 32 */     paramSchema.register(map, "minecraft:sculk_shrieker", () -> DSL.optionalFields("listener", DSL.optionalFields("event", DSL.optionalFields("game_event", References.GAME_EVENT_NAME.in(paramSchema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3078.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */