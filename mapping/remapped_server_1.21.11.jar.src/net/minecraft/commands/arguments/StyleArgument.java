/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.SnbtGrammar;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.parsing.packrat.commands.CommandArgumentParser;
/*    */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*    */ 
/*    */ public class StyleArgument extends ParserBasedArgument<Style> {
/* 21 */   private static final Collection<String> EXAMPLES = List.of("{bold: true}", "{color: 'red'}", "{}");
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_STYLE;
/*    */ 
/*    */   
/*    */   static {
/* 27 */     ERROR_INVALID_STYLE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("argument.style.invalid", new Object[] { paramObject }));
/*    */   }
/* 29 */   private static final DynamicOps<Tag> OPS = (DynamicOps<Tag>)NbtOps.INSTANCE;
/* 30 */   private static final CommandArgumentParser<Tag> TAG_PARSER = (CommandArgumentParser<Tag>)SnbtGrammar.createParser(OPS);
/*    */   
/*    */   private StyleArgument(HolderLookup.Provider paramProvider) {
/* 33 */     super(TAG_PARSER.withCodec((DynamicOps)paramProvider
/* 34 */           .createSerializationContext(OPS), TAG_PARSER, Style.Serializer.CODEC, ERROR_INVALID_STYLE));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Style getStyle(CommandContext<CommandSourceStack> paramCommandContext, String paramString) {
/* 42 */     return (Style)paramCommandContext.getArgument(paramString, Style.class);
/*    */   }
/*    */   
/*    */   public static StyleArgument style(CommandBuildContext paramCommandBuildContext) {
/* 46 */     return new StyleArgument((HolderLookup.Provider)paramCommandBuildContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 51 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\StyleArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */