/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class DynamicNCommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Function function;
/*    */   
/*    */   public DynamicNCommandExceptionType(Function paramFunction) {
/* 13 */     this.function = paramFunction;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create(Object paramObject, Object... paramVarArgs) {
/* 17 */     return new CommandSyntaxException(this, this.function.apply(paramVarArgs));
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader paramImmutableStringReader, Object... paramVarArgs) {
/* 21 */     return new CommandSyntaxException(this, this.function.apply(paramVarArgs), paramImmutableStringReader.getString(), paramImmutableStringReader.getCursor());
/*    */   }
/*    */   
/*    */   public static interface Function {
/*    */     Message apply(Object[] param1ArrayOfObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\DynamicNCommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */