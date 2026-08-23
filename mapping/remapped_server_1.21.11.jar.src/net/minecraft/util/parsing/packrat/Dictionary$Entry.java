/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ class Entry<S, T>
/*    */   implements NamedRule<S, T>, Supplier<String>
/*    */ {
/*    */   private final Atom<T> name;
/*    */   Rule<S, T> value;
/*    */   
/*    */   private Entry(Atom<T> paramAtom) {
/* 81 */     this.name = paramAtom;
/*    */   }
/*    */ 
/*    */   
/*    */   public Atom<T> name() {
/* 86 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public Rule<S, T> value() {
/* 91 */     return Objects.<Rule<S, T>>requireNonNull(this.value, this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String get() {
/* 97 */     return "Unbound rule " + String.valueOf(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\Dictionary$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */