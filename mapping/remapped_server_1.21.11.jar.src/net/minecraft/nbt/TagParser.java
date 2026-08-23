/*    */ package net.minecraft.nbt;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*    */ 
/*    */ public class TagParser<T> {
/* 14 */   public static final SimpleCommandExceptionType ERROR_TRAILING_DATA = new SimpleCommandExceptionType((Message)Component.translatable("argument.nbt.trailing"));
/* 15 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_COMPOUND = new SimpleCommandExceptionType((Message)Component.translatable("argument.nbt.expected.compound"));
/*    */   
/*    */   public static final char ELEMENT_SEPARATOR = ',';
/*    */   
/*    */   public static final char NAME_VALUE_SEPARATOR = ':';
/* 20 */   private static final TagParser<Tag> NBT_OPS_PARSER = create(NbtOps.INSTANCE); public static final Codec<CompoundTag> FLATTENED_CODEC;
/*    */   
/*    */   static {
/* 23 */     FLATTENED_CODEC = Codec.STRING.comapFlatMap(paramString -> {
/*    */           try {
/*    */             Tag tag = NBT_OPS_PARSER.parseFully(paramString); if (tag instanceof CompoundTag) {
/*    */               CompoundTag compoundTag = (CompoundTag)tag;
/*    */               return DataResult.success(compoundTag, Lifecycle.stable());
/*    */             } 
/*    */             return DataResult.error(());
/* 30 */           } catch (CommandSyntaxException commandSyntaxException) {
/*    */             Objects.requireNonNull(commandSyntaxException);
/*    */             return DataResult.error(commandSyntaxException::getMessage);
/*    */           } 
/*    */         }CompoundTag::toString);
/*    */   }
/*    */   
/* 37 */   public static final Codec<CompoundTag> LENIENT_CODEC = Codec.withAlternative(FLATTENED_CODEC, CompoundTag.CODEC);
/*    */ 
/*    */   
/*    */   private final DynamicOps<T> ops;
/*    */   
/*    */   private final Grammar<T> grammar;
/*    */ 
/*    */   
/*    */   private TagParser(DynamicOps<T> paramDynamicOps, Grammar<T> paramGrammar) {
/* 46 */     this.ops = paramDynamicOps;
/* 47 */     this.grammar = paramGrammar;
/*    */   }
/*    */   
/*    */   public DynamicOps<T> getOps() {
/* 51 */     return this.ops;
/*    */   }
/*    */   
/*    */   public static <T> TagParser<T> create(DynamicOps<T> paramDynamicOps) {
/* 55 */     return new TagParser<>(paramDynamicOps, SnbtGrammar.createParser(paramDynamicOps));
/*    */   }
/*    */   
/*    */   private static CompoundTag castToCompoundOrThrow(StringReader paramStringReader, Tag paramTag) throws CommandSyntaxException {
/* 59 */     if (paramTag instanceof CompoundTag) return (CompoundTag)paramTag;
/*    */ 
/*    */     
/* 62 */     throw ERROR_EXPECTED_COMPOUND.createWithContext(paramStringReader);
/*    */   }
/*    */   
/*    */   public static CompoundTag parseCompoundFully(String paramString) throws CommandSyntaxException {
/* 66 */     StringReader stringReader = new StringReader(paramString);
/* 67 */     return castToCompoundOrThrow(stringReader, NBT_OPS_PARSER.parseFully(stringReader));
/*    */   }
/*    */   
/*    */   public T parseFully(String paramString) throws CommandSyntaxException {
/* 71 */     return parseFully(new StringReader(paramString));
/*    */   }
/*    */   
/*    */   public T parseFully(StringReader paramStringReader) throws CommandSyntaxException {
/* 75 */     Object object = this.grammar.parseForCommands(paramStringReader);
/*    */     
/* 77 */     paramStringReader.skipWhitespace();
/*    */     
/* 79 */     if (paramStringReader.canRead()) {
/* 80 */       throw ERROR_TRAILING_DATA.createWithContext(paramStringReader);
/*    */     }
/* 82 */     return (T)object;
/*    */   }
/*    */   
/*    */   public T parseAsArgument(StringReader paramStringReader) throws CommandSyntaxException {
/* 86 */     return (T)this.grammar.parseForCommands(paramStringReader);
/*    */   }
/*    */   
/*    */   public static CompoundTag parseCompoundAsArgument(StringReader paramStringReader) throws CommandSyntaxException {
/* 90 */     Tag tag = NBT_OPS_PARSER.parseAsArgument(paramStringReader);
/* 91 */     return castToCompoundOrThrow(paramStringReader, tag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\TagParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */