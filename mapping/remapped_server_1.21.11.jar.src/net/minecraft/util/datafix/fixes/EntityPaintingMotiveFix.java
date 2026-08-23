/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityPaintingMotiveFix extends NamedEntityFix {
/*    */   public EntityPaintingMotiveFix(Schema paramSchema, boolean paramBoolean) {
/* 17 */     super(paramSchema, paramBoolean, "EntityPaintingMotiveFix", References.ENTITY, "minecraft:painting");
/*    */   } private static final Map<String, String> MAP;
/*    */   static {
/* 20 */     MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), paramHashMap -> {
/*    */           paramHashMap.put("donkeykong", "donkey_kong");
/*    */           paramHashMap.put("burningskull", "burning_skull");
/*    */           paramHashMap.put("skullandroses", "skull_and_roses");
/*    */         });
/*    */   }
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 27 */     Optional<String> optional = paramDynamic.get("Motive").asString().result();
/* 28 */     if (optional.isPresent()) {
/* 29 */       String str = ((String)optional.get()).toLowerCase(Locale.ROOT);
/* 30 */       return paramDynamic.set("Motive", paramDynamic.createString(NamespacedSchema.ensureNamespaced(MAP.getOrDefault(str, str))));
/*    */     } 
/* 32 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 37 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityPaintingMotiveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */