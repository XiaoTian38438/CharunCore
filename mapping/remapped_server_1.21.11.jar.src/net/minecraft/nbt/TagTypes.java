/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public class TagTypes {
/*  4 */   private static final TagType<?>[] TYPES = new TagType[] { EndTag.TYPE, ByteTag.TYPE, ShortTag.TYPE, IntTag.TYPE, LongTag.TYPE, FloatTag.TYPE, DoubleTag.TYPE, ByteArrayTag.TYPE, StringTag.TYPE, ListTag.TYPE, CompoundTag.TYPE, IntArrayTag.TYPE, LongArrayTag.TYPE };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TagType<?> getType(int paramInt) {
/* 21 */     if (paramInt < 0 || paramInt >= TYPES.length) {
/* 22 */       return TagType.createInvalid(paramInt);
/*    */     }
/*    */     
/* 25 */     return TYPES[paramInt];
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\TagTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */