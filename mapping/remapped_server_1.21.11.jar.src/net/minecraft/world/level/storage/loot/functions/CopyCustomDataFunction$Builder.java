/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
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
/*     */ public class Builder
/*     */   extends LootItemConditionalFunction.Builder<CopyCustomDataFunction.Builder>
/*     */ {
/*     */   private final NbtProvider source;
/*  99 */   private final List<CopyCustomDataFunction.CopyOperation> ops = Lists.newArrayList();
/*     */   
/*     */   Builder(NbtProvider paramNbtProvider) {
/* 102 */     this.source = paramNbtProvider;
/*     */   }
/*     */   
/*     */   public Builder copy(String paramString1, String paramString2, CopyCustomDataFunction.MergeStrategy paramMergeStrategy) {
/*     */     try {
/* 107 */       this.ops.add(new CopyCustomDataFunction.CopyOperation(NbtPathArgument.NbtPath.of(paramString1), NbtPathArgument.NbtPath.of(paramString2), paramMergeStrategy));
/* 108 */     } catch (CommandSyntaxException commandSyntaxException) {
/* 109 */       throw new IllegalArgumentException(commandSyntaxException);
/*     */     } 
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public Builder copy(String paramString1, String paramString2) {
/* 115 */     return copy(paramString1, paramString2, CopyCustomDataFunction.MergeStrategy.REPLACE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Builder getThis() {
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunction build() {
/* 125 */     return new CopyCustomDataFunction(getConditions(), this.source, this.ops);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */