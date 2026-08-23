/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*     */ import net.minecraft.world.level.block.BannerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class BannerBlockEntity extends BlockEntity implements Nameable {
/*     */   public static final int MAX_PATTERNS = 6;
/*     */   private static final String TAG_PATTERNS = "patterns";
/*  26 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("block.minecraft.banner");
/*     */   
/*     */   private Component name;
/*     */   private final DyeColor baseColor;
/*  30 */   private BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
/*     */   
/*     */   public BannerBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  33 */     this(paramBlockPos, paramBlockState, ((AbstractBannerBlock)paramBlockState.getBlock()).getColor());
/*     */   }
/*     */   
/*     */   public BannerBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState, DyeColor paramDyeColor) {
/*  37 */     super(BlockEntityType.BANNER, paramBlockPos, paramBlockState);
/*  38 */     this.baseColor = paramDyeColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/*  43 */     if (this.name != null) {
/*  44 */       return this.name;
/*     */     }
/*  46 */     return DEFAULT_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getCustomName() {
/*  51 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  56 */     super.saveAdditional(paramValueOutput);
/*     */     
/*  58 */     if (!this.patterns.equals(BannerPatternLayers.EMPTY)) {
/*  59 */       paramValueOutput.store("patterns", BannerPatternLayers.CODEC, this.patterns);
/*     */     }
/*     */     
/*  62 */     paramValueOutput.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  67 */     super.loadAdditional(paramValueInput);
/*     */     
/*  69 */     this.name = parseCustomNameSafe(paramValueInput, "CustomName");
/*     */     
/*  71 */     this.patterns = paramValueInput.read("patterns", BannerPatternLayers.CODEC).orElse(BannerPatternLayers.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  76 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/*  82 */     return saveWithoutMetadata(paramProvider);
/*     */   }
/*     */   
/*     */   public BannerPatternLayers getPatterns() {
/*  86 */     return this.patterns;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  90 */     ItemStack itemStack = new ItemStack((ItemLike)BannerBlock.byColor(this.baseColor));
/*  91 */     itemStack.applyComponents(collectComponents());
/*  92 */     return itemStack;
/*     */   }
/*     */   
/*     */   public DyeColor getBaseColor() {
/*  96 */     return this.baseColor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 101 */     super.applyImplicitComponents(paramDataComponentGetter);
/* 102 */     this.patterns = (BannerPatternLayers)paramDataComponentGetter.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 103 */     this.name = (Component)paramDataComponentGetter.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 108 */     super.collectImplicitComponents(paramBuilder);
/* 109 */     paramBuilder.set(DataComponents.BANNER_PATTERNS, this.patterns);
/* 110 */     paramBuilder.set(DataComponents.CUSTOM_NAME, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 115 */     paramValueOutput.discard("patterns");
/* 116 */     paramValueOutput.discard("CustomName");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BannerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */