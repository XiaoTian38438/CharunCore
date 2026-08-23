/*     */ package com.mojang.brigadier.tree;
/*     */ 
/*     */ import com.mojang.brigadier.AmbiguityConsumer;
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CommandNode<S>
/*     */   implements Comparable<CommandNode<S>>
/*     */ {
/*  27 */   private final Map<String, CommandNode<S>> children = new LinkedHashMap<>();
/*  28 */   private final Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap<>();
/*  29 */   private final Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap<>();
/*     */   private final Predicate<S> requirement;
/*     */   private final CommandNode<S> redirect;
/*     */   private final RedirectModifier<S> modifier;
/*     */   private final boolean forks;
/*     */   private Command<S> command;
/*     */   
/*     */   protected CommandNode(Command<S> paramCommand, Predicate<S> paramPredicate, CommandNode<S> paramCommandNode, RedirectModifier<S> paramRedirectModifier, boolean paramBoolean) {
/*  37 */     this.command = paramCommand;
/*  38 */     this.requirement = paramPredicate;
/*  39 */     this.redirect = paramCommandNode;
/*  40 */     this.modifier = paramRedirectModifier;
/*  41 */     this.forks = paramBoolean;
/*     */   }
/*     */   
/*     */   public Command<S> getCommand() {
/*  45 */     return this.command;
/*     */   }
/*     */   
/*     */   public Collection<CommandNode<S>> getChildren() {
/*  49 */     return this.children.values();
/*     */   }
/*     */   
/*     */   public CommandNode<S> getChild(String paramString) {
/*  53 */     return this.children.get(paramString);
/*     */   }
/*     */   
/*     */   public CommandNode<S> getRedirect() {
/*  57 */     return this.redirect;
/*     */   }
/*     */   
/*     */   public RedirectModifier<S> getRedirectModifier() {
/*  61 */     return this.modifier;
/*     */   }
/*     */   
/*     */   public boolean canUse(S paramS) {
/*  65 */     return this.requirement.test(paramS);
/*     */   }
/*     */   
/*     */   public void addChild(CommandNode<S> paramCommandNode) {
/*  69 */     if (paramCommandNode instanceof RootCommandNode) {
/*  70 */       throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
/*     */     }
/*     */     
/*  73 */     CommandNode commandNode = this.children.get(paramCommandNode.getName());
/*  74 */     if (commandNode != null) {
/*     */       
/*  76 */       if (paramCommandNode.getCommand() != null) {
/*  77 */         commandNode.command = paramCommandNode.getCommand();
/*     */       }
/*  79 */       for (CommandNode<S> commandNode1 : paramCommandNode.getChildren()) {
/*  80 */         commandNode.addChild(commandNode1);
/*     */       }
/*     */     } else {
/*  83 */       this.children.put(paramCommandNode.getName(), paramCommandNode);
/*  84 */       if (paramCommandNode instanceof LiteralCommandNode) {
/*  85 */         this.literals.put(paramCommandNode.getName(), (LiteralCommandNode<S>)paramCommandNode);
/*  86 */       } else if (paramCommandNode instanceof ArgumentCommandNode) {
/*  87 */         this.arguments.put(paramCommandNode.getName(), (ArgumentCommandNode)paramCommandNode);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void findAmbiguities(AmbiguityConsumer<S> paramAmbiguityConsumer) {
/*  93 */     HashSet<String> hashSet = new HashSet();
/*     */     
/*  95 */     for (CommandNode<S> commandNode : this.children.values()) {
/*  96 */       for (CommandNode<S> commandNode1 : this.children.values()) {
/*  97 */         if (commandNode == commandNode1) {
/*     */           continue;
/*     */         }
/*     */         
/* 101 */         for (String str : commandNode.getExamples()) {
/* 102 */           if (commandNode1.isValidInput(str)) {
/* 103 */             hashSet.add(str);
/*     */           }
/*     */         } 
/*     */         
/* 107 */         if (hashSet.size() > 0) {
/* 108 */           paramAmbiguityConsumer.ambiguous(this, commandNode, commandNode1, hashSet);
/* 109 */           hashSet = new HashSet<>();
/*     */         } 
/*     */       } 
/*     */       
/* 113 */       commandNode.findAmbiguities(paramAmbiguityConsumer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 121 */     if (this == paramObject) return true; 
/* 122 */     if (!(paramObject instanceof CommandNode)) return false;
/*     */     
/* 124 */     CommandNode commandNode = (CommandNode)paramObject;
/*     */     
/* 126 */     if (!this.children.equals(commandNode.children)) return false; 
/* 127 */     if ((this.command != null) ? !this.command.equals(commandNode.command) : (commandNode.command != null)) return false;
/*     */     
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 134 */     return 31 * this.children.hashCode() + ((this.command != null) ? this.command.hashCode() : 0);
/*     */   }
/*     */   
/*     */   public Predicate<S> getRequirement() {
/* 138 */     return this.requirement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<? extends CommandNode<S>> getRelevantNodes(StringReader paramStringReader) {
/* 154 */     if (this.literals.size() > 0) {
/* 155 */       int i = paramStringReader.getCursor();
/* 156 */       while (paramStringReader.canRead() && paramStringReader.peek() != ' ') {
/* 157 */         paramStringReader.skip();
/*     */       }
/* 159 */       String str = paramStringReader.getString().substring(i, paramStringReader.getCursor());
/* 160 */       paramStringReader.setCursor(i);
/* 161 */       LiteralCommandNode<S> literalCommandNode = this.literals.get(str);
/* 162 */       if (literalCommandNode != null) {
/* 163 */         return Collections.singleton(literalCommandNode);
/*     */       }
/* 165 */       return this.arguments.values();
/*     */     } 
/*     */     
/* 168 */     return this.arguments.values();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(CommandNode<S> paramCommandNode) {
/* 174 */     if (this instanceof LiteralCommandNode == paramCommandNode instanceof LiteralCommandNode) {
/* 175 */       return getSortedKey().compareTo(paramCommandNode.getSortedKey());
/*     */     }
/*     */     
/* 178 */     return (paramCommandNode instanceof LiteralCommandNode) ? 1 : -1;
/*     */   }
/*     */   
/*     */   public boolean isFork() {
/* 182 */     return this.forks;
/*     */   }
/*     */   
/*     */   protected abstract boolean isValidInput(String paramString);
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract String getUsageText();
/*     */   
/*     */   public abstract void parse(StringReader paramStringReader, CommandContextBuilder<S> paramCommandContextBuilder) throws CommandSyntaxException;
/*     */   
/*     */   public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) throws CommandSyntaxException;
/*     */   
/*     */   public abstract ArgumentBuilder<S, ?> createBuilder();
/*     */   
/*     */   protected abstract String getSortedKey();
/*     */   
/*     */   public abstract Collection<String> getExamples();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\tree\CommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */