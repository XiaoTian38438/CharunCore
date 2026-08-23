/*     */ package com.mojang.brigadier.context;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandContextBuilder<S>
/*     */ {
/*  17 */   private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();
/*     */   private final CommandNode<S> rootNode;
/*  19 */   private final List<ParsedCommandNode<S>> nodes = new ArrayList<>();
/*     */   private final CommandDispatcher<S> dispatcher;
/*     */   private S source;
/*     */   private Command<S> command;
/*     */   private CommandContextBuilder<S> child;
/*     */   private StringRange range;
/*  25 */   private RedirectModifier<S> modifier = null;
/*     */   private boolean forks;
/*     */   
/*     */   public CommandContextBuilder(CommandDispatcher<S> paramCommandDispatcher, S paramS, CommandNode<S> paramCommandNode, int paramInt) {
/*  29 */     this.rootNode = paramCommandNode;
/*  30 */     this.dispatcher = paramCommandDispatcher;
/*  31 */     this.source = paramS;
/*  32 */     this.range = StringRange.at(paramInt);
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withSource(S paramS) {
/*  36 */     this.source = paramS;
/*  37 */     return this;
/*     */   }
/*     */   
/*     */   public S getSource() {
/*  41 */     return this.source;
/*     */   }
/*     */   
/*     */   public CommandNode<S> getRootNode() {
/*  45 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withArgument(String paramString, ParsedArgument<S, ?> paramParsedArgument) {
/*  49 */     this.arguments.put(paramString, paramParsedArgument);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public Map<String, ParsedArgument<S, ?>> getArguments() {
/*  54 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withCommand(Command<S> paramCommand) {
/*  58 */     this.command = paramCommand;
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withNode(CommandNode<S> paramCommandNode, StringRange paramStringRange) {
/*  63 */     this.nodes.add(new ParsedCommandNode<>(paramCommandNode, paramStringRange));
/*  64 */     this.range = StringRange.encompassing(this.range, paramStringRange);
/*  65 */     this.modifier = paramCommandNode.getRedirectModifier();
/*  66 */     this.forks = paramCommandNode.isFork();
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> copy() {
/*  71 */     CommandContextBuilder<S> commandContextBuilder = new CommandContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.getStart());
/*  72 */     commandContextBuilder.command = this.command;
/*  73 */     commandContextBuilder.arguments.putAll(this.arguments);
/*  74 */     commandContextBuilder.nodes.addAll(this.nodes);
/*  75 */     commandContextBuilder.child = this.child;
/*  76 */     commandContextBuilder.range = this.range;
/*  77 */     commandContextBuilder.forks = this.forks;
/*  78 */     return commandContextBuilder;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withChild(CommandContextBuilder<S> paramCommandContextBuilder) {
/*  82 */     this.child = paramCommandContextBuilder;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> getChild() {
/*  87 */     return this.child;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> getLastChild() {
/*  91 */     CommandContextBuilder<S> commandContextBuilder = this;
/*  92 */     while (commandContextBuilder.getChild() != null) {
/*  93 */       commandContextBuilder = commandContextBuilder.getChild();
/*     */     }
/*  95 */     return commandContextBuilder;
/*     */   }
/*     */   
/*     */   public Command<S> getCommand() {
/*  99 */     return this.command;
/*     */   }
/*     */   
/*     */   public List<ParsedCommandNode<S>> getNodes() {
/* 103 */     return this.nodes;
/*     */   }
/*     */   
/*     */   public CommandContext<S> build(String paramString) {
/* 107 */     return new CommandContext<>(this.source, paramString, this.arguments, this.command, this.rootNode, this.nodes, this.range, (this.child == null) ? null : this.child.build(paramString), this.modifier, this.forks);
/*     */   }
/*     */   
/*     */   public CommandDispatcher<S> getDispatcher() {
/* 111 */     return this.dispatcher;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/* 115 */     return this.range;
/*     */   }
/*     */   
/*     */   public SuggestionContext<S> findSuggestionContext(int paramInt) {
/* 119 */     if (this.range.getStart() <= paramInt) {
/* 120 */       if (this.range.getEnd() < paramInt) {
/* 121 */         if (this.child != null)
/* 122 */           return this.child.findSuggestionContext(paramInt); 
/* 123 */         if (!this.nodes.isEmpty()) {
/* 124 */           ParsedCommandNode<S> parsedCommandNode = this.nodes.get(this.nodes.size() - 1);
/* 125 */           return new SuggestionContext<>(parsedCommandNode.getNode(), parsedCommandNode.getRange().getEnd() + 1);
/*     */         } 
/* 127 */         return new SuggestionContext<>(this.rootNode, this.range.getStart());
/*     */       } 
/*     */       
/* 130 */       CommandNode<S> commandNode = this.rootNode;
/* 131 */       for (ParsedCommandNode<S> parsedCommandNode : this.nodes) {
/* 132 */         StringRange stringRange = parsedCommandNode.getRange();
/* 133 */         if (stringRange.getStart() <= paramInt && paramInt <= stringRange.getEnd()) {
/* 134 */           return new SuggestionContext<>(commandNode, stringRange.getStart());
/*     */         }
/* 136 */         commandNode = parsedCommandNode.getNode();
/*     */       } 
/* 138 */       if (commandNode == null) {
/* 139 */         throw new IllegalStateException("Can't find node before cursor");
/*     */       }
/* 141 */       return new SuggestionContext<>(commandNode, this.range.getStart());
/*     */     } 
/*     */     
/* 144 */     throw new IllegalStateException("Can't find node before cursor");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\CommandContextBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */