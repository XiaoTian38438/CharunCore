/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class AttributesRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AttributesRenameFix(Schema paramSchema, String paramString, UnaryOperator<String> paramUnaryOperator) {
/* 19 */     super(paramSchema, false);
/* 20 */     this.name = paramString;
/* 21 */     this.renames = paramUnaryOperator;
/*    */   }
/*    */   private final UnaryOperator<String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     return TypeRewriteRule.seq(
/* 27 */         fixTypeEverywhereTyped(this.name + " (Components)", getInputSchema().getType(References.DATA_COMPONENTS), this::fixDataComponents), new TypeRewriteRule[] {
/* 28 */           fixTypeEverywhereTyped(this.name + " (Entity)", getInputSchema().getType(References.ENTITY), this::fixEntity), 
/* 29 */           fixTypeEverywhereTyped(this.name + " (Player)", getInputSchema().getType(References.PLAYER), this::fixEntity)
/*    */         });
/*    */   }
/*    */   
/*    */   private Typed<?> fixDataComponents(Typed<?> paramTyped) {
/* 34 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("minecraft:attribute_modifiers", ()));
/*    */   }
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
/*    */   private Typed<?> fixEntity(Typed<?> paramTyped) {
/* 47 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> paramDynamic.update("attributes", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixIdField(Dynamic<?> paramDynamic) {
/* 58 */     return ExtraDataFixUtils.fixStringField(paramDynamic, "id", this.renames);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTypeField(Dynamic<?> paramDynamic) {
/* 62 */     return ExtraDataFixUtils.fixStringField(paramDynamic, "type", this.renames);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\AttributesRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */