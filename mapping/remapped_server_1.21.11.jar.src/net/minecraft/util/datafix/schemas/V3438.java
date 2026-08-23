/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V3438
/*    */   extends NamespacedSchema {
/*    */   public V3438(int paramInt, Schema paramSchema) {
/* 11 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 16 */     Map<String, Supplier> map = super.registerBlockEntities(paramSchema);
/* 17 */     map.put("minecraft:brushable_block", map.remove("minecraft:suspicious_sand"));
/* 18 */     paramSchema.registerSimple(map, "minecraft:calibrated_sculk_sensor");
/*    */     
/* 20 */     return (Map)map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V3438.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */