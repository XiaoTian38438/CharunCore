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
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SkullBlockEntity extends BlockEntity {
/*     */   private static final String TAG_PROFILE = "profile";
/*     */   private static final String TAG_NOTE_BLOCK_SOUND = "note_block_sound";
/*     */   private static final String TAG_CUSTOM_NAME = "custom_name";
/*     */   private ResolvableProfile owner;
/*     */   private Identifier noteBlockSound;
/*     */   private int animationTickCount;
/*     */   private boolean isAnimating;
/*     */   private Component customName;
/*     */   
/*     */   public SkullBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  33 */     super(BlockEntityType.SKULL, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  38 */     super.saveAdditional(paramValueOutput);
/*     */     
/*  40 */     paramValueOutput.storeNullable("profile", ResolvableProfile.CODEC, this.owner);
/*  41 */     paramValueOutput.storeNullable("note_block_sound", Identifier.CODEC, this.noteBlockSound);
/*  42 */     paramValueOutput.storeNullable("custom_name", ComponentSerialization.CODEC, this.customName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  47 */     super.loadAdditional(paramValueInput);
/*     */     
/*  49 */     this.owner = paramValueInput.read("profile", ResolvableProfile.CODEC).orElse(null);
/*  50 */     this.noteBlockSound = paramValueInput.read("note_block_sound", Identifier.CODEC).orElse(null);
/*  51 */     this.customName = parseCustomNameSafe(paramValueInput, "custom_name");
/*     */   }
/*     */   
/*     */   public static void animation(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, SkullBlockEntity paramSkullBlockEntity) {
/*  55 */     if (paramBlockState.hasProperty((Property)SkullBlock.POWERED) && ((Boolean)paramBlockState.getValue((Property)SkullBlock.POWERED)).booleanValue()) {
/*  56 */       paramSkullBlockEntity.isAnimating = true;
/*  57 */       paramSkullBlockEntity.animationTickCount++;
/*     */     } else {
/*  59 */       paramSkullBlockEntity.isAnimating = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAnimation(float paramFloat) {
/*  64 */     if (this.isAnimating) {
/*  65 */       return this.animationTickCount + paramFloat;
/*     */     }
/*  67 */     return this.animationTickCount;
/*     */   }
/*     */   
/*     */   public ResolvableProfile getOwnerProfile() {
/*  71 */     return this.owner;
/*     */   }
/*     */   
/*     */   public Identifier getNoteBlockSound() {
/*  75 */     return this.noteBlockSound;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  80 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/*  85 */     return saveCustomOnly(paramProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/*  90 */     super.applyImplicitComponents(paramDataComponentGetter);
/*  91 */     this.owner = (ResolvableProfile)paramDataComponentGetter.get(DataComponents.PROFILE);
/*  92 */     this.noteBlockSound = (Identifier)paramDataComponentGetter.get(DataComponents.NOTE_BLOCK_SOUND);
/*  93 */     this.customName = (Component)paramDataComponentGetter.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/*  98 */     super.collectImplicitComponents(paramBuilder);
/*  99 */     paramBuilder.set(DataComponents.PROFILE, this.owner);
/* 100 */     paramBuilder.set(DataComponents.NOTE_BLOCK_SOUND, this.noteBlockSound);
/* 101 */     paramBuilder.set(DataComponents.CUSTOM_NAME, this.customName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 106 */     super.removeComponentsFromTag(paramValueOutput);
/* 107 */     paramValueOutput.discard("profile");
/* 108 */     paramValueOutput.discard("note_block_sound");
/* 109 */     paramValueOutput.discard("custom_name");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\SkullBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */