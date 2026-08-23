/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4420
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4420(int paramInt, Schema paramSchema) {
/* 14 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(paramSchema);
/* 20 */     paramSchema.register(map, "minecraft:area_effect_cloud", paramString -> DSL.optionalFields("custom_particle", References.PARTICLE.in(paramSchema)));
/*    */ 
/*    */     
/* 23 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4420.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */