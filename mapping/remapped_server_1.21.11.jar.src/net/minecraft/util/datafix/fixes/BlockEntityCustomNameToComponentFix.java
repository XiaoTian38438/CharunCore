/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlockEntityCustomNameToComponentFix
/*    */   extends DataFix {
/* 20 */   private static final Set<String> NAMEABLE_BLOCK_ENTITIES = Set.of(new String[] { "minecraft:beacon", "minecraft:banner", "minecraft:brewing_stand", "minecraft:chest", "minecraft:trapped_chest", "minecraft:dispenser", "minecraft:dropper", "minecraft:enchanting_table", "minecraft:furnace", "minecraft:hopper", "minecraft:shulker_box" });
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
/*    */   public BlockEntityCustomNameToComponentFix(Schema paramSchema) {
/* 36 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 41 */     OpticFinder opticFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 43 */     Type type1 = getInputSchema().getType(References.BLOCK_ENTITY);
/* 44 */     Type type2 = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 45 */     Type type3 = ExtraDataFixUtils.patchSubType(type1, type1, type2);
/*    */     
/* 47 */     return fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", type1, type2, paramTyped -> {
/*    */           Optional optional = paramTyped.getOptional(paramOpticFinder);
/* 49 */           return (optional.isPresent() && !NAMEABLE_BLOCK_ENTITIES.contains(optional.get())) ? ExtraDataFixUtils.cast(paramType1, paramTyped) : Util.writeAndReadTypedOrThrow(ExtraDataFixUtils.cast(paramType2, paramTyped), paramType1, BlockEntityCustomNameToComponentFix::fixTagCustomName);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Dynamic<T> fixTagCustomName(Dynamic<T> paramDynamic) {
/* 61 */     String str = paramDynamic.get("CustomName").asString("");
/*    */     
/* 63 */     if (str.isEmpty()) {
/* 64 */       return paramDynamic.remove("CustomName");
/*    */     }
/* 66 */     return paramDynamic.set("CustomName", LegacyComponentDataFixUtils.createPlainTextComponent(paramDynamic.getOps(), str));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\BlockEntityCustomNameToComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */