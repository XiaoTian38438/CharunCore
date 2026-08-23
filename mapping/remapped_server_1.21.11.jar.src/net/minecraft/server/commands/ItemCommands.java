/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SlotProvider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public class ItemCommands {
/*     */   static final Dynamic3CommandExceptionType ERROR_TARGET_NOT_A_CONTAINER;
/*     */   static final Dynamic3CommandExceptionType ERROR_SOURCE_NOT_A_CONTAINER;
/*     */   static final DynamicCommandExceptionType ERROR_TARGET_INAPPLICABLE_SLOT;
/*     */   
/*     */   static {
/*  54 */     ERROR_TARGET_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((paramObject1, paramObject2, paramObject3) -> Component.translatableEscape("commands.item.target.not_a_container", new Object[] { paramObject1, paramObject2, paramObject3 }));
/*  55 */     ERROR_SOURCE_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((paramObject1, paramObject2, paramObject3) -> Component.translatableEscape("commands.item.source.not_a_container", new Object[] { paramObject1, paramObject2, paramObject3 }));
/*     */     
/*  57 */     ERROR_TARGET_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.item.target.no_such_slot", new Object[] { paramObject }));
/*  58 */     ERROR_SOURCE_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.item.source.no_such_slot", new Object[] { paramObject }));
/*     */     
/*  60 */     ERROR_TARGET_NO_CHANGES = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.item.target.no_changes", new Object[] { paramObject }));
/*  61 */     ERROR_TARGET_NO_CHANGES_KNOWN_ITEM = new Dynamic2CommandExceptionType((paramObject1, paramObject2) -> Component.translatableEscape("commands.item.target.no_changed.known_item", new Object[] { paramObject1, paramObject2 }));
/*     */   } private static final DynamicCommandExceptionType ERROR_SOURCE_INAPPLICABLE_SLOT; private static final DynamicCommandExceptionType ERROR_TARGET_NO_CHANGES; private static final Dynamic2CommandExceptionType ERROR_TARGET_NO_CHANGES_KNOWN_ITEM;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  64 */     paramCommandDispatcher.register(
/*  65 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("item")
/*  66 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  67 */         .then((
/*  68 */           (LiteralArgumentBuilder)Commands.literal("replace")
/*  69 */           .then(
/*  70 */             Commands.literal("block")
/*  71 */             .then(
/*  72 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/*  73 */               .then((
/*  74 */                 (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/*  75 */                 .then(
/*  76 */                   Commands.literal("with")
/*  77 */                   .then((
/*  78 */                     (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(paramCommandBuildContext))
/*  79 */                     .executes(paramCommandContext -> setBlockItem((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"), ItemArgument.getItem(paramCommandContext, "item").createItemStack(1, false))))
/*  80 */                     .then(
/*  81 */                       Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 99))
/*  82 */                       .executes(paramCommandContext -> setBlockItem((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"), ItemArgument.getItem(paramCommandContext, "item").createItemStack(IntegerArgumentType.getInteger(paramCommandContext, "count"), true)))))))
/*     */ 
/*     */ 
/*     */                 
/*  86 */                 .then((
/*  87 */                   (LiteralArgumentBuilder)Commands.literal("from")
/*  88 */                   .then(
/*  89 */                     Commands.literal("block")
/*  90 */                     .then(
/*  91 */                       Commands.argument("source", (ArgumentType)BlockPosArgument.blockPos())
/*  92 */                       .then((
/*  93 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/*  94 */                         .executes(paramCommandContext -> blockToBlock((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"))))
/*  95 */                         .then(
/*  96 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(paramCommandBuildContext))
/*  97 */                           .executes(paramCommandContext -> blockToBlock((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"), ResourceOrIdArgument.getLootModifier(paramCommandContext, "modifier"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 102 */                   .then(
/* 103 */                     Commands.literal("entity")
/* 104 */                     .then(
/* 105 */                       Commands.argument("source", (ArgumentType)EntityArgument.entity())
/* 106 */                       .then((
/* 107 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 108 */                         .executes(paramCommandContext -> entityToBlock((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"))))
/* 109 */                         .then(
/* 110 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(paramCommandBuildContext))
/* 111 */                           .executes(paramCommandContext -> entityToBlock((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"), ResourceOrIdArgument.getLootModifier(paramCommandContext, "modifier"))))))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 120 */           .then(
/* 121 */             Commands.literal("entity")
/* 122 */             .then(
/* 123 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 124 */               .then((
/* 125 */                 (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 126 */                 .then(
/* 127 */                   Commands.literal("with")
/* 128 */                   .then((
/* 129 */                     (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(paramCommandBuildContext))
/* 130 */                     .executes(paramCommandContext -> setEntityItem((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"), ItemArgument.getItem(paramCommandContext, "item").createItemStack(1, false))))
/* 131 */                     .then(
/* 132 */                       Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1, 99))
/* 133 */                       .executes(paramCommandContext -> setEntityItem((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"), ItemArgument.getItem(paramCommandContext, "item").createItemStack(IntegerArgumentType.getInteger(paramCommandContext, "count"), true)))))))
/*     */ 
/*     */ 
/*     */                 
/* 137 */                 .then((
/* 138 */                   (LiteralArgumentBuilder)Commands.literal("from")
/* 139 */                   .then(
/* 140 */                     Commands.literal("block")
/* 141 */                     .then(
/* 142 */                       Commands.argument("source", (ArgumentType)BlockPosArgument.blockPos())
/* 143 */                       .then((
/* 144 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 145 */                         .executes(paramCommandContext -> blockToEntities((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"))))
/* 146 */                         .then(
/* 147 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(paramCommandBuildContext))
/* 148 */                           .executes(paramCommandContext -> blockToEntities((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"), ResourceOrIdArgument.getLootModifier(paramCommandContext, "modifier"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 153 */                   .then(
/* 154 */                     Commands.literal("entity")
/* 155 */                     .then(
/* 156 */                       Commands.argument("source", (ArgumentType)EntityArgument.entity())
/* 157 */                       .then((
/* 158 */                         (RequiredArgumentBuilder)Commands.argument("sourceSlot", (ArgumentType)SlotArgument.slot())
/* 159 */                         .executes(paramCommandContext -> entityToEntities((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"))))
/* 160 */                         .then(
/* 161 */                           Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(paramCommandBuildContext))
/* 162 */                           .executes(paramCommandContext -> entityToEntities((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "source"), SlotArgument.getSlot(paramCommandContext, "sourceSlot"), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"), ResourceOrIdArgument.getLootModifier(paramCommandContext, "modifier")))))))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 172 */         .then((
/* 173 */           (LiteralArgumentBuilder)Commands.literal("modify")
/* 174 */           .then(
/* 175 */             Commands.literal("block")
/* 176 */             .then(
/* 177 */               Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 178 */               .then(
/* 179 */                 Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 180 */                 .then(
/* 181 */                   Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(paramCommandBuildContext))
/* 182 */                   .executes(paramCommandContext -> modifyBlockItem((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getLoadedBlockPos(paramCommandContext, "pos"), SlotArgument.getSlot(paramCommandContext, "slot"), ResourceOrIdArgument.getLootModifier(paramCommandContext, "modifier"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 187 */           .then(
/* 188 */             Commands.literal("entity")
/* 189 */             .then(
/* 190 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 191 */               .then(
/* 192 */                 Commands.argument("slot", (ArgumentType)SlotArgument.slot())
/* 193 */                 .then(
/* 194 */                   Commands.argument("modifier", (ArgumentType)ResourceOrIdArgument.lootModifier(paramCommandBuildContext))
/* 195 */                   .executes(paramCommandContext -> modifyEntityItem((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), SlotArgument.getSlot(paramCommandContext, "slot"), ResourceOrIdArgument.getLootModifier(paramCommandContext, "modifier")))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int modifyBlockItem(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, int paramInt, Holder<LootItemFunction> paramHolder) throws CommandSyntaxException {
/* 205 */     Container container = getContainer(paramCommandSourceStack, paramBlockPos, ERROR_TARGET_NOT_A_CONTAINER);
/* 206 */     if (paramInt < 0 || paramInt >= container.getContainerSize()) {
/* 207 */       throw ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(paramInt));
/*     */     }
/*     */     
/* 210 */     ItemStack itemStack = applyModifier(paramCommandSourceStack, paramHolder, container.getItem(paramInt));
/* 211 */     container.setItem(paramInt, itemStack);
/* 212 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.item.block.set.success", new Object[] { Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()), paramItemStack.getDisplayName() }), true);
/* 213 */     return 1;
/*     */   }
/*     */   
/*     */   private static int modifyEntityItem(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, int paramInt, Holder<LootItemFunction> paramHolder) throws CommandSyntaxException {
/* 217 */     HashMap<Entity, ItemStack> hashMap = Maps.newHashMapWithExpectedSize(paramCollection.size());
/*     */     
/* 219 */     for (Entity entity : paramCollection) {
/* 220 */       SlotAccess slotAccess = entity.getSlot(paramInt);
/* 221 */       if (slotAccess != null) {
/* 222 */         ItemStack itemStack = applyModifier(paramCommandSourceStack, paramHolder, slotAccess.get().copy());
/* 223 */         if (slotAccess.set(itemStack)) {
/* 224 */           hashMap.put(entity, itemStack);
/* 225 */           if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 226 */             serverPlayer.containerMenu.broadcastChanges(); }
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     if (hashMap.isEmpty()) {
/* 233 */       throw ERROR_TARGET_NO_CHANGES.create(Integer.valueOf(paramInt));
/*     */     }
/*     */     
/* 236 */     if (hashMap.size() == 1) {
/* 237 */       Map.Entry entry = hashMap.entrySet().iterator().next();
/* 238 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", new Object[] { ((Entity)paramEntry.getKey()).getDisplayName(), ((ItemStack)paramEntry.getValue()).getDisplayName() }), true);
/*     */     } else {
/* 240 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", new Object[] { Integer.valueOf(paramMap.size()) }), true);
/*     */     } 
/*     */     
/* 243 */     return hashMap.size();
/*     */   }
/*     */   
/*     */   private static int setBlockItem(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, int paramInt, ItemStack paramItemStack) throws CommandSyntaxException {
/* 247 */     Container container = getContainer(paramCommandSourceStack, paramBlockPos, ERROR_TARGET_NOT_A_CONTAINER);
/* 248 */     if (paramInt < 0 || paramInt >= container.getContainerSize()) {
/* 249 */       throw ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(paramInt));
/*     */     }
/*     */     
/* 252 */     container.setItem(paramInt, paramItemStack);
/* 253 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.item.block.set.success", new Object[] { Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()), paramItemStack.getDisplayName() }), true);
/* 254 */     return 1;
/*     */   }
/*     */   
/*     */   static Container getContainer(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, Dynamic3CommandExceptionType paramDynamic3CommandExceptionType) throws CommandSyntaxException {
/* 258 */     BlockEntity blockEntity = paramCommandSourceStack.getLevel().getBlockEntity(paramBlockPos);
/* 259 */     if (blockEntity instanceof Container) return (Container)blockEntity;
/*     */ 
/*     */     
/* 262 */     throw paramDynamic3CommandExceptionType.create(Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   private static int setEntityItem(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, int paramInt, ItemStack paramItemStack) throws CommandSyntaxException {
/* 266 */     ArrayList<Entity> arrayList = Lists.newArrayListWithCapacity(paramCollection.size());
/*     */     
/* 268 */     for (Entity entity : paramCollection) {
/* 269 */       SlotAccess slotAccess = entity.getSlot(paramInt);
/* 270 */       if (slotAccess != null && slotAccess.set(paramItemStack.copy())) {
/* 271 */         arrayList.add(entity);
/* 272 */         if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 273 */           serverPlayer.containerMenu.broadcastChanges(); }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 278 */     if (arrayList.isEmpty()) {
/* 279 */       throw ERROR_TARGET_NO_CHANGES_KNOWN_ITEM.create(paramItemStack.getDisplayName(), Integer.valueOf(paramInt));
/*     */     }
/*     */     
/* 282 */     if (arrayList.size() == 1) {
/* 283 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", new Object[] { ((Entity)paramList.getFirst()).getDisplayName(), paramItemStack.getDisplayName() }), true);
/*     */     } else {
/* 285 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", new Object[] { Integer.valueOf(paramList.size()), paramItemStack.getDisplayName() }), true);
/*     */     } 
/*     */     
/* 288 */     return arrayList.size();
/*     */   }
/*     */   
/*     */   private static int blockToEntities(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, int paramInt1, Collection<? extends Entity> paramCollection, int paramInt2) throws CommandSyntaxException {
/* 292 */     return setEntityItem(paramCommandSourceStack, paramCollection, paramInt2, getBlockItem(paramCommandSourceStack, paramBlockPos, paramInt1));
/*     */   }
/*     */   
/*     */   private static int blockToEntities(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, int paramInt1, Collection<? extends Entity> paramCollection, int paramInt2, Holder<LootItemFunction> paramHolder) throws CommandSyntaxException {
/* 296 */     return setEntityItem(paramCommandSourceStack, paramCollection, paramInt2, applyModifier(paramCommandSourceStack, paramHolder, getBlockItem(paramCommandSourceStack, paramBlockPos, paramInt1)));
/*     */   }
/*     */   
/*     */   private static int blockToBlock(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos1, int paramInt1, BlockPos paramBlockPos2, int paramInt2) throws CommandSyntaxException {
/* 300 */     return setBlockItem(paramCommandSourceStack, paramBlockPos2, paramInt2, getBlockItem(paramCommandSourceStack, paramBlockPos1, paramInt1));
/*     */   }
/*     */   
/*     */   private static int blockToBlock(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos1, int paramInt1, BlockPos paramBlockPos2, int paramInt2, Holder<LootItemFunction> paramHolder) throws CommandSyntaxException {
/* 304 */     return setBlockItem(paramCommandSourceStack, paramBlockPos2, paramInt2, applyModifier(paramCommandSourceStack, paramHolder, getBlockItem(paramCommandSourceStack, paramBlockPos1, paramInt1)));
/*     */   }
/*     */   
/*     */   private static int entityToBlock(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt1, BlockPos paramBlockPos, int paramInt2) throws CommandSyntaxException {
/* 308 */     return setBlockItem(paramCommandSourceStack, paramBlockPos, paramInt2, getItemInSlot((SlotProvider)paramEntity, paramInt1));
/*     */   }
/*     */   
/*     */   private static int entityToBlock(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt1, BlockPos paramBlockPos, int paramInt2, Holder<LootItemFunction> paramHolder) throws CommandSyntaxException {
/* 312 */     return setBlockItem(paramCommandSourceStack, paramBlockPos, paramInt2, applyModifier(paramCommandSourceStack, paramHolder, getItemInSlot((SlotProvider)paramEntity, paramInt1)));
/*     */   }
/*     */   
/*     */   private static int entityToEntities(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt1, Collection<? extends Entity> paramCollection, int paramInt2) throws CommandSyntaxException {
/* 316 */     return setEntityItem(paramCommandSourceStack, paramCollection, paramInt2, getItemInSlot((SlotProvider)paramEntity, paramInt1));
/*     */   }
/*     */   
/*     */   private static int entityToEntities(CommandSourceStack paramCommandSourceStack, Entity paramEntity, int paramInt1, Collection<? extends Entity> paramCollection, int paramInt2, Holder<LootItemFunction> paramHolder) throws CommandSyntaxException {
/* 320 */     return setEntityItem(paramCommandSourceStack, paramCollection, paramInt2, applyModifier(paramCommandSourceStack, paramHolder, getItemInSlot((SlotProvider)paramEntity, paramInt1)));
/*     */   }
/*     */   
/*     */   private static ItemStack applyModifier(CommandSourceStack paramCommandSourceStack, Holder<LootItemFunction> paramHolder, ItemStack paramItemStack) {
/* 324 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     LootParams lootParams = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, paramCommandSourceStack.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, paramCommandSourceStack.getEntity()).create(LootContextParamSets.COMMAND);
/* 330 */     LootContext lootContext = (new LootContext.Builder(lootParams)).create(Optional.empty());
/* 331 */     lootContext.pushVisitedElement(LootContext.createVisitedEntry((LootItemFunction)paramHolder.value()));
/*     */     
/* 333 */     ItemStack itemStack = (ItemStack)((LootItemFunction)paramHolder.value()).apply(paramItemStack, lootContext);
/* 334 */     itemStack.limitSize(itemStack.getMaxStackSize());
/* 335 */     return itemStack;
/*     */   }
/*     */   
/*     */   private static ItemStack getItemInSlot(SlotProvider paramSlotProvider, int paramInt) throws CommandSyntaxException {
/* 339 */     SlotAccess slotAccess = paramSlotProvider.getSlot(paramInt);
/* 340 */     if (slotAccess == null) {
/* 341 */       throw ERROR_SOURCE_INAPPLICABLE_SLOT.create(Integer.valueOf(paramInt));
/*     */     }
/* 343 */     return slotAccess.get().copy();
/*     */   }
/*     */   
/*     */   private static ItemStack getBlockItem(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, int paramInt) throws CommandSyntaxException {
/* 347 */     Container container = getContainer(paramCommandSourceStack, paramBlockPos, ERROR_SOURCE_NOT_A_CONTAINER);
/* 348 */     return getItemInSlot((SlotProvider)container, paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ItemCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */