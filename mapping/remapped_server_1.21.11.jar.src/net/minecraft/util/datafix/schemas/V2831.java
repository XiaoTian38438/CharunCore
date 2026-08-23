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
/*    */ public class V2831
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2831(int paramInt, Schema paramSchema) {
/* 17 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 22 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 24 */     paramSchema.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("data", DSL.fields("entity", References.ENTITY_TREE.in(paramSchema)))), "SpawnData", DSL.fields("entity", References.ENTITY_TREE.in(paramSchema))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V2831.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */