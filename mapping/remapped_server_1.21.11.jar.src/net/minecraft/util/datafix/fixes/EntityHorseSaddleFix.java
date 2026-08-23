/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityHorseSaddleFix
/*    */   extends NamedEntityFix {
/*    */   public EntityHorseSaddleFix(Schema paramSchema, boolean paramBoolean) {
/* 17 */     super(paramSchema, paramBoolean, "EntityHorseSaddleFix", References.ENTITY, "EntityHorse");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 24 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 25 */     Type type = getInputSchema().getTypeRaw(References.ITEM_STACK);
/* 26 */     OpticFinder opticFinder2 = DSL.fieldFinder("SaddleItem", type);
/*    */     
/* 28 */     Optional optional = paramTyped.getOptionalTyped(opticFinder2);
/* 29 */     Dynamic dynamic = (Dynamic)paramTyped.get(DSL.remainderFinder());
/* 30 */     if (optional.isEmpty() && dynamic.get("Saddle").asBoolean(false)) {
/* 31 */       Typed typed = (Typed)type.pointTyped(paramTyped.getOps()).orElseThrow(IllegalStateException::new);
/* 32 */       typed = typed.set(opticFinder1, Pair.of(References.ITEM_NAME.typeName(), "minecraft:saddle"));
/*    */       
/* 34 */       Dynamic dynamic1 = dynamic.emptyMap();
/* 35 */       dynamic1 = dynamic1.set("Count", dynamic1.createByte((byte)1));
/* 36 */       dynamic1 = dynamic1.set("Damage", dynamic1.createShort((short)0));
/*    */       
/* 38 */       typed = typed.set(DSL.remainderFinder(), dynamic1);
/* 39 */       dynamic.remove("Saddle");
/*    */       
/* 41 */       return paramTyped.set(opticFinder2, typed).set(DSL.remainderFinder(), dynamic);
/*    */     } 
/* 43 */     return paramTyped;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityHorseSaddleFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */