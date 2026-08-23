package net.minecraft.nbt;

public interface TagVisitor {
  void visitString(StringTag paramStringTag);
  
  void visitByte(ByteTag paramByteTag);
  
  void visitShort(ShortTag paramShortTag);
  
  void visitInt(IntTag paramIntTag);
  
  void visitLong(LongTag paramLongTag);
  
  void visitFloat(FloatTag paramFloatTag);
  
  void visitDouble(DoubleTag paramDoubleTag);
  
  void visitByteArray(ByteArrayTag paramByteArrayTag);
  
  void visitIntArray(IntArrayTag paramIntArrayTag);
  
  void visitLongArray(LongArrayTag paramLongArrayTag);
  
  void visitList(ListTag paramListTag);
  
  void visitCompound(CompoundTag paramCompoundTag);
  
  void visitEnd(EndTag paramEndTag);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\TagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */