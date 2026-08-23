/*     */ package net.minecraft.nbt.visitors;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import net.minecraft.nbt.ByteArrayTag;
/*     */ import net.minecraft.nbt.ByteTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.DoubleTag;
/*     */ import net.minecraft.nbt.EndTag;
/*     */ import net.minecraft.nbt.FloatTag;
/*     */ import net.minecraft.nbt.IntArrayTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.LongArrayTag;
/*     */ import net.minecraft.nbt.LongTag;
/*     */ import net.minecraft.nbt.ShortTag;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagType;
/*     */ 
/*     */ public class CollectToTag
/*     */   implements StreamTagVisitor
/*     */ {
/*  25 */   private final Deque<ContainerBuilder> containerStack = new ArrayDeque<>();
/*     */   
/*     */   public CollectToTag() {
/*  28 */     this.containerStack.addLast(new RootBuilder());
/*     */   }
/*     */   
/*     */   public Tag getResult() {
/*  32 */     return ((ContainerBuilder)this.containerStack.getFirst()).build();
/*     */   }
/*     */   
/*     */   protected int depth() {
/*  36 */     return this.containerStack.size() - 1;
/*     */   }
/*     */   
/*     */   private void appendEntry(Tag paramTag) {
/*  40 */     ((ContainerBuilder)this.containerStack.getLast()).acceptValue(paramTag);
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitEnd() {
/*  45 */     appendEntry((Tag)EndTag.INSTANCE);
/*  46 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(String paramString) {
/*  51 */     appendEntry((Tag)StringTag.valueOf(paramString));
/*  52 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(byte paramByte) {
/*  57 */     appendEntry((Tag)ByteTag.valueOf(paramByte));
/*  58 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(short paramShort) {
/*  63 */     appendEntry((Tag)ShortTag.valueOf(paramShort));
/*  64 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(int paramInt) {
/*  69 */     appendEntry((Tag)IntTag.valueOf(paramInt));
/*  70 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(long paramLong) {
/*  75 */     appendEntry((Tag)LongTag.valueOf(paramLong));
/*  76 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(float paramFloat) {
/*  81 */     appendEntry((Tag)FloatTag.valueOf(paramFloat));
/*  82 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(double paramDouble) {
/*  87 */     appendEntry((Tag)DoubleTag.valueOf(paramDouble));
/*  88 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(byte[] paramArrayOfbyte) {
/*  93 */     appendEntry((Tag)new ByteArrayTag(paramArrayOfbyte));
/*  94 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(int[] paramArrayOfint) {
/*  99 */     appendEntry((Tag)new IntArrayTag(paramArrayOfint));
/* 100 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visit(long[] paramArrayOflong) {
/* 105 */     appendEntry((Tag)new LongArrayTag(paramArrayOflong));
/* 106 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitList(TagType<?> paramTagType, int paramInt) {
/* 111 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.EntryResult visitElement(TagType<?> paramTagType, int paramInt) {
/* 116 */     enterContainerIfNeeded(paramTagType);
/* 117 */     return StreamTagVisitor.EntryResult.ENTER;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType) {
/* 122 */     return StreamTagVisitor.EntryResult.ENTER;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.EntryResult visitEntry(TagType<?> paramTagType, String paramString) {
/* 127 */     ((ContainerBuilder)this.containerStack.getLast()).acceptKey(paramString);
/* 128 */     enterContainerIfNeeded(paramTagType);
/* 129 */     return StreamTagVisitor.EntryResult.ENTER;
/*     */   }
/*     */   
/*     */   private void enterContainerIfNeeded(TagType<?> paramTagType) {
/* 133 */     if (paramTagType == ListTag.TYPE) {
/* 134 */       this.containerStack.addLast(new ListBuilder());
/* 135 */     } else if (paramTagType == CompoundTag.TYPE) {
/* 136 */       this.containerStack.addLast(new CompoundBuilder());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitContainerEnd() {
/* 142 */     ContainerBuilder containerBuilder = this.containerStack.removeLast();
/* 143 */     Tag tag = containerBuilder.build();
/* 144 */     if (tag != null) {
/* 145 */       ((ContainerBuilder)this.containerStack.getLast()).acceptValue(tag);
/*     */     }
/* 147 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> paramTagType) {
/* 152 */     enterContainerIfNeeded(paramTagType);
/* 153 */     return StreamTagVisitor.ValueResult.CONTINUE;
/*     */   }
/*     */   
/*     */   private static interface ContainerBuilder
/*     */   {
/*     */     default void acceptKey(String param1String) {}
/*     */     
/*     */     void acceptValue(Tag param1Tag);
/*     */     
/*     */     Tag build();
/*     */   }
/*     */   
/*     */   private static class RootBuilder
/*     */     implements ContainerBuilder {
/*     */     private Tag result;
/*     */     
/*     */     public void acceptValue(Tag param1Tag) {
/* 170 */       this.result = param1Tag;
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag build() {
/* 175 */       return this.result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompoundBuilder implements ContainerBuilder {
/* 180 */     private final CompoundTag compound = new CompoundTag();
/* 181 */     private String lastId = "";
/*     */ 
/*     */     
/*     */     public void acceptKey(String param1String) {
/* 185 */       this.lastId = param1String;
/*     */     }
/*     */ 
/*     */     
/*     */     public void acceptValue(Tag param1Tag) {
/* 190 */       this.compound.put(this.lastId, param1Tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag build() {
/* 195 */       return (Tag)this.compound;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ListBuilder implements ContainerBuilder {
/* 200 */     private final ListTag list = new ListTag();
/*     */ 
/*     */     
/*     */     public void acceptValue(Tag param1Tag) {
/* 204 */       this.list.addAndUnwrap(param1Tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag build() {
/* 209 */       return (Tag)this.list;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\visitors\CollectToTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */