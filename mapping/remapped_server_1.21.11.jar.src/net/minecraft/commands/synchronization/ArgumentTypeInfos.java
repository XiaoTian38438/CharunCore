/*     */ package net.minecraft.commands.synchronization;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.LongArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.commands.arguments.AngleArgument;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.commands.arguments.DimensionArgument;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.GameModeArgument;
/*     */ import net.minecraft.commands.arguments.GameProfileArgument;
/*     */ import net.minecraft.commands.arguments.HeightmapTypeArgument;
/*     */ import net.minecraft.commands.arguments.HexColorArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.MessageArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.NbtTagArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
/*     */ import net.minecraft.commands.arguments.OperationArgument;
/*     */ import net.minecraft.commands.arguments.ParticleArgument;
/*     */ import net.minecraft.commands.arguments.RangeArgument;
/*     */ import net.minecraft.commands.arguments.ResourceArgument;
/*     */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
/*     */ import net.minecraft.commands.arguments.ResourceSelectorArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.ScoreboardSlotArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.SlotsArgument;
/*     */ import net.minecraft.commands.arguments.StyleArgument;
/*     */ import net.minecraft.commands.arguments.TeamArgument;
/*     */ import net.minecraft.commands.arguments.TemplateMirrorArgument;
/*     */ import net.minecraft.commands.arguments.TemplateRotationArgument;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.commands.arguments.item.ItemPredicateArgument;
/*     */ import net.minecraft.commands.synchronization.brigadier.DoubleArgumentInfo;
/*     */ import net.minecraft.commands.synchronization.brigadier.FloatArgumentInfo;
/*     */ import net.minecraft.commands.synchronization.brigadier.IntegerArgumentInfo;
/*     */ import net.minecraft.commands.synchronization.brigadier.LongArgumentInfo;
/*     */ import net.minecraft.commands.synchronization.brigadier.StringArgumentSerializer;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ public class ArgumentTypeInfos
/*     */ {
/*  69 */   private static final Map<Class<?>, ArgumentTypeInfo<?, ?>> BY_CLASS = Maps.newHashMap();
/*     */   
/*     */   private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(Registry<ArgumentTypeInfo<?, ?>> paramRegistry, String paramString, Class<? extends A> paramClass, ArgumentTypeInfo<A, T> paramArgumentTypeInfo) {
/*  72 */     BY_CLASS.put(paramClass, paramArgumentTypeInfo);
/*  73 */     return (ArgumentTypeInfo<A, T>)Registry.register(paramRegistry, paramString, paramArgumentTypeInfo);
/*     */   }
/*     */   
/*     */   public static ArgumentTypeInfo<?, ?> bootstrap(Registry<ArgumentTypeInfo<?, ?>> paramRegistry) {
/*  77 */     register(paramRegistry, "brigadier:bool", BoolArgumentType.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(BoolArgumentType::bool));
/*  78 */     register(paramRegistry, "brigadier:float", FloatArgumentType.class, (ArgumentTypeInfo<FloatArgumentType, ArgumentTypeInfo.Template<FloatArgumentType>>)new FloatArgumentInfo());
/*  79 */     register(paramRegistry, "brigadier:double", DoubleArgumentType.class, (ArgumentTypeInfo<DoubleArgumentType, ArgumentTypeInfo.Template<DoubleArgumentType>>)new DoubleArgumentInfo());
/*  80 */     register(paramRegistry, "brigadier:integer", IntegerArgumentType.class, (ArgumentTypeInfo<IntegerArgumentType, ArgumentTypeInfo.Template<IntegerArgumentType>>)new IntegerArgumentInfo());
/*  81 */     register(paramRegistry, "brigadier:long", LongArgumentType.class, (ArgumentTypeInfo<LongArgumentType, ArgumentTypeInfo.Template<LongArgumentType>>)new LongArgumentInfo());
/*  82 */     register(paramRegistry, "brigadier:string", StringArgumentType.class, (ArgumentTypeInfo<StringArgumentType, ArgumentTypeInfo.Template<StringArgumentType>>)new StringArgumentSerializer());
/*     */     
/*  84 */     register(paramRegistry, "entity", EntityArgument.class, (ArgumentTypeInfo<EntityArgument, ArgumentTypeInfo.Template<EntityArgument>>)new EntityArgument.Info());
/*  85 */     register(paramRegistry, "game_profile", GameProfileArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(GameProfileArgument::gameProfile));
/*  86 */     register(paramRegistry, "block_pos", BlockPosArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(BlockPosArgument::blockPos));
/*  87 */     register(paramRegistry, "column_pos", ColumnPosArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(ColumnPosArgument::columnPos));
/*  88 */     register(paramRegistry, "vec3", Vec3Argument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(Vec3Argument::vec3));
/*  89 */     register(paramRegistry, "vec2", Vec2Argument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(Vec2Argument::vec2));
/*  90 */     register(paramRegistry, "block_state", BlockStateArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(BlockStateArgument::block));
/*  91 */     register(paramRegistry, "block_predicate", BlockPredicateArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(BlockPredicateArgument::blockPredicate));
/*  92 */     register(paramRegistry, "item_stack", ItemArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ItemArgument::item));
/*  93 */     register(paramRegistry, "item_predicate", ItemPredicateArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ItemPredicateArgument::itemPredicate));
/*  94 */     register(paramRegistry, "color", ColorArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(ColorArgument::color));
/*  95 */     register(paramRegistry, "hex_color", HexColorArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(HexColorArgument::hexColor));
/*  96 */     register(paramRegistry, "component", ComponentArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ComponentArgument::textComponent));
/*  97 */     register(paramRegistry, "style", StyleArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(StyleArgument::style));
/*  98 */     register(paramRegistry, "message", MessageArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(MessageArgument::message));
/*  99 */     register(paramRegistry, "nbt_compound_tag", CompoundTagArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(CompoundTagArgument::compoundTag));
/* 100 */     register(paramRegistry, "nbt_tag", NbtTagArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(NbtTagArgument::nbtTag));
/* 101 */     register(paramRegistry, "nbt_path", NbtPathArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(NbtPathArgument::nbtPath));
/* 102 */     register(paramRegistry, "objective", ObjectiveArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(ObjectiveArgument::objective));
/* 103 */     register(paramRegistry, "objective_criteria", ObjectiveCriteriaArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(ObjectiveCriteriaArgument::criteria));
/* 104 */     register(paramRegistry, "operation", OperationArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(OperationArgument::operation));
/* 105 */     register(paramRegistry, "particle", ParticleArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ParticleArgument::particle));
/* 106 */     register(paramRegistry, "angle", AngleArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(AngleArgument::angle));
/* 107 */     register(paramRegistry, "rotation", RotationArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(RotationArgument::rotation));
/* 108 */     register(paramRegistry, "scoreboard_slot", ScoreboardSlotArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(ScoreboardSlotArgument::displaySlot));
/* 109 */     register(paramRegistry, "score_holder", ScoreHolderArgument.class, (ArgumentTypeInfo<ScoreHolderArgument, ArgumentTypeInfo.Template<ScoreHolderArgument>>)new ScoreHolderArgument.Info());
/* 110 */     register(paramRegistry, "swizzle", SwizzleArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(SwizzleArgument::swizzle));
/* 111 */     register(paramRegistry, "team", TeamArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(TeamArgument::team));
/* 112 */     register(paramRegistry, "item_slot", SlotArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(SlotArgument::slot));
/* 113 */     register(paramRegistry, "item_slots", SlotsArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(SlotsArgument::slots));
/* 114 */     register(paramRegistry, "resource_location", IdentifierArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(IdentifierArgument::id));
/* 115 */     register(paramRegistry, "function", FunctionArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(FunctionArgument::functions));
/* 116 */     register(paramRegistry, "entity_anchor", EntityAnchorArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(EntityAnchorArgument::anchor));
/* 117 */     register(paramRegistry, "int_range", RangeArgument.Ints.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(RangeArgument::intRange));
/* 118 */     register(paramRegistry, "float_range", RangeArgument.Floats.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(RangeArgument::floatRange));
/* 119 */     register(paramRegistry, "dimension", DimensionArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(DimensionArgument::dimension));
/* 120 */     register(paramRegistry, "gamemode", GameModeArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(GameModeArgument::gameMode));
/* 121 */     register(paramRegistry, "time", TimeArgument.class, (ArgumentTypeInfo<TimeArgument, ArgumentTypeInfo.Template<TimeArgument>>)new TimeArgument.Info());
/* 122 */     register(paramRegistry, "resource_or_tag", (Class)fixClassType(ResourceOrTagArgument.class), (ArgumentTypeInfo<ArgumentType<?>, ArgumentTypeInfo.Template<ArgumentType<?>>>)new ResourceOrTagArgument.Info());
/* 123 */     register(paramRegistry, "resource_or_tag_key", (Class)fixClassType(ResourceOrTagKeyArgument.class), (ArgumentTypeInfo<ArgumentType<?>, ArgumentTypeInfo.Template<ArgumentType<?>>>)new ResourceOrTagKeyArgument.Info());
/* 124 */     register(paramRegistry, "resource", (Class)fixClassType(ResourceArgument.class), (ArgumentTypeInfo<ArgumentType<?>, ArgumentTypeInfo.Template<ArgumentType<?>>>)new ResourceArgument.Info());
/* 125 */     register(paramRegistry, "resource_key", (Class)fixClassType(ResourceKeyArgument.class), (ArgumentTypeInfo<ArgumentType<?>, ArgumentTypeInfo.Template<ArgumentType<?>>>)new ResourceKeyArgument.Info());
/* 126 */     register(paramRegistry, "resource_selector", (Class)fixClassType(ResourceSelectorArgument.class), (ArgumentTypeInfo<ArgumentType<?>, ArgumentTypeInfo.Template<ArgumentType<?>>>)new ResourceSelectorArgument.Info());
/* 127 */     register(paramRegistry, "template_mirror", TemplateMirrorArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(TemplateMirrorArgument::templateMirror));
/* 128 */     register(paramRegistry, "template_rotation", TemplateRotationArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(TemplateRotationArgument::templateRotation));
/* 129 */     register(paramRegistry, "heightmap", HeightmapTypeArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextFree(HeightmapTypeArgument::heightmap));
/* 130 */     register(paramRegistry, "loot_table", ResourceOrIdArgument.LootTableArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ResourceOrIdArgument::lootTable));
/* 131 */     register(paramRegistry, "loot_predicate", ResourceOrIdArgument.LootPredicateArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ResourceOrIdArgument::lootPredicate));
/* 132 */     register(paramRegistry, "loot_modifier", ResourceOrIdArgument.LootModifierArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ResourceOrIdArgument::lootModifier));
/* 133 */     register(paramRegistry, "dialog", ResourceOrIdArgument.DialogArgument.class, (ArgumentTypeInfo)SingletonArgumentInfo.contextAware(ResourceOrIdArgument::dialog));
/* 134 */     return register(paramRegistry, "uuid", UuidArgument.class, SingletonArgumentInfo.contextFree(UuidArgument::uuid));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends ArgumentType<?>> Class<T> fixClassType(Class<? super T> paramClass) {
/* 139 */     return (Class)paramClass;
/*     */   }
/*     */   
/*     */   public static boolean isClassRecognized(Class<?> paramClass) {
/* 143 */     return BY_CLASS.containsKey(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A extends ArgumentType<?>> ArgumentTypeInfo<A, ?> byClass(A paramA) {
/* 148 */     ArgumentTypeInfo<A, ?> argumentTypeInfo = (ArgumentTypeInfo)BY_CLASS.get(paramA.getClass());
/* 149 */     if (argumentTypeInfo == null) {
/* 150 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Unrecognized argument type %s (%s)", new Object[] { paramA, paramA.getClass() }));
/*     */     }
/* 152 */     return argumentTypeInfo;
/*     */   }
/*     */   
/*     */   public static <A extends ArgumentType<?>> ArgumentTypeInfo.Template<A> unpack(A paramA) {
/* 156 */     return byClass(paramA).unpack(paramA);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */