/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.JukeboxSongPlayer;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.JukeboxBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.ContainerSingleItem;
/*     */ 
/*     */ public class JukeboxBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
/*     */   public static final String SONG_ITEM_TAG_ID = "RecordItem";
/*  29 */   private ItemStack item = ItemStack.EMPTY; public static final String TICKS_SINCE_SONG_STARTED_TAG_ID = "ticks_since_song_started";
/*  30 */   private final JukeboxSongPlayer jukeboxSongPlayer = new JukeboxSongPlayer(this::onSongChanged, getBlockPos());
/*     */   
/*     */   public JukeboxBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  33 */     super(BlockEntityType.JUKEBOX, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public JukeboxSongPlayer getSongPlayer() {
/*  37 */     return this.jukeboxSongPlayer;
/*     */   }
/*     */   
/*     */   public void onSongChanged() {
/*  41 */     this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
/*  42 */     setChanged();
/*     */   }
/*     */   
/*     */   private void notifyItemChangedInJukebox(boolean paramBoolean) {
/*  46 */     if (this.level == null || this.level.getBlockState(getBlockPos()) != getBlockState()) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     this.level.setBlock(getBlockPos(), (BlockState)getBlockState().setValue((Property)JukeboxBlock.HAS_RECORD, Boolean.valueOf(paramBoolean)), 2);
/*  51 */     this.level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
/*     */   }
/*     */   
/*     */   public void popOutTheItem() {
/*  55 */     if (this.level == null || this.level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     BlockPos blockPos = getBlockPos();
/*  60 */     ItemStack itemStack1 = getTheItem();
/*  61 */     if (itemStack1.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  65 */     removeTheItem();
/*     */     
/*  67 */     Vec3 vec3 = Vec3.atLowerCornerWithOffset((Vec3i)blockPos, 0.5D, 1.01D, 0.5D).offsetRandomXZ(this.level.random, 0.7F);
/*  68 */     ItemStack itemStack2 = itemStack1.copy();
/*     */     
/*  70 */     ItemEntity itemEntity = new ItemEntity(this.level, vec3.x(), vec3.y(), vec3.z(), itemStack2);
/*  71 */     itemEntity.setDefaultPickUpDelay();
/*  72 */     this.level.addFreshEntity((Entity)itemEntity);
/*  73 */     onSongChanged();
/*     */   }
/*     */   
/*     */   public static void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, JukeboxBlockEntity paramJukeboxBlockEntity) {
/*  77 */     paramJukeboxBlockEntity.jukeboxSongPlayer.tick((LevelAccessor)paramLevel, paramBlockState);
/*     */   }
/*     */   
/*     */   public int getComparatorOutput() {
/*  81 */     return ((Integer)JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), this.item).map(Holder::value).map(JukeboxSong::comparatorOutput).orElse(Integer.valueOf(0))).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  86 */     super.loadAdditional(paramValueInput);
/*     */     
/*  88 */     ItemStack itemStack = paramValueInput.read("RecordItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*  89 */     if (!this.item.isEmpty() && !ItemStack.isSameItemSameComponents(itemStack, this.item)) {
/*  90 */       this.jukeboxSongPlayer.stop((LevelAccessor)this.level, getBlockState());
/*     */     }
/*  92 */     this.item = itemStack;
/*     */     
/*  94 */     paramValueInput.getLong("ticks_since_song_started").ifPresent(paramLong -> JukeboxSong.fromStack(paramValueInput.lookup(), this.item).ifPresent(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 101 */     super.saveAdditional(paramValueOutput);
/*     */     
/* 103 */     if (!getTheItem().isEmpty()) {
/* 104 */       paramValueOutput.store("RecordItem", ItemStack.CODEC, getTheItem());
/*     */     }
/*     */     
/* 107 */     if (this.jukeboxSongPlayer.getSong() != null) {
/* 108 */       paramValueOutput.putLong("ticks_since_song_started", this.jukeboxSongPlayer.getTicksSinceSongStarted());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getTheItem() {
/* 114 */     return this.item;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack splitTheItem(int paramInt) {
/* 119 */     ItemStack itemStack = this.item;
/* 120 */     setTheItem(ItemStack.EMPTY);
/* 121 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTheItem(ItemStack paramItemStack) {
/* 126 */     this.item = paramItemStack;
/*     */     
/* 128 */     boolean bool = !this.item.isEmpty() ? true : false;
/* 129 */     Optional<Holder> optional = JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), this.item);
/*     */     
/* 131 */     notifyItemChangedInJukebox(bool);
/* 132 */     if (bool && optional.isPresent()) {
/* 133 */       this.jukeboxSongPlayer.play((LevelAccessor)this.level, optional.get());
/*     */     } else {
/* 135 */       this.jukeboxSongPlayer.stop((LevelAccessor)this.level, getBlockState());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemoved() {
/* 141 */     super.setRemoved();
/* 142 */     this.level.gameEvent((Holder)GameEvent.JUKEBOX_STOP_PLAY, getBlockPos(), GameEvent.Context.of(getBlockState()));
/* 143 */     this.level.levelEvent(1011, getBlockPos(), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/* 148 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getContainerBlockEntity() {
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int paramInt, ItemStack paramItemStack) {
/* 158 */     return (paramItemStack.has(DataComponents.JUKEBOX_PLAYABLE) && getItem(paramInt).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(Container paramContainer, int paramInt, ItemStack paramItemStack) {
/* 163 */     return paramContainer.hasAnyMatching(ItemStack::isEmpty);
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 168 */     popOutTheItem();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setSongItemWithoutPlaying(ItemStack paramItemStack) {
/* 173 */     this.item = paramItemStack;
/* 174 */     JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), paramItemStack).ifPresent(paramHolder -> this.jukeboxSongPlayer.setSongWithoutPlaying(paramHolder, 0L));
/* 175 */     this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
/* 176 */     setChanged();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void tryForcePlaySong() {
/* 181 */     JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), getTheItem()).ifPresent(paramHolder -> this.jukeboxSongPlayer.play((LevelAccessor)this.level, paramHolder));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\JukeboxBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */