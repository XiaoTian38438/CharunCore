/*     */ package net.minecraft.world.item;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.LinkedHashMultiset;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Multisets;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.MapPostProcessing;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ public class MapItem extends Item {
/*     */   public static final int IMAGE_WIDTH = 128;
/*     */   public static final int IMAGE_HEIGHT = 128;
/*     */   
/*     */   public MapItem(Item.Properties paramProperties) {
/*  40 */     super(paramProperties);
/*     */   }
/*     */   
/*     */   public static ItemStack create(ServerLevel paramServerLevel, int paramInt1, int paramInt2, byte paramByte, boolean paramBoolean1, boolean paramBoolean2) {
/*  44 */     ItemStack itemStack = new ItemStack(Items.FILLED_MAP);
/*  45 */     MapId mapId = createNewSavedData(paramServerLevel, paramInt1, paramInt2, paramByte, paramBoolean1, paramBoolean2, paramServerLevel.dimension());
/*  46 */     itemStack.set(DataComponents.MAP_ID, mapId);
/*  47 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static MapItemSavedData getSavedData(MapId paramMapId, Level paramLevel) {
/*  51 */     return (paramMapId == null) ? null : paramLevel.getMapData(paramMapId);
/*     */   }
/*     */   
/*     */   public static MapItemSavedData getSavedData(ItemStack paramItemStack, Level paramLevel) {
/*  55 */     MapId mapId = (MapId)paramItemStack.get(DataComponents.MAP_ID);
/*  56 */     return getSavedData(mapId, paramLevel);
/*     */   }
/*     */   
/*     */   private static MapId createNewSavedData(ServerLevel paramServerLevel, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, ResourceKey<Level> paramResourceKey) {
/*  60 */     MapItemSavedData mapItemSavedData = MapItemSavedData.createFresh(paramInt1, paramInt2, (byte)paramInt3, paramBoolean1, paramBoolean2, paramResourceKey);
/*  61 */     MapId mapId = paramServerLevel.getFreeMapId();
/*  62 */     paramServerLevel.setMapData(mapId, mapItemSavedData);
/*  63 */     return mapId;
/*     */   }
/*     */   
/*     */   public void update(Level paramLevel, Entity paramEntity, MapItemSavedData paramMapItemSavedData) {
/*  67 */     if (paramLevel.dimension() != paramMapItemSavedData.dimension || !(paramEntity instanceof Player)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  72 */     int i = 1 << paramMapItemSavedData.scale;
/*  73 */     int j = paramMapItemSavedData.centerX;
/*  74 */     int k = paramMapItemSavedData.centerZ;
/*     */ 
/*     */     
/*  77 */     int m = Mth.floor(paramEntity.getX() - j) / i + 64;
/*  78 */     int n = Mth.floor(paramEntity.getZ() - k) / i + 64;
/*  79 */     int i1 = 128 / i;
/*     */     
/*  81 */     if (paramLevel.dimensionType().hasCeiling()) {
/*  82 */       i1 /= 2;
/*     */     }
/*     */     
/*  85 */     MapItemSavedData.HoldingPlayer holdingPlayer = paramMapItemSavedData.getHoldingPlayer((Player)paramEntity);
/*  86 */     holdingPlayer.step++;
/*  87 */     BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
/*  88 */     BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
/*     */     
/*  90 */     boolean bool = false;
/*  91 */     for (int i2 = m - i1 + 1; i2 < m + i1; i2++) {
/*  92 */       if ((i2 & 0xF) == (holdingPlayer.step & 0xF) || bool) {
/*     */ 
/*     */ 
/*     */         
/*  96 */         bool = false;
/*  97 */         double d = 0.0D;
/*  98 */         for (int i3 = n - i1 - 1; i3 < n + i1; i3++) {
/*  99 */           if (i2 >= 0 && i3 >= -1 && i2 < 128 && i3 < 128) {
/*     */ 
/*     */ 
/*     */             
/* 103 */             int i4 = Mth.square(i2 - m) + Mth.square(i3 - n);
/*     */             
/* 105 */             boolean bool1 = (i4 > (i1 - 2) * (i1 - 2)) ? true : false;
/*     */             
/* 107 */             int i5 = (j / i + i2 - 64) * i;
/* 108 */             int i6 = (k / i + i3 - 64) * i;
/*     */             
/* 110 */             LinkedHashMultiset linkedHashMultiset = LinkedHashMultiset.create();
/*     */             
/* 112 */             LevelChunk levelChunk = paramLevel.getChunk(SectionPos.blockToSectionCoord(i5), SectionPos.blockToSectionCoord(i6));
/* 113 */             if (!levelChunk.isEmpty()) {
/*     */               MapColor.Brightness brightness;
/*     */ 
/*     */               
/* 117 */               int i7 = 0;
/*     */               
/* 119 */               double d1 = 0.0D;
/* 120 */               if (paramLevel.dimensionType().hasCeiling()) {
/* 121 */                 int i8 = i5 + i6 * 231871;
/* 122 */                 i8 = i8 * i8 * 31287121 + i8 * 11;
/*     */                 
/* 124 */                 if ((i8 >> 20 & 0x1) == 0) {
/* 125 */                   linkedHashMultiset.add(Blocks.DIRT.defaultBlockState().getMapColor((BlockGetter)paramLevel, BlockPos.ZERO), 10);
/*     */                 } else {
/* 127 */                   linkedHashMultiset.add(Blocks.STONE.defaultBlockState().getMapColor((BlockGetter)paramLevel, BlockPos.ZERO), 100);
/*     */                 } 
/*     */                 
/* 130 */                 d1 = 100.0D;
/*     */               
/*     */               }
/*     */               else {
/*     */ 
/*     */                 
/* 136 */                 for (byte b = 0; b < i; b++) {
/* 137 */                   for (byte b1 = 0; b1 < i; b1++) {
/* 138 */                     BlockState blockState; mutableBlockPos1.set(i5 + b, 0, i6 + b1);
/* 139 */                     int i8 = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, mutableBlockPos1.getX(), mutableBlockPos1.getZ()) + 1;
/*     */                     
/* 141 */                     if (i8 > paramLevel.getMinY()) {
/*     */                       do {
/* 143 */                         i8--;
/* 144 */                         mutableBlockPos1.setY(i8);
/* 145 */                         blockState = levelChunk.getBlockState((BlockPos)mutableBlockPos1);
/* 146 */                       } while (blockState.getMapColor((BlockGetter)paramLevel, (BlockPos)mutableBlockPos1) == MapColor.NONE && i8 > paramLevel.getMinY());
/*     */                       
/* 148 */                       if (i8 > paramLevel.getMinY() && !blockState.getFluidState().isEmpty()) {
/*     */                         BlockState blockState1;
/* 150 */                         int i9 = i8 - 1;
/*     */                         
/* 152 */                         mutableBlockPos2.set((Vec3i)mutableBlockPos1);
/*     */                         do {
/* 154 */                           mutableBlockPos2.setY(i9--);
/* 155 */                           blockState1 = levelChunk.getBlockState((BlockPos)mutableBlockPos2);
/* 156 */                           i7++;
/* 157 */                         } while (i9 > paramLevel.getMinY() && !blockState1.getFluidState().isEmpty());
/*     */                         
/* 159 */                         blockState = getCorrectStateForFluidBlock(paramLevel, blockState, (BlockPos)mutableBlockPos1);
/*     */                       } 
/*     */                     } else {
/* 162 */                       blockState = Blocks.BEDROCK.defaultBlockState();
/*     */                     } 
/*     */                     
/* 165 */                     paramMapItemSavedData.checkBanners((BlockGetter)paramLevel, mutableBlockPos1.getX(), mutableBlockPos1.getZ());
/*     */                     
/* 167 */                     d1 += i8 / (i * i);
/*     */                     
/* 169 */                     linkedHashMultiset.add(blockState.getMapColor((BlockGetter)paramLevel, (BlockPos)mutableBlockPos1));
/*     */                   } 
/*     */                 } 
/*     */               } 
/* 173 */               i7 /= i * i;
/*     */               
/* 175 */               MapColor mapColor = (MapColor)Iterables.getFirst((Iterable)Multisets.copyHighestCountFirst((Multiset)linkedHashMultiset), MapColor.NONE);
/*     */               
/* 177 */               if (mapColor == MapColor.WATER) {
/* 178 */                 double d2 = i7 * 0.1D + (i2 + i3 & 0x1) * 0.2D;
/* 179 */                 if (d2 < 0.5D) {
/* 180 */                   brightness = MapColor.Brightness.HIGH;
/* 181 */                 } else if (d2 > 0.9D) {
/* 182 */                   brightness = MapColor.Brightness.LOW;
/*     */                 } else {
/* 184 */                   brightness = MapColor.Brightness.NORMAL;
/*     */                 } 
/*     */               } else {
/* 187 */                 double d2 = (d1 - d) * 4.0D / (i + 4) + ((i2 + i3 & 0x1) - 0.5D) * 0.4D;
/* 188 */                 if (d2 > 0.6D) {
/* 189 */                   brightness = MapColor.Brightness.HIGH;
/* 190 */                 } else if (d2 < -0.6D) {
/* 191 */                   brightness = MapColor.Brightness.LOW;
/*     */                 } else {
/* 193 */                   brightness = MapColor.Brightness.NORMAL;
/*     */                 } 
/*     */               } 
/*     */               
/* 197 */               d = d1;
/*     */               
/* 199 */               if (i3 >= 0)
/*     */               {
/*     */                 
/* 202 */                 if (i4 < i1 * i1)
/*     */                 {
/*     */                   
/* 205 */                   if (!bool1 || (i2 + i3 & 0x1) != 0)
/*     */                   {
/*     */ 
/*     */                     
/* 209 */                     bool |= paramMapItemSavedData.updateColor(i2, i3, mapColor.getPackedId(brightness)); }  }  } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } private BlockState getCorrectStateForFluidBlock(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos) {
/* 215 */     FluidState fluidState = paramBlockState.getFluidState();
/* 216 */     if (!fluidState.isEmpty() && !paramBlockState.isFaceSturdy((BlockGetter)paramLevel, paramBlockPos, Direction.UP)) {
/* 217 */       return fluidState.createLegacyBlock();
/*     */     }
/*     */     
/* 220 */     return paramBlockState;
/*     */   }
/*     */   
/*     */   private static boolean isBiomeWatery(boolean[] paramArrayOfboolean, int paramInt1, int paramInt2) {
/* 224 */     return paramArrayOfboolean[paramInt2 * 128 + paramInt1];
/*     */   }
/*     */   
/*     */   public static void renderBiomePreviewMap(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 228 */     MapItemSavedData mapItemSavedData = getSavedData(paramItemStack, (Level)paramServerLevel);
/* 229 */     if (mapItemSavedData == null) {
/*     */       return;
/*     */     }
/* 232 */     if (paramServerLevel.dimension() != mapItemSavedData.dimension) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 237 */     int i = 1 << mapItemSavedData.scale;
/* 238 */     int j = mapItemSavedData.centerX;
/* 239 */     int k = mapItemSavedData.centerZ;
/*     */     
/* 241 */     boolean[] arrayOfBoolean = new boolean[16384];
/*     */     
/* 243 */     int m = j / i - 64;
/* 244 */     int n = k / i - 64;
/* 245 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(); byte b;
/* 246 */     for (b = 0; b < ''; b++) {
/* 247 */       for (byte b1 = 0; b1 < ''; b1++) {
/* 248 */         Holder holder = paramServerLevel.getBiome((BlockPos)mutableBlockPos.set((m + b1) * i, 0, (n + b) * i));
/* 249 */         arrayOfBoolean[b * 128 + b1] = holder.is(BiomeTags.WATER_ON_MAP_OUTLINES);
/*     */       } 
/*     */     } 
/* 252 */     for (b = 1; b < 127; b++) {
/* 253 */       for (byte b1 = 1; b1 < 127; b1++) {
/* 254 */         byte b2 = 0;
/* 255 */         for (byte b3 = -1; b3 < 2; b3++) {
/* 256 */           for (byte b4 = -1; b4 < 2; b4++) {
/* 257 */             if ((b3 != 0 || b4 != 0) && isBiomeWatery(arrayOfBoolean, b + b3, b1 + b4)) {
/* 258 */               b2++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 263 */         MapColor.Brightness brightness = MapColor.Brightness.LOWEST;
/* 264 */         MapColor mapColor = MapColor.NONE;
/*     */         
/* 266 */         if (isBiomeWatery(arrayOfBoolean, b, b1)) {
/* 267 */           mapColor = MapColor.COLOR_ORANGE;
/* 268 */           if (b2 > 7 && b1 % 2 == 0) {
/* 269 */             switch ((b + (int)(Mth.sin((b1 + 0.0F)) * 7.0F)) / 8 % 5) { case 0: case 4:
/* 270 */                 brightness = MapColor.Brightness.LOW; break;
/* 271 */               case 1: case 3: brightness = MapColor.Brightness.NORMAL; break;
/* 272 */               case 2: brightness = MapColor.Brightness.HIGH; break; }
/*     */           
/* 274 */           } else if (b2 > 7) {
/* 275 */             mapColor = MapColor.NONE;
/* 276 */           } else if (b2 > 5) {
/* 277 */             brightness = MapColor.Brightness.NORMAL;
/* 278 */           } else if (b2 > 3) {
/* 279 */             brightness = MapColor.Brightness.LOW;
/* 280 */           } else if (b2 > 1) {
/* 281 */             brightness = MapColor.Brightness.LOW;
/*     */           } 
/* 283 */         } else if (b2 > 0) {
/* 284 */           mapColor = MapColor.COLOR_BROWN;
/* 285 */           if (b2 > 3) {
/* 286 */             brightness = MapColor.Brightness.NORMAL;
/*     */           } else {
/* 288 */             brightness = MapColor.Brightness.LOWEST;
/*     */           } 
/*     */         } 
/*     */         
/* 292 */         if (mapColor != MapColor.NONE) {
/* 293 */           mapItemSavedData.setColor(b, b1, mapColor.getPackedId(brightness));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack paramItemStack, ServerLevel paramServerLevel, Entity paramEntity, EquipmentSlot paramEquipmentSlot) {
/* 301 */     MapItemSavedData mapItemSavedData = getSavedData(paramItemStack, (Level)paramServerLevel);
/* 302 */     if (mapItemSavedData == null) {
/*     */       return;
/*     */     }
/*     */     
/* 306 */     if (paramEntity instanceof Player) { Player player = (Player)paramEntity;
/* 307 */       mapItemSavedData.tickCarriedBy(player, paramItemStack); }
/*     */ 
/*     */     
/* 310 */     if (!mapItemSavedData.locked && paramEquipmentSlot != null && paramEquipmentSlot.getType() == EquipmentSlot.Type.HAND) {
/* 311 */       update((Level)paramServerLevel, paramEntity, mapItemSavedData);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCraftedPostProcess(ItemStack paramItemStack, Level paramLevel) {
/* 319 */     MapPostProcessing mapPostProcessing = paramItemStack.<MapPostProcessing>remove(DataComponents.MAP_POST_PROCESSING);
/* 320 */     if (mapPostProcessing == null) {
/*     */       return;
/*     */     }
/* 323 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 324 */       switch (mapPostProcessing) { case LOCK:
/* 325 */           lockMap(paramItemStack, serverLevel); break;
/* 326 */         case SCALE: scaleMap(paramItemStack, serverLevel);
/*     */           break; }
/*     */        }
/*     */   
/*     */   }
/*     */   private static void scaleMap(ItemStack paramItemStack, ServerLevel paramServerLevel) {
/* 332 */     MapItemSavedData mapItemSavedData = getSavedData(paramItemStack, (Level)paramServerLevel);
/*     */     
/* 334 */     if (mapItemSavedData != null) {
/* 335 */       MapId mapId = paramServerLevel.getFreeMapId();
/* 336 */       paramServerLevel.setMapData(mapId, mapItemSavedData.scaled());
/* 337 */       paramItemStack.set(DataComponents.MAP_ID, mapId);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void lockMap(ItemStack paramItemStack, ServerLevel paramServerLevel) {
/* 342 */     MapItemSavedData mapItemSavedData = getSavedData(paramItemStack, (Level)paramServerLevel);
/* 343 */     if (mapItemSavedData != null) {
/* 344 */       MapId mapId = paramServerLevel.getFreeMapId();
/* 345 */       MapItemSavedData mapItemSavedData1 = mapItemSavedData.locked();
/* 346 */       paramServerLevel.setMapData(mapId, mapItemSavedData1);
/* 347 */       paramItemStack.set(DataComponents.MAP_ID, mapId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 353 */     BlockState blockState = paramUseOnContext.getLevel().getBlockState(paramUseOnContext.getClickedPos());
/* 354 */     if (blockState.is(BlockTags.BANNERS)) {
/* 355 */       if (!paramUseOnContext.getLevel().isClientSide()) {
/* 356 */         MapItemSavedData mapItemSavedData = getSavedData(paramUseOnContext.getItemInHand(), paramUseOnContext.getLevel());
/* 357 */         if (mapItemSavedData != null && 
/* 358 */           !mapItemSavedData.toggleBanner((LevelAccessor)paramUseOnContext.getLevel(), paramUseOnContext.getClickedPos())) {
/* 359 */           return (InteractionResult)InteractionResult.FAIL;
/*     */         }
/*     */       } 
/*     */       
/* 363 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 365 */     return super.useOn(paramUseOnContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\MapItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */