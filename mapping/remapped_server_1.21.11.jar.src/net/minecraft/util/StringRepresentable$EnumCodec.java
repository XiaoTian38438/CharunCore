/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
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
/*    */ public class EnumCodec<E extends Enum<E> & StringRepresentable>
/*    */   extends StringRepresentable.StringRepresentableCodec<E>
/*    */ {
/*    */   private final Function<String, E> resolver;
/*    */   
/*    */   public EnumCodec(E[] paramArrayOfE, Function<String, E> paramFunction) {
/* 49 */     super(paramArrayOfE, paramFunction, paramObject -> ((Enum)paramObject).ordinal());
/* 50 */     this.resolver = paramFunction;
/*    */   }
/*    */   
/*    */   public E byName(String paramString) {
/* 54 */     return this.resolver.apply(paramString);
/*    */   }
/*    */   
/*    */   public E byName(String paramString, E paramE) {
/* 58 */     return (E)Objects.<Enum>requireNonNullElse((Enum)byName(paramString), (Enum)paramE);
/*    */   }
/*    */   
/*    */   public E byName(String paramString, Supplier<? extends E> paramSupplier) {
/* 62 */     return (E)Objects.<Enum>requireNonNullElseGet((Enum)byName(paramString), paramSupplier);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\StringRepresentable$EnumCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */