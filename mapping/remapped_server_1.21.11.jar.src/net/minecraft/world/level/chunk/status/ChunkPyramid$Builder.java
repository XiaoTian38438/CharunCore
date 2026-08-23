/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ public class Builder
/*     */ {
/*  93 */   private final List<ChunkStep> steps = new ArrayList<>();
/*     */   
/*     */   public ChunkPyramid build() {
/*  96 */     return new ChunkPyramid(ImmutableList.copyOf(this.steps));
/*     */   }
/*     */   
/*     */   public Builder step(ChunkStatus paramChunkStatus, UnaryOperator<ChunkStep.Builder> paramUnaryOperator) {
/*     */     ChunkStep.Builder builder;
/* 101 */     if (this.steps.isEmpty()) {
/* 102 */       builder = new ChunkStep.Builder(paramChunkStatus);
/*     */     } else {
/* 104 */       builder = new ChunkStep.Builder(paramChunkStatus, this.steps.getLast());
/*     */     } 
/* 106 */     this.steps.add(((ChunkStep.Builder)paramUnaryOperator.apply(builder)).build());
/* 107 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\status\ChunkPyramid$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */