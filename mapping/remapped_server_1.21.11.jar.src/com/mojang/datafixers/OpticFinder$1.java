/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
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
/*    */ class null
/*    */   implements OpticFinder<FT>
/*    */ {
/*    */   public Type<FT> type() {
/* 24 */     return outer.type();
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> paramType, Type<FR> paramType1, boolean paramBoolean) {
/* 29 */     Either either = outer.findType(type, paramType1, paramBoolean);
/* 30 */     return (Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException>)either.map(paramTypedOptic -> cap(paramType, paramTypedOptic, paramBoolean), Either::right);
/*    */   }
/*    */   
/*    */   private <A, FR, GR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> cap(Type<A> paramType, TypedOptic<GT, GR, FT, FR> paramTypedOptic, boolean paramBoolean) {
/* 34 */     Either either = DSL.fieldFinder(name, type).findType(paramType, paramTypedOptic.tType(), paramBoolean);
/* 35 */     return either.mapLeft(paramTypedOptic2 -> paramTypedOptic2.compose(paramTypedOptic1));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\OpticFinder$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */