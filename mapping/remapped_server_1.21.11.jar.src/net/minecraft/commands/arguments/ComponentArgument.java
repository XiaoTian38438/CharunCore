/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.SnbtGrammar;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.util.parsing.packrat.commands.CommandArgumentParser;
/*    */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ComponentArgument extends ParserBasedArgument<Component> {
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_COMPONENT;
/* 25 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "\"hello world\"", "'hello world'", "\"\"", "{text:\"hello world\"}", "[\"\"]" }); static {
/* 26 */     ERROR_INVALID_COMPONENT = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("argument.component.invalid", new Object[] { paramObject }));
/*    */   }
/* 28 */   private static final DynamicOps<Tag> OPS = (DynamicOps<Tag>)NbtOps.INSTANCE;
/* 29 */   private static final CommandArgumentParser<Tag> TAG_PARSER = (CommandArgumentParser<Tag>)SnbtGrammar.createParser(OPS);
/*    */   
/*    */   private ComponentArgument(HolderLookup.Provider paramProvider) {
/* 32 */     super(TAG_PARSER.withCodec((DynamicOps)paramProvider
/* 33 */           .createSerializationContext(OPS), TAG_PARSER, ComponentSerialization.CODEC, ERROR_INVALID_COMPONENT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Component getRawComponent(CommandContext<CommandSourceStack> paramCommandContext, String paramString) {
/* 41 */     return (Component)paramCommandContext.getArgument(paramString, Component.class);
/*    */   }
/*    */   
/*    */   public static Component getResolvedComponent(CommandContext<CommandSourceStack> paramCommandContext, String paramString, Entity paramEntity) throws CommandSyntaxException {
/* 45 */     return (Component)ComponentUtils.updateForEntity((CommandSourceStack)paramCommandContext.getSource(), getRawComponent(paramCommandContext, paramString), paramEntity, 0);
/*    */   }
/*    */   
/*    */   public static Component getResolvedComponent(CommandContext<CommandSourceStack> paramCommandContext, String paramString) throws CommandSyntaxException {
/* 49 */     return getResolvedComponent(paramCommandContext, paramString, ((CommandSourceStack)paramCommandContext.getSource()).getEntity());
/*    */   }
/*    */   
/*    */   public static ComponentArgument textComponent(CommandBuildContext paramCommandBuildContext) {
/* 53 */     return new ComponentArgument((HolderLookup.Provider)paramCommandBuildContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 58 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\ComponentArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */