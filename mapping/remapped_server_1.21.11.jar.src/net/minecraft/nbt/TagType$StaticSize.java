/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface StaticSize<T extends Tag>
/*    */   extends TagType<T>
/*    */ {
/*    */   default void skip(DataInput paramDataInput, NbtAccounter paramNbtAccounter) throws IOException {
/* 31 */     paramDataInput.skipBytes(size());
/*    */   }
/*    */ 
/*    */   
/*    */   default void skip(DataInput paramDataInput, int paramInt, NbtAccounter paramNbtAccounter) throws IOException {
/* 36 */     paramDataInput.skipBytes(size() * paramInt);
/*    */   }
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\TagType$StaticSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */