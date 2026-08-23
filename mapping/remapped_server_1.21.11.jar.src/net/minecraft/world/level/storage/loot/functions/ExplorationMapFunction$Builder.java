/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<ExplorationMapFunction.Builder>
/*     */ {
/*  93 */   private TagKey<Structure> destination = ExplorationMapFunction.DEFAULT_DESTINATION;
/*  94 */   private Holder<MapDecorationType> mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
/*  95 */   private byte zoom = 2;
/*  96 */   private int searchRadius = 50;
/*     */   
/*     */   private boolean skipKnownStructures = true;
/*     */   
/*     */   protected Builder getThis() {
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDestination(TagKey<Structure> paramTagKey) {
/* 105 */     this.destination = paramTagKey;
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setMapDecoration(Holder<MapDecorationType> paramHolder) {
/* 110 */     this.mapDecoration = paramHolder;
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setZoom(byte paramByte) {
/* 115 */     this.zoom = paramByte;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSearchRadius(int paramInt) {
/* 120 */     this.searchRadius = paramInt;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSkipKnownStructures(boolean paramBoolean) {
/* 125 */     this.skipKnownStructures = paramBoolean;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunction build() {
/* 131 */     return new ExplorationMapFunction(getConditions(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\ExplorationMapFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */