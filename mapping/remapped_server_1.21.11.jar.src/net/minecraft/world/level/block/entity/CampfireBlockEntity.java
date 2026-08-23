/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.crafting.CampfireCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CampfireBlockEntity extends BlockEntity implements Clearable {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int BURN_COOL_SPEED = 2;
/*     */   private static final int NUM_SLOTS = 4;
/*  48 */   private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
/*  49 */   private final int[] cookingProgress = new int[4];
/*  50 */   private final int[] cookingTime = new int[4];
/*     */   
/*     */   public CampfireBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  53 */     super(BlockEntityType.CAMPFIRE, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public static void cookTick(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, CampfireBlockEntity paramCampfireBlockEntity, RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> paramCachedCheck) {
/*  57 */     boolean bool = false;
/*  58 */     for (byte b = 0; b < paramCampfireBlockEntity.items.size(); b++) {
/*  59 */       ItemStack itemStack = (ItemStack)paramCampfireBlockEntity.items.get(b);
/*  60 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/*  64 */         bool = true;
/*  65 */         paramCampfireBlockEntity.cookingProgress[b] = paramCampfireBlockEntity.cookingProgress[b] + 1;
/*  66 */         if (paramCampfireBlockEntity.cookingProgress[b] >= paramCampfireBlockEntity.cookingTime[b]) {
/*  67 */           SingleRecipeInput singleRecipeInput = new SingleRecipeInput(itemStack);
/*     */           
/*  69 */           ItemStack itemStack1 = paramCachedCheck.getRecipeFor((RecipeInput)singleRecipeInput, paramServerLevel).map(paramRecipeHolder -> ((CampfireCookingRecipe)paramRecipeHolder.value()).assemble(paramSingleRecipeInput, (HolderLookup.Provider)paramServerLevel.registryAccess())).orElse(itemStack);
/*  70 */           if (itemStack1.isItemEnabled(paramServerLevel.enabledFeatures())) {
/*  71 */             Containers.dropItemStack((Level)paramServerLevel, paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), itemStack1);
/*  72 */             paramCampfireBlockEntity.items.set(b, ItemStack.EMPTY);
/*  73 */             paramServerLevel.sendBlockUpdated(paramBlockPos, paramBlockState, paramBlockState, 3);
/*  74 */             paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  79 */     if (bool) {
/*  80 */       setChanged((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void cooldownTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, CampfireBlockEntity paramCampfireBlockEntity) {
/*  85 */     boolean bool = false;
/*     */     
/*  87 */     for (byte b = 0; b < paramCampfireBlockEntity.items.size(); b++) {
/*  88 */       if (paramCampfireBlockEntity.cookingProgress[b] > 0) {
/*  89 */         bool = true;
/*  90 */         paramCampfireBlockEntity.cookingProgress[b] = Mth.clamp(paramCampfireBlockEntity.cookingProgress[b] - 2, 0, paramCampfireBlockEntity.cookingTime[b]);
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (bool) {
/*  95 */       setChanged(paramLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void particleTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, CampfireBlockEntity paramCampfireBlockEntity) {
/* 100 */     RandomSource randomSource = paramLevel.random;
/*     */     
/* 102 */     if (randomSource.nextFloat() < 0.11F) {
/* 103 */       for (byte b1 = 0; b1 < randomSource.nextInt(2) + 2; b1++) {
/* 104 */         CampfireBlock.makeParticles(paramLevel, paramBlockPos, ((Boolean)paramBlockState.getValue((Property)CampfireBlock.SIGNAL_FIRE)).booleanValue(), false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 109 */     int i = ((Direction)paramBlockState.getValue((Property)CampfireBlock.FACING)).get2DDataValue();
/* 110 */     for (byte b = 0; b < paramCampfireBlockEntity.items.size(); b++) {
/* 111 */       if (!((ItemStack)paramCampfireBlockEntity.items.get(b)).isEmpty() && randomSource.nextFloat() < 0.2F) {
/* 112 */         Direction direction = Direction.from2DDataValue(Math.floorMod(b + i, 4));
/* 113 */         float f = 0.3125F;
/*     */         
/* 115 */         double d1 = paramBlockPos.getX() + 0.5D - (direction.getStepX() * 0.3125F) + (direction.getClockWise().getStepX() * 0.3125F);
/* 116 */         double d2 = paramBlockPos.getY() + 0.5D;
/* 117 */         double d3 = paramBlockPos.getZ() + 0.5D - (direction.getStepZ() * 0.3125F) + (direction.getClockWise().getStepZ() * 0.3125F);
/*     */         
/* 119 */         for (byte b1 = 0; b1 < 4; b1++) {
/* 120 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 5.0E-4D, 0.0D);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 127 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/* 132 */     super.loadAdditional(paramValueInput);
/*     */     
/* 134 */     this.items.clear();
/* 135 */     ContainerHelper.loadAllItems(paramValueInput, this.items);
/*     */     
/* 137 */     paramValueInput.getIntArray("CookingTimes").ifPresentOrElse(paramArrayOfint -> System.arraycopy(paramArrayOfint, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, paramArrayOfint.length)), () -> Arrays.fill(this.cookingProgress, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     paramValueInput.getIntArray("CookingTotalTimes").ifPresentOrElse(paramArrayOfint -> System.arraycopy(paramArrayOfint, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, paramArrayOfint.length)), () -> Arrays.fill(this.cookingTime, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 150 */     super.saveAdditional(paramValueOutput);
/*     */     
/* 152 */     ContainerHelper.saveAllItems(paramValueOutput, this.items, true);
/*     */     
/* 154 */     paramValueOutput.putIntArray("CookingTimes", this.cookingProgress);
/* 155 */     paramValueOutput.putIntArray("CookingTotalTimes", this.cookingTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 160 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/* 165 */     ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 166 */     try { TagValueOutput tagValueOutput = TagValueOutput.createWithContext((ProblemReporter)scopedCollector, paramProvider);
/* 167 */       ContainerHelper.saveAllItems((ValueOutput)tagValueOutput, this.items, true);
/* 168 */       CompoundTag compoundTag = tagValueOutput.buildResult();
/* 169 */       scopedCollector.close(); return compoundTag; }
/*     */     catch (Throwable throwable) { try { scopedCollector.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 173 */      } public boolean placeFood(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) { for (byte b = 0; b < this.items.size(); b++) {
/* 174 */       ItemStack itemStack = (ItemStack)this.items.get(b);
/* 175 */       if (itemStack.isEmpty()) {
/* 176 */         Optional optional = paramServerLevel.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, (RecipeInput)new SingleRecipeInput(paramItemStack), (Level)paramServerLevel);
/* 177 */         if (optional.isEmpty())
/*     */         {
/* 179 */           return false;
/*     */         }
/*     */         
/* 182 */         this.cookingTime[b] = ((CampfireCookingRecipe)((RecipeHolder)optional.get()).value()).cookingTime();
/* 183 */         this.cookingProgress[b] = 0;
/*     */         
/* 185 */         this.items.set(b, paramItemStack.consumeAndReturn(1, paramLivingEntity));
/* 186 */         paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of((Entity)paramLivingEntity, getBlockState()));
/*     */         
/* 188 */         markUpdated();
/*     */         
/* 190 */         return true;
/*     */       } 
/*     */     } 
/* 193 */     return false; }
/*     */ 
/*     */   
/*     */   private void markUpdated() {
/* 197 */     setChanged();
/* 198 */     getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 203 */     this.items.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 208 */     if (this.level != null) {
/* 209 */       Containers.dropContents(this.level, paramBlockPos, getItems());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 215 */     super.applyImplicitComponents(paramDataComponentGetter);
/* 216 */     ((ItemContainerContents)paramDataComponentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(getItems());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 221 */     super.collectImplicitComponents(paramBuilder);
/* 222 */     paramBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems((List)getItems()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 227 */     paramValueOutput.discard("Items");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\CampfireBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */