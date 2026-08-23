/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Deque;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public class SkipFields extends CollectToTag {
/* 10 */   private final Deque<FieldTree> stack = new ArrayDeque<>();
/*    */   
/*    */   public SkipFields(FieldSelector... paramVarArgs) {
/* 13 */     FieldTree fieldTree = FieldTree.createRoot();
/* 14 */     for (FieldSelector fieldSelector : paramVarArgs) {
/* 15 */       fieldTree.addEntry(fieldSelector);
/*    */     }
/* 17 */     this.stack.push(fieldTree);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType, String paramString) {
/* 22 */     FieldTree fieldTree = this.stack.element();
/* 23 */     if (fieldTree.isSelected(paramTagType, paramString)) {
/* 24 */       return StreamTagVisitor.EntryResult.SKIP;
/*    */     }
/*    */     
/* 27 */     if (paramTagType == CompoundTag.TYPE) {
/* 28 */       FieldTree fieldTree1 = fieldTree.fieldsToRecurse().get(paramString);
/* 29 */       if (fieldTree1 != null) {
/* 30 */         this.stack.push(fieldTree1);
/*    */       }
/*    */     } 
/*    */     
/* 34 */     return super.visitEntry(paramTagType, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult visitContainerEnd() {
/* 39 */     if (depth() == ((FieldTree)this.stack.element()).depth()) {
/* 40 */       this.stack.pop();
/*    */     }
/* 42 */     return super.visitContainerEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\visitors\SkipFields.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */