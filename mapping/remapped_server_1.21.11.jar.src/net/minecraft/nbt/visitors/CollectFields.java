/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Deque;
/*    */ import java.util.Set;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.StreamTagVisitor;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public class CollectFields
/*    */   extends CollectToTag {
/*    */   private int fieldsToGetCount;
/*    */   private final Set<TagType<?>> wantedTypes;
/* 15 */   private final Deque<FieldTree> stack = new ArrayDeque<>();
/*    */   
/*    */   public CollectFields(FieldSelector... paramVarArgs) {
/* 18 */     this.fieldsToGetCount = paramVarArgs.length;
/*    */     
/* 20 */     ImmutableSet.Builder builder = ImmutableSet.builder();
/* 21 */     FieldTree fieldTree = FieldTree.createRoot();
/* 22 */     for (FieldSelector fieldSelector : paramVarArgs) {
/* 23 */       fieldTree.addEntry(fieldSelector);
/* 24 */       builder.add(fieldSelector.type());
/*    */     } 
/* 26 */     this.stack.push(fieldTree);
/*    */     
/* 28 */     builder.add(CompoundTag.TYPE);
/* 29 */     this.wantedTypes = (Set<TagType<?>>)builder.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> paramTagType) {
/* 34 */     if (paramTagType != CompoundTag.TYPE) {
/* 35 */       return StreamTagVisitor.ValueResult.HALT;
/*    */     }
/* 37 */     return super.visitRootEntry(paramTagType);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType) {
/* 42 */     FieldTree fieldTree = this.stack.element();
/* 43 */     if (depth() > fieldTree.depth()) {
/* 44 */       return super.visitEntry(paramTagType);
/*    */     }
/* 46 */     if (this.fieldsToGetCount <= 0) {
/* 47 */       return StreamTagVisitor.EntryResult.BREAK;
/*    */     }
/* 49 */     if (!this.wantedTypes.contains(paramTagType)) {
/* 50 */       return StreamTagVisitor.EntryResult.SKIP;
/*    */     }
/* 52 */     return super.visitEntry(paramTagType);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType, String paramString) {
/* 57 */     FieldTree fieldTree = this.stack.element();
/* 58 */     if (depth() > fieldTree.depth()) {
/* 59 */       return super.visitEntry(paramTagType, paramString);
/*    */     }
/*    */     
/* 62 */     if (fieldTree.selectedFields().remove(paramString, paramTagType)) {
/* 63 */       this.fieldsToGetCount--;
/* 64 */       return super.visitEntry(paramTagType, paramString);
/*    */     } 
/*    */     
/* 67 */     if (paramTagType == CompoundTag.TYPE) {
/* 68 */       FieldTree fieldTree1 = fieldTree.fieldsToRecurse().get(paramString);
/* 69 */       if (fieldTree1 != null) {
/* 70 */         this.stack.push(fieldTree1);
/* 71 */         return super.visitEntry(paramTagType, paramString);
/*    */       } 
/*    */     } 
/*    */     
/* 75 */     return StreamTagVisitor.EntryResult.SKIP;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamTagVisitor.ValueResult visitContainerEnd() {
/* 80 */     if (depth() == ((FieldTree)this.stack.element()).depth()) {
/* 81 */       this.stack.pop();
/*    */     }
/* 83 */     return super.visitContainerEnd();
/*    */   }
/*    */   
/*    */   public int getMissingFieldCount() {
/* 87 */     return this.fieldsToGetCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\visitors\CollectFields.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */