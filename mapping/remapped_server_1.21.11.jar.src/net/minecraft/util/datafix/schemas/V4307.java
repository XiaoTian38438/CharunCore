/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.SequencedMap;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V4307
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4307(int paramInt, Schema paramSchema) {
/* 19 */     super(paramInt, paramSchema);
/*    */   }
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema paramSchema) {
/* 23 */     SequencedMap<String, Supplier<TypeTemplate>> sequencedMap = V4059.components(paramSchema);
/* 24 */     sequencedMap.put("minecraft:can_place_on", () -> adventureModePredicate(paramSchema));
/* 25 */     sequencedMap.put("minecraft:can_break", () -> adventureModePredicate(paramSchema));
/* 26 */     return sequencedMap;
/*    */   }
/*    */   
/*    */   private static TypeTemplate adventureModePredicate(Schema paramSchema) {
/* 30 */     TypeTemplate typeTemplate = DSL.optionalFields("blocks", 
/* 31 */         DSL.or(References.BLOCK_NAME.in(paramSchema), DSL.list(References.BLOCK_NAME.in(paramSchema))));
/*    */     
/* 33 */     return DSL.or(typeTemplate, DSL.list(typeTemplate));
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 38 */     super.registerTypes(paramSchema, paramMap1, paramMap2);
/* 39 */     paramSchema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(paramSchema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\schemas\V4307.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */