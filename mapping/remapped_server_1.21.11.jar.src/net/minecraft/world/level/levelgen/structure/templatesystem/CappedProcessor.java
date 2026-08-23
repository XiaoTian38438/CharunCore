/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public class CappedProcessor extends StructureProcessor {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter(()), (App)IntProvider.POSITIVE_CODEC.fieldOf("limit").forGetter(())).apply((Applicative)paramInstance, CappedProcessor::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<CappedProcessor> CODEC;
/*    */   private final StructureProcessor delegate;
/*    */   private final IntProvider limit;
/*    */   
/*    */   public CappedProcessor(StructureProcessor paramStructureProcessor, IntProvider paramIntProvider) {
/* 26 */     this.delegate = paramStructureProcessor;
/* 27 */     this.limit = paramIntProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 32 */     return StructureProcessorType.CAPPED;
/*    */   }
/*    */ 
/*    */   
/*    */   public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor paramServerLevelAccessor, BlockPos paramBlockPos1, BlockPos paramBlockPos2, List<StructureTemplate.StructureBlockInfo> paramList1, List<StructureTemplate.StructureBlockInfo> paramList2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 37 */     if (this.limit.getMaxValue() == 0 || paramList2.isEmpty()) {
/* 38 */       return paramList2;
/*    */     }
/*    */     
/* 41 */     if (paramList1.size() != paramList2.size()) {
/* 42 */       Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + paramList1.size() + ", Processed size: " + paramList2.size());
/* 43 */       return paramList2;
/*    */     } 
/*    */     
/* 46 */     RandomSource randomSource = RandomSource.create(paramServerLevelAccessor.getLevel().getSeed()).forkPositional().at(paramBlockPos1);
/*    */     
/* 48 */     int i = Math.min(this.limit.sample(randomSource), paramList2.size());
/*    */     
/* 50 */     if (i < 1) {
/* 51 */       return paramList2;
/*    */     }
/*    */     
/* 54 */     IntArrayList intArrayList = Util.toShuffledList(IntStream.range(0, paramList2.size()), randomSource);
/*    */     
/* 56 */     IntIterator intIterator = intArrayList.intIterator();
/* 57 */     byte b = 0;
/*    */     
/* 59 */     while (intIterator.hasNext() && b < i) {
/* 60 */       int j = intIterator.nextInt();
/* 61 */       StructureTemplate.StructureBlockInfo structureBlockInfo1 = paramList1.get(j);
/* 62 */       StructureTemplate.StructureBlockInfo structureBlockInfo2 = paramList2.get(j);
/*    */       
/* 64 */       StructureTemplate.StructureBlockInfo structureBlockInfo3 = this.delegate.processBlock((LevelReader)paramServerLevelAccessor, paramBlockPos1, paramBlockPos2, structureBlockInfo1, structureBlockInfo2, paramStructurePlaceSettings);
/*    */       
/* 66 */       if (structureBlockInfo3 != null && !structureBlockInfo2.equals(structureBlockInfo3)) {
/* 67 */         b++;
/* 68 */         paramList2.set(j, structureBlockInfo3);
/*    */       } 
/*    */     } 
/*    */     
/* 72 */     return paramList2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\CappedProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */