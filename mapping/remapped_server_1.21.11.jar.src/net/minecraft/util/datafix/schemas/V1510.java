/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1510
/*    */   extends NamespacedSchema {
/*    */   public V1510(int paramInt, Schema paramSchema) {
/* 11 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 16 */     Map<String, Supplier> map = super.registerEntities(paramSchema);
/*    */     
/* 18 */     map.put("minecraft:command_block_minecart", map.remove("minecraft:commandblock_minecart"));
/* 19 */     map.put("minecraft:end_crystal", map.remove("minecraft:ender_crystal"));
/* 20 */     map.put("minecraft:snow_golem", map.remove("minecraft:snowman"));
/* 21 */     map.put("minecraft:evoker", map.remove("minecraft:evocation_illager"));
/* 22 */     map.put("minecraft:evoker_fangs", map.remove("minecraft:evocation_fangs"));
/* 23 */     map.put("minecraft:illusioner", map.remove("minecraft:illusion_illager"));
/* 24 */     map.put("minecraft:vindicator", map.remove("minecraft:vindication_illager"));
/* 25 */     map.put("minecraft:iron_golem", map.remove("minecraft:villager_golem"));
/* 26 */     map.put("minecraft:experience_orb", map.remove("minecraft:xp_orb"));
/* 27 */     map.put("minecraft:experience_bottle", map.remove("minecraft:xp_bottle"));
/* 28 */     map.put("minecraft:eye_of_ender", map.remove("minecraft:eye_of_ender_signal"));
/* 29 */     map.put("minecraft:firework_rocket", map.remove("minecraft:fireworks_rocket"));
/*    */     
/* 31 */     return (Map)map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1510.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */