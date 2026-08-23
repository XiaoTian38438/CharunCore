/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class BlockRotProcessor extends StructureProcessor {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("rottable_blocks").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("integrity").forGetter(())).apply((Applicative)paramInstance, BlockRotProcessor::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<BlockRotProcessor> CODEC;
/*    */   private final Optional<HolderSet<Block>> rottableBlocks;
/*    */   private final float integrity;
/*    */   
/*    */   public BlockRotProcessor(HolderSet<Block> paramHolderSet, float paramFloat) {
/* 26 */     this(Optional.of(paramHolderSet), paramFloat);
/*    */   }
/*    */   
/*    */   public BlockRotProcessor(float paramFloat) {
/* 30 */     this(Optional.empty(), paramFloat);
/*    */   }
/*    */   
/*    */   private BlockRotProcessor(Optional<HolderSet<Block>> paramOptional, float paramFloat) {
/* 34 */     this.integrity = paramFloat;
/* 35 */     this.rottableBlocks = paramOptional;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings) {
/* 40 */     RandomSource randomSource = paramStructurePlaceSettings.getRandom(paramStructureBlockInfo2.pos());
/*    */     
/* 42 */     if ((this.rottableBlocks.isPresent() && !paramStructureBlockInfo1.state().is(this.rottableBlocks.get())) || randomSource.nextFloat() <= this.integrity) {
/* 43 */       return paramStructureBlockInfo2;
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 50 */     return StructureProcessorType.BLOCK_ROT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockRotProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */