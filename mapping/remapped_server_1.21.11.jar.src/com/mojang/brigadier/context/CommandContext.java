/*     */ package com.mojang.brigadier.context;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandContext<S>
/*     */ {
/*  16 */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();
/*     */   
/*     */   static {
/*  19 */     PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
/*  20 */     PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
/*  21 */     PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
/*  22 */     PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
/*  23 */     PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
/*  24 */     PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
/*  25 */     PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
/*  26 */     PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final S source;
/*     */ 
/*     */   
/*     */   private final String input;
/*     */ 
/*     */   
/*     */   private final Command<S> command;
/*     */ 
/*     */   
/*     */   private final Map<String, ParsedArgument<S, ?>> arguments;
/*     */ 
/*     */   
/*     */   private final CommandNode<S> rootNode;
/*     */ 
/*     */   
/*     */   private final List<ParsedCommandNode<S>> nodes;
/*     */   
/*     */   private final StringRange range;
/*     */   
/*     */   private final CommandContext<S> child;
/*     */   
/*     */   private final RedirectModifier<S> modifier;
/*     */   
/*     */   private final boolean forks;
/*     */ 
/*     */   
/*     */   public CommandContext(S paramS, String paramString, Map<String, ParsedArgument<S, ?>> paramMap, Command<S> paramCommand, CommandNode<S> paramCommandNode, List<ParsedCommandNode<S>> paramList, StringRange paramStringRange, CommandContext<S> paramCommandContext, RedirectModifier<S> paramRedirectModifier, boolean paramBoolean) {
/*  58 */     this.source = paramS;
/*  59 */     this.input = paramString;
/*  60 */     this.arguments = paramMap;
/*  61 */     this.command = paramCommand;
/*  62 */     this.rootNode = paramCommandNode;
/*  63 */     this.nodes = paramList;
/*  64 */     this.range = paramStringRange;
/*  65 */     this.child = paramCommandContext;
/*  66 */     this.modifier = paramRedirectModifier;
/*  67 */     this.forks = paramBoolean;
/*     */   }
/*     */   
/*     */   public CommandContext<S> copyFor(S paramS) {
/*  71 */     if (this.source == paramS) {
/*  72 */       return this;
/*     */     }
/*  74 */     return new CommandContext(paramS, this.input, this.arguments, this.command, this.rootNode, this.nodes, this.range, this.child, this.modifier, this.forks);
/*     */   }
/*     */   
/*     */   public CommandContext<S> getChild() {
/*  78 */     return this.child;
/*     */   }
/*     */   
/*     */   public CommandContext<S> getLastChild() {
/*  82 */     CommandContext<S> commandContext = this;
/*  83 */     while (commandContext.getChild() != null) {
/*  84 */       commandContext = commandContext.getChild();
/*     */     }
/*  86 */     return commandContext;
/*     */   }
/*     */   
/*     */   public Command<S> getCommand() {
/*  90 */     return this.command;
/*     */   }
/*     */   
/*     */   public S getSource() {
/*  94 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V getArgument(String paramString, Class<V> paramClass) {
/*  99 */     ParsedArgument parsedArgument = this.arguments.get(paramString);
/*     */     
/* 101 */     if (parsedArgument == null) {
/* 102 */       throw new IllegalArgumentException("No such argument '" + paramString + "' exists on this command");
/*     */     }
/*     */     
/* 105 */     Object object = parsedArgument.getResult();
/* 106 */     if (((Class)PRIMITIVE_TO_WRAPPER.getOrDefault(paramClass, paramClass)).isAssignableFrom(object.getClass())) {
/* 107 */       return (V)object;
/*     */     }
/* 109 */     throw new IllegalArgumentException("Argument '" + paramString + "' is defined as " + object.getClass().getSimpleName() + ", not " + paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 115 */     if (this == paramObject) return true; 
/* 116 */     if (!(paramObject instanceof CommandContext)) return false;
/*     */     
/* 118 */     CommandContext commandContext = (CommandContext)paramObject;
/*     */     
/* 120 */     if (!this.arguments.equals(commandContext.arguments)) return false; 
/* 121 */     if (!this.rootNode.equals(commandContext.rootNode)) return false; 
/* 122 */     if (this.nodes.size() != commandContext.nodes.size() || !this.nodes.equals(commandContext.nodes)) return false; 
/* 123 */     if ((this.command != null) ? !this.command.equals(commandContext.command) : (commandContext.command != null)) return false; 
/* 124 */     if (!this.source.equals(commandContext.source)) return false; 
/* 125 */     if ((this.child != null) ? !this.child.equals(commandContext.child) : (commandContext.child != null)) return false;
/*     */     
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 132 */     int i = this.source.hashCode();
/* 133 */     i = 31 * i + this.arguments.hashCode();
/* 134 */     i = 31 * i + ((this.command != null) ? this.command.hashCode() : 0);
/* 135 */     i = 31 * i + this.rootNode.hashCode();
/* 136 */     i = 31 * i + this.nodes.hashCode();
/* 137 */     i = 31 * i + ((this.child != null) ? this.child.hashCode() : 0);
/* 138 */     return i;
/*     */   }
/*     */   
/*     */   public RedirectModifier<S> getRedirectModifier() {
/* 142 */     return this.modifier;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/* 146 */     return this.range;
/*     */   }
/*     */   
/*     */   public String getInput() {
/* 150 */     return this.input;
/*     */   }
/*     */   
/*     */   public CommandNode<S> getRootNode() {
/* 154 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public List<ParsedCommandNode<S>> getNodes() {
/* 158 */     return this.nodes;
/*     */   }
/*     */   
/*     */   public boolean hasNodes() {
/* 162 */     return !this.nodes.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isForked() {
/* 166 */     return this.forks;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\CommandContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */