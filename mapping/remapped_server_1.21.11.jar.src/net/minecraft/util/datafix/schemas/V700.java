/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V700
/*    */   extends Schema {
/*    */   public V700(int paramInt, Schema paramSchema) {
/* 11 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 17 */     paramSchema.registerSimple(map, "ElderGuardian");
/* 18 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V700.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */