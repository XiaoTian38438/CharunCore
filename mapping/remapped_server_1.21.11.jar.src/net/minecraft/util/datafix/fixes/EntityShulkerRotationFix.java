/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityShulkerRotationFix extends NamedEntityFix {
/*    */   public EntityShulkerRotationFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 16 */     List<Double> list = paramDynamic.get("Rotation").asList(paramDynamic -> Double.valueOf(paramDynamic.asDouble(180.0D)));
/* 17 */     if (!list.isEmpty()) {
/* 18 */       list.set(0, Double.valueOf(((Double)list.get(0)).doubleValue() - 180.0D));
/* 19 */       Objects.requireNonNull(paramDynamic); return paramDynamic.set("Rotation", paramDynamic.createList(list.stream().map(paramDynamic::createDouble)));
/*    */     } 
/* 21 */     return paramDynamic;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 26 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityShulkerRotationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */