/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LootCommand
/*     */ {
/*     */   private static final DynamicCommandExceptionType ERROR_NO_HELD_ITEMS;
/*     */   private static final DynamicCommandExceptionType ERROR_NO_ENTITY_LOOT_TABLE;
/*     */   private static final DynamicCommandExceptionType ERROR_NO_BLOCK_LOOT_TABLE;
/*     */   
/*     */   static {
/*  60 */     ERROR_NO_HELD_ITEMS = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.drop.no_held_items", new Object[] { paramObject }));
/*  61 */     ERROR_NO_ENTITY_LOOT_TABLE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.drop.no_loot_table.entity", new Object[] { paramObject }));
/*  62 */     ERROR_NO_BLOCK_LOOT_TABLE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.drop.no_loot_table.block", new Object[] { paramObject }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  65 */     paramCommandDispatcher.register(
/*  66 */         addTargets(
/*  67 */           (LiteralArgumentBuilder)Commands.literal("loot")
/*  68 */           .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)), (paramArgumentBuilder, paramDropConsumer) -> paramArgumentBuilder.then(Commands.literal("fish").then(Commands.argument("loot_table", (ArgumentType)ResourceOrIdArgument.lootTable(paramCommandBuildContext)).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()).executes(())).then(Commands.argument("tool", (ArgumentType)ItemArgument.item(paramCommandBuildContext)).executes(()))).then(Commands.literal("mainhand").executes(()))).then(Commands.literal("offhand").executes(()))))).then(Commands.literal("loot").then(Commands.argument("loot_table", (ArgumentType)ResourceOrIdArgument.lootTable(paramCommandBuildContext)).executes(()))).then(Commands.literal("kill").then(Commands.argument("target", (ArgumentType)EntityArgument.entity()).executes(()))).then(Commands.literal("mine").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos()).executes(())).then(Commands.argument("tool", (ArgumentType)ItemArgument.item(paramCommandBuildContext)).executes(()))).then(Commands.literal("mainhand").executes(()))).then(Commands.literal("offhand").executes(()))))));
/*     */   }
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
/*     */   private static <T extends ArgumentBuilder<CommandSourceStack, T>> T addTargets(T paramT, TailProvider paramTailProvider) {
/* 146 */     return (T)paramT
/* 147 */       .then((
/* 148 */         (LiteralArgumentBuilder)Commands.literal("replace")
/* 149 */         .then(Commands.literal("entity")
/* 150 */           .then(
/* 151 */             Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/* 152 */             .then(paramTailProvider
/* 153 */               .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("slot", (ArgumentType)SlotArgument.slot()), (paramCommandContext, paramList, paramCallback) -> entityReplace(EntityArgument.getEntities(paramCommandContext, "entities"), SlotArgument.getSlot(paramCommandContext, "slot"), paramList.size(), paramList, paramCallback))
/*     */ 
/*     */               
/* 156 */               .then(paramTailProvider
/* 157 */                 .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0)), (paramCommandContext, paramList, paramCallback) -> entityReplace(EntityArgument.getEntities(paramCommandContext, "entities"), SlotArgument.getSlot(paramCommandContext, "slot"), IntegerArgumentType.getInteger(paramCommandContext, "count"), paramList, paramCallback)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 164 */         .then(Commands.literal("block")
/* 165 */           .then(
/* 166 */             Commands.argument("targetPos", (ArgumentType)BlockPosArgument.blockPos())
/* 167 */             .then(paramTailProvider
/* 168 */               .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("slot", (ArgumentType)SlotArgument.slot()), (paramCommandContext, paramList, paramCallback) -> blockReplace((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "targetPos"), SlotArgument.getSlot(paramCommandContext, "slot"), paramList.size(), paramList, paramCallback))
/*     */ 
/*     */               
/* 171 */               .then(paramTailProvider
/* 172 */                 .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0)), (paramCommandContext, paramList, paramCallback) -> blockReplace((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "targetPos"), IntegerArgumentType.getInteger(paramCommandContext, "slot"), IntegerArgumentType.getInteger(paramCommandContext, "count"), paramList, paramCallback)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       .then(
/* 181 */         Commands.literal("insert")
/* 182 */         .then(paramTailProvider
/* 183 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targetPos", (ArgumentType)BlockPosArgument.blockPos()), (paramCommandContext, paramList, paramCallback) -> blockDistribute((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "targetPos"), paramList, paramCallback))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       .then(
/* 189 */         Commands.literal("give")
/* 190 */         .then(paramTailProvider
/* 191 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("players", (ArgumentType)EntityArgument.players()), (paramCommandContext, paramList, paramCallback) -> playerGive(EntityArgument.getPlayers(paramCommandContext, "players"), paramList, paramCallback))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       .then(
/* 197 */         Commands.literal("spawn")
/* 198 */         .then(paramTailProvider
/* 199 */           .construct((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targetPos", (ArgumentType)Vec3Argument.vec3()), (paramCommandContext, paramList, paramCallback) -> dropInWorld((CommandSourceStack)paramCommandContext.getSource(), Vec3Argument.getVec3(paramCommandContext, "targetPos"), paramList, paramCallback))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Container getContainer(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos) throws CommandSyntaxException {
/* 207 */     BlockEntity blockEntity = paramCommandSourceStack.getLevel().getBlockEntity(paramBlockPos);
/* 208 */     if (!(blockEntity instanceof Container)) {
/* 209 */       throw ItemCommands.ERROR_TARGET_NOT_A_CONTAINER.create(Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()));
/*     */     }
/*     */     
/* 212 */     return (Container)blockEntity;
/*     */   }
/*     */   
/*     */   private static int blockDistribute(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, List<ItemStack> paramList, Callback paramCallback) throws CommandSyntaxException {
/* 216 */     Container container = getContainer(paramCommandSourceStack, paramBlockPos);
/*     */     
/* 218 */     ArrayList<ItemStack> arrayList = Lists.newArrayListWithCapacity(paramList.size());
/* 219 */     for (ItemStack itemStack : paramList) {
/* 220 */       if (distributeToContainer(container, itemStack.copy())) {
/* 221 */         container.setChanged();
/* 222 */         arrayList.add(itemStack);
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     paramCallback.accept(arrayList);
/* 227 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static boolean distributeToContainer(Container paramContainer, ItemStack paramItemStack) {
/* 231 */     boolean bool = false;
/*     */     
/* 233 */     for (byte b = 0; b < paramContainer.getContainerSize() && !paramItemStack.isEmpty(); b++) {
/* 234 */       ItemStack itemStack = paramContainer.getItem(b);
/*     */       
/* 236 */       if (paramContainer.canPlaceItem(b, paramItemStack)) {
/* 237 */         if (itemStack.isEmpty()) {
/* 238 */           paramContainer.setItem(b, paramItemStack);
/* 239 */           bool = true; break;
/*     */         } 
/* 241 */         if (canMergeItems(itemStack, paramItemStack)) {
/* 242 */           int i = paramItemStack.getMaxStackSize() - itemStack.getCount();
/* 243 */           int j = Math.min(paramItemStack.getCount(), i);
/*     */           
/* 245 */           paramItemStack.shrink(j);
/* 246 */           itemStack.grow(j);
/* 247 */           bool = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 252 */     return bool;
/*     */   }
/*     */   
/*     */   private static int blockReplace(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, int paramInt1, int paramInt2, List<ItemStack> paramList, Callback paramCallback) throws CommandSyntaxException {
/* 256 */     Container container = getContainer(paramCommandSourceStack, paramBlockPos);
/*     */     
/* 258 */     int i = container.getContainerSize();
/* 259 */     if (paramInt1 < 0 || paramInt1 >= i) {
/* 260 */       throw ItemCommands.ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(paramInt1));
/*     */     }
/*     */     
/* 263 */     ArrayList<ItemStack> arrayList = Lists.newArrayListWithCapacity(paramList.size());
/*     */     
/* 265 */     for (byte b = 0; b < paramInt2; b++) {
/* 266 */       int j = paramInt1 + b;
/* 267 */       ItemStack itemStack = (b < paramList.size()) ? paramList.get(b) : ItemStack.EMPTY;
/*     */       
/* 269 */       if (container.canPlaceItem(j, itemStack)) {
/* 270 */         container.setItem(j, itemStack);
/* 271 */         arrayList.add(itemStack);
/*     */       } 
/*     */     } 
/*     */     
/* 275 */     paramCallback.accept(arrayList);
/* 276 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static boolean canMergeItems(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 280 */     return (paramItemStack1.getCount() <= paramItemStack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(paramItemStack1, paramItemStack2));
/*     */   }
/*     */   
/*     */   private static int playerGive(Collection<ServerPlayer> paramCollection, List<ItemStack> paramList, Callback paramCallback) throws CommandSyntaxException {
/* 284 */     ArrayList<ItemStack> arrayList = Lists.newArrayListWithCapacity(paramList.size());
/* 285 */     for (ItemStack itemStack : paramList) {
/* 286 */       for (ServerPlayer serverPlayer : paramCollection) {
/* 287 */         if (serverPlayer.getInventory().add(itemStack.copy())) {
/* 288 */           arrayList.add(itemStack);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 293 */     paramCallback.accept(arrayList);
/* 294 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static void setSlots(Entity paramEntity, List<ItemStack> paramList1, int paramInt1, int paramInt2, List<ItemStack> paramList2) {
/* 298 */     for (byte b = 0; b < paramInt2; b++) {
/* 299 */       ItemStack itemStack = (b < paramList1.size()) ? paramList1.get(b) : ItemStack.EMPTY;
/* 300 */       SlotAccess slotAccess = paramEntity.getSlot(paramInt1 + b);
/* 301 */       if (slotAccess != null && slotAccess.set(itemStack.copy())) {
/* 302 */         paramList2.add(itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int entityReplace(Collection<? extends Entity> paramCollection, int paramInt1, int paramInt2, List<ItemStack> paramList, Callback paramCallback) throws CommandSyntaxException {
/* 308 */     ArrayList<ItemStack> arrayList = Lists.newArrayListWithCapacity(paramList.size());
/*     */     
/* 310 */     for (Entity entity : paramCollection) {
/* 311 */       if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 312 */         setSlots(entity, paramList, paramInt1, paramInt2, arrayList);
/* 313 */         serverPlayer.containerMenu.broadcastChanges(); continue; }
/*     */       
/* 315 */       setSlots(entity, paramList, paramInt1, paramInt2, arrayList);
/*     */     } 
/*     */ 
/*     */     
/* 319 */     paramCallback.accept(arrayList);
/* 320 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static int dropInWorld(CommandSourceStack paramCommandSourceStack, Vec3 paramVec3, List<ItemStack> paramList, Callback paramCallback) throws CommandSyntaxException {
/* 324 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 325 */     paramList.forEach(paramItemStack -> {
/*     */           ItemEntity itemEntity = new ItemEntity((Level)paramServerLevel, paramVec3.x, paramVec3.y, paramVec3.z, paramItemStack.copy());
/*     */           
/*     */           itemEntity.setDefaultPickUpDelay();
/*     */           paramServerLevel.addFreshEntity((Entity)itemEntity);
/*     */         });
/* 331 */     paramCallback.accept(paramList);
/* 332 */     return paramList.size();
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack paramCommandSourceStack, List<ItemStack> paramList) {
/* 336 */     if (paramList.size() == 1) {
/* 337 */       ItemStack itemStack = paramList.get(0);
/* 338 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.drop.success.single", new Object[] { Integer.valueOf(paramItemStack.getCount()), paramItemStack.getDisplayName() }), false);
/*     */     } else {
/* 340 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.drop.success.multiple", new Object[] { Integer.valueOf(paramList.size()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack paramCommandSourceStack, List<ItemStack> paramList, ResourceKey<LootTable> paramResourceKey) {
/* 345 */     if (paramList.size() == 1) {
/* 346 */       ItemStack itemStack = paramList.get(0);
/* 347 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.drop.success.single_with_table", new Object[] { Integer.valueOf(paramItemStack.getCount()), paramItemStack.getDisplayName(), Component.translationArg(paramResourceKey.identifier()) }), false);
/*     */     } else {
/* 349 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.drop.success.multiple_with_table", new Object[] { Integer.valueOf(paramList.size()), Component.translationArg(paramResourceKey.identifier()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ItemStack getSourceHandItem(CommandSourceStack paramCommandSourceStack, EquipmentSlot paramEquipmentSlot) throws CommandSyntaxException {
/* 354 */     Entity entity = paramCommandSourceStack.getEntityOrException();
/* 355 */     if (entity instanceof LivingEntity) {
/* 356 */       return ((LivingEntity)entity).getItemBySlot(paramEquipmentSlot);
/*     */     }
/* 358 */     throw ERROR_NO_HELD_ITEMS.create(entity.getDisplayName());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int dropBlockLoot(CommandContext<CommandSourceStack> paramCommandContext, BlockPos paramBlockPos, ItemStack paramItemStack, DropConsumer paramDropConsumer) throws CommandSyntaxException {
/* 363 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 364 */     ServerLevel serverLevel = commandSourceStack.getLevel();
/* 365 */     BlockState blockState = serverLevel.getBlockState(paramBlockPos);
/* 366 */     BlockEntity blockEntity = serverLevel.getBlockEntity(paramBlockPos);
/*     */     
/* 368 */     Optional optional = blockState.getBlock().getLootTable();
/* 369 */     if (optional.isEmpty()) {
/* 370 */       throw ERROR_NO_BLOCK_LOOT_TABLE.create(blockState.getBlock().getName());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 378 */     LootParams.Builder builder = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)paramBlockPos)).withParameter(LootContextParams.BLOCK_STATE, blockState).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, commandSourceStack.getEntity()).withParameter(LootContextParams.TOOL, paramItemStack);
/*     */     
/* 380 */     List<ItemStack> list = blockState.getDrops(builder);
/* 381 */     return paramDropConsumer.accept(paramCommandContext, list, paramList -> callback(paramCommandSourceStack, paramList, paramOptional.get()));
/*     */   }
/*     */   
/*     */   private static int dropKillLoot(CommandContext<CommandSourceStack> paramCommandContext, Entity paramEntity, DropConsumer paramDropConsumer) throws CommandSyntaxException {
/* 385 */     Optional<ResourceKey> optional = paramEntity.getLootTable();
/* 386 */     if (optional.isEmpty()) {
/* 387 */       throw ERROR_NO_ENTITY_LOOT_TABLE.create(paramEntity.getDisplayName());
/*     */     }
/* 389 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*     */     
/* 391 */     LootParams.Builder builder = new LootParams.Builder(commandSourceStack.getLevel());
/* 392 */     Entity entity = commandSourceStack.getEntity();
/* 393 */     if (entity instanceof Player) { Player player = (Player)entity;
/* 394 */       builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player); }
/*     */     
/* 396 */     builder.withParameter(LootContextParams.DAMAGE_SOURCE, paramEntity.damageSources().magic());
/* 397 */     builder.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, entity);
/* 398 */     builder.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, entity);
/* 399 */     builder.withParameter(LootContextParams.THIS_ENTITY, paramEntity);
/* 400 */     builder.withParameter(LootContextParams.ORIGIN, commandSourceStack.getPosition());
/* 401 */     LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
/*     */     
/* 403 */     LootTable lootTable = commandSourceStack.getServer().reloadableRegistries().getLootTable(optional.get());
/* 404 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(lootParams);
/* 405 */     return paramDropConsumer.accept(paramCommandContext, (List<ItemStack>)objectArrayList, paramList -> callback(paramCommandSourceStack, paramList, paramOptional.get()));
/*     */   }
/*     */   
/*     */   private static int dropChestLoot(CommandContext<CommandSourceStack> paramCommandContext, Holder<LootTable> paramHolder, DropConsumer paramDropConsumer) throws CommandSyntaxException {
/* 409 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     LootParams lootParams = (new LootParams.Builder(commandSourceStack.getLevel())).withOptionalParameter(LootContextParams.THIS_ENTITY, commandSourceStack.getEntity()).withParameter(LootContextParams.ORIGIN, commandSourceStack.getPosition()).create(LootContextParamSets.CHEST);
/*     */     
/* 416 */     return drop(paramCommandContext, paramHolder, lootParams, paramDropConsumer);
/*     */   }
/*     */   
/*     */   private static int dropFishingLoot(CommandContext<CommandSourceStack> paramCommandContext, Holder<LootTable> paramHolder, BlockPos paramBlockPos, ItemStack paramItemStack, DropConsumer paramDropConsumer) throws CommandSyntaxException {
/* 420 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     LootParams lootParams = (new LootParams.Builder(commandSourceStack.getLevel())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)paramBlockPos)).withParameter(LootContextParams.TOOL, paramItemStack).withOptionalParameter(LootContextParams.THIS_ENTITY, commandSourceStack.getEntity()).create(LootContextParamSets.FISHING);
/*     */     
/* 428 */     return drop(paramCommandContext, paramHolder, lootParams, paramDropConsumer);
/*     */   }
/*     */   
/*     */   private static int drop(CommandContext<CommandSourceStack> paramCommandContext, Holder<LootTable> paramHolder, LootParams paramLootParams, DropConsumer paramDropConsumer) throws CommandSyntaxException {
/* 432 */     CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/* 433 */     ObjectArrayList objectArrayList = ((LootTable)paramHolder.value()).getRandomItems(paramLootParams);
/* 434 */     return paramDropConsumer.accept(paramCommandContext, (List<ItemStack>)objectArrayList, paramList -> callback(paramCommandSourceStack, paramList));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface TailProvider {
/*     */     ArgumentBuilder<CommandSourceStack, ?> construct(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, LootCommand.DropConsumer param1DropConsumer);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DropConsumer {
/*     */     int accept(CommandContext<CommandSourceStack> param1CommandContext, List<ItemStack> param1List, LootCommand.Callback param1Callback) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface Callback {
/*     */     void accept(List<ItemStack> param1List) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\LootCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */