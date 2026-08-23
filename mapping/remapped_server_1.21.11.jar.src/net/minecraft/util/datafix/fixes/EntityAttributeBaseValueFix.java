/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.DoubleUnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityAttributeBaseValueFix
/*    */   extends NamedEntityFix {
/*    */   private final String attributeId;
/*    */   private final DoubleUnaryOperator valueFixer;
/*    */   
/*    */   public EntityAttributeBaseValueFix(Schema paramSchema, String paramString1, String paramString2, String paramString3, DoubleUnaryOperator paramDoubleUnaryOperator) {
/* 16 */     super(paramSchema, false, paramString1, References.ENTITY, paramString2);
/* 17 */     this.attributeId = paramString3;
/* 18 */     this.valueFixer = paramDoubleUnaryOperator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 23 */     return paramTyped.update(DSL.remainderFinder(), this::fixValue);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixValue(Dynamic<?> paramDynamic) {
/* 27 */     return paramDynamic.update("attributes", paramDynamic2 -> paramDynamic1.createList(paramDynamic2.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityAttributeBaseValueFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */