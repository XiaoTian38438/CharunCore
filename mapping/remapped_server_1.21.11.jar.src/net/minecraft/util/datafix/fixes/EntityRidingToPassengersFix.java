/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class EntityRidingToPassengersFix
/*    */   extends DataFix {
/*    */   public EntityRidingToPassengersFix(Schema paramSchema, boolean paramBoolean) {
/* 22 */     super(paramSchema, paramBoolean);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Schema schema1 = getInputSchema();
/* 28 */     Schema schema2 = getOutputSchema();
/*    */     
/* 30 */     Type<?> type1 = schema1.getTypeRaw(References.ENTITY_TREE);
/* 31 */     Type<?> type2 = schema2.getTypeRaw(References.ENTITY_TREE);
/* 32 */     Type<?> type3 = schema1.getTypeRaw(References.ENTITY);
/*    */     
/* 34 */     return cap(schema1, schema2, type1, type2, type3);
/*    */   }
/*    */   
/*    */   private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule cap(Schema paramSchema1, Schema paramSchema2, Type<OldEntityTree> paramType, Type<NewEntityTree> paramType1, Type<Entity> paramType2) {
/* 38 */     Type type1 = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/* 39 */           DSL.optional((Type)DSL.field("Riding", paramType)), paramType2));
/*    */ 
/*    */ 
/*    */     
/* 43 */     Type type2 = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/* 44 */           DSL.optional((Type)DSL.field("Passengers", (Type)DSL.list(paramType1))), paramType2));
/*    */ 
/*    */ 
/*    */     
/* 48 */     Type type3 = paramSchema1.getType(References.ENTITY_TREE);
/* 49 */     Type type4 = paramSchema2.getType(References.ENTITY_TREE);
/*    */     
/* 51 */     if (!Objects.equals(type3, type1)) {
/* 52 */       throw new IllegalStateException("Old entity type is not what was expected.");
/*    */     }
/*    */     
/* 55 */     if (!type4.equals(type2, true, true)) {
/* 56 */       throw new IllegalStateException("New entity type is not what was expected.");
/*    */     }
/*    */     
/* 59 */     OpticFinder opticFinder1 = DSL.typeFinder(type1);
/* 60 */     OpticFinder opticFinder2 = DSL.typeFinder(type2);
/* 61 */     OpticFinder opticFinder3 = DSL.typeFinder(paramType1);
/*    */     
/* 63 */     Type type5 = paramSchema1.getType(References.PLAYER);
/* 64 */     Type type6 = paramSchema2.getType(References.PLAYER);
/*    */     
/* 66 */     return TypeRewriteRule.seq(
/* 67 */         fixTypeEverywhere("EntityRidingToPassengerFix", type1, type2, paramDynamicOps -> ()), 
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
/*    */ 
/*    */         
/* 90 */         writeAndRead("player RootVehicle injecter", type5, type6));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityRidingToPassengersFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */