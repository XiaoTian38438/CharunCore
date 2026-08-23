/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ExplorationMapFunction extends LootItemConditionalFunction {
/*  30 */   public static final TagKey<Structure> DEFAULT_DESTINATION = StructureTags.ON_TREASURE_MAPS;
/*  31 */   public static final Holder<MapDecorationType> DEFAULT_DECORATION = MapDecorationTypes.WOODLAND_MANSION; public static final byte DEFAULT_ZOOM = 2; public static final int DEFAULT_SEARCH_RADIUS = 50;
/*     */   public static final boolean DEFAULT_SKIP_EXISTING = true;
/*     */   public static final MapCodec<ExplorationMapFunction> CODEC;
/*     */   
/*     */   static {
/*  36 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)TagKey.codec(Registries.STRUCTURE).optionalFieldOf("destination", DEFAULT_DESTINATION).forGetter(()), (App)MapDecorationType.CODEC.optionalFieldOf("decoration", DEFAULT_DECORATION).forGetter(()), (App)Codec.BYTE.optionalFieldOf("zoom", Byte.valueOf((byte)2)).forGetter(()), (App)Codec.INT.optionalFieldOf("search_radius", Integer.valueOf(50)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("skip_existing_chunks", Boolean.valueOf(true)).forGetter(()))).apply((Applicative)paramInstance, ExplorationMapFunction::new));
/*     */   }
/*     */ 
/*     */   
/*     */   private final TagKey<Structure> destination;
/*     */   
/*     */   private final Holder<MapDecorationType> mapDecoration;
/*     */   
/*     */   private final byte zoom;
/*     */   
/*     */   private final int searchRadius;
/*     */   
/*     */   private final boolean skipKnownStructures;
/*     */ 
/*     */   
/*     */   ExplorationMapFunction(List<LootItemCondition> paramList, TagKey<Structure> paramTagKey, Holder<MapDecorationType> paramHolder, byte paramByte, int paramInt, boolean paramBoolean) {
/*  52 */     super(paramList);
/*  53 */     this.destination = paramTagKey;
/*  54 */     this.mapDecoration = paramHolder;
/*  55 */     this.zoom = paramByte;
/*  56 */     this.searchRadius = paramInt;
/*  57 */     this.skipKnownStructures = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<ExplorationMapFunction> getType() {
/*  62 */     return LootItemFunctions.EXPLORATION_MAP;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  67 */     return Set.of(LootContextParams.ORIGIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/*  72 */     if (!paramItemStack.is(Items.MAP)) {
/*  73 */       return paramItemStack;
/*     */     }
/*     */     
/*  76 */     Vec3 vec3 = (Vec3)paramLootContext.getOptionalParameter(LootContextParams.ORIGIN);
/*  77 */     if (vec3 != null) {
/*  78 */       ServerLevel serverLevel = paramLootContext.getLevel();
/*     */       
/*  80 */       BlockPos blockPos = serverLevel.findNearestMapStructure(this.destination, BlockPos.containing((Position)vec3), this.searchRadius, this.skipKnownStructures);
/*  81 */       if (blockPos != null) {
/*  82 */         ItemStack itemStack = MapItem.create(serverLevel, blockPos.getX(), blockPos.getZ(), this.zoom, true, true);
/*  83 */         MapItem.renderBiomePreviewMap(serverLevel, itemStack);
/*  84 */         MapItemSavedData.addTargetDecoration(itemStack, blockPos, "+", this.mapDecoration);
/*  85 */         return itemStack;
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return paramItemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  93 */     private TagKey<Structure> destination = ExplorationMapFunction.DEFAULT_DESTINATION;
/*  94 */     private Holder<MapDecorationType> mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
/*  95 */     private byte zoom = 2;
/*  96 */     private int searchRadius = 50;
/*     */     
/*     */     private boolean skipKnownStructures = true;
/*     */     
/*     */     protected Builder getThis() {
/* 101 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDestination(TagKey<Structure> param1TagKey) {
/* 105 */       this.destination = param1TagKey;
/* 106 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMapDecoration(Holder<MapDecorationType> param1Holder) {
/* 110 */       this.mapDecoration = param1Holder;
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setZoom(byte param1Byte) {
/* 115 */       this.zoom = param1Byte;
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSearchRadius(int param1Int) {
/* 120 */       this.searchRadius = param1Int;
/* 121 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSkipKnownStructures(boolean param1Boolean) {
/* 125 */       this.skipKnownStructures = param1Boolean;
/* 126 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 131 */       return new ExplorationMapFunction(getConditions(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder makeExplorationMap() {
/* 136 */     return new Builder();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ExplorationMapFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */