/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1928
/*    */   extends NamespacedSchema {
/*    */   public V1928(int paramInt, Schema paramSchema) {
/* 11 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 15 */     paramSchema.registerSimple(paramMap, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/*    */     
/* 22 */     map.remove("minecraft:illager_beast");
/* 23 */     registerMob(paramSchema, map, "minecraft:ravager");
/*    */     
/* 25 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V1928.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */