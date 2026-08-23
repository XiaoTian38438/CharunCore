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
/*    */ public class V4067
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4067(int paramInt, Schema paramSchema) {
/* 16 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 22 */     map.remove("minecraft:boat");
/* 23 */     map.remove("minecraft:chest_boat");
/*    */     
/* 25 */     registerSimple(map, "minecraft:oak_boat");
/* 26 */     registerSimple(map, "minecraft:spruce_boat");
/* 27 */     registerSimple(map, "minecraft:birch_boat");
/* 28 */     registerSimple(map, "minecraft:jungle_boat");
/* 29 */     registerSimple(map, "minecraft:acacia_boat");
/* 30 */     registerSimple(map, "minecraft:cherry_boat");
/* 31 */     registerSimple(map, "minecraft:dark_oak_boat");
/* 32 */     registerSimple(map, "minecraft:mangrove_boat");
/* 33 */     registerSimple(map, "minecraft:bamboo_raft");
/*    */     
/* 35 */     registerChestBoat(map, "minecraft:oak_chest_boat");
/* 36 */     registerChestBoat(map, "minecraft:spruce_chest_boat");
/* 37 */     registerChestBoat(map, "minecraft:birch_chest_boat");
/* 38 */     registerChestBoat(map, "minecraft:jungle_chest_boat");
/* 39 */     registerChestBoat(map, "minecraft:acacia_chest_boat");
/* 40 */     registerChestBoat(map, "minecraft:cherry_chest_boat");
/* 41 */     registerChestBoat(map, "minecraft:dark_oak_chest_boat");
/* 42 */     registerChestBoat(map, "minecraft:mangrove_chest_boat");
/* 43 */     registerChestBoat(map, "minecraft:bamboo_chest_raft");
/*    */     
/* 45 */     return map;
/*    */   }
/*    */   
/*    */   private void registerChestBoat(Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 49 */     register(paramMap, paramString, paramString -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(this))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4067.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */