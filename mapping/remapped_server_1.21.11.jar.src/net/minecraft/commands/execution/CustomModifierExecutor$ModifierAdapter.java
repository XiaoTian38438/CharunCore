/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.RedirectModifier;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ModifierAdapter<T>
/*    */   extends RedirectModifier<T>, CustomModifierExecutor<T>
/*    */ {
/*    */   default Collection<T> apply(CommandContext<T> paramCommandContext) throws CommandSyntaxException {
/* 17 */     throw new UnsupportedOperationException("This function should not run");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\CustomModifierExecutor$ModifierAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */