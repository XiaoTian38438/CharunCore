/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
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
/*     */ class ListWrapper
/*     */   implements ValueOutput.ValueOutputList
/*     */ {
/*     */   private final String fieldName;
/*     */   private final ProblemReporter problemReporter;
/*     */   private final DynamicOps<Tag> ops;
/*     */   private final ListTag output;
/*     */   
/*     */   ListWrapper(String paramString, ProblemReporter paramProblemReporter, DynamicOps<Tag> paramDynamicOps, ListTag paramListTag) {
/* 154 */     this.fieldName = paramString;
/* 155 */     this.problemReporter = paramProblemReporter;
/* 156 */     this.ops = paramDynamicOps;
/* 157 */     this.output = paramListTag;
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueOutput addChild() {
/* 162 */     int i = this.output.size();
/* 163 */     CompoundTag compoundTag = new CompoundTag();
/* 164 */     this.output.add(compoundTag);
/* 165 */     return new TagValueOutput(this.problemReporter.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement(this.fieldName, i)), this.ops, compoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardLast() {
/* 170 */     this.output.removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 175 */     return this.output.isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\TagValueOutput$ListWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */