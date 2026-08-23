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
/*    */ public class V1470
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1470(int paramInt, Schema paramSchema) {
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
/*    */ 
/*    */     
/* 27 */     registerMob(paramSchema, map, "minecraft:turtle");
/* 28 */     registerMob(paramSchema, map, "minecraft:cod_mob");
/* 29 */     registerMob(paramSchema, map, "minecraft:tropical_fish");
/* 30 */     registerMob(paramSchema, map, "minecraft:salmon_mob");
/* 31 */     registerMob(paramSchema, map, "minecraft:puffer_fish");
/* 32 */     registerMob(paramSchema, map, "minecraft:phantom");
/* 33 */     registerMob(paramSchema, map, "minecraft:dolphin");
/* 34 */     registerMob(paramSchema, map, "minecraft:drowned");
/*    */     
/* 36 */     paramSchema.register(map, "minecraft:trident", paramString -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(paramSchema), "Trident", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1470.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */