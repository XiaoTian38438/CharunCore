/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V2832
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2832(int paramInt, Schema paramSchema) {
/* 32 */     super(paramInt, paramSchema);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 37 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/*    */     
/* 39 */     paramSchema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(paramSchema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(paramSchema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(paramSchema))), "Sections", DSL.list(DSL.optionalFields("biomes", DSL.optionalFields("palette", DSL.list(References.BIOME.in(paramSchema))), "block_states", DSL.optionalFields("palette", DSL.list(References.BLOCK_STATE.in(paramSchema))))), "Structures", DSL.optionalFields("Starts", DSL.compoundList(References.STRUCTURE_FEATURE.in(paramSchema))))));
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     paramSchema.registerType(false, References.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, () -> DSL.constType(namespacedString()));
/*    */     
/* 60 */     paramSchema.registerType(false, References.WORLD_GEN_SETTINGS, () -> DSL.fields("dimensions", DSL.compoundList(DSL.constType(namespacedString()), DSL.fields("generator", (TypeTemplate)DSL.taggedChoiceLazy("type", DSL.string(), (Map)ImmutableMap.of("minecraft:debug", DSL::remainder, "minecraft:flat", (), "minecraft:noise", ()))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V2832.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */