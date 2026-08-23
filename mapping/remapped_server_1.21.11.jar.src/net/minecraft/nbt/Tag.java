/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Tag
/*     */ {
/*     */   public static final int OBJECT_HEADER = 8;
/*     */   public static final int ARRAY_HEADER = 12;
/*     */   public static final int OBJECT_REFERENCE = 4;
/*     */   public static final int STRING_SIZE = 28;
/*     */   public static final byte TAG_END = 0;
/*     */   public static final byte TAG_BYTE = 1;
/*     */   public static final byte TAG_SHORT = 2;
/*     */   public static final byte TAG_INT = 3;
/*     */   public static final byte TAG_LONG = 4;
/*     */   public static final byte TAG_FLOAT = 5;
/*     */   public static final byte TAG_DOUBLE = 6;
/*     */   public static final byte TAG_BYTE_ARRAY = 7;
/*     */   public static final byte TAG_STRING = 8;
/*     */   public static final byte TAG_LIST = 9;
/*     */   public static final byte TAG_COMPOUND = 10;
/*     */   public static final byte TAG_INT_ARRAY = 11;
/*     */   public static final byte TAG_LONG_ARRAY = 12;
/*     */   public static final int MAX_DEPTH = 512;
/*     */   
/*     */   default void acceptAsRoot(StreamTagVisitor paramStreamTagVisitor) {
/*  52 */     StreamTagVisitor.ValueResult valueResult = paramStreamTagVisitor.visitRootEntry(getType());
/*  53 */     if (valueResult == StreamTagVisitor.ValueResult.CONTINUE) {
/*  54 */       accept(paramStreamTagVisitor);
/*     */     }
/*     */   }
/*     */   
/*     */   default Optional<String> asString() {
/*  59 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<Number> asNumber() {
/*  63 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<Byte> asByte() {
/*  67 */     return asNumber().map(Number::byteValue);
/*     */   }
/*     */   
/*     */   default Optional<Short> asShort() {
/*  71 */     return asNumber().map(Number::shortValue);
/*     */   }
/*     */   
/*     */   default Optional<Integer> asInt() {
/*  75 */     return asNumber().map(Number::intValue);
/*     */   }
/*     */   
/*     */   default Optional<Long> asLong() {
/*  79 */     return asNumber().map(Number::longValue);
/*     */   }
/*     */   
/*     */   default Optional<Float> asFloat() {
/*  83 */     return asNumber().map(Number::floatValue);
/*     */   }
/*     */   
/*     */   default Optional<Double> asDouble() {
/*  87 */     return asNumber().map(Number::doubleValue);
/*     */   }
/*     */   
/*     */   default Optional<Boolean> asBoolean() {
/*  91 */     return asByte().map(paramByte -> Boolean.valueOf((paramByte.byteValue() != 0)));
/*     */   }
/*     */   
/*     */   default Optional<byte[]> asByteArray() {
/*  95 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<int[]> asIntArray() {
/*  99 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<long[]> asLongArray() {
/* 103 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<CompoundTag> asCompound() {
/* 107 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default Optional<ListTag> asList() {
/* 111 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   void write(DataOutput paramDataOutput) throws IOException;
/*     */   
/*     */   String toString();
/*     */   
/*     */   byte getId();
/*     */   
/*     */   TagType<?> getType();
/*     */   
/*     */   Tag copy();
/*     */   
/*     */   int sizeInBytes();
/*     */   
/*     */   void accept(TagVisitor paramTagVisitor);
/*     */   
/*     */   StreamTagVisitor.ValueResult accept(StreamTagVisitor paramStreamTagVisitor);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\Tag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */