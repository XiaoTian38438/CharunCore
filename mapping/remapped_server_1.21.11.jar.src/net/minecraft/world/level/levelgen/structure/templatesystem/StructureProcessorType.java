/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ 
/*    */ public interface StructureProcessorType<P extends StructureProcessor> {
/* 12 */   public static final Codec<StructureProcessor> SINGLE_CODEC = BuiltInRegistries.STRUCTURE_PROCESSOR.byNameCodec().dispatch("processor_type", StructureProcessor::getType, StructureProcessorType::codec);
/* 13 */   public static final Codec<StructureProcessorList> LIST_OBJECT_CODEC = SINGLE_CODEC.listOf().xmap(StructureProcessorList::new, StructureProcessorList::list);
/*    */   
/* 15 */   public static final Codec<StructureProcessorList> DIRECT_CODEC = Codec.withAlternative(LIST_OBJECT_CODEC
/* 16 */       .fieldOf("processors").codec(), LIST_OBJECT_CODEC);
/*    */ 
/*    */   
/* 19 */   public static final Codec<Holder<StructureProcessorList>> LIST_CODEC = (Codec<Holder<StructureProcessorList>>)RegistryFileCodec.create(Registries.PROCESSOR_LIST, DIRECT_CODEC);
/* 20 */   public static final StructureProcessorType<BlockIgnoreProcessor> BLOCK_IGNORE = register("block_ignore", BlockIgnoreProcessor.CODEC);
/* 21 */   public static final StructureProcessorType<BlockRotProcessor> BLOCK_ROT = register("block_rot", BlockRotProcessor.CODEC);
/* 22 */   public static final StructureProcessorType<GravityProcessor> GRAVITY = register("gravity", GravityProcessor.CODEC);
/* 23 */   public static final StructureProcessorType<JigsawReplacementProcessor> JIGSAW_REPLACEMENT = register("jigsaw_replacement", JigsawReplacementProcessor.CODEC);
/* 24 */   public static final StructureProcessorType<RuleProcessor> RULE = register("rule", RuleProcessor.CODEC);
/* 25 */   public static final StructureProcessorType<NopProcessor> NOP = register("nop", NopProcessor.CODEC);
/* 26 */   public static final StructureProcessorType<BlockAgeProcessor> BLOCK_AGE = register("block_age", BlockAgeProcessor.CODEC);
/* 27 */   public static final StructureProcessorType<BlackstoneReplaceProcessor> BLACKSTONE_REPLACE = register("blackstone_replace", BlackstoneReplaceProcessor.CODEC);
/* 28 */   public static final StructureProcessorType<LavaSubmergedBlockProcessor> LAVA_SUBMERGED_BLOCK = register("lava_submerged_block", LavaSubmergedBlockProcessor.CODEC);
/* 29 */   public static final StructureProcessorType<ProtectedBlockProcessor> PROTECTED_BLOCKS = register("protected_blocks", ProtectedBlockProcessor.CODEC);
/* 30 */   public static final StructureProcessorType<CappedProcessor> CAPPED = register("capped", CappedProcessor.CODEC);
/*    */ 
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   static <P extends StructureProcessor> StructureProcessorType<P> register(String paramString, MapCodec<P> paramMapCodec) {
/* 36 */     return (StructureProcessorType<P>)Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, paramString, () -> paramMapCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */