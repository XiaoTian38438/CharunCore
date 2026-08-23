/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityRedundantChanceTagsFix extends DataFix {
/* 13 */   private static final Codec<List<Float>> FLOAT_LIST_CODEC = Codec.FLOAT.listOf();
/*    */   
/*    */   public EntityRedundantChanceTagsFix(Schema paramSchema, boolean paramBoolean) {
/* 16 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 21 */     return fixTypeEverywhereTyped("EntityRedundantChanceTagsFix", getInputSchema().getType(References.ENTITY), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
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
/*    */   private static boolean isZeroList(OptionalDynamic<?> paramOptionalDynamic, int paramInt) {
/* 34 */     Objects.requireNonNull(FLOAT_LIST_CODEC); return ((Boolean)paramOptionalDynamic.flatMap(FLOAT_LIST_CODEC::parse).map(paramList -> Boolean.valueOf((paramList.size() == paramInt && paramList.stream().allMatch(())))).result().orElse(Boolean.valueOf(false))).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityRedundantChanceTagsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */