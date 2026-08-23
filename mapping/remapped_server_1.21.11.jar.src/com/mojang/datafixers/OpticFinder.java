/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface OpticFinder<FT>
/*    */ {
/*    */   Type<FT> type();
/*    */   
/*    */   <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> paramType, Type<FR> paramType1, boolean paramBoolean);
/*    */   
/*    */   default <A> Either<TypedOptic<A, ?, FT, FT>, Type.FieldNotFoundException> findType(Type<A> paramType, boolean paramBoolean) {
/* 16 */     return findType(paramType, type(), paramBoolean);
/*    */   }
/*    */   
/*    */   default <GT> OpticFinder<FT> inField(@Nullable final String name, final Type<GT> type) {
/* 20 */     final OpticFinder outer = this;
/* 21 */     return new OpticFinder<FT>()
/*    */       {
/*    */         public Type<FT> type() {
/* 24 */           return outer.type();
/*    */         }
/*    */ 
/*    */         
/*    */         public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> param1Type, Type<FR> param1Type1, boolean param1Boolean) {
/* 29 */           Either either = outer.findType(type, param1Type1, param1Boolean);
/* 30 */           return (Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException>)either.map(param1TypedOptic -> cap(param1Type, param1TypedOptic, param1Boolean), Either::right);
/*    */         }
/*    */         
/*    */         private <A, FR, GR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> cap(Type<A> param1Type, TypedOptic<GT, GR, FT, FR> param1TypedOptic, boolean param1Boolean) {
/* 34 */           Either either = DSL.fieldFinder(name, type).findType(param1Type, param1TypedOptic.tType(), param1Boolean);
/* 35 */           return either.mapLeft(param1TypedOptic2 -> param1TypedOptic2.compose(param1TypedOptic1));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\OpticFinder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */