/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ public final class Inj1<F, G, F2>
/*    */   implements Prism<Either<F, G>, Either<F2, G>, F, F2>
/*    */ {
/*  8 */   public static final Inj1<?, ?, ?> INSTANCE = new Inj1();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Either<Either<F2, G>, F> match(Either<F, G> paramEither) {
/* 15 */     return (Either<Either<F2, G>, F>)paramEither.map(Either::right, paramObject -> Either.left(Either.right(paramObject)));
/*    */   }
/*    */ 
/*    */   
/*    */   public Either<F2, G> build(F2 paramF2) {
/* 20 */     return Either.left(paramF2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "inj1";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Inj1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */