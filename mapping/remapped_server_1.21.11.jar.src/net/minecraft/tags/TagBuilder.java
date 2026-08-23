/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class TagBuilder
/*    */ {
/*  9 */   private final List<TagEntry> entries = new ArrayList<>();
/*    */   
/*    */   public static TagBuilder create() {
/* 12 */     return new TagBuilder();
/*    */   }
/*    */   
/*    */   public List<TagEntry> build() {
/* 16 */     return List.copyOf(this.entries);
/*    */   }
/*    */   
/*    */   public TagBuilder add(TagEntry paramTagEntry) {
/* 20 */     this.entries.add(paramTagEntry);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public TagBuilder addElement(Identifier paramIdentifier) {
/* 25 */     return add(TagEntry.element(paramIdentifier));
/*    */   }
/*    */   
/*    */   public TagBuilder addOptionalElement(Identifier paramIdentifier) {
/* 29 */     return add(TagEntry.optionalElement(paramIdentifier));
/*    */   }
/*    */   
/*    */   public TagBuilder addTag(Identifier paramIdentifier) {
/* 33 */     return add(TagEntry.tag(paramIdentifier));
/*    */   }
/*    */   
/*    */   public TagBuilder addOptionalTag(Identifier paramIdentifier) {
/* 37 */     return add(TagEntry.optionalTag(paramIdentifier));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\TagBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */