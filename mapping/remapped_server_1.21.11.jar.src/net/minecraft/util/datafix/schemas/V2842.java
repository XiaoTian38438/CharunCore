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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V2842
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2842(int paramInt, Schema paramSchema) {
/* 25 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 30 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 32 */     paramSchema.registerType(false, References.CHUNK, () -> DSL.optionalFields("entities", DSL.list(References.ENTITY_TREE.in(paramSchema)), "block_entities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(paramSchema), DSL.remainder())), "block_ticks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(paramSchema))), "sections", DSL.list(DSL.optionalFields("biomes", DSL.optionalFields("palette", DSL.list(References.BIOME.in(paramSchema))), "block_states", DSL.optionalFields("palette", DSL.list(References.BLOCK_STATE.in(paramSchema))))), "structures", DSL.optionalFields("starts", DSL.compoundList(References.STRUCTURE_FEATURE.in(paramSchema)))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V2842.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */