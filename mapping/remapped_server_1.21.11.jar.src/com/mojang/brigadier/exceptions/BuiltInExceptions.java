/*     */ package com.mojang.brigadier.exceptions;
/*     */ public class BuiltInExceptions implements BuiltInExceptionProvider {
/*     */   private static final Dynamic2CommandExceptionType DOUBLE_TOO_SMALL;
/*     */   private static final Dynamic2CommandExceptionType DOUBLE_TOO_BIG;
/*     */   private static final Dynamic2CommandExceptionType FLOAT_TOO_SMALL;
/*     */   private static final Dynamic2CommandExceptionType FLOAT_TOO_BIG;
/*     */   
/*     */   static {
/*   9 */     DOUBLE_TOO_SMALL = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Double must not be less than " + paramObject2 + ", found " + paramObject1));
/*  10 */     DOUBLE_TOO_BIG = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Double must not be more than " + paramObject2 + ", found " + paramObject1));
/*     */     
/*  12 */     FLOAT_TOO_SMALL = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Float must not be less than " + paramObject2 + ", found " + paramObject1));
/*  13 */     FLOAT_TOO_BIG = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Float must not be more than " + paramObject2 + ", found " + paramObject1));
/*     */     
/*  15 */     INTEGER_TOO_SMALL = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Integer must not be less than " + paramObject2 + ", found " + paramObject1));
/*  16 */     INTEGER_TOO_BIG = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Integer must not be more than " + paramObject2 + ", found " + paramObject1));
/*     */     
/*  18 */     LONG_TOO_SMALL = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Long must not be less than " + paramObject2 + ", found " + paramObject1));
/*  19 */     LONG_TOO_BIG = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> new LiteralMessage("Long must not be more than " + paramObject2 + ", found " + paramObject1));
/*     */     
/*  21 */     LITERAL_INCORRECT = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Expected literal " + paramObject));
/*     */   }
/*  23 */   private static final Dynamic2CommandExceptionType INTEGER_TOO_SMALL; private static final Dynamic2CommandExceptionType INTEGER_TOO_BIG; private static final Dynamic2CommandExceptionType LONG_TOO_SMALL; private static final Dynamic2CommandExceptionType LONG_TOO_BIG; private static final DynamicCommandExceptionType LITERAL_INCORRECT; private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected quote to start a string"));
/*  24 */   private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType((Message)new LiteralMessage("Unclosed quoted string")); private static final DynamicCommandExceptionType READER_INVALID_ESCAPE; private static final DynamicCommandExceptionType READER_INVALID_BOOL; private static final DynamicCommandExceptionType READER_INVALID_INT; static {
/*  25 */     READER_INVALID_ESCAPE = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Invalid escape sequence '" + paramObject + "' in quoted string"));
/*  26 */     READER_INVALID_BOOL = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Invalid bool, expected true or false but found '" + paramObject + "'"));
/*  27 */     READER_INVALID_INT = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Invalid integer '" + paramObject + "'"));
/*  28 */   } private static final SimpleCommandExceptionType READER_EXPECTED_INT = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected integer")); private static final DynamicCommandExceptionType READER_INVALID_LONG; static {
/*  29 */     READER_INVALID_LONG = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Invalid long '" + paramObject + "'"));
/*  30 */   } private static final SimpleCommandExceptionType READER_EXPECTED_LONG = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected long")); private static final DynamicCommandExceptionType READER_INVALID_DOUBLE; static {
/*  31 */     READER_INVALID_DOUBLE = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Invalid double '" + paramObject + "'"));
/*  32 */   } private static final SimpleCommandExceptionType READER_EXPECTED_DOUBLE = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected double")); private static final DynamicCommandExceptionType READER_INVALID_FLOAT; static {
/*  33 */     READER_INVALID_FLOAT = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Invalid float '" + paramObject + "'"));
/*  34 */   } private static final SimpleCommandExceptionType READER_EXPECTED_FLOAT = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected float"));
/*  35 */   private static final SimpleCommandExceptionType READER_EXPECTED_BOOL = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected bool")); private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL; static {
/*  36 */     READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Expected '" + paramObject + "'"));
/*     */   }
/*  38 */   private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType((Message)new LiteralMessage("Unknown command"));
/*  39 */   private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType((Message)new LiteralMessage("Incorrect argument for command"));
/*  40 */   private static final SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType((Message)new LiteralMessage("Expected whitespace to end one argument, but found trailing data")); private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION; static {
/*  41 */     DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType(paramObject -> new LiteralMessage("Could not parse command: " + paramObject));
/*     */   }
/*     */   
/*     */   public Dynamic2CommandExceptionType doubleTooLow() {
/*  45 */     return DOUBLE_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType doubleTooHigh() {
/*  50 */     return DOUBLE_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType floatTooLow() {
/*  55 */     return FLOAT_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType floatTooHigh() {
/*  60 */     return FLOAT_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType integerTooLow() {
/*  65 */     return INTEGER_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType integerTooHigh() {
/*  70 */     return INTEGER_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType longTooLow() {
/*  75 */     return LONG_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType longTooHigh() {
/*  80 */     return LONG_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType literalIncorrect() {
/*  85 */     return LITERAL_INCORRECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedStartOfQuote() {
/*  90 */     return READER_EXPECTED_START_OF_QUOTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedEndOfQuote() {
/*  95 */     return READER_EXPECTED_END_OF_QUOTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidEscape() {
/* 100 */     return READER_INVALID_ESCAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidBool() {
/* 105 */     return READER_INVALID_BOOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidInt() {
/* 110 */     return READER_INVALID_INT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedInt() {
/* 115 */     return READER_EXPECTED_INT;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidLong() {
/* 120 */     return READER_INVALID_LONG;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedLong() {
/* 125 */     return READER_EXPECTED_LONG;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidDouble() {
/* 130 */     return READER_INVALID_DOUBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedDouble() {
/* 135 */     return READER_EXPECTED_DOUBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidFloat() {
/* 140 */     return READER_INVALID_FLOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedFloat() {
/* 145 */     return READER_EXPECTED_FLOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedBool() {
/* 150 */     return READER_EXPECTED_BOOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerExpectedSymbol() {
/* 155 */     return READER_EXPECTED_SYMBOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType dispatcherUnknownCommand() {
/* 160 */     return DISPATCHER_UNKNOWN_COMMAND;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType dispatcherUnknownArgument() {
/* 165 */     return DISPATCHER_UNKNOWN_ARGUMENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
/* 170 */     return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType dispatcherParseException() {
/* 175 */     return DISPATCHER_PARSE_EXCEPTION;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\exceptions\BuiltInExceptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */