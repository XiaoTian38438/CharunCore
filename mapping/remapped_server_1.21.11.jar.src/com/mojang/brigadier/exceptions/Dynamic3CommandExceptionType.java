/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class Dynamic3CommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Function function;
/*    */   
/*    */   public Dynamic3CommandExceptionType(Function paramFunction) {
/* 13 */     this.function = paramFunction;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create(Object paramObject1, Object paramObject2, Object paramObject3) {
/* 17 */     return new CommandSyntaxException(this, this.function.apply(paramObject1, paramObject2, paramObject3));
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader paramImmutableStringReader, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 21 */     return new CommandSyntaxException(this, this.function.apply(paramObject1, paramObject2, paramObject3), paramImmutableStringReader.getString(), paramImmutableStringReader.getCursor());
/*    */   }
/*    */   
/*    */   public static interface Function {
/*    */     Message apply(Object param1Object1, Object param1Object2, Object param1Object3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\Dynamic3CommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */