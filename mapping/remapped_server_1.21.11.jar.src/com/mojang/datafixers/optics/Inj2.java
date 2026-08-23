/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ public final class Inj2<F, G, G2>
/*    */   implements Prism<Either<F, G>, Either<F, G2>, G, G2>
/*    */ {
/*  8 */   public static final Inj2<?, ?, ?> INSTANCE = new Inj2();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Either<Either<F, G2>, G> match(Either<F, G> paramEither) {
/* 15 */     return (Either<Either<F, G2>, G>)paramEither.map(paramObject -> Either.left(Either.left(paramObject)), Either::right);
/*    */   }
/*    */ 
/*    */   
/*    */   public Either<F, G2> build(G2 paramG2) {
/* 20 */     return Either.right(paramG2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "inj2";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Inj2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */