/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicCommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Function<Object, Message> function;
/*    */   
/*    */   public DynamicCommandExceptionType(Function<Object, Message> paramFunction) {
/* 15 */     this.function = paramFunction;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create(Object paramObject) {
/* 19 */     return new CommandSyntaxException(this, this.function.apply(paramObject));
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader paramImmutableStringReader, Object paramObject) {
/* 23 */     return new CommandSyntaxException(this, this.function.apply(paramObject), paramImmutableStringReader.getString(), paramImmutableStringReader.getCursor());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\DynamicCommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */