/*     */ package net.minecraft.world.item;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AdventureModePredicate {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final Codec<AdventureModePredicate> CODEC; public static final StreamCodec<RegistryFriendlyByteBuf, AdventureModePredicate> STREAM_CODEC;
/*     */   
/*     */   static {
/*  29 */     CODEC = ExtraCodecs.compactListCodec(BlockPredicate.CODEC, ExtraCodecs.nonEmptyList(BlockPredicate.CODEC.listOf())).xmap(AdventureModePredicate::new, paramAdventureModePredicate -> paramAdventureModePredicate.predicates);
/*     */     
/*  31 */     STREAM_CODEC = StreamCodec.composite(BlockPredicate.STREAM_CODEC
/*  32 */         .apply(ByteBufCodecs.list()), paramAdventureModePredicate -> paramAdventureModePredicate.predicates, AdventureModePredicate::new);
/*     */   }
/*     */ 
/*     */   
/*  36 */   public static final Component CAN_BREAK_HEADER = (Component)Component.translatable("item.canBreak").withStyle(ChatFormatting.GRAY);
/*  37 */   public static final Component CAN_PLACE_HEADER = (Component)Component.translatable("item.canPlace").withStyle(ChatFormatting.GRAY);
/*  38 */   private static final Component UNKNOWN_USE = (Component)Component.translatable("item.canUse.unknown").withStyle(ChatFormatting.GRAY);
/*     */   
/*     */   private final List<BlockPredicate> predicates;
/*     */   
/*     */   private List<Component> cachedTooltip;
/*     */   
/*     */   private BlockInWorld lastCheckedBlock;
/*     */   
/*     */   private boolean lastResult;
/*     */   private boolean checksBlockEntity;
/*     */   
/*     */   public AdventureModePredicate(List<BlockPredicate> paramList) {
/*  50 */     this.predicates = paramList;
/*     */   }
/*     */   
/*     */   private static boolean areSameBlocks(BlockInWorld paramBlockInWorld1, BlockInWorld paramBlockInWorld2, boolean paramBoolean) {
/*  54 */     if (paramBlockInWorld2 == null || paramBlockInWorld1.getState() != paramBlockInWorld2.getState()) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (!paramBoolean) {
/*  58 */       return true;
/*     */     }
/*  60 */     if (paramBlockInWorld1.getEntity() == null && paramBlockInWorld2.getEntity() == null) {
/*  61 */       return true;
/*     */     }
/*  63 */     if (paramBlockInWorld1.getEntity() == null || paramBlockInWorld2.getEntity() == null) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(LOGGER); 
/*  68 */     try { RegistryAccess registryAccess = paramBlockInWorld1.getLevel().registryAccess();
/*     */       
/*  70 */       CompoundTag compoundTag1 = saveBlockEntity(paramBlockInWorld1.getEntity(), registryAccess, (ProblemReporter)scopedCollector);
/*  71 */       CompoundTag compoundTag2 = saveBlockEntity(paramBlockInWorld2.getEntity(), registryAccess, (ProblemReporter)scopedCollector);
/*     */       
/*  73 */       boolean bool = Objects.equals(compoundTag1, compoundTag2);
/*  74 */       scopedCollector.close(); return bool; }
/*     */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  78 */      } private static CompoundTag saveBlockEntity(BlockEntity paramBlockEntity, RegistryAccess paramRegistryAccess, ProblemReporter paramProblemReporter) { TagValueOutput tagValueOutput = TagValueOutput.createWithContext(paramProblemReporter.forChild(paramBlockEntity.problemPath()), (HolderLookup.Provider)paramRegistryAccess);
/*  79 */     paramBlockEntity.saveWithId((ValueOutput)tagValueOutput);
/*  80 */     return tagValueOutput.buildResult(); }
/*     */ 
/*     */   
/*     */   public boolean test(BlockInWorld paramBlockInWorld) {
/*  84 */     if (areSameBlocks(paramBlockInWorld, this.lastCheckedBlock, this.checksBlockEntity)) {
/*  85 */       return this.lastResult;
/*     */     }
/*     */     
/*  88 */     this.lastCheckedBlock = paramBlockInWorld;
/*  89 */     this.checksBlockEntity = false;
/*     */     
/*  91 */     for (BlockPredicate blockPredicate : this.predicates) {
/*  92 */       if (blockPredicate.matches(paramBlockInWorld)) {
/*  93 */         this.checksBlockEntity |= blockPredicate.requiresNbt();
/*  94 */         this.lastResult = true;
/*  95 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     this.lastResult = false;
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   private List<Component> tooltip() {
/* 104 */     if (this.cachedTooltip == null) {
/* 105 */       this.cachedTooltip = computeTooltip(this.predicates);
/*     */     }
/* 107 */     return this.cachedTooltip;
/*     */   }
/*     */   
/*     */   public void addToTooltip(Consumer<Component> paramConsumer) {
/* 111 */     tooltip().forEach(paramConsumer);
/*     */   }
/*     */   
/*     */   private static List<Component> computeTooltip(List<BlockPredicate> paramList) {
/* 115 */     for (BlockPredicate blockPredicate : paramList) {
/*     */       
/* 117 */       if (blockPredicate.blocks().isEmpty()) {
/* 118 */         return List.of(UNKNOWN_USE);
/*     */       }
/*     */     } 
/* 121 */     return paramList.stream()
/* 122 */       .flatMap(paramBlockPredicate -> ((HolderSet)paramBlockPredicate.blocks().orElseThrow()).stream())
/* 123 */       .distinct()
/* 124 */       .map(paramHolder -> ((Block)paramHolder.value()).getName().withStyle(ChatFormatting.DARK_GRAY))
/* 125 */       .toList();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 130 */     if (this == paramObject) {
/* 131 */       return true;
/*     */     }
/* 133 */     if (paramObject instanceof AdventureModePredicate) { AdventureModePredicate adventureModePredicate = (AdventureModePredicate)paramObject;
/* 134 */       return this.predicates.equals(adventureModePredicate.predicates); }
/*     */     
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 141 */     return this.predicates.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     return "AdventureModePredicate{predicates=" + String.valueOf(this.predicates) + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\AdventureModePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */