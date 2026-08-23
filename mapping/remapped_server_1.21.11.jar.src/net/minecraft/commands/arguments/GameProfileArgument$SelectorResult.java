/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
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
/*    */ public class SelectorResult
/*    */   implements GameProfileArgument.Result
/*    */ {
/*    */   private final EntitySelector selector;
/*    */   
/*    */   public SelectorResult(EntitySelector paramEntitySelector) {
/* 80 */     this.selector = paramEntitySelector;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<NameAndId> getNames(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/* 85 */     List list = this.selector.findPlayers(paramCommandSourceStack);
/* 86 */     if (list.isEmpty()) {
/* 87 */       throw EntityArgument.NO_PLAYERS_FOUND.create();
/*    */     }
/* 89 */     ArrayList<NameAndId> arrayList = new ArrayList();
/* 90 */     for (ServerPlayer serverPlayer : list) {
/* 91 */       arrayList.add(serverPlayer.nameAndId());
/*    */     }
/* 93 */     return arrayList;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\GameProfileArgument$SelectorResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */