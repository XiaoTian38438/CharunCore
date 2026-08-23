/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BrushableBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BrushableBlockEntity extends BlockEntity {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String LOOT_TABLE_TAG = "LootTable";
/*     */   private static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
/*     */   private static final String HIT_DIRECTION_TAG = "hit_direction";
/*     */   private static final String ITEM_TAG = "item";
/*     */   private static final int BRUSH_COOLDOWN_TICKS = 10;
/*     */   private static final int BRUSH_RESET_TICKS = 40;
/*     */   private static final int REQUIRED_BRUSHES_TO_BREAK = 10;
/*     */   private int brushCount;
/*     */   private long brushCountResetsAtTick;
/*     */   private long coolDownEndsAtTick;
/*  54 */   private ItemStack item = ItemStack.EMPTY;
/*     */   private Direction hitDirection;
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*     */   public BrushableBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  60 */     super(BlockEntityType.BRUSHABLE_BLOCK, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   public boolean brush(long paramLong, ServerLevel paramServerLevel, LivingEntity paramLivingEntity, Direction paramDirection, ItemStack paramItemStack) {
/*  64 */     if (this.hitDirection == null) {
/*  65 */       this.hitDirection = paramDirection;
/*     */     }
/*  67 */     this.brushCountResetsAtTick = paramLong + 40L;
/*     */     
/*  69 */     if (paramLong < this.coolDownEndsAtTick) {
/*  70 */       return false;
/*     */     }
/*  72 */     this.coolDownEndsAtTick = paramLong + 10L;
/*     */     
/*  74 */     unpackLootTable(paramServerLevel, paramLivingEntity, paramItemStack);
/*     */     
/*  76 */     int i = getCompletionState();
/*     */     
/*  78 */     if (++this.brushCount >= 10) {
/*  79 */       brushingCompleted(paramServerLevel, paramLivingEntity, paramItemStack);
/*  80 */       return true;
/*     */     } 
/*     */     
/*  83 */     paramServerLevel.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
/*     */     
/*  85 */     int j = getCompletionState();
/*  86 */     if (i != j) {
/*  87 */       BlockState blockState1 = getBlockState();
/*  88 */       BlockState blockState2 = (BlockState)blockState1.setValue((Property)BlockStateProperties.DUSTED, Integer.valueOf(j));
/*  89 */       paramServerLevel.setBlock(getBlockPos(), blockState2, 3);
/*     */     } 
/*     */     
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   private void unpackLootTable(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  96 */     if (this.lootTable == null) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     LootTable lootTable = paramServerLevel.getServer().reloadableRegistries().getLootTable(this.lootTable);
/* 101 */     if (paramLivingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramLivingEntity;
/* 102 */       CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.lootTable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     LootParams lootParams = (new LootParams.Builder(paramServerLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)this.worldPosition)).withLuck(paramLivingEntity.getLuck()).withParameter(LootContextParams.THIS_ENTITY, paramLivingEntity).withParameter(LootContextParams.TOOL, paramItemStack).create(LootContextParamSets.ARCHAEOLOGY);
/*     */     
/* 112 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(lootParams, this.lootTableSeed);
/*     */     
/* 114 */     switch (objectArrayList.size()) { case 0: 
/*     */       case 1:
/*     */       
/*     */       default:
/* 118 */         LOGGER.warn("Expected max 1 loot from loot table {}, but got {}", this.lootTable.identifier(), Integer.valueOf(objectArrayList.size())); break; }
/* 119 */      this.item = (ItemStack)objectArrayList.getFirst();
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.lootTable = null;
/* 124 */     setChanged();
/*     */   }
/*     */   private void brushingCompleted(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*     */     Block block2;
/* 128 */     dropContent(paramServerLevel, paramLivingEntity, paramItemStack);
/* 129 */     BlockState blockState = getBlockState();
/* 130 */     paramServerLevel.levelEvent(3008, getBlockPos(), Block.getId(blockState));
/* 131 */     Block block1 = getBlockState().getBlock();
/*     */ 
/*     */     
/* 134 */     if (block1 instanceof BrushableBlock) { BrushableBlock brushableBlock = (BrushableBlock)block1;
/* 135 */       block2 = brushableBlock.getTurnsInto(); }
/*     */     else
/* 137 */     { block2 = Blocks.AIR; }
/*     */ 
/*     */     
/* 140 */     paramServerLevel.setBlock(this.worldPosition, block2.defaultBlockState(), 3);
/*     */   }
/*     */   
/*     */   private void dropContent(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 144 */     unpackLootTable(paramServerLevel, paramLivingEntity, paramItemStack);
/*     */     
/* 146 */     if (!this.item.isEmpty()) {
/* 147 */       double d1 = EntityType.ITEM.getWidth();
/* 148 */       double d2 = 1.0D - d1;
/* 149 */       double d3 = d1 / 2.0D;
/*     */       
/* 151 */       Direction direction = Objects.<Direction>requireNonNullElse(this.hitDirection, Direction.UP);
/* 152 */       BlockPos blockPos = this.worldPosition.relative(direction, 1);
/*     */       
/* 154 */       double d4 = blockPos.getX() + 0.5D * d2 + d3;
/* 155 */       double d5 = blockPos.getY() + 0.5D + (EntityType.ITEM.getHeight() / 2.0F);
/* 156 */       double d6 = blockPos.getZ() + 0.5D * d2 + d3;
/*     */       
/* 158 */       ItemEntity itemEntity = new ItemEntity((Level)paramServerLevel, d4, d5, d6, this.item.split(paramServerLevel.random.nextInt(21) + 10));
/* 159 */       itemEntity.setDeltaMovement(Vec3.ZERO);
/* 160 */       paramServerLevel.addFreshEntity((Entity)itemEntity);
/*     */       
/* 162 */       this.item = ItemStack.EMPTY;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void checkReset(ServerLevel paramServerLevel) {
/* 167 */     if (this.brushCount != 0 && paramServerLevel.getGameTime() >= this.brushCountResetsAtTick) {
/* 168 */       int i = getCompletionState();
/* 169 */       this.brushCount = Math.max(0, this.brushCount - 2);
/* 170 */       int j = getCompletionState();
/*     */       
/* 172 */       if (i != j) {
/* 173 */         paramServerLevel.setBlock(getBlockPos(), (BlockState)getBlockState().setValue((Property)BlockStateProperties.DUSTED, Integer.valueOf(j)), 3);
/*     */       }
/* 175 */       byte b = 4;
/* 176 */       this.brushCountResetsAtTick = paramServerLevel.getGameTime() + 4L;
/*     */     } 
/*     */     
/* 179 */     if (this.brushCount == 0) {
/* 180 */       this.hitDirection = null;
/* 181 */       this.brushCountResetsAtTick = 0L;
/* 182 */       this.coolDownEndsAtTick = 0L;
/*     */     } else {
/* 184 */       paramServerLevel.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryLoadLootTable(ValueInput paramValueInput) {
/* 189 */     this.lootTable = paramValueInput.read("LootTable", LootTable.KEY_CODEC).orElse(null);
/* 190 */     this.lootTableSeed = paramValueInput.getLongOr("LootTableSeed", 0L);
/* 191 */     return (this.lootTable != null);
/*     */   }
/*     */   
/*     */   private boolean trySaveLootTable(ValueOutput paramValueOutput) {
/* 195 */     if (this.lootTable == null) {
/* 196 */       return false;
/*     */     }
/*     */     
/* 199 */     paramValueOutput.store("LootTable", LootTable.KEY_CODEC, this.lootTable);
/* 200 */     if (this.lootTableSeed != 0L) {
/* 201 */       paramValueOutput.putLong("LootTableSeed", this.lootTableSeed);
/*     */     }
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/* 208 */     CompoundTag compoundTag = super.getUpdateTag(paramProvider);
/* 209 */     compoundTag.storeNullable("hit_direction", Direction.LEGACY_ID_CODEC, this.hitDirection);
/* 210 */     if (!this.item.isEmpty()) {
/* 211 */       RegistryOps registryOps = paramProvider.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 212 */       compoundTag.store("item", ItemStack.CODEC, (DynamicOps)registryOps, this.item);
/*     */     } 
/* 214 */     return compoundTag;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 219 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/* 224 */     super.loadAdditional(paramValueInput);
/*     */     
/* 226 */     if (!tryLoadLootTable(paramValueInput)) {
/* 227 */       this.item = paramValueInput.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*     */     } else {
/* 229 */       this.item = ItemStack.EMPTY;
/*     */     } 
/*     */     
/* 232 */     this.hitDirection = paramValueInput.read("hit_direction", Direction.LEGACY_ID_CODEC).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 237 */     super.saveAdditional(paramValueOutput);
/*     */     
/* 239 */     if (!trySaveLootTable(paramValueOutput) && !this.item.isEmpty()) {
/* 240 */       paramValueOutput.store("item", ItemStack.CODEC, this.item);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> paramResourceKey, long paramLong) {
/* 245 */     this.lootTable = paramResourceKey;
/* 246 */     this.lootTableSeed = paramLong;
/*     */   }
/*     */   
/*     */   private int getCompletionState() {
/* 250 */     if (this.brushCount == 0) {
/* 251 */       return 0;
/*     */     }
/* 253 */     if (this.brushCount < 3) {
/* 254 */       return 1;
/*     */     }
/* 256 */     if (this.brushCount < 6) {
/* 257 */       return 2;
/*     */     }
/* 259 */     return 3;
/*     */   }
/*     */   
/*     */   public Direction getHitDirection() {
/* 263 */     return this.hitDirection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/* 272 */     return this.item;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BrushableBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */