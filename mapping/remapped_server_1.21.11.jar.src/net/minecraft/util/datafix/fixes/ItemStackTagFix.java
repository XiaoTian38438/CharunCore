/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class ItemStackTagFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final Predicate<String> idFilter;
/*    */   
/*    */   public ItemStackTagFix(Schema paramSchema, String paramString, Predicate<String> paramPredicate) {
/* 23 */     super(paramSchema, false);
/* 24 */     this.name = paramString;
/* 25 */     this.idFilter = paramPredicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public final TypeRewriteRule makeRule() {
/* 30 */     Type<?> type = getInputSchema().getType(References.ITEM_STACK);
/* 31 */     return fixTypeEverywhereTyped(this.name, type, createFixer(type, this.idFilter, this::fixItemStackTag));
/*    */   }
/*    */   
/*    */   public static UnaryOperator<Typed<?>> createFixer(Type<?> paramType, Predicate<String> paramPredicate, UnaryOperator<Typed<?>> paramUnaryOperator) {
/* 35 */     OpticFinder opticFinder1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 36 */     OpticFinder opticFinder2 = paramType.findField("tag");
/* 37 */     return paramTyped -> {
/*    */         Optional<Pair> optional = paramTyped.getOptional(paramOpticFinder1);
/* 39 */         return (optional.isPresent() && paramPredicate.test((String)((Pair)optional.get()).getSecond())) ? paramTyped.updateTyped(paramOpticFinder2, paramUnaryOperator) : paramTyped;
/*    */       };
/*    */   }
/*    */   
/*    */   protected abstract Typed<?> fixItemStackTag(Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ItemStackTagFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */