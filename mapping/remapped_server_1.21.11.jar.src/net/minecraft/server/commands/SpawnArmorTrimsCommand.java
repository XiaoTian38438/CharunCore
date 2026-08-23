/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ 
/*     */ public class SpawnArmorTrimsCommand {
/*  40 */   private static final List<ResourceKey<TrimPattern>> VANILLA_TRIM_PATTERNS = List.of((ResourceKey<TrimPattern>[])new ResourceKey[] { TrimPatterns.SENTRY, TrimPatterns.DUNE, TrimPatterns.COAST, TrimPatterns.WILD, TrimPatterns.WARD, TrimPatterns.EYE, TrimPatterns.VEX, TrimPatterns.TIDE, TrimPatterns.SNOUT, TrimPatterns.RIB, TrimPatterns.SPIRE, TrimPatterns.WAYFINDER, TrimPatterns.SHAPER, TrimPatterns.SILENCE, TrimPatterns.RAISER, TrimPatterns.HOST, TrimPatterns.FLOW, TrimPatterns.BOLT });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static final List<ResourceKey<TrimMaterial>> VANILLA_TRIM_MATERIALS = List.of((ResourceKey<TrimMaterial>[])new ResourceKey[] { TrimMaterials.QUARTZ, TrimMaterials.IRON, TrimMaterials.NETHERITE, TrimMaterials.REDSTONE, TrimMaterials.COPPER, TrimMaterials.GOLD, TrimMaterials.EMERALD, TrimMaterials.DIAMOND, TrimMaterials.LAPIS, TrimMaterials.AMETHYST, TrimMaterials.RESIN });
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final ToIntFunction<ResourceKey<TrimPattern>> TRIM_PATTERN_ORDER = Util.createIndexLookup(VANILLA_TRIM_PATTERNS);
/*  51 */   private static final ToIntFunction<ResourceKey<TrimMaterial>> TRIM_MATERIAL_ORDER = Util.createIndexLookup(VANILLA_TRIM_MATERIALS); private static final DynamicCommandExceptionType ERROR_INVALID_PATTERN;
/*     */   
/*     */   static {
/*  54 */     ERROR_INVALID_PATTERN = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("Invalid pattern", new Object[] { paramObject }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  57 */     paramCommandDispatcher.register(
/*  58 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawn_armor_trims")
/*  59 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  60 */         .then(
/*  61 */           Commands.literal("*_lag_my_game")
/*  62 */           .executes(paramCommandContext -> spawnAllArmorTrims((CommandSourceStack)paramCommandContext.getSource(), (Player)((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()))))
/*     */         
/*  64 */         .then(
/*  65 */           Commands.argument("pattern", (ArgumentType)ResourceKeyArgument.key(Registries.TRIM_PATTERN))
/*  66 */           .executes(paramCommandContext -> spawnArmorTrim((CommandSourceStack)paramCommandContext.getSource(), (Player)((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException(), ResourceKeyArgument.getRegistryKey(paramCommandContext, "pattern", Registries.TRIM_PATTERN, ERROR_INVALID_PATTERN)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int spawnAllArmorTrims(CommandSourceStack paramCommandSourceStack, Player paramPlayer) {
/*  72 */     return spawnArmorTrims(paramCommandSourceStack, paramPlayer, paramCommandSourceStack.getServer().registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).listElements());
/*     */   }
/*     */   
/*     */   private static int spawnArmorTrim(CommandSourceStack paramCommandSourceStack, Player paramPlayer, ResourceKey<TrimPattern> paramResourceKey) {
/*  76 */     return spawnArmorTrims(paramCommandSourceStack, paramPlayer, Stream.of(paramCommandSourceStack.getServer().registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).get(paramResourceKey).orElseThrow()));
/*     */   }
/*     */   
/*     */   private static int spawnArmorTrims(CommandSourceStack paramCommandSourceStack, Player paramPlayer, Stream<Holder.Reference<TrimPattern>> paramStream) {
/*  80 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/*     */     
/*  82 */     List<Holder.Reference> list1 = paramStream.sorted(Comparator.comparing(paramReference -> Integer.valueOf(TRIM_PATTERN_ORDER.applyAsInt(paramReference.key())))).toList();
/*  83 */     List<Holder.Reference> list2 = serverLevel.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL).listElements().sorted(Comparator.comparing(paramReference -> Integer.valueOf(TRIM_MATERIAL_ORDER.applyAsInt(paramReference.key())))).toList();
/*  84 */     List<Holder.Reference<Item>> list = findEquippableItemsWithAssets((HolderLookup<Item>)serverLevel.registryAccess().lookupOrThrow(Registries.ITEM));
/*     */     
/*  86 */     BlockPos blockPos = paramPlayer.blockPosition().relative(paramPlayer.getDirection(), 5);
/*     */     
/*  88 */     double d = 3.0D;
/*  89 */     for (byte b = 0; b < list2.size(); b++) {
/*  90 */       Holder.Reference reference = list2.get(b);
/*  91 */       for (byte b1 = 0; b1 < list1.size(); b1++) {
/*  92 */         Holder.Reference reference1 = list1.get(b1);
/*  93 */         ArmorTrim armorTrim = new ArmorTrim((Holder)reference, (Holder)reference1);
/*     */         
/*  95 */         for (byte b2 = 0; b2 < list.size(); b2++) {
/*  96 */           Holder.Reference reference2 = list.get(b2);
/*     */           
/*  98 */           double d1 = blockPos.getX() + 0.5D - b2 * 3.0D;
/*  99 */           double d2 = blockPos.getY() + 0.5D + b * 3.0D;
/* 100 */           double d3 = blockPos.getZ() + 0.5D + (b1 * 10);
/* 101 */           ArmorStand armorStand = new ArmorStand((Level)serverLevel, d1, d2, d3);
/* 102 */           armorStand.setYRot(180.0F);
/* 103 */           armorStand.setNoGravity(true);
/*     */           
/* 105 */           ItemStack itemStack = new ItemStack((Holder)reference2);
/* 106 */           Equippable equippable = Objects.<Equippable>requireNonNull((Equippable)itemStack.get(DataComponents.EQUIPPABLE));
/* 107 */           itemStack.set(DataComponents.TRIM, armorTrim);
/* 108 */           armorStand.setItemSlot(equippable.slot(), itemStack);
/* 109 */           if (b2 == 0) {
/* 110 */             armorStand.setCustomName((Component)((TrimPattern)armorTrim.pattern().value()).copyWithStyle(armorTrim.material()).copy().append(" & ").append(((TrimMaterial)armorTrim.material().value()).description()));
/* 111 */             armorStand.setCustomNameVisible(true);
/*     */           } else {
/* 113 */             armorStand.setInvisible(true);
/*     */           } 
/* 115 */           serverLevel.addFreshEntity((Entity)armorStand);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     paramCommandSourceStack.sendSuccess(() -> Component.literal("Armorstands with trimmed armor spawned around you"), true);
/*     */     
/* 122 */     return 1;
/*     */   }
/*     */   
/*     */   private static List<Holder.Reference<Item>> findEquippableItemsWithAssets(HolderLookup<Item> paramHolderLookup) {
/* 126 */     ArrayList<Holder.Reference<Item>> arrayList = new ArrayList();
/* 127 */     paramHolderLookup.listElements().forEach(paramReference -> {
/*     */           Equippable equippable = (Equippable)((Item)paramReference.value()).components().get(DataComponents.EQUIPPABLE);
/*     */           if (equippable != null && equippable.slot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR && equippable.assetId().isPresent()) {
/*     */             paramList.add(paramReference);
/*     */           }
/*     */         });
/* 133 */     return arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SpawnArmorTrimsCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */