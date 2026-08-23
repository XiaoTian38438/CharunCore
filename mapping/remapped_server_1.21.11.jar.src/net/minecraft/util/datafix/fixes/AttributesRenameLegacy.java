/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class AttributesRenameLegacy extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AttributesRenameLegacy(Schema paramSchema, String paramString, UnaryOperator<String> paramUnaryOperator) {
/* 20 */     super(paramSchema, false);
/* 21 */     this.name = paramString;
/* 22 */     this.renames = paramUnaryOperator;
/*    */   }
/*    */   private final UnaryOperator<String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type type = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder opticFinder = type.findField("tag");
/* 29 */     return TypeRewriteRule.seq(
/* 30 */         fixTypeEverywhereTyped(this.name + " (ItemStack)", type, paramTyped -> paramTyped.updateTyped(paramOpticFinder, this::fixItemStackTag)), new TypeRewriteRule[] {
/*    */ 
/*    */           
/* 33 */           fixTypeEverywhereTyped(this.name + " (Entity)", getInputSchema().getType(References.ENTITY), this::fixEntity), 
/* 34 */           fixTypeEverywhereTyped(this.name + " (Player)", getInputSchema().getType(References.PLAYER), this::fixEntity)
/*    */         });
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixName(Dynamic<?> paramDynamic) {
/* 39 */     Objects.requireNonNull(paramDynamic); return (Dynamic)DataFixUtils.orElse(paramDynamic.asString().result().map(this.renames).map(paramDynamic::createString), paramDynamic);
/*    */   }
/*    */   
/*    */   private Typed<?> fixItemStackTag(Typed<?> paramTyped) {
/* 43 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("AttributeModifiers", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> fixEntity(Typed<?> paramTyped) {
/* 51 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("Attributes", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AttributesRenameLegacy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */