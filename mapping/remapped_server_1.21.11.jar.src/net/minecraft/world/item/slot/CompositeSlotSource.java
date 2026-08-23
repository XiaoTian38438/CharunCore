/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeSlotSource implements SlotSource {
/*    */   protected final List<SlotSource> terms;
/*    */   
/*    */   protected CompositeSlotSource(List<SlotSource> paramList) {
/* 18 */     this.terms = paramList;
/* 19 */     this.compositeSlotSource = SlotSources.group(paramList);
/*    */   }
/*    */   private final Function<LootContext, SlotCollection> compositeSlotSource;
/*    */   protected static <T extends CompositeSlotSource> MapCodec<T> createCodec(Function<List<SlotSource>, T> paramFunction) {
/* 23 */     return RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)SlotSources.CODEC.listOf().fieldOf("terms").forGetter(())).apply((Applicative)paramInstance, paramFunction));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static <T extends CompositeSlotSource> Codec<T> createInlineCodec(Function<List<SlotSource>, T> paramFunction) {
/* 29 */     return SlotSources.CODEC.listOf().xmap(paramFunction, paramCompositeSlotSource -> paramCompositeSlotSource.terms);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotCollection provide(LootContext paramLootContext) {
/* 37 */     return this.compositeSlotSource.apply(paramLootContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext paramValidationContext) {
/* 42 */     super.validate(paramValidationContext);
/*    */     
/* 44 */     for (byte b = 0; b < this.terms.size(); b++)
/* 45 */       ((SlotSource)this.terms.get(b)).validate(paramValidationContext.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("terms", b))); 
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends CompositeSlotSource> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\slot\CompositeSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */