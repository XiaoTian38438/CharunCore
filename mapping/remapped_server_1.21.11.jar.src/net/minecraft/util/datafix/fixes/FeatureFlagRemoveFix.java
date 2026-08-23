/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class FeatureFlagRemoveFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public FeatureFlagRemoveFix(Schema paramSchema, String paramString, Set<String> paramSet) {
/* 21 */     super(paramSchema, false);
/* 22 */     this.name = paramString;
/* 23 */     this.flagsToRemove = paramSet;
/*    */   }
/*    */   private final Set<String> flagsToRemove;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 28 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.LIGHTWEIGHT_LEVEL), paramTyped -> paramTyped.update(DSL.remainderFinder(), this::fixTag));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> Dynamic<T> fixTag(Dynamic<T> paramDynamic) {
/* 34 */     List list = (List)paramDynamic.get("removed_features").asStream().collect(Collectors.toCollection(java.util.ArrayList::new));
/* 35 */     Dynamic<T> dynamic = paramDynamic.update("enabled_features", paramDynamic2 -> {
/*    */           Objects.requireNonNull(paramDynamic1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           return (Dynamic)DataFixUtils.orElse(paramDynamic2.asStreamOpt().result().map(()).map(paramDynamic1::createList), paramDynamic2);
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     if (!list.isEmpty()) {
/* 50 */       dynamic = dynamic.set("removed_features", paramDynamic.createList(list.stream()));
/*    */     }
/* 52 */     return dynamic;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FeatureFlagRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */