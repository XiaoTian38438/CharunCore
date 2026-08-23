/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.Hopper;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class MinecartHopper extends AbstractMinecartContainer implements Hopper {
/*     */   private static final boolean DEFAULT_ENABLED = true;
/*     */   private boolean enabled = true;
/*     */   private boolean consumedItemThisFrame = false;
/*     */   
/*     */   public MinecartHopper(EntityType<? extends MinecartHopper> paramEntityType, Level paramLevel) {
/*  32 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  37 */     return Blocks.HOPPER.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultDisplayOffset() {
/*  42 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  47 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel paramServerLevel, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
/*  52 */     boolean bool = !paramBoolean;
/*     */     
/*  54 */     if (bool != isEnabled()) {
/*  55 */       setEnabled(bool);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  60 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean paramBoolean) {
/*  64 */     this.enabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelX() {
/*  69 */     return getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelY() {
/*  74 */     return getY() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelZ() {
/*  79 */     return getZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGridAligned() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  89 */     this.consumedItemThisFrame = false;
/*  90 */     super.tick();
/*  91 */     tryConsumeItems();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double makeStepAlongTrack(BlockPos paramBlockPos, RailShape paramRailShape, double paramDouble) {
/*  96 */     double d = super.makeStepAlongTrack(paramBlockPos, paramRailShape, paramDouble);
/*  97 */     tryConsumeItems();
/*  98 */     return d;
/*     */   }
/*     */   
/*     */   private void tryConsumeItems() {
/* 102 */     if (!level().isClientSide() && isAlive() && isEnabled() && !this.consumedItemThisFrame && 
/* 103 */       suckInItems()) {
/* 104 */       this.consumedItemThisFrame = true;
/* 105 */       setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean suckInItems() {
/* 111 */     if (HopperBlockEntity.suckInItems(level(), this)) {
/* 112 */       return true;
/*     */     }
/*     */     
/* 115 */     List list = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.25D, 0.0D, 0.25D), EntitySelector.ENTITY_STILL_ALIVE);
/*     */     
/* 117 */     for (ItemEntity itemEntity : list) {
/* 118 */       if (HopperBlockEntity.addItem((Container)this, itemEntity)) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 128 */     return Items.HOPPER_MINECART;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 133 */     return new ItemStack((ItemLike)Items.HOPPER_MINECART);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 138 */     super.addAdditionalSaveData(paramValueOutput);
/* 139 */     paramValueOutput.putBoolean("Enabled", this.enabled);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 144 */     super.readAdditionalSaveData(paramValueInput);
/* 145 */     this.enabled = paramValueInput.getBooleanOr("Enabled", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory) {
/* 150 */     return (AbstractContainerMenu)new HopperMenu(paramInt, paramInventory, (Container)this);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartHopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */