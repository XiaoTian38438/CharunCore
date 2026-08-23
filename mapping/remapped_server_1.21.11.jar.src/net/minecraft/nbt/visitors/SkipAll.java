/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public interface SkipAll extends StreamTagVisitor {
/*  7 */   public static final SkipAll INSTANCE = new SkipAll() {
/*    */     
/*    */     };
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitEnd() {
/* 12 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(String paramString) {
/* 17 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(byte paramByte) {
/* 22 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(short paramShort) {
/* 27 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(int paramInt) {
/* 32 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(long paramLong) {
/* 37 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(float paramFloat) {
/* 42 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(double paramDouble) {
/* 47 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(byte[] paramArrayOfbyte) {
/* 52 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(int[] paramArrayOfint) {
/* 57 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visit(long[] paramArrayOflong) {
/* 62 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitList(TagType<?> paramTagType, int paramInt) {
/* 67 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.EntryResult visitElement(TagType<?> paramTagType, int paramInt) {
/* 72 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType) {
/* 77 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType, String paramString) {
/* 82 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitContainerEnd() {
/* 87 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   default StreamTagVisitor.ValueResult visitRootEntry(TagType<?> paramTagType) {
/* 92 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\visitors\SkipAll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */