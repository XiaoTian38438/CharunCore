/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NodeResolver<S>
/*     */ {
/*     */   private final CommandBuildContext context;
/*     */   private final ClientboundCommandsPacket.NodeBuilder<S> builder;
/*     */   private final List<ClientboundCommandsPacket.Entry> entries;
/*     */   private final List<CommandNode<S>> nodes;
/*     */   
/*     */   NodeResolver(CommandBuildContext paramCommandBuildContext, ClientboundCommandsPacket.NodeBuilder<S> paramNodeBuilder, List<ClientboundCommandsPacket.Entry> paramList) {
/* 300 */     this.context = paramCommandBuildContext;
/* 301 */     this.builder = paramNodeBuilder;
/* 302 */     this.entries = paramList;
/* 303 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/* 304 */     objectArrayList.size(paramList.size());
/* 305 */     this.nodes = (List<CommandNode<S>>)objectArrayList;
/*     */   }
/*     */   
/*     */   public CommandNode<S> resolve(int paramInt) {
/* 309 */     CommandNode<S> commandNode2, commandNode1 = this.nodes.get(paramInt);
/* 310 */     if (commandNode1 != null) {
/* 311 */       return commandNode1;
/*     */     }
/*     */     
/* 314 */     ClientboundCommandsPacket.Entry entry = this.entries.get(paramInt);
/*     */ 
/*     */     
/* 317 */     if (entry.stub == null) {
/* 318 */       RootCommandNode rootCommandNode = new RootCommandNode();
/*     */     } else {
/* 320 */       ArgumentBuilder<S, ?> argumentBuilder = entry.stub.build(this.context, this.builder);
/* 321 */       if ((entry.flags & 0x8) != 0) {
/* 322 */         argumentBuilder.redirect(resolve(entry.redirect));
/*     */       }
/* 324 */       boolean bool1 = ((entry.flags & 0x4) != 0) ? true : false;
/* 325 */       boolean bool2 = ((entry.flags & 0x20) != 0) ? true : false;
/* 326 */       commandNode2 = this.builder.configure(argumentBuilder, bool1, bool2).build();
/*     */     } 
/* 328 */     this.nodes.set(paramInt, commandNode2);
/*     */     
/* 330 */     for (int i : entry.children) {
/* 331 */       CommandNode<S> commandNode = resolve(i);
/* 332 */       if (!(commandNode instanceof RootCommandNode)) {
/* 333 */         commandNode2.addChild(commandNode);
/*     */       }
/*     */     } 
/* 336 */     return commandNode2;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */