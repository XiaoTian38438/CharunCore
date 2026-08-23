/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.ShelfBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ShelfBlockEntity extends BlockEntity implements ItemOwner, ListBackedContainer {
/*     */   public static final int MAX_ITEMS = 3;
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String ALIGN_ITEMS_TO_BOTTOM_TAG = "align_items_to_bottom";
/*  38 */   private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   private boolean alignItemsToBottom;
/*     */   
/*     */   public ShelfBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  42 */     super(BlockEntityType.SHELF, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  47 */     super.loadAdditional(paramValueInput);
/*  48 */     this.items.clear();
/*  49 */     ContainerHelper.loadAllItems(paramValueInput, this.items);
/*  50 */     this.alignItemsToBottom = paramValueInput.getBooleanOr("align_items_to_bottom", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  55 */     super.saveAdditional(paramValueOutput);
/*  56 */     ContainerHelper.saveAllItems(paramValueOutput, this.items, true);
/*  57 */     paramValueOutput.putBoolean("align_items_to_bottom", this.alignItemsToBottom);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  62 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/*  67 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/*  68 */     try { TagValueOutput tagValueOutput = TagValueOutput.createWithContext((ProblemReporter)scopedCollector, paramProvider);
/*  69 */       ContainerHelper.saveAllItems((ValueOutput)tagValueOutput, this.items, true);
/*  70 */       tagValueOutput.putBoolean("align_items_to_bottom", this.alignItemsToBottom);
/*  71 */       CompoundTag compoundTag = tagValueOutput.buildResult();
/*  72 */       scopedCollector.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*     */       catch (Throwable throwable1)
/*     */       { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/*  77 */      } public NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  82 */     return Container.stillValidBlockEntity(this, paramPlayer);
/*     */   }
/*     */   
/*     */   public ItemStack swapItemNoUpdate(int paramInt, ItemStack paramItemStack) {
/*  86 */     ItemStack itemStack = removeItemNoUpdate(paramInt);
/*  87 */     setItemNoUpdate(paramInt, paramItemStack);
/*  88 */     return itemStack;
/*     */   }
/*     */   
/*     */   public void setChanged(Holder.Reference<GameEvent> paramReference) {
/*  92 */     super.setChanged();
/*  93 */     if (this.level != null) {
/*  94 */       if (paramReference != null) {
/*  95 */         this.level.gameEvent((Holder)paramReference, this.worldPosition, GameEvent.Context.of(getBlockState()));
/*     */       }
/*  97 */       getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 103 */     setChanged(GameEvent.BLOCK_ACTIVATE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 108 */     super.applyImplicitComponents(paramDataComponentGetter);
/* 109 */     ((ItemContainerContents)paramDataComponentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 114 */     super.collectImplicitComponents(paramBuilder);
/* 115 */     paramBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems((List)this.items));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 120 */     paramValueOutput.discard("Items");
/*     */   }
/*     */ 
/*     */   
/*     */   public Level level() {
/* 125 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 position() {
/* 130 */     return getBlockPos().getCenter();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVisualRotationYInDegrees() {
/* 135 */     return ((Direction)getBlockState().getValue((Property)ShelfBlock.FACING)).getOpposite().toYRot();
/*     */   }
/*     */   
/*     */   public boolean getAlignItemsToBottom() {
/* 139 */     return this.alignItemsToBottom;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\ShelfBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */