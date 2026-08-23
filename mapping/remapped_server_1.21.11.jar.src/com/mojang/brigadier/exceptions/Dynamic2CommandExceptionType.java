/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class Dynamic2CommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Function function;
/*    */   
/*    */   public Dynamic2CommandExceptionType(Function paramFunction) {
/* 13 */     this.function = paramFunction;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create(Object paramObject1, Object paramObject2) {
/* 17 */     return new CommandSyntaxException(this, this.function.apply(paramObject1, paramObject2));
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader paramImmutableStringReader, Object paramObject1, Object paramObject2) {
/* 21 */     return new CommandSyntaxException(this, this.function.apply(paramObject1, paramObject2), paramImmutableStringReader.getString(), paramImmutableStringReader.getCursor());
/*    */   }
/*    */   
/*    */   public static interface Function {
/*    */     Message apply(Object param1Object1, Object param1Object2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\Dynamic2CommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */