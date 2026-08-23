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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V1451_3
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1451_3(int paramInt, Schema paramSchema) {
/* 19 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/*    */ 
/*    */     
/* 27 */     paramSchema.registerSimple(map, "minecraft:egg");
/* 28 */     paramSchema.registerSimple(map, "minecraft:ender_pearl");
/* 29 */     paramSchema.registerSimple(map, "minecraft:fireball");
/* 30 */     paramSchema.register(map, "minecraft:potion", paramString -> DSL.optionalFields("Potion", References.ITEM_STACK.in(paramSchema)));
/*    */ 
/*    */     
/* 33 */     paramSchema.registerSimple(map, "minecraft:small_fireball");
/* 34 */     paramSchema.registerSimple(map, "minecraft:snowball");
/* 35 */     paramSchema.registerSimple(map, "minecraft:wither_skull");
/* 36 */     paramSchema.registerSimple(map, "minecraft:xp_bottle");
/*    */     
/* 38 */     paramSchema.register(map, "minecraft:arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */     
/* 41 */     paramSchema.register(map, "minecraft:enderman", () -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */     
/* 44 */     paramSchema.register(map, "minecraft:falling_block", () -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(paramSchema), "TileEntityData", References.BLOCK_ENTITY.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 48 */     paramSchema.register(map, "minecraft:spectral_arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */     
/* 51 */     paramSchema.register(map, "minecraft:chest_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*    */ 
/*    */ 
/*    */     
/* 55 */     paramSchema.register(map, "minecraft:commandblock_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), "LastOutput", References.TEXT_COMPONENT.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 59 */     paramSchema.register(map, "minecraft:furnace_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */     
/* 62 */     paramSchema.register(map, "minecraft:hopper_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), "Items", DSL.list(References.ITEM_STACK.in(paramSchema))));
/*    */ 
/*    */ 
/*    */     
/* 66 */     paramSchema.register(map, "minecraft:minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */     
/* 69 */     paramSchema.register(map, "minecraft:spawner_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema), References.UNTAGGED_SPAWNER.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 73 */     paramSchema.register(map, "minecraft:tnt_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(paramSchema)));
/*    */ 
/*    */ 
/*    */     
/* 77 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1451_3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */