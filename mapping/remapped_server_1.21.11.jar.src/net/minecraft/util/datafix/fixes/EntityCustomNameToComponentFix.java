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
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityCustomNameToComponentFix
/*    */   extends DataFix
/*    */ {
/*    */   public EntityCustomNameToComponentFix(Schema paramSchema) {
/* 22 */     super(paramSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Type type1 = getInputSchema().getType(References.ENTITY);
/* 28 */     Type type2 = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 30 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 32 */     OpticFinder opticFinder2 = type1.findField("CustomName");
/* 33 */     Type type3 = type2.findFieldType("CustomName");
/*    */     
/* 35 */     return fixTypeEverywhereTyped("EntityCustomNameToComponentFix", type1, type2, paramTyped -> fixEntity(paramTyped, paramType1, paramOpticFinder1, paramOpticFinder2, paramType2));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Typed<?> fixEntity(Typed<?> paramTyped, Type<?> paramType, OpticFinder<String> paramOpticFinder1, OpticFinder<String> paramOpticFinder2, Type<T> paramType1) {
/* 41 */     Optional<String> optional = paramTyped.getOptional(paramOpticFinder2);
/* 42 */     if (optional.isEmpty()) {
/* 43 */       return ExtraDataFixUtils.cast(paramType, paramTyped);
/*    */     }
/*    */ 
/*    */     
/* 47 */     if (((String)optional.get()).isEmpty()) {
/* 48 */       return Util.writeAndReadTypedOrThrow(paramTyped, paramType, paramDynamic -> paramDynamic.remove("CustomName"));
/*    */     }
/*    */     
/* 51 */     String str = paramTyped.getOptional(paramOpticFinder1).orElse("");
/* 52 */     Dynamic<?> dynamic = fixCustomName(paramTyped.getOps(), optional.get(), str);
/* 53 */     return paramTyped.set(paramOpticFinder2, Util.readTypedOrThrow(paramType1, dynamic));
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> fixCustomName(DynamicOps<T> paramDynamicOps, String paramString1, String paramString2) {
/* 58 */     if ("minecraft:commandblock_minecart".equals(paramString2)) {
/* 59 */       return new Dynamic(paramDynamicOps, paramDynamicOps.createString(paramString1));
/*    */     }
/* 61 */     return LegacyComponentDataFixUtils.createPlainTextComponent(paramDynamicOps, paramString1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityCustomNameToComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */