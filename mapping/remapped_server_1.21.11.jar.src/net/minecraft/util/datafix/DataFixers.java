/*      */ package net.minecraft.util.datafix;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*      */ import com.mojang.datafixers.DSL;
/*      */ import com.mojang.datafixers.DataFix;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.DataFixerBuilder;
/*      */ import com.mojang.datafixers.OpticFinder;
/*      */ import com.mojang.datafixers.TypeRewriteRule;
/*      */ import com.mojang.datafixers.Typed;
/*      */ import com.mojang.datafixers.schemas.Schema;
/*      */ import com.mojang.datafixers.types.Type;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.fixes.AbstractArrowPickupFix;
/*      */ import net.minecraft.util.datafix.fixes.AddFieldFix;
/*      */ import net.minecraft.util.datafix.fixes.AddFlagIfNotPresentFix;
/*      */ import net.minecraft.util.datafix.fixes.AddNewChoices;
/*      */ import net.minecraft.util.datafix.fixes.AdvancementsFix;
/*      */ import net.minecraft.util.datafix.fixes.AdvancementsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.AreaEffectCloudDurationScaleFix;
/*      */ import net.minecraft.util.datafix.fixes.AreaEffectCloudPotionFix;
/*      */ import net.minecraft.util.datafix.fixes.AttributeIdPrefixFix;
/*      */ import net.minecraft.util.datafix.fixes.AttributeModifierIdFix;
/*      */ import net.minecraft.util.datafix.fixes.AttributesRenameLegacy;
/*      */ import net.minecraft.util.datafix.fixes.BannerEntityCustomNameToOverrideComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.BannerPatternFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.BedItemColorFix;
/*      */ import net.minecraft.util.datafix.fixes.BeehiveFieldRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.BiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.BitStorageAlignFix;
/*      */ import net.minecraft.util.datafix.fixes.BlendingDataFix;
/*      */ import net.minecraft.util.datafix.fixes.BlendingDataRemoveFromNetherEndFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityBannerColorFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityBlockStateFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityCustomNameToComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityFurnaceBurnTimeFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityIdFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityJukeboxFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityShulkerBoxColorFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntitySignDoubleSidedEditableTextFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockEntityUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockNameFlatteningFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockPosFormatAndRenamesFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockPropertyRenameAndFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.BlockStateStructureTemplateFix;
/*      */ import net.minecraft.util.datafix.fixes.BoatSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.CarvingStepRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.CatTypeFix;
/*      */ import net.minecraft.util.datafix.fixes.CauldronRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.CavesAndCliffsRenames;
/*      */ import net.minecraft.util.datafix.fixes.ChestedHorsesInventoryZeroIndexingFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkBedBlockEntityInjecterFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkBiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkDeleteIgnoredLightDataFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkDeleteLightFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkHeightAndBiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkLightRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkProtoTickListFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkRenamesFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkStatusFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkStatusFix2;
/*      */ import net.minecraft.util.datafix.fixes.ChunkStructuresTemplateRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkTicketUnpackPosFix;
/*      */ import net.minecraft.util.datafix.fixes.ChunkToProtochunkFix;
/*      */ import net.minecraft.util.datafix.fixes.ColorlessShulkerEntityFix;
/*      */ import net.minecraft.util.datafix.fixes.ContainerBlockEntityLockPredicateFix;
/*      */ import net.minecraft.util.datafix.fixes.CopperGolemWeatherStateFix;
/*      */ import net.minecraft.util.datafix.fixes.CriteriaRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.CustomModelDataExpandFix;
/*      */ import net.minecraft.util.datafix.fixes.DebugProfileOverlayReferenceFix;
/*      */ import net.minecraft.util.datafix.fixes.DecoratedPotFieldRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.DropChancesFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.DropInvalidSignDataFix;
/*      */ import net.minecraft.util.datafix.fixes.DyeItemRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EffectDurationFix;
/*      */ import net.minecraft.util.datafix.fixes.EmptyItemInHotbarFix;
/*      */ import net.minecraft.util.datafix.fixes.EmptyItemInVillagerTradeFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityArmorStandSilentFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityAttributeBaseValueFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityBlockStateFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityBrushableBlockFieldsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityCatSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityCodSalmonFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityCustomNameToComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityElderGuardianSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityEquipmentToArmorAndHandFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityFallDistanceFloatToDoubleFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityFieldsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityGoatMissingStateFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityHealthFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityHorseSaddleFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityHorseSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityIdFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityItemFrameDirectionFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityMinecartIdentifiersFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityPaintingItemFrameDirectionFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityPaintingMotiveFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityProjectileOwnerFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityPufferfishRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityRavagerRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityRedundantChanceTagsFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityRidingToPassengersFix;
/*      */ import net.minecraft.util.datafix.fixes.EntitySalmonSizeFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityShulkerColorFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityShulkerRotationFix;
/*      */ import net.minecraft.util.datafix.fixes.EntitySkeletonSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntitySpawnerItemVariantComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityStringUuidFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityTheRenameningFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityTippedArrowFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityVariantFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityWolfColorFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityZombieSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityZombieVillagerTypeFix;
/*      */ import net.minecraft.util.datafix.fixes.EntityZombifiedPiglinRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.EquipmentFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.EquippableAssetRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.FeatureFlagRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.FilteredBooksFix;
/*      */ import net.minecraft.util.datafix.fixes.FilteredSignsFix;
/*      */ import net.minecraft.util.datafix.fixes.FireResistantToDamageResistantComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.FixProjectileStoredItem;
/*      */ import net.minecraft.util.datafix.fixes.FixWolfHealth;
/*      */ import net.minecraft.util.datafix.fixes.FoodToConsumableFix;
/*      */ import net.minecraft.util.datafix.fixes.ForcePoiRebuild;
/*      */ import net.minecraft.util.datafix.fixes.ForcedChunkToTicketFix;
/*      */ import net.minecraft.util.datafix.fixes.FurnaceRecipeFix;
/*      */ import net.minecraft.util.datafix.fixes.GameRuleRegistryFix;
/*      */ import net.minecraft.util.datafix.fixes.GoatHornIdFix;
/*      */ import net.minecraft.util.datafix.fixes.GossipUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.HeightmapRenamingFix;
/*      */ import net.minecraft.util.datafix.fixes.HorseBodyArmorItemFix;
/*      */ import net.minecraft.util.datafix.fixes.IglooMetadataRemovalFix;
/*      */ import net.minecraft.util.datafix.fixes.InlineBlockPosFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.InvalidBlockEntityLockFix;
/*      */ import net.minecraft.util.datafix.fixes.InvalidLockComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemBannerColorFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemCustomNameToComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemIdFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemLoreFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemPotionFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemShulkerBoxColorFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemSpawnEggFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackCustomNameToOverrideComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackEnchantmentNamesFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackMapIdFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackSpawnEggFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackTheFlatteningFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemStackUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.ItemWaterPotionFix;
/*      */ import net.minecraft.util.datafix.fixes.JigsawPropertiesFix;
/*      */ import net.minecraft.util.datafix.fixes.JigsawRotationFix;
/*      */ import net.minecraft.util.datafix.fixes.JukeboxTicksSinceSongStartedFix;
/*      */ import net.minecraft.util.datafix.fixes.LeavesFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyDimensionIdFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyDragonFightFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyHoverEventFix;
/*      */ import net.minecraft.util.datafix.fixes.LegacyWorldBorderFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelDataGeneratorOptionsFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelFlatGeneratorInfoFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelLegacyWorldGenSettingsFix;
/*      */ import net.minecraft.util.datafix.fixes.LevelUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.LockComponentPredicateFix;
/*      */ import net.minecraft.util.datafix.fixes.LodestoneCompassComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.MapBannerBlockPosFormatFix;
/*      */ import net.minecraft.util.datafix.fixes.MapIdFix;
/*      */ import net.minecraft.util.datafix.fixes.MemoryExpiryDataFix;
/*      */ import net.minecraft.util.datafix.fixes.MissingDimensionFix;
/*      */ import net.minecraft.util.datafix.fixes.MobEffectIdFix;
/*      */ import net.minecraft.util.datafix.fixes.MobSpawnerEntityIdentifiersFix;
/*      */ import net.minecraft.util.datafix.fixes.NamedEntityConvertUncheckedFix;
/*      */ import net.minecraft.util.datafix.fixes.NamedEntityWriteReadFix;
/*      */ import net.minecraft.util.datafix.fixes.NamespacedTypeRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.NewVillageFix;
/*      */ import net.minecraft.util.datafix.fixes.ObjectiveRenderTypeFix;
/*      */ import net.minecraft.util.datafix.fixes.OminousBannerBlockEntityRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.OminousBannerRarityFix;
/*      */ import net.minecraft.util.datafix.fixes.OminousBannerRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsAccessibilityOnboardFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsAddTextBackgroundFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsAmbientOcclusionFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsFancyGraphicsToGraphicsModeFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsForceVBOFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsGraphicsModeSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsKeyLwjgl3Fix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsKeyTranslationFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsLowerCaseLanguageFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsMenuBlurrinessFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsMusicToastFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsProgrammerArtFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsRenameFieldFix;
/*      */ import net.minecraft.util.datafix.fixes.OptionsSetGraphicsPresetToCustomFix;
/*      */ import net.minecraft.util.datafix.fixes.OverreachingTickFix;
/*      */ import net.minecraft.util.datafix.fixes.ParticleUnflatteningFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerEquipmentFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerHeadBlockProfileFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerRespawnDataFix;
/*      */ import net.minecraft.util.datafix.fixes.PlayerUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.PoiTypeRemoveFix;
/*      */ import net.minecraft.util.datafix.fixes.PoiTypeRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.PrimedTntBlockStateFixer;
/*      */ import net.minecraft.util.datafix.fixes.ProjectileStoredWeaponFix;
/*      */ import net.minecraft.util.datafix.fixes.RaidRenamesDataFix;
/*      */ import net.minecraft.util.datafix.fixes.RandomSequenceSettingsFix;
/*      */ import net.minecraft.util.datafix.fixes.RecipesFix;
/*      */ import net.minecraft.util.datafix.fixes.RecipesRenameningFix;
/*      */ import net.minecraft.util.datafix.fixes.RedstoneWireConnectionsFix;
/*      */ import net.minecraft.util.datafix.fixes.References;
/*      */ import net.minecraft.util.datafix.fixes.RemapChunkStatusFix;
/*      */ import net.minecraft.util.datafix.fixes.RemoveBlockEntityTagFix;
/*      */ import net.minecraft.util.datafix.fixes.RemoveEmptyItemInBrushableBlockFix;
/*      */ import net.minecraft.util.datafix.fixes.RemoveGolemGossipFix;
/*      */ import net.minecraft.util.datafix.fixes.RenameEnchantmentsFix;
/*      */ import net.minecraft.util.datafix.fixes.RenamedCoralFansFix;
/*      */ import net.minecraft.util.datafix.fixes.RenamedCoralFix;
/*      */ import net.minecraft.util.datafix.fixes.ReorganizePoi;
/*      */ import net.minecraft.util.datafix.fixes.SaddleEquipmentSlotFix;
/*      */ import net.minecraft.util.datafix.fixes.SavedDataFeaturePoolElementFix;
/*      */ import net.minecraft.util.datafix.fixes.SavedDataUUIDFix;
/*      */ import net.minecraft.util.datafix.fixes.ScoreboardDisplayNameFix;
/*      */ import net.minecraft.util.datafix.fixes.ScoreboardDisplaySlotFix;
/*      */ import net.minecraft.util.datafix.fixes.SignTextStrictJsonFix;
/*      */ import net.minecraft.util.datafix.fixes.SpawnerDataFix;
/*      */ import net.minecraft.util.datafix.fixes.StatsCounterFix;
/*      */ import net.minecraft.util.datafix.fixes.StatsRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.StriderGravityFix;
/*      */ import net.minecraft.util.datafix.fixes.StructureReferenceCountFix;
/*      */ import net.minecraft.util.datafix.fixes.StructureSettingsFlattenFix;
/*      */ import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
/*      */ import net.minecraft.util.datafix.fixes.TextComponentHoverAndClickEventFix;
/*      */ import net.minecraft.util.datafix.fixes.TextComponentStringifiedFlagsFix;
/*      */ import net.minecraft.util.datafix.fixes.ThrownPotionSplitFix;
/*      */ import net.minecraft.util.datafix.fixes.TippedArrowPotionToItemFix;
/*      */ import net.minecraft.util.datafix.fixes.TooltipDisplayComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.TrappedChestBlockEntityFix;
/*      */ import net.minecraft.util.datafix.fixes.TrialSpawnerConfigFix;
/*      */ import net.minecraft.util.datafix.fixes.TrialSpawnerConfigInRegistryFix;
/*      */ import net.minecraft.util.datafix.fixes.TridentAnimationFix;
/*      */ import net.minecraft.util.datafix.fixes.UnflattenTextComponentFix;
/*      */ import net.minecraft.util.datafix.fixes.VariantRenameFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerDataFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerFollowRangeFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerRebuildLevelAndXpFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerSetCanPickUpLootFix;
/*      */ import net.minecraft.util.datafix.fixes.VillagerTradeFix;
/*      */ import net.minecraft.util.datafix.fixes.WallPropertyFix;
/*      */ import net.minecraft.util.datafix.fixes.WeaponSmithChestLootTableFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldBorderWarningTimeFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldGenSettingsDisallowOldCustomWorldsFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldGenSettingsFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldGenSettingsHeightAndBiomeFix;
/*      */ import net.minecraft.util.datafix.fixes.WorldSpawnDataFix;
/*      */ import net.minecraft.util.datafix.fixes.WriteAndReadFix;
/*      */ import net.minecraft.util.datafix.fixes.WrittenBookPagesStrictJsonFix;
/*      */ import net.minecraft.util.datafix.fixes.ZombieVillagerRebuildXpFix;
/*      */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DataFixers
/*      */ {
/*  398 */   private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
/*  399 */   private static final BiFunction<Integer, Schema, Schema> SAME_NAMESPACED = NamespacedSchema::new;
/*  400 */   private static final DataFixerBuilder.Result DATA_FIXER = createFixerUpper();
/*      */ 
/*      */   
/*      */   public static final int BLENDING_VERSION = 4295;
/*      */ 
/*      */   
/*      */   public static DataFixer getDataFixer() {
/*  407 */     return DATA_FIXER.fixer();
/*      */   }
/*      */   
/*      */   private static DataFixerBuilder.Result createFixerUpper() {
/*  411 */     DataFixerBuilder dataFixerBuilder = new DataFixerBuilder(SharedConstants.getCurrentVersion().dataVersion().version());
/*  412 */     addFixers(dataFixerBuilder);
/*  413 */     return dataFixerBuilder.build();
/*      */   }
/*      */   
/*      */   public static CompletableFuture<?> optimize(Set<DSL.TypeReference> paramSet) {
/*  417 */     if (paramSet.isEmpty()) {
/*  418 */       return CompletableFuture.completedFuture(null);
/*      */     }
/*      */     
/*  421 */     ExecutorService executorService = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder())
/*  422 */         .setNameFormat("Datafixer Bootstrap")
/*  423 */         .setDaemon(true)
/*  424 */         .setPriority(1)
/*  425 */         .build());
/*  426 */     return DATA_FIXER.optimize(paramSet, executorService);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addFixers(DataFixerBuilder paramDataFixerBuilder) {
/*  434 */     paramDataFixerBuilder.addSchema(99, net.minecraft.util.datafix.schemas.V99::new);
/*      */ 
/*      */     
/*  437 */     Schema schema1 = paramDataFixerBuilder.addSchema(100, net.minecraft.util.datafix.schemas.V100::new);
/*  438 */     paramDataFixerBuilder.addFixer((DataFix)new EntityEquipmentToArmorAndHandFix(schema1));
/*      */     
/*  440 */     Schema schema2 = paramDataFixerBuilder.addSchema(101, SAME);
/*  441 */     paramDataFixerBuilder.addFixer((DataFix)new VillagerSetCanPickUpLootFix(schema2));
/*      */     
/*  443 */     Schema schema3 = paramDataFixerBuilder.addSchema(102, net.minecraft.util.datafix.schemas.V102::new);
/*  444 */     paramDataFixerBuilder.addFixer((DataFix)new ItemIdFix(schema3, true));
/*  445 */     paramDataFixerBuilder.addFixer((DataFix)new ItemPotionFix(schema3, false));
/*      */     
/*  447 */     Schema schema4 = paramDataFixerBuilder.addSchema(105, SAME);
/*  448 */     paramDataFixerBuilder.addFixer((DataFix)new ItemSpawnEggFix(schema4, true));
/*      */ 
/*      */     
/*  451 */     Schema schema5 = paramDataFixerBuilder.addSchema(106, net.minecraft.util.datafix.schemas.V106::new);
/*  452 */     paramDataFixerBuilder.addFixer((DataFix)new MobSpawnerEntityIdentifiersFix(schema5, true));
/*      */     
/*  454 */     Schema schema6 = paramDataFixerBuilder.addSchema(107, net.minecraft.util.datafix.schemas.V107::new);
/*  455 */     paramDataFixerBuilder.addFixer((DataFix)new EntityMinecartIdentifiersFix(schema6));
/*      */     
/*  457 */     Schema schema7 = paramDataFixerBuilder.addSchema(108, SAME);
/*  458 */     paramDataFixerBuilder.addFixer((DataFix)new EntityStringUuidFix(schema7, true));
/*      */     
/*  460 */     Schema schema8 = paramDataFixerBuilder.addSchema(109, SAME);
/*  461 */     paramDataFixerBuilder.addFixer((DataFix)new EntityHealthFix(schema8, true));
/*      */     
/*  463 */     Schema schema9 = paramDataFixerBuilder.addSchema(110, SAME);
/*  464 */     paramDataFixerBuilder.addFixer((DataFix)new EntityHorseSaddleFix(schema9, true));
/*      */     
/*  466 */     Schema schema10 = paramDataFixerBuilder.addSchema(111, SAME);
/*  467 */     paramDataFixerBuilder.addFixer((DataFix)new EntityPaintingItemFrameDirectionFix(schema10, true));
/*      */     
/*  469 */     Schema schema11 = paramDataFixerBuilder.addSchema(113, SAME);
/*  470 */     paramDataFixerBuilder.addFixer((DataFix)new EntityRedundantChanceTagsFix(schema11, true));
/*      */     
/*  472 */     Schema schema12 = paramDataFixerBuilder.addSchema(135, net.minecraft.util.datafix.schemas.V135::new);
/*  473 */     paramDataFixerBuilder.addFixer((DataFix)new EntityRidingToPassengersFix(schema12, true));
/*      */     
/*  475 */     Schema schema13 = paramDataFixerBuilder.addSchema(143, net.minecraft.util.datafix.schemas.V143::new);
/*  476 */     paramDataFixerBuilder.addFixer((DataFix)new EntityTippedArrowFix(schema13, true));
/*      */     
/*  478 */     Schema schema14 = paramDataFixerBuilder.addSchema(147, SAME);
/*  479 */     paramDataFixerBuilder.addFixer((DataFix)new EntityArmorStandSilentFix(schema14, true));
/*      */     
/*  481 */     Schema schema15 = paramDataFixerBuilder.addSchema(165, SAME);
/*      */     
/*  483 */     paramDataFixerBuilder.addFixer((DataFix)new SignTextStrictJsonFix(schema15));
/*  484 */     paramDataFixerBuilder.addFixer((DataFix)new WrittenBookPagesStrictJsonFix(schema15));
/*      */ 
/*      */     
/*  487 */     Schema schema16 = paramDataFixerBuilder.addSchema(501, net.minecraft.util.datafix.schemas.V501::new);
/*  488 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema16, "Add 1.10 entities fix", References.ENTITY));
/*      */     
/*  490 */     Schema schema17 = paramDataFixerBuilder.addSchema(502, SAME);
/*  491 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema17, "cooked_fished item renamer", paramString -> Objects.equals(NamespacedSchema.ensureNamespaced(paramString), "minecraft:cooked_fished") ? "minecraft:cooked_fish" : paramString));
/*      */ 
/*      */     
/*  494 */     paramDataFixerBuilder.addFixer((DataFix)new EntityZombieVillagerTypeFix(schema17, false));
/*      */     
/*  496 */     Schema schema18 = paramDataFixerBuilder.addSchema(505, SAME);
/*  497 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsForceVBOFix(schema18, false));
/*      */ 
/*      */     
/*  500 */     Schema schema19 = paramDataFixerBuilder.addSchema(700, net.minecraft.util.datafix.schemas.V700::new);
/*  501 */     paramDataFixerBuilder.addFixer((DataFix)new EntityElderGuardianSplitFix(schema19, true));
/*      */     
/*  503 */     Schema schema20 = paramDataFixerBuilder.addSchema(701, net.minecraft.util.datafix.schemas.V701::new);
/*  504 */     paramDataFixerBuilder.addFixer((DataFix)new EntitySkeletonSplitFix(schema20, true));
/*      */     
/*  506 */     Schema schema21 = paramDataFixerBuilder.addSchema(702, net.minecraft.util.datafix.schemas.V702::new);
/*  507 */     paramDataFixerBuilder.addFixer((DataFix)new EntityZombieSplitFix(schema21));
/*      */     
/*  509 */     Schema schema22 = paramDataFixerBuilder.addSchema(703, net.minecraft.util.datafix.schemas.V703::new);
/*  510 */     paramDataFixerBuilder.addFixer((DataFix)new EntityHorseSplitFix(schema22, true));
/*      */ 
/*      */     
/*  513 */     Schema schema23 = paramDataFixerBuilder.addSchema(704, net.minecraft.util.datafix.schemas.V704::new);
/*  514 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityIdFix(schema23, true));
/*      */     
/*  516 */     Schema schema24 = paramDataFixerBuilder.addSchema(705, net.minecraft.util.datafix.schemas.V705::new);
/*  517 */     paramDataFixerBuilder.addFixer((DataFix)new EntityIdFix(schema24, true));
/*      */     
/*  519 */     Schema schema25 = paramDataFixerBuilder.addSchema(804, SAME_NAMESPACED);
/*  520 */     paramDataFixerBuilder.addFixer((DataFix)new ItemBannerColorFix(schema25, true));
/*      */     
/*  522 */     Schema schema26 = paramDataFixerBuilder.addSchema(806, SAME_NAMESPACED);
/*  523 */     paramDataFixerBuilder.addFixer((DataFix)new ItemWaterPotionFix(schema26, false));
/*      */ 
/*      */     
/*  526 */     Schema schema27 = paramDataFixerBuilder.addSchema(808, net.minecraft.util.datafix.schemas.V808::new);
/*  527 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema27, "added shulker box", References.BLOCK_ENTITY));
/*      */     
/*  529 */     Schema schema28 = paramDataFixerBuilder.addSchema(808, 1, SAME_NAMESPACED);
/*  530 */     paramDataFixerBuilder.addFixer((DataFix)new EntityShulkerColorFix(schema28, false));
/*      */     
/*  532 */     Schema schema29 = paramDataFixerBuilder.addSchema(813, SAME_NAMESPACED);
/*  533 */     paramDataFixerBuilder.addFixer((DataFix)new ItemShulkerBoxColorFix(schema29, false));
/*  534 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityShulkerBoxColorFix(schema29, false));
/*      */     
/*  536 */     Schema schema30 = paramDataFixerBuilder.addSchema(816, SAME_NAMESPACED);
/*  537 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsLowerCaseLanguageFix(schema30, false));
/*      */ 
/*      */     
/*  540 */     Schema schema31 = paramDataFixerBuilder.addSchema(820, SAME_NAMESPACED);
/*  541 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema31, "totem item renamer", createRenamer("minecraft:totem", "minecraft:totem_of_undying")));
/*      */ 
/*      */     
/*  544 */     Schema schema32 = paramDataFixerBuilder.addSchema(1022, net.minecraft.util.datafix.schemas.V1022::new);
/*  545 */     paramDataFixerBuilder.addFixer((DataFix)new WriteAndReadFix(schema32, "added shoulder entities to players", References.PLAYER));
/*      */     
/*  547 */     Schema schema33 = paramDataFixerBuilder.addSchema(1125, net.minecraft.util.datafix.schemas.V1125::new);
/*  548 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkBedBlockEntityInjecterFix(schema33, true));
/*  549 */     paramDataFixerBuilder.addFixer((DataFix)new BedItemColorFix(schema33, false));
/*      */ 
/*      */     
/*  552 */     Schema schema34 = paramDataFixerBuilder.addSchema(1344, SAME_NAMESPACED);
/*  553 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsKeyLwjgl3Fix(schema34, false));
/*      */     
/*  555 */     Schema schema35 = paramDataFixerBuilder.addSchema(1446, SAME_NAMESPACED);
/*  556 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsKeyTranslationFix(schema35, false));
/*      */     
/*  558 */     Schema schema36 = paramDataFixerBuilder.addSchema(1450, SAME_NAMESPACED);
/*  559 */     paramDataFixerBuilder.addFixer((DataFix)new BlockStateStructureTemplateFix(schema36, false));
/*      */     
/*  561 */     Schema schema37 = paramDataFixerBuilder.addSchema(1451, net.minecraft.util.datafix.schemas.V1451::new);
/*  562 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema37, "AddTrappedChestFix", References.BLOCK_ENTITY));
/*      */     
/*  564 */     Schema schema38 = paramDataFixerBuilder.addSchema(1451, 1, net.minecraft.util.datafix.schemas.V1451_1::new);
/*  565 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkPalettedStorageFix(schema38, true));
/*      */ 
/*      */     
/*  568 */     Schema schema39 = paramDataFixerBuilder.addSchema(1451, 2, net.minecraft.util.datafix.schemas.V1451_2::new);
/*  569 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityBlockStateFix(schema39, true));
/*      */     
/*  571 */     Schema schema40 = paramDataFixerBuilder.addSchema(1451, 3, net.minecraft.util.datafix.schemas.V1451_3::new);
/*  572 */     paramDataFixerBuilder.addFixer((DataFix)new EntityBlockStateFix(schema40, true));
/*  573 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackMapIdFix(schema40, false));
/*      */     
/*  575 */     Schema schema41 = paramDataFixerBuilder.addSchema(1451, 4, net.minecraft.util.datafix.schemas.V1451_4::new);
/*  576 */     paramDataFixerBuilder.addFixer((DataFix)new BlockNameFlatteningFix(schema41, true));
/*  577 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackTheFlatteningFix(schema41, false));
/*      */     
/*  579 */     Schema schema42 = paramDataFixerBuilder.addSchema(1451, 5, net.minecraft.util.datafix.schemas.V1451_5::new);
/*      */     
/*  581 */     paramDataFixerBuilder.addFixer((DataFix)new RemoveBlockEntityTagFix(schema42, Set.of("minecraft:noteblock", "minecraft:flower_pot")));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  586 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackSpawnEggFix(schema42, false, "minecraft:spawn_egg"));
/*  587 */     paramDataFixerBuilder.addFixer((DataFix)new EntityWolfColorFix(schema42, false));
/*  588 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityBannerColorFix(schema42, false));
/*  589 */     paramDataFixerBuilder.addFixer((DataFix)new LevelFlatGeneratorInfoFix(schema42, false));
/*      */     
/*  591 */     Schema schema43 = paramDataFixerBuilder.addSchema(1451, 6, net.minecraft.util.datafix.schemas.V1451_6::new);
/*  592 */     paramDataFixerBuilder.addFixer((DataFix)new StatsCounterFix(schema43, true));
/*  593 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityJukeboxFix(schema43, false));
/*      */     
/*  595 */     Schema schema44 = paramDataFixerBuilder.addSchema(1451, 7, SAME_NAMESPACED);
/*  596 */     paramDataFixerBuilder.addFixer((DataFix)new VillagerTradeFix(schema44));
/*      */     
/*  598 */     Schema schema45 = paramDataFixerBuilder.addSchema(1456, SAME_NAMESPACED);
/*  599 */     paramDataFixerBuilder.addFixer((DataFix)new EntityItemFrameDirectionFix(schema45, false));
/*      */     
/*  601 */     Schema schema46 = paramDataFixerBuilder.addSchema(1458, net.minecraft.util.datafix.schemas.V1458::new);
/*  602 */     paramDataFixerBuilder.addFixer((DataFix)new EntityCustomNameToComponentFix(schema46));
/*  603 */     paramDataFixerBuilder.addFixer((DataFix)new ItemCustomNameToComponentFix(schema46));
/*  604 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityCustomNameToComponentFix(schema46));
/*      */     
/*  606 */     Schema schema47 = paramDataFixerBuilder.addSchema(1460, net.minecraft.util.datafix.schemas.V1460::new);
/*  607 */     paramDataFixerBuilder.addFixer((DataFix)new EntityPaintingMotiveFix(schema47, false));
/*      */     
/*  609 */     Schema schema48 = paramDataFixerBuilder.addSchema(1466, net.minecraft.util.datafix.schemas.V1466::new);
/*  610 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema48, "Add DUMMY block entity", References.BLOCK_ENTITY));
/*  611 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkToProtochunkFix(schema48, true));
/*      */ 
/*      */     
/*  614 */     Schema schema49 = paramDataFixerBuilder.addSchema(1470, net.minecraft.util.datafix.schemas.V1470::new);
/*  615 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema49, "Add 1.13 entities fix", References.ENTITY));
/*      */     
/*  617 */     Schema schema50 = paramDataFixerBuilder.addSchema(1474, SAME_NAMESPACED);
/*  618 */     paramDataFixerBuilder.addFixer((DataFix)new ColorlessShulkerEntityFix(schema50, false));
/*  619 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema50, "Colorless shulker block fixer", paramString -> Objects.equals(NamespacedSchema.ensureNamespaced(paramString), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : paramString));
/*  620 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema50, "Colorless shulker item fixer", paramString -> Objects.equals(NamespacedSchema.ensureNamespaced(paramString), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : paramString));
/*      */     
/*  622 */     Schema schema51 = paramDataFixerBuilder.addSchema(1475, SAME_NAMESPACED);
/*  623 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema51, "Flowing fixer", createRenamer(
/*  624 */             (Map<String, String>)ImmutableMap.of("minecraft:flowing_water", "minecraft:water", "minecraft:flowing_lava", "minecraft:lava"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  630 */     Schema schema52 = paramDataFixerBuilder.addSchema(1480, SAME_NAMESPACED);
/*  631 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema52, "Rename coral blocks", createRenamer(RenamedCoralFix.RENAMED_IDS)));
/*  632 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema52, "Rename coral items", createRenamer(RenamedCoralFix.RENAMED_IDS)));
/*      */     
/*  634 */     Schema schema53 = paramDataFixerBuilder.addSchema(1481, net.minecraft.util.datafix.schemas.V1481::new);
/*  635 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema53, "Add conduit", References.BLOCK_ENTITY));
/*      */     
/*  637 */     Schema schema54 = paramDataFixerBuilder.addSchema(1483, net.minecraft.util.datafix.schemas.V1483::new);
/*  638 */     paramDataFixerBuilder.addFixer((DataFix)new EntityPufferfishRenameFix(schema54, true));
/*  639 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema54, "Rename pufferfish egg item", createRenamer(EntityPufferfishRenameFix.RENAMED_IDS)));
/*      */     
/*  641 */     Schema schema55 = paramDataFixerBuilder.addSchema(1484, SAME_NAMESPACED);
/*  642 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema55, "Rename seagrass items", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
/*      */ 
/*      */ 
/*      */     
/*  646 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema55, "Rename seagrass blocks", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  652 */     paramDataFixerBuilder.addFixer((DataFix)new HeightmapRenamingFix(schema55, false));
/*      */ 
/*      */     
/*  655 */     Schema schema56 = paramDataFixerBuilder.addSchema(1486, net.minecraft.util.datafix.schemas.V1486::new);
/*  656 */     paramDataFixerBuilder.addFixer((DataFix)new EntityCodSalmonFix(schema56, true));
/*  657 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema56, "Rename cod/salmon egg items", createRenamer(EntityCodSalmonFix.RENAMED_EGG_IDS)));
/*      */     
/*  659 */     Schema schema57 = paramDataFixerBuilder.addSchema(1487, SAME_NAMESPACED);
/*  660 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema57, "Rename prismarine_brick(s)_* blocks", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
/*      */ 
/*      */ 
/*      */     
/*  664 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema57, "Rename prismarine_brick(s)_* items", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  669 */     Schema schema58 = paramDataFixerBuilder.addSchema(1488, net.minecraft.util.datafix.schemas.V1488::new);
/*  670 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema58, "Rename kelp/kelptop", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:kelp_top", "minecraft:kelp", "minecraft:kelp", "minecraft:kelp_plant"))));
/*      */ 
/*      */ 
/*      */     
/*  674 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema58, "Rename kelptop", createRenamer("minecraft:kelp_top", "minecraft:kelp")));
/*  675 */     paramDataFixerBuilder.addFixer((DataFix)new NamedEntityWriteReadFix(schema58, true, "Command block block entity custom name fix", References.BLOCK_ENTITY, "minecraft:command_block")
/*      */         {
/*      */           protected <T> Dynamic<T> fix(Dynamic<T> param1Dynamic) {
/*  678 */             return BlockEntityCustomNameToComponentFix.fixTagCustomName(param1Dynamic);
/*      */           }
/*      */         });
/*  681 */     paramDataFixerBuilder.addFixer(new DataFix(schema58, false)
/*      */         {
/*      */           protected TypeRewriteRule makeRule()
/*      */           {
/*  685 */             Type type = getInputSchema().getType(References.ENTITY);
/*  686 */             OpticFinder opticFinder1 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*  687 */             OpticFinder opticFinder2 = type.findField("CustomName");
/*  688 */             OpticFinder opticFinder3 = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*  689 */             return fixTypeEverywhereTyped("Command block minecart custom name fix", type, param1Typed -> {
/*      */                   String str = param1Typed.getOptional(param1OpticFinder1).orElse("");
/*      */ 
/*      */ 
/*      */                   
/*      */                   return !"minecraft:commandblock_minecart".equals(str) ? param1Typed : param1Typed.updateTyped(param1OpticFinder2, ());
/*      */                 });
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*  700 */     paramDataFixerBuilder.addFixer((DataFix)new IglooMetadataRemovalFix(schema58, false));
/*      */     
/*  702 */     Schema schema59 = paramDataFixerBuilder.addSchema(1490, SAME_NAMESPACED);
/*  703 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema59, "Rename melon_block", createRenamer("minecraft:melon_block", "minecraft:melon")));
/*  704 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema59, "Rename melon_block/melon/speckled_melon", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:melon_block", "minecraft:melon", "minecraft:melon", "minecraft:melon_slice", "minecraft:speckled_melon", "minecraft:glistering_melon_slice"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  710 */     Schema schema60 = paramDataFixerBuilder.addSchema(1492, SAME_NAMESPACED);
/*  711 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkStructuresTemplateRenameFix(schema60, false));
/*      */     
/*  713 */     Schema schema61 = paramDataFixerBuilder.addSchema(1494, SAME_NAMESPACED);
/*  714 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackEnchantmentNamesFix(schema61, false));
/*      */     
/*  716 */     Schema schema62 = paramDataFixerBuilder.addSchema(1496, SAME_NAMESPACED);
/*  717 */     paramDataFixerBuilder.addFixer((DataFix)new LeavesFix(schema62, false));
/*      */     
/*  719 */     Schema schema63 = paramDataFixerBuilder.addSchema(1500, SAME_NAMESPACED);
/*  720 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityKeepPacked(schema63, false));
/*      */     
/*  722 */     Schema schema64 = paramDataFixerBuilder.addSchema(1501, SAME_NAMESPACED);
/*  723 */     paramDataFixerBuilder.addFixer((DataFix)new AdvancementsFix(schema64, false));
/*      */     
/*  725 */     Schema schema65 = paramDataFixerBuilder.addSchema(1502, SAME_NAMESPACED);
/*  726 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema65, "Recipes fix", References.RECIPE, createRenamer(RecipesFix.RECIPES)));
/*      */     
/*  728 */     Schema schema66 = paramDataFixerBuilder.addSchema(1506, SAME_NAMESPACED);
/*  729 */     paramDataFixerBuilder.addFixer((DataFix)new LevelDataGeneratorOptionsFix(schema66, false));
/*      */     
/*  731 */     Schema schema67 = paramDataFixerBuilder.addSchema(1510, net.minecraft.util.datafix.schemas.V1510::new);
/*  732 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema67, "Block renamening fix", createRenamer(EntityTheRenameningFix.RENAMED_BLOCKS)));
/*  733 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema67, "Item renamening fix", createRenamer(EntityTheRenameningFix.RENAMED_ITEMS)));
/*  734 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema67, "Recipes renamening fix", References.RECIPE, createRenamer(RecipesRenameningFix.RECIPES)));
/*  735 */     paramDataFixerBuilder.addFixer((DataFix)new EntityTheRenameningFix(schema67, true));
/*  736 */     paramDataFixerBuilder.addFixer((DataFix)new StatsRenameFix(schema67, "SwimStatsRenameFix", (Map)ImmutableMap.of("minecraft:swim_one_cm", "minecraft:walk_on_water_one_cm", "minecraft:dive_one_cm", "minecraft:walk_under_water_one_cm")));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  741 */     Schema schema68 = paramDataFixerBuilder.addSchema(1514, SAME_NAMESPACED);
/*  742 */     paramDataFixerBuilder.addFixer((DataFix)new ScoreboardDisplayNameFix(schema68, "ObjectiveDisplayNameFix", References.OBJECTIVE));
/*  743 */     paramDataFixerBuilder.addFixer((DataFix)new ScoreboardDisplayNameFix(schema68, "TeamDisplayNameFix", References.TEAM));
/*  744 */     paramDataFixerBuilder.addFixer((DataFix)new ObjectiveRenderTypeFix(schema68));
/*      */     
/*  746 */     Schema schema69 = paramDataFixerBuilder.addSchema(1515, SAME_NAMESPACED);
/*  747 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema69, "Rename coral fan blocks", createRenamer(RenamedCoralFansFix.RENAMED_IDS)));
/*      */     
/*  749 */     Schema schema70 = paramDataFixerBuilder.addSchema(1624, SAME_NAMESPACED);
/*  750 */     paramDataFixerBuilder.addFixer((DataFix)new TrappedChestBlockEntityFix(schema70, false));
/*      */     
/*  752 */     Schema schema71 = paramDataFixerBuilder.addSchema(1800, net.minecraft.util.datafix.schemas.V1800::new);
/*  753 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema71, "Added 1.14 mobs fix", References.ENTITY));
/*  754 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema71, "Rename dye items", createRenamer(DyeItemRenameFix.RENAMED_IDS)));
/*      */     
/*  756 */     Schema schema72 = paramDataFixerBuilder.addSchema(1801, net.minecraft.util.datafix.schemas.V1801::new);
/*  757 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema72, "Added Illager Beast", References.ENTITY));
/*      */     
/*  759 */     Schema schema73 = paramDataFixerBuilder.addSchema(1802, SAME_NAMESPACED);
/*  760 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema73, "Rename sign blocks & stone slabs", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign", "minecraft:wall_sign", "minecraft:oak_wall_sign"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  765 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema73, "Rename sign item & stone slabs", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  770 */     Schema schema74 = paramDataFixerBuilder.addSchema(1803, SAME_NAMESPACED);
/*  771 */     paramDataFixerBuilder.addFixer((DataFix)new ItemLoreFix(schema74));
/*      */     
/*  773 */     Schema schema75 = paramDataFixerBuilder.addSchema(1904, net.minecraft.util.datafix.schemas.V1904::new);
/*  774 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema75, "Added Cats", References.ENTITY));
/*  775 */     paramDataFixerBuilder.addFixer((DataFix)new EntityCatSplitFix(schema75, false));
/*      */     
/*  777 */     Schema schema76 = paramDataFixerBuilder.addSchema(1905, SAME_NAMESPACED);
/*  778 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkStatusFix(schema76, false));
/*      */     
/*  780 */     Schema schema77 = paramDataFixerBuilder.addSchema(1906, net.minecraft.util.datafix.schemas.V1906::new);
/*  781 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema77, "Add POI Blocks", References.BLOCK_ENTITY));
/*      */     
/*  783 */     Schema schema78 = paramDataFixerBuilder.addSchema(1909, net.minecraft.util.datafix.schemas.V1909::new);
/*  784 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema78, "Add jigsaw", References.BLOCK_ENTITY));
/*      */     
/*  786 */     Schema schema79 = paramDataFixerBuilder.addSchema(1911, SAME_NAMESPACED);
/*  787 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkStatusFix2(schema79, false));
/*      */     
/*  789 */     Schema schema80 = paramDataFixerBuilder.addSchema(1914, SAME_NAMESPACED);
/*  790 */     paramDataFixerBuilder.addFixer((DataFix)new WeaponSmithChestLootTableFix(schema80, false));
/*      */     
/*  792 */     Schema schema81 = paramDataFixerBuilder.addSchema(1917, SAME_NAMESPACED);
/*  793 */     paramDataFixerBuilder.addFixer((DataFix)new CatTypeFix(schema81, false));
/*      */     
/*  795 */     Schema schema82 = paramDataFixerBuilder.addSchema(1918, SAME_NAMESPACED);
/*  796 */     paramDataFixerBuilder.addFixer((DataFix)new VillagerDataFix(schema82, "minecraft:villager"));
/*  797 */     paramDataFixerBuilder.addFixer((DataFix)new VillagerDataFix(schema82, "minecraft:zombie_villager"));
/*      */     
/*  799 */     Schema schema83 = paramDataFixerBuilder.addSchema(1920, net.minecraft.util.datafix.schemas.V1920::new);
/*  800 */     paramDataFixerBuilder.addFixer((DataFix)new NewVillageFix(schema83, false));
/*  801 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema83, "Add campfire", References.BLOCK_ENTITY));
/*      */     
/*  803 */     Schema schema84 = paramDataFixerBuilder.addSchema(1925, SAME_NAMESPACED);
/*  804 */     paramDataFixerBuilder.addFixer((DataFix)new MapIdFix(schema84));
/*      */     
/*  806 */     Schema schema85 = paramDataFixerBuilder.addSchema(1928, net.minecraft.util.datafix.schemas.V1928::new);
/*  807 */     paramDataFixerBuilder.addFixer((DataFix)new EntityRavagerRenameFix(schema85, true));
/*  808 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema85, "Rename ravager egg item", createRenamer(EntityRavagerRenameFix.RENAMED_IDS)));
/*      */     
/*  810 */     Schema schema86 = paramDataFixerBuilder.addSchema(1929, net.minecraft.util.datafix.schemas.V1929::new);
/*  811 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema86, "Add Wandering Trader and Trader Llama", References.ENTITY));
/*      */     
/*  813 */     Schema schema87 = paramDataFixerBuilder.addSchema(1931, net.minecraft.util.datafix.schemas.V1931::new);
/*  814 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema87, "Added Fox", References.ENTITY));
/*      */     
/*  816 */     Schema schema88 = paramDataFixerBuilder.addSchema(1936, SAME_NAMESPACED);
/*  817 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsAddTextBackgroundFix(schema88, false));
/*      */     
/*  819 */     Schema schema89 = paramDataFixerBuilder.addSchema(1946, SAME_NAMESPACED);
/*  820 */     paramDataFixerBuilder.addFixer((DataFix)new ReorganizePoi(schema89, false));
/*      */     
/*  822 */     Schema schema90 = paramDataFixerBuilder.addSchema(1948, SAME_NAMESPACED);
/*  823 */     paramDataFixerBuilder.addFixer((DataFix)new OminousBannerRenameFix(schema90));
/*      */     
/*  825 */     Schema schema91 = paramDataFixerBuilder.addSchema(1953, SAME_NAMESPACED);
/*  826 */     paramDataFixerBuilder.addFixer((DataFix)new OminousBannerBlockEntityRenameFix(schema91, false));
/*      */     
/*  828 */     Schema schema92 = paramDataFixerBuilder.addSchema(1955, SAME_NAMESPACED);
/*  829 */     paramDataFixerBuilder.addFixer((DataFix)new VillagerRebuildLevelAndXpFix(schema92, false));
/*  830 */     paramDataFixerBuilder.addFixer((DataFix)new ZombieVillagerRebuildXpFix(schema92, false));
/*      */     
/*  832 */     Schema schema93 = paramDataFixerBuilder.addSchema(1961, SAME_NAMESPACED);
/*  833 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkLightRemoveFix(schema93, false));
/*      */     
/*  835 */     Schema schema94 = paramDataFixerBuilder.addSchema(1963, SAME_NAMESPACED);
/*  836 */     paramDataFixerBuilder.addFixer((DataFix)new RemoveGolemGossipFix(schema94, false));
/*      */     
/*  838 */     Schema schema95 = paramDataFixerBuilder.addSchema(2100, net.minecraft.util.datafix.schemas.V2100::new);
/*  839 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema95, "Added Bee and Bee Stinger", References.ENTITY));
/*  840 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema95, "Add beehive", References.BLOCK_ENTITY));
/*  841 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema95, "Rename sugar recipe", References.RECIPE, createRenamer("minecraft:sugar", "minecraft:sugar_from_sugar_cane")));
/*  842 */     paramDataFixerBuilder.addFixer((DataFix)new AdvancementsRenameFix(schema95, false, "Rename sugar recipe advancement", createRenamer("minecraft:recipes/misc/sugar", "minecraft:recipes/misc/sugar_from_sugar_cane")));
/*      */     
/*  844 */     Schema schema96 = paramDataFixerBuilder.addSchema(2202, SAME_NAMESPACED);
/*  845 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkBiomeFix(schema96, false));
/*      */     
/*  847 */     Schema schema97 = paramDataFixerBuilder.addSchema(2209, SAME_NAMESPACED);
/*  848 */     UnaryOperator<String> unaryOperator1 = createRenamer("minecraft:bee_hive", "minecraft:beehive");
/*  849 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema97, "Rename bee_hive item to beehive", unaryOperator1));
/*  850 */     paramDataFixerBuilder.addFixer((DataFix)new PoiTypeRenameFix(schema97, "Rename bee_hive poi to beehive", unaryOperator1));
/*  851 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema97, "Rename bee_hive block to beehive", unaryOperator1));
/*      */     
/*  853 */     Schema schema98 = paramDataFixerBuilder.addSchema(2211, SAME_NAMESPACED);
/*  854 */     paramDataFixerBuilder.addFixer((DataFix)new StructureReferenceCountFix(schema98, false));
/*      */     
/*  856 */     Schema schema99 = paramDataFixerBuilder.addSchema(2218, SAME_NAMESPACED);
/*  857 */     paramDataFixerBuilder.addFixer((DataFix)new ForcePoiRebuild(schema99, false));
/*      */     
/*  859 */     Schema schema100 = paramDataFixerBuilder.addSchema(2501, net.minecraft.util.datafix.schemas.V2501::new);
/*  860 */     paramDataFixerBuilder.addFixer((DataFix)new FurnaceRecipeFix(schema100, true));
/*      */     
/*  862 */     Schema schema101 = paramDataFixerBuilder.addSchema(2502, net.minecraft.util.datafix.schemas.V2502::new);
/*  863 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema101, "Added Hoglin", References.ENTITY));
/*      */     
/*  865 */     Schema schema102 = paramDataFixerBuilder.addSchema(2503, SAME_NAMESPACED);
/*  866 */     paramDataFixerBuilder.addFixer((DataFix)new WallPropertyFix(schema102, false));
/*  867 */     paramDataFixerBuilder.addFixer((DataFix)new AdvancementsRenameFix(schema102, false, "Composter category change", createRenamer("minecraft:recipes/misc/composter", "minecraft:recipes/decorations/composter")));
/*      */     
/*  869 */     Schema schema103 = paramDataFixerBuilder.addSchema(2505, net.minecraft.util.datafix.schemas.V2505::new);
/*  870 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema103, "Added Piglin", References.ENTITY));
/*  871 */     paramDataFixerBuilder.addFixer((DataFix)new MemoryExpiryDataFix(schema103, "minecraft:villager"));
/*      */     
/*  873 */     Schema schema104 = paramDataFixerBuilder.addSchema(2508, SAME_NAMESPACED);
/*  874 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema104, "Renamed fungi items to fungus", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
/*      */ 
/*      */ 
/*      */     
/*  878 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema104, "Renamed fungi blocks to fungus", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  883 */     Schema schema105 = paramDataFixerBuilder.addSchema(2509, net.minecraft.util.datafix.schemas.V2509::new);
/*  884 */     paramDataFixerBuilder.addFixer((DataFix)new EntityZombifiedPiglinRenameFix(schema105));
/*  885 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema105, "Rename zombie pigman egg item", createRenamer(EntityZombifiedPiglinRenameFix.RENAMED_IDS)));
/*      */     
/*  887 */     Schema schema106 = paramDataFixerBuilder.addSchema(2511, SAME_NAMESPACED);
/*  888 */     paramDataFixerBuilder.addFixer((DataFix)new EntityProjectileOwnerFix(schema106));
/*      */     
/*  890 */     Schema schema107 = paramDataFixerBuilder.addSchema(2511, 1, net.minecraft.util.datafix.schemas.V2511_1::new);
/*  891 */     paramDataFixerBuilder.addFixer((DataFix)new NamedEntityConvertUncheckedFix(schema107, "SplashPotionItemFieldRenameFix", References.ENTITY, "minecraft:potion"));
/*      */     
/*  893 */     Schema schema108 = paramDataFixerBuilder.addSchema(2514, SAME_NAMESPACED);
/*  894 */     paramDataFixerBuilder.addFixer((DataFix)new EntityUUIDFix(schema108));
/*  895 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityUUIDFix(schema108));
/*  896 */     paramDataFixerBuilder.addFixer((DataFix)new PlayerUUIDFix(schema108));
/*  897 */     paramDataFixerBuilder.addFixer((DataFix)new LevelUUIDFix(schema108));
/*  898 */     paramDataFixerBuilder.addFixer((DataFix)new SavedDataUUIDFix(schema108));
/*  899 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackUUIDFix(schema108));
/*      */     
/*  901 */     Schema schema109 = paramDataFixerBuilder.addSchema(2516, SAME_NAMESPACED);
/*  902 */     paramDataFixerBuilder.addFixer((DataFix)new GossipUUIDFix(schema109, "minecraft:villager"));
/*  903 */     paramDataFixerBuilder.addFixer((DataFix)new GossipUUIDFix(schema109, "minecraft:zombie_villager"));
/*      */     
/*  905 */     Schema schema110 = paramDataFixerBuilder.addSchema(2518, SAME_NAMESPACED);
/*  906 */     paramDataFixerBuilder.addFixer((DataFix)new JigsawPropertiesFix(schema110, false));
/*  907 */     paramDataFixerBuilder.addFixer((DataFix)new JigsawRotationFix(schema110));
/*      */     
/*  909 */     Schema schema111 = paramDataFixerBuilder.addSchema(2519, net.minecraft.util.datafix.schemas.V2519::new);
/*  910 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema111, "Added Strider", References.ENTITY));
/*      */     
/*  912 */     Schema schema112 = paramDataFixerBuilder.addSchema(2522, net.minecraft.util.datafix.schemas.V2522::new);
/*  913 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema112, "Added Zoglin", References.ENTITY));
/*      */     
/*  915 */     Schema schema113 = paramDataFixerBuilder.addSchema(2523, SAME_NAMESPACED);
/*  916 */     paramDataFixerBuilder.addFixer((DataFix)new AttributesRenameLegacy(schema113, "Attribute renames", createRenamerNoNamespace((Map<String, String>)ImmutableMap.builder()
/*  917 */             .put("generic.maxHealth", "minecraft:generic.max_health")
/*  918 */             .put("Max Health", "minecraft:generic.max_health")
/*      */             
/*  920 */             .put("zombie.spawnReinforcements", "minecraft:zombie.spawn_reinforcements")
/*  921 */             .put("Spawn Reinforcements Chance", "minecraft:zombie.spawn_reinforcements")
/*      */             
/*  923 */             .put("horse.jumpStrength", "minecraft:horse.jump_strength")
/*  924 */             .put("Jump Strength", "minecraft:horse.jump_strength")
/*      */             
/*  926 */             .put("generic.followRange", "minecraft:generic.follow_range")
/*  927 */             .put("Follow Range", "minecraft:generic.follow_range")
/*      */             
/*  929 */             .put("generic.knockbackResistance", "minecraft:generic.knockback_resistance")
/*  930 */             .put("Knockback Resistance", "minecraft:generic.knockback_resistance")
/*      */             
/*  932 */             .put("generic.movementSpeed", "minecraft:generic.movement_speed")
/*  933 */             .put("Movement Speed", "minecraft:generic.movement_speed")
/*      */             
/*  935 */             .put("generic.flyingSpeed", "minecraft:generic.flying_speed")
/*  936 */             .put("Flying Speed", "minecraft:generic.flying_speed")
/*      */             
/*  938 */             .put("generic.attackDamage", "minecraft:generic.attack_damage")
/*  939 */             .put("generic.attackKnockback", "minecraft:generic.attack_knockback")
/*  940 */             .put("generic.attackSpeed", "minecraft:generic.attack_speed")
/*  941 */             .put("generic.armorToughness", "minecraft:generic.armor_toughness")
/*  942 */             .build())));
/*      */ 
/*      */     
/*  945 */     Schema schema114 = paramDataFixerBuilder.addSchema(2527, SAME_NAMESPACED);
/*  946 */     paramDataFixerBuilder.addFixer((DataFix)new BitStorageAlignFix(schema114));
/*      */     
/*  948 */     Schema schema115 = paramDataFixerBuilder.addSchema(2528, SAME_NAMESPACED);
/*  949 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema115, "Rename soul fire torch and soul fire lantern", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
/*      */ 
/*      */ 
/*      */     
/*  953 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema115, "Rename soul fire torch and soul fire lantern", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_wall_torch", "minecraft:soul_wall_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  959 */     Schema schema116 = paramDataFixerBuilder.addSchema(2529, SAME_NAMESPACED);
/*  960 */     paramDataFixerBuilder.addFixer((DataFix)new StriderGravityFix(schema116, false));
/*      */     
/*  962 */     Schema schema117 = paramDataFixerBuilder.addSchema(2531, SAME_NAMESPACED);
/*  963 */     paramDataFixerBuilder.addFixer((DataFix)new RedstoneWireConnectionsFix(schema117));
/*      */     
/*  965 */     Schema schema118 = paramDataFixerBuilder.addSchema(2533, SAME_NAMESPACED);
/*  966 */     paramDataFixerBuilder.addFixer((DataFix)new VillagerFollowRangeFix(schema118));
/*      */     
/*  968 */     Schema schema119 = paramDataFixerBuilder.addSchema(2535, SAME_NAMESPACED);
/*  969 */     paramDataFixerBuilder.addFixer((DataFix)new EntityShulkerRotationFix(schema119));
/*      */ 
/*      */     
/*  972 */     Schema schema120 = paramDataFixerBuilder.addSchema(2537, SAME_NAMESPACED);
/*  973 */     paramDataFixerBuilder.addFixer((DataFix)new LegacyDimensionIdFix(schema120));
/*      */     
/*  975 */     Schema schema121 = paramDataFixerBuilder.addSchema(2538, SAME_NAMESPACED);
/*  976 */     paramDataFixerBuilder.addFixer((DataFix)new LevelLegacyWorldGenSettingsFix(schema121));
/*      */     
/*  978 */     Schema schema122 = paramDataFixerBuilder.addSchema(2550, SAME_NAMESPACED);
/*  979 */     paramDataFixerBuilder.addFixer((DataFix)new WorldGenSettingsFix(schema122));
/*      */     
/*  981 */     Schema schema123 = paramDataFixerBuilder.addSchema(2551, net.minecraft.util.datafix.schemas.V2551::new);
/*  982 */     paramDataFixerBuilder.addFixer((DataFix)new WriteAndReadFix(schema123, "add types to WorldGenData", References.WORLD_GEN_SETTINGS));
/*      */     
/*  984 */     Schema schema124 = paramDataFixerBuilder.addSchema(2552, SAME_NAMESPACED);
/*  985 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema124, "Nether biome rename", References.BIOME, createRenamer("minecraft:nether", "minecraft:nether_wastes")));
/*      */ 
/*      */     
/*  988 */     Schema schema125 = paramDataFixerBuilder.addSchema(2553, SAME_NAMESPACED);
/*  989 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema125, "Biomes fix", References.BIOME, createRenamer(BiomeFix.BIOMES)));
/*      */ 
/*      */     
/*  992 */     Schema schema126 = paramDataFixerBuilder.addSchema(2556, SAME_NAMESPACED);
/*  993 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsFancyGraphicsToGraphicsModeFix(schema126));
/*      */     
/*  995 */     Schema schema127 = paramDataFixerBuilder.addSchema(2558, SAME_NAMESPACED);
/*  996 */     paramDataFixerBuilder.addFixer((DataFix)new MissingDimensionFix(schema127, false));
/*  997 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsRenameFieldFix(schema127, false, "Rename swapHands setting", "key_key.swapHands", "key_key.swapOffhand"));
/*      */     
/*  999 */     Schema schema128 = paramDataFixerBuilder.addSchema(2568, net.minecraft.util.datafix.schemas.V2568::new);
/* 1000 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema128, "Added Piglin Brute", References.ENTITY));
/*      */     
/* 1002 */     Schema schema129 = paramDataFixerBuilder.addSchema(2571, net.minecraft.util.datafix.schemas.V2571::new);
/* 1003 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema129, "Added Goat", References.ENTITY));
/*      */     
/* 1005 */     Schema schema130 = paramDataFixerBuilder.addSchema(2679, SAME_NAMESPACED);
/* 1006 */     paramDataFixerBuilder.addFixer((DataFix)new CauldronRenameFix(schema130, false));
/*      */     
/* 1008 */     Schema schema131 = paramDataFixerBuilder.addSchema(2680, SAME_NAMESPACED);
/* 1009 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema131, "Renamed grass path item to dirt path", createRenamer("minecraft:grass_path", "minecraft:dirt_path")));
/* 1010 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema131, "Renamed grass path block to dirt path", createRenamer("minecraft:grass_path", "minecraft:dirt_path")));
/*      */     
/* 1012 */     Schema schema132 = paramDataFixerBuilder.addSchema(2684, net.minecraft.util.datafix.schemas.V2684::new);
/* 1013 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema132, "Added Sculk Sensor", References.BLOCK_ENTITY));
/*      */     
/* 1015 */     Schema schema133 = paramDataFixerBuilder.addSchema(2686, net.minecraft.util.datafix.schemas.V2686::new);
/* 1016 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema133, "Added Axolotl", References.ENTITY));
/*      */     
/* 1018 */     Schema schema134 = paramDataFixerBuilder.addSchema(2688, net.minecraft.util.datafix.schemas.V2688::new);
/* 1019 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema134, "Added Glow Squid", References.ENTITY));
/* 1020 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema134, "Added Glow Item Frame", References.ENTITY));
/*      */     
/* 1022 */     Schema schema135 = paramDataFixerBuilder.addSchema(2690, SAME_NAMESPACED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1044 */     ImmutableMap immutableMap1 = ImmutableMap.builder().put("minecraft:weathered_copper_block", "minecraft:oxidized_copper_block").put("minecraft:semi_weathered_copper_block", "minecraft:weathered_copper_block").put("minecraft:lightly_weathered_copper_block", "minecraft:exposed_copper_block").put("minecraft:weathered_cut_copper", "minecraft:oxidized_cut_copper").put("minecraft:semi_weathered_cut_copper", "minecraft:weathered_cut_copper").put("minecraft:lightly_weathered_cut_copper", "minecraft:exposed_cut_copper").put("minecraft:weathered_cut_copper_stairs", "minecraft:oxidized_cut_copper_stairs").put("minecraft:semi_weathered_cut_copper_stairs", "minecraft:weathered_cut_copper_stairs").put("minecraft:lightly_weathered_cut_copper_stairs", "minecraft:exposed_cut_copper_stairs").put("minecraft:weathered_cut_copper_slab", "minecraft:oxidized_cut_copper_slab").put("minecraft:semi_weathered_cut_copper_slab", "minecraft:weathered_cut_copper_slab").put("minecraft:lightly_weathered_cut_copper_slab", "minecraft:exposed_cut_copper_slab").put("minecraft:waxed_semi_weathered_copper", "minecraft:waxed_weathered_copper").put("minecraft:waxed_lightly_weathered_copper", "minecraft:waxed_exposed_copper").put("minecraft:waxed_semi_weathered_cut_copper", "minecraft:waxed_weathered_cut_copper").put("minecraft:waxed_lightly_weathered_cut_copper", "minecraft:waxed_exposed_cut_copper").put("minecraft:waxed_semi_weathered_cut_copper_stairs", "minecraft:waxed_weathered_cut_copper_stairs").put("minecraft:waxed_lightly_weathered_cut_copper_stairs", "minecraft:waxed_exposed_cut_copper_stairs").put("minecraft:waxed_semi_weathered_cut_copper_slab", "minecraft:waxed_weathered_cut_copper_slab").put("minecraft:waxed_lightly_weathered_cut_copper_slab", "minecraft:waxed_exposed_cut_copper_slab").build();
/*      */     
/* 1046 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema135, "Renamed copper block items to new oxidized terms", createRenamer((Map<String, String>)immutableMap1)));
/* 1047 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema135, "Renamed copper blocks to new oxidized terms", createRenamer((Map<String, String>)immutableMap1)));
/*      */     
/* 1049 */     Schema schema136 = paramDataFixerBuilder.addSchema(2691, SAME_NAMESPACED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1055 */     ImmutableMap immutableMap2 = ImmutableMap.builder().put("minecraft:waxed_copper", "minecraft:waxed_copper_block").put("minecraft:oxidized_copper_block", "minecraft:oxidized_copper").put("minecraft:weathered_copper_block", "minecraft:weathered_copper").put("minecraft:exposed_copper_block", "minecraft:exposed_copper").build();
/*      */     
/* 1057 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema136, "Rename copper item suffixes", createRenamer((Map<String, String>)immutableMap2)));
/* 1058 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema136, "Rename copper blocks suffixes", createRenamer((Map<String, String>)immutableMap2)));
/*      */     
/* 1060 */     Schema schema137 = paramDataFixerBuilder.addSchema(2693, SAME_NAMESPACED);
/* 1061 */     paramDataFixerBuilder.addFixer((DataFix)new AddFlagIfNotPresentFix(schema137, References.WORLD_GEN_SETTINGS, "has_increased_height_already", false));
/*      */     
/* 1063 */     Schema schema138 = paramDataFixerBuilder.addSchema(2696, SAME_NAMESPACED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1082 */     ImmutableMap immutableMap3 = ImmutableMap.builder().put("minecraft:grimstone", "minecraft:deepslate").put("minecraft:grimstone_slab", "minecraft:cobbled_deepslate_slab").put("minecraft:grimstone_stairs", "minecraft:cobbled_deepslate_stairs").put("minecraft:grimstone_wall", "minecraft:cobbled_deepslate_wall").put("minecraft:polished_grimstone", "minecraft:polished_deepslate").put("minecraft:polished_grimstone_slab", "minecraft:polished_deepslate_slab").put("minecraft:polished_grimstone_stairs", "minecraft:polished_deepslate_stairs").put("minecraft:polished_grimstone_wall", "minecraft:polished_deepslate_wall").put("minecraft:grimstone_tiles", "minecraft:deepslate_tiles").put("minecraft:grimstone_tile_slab", "minecraft:deepslate_tile_slab").put("minecraft:grimstone_tile_stairs", "minecraft:deepslate_tile_stairs").put("minecraft:grimstone_tile_wall", "minecraft:deepslate_tile_wall").put("minecraft:grimstone_bricks", "minecraft:deepslate_bricks").put("minecraft:grimstone_brick_slab", "minecraft:deepslate_brick_slab").put("minecraft:grimstone_brick_stairs", "minecraft:deepslate_brick_stairs").put("minecraft:grimstone_brick_wall", "minecraft:deepslate_brick_wall").put("minecraft:chiseled_grimstone", "minecraft:chiseled_deepslate").build();
/*      */     
/* 1084 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema138, "Renamed grimstone block items to deepslate", createRenamer((Map<String, String>)immutableMap3)));
/* 1085 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema138, "Renamed grimstone blocks to deepslate", createRenamer((Map<String, String>)immutableMap3)));
/*      */     
/* 1087 */     Schema schema139 = paramDataFixerBuilder.addSchema(2700, SAME_NAMESPACED);
/* 1088 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema139, "Renamed cave vines blocks", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:cave_vines_head", "minecraft:cave_vines", "minecraft:cave_vines_body", "minecraft:cave_vines_plant"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1093 */     Schema schema140 = paramDataFixerBuilder.addSchema(2701, SAME_NAMESPACED);
/* 1094 */     paramDataFixerBuilder.addFixer((DataFix)new SavedDataFeaturePoolElementFix(schema140));
/*      */     
/* 1096 */     Schema schema141 = paramDataFixerBuilder.addSchema(2702, SAME_NAMESPACED);
/* 1097 */     paramDataFixerBuilder.addFixer((DataFix)new AbstractArrowPickupFix(schema141));
/*      */     
/* 1099 */     Schema schema142 = paramDataFixerBuilder.addSchema(2704, net.minecraft.util.datafix.schemas.V2704::new);
/* 1100 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema142, "Added Goat", References.ENTITY));
/*      */     
/* 1102 */     Schema schema143 = paramDataFixerBuilder.addSchema(2707, net.minecraft.util.datafix.schemas.V2707::new);
/* 1103 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema143, "Added Marker", References.ENTITY));
/* 1104 */     paramDataFixerBuilder.addFixer((DataFix)new AddFlagIfNotPresentFix(schema143, References.WORLD_GEN_SETTINGS, "has_increased_height_already", true));
/*      */     
/* 1106 */     Schema schema144 = paramDataFixerBuilder.addSchema(2710, SAME_NAMESPACED);
/* 1107 */     paramDataFixerBuilder.addFixer((DataFix)new StatsRenameFix(schema144, "Renamed play_one_minute stat to play_time", (Map)ImmutableMap.of("minecraft:play_one_minute", "minecraft:play_time")));
/*      */     
/* 1109 */     Schema schema145 = paramDataFixerBuilder.addSchema(2717, SAME_NAMESPACED);
/* 1110 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema145, "Rename azalea_leaves_flowers", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:azalea_leaves_flowers", "minecraft:flowering_azalea_leaves"))));
/*      */ 
/*      */     
/* 1113 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema145, "Rename azalea_leaves_flowers items", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:azalea_leaves_flowers", "minecraft:flowering_azalea_leaves"))));
/*      */ 
/*      */ 
/*      */     
/* 1117 */     Schema schema146 = paramDataFixerBuilder.addSchema(2825, SAME_NAMESPACED);
/* 1118 */     paramDataFixerBuilder.addFixer((DataFix)new AddFlagIfNotPresentFix(schema146, References.WORLD_GEN_SETTINGS, "has_increased_height_already", false));
/*      */     
/* 1120 */     Schema schema147 = paramDataFixerBuilder.addSchema(2831, net.minecraft.util.datafix.schemas.V2831::new);
/* 1121 */     paramDataFixerBuilder.addFixer((DataFix)new SpawnerDataFix(schema147));
/*      */     
/* 1123 */     Schema schema148 = paramDataFixerBuilder.addSchema(2832, net.minecraft.util.datafix.schemas.V2832::new);
/* 1124 */     paramDataFixerBuilder.addFixer((DataFix)new WorldGenSettingsHeightAndBiomeFix(schema148));
/* 1125 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkHeightAndBiomeFix(schema148));
/*      */     
/* 1127 */     Schema schema149 = paramDataFixerBuilder.addSchema(2833, SAME_NAMESPACED);
/* 1128 */     paramDataFixerBuilder.addFixer((DataFix)new WorldGenSettingsDisallowOldCustomWorldsFix(schema149));
/*      */     
/* 1130 */     Schema schema150 = paramDataFixerBuilder.addSchema(2838, SAME_NAMESPACED);
/* 1131 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema150, "Caves and Cliffs biome renames", References.BIOME, createRenamer((Map<String, String>)CavesAndCliffsRenames.RENAMES)));
/*      */     
/* 1133 */     Schema schema151 = paramDataFixerBuilder.addSchema(2841, SAME_NAMESPACED);
/* 1134 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkProtoTickListFix(schema151));
/*      */     
/* 1136 */     Schema schema152 = paramDataFixerBuilder.addSchema(2842, net.minecraft.util.datafix.schemas.V2842::new);
/* 1137 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkRenamesFix(schema152));
/*      */     
/* 1139 */     Schema schema153 = paramDataFixerBuilder.addSchema(2843, SAME_NAMESPACED);
/* 1140 */     paramDataFixerBuilder.addFixer((DataFix)new OverreachingTickFix(schema153));
/* 1141 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema153, "Remove Deep Warm Ocean", References.BIOME, createRenamer("minecraft:deep_warm_ocean", "minecraft:warm_ocean")));
/*      */     
/* 1143 */     Schema schema154 = paramDataFixerBuilder.addSchema(2846, SAME_NAMESPACED);
/* 1144 */     paramDataFixerBuilder.addFixer((DataFix)new AdvancementsRenameFix(schema154, false, "Rename some C&C part 2 advancements", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:husbandry/play_jukebox_in_meadows", "minecraft:adventure/play_jukebox_in_meadows", "minecraft:adventure/caves_and_cliff", "minecraft:adventure/fall_from_world_height", "minecraft:adventure/ride_strider_in_overworld_lava", "minecraft:nether/ride_strider_in_overworld_lava"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1150 */     Schema schema155 = paramDataFixerBuilder.addSchema(2852, SAME_NAMESPACED);
/* 1151 */     paramDataFixerBuilder.addFixer((DataFix)new WorldGenSettingsDisallowOldCustomWorldsFix(schema155));
/*      */     
/* 1153 */     Schema schema156 = paramDataFixerBuilder.addSchema(2967, SAME_NAMESPACED);
/* 1154 */     paramDataFixerBuilder.addFixer((DataFix)new StructureSettingsFlattenFix(schema156));
/*      */     
/* 1156 */     Schema schema157 = paramDataFixerBuilder.addSchema(2970, SAME_NAMESPACED);
/* 1157 */     paramDataFixerBuilder.addFixer((DataFix)new StructuresBecomeConfiguredFix(schema157));
/*      */     
/* 1159 */     Schema schema158 = paramDataFixerBuilder.addSchema(3076, net.minecraft.util.datafix.schemas.V3076::new);
/* 1160 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema158, "Added Sculk Catalyst", References.BLOCK_ENTITY));
/*      */     
/* 1162 */     Schema schema159 = paramDataFixerBuilder.addSchema(3077, SAME_NAMESPACED);
/* 1163 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkDeleteIgnoredLightDataFix(schema159));
/*      */     
/* 1165 */     Schema schema160 = paramDataFixerBuilder.addSchema(3078, net.minecraft.util.datafix.schemas.V3078::new);
/* 1166 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema160, "Added Frog", References.ENTITY));
/* 1167 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema160, "Added Tadpole", References.ENTITY));
/* 1168 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema160, "Added Sculk Shrieker", References.BLOCK_ENTITY));
/*      */     
/* 1170 */     Schema schema161 = paramDataFixerBuilder.addSchema(3081, net.minecraft.util.datafix.schemas.V3081::new);
/* 1171 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema161, "Added Warden", References.ENTITY));
/*      */     
/* 1173 */     Schema schema162 = paramDataFixerBuilder.addSchema(3082, net.minecraft.util.datafix.schemas.V3082::new);
/* 1174 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema162, "Added Chest Boat", References.ENTITY));
/*      */     
/* 1176 */     Schema schema163 = paramDataFixerBuilder.addSchema(3083, net.minecraft.util.datafix.schemas.V3083::new);
/* 1177 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema163, "Added Allay", References.ENTITY));
/*      */     
/* 1179 */     Schema schema164 = paramDataFixerBuilder.addSchema(3084, SAME_NAMESPACED);
/* 1180 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema164, "game_event_renames_3084", References.GAME_EVENT_NAME, createRenamer((Map<String, String>)ImmutableMap.builder()
/* 1181 */             .put("minecraft:block_press", "minecraft:block_activate")
/* 1182 */             .put("minecraft:block_switch", "minecraft:block_activate")
/* 1183 */             .put("minecraft:block_unpress", "minecraft:block_deactivate")
/* 1184 */             .put("minecraft:block_unswitch", "minecraft:block_deactivate")
/* 1185 */             .put("minecraft:drinking_finish", "minecraft:drink")
/* 1186 */             .put("minecraft:elytra_free_fall", "minecraft:elytra_glide")
/* 1187 */             .put("minecraft:entity_damaged", "minecraft:entity_damage")
/* 1188 */             .put("minecraft:entity_dying", "minecraft:entity_die")
/* 1189 */             .put("minecraft:entity_killed", "minecraft:entity_die")
/* 1190 */             .put("minecraft:mob_interact", "minecraft:entity_interact")
/* 1191 */             .put("minecraft:ravager_roar", "minecraft:entity_roar")
/* 1192 */             .put("minecraft:ring_bell", "minecraft:block_change")
/* 1193 */             .put("minecraft:shulker_close", "minecraft:container_close")
/* 1194 */             .put("minecraft:shulker_open", "minecraft:container_open")
/* 1195 */             .put("minecraft:wolf_shaking", "minecraft:entity_shake")
/* 1196 */             .build())));
/*      */ 
/*      */     
/* 1199 */     Schema schema165 = paramDataFixerBuilder.addSchema(3086, SAME_NAMESPACED);
/* 1200 */     Objects.requireNonNull((Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> { paramInt2ObjectOpenHashMap.defaultReturnValue("minecraft:tabby"); paramInt2ObjectOpenHashMap.put(0, "minecraft:tabby"); paramInt2ObjectOpenHashMap.put(1, "minecraft:black"); paramInt2ObjectOpenHashMap.put(2, "minecraft:red"); paramInt2ObjectOpenHashMap.put(3, "minecraft:siamese"); paramInt2ObjectOpenHashMap.put(4, "minecraft:british"); paramInt2ObjectOpenHashMap.put(5, "minecraft:calico"); paramInt2ObjectOpenHashMap.put(6, "minecraft:persian"); paramInt2ObjectOpenHashMap.put(7, "minecraft:ragdoll"); paramInt2ObjectOpenHashMap.put(8, "minecraft:white"); paramInt2ObjectOpenHashMap.put(9, "minecraft:jellie"); paramInt2ObjectOpenHashMap.put(10, "minecraft:all_black"); })); paramDataFixerBuilder.addFixer((DataFix)new EntityVariantFix(schema165, "Change cat variant type", References.ENTITY, "minecraft:cat", "CatType", (Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> { paramInt2ObjectOpenHashMap.defaultReturnValue("minecraft:tabby"); paramInt2ObjectOpenHashMap.put(0, "minecraft:tabby"); paramInt2ObjectOpenHashMap.put(1, "minecraft:black"); paramInt2ObjectOpenHashMap.put(2, "minecraft:red"); paramInt2ObjectOpenHashMap.put(3, "minecraft:siamese"); paramInt2ObjectOpenHashMap.put(4, "minecraft:british"); paramInt2ObjectOpenHashMap.put(5, "minecraft:calico"); paramInt2ObjectOpenHashMap.put(6, "minecraft:persian"); paramInt2ObjectOpenHashMap.put(7, "minecraft:ragdoll"); paramInt2ObjectOpenHashMap.put(8, "minecraft:white"); paramInt2ObjectOpenHashMap.put(9, "minecraft:jellie"); paramInt2ObjectOpenHashMap.put(10, "minecraft:all_black"); })::get));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1227 */     ImmutableMap immutableMap4 = ImmutableMap.builder().put("textures/entity/cat/tabby.png", "minecraft:tabby").put("textures/entity/cat/black.png", "minecraft:black").put("textures/entity/cat/red.png", "minecraft:red").put("textures/entity/cat/siamese.png", "minecraft:siamese").put("textures/entity/cat/british_shorthair.png", "minecraft:british").put("textures/entity/cat/calico.png", "minecraft:calico").put("textures/entity/cat/persian.png", "minecraft:persian").put("textures/entity/cat/ragdoll.png", "minecraft:ragdoll").put("textures/entity/cat/white.png", "minecraft:white").put("textures/entity/cat/jellie.png", "minecraft:jellie").put("textures/entity/cat/all_black.png", "minecraft:all_black").build();
/* 1228 */     paramDataFixerBuilder.addFixer((DataFix)new CriteriaRenameFix(schema165, "Migrate cat variant advancement", "minecraft:husbandry/complete_catalogue", paramString -> (String)paramImmutableMap.getOrDefault(paramString, paramString)));
/*      */     
/* 1230 */     Schema schema166 = paramDataFixerBuilder.addSchema(3087, SAME_NAMESPACED);
/* 1231 */     Objects.requireNonNull((Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> { paramInt2ObjectOpenHashMap.put(0, "minecraft:temperate"); paramInt2ObjectOpenHashMap.put(1, "minecraft:warm"); paramInt2ObjectOpenHashMap.put(2, "minecraft:cold"); })); paramDataFixerBuilder.addFixer((DataFix)new EntityVariantFix(schema166, "Change frog variant type", References.ENTITY, "minecraft:frog", "Variant", (Int2ObjectOpenHashMap)Util.make(new Int2ObjectOpenHashMap(), paramInt2ObjectOpenHashMap -> { paramInt2ObjectOpenHashMap.put(0, "minecraft:temperate"); paramInt2ObjectOpenHashMap.put(1, "minecraft:warm"); paramInt2ObjectOpenHashMap.put(2, "minecraft:cold"); })::get));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1237 */     Schema schema167 = paramDataFixerBuilder.addSchema(3090, SAME_NAMESPACED);
/* 1238 */     paramDataFixerBuilder.addFixer((DataFix)new EntityFieldsRenameFix(schema167, "EntityPaintingFieldsRenameFix", "minecraft:painting", 
/* 1239 */           Map.of("Motive", "variant", "Facing", "facing")));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1245 */     Schema schema168 = paramDataFixerBuilder.addSchema(3093, SAME_NAMESPACED);
/* 1246 */     paramDataFixerBuilder.addFixer((DataFix)new EntityGoatMissingStateFix(schema168));
/*      */     
/* 1248 */     Schema schema169 = paramDataFixerBuilder.addSchema(3094, SAME_NAMESPACED);
/* 1249 */     paramDataFixerBuilder.addFixer((DataFix)new GoatHornIdFix(schema169));
/*      */     
/* 1251 */     Schema schema170 = paramDataFixerBuilder.addSchema(3097, SAME_NAMESPACED);
/* 1252 */     paramDataFixerBuilder.addFixer((DataFix)new FilteredBooksFix(schema170));
/* 1253 */     paramDataFixerBuilder.addFixer((DataFix)new FilteredSignsFix(schema170));
/* 1254 */     Map<String, String> map = Map.of("minecraft:british", "minecraft:british_shorthair");
/* 1255 */     paramDataFixerBuilder.addFixer((DataFix)new VariantRenameFix(schema170, "Rename british shorthair", References.ENTITY, "minecraft:cat", map));
/* 1256 */     paramDataFixerBuilder.addFixer((DataFix)new CriteriaRenameFix(schema170, "Migrate cat variant advancement for british shorthair", "minecraft:husbandry/complete_catalogue", paramString -> (String)paramMap.getOrDefault(paramString, paramString)));
/* 1257 */     Objects.requireNonNull(Set.of("minecraft:unemployed", "minecraft:nitwit")); paramDataFixerBuilder.addFixer((DataFix)new PoiTypeRemoveFix(schema170, "Remove unpopulated villager PoI types", Set.of("minecraft:unemployed", "minecraft:nitwit")::contains));
/*      */     
/* 1259 */     Schema schema171 = paramDataFixerBuilder.addSchema(3108, SAME_NAMESPACED);
/* 1260 */     paramDataFixerBuilder.addFixer((DataFix)new BlendingDataRemoveFromNetherEndFix(schema171));
/*      */     
/* 1262 */     Schema schema172 = paramDataFixerBuilder.addSchema(3201, SAME_NAMESPACED);
/* 1263 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsProgrammerArtFix(schema172));
/*      */     
/* 1265 */     Schema schema173 = paramDataFixerBuilder.addSchema(3202, net.minecraft.util.datafix.schemas.V3202::new);
/* 1266 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema173, "Added Hanging Sign", References.BLOCK_ENTITY));
/*      */     
/* 1268 */     Schema schema174 = paramDataFixerBuilder.addSchema(3203, net.minecraft.util.datafix.schemas.V3203::new);
/* 1269 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema174, "Added Camel", References.ENTITY));
/*      */     
/* 1271 */     Schema schema175 = paramDataFixerBuilder.addSchema(3204, net.minecraft.util.datafix.schemas.V3204::new);
/* 1272 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema175, "Added Chiseled Bookshelf", References.BLOCK_ENTITY));
/*      */     
/* 1274 */     Schema schema176 = paramDataFixerBuilder.addSchema(3209, SAME_NAMESPACED);
/*      */     
/* 1276 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackSpawnEggFix(schema176, false, "minecraft:pig_spawn_egg"));
/*      */     
/* 1278 */     Schema schema177 = paramDataFixerBuilder.addSchema(3214, SAME_NAMESPACED);
/* 1279 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsAmbientOcclusionFix(schema177));
/*      */     
/* 1281 */     Schema schema178 = paramDataFixerBuilder.addSchema(3319, SAME_NAMESPACED);
/* 1282 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsAccessibilityOnboardFix(schema178));
/*      */     
/* 1284 */     Schema schema179 = paramDataFixerBuilder.addSchema(3322, SAME_NAMESPACED);
/* 1285 */     paramDataFixerBuilder.addFixer((DataFix)new EffectDurationFix(schema179));
/*      */     
/* 1287 */     Schema schema180 = paramDataFixerBuilder.addSchema(3325, net.minecraft.util.datafix.schemas.V3325::new);
/* 1288 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema180, "Added displays", References.ENTITY));
/*      */     
/* 1290 */     Schema schema181 = paramDataFixerBuilder.addSchema(3326, net.minecraft.util.datafix.schemas.V3326::new);
/* 1291 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema181, "Added Sniffer", References.ENTITY));
/*      */     
/* 1293 */     Schema schema182 = paramDataFixerBuilder.addSchema(3327, net.minecraft.util.datafix.schemas.V3327::new);
/* 1294 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema182, "Archaeology", References.BLOCK_ENTITY));
/*      */     
/* 1296 */     Schema schema183 = paramDataFixerBuilder.addSchema(3328, net.minecraft.util.datafix.schemas.V3328::new);
/* 1297 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema183, "Added interaction", References.ENTITY));
/*      */     
/* 1299 */     Schema schema184 = paramDataFixerBuilder.addSchema(3438, net.minecraft.util.datafix.schemas.V3438::new);
/* 1300 */     paramDataFixerBuilder.addFixer(BlockEntityRenameFix.create(schema184, "Rename Suspicious Sand to Brushable Block", createRenamer("minecraft:suspicious_sand", "minecraft:brushable_block")));
/* 1301 */     paramDataFixerBuilder.addFixer((DataFix)new EntityBrushableBlockFieldsRenameFix(schema184));
/* 1302 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema184, "Pottery shard renaming", createRenamer(
/* 1303 */             (Map<String, String>)ImmutableMap.of("minecraft:pottery_shard_archer", "minecraft:archer_pottery_shard", "minecraft:pottery_shard_prize", "minecraft:prize_pottery_shard", "minecraft:pottery_shard_arms_up", "minecraft:arms_up_pottery_shard", "minecraft:pottery_shard_skull", "minecraft:skull_pottery_shard"))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1309 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema184, "Added calibrated sculk sensor", References.BLOCK_ENTITY));
/*      */     
/* 1311 */     Schema schema185 = paramDataFixerBuilder.addSchema(3439, net.minecraft.util.datafix.schemas.V3439::new);
/* 1312 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntitySignDoubleSidedEditableTextFix(schema185, "Updated sign text format for Signs", "minecraft:sign"));
/*      */     
/* 1314 */     Schema schema186 = paramDataFixerBuilder.addSchema(3439, 1, net.minecraft.util.datafix.schemas.V3439_1::new);
/* 1315 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntitySignDoubleSidedEditableTextFix(schema186, "Updated sign text format for Hanging Signs", "minecraft:hanging_sign"));
/*      */     
/* 1317 */     Schema schema187 = paramDataFixerBuilder.addSchema(3440, SAME_NAMESPACED);
/* 1318 */     paramDataFixerBuilder.addFixer((DataFix)new NamespacedTypeRenameFix(schema187, "Replace experimental 1.20 overworld", References.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, createRenamer("minecraft:overworld_update_1_20", "minecraft:overworld")));
/* 1319 */     paramDataFixerBuilder.addFixer((DataFix)new FeatureFlagRemoveFix(schema187, "Remove 1.20 feature toggle", Set.of("minecraft:update_1_20")));
/*      */     
/* 1321 */     Schema schema188 = paramDataFixerBuilder.addSchema(3447, SAME_NAMESPACED);
/* 1322 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema188, "Pottery shard item renaming to Pottery sherd", createRenamer(
/* 1323 */             (Map<String, String>)Stream.<String>of(new String[] {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 "minecraft:angler_pottery_shard", "minecraft:archer_pottery_shard", "minecraft:arms_up_pottery_shard", "minecraft:blade_pottery_shard", "minecraft:brewer_pottery_shard", "minecraft:burn_pottery_shard", "minecraft:danger_pottery_shard", "minecraft:explorer_pottery_shard", "minecraft:friend_pottery_shard", "minecraft:heart_pottery_shard",
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 "minecraft:heartbreak_pottery_shard", "minecraft:howl_pottery_shard", "minecraft:miner_pottery_shard", "minecraft:mourner_pottery_shard", "minecraft:plenty_pottery_shard", "minecraft:prize_pottery_shard", "minecraft:sheaf_pottery_shard", "minecraft:shelter_pottery_shard", "minecraft:skull_pottery_shard", "minecraft:snort_pottery_shard"
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1344 */               }).collect(Collectors.toMap(
/* 1345 */                 Function.identity(), paramString -> paramString.replace("_pottery_shard", "_pottery_sherd"))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1350 */     Schema schema189 = paramDataFixerBuilder.addSchema(3448, net.minecraft.util.datafix.schemas.V3448::new);
/* 1351 */     paramDataFixerBuilder.addFixer((DataFix)new DecoratedPotFieldRenameFix(schema189));
/*      */     
/* 1353 */     Schema schema190 = paramDataFixerBuilder.addSchema(3450, SAME_NAMESPACED);
/* 1354 */     paramDataFixerBuilder.addFixer((DataFix)new RemapChunkStatusFix(schema190, "Remove liquid_carvers and heightmap chunk statuses", createRenamer(Map.of("minecraft:liquid_carvers", "minecraft:carvers", "minecraft:heightmaps", "minecraft:spawn"))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1359 */     Schema schema191 = paramDataFixerBuilder.addSchema(3451, SAME_NAMESPACED);
/* 1360 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkDeleteLightFix(schema191));
/*      */     
/* 1362 */     Schema schema192 = paramDataFixerBuilder.addSchema(3459, SAME_NAMESPACED);
/* 1363 */     paramDataFixerBuilder.addFixer((DataFix)new LegacyDragonFightFix(schema192));
/*      */     
/* 1365 */     Schema schema193 = paramDataFixerBuilder.addSchema(3564, SAME_NAMESPACED);
/* 1366 */     paramDataFixerBuilder.addFixer((DataFix)new DropInvalidSignDataFix(schema193, "minecraft:sign"));
/*      */     
/* 1368 */     Schema schema194 = paramDataFixerBuilder.addSchema(3564, 1, SAME_NAMESPACED);
/* 1369 */     paramDataFixerBuilder.addFixer((DataFix)new DropInvalidSignDataFix(schema194, "minecraft:hanging_sign"));
/*      */     
/* 1371 */     Schema schema195 = paramDataFixerBuilder.addSchema(3565, SAME_NAMESPACED);
/* 1372 */     paramDataFixerBuilder.addFixer((DataFix)new RandomSequenceSettingsFix(schema195));
/*      */     
/* 1374 */     Schema schema196 = paramDataFixerBuilder.addSchema(3566, SAME_NAMESPACED);
/* 1375 */     paramDataFixerBuilder.addFixer((DataFix)new ScoreboardDisplaySlotFix(schema196));
/*      */     
/* 1377 */     Schema schema197 = paramDataFixerBuilder.addSchema(3568, SAME_NAMESPACED);
/* 1378 */     paramDataFixerBuilder.addFixer((DataFix)new MobEffectIdFix(schema197));
/*      */     
/* 1380 */     Schema schema198 = paramDataFixerBuilder.addSchema(3682, net.minecraft.util.datafix.schemas.V3682::new);
/* 1381 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema198, "Added Crafter", References.BLOCK_ENTITY));
/*      */     
/* 1383 */     Schema schema199 = paramDataFixerBuilder.addSchema(3683, net.minecraft.util.datafix.schemas.V3683::new);
/* 1384 */     paramDataFixerBuilder.addFixer((DataFix)new PrimedTntBlockStateFixer(schema199));
/*      */     
/* 1386 */     Schema schema200 = paramDataFixerBuilder.addSchema(3685, net.minecraft.util.datafix.schemas.V3685::new);
/* 1387 */     paramDataFixerBuilder.addFixer((DataFix)new FixProjectileStoredItem(schema200));
/*      */     
/* 1389 */     Schema schema201 = paramDataFixerBuilder.addSchema(3689, net.minecraft.util.datafix.schemas.V3689::new);
/* 1390 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema201, "Added Breeze", References.ENTITY));
/* 1391 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema201, "Added Trial Spawner", References.BLOCK_ENTITY));
/*      */     
/* 1393 */     Schema schema202 = paramDataFixerBuilder.addSchema(3692, SAME_NAMESPACED);
/* 1394 */     UnaryOperator<String> unaryOperator2 = createRenamer(Map.of("minecraft:grass", "minecraft:short_grass"));
/* 1395 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema202, "Rename grass block to short_grass", unaryOperator2));
/* 1396 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema202, "Rename grass item to short_grass", unaryOperator2));
/*      */     
/* 1398 */     Schema schema203 = paramDataFixerBuilder.addSchema(3799, net.minecraft.util.datafix.schemas.V3799::new);
/* 1399 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema203, "Added Armadillo", References.ENTITY));
/*      */     
/* 1401 */     Schema schema204 = paramDataFixerBuilder.addSchema(3800, SAME_NAMESPACED);
/* 1402 */     UnaryOperator<String> unaryOperator3 = createRenamer(Map.of("minecraft:scute", "minecraft:turtle_scute"));
/* 1403 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema204, "Rename scute item to turtle_scute", unaryOperator3));
/*      */     
/* 1405 */     Schema schema205 = paramDataFixerBuilder.addSchema(3803, SAME_NAMESPACED);
/* 1406 */     paramDataFixerBuilder.addFixer((DataFix)new RenameEnchantmentsFix(schema205, "Rename sweeping enchant to sweeping_edge", Map.of("minecraft:sweeping", "minecraft:sweeping_edge")));
/*      */     
/* 1408 */     Schema schema206 = paramDataFixerBuilder.addSchema(3807, net.minecraft.util.datafix.schemas.V3807::new);
/* 1409 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema206, "Added Vault", References.BLOCK_ENTITY));
/*      */     
/* 1411 */     Schema schema207 = paramDataFixerBuilder.addSchema(3807, 1, SAME_NAMESPACED);
/* 1412 */     paramDataFixerBuilder.addFixer((DataFix)new MapBannerBlockPosFormatFix(schema207));
/*      */     
/* 1414 */     Schema schema208 = paramDataFixerBuilder.addSchema(3808, net.minecraft.util.datafix.schemas.V3808::new);
/* 1415 */     paramDataFixerBuilder.addFixer((DataFix)new HorseBodyArmorItemFix(schema208, "minecraft:horse", "ArmorItem", true));
/*      */     
/* 1417 */     Schema schema209 = paramDataFixerBuilder.addSchema(3808, 1, net.minecraft.util.datafix.schemas.V3808_1::new);
/* 1418 */     paramDataFixerBuilder.addFixer((DataFix)new HorseBodyArmorItemFix(schema209, "minecraft:llama", "DecorItem", false));
/*      */     
/* 1420 */     Schema schema210 = paramDataFixerBuilder.addSchema(3808, 2, net.minecraft.util.datafix.schemas.V3808_2::new);
/* 1421 */     paramDataFixerBuilder.addFixer((DataFix)new HorseBodyArmorItemFix(schema210, "minecraft:trader_llama", "DecorItem", false));
/*      */     
/* 1423 */     Schema schema211 = paramDataFixerBuilder.addSchema(3809, SAME_NAMESPACED);
/* 1424 */     paramDataFixerBuilder.addFixer((DataFix)new ChestedHorsesInventoryZeroIndexingFix(schema211));
/*      */     
/* 1426 */     Schema schema212 = paramDataFixerBuilder.addSchema(3812, SAME_NAMESPACED);
/* 1427 */     paramDataFixerBuilder.addFixer((DataFix)new FixWolfHealth(schema212));
/*      */     
/* 1429 */     Schema schema213 = paramDataFixerBuilder.addSchema(3813, net.minecraft.util.datafix.schemas.V3813::new);
/* 1430 */     paramDataFixerBuilder.addFixer((DataFix)new BlockPosFormatAndRenamesFix(schema213));
/*      */     
/* 1432 */     Schema schema214 = paramDataFixerBuilder.addSchema(3814, SAME_NAMESPACED);
/* 1433 */     paramDataFixerBuilder.addFixer((DataFix)new AttributesRenameLegacy(schema214, "Rename jump strength attribute", createRenamer("minecraft:horse.jump_strength", "minecraft:generic.jump_strength")));
/*      */     
/* 1435 */     Schema schema215 = paramDataFixerBuilder.addSchema(3816, net.minecraft.util.datafix.schemas.V3816::new);
/* 1436 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema215, "Added Bogged", References.ENTITY));
/*      */     
/* 1438 */     Schema schema216 = paramDataFixerBuilder.addSchema(3818, net.minecraft.util.datafix.schemas.V3818::new);
/* 1439 */     paramDataFixerBuilder.addFixer((DataFix)new BeehiveFieldRenameFix(schema216));
/* 1440 */     paramDataFixerBuilder.addFixer((DataFix)new EmptyItemInHotbarFix(schema216));
/*      */     
/* 1442 */     Schema schema217 = paramDataFixerBuilder.addSchema(3818, 1, SAME_NAMESPACED);
/* 1443 */     paramDataFixerBuilder.addFixer((DataFix)new BannerPatternFormatFix(schema217));
/*      */     
/* 1445 */     Schema schema218 = paramDataFixerBuilder.addSchema(3818, 2, SAME_NAMESPACED);
/* 1446 */     paramDataFixerBuilder.addFixer((DataFix)new TippedArrowPotionToItemFix(schema218));
/*      */     
/* 1448 */     Schema schema219 = paramDataFixerBuilder.addSchema(3818, 3, net.minecraft.util.datafix.schemas.V3818_3::new);
/*      */     
/* 1450 */     paramDataFixerBuilder.addFixer((DataFix)new WriteAndReadFix(schema219, "Inject data component types", References.DATA_COMPONENTS));
/*      */     
/* 1452 */     Schema schema220 = paramDataFixerBuilder.addSchema(3818, 4, net.minecraft.util.datafix.schemas.V3818_4::new);
/* 1453 */     paramDataFixerBuilder.addFixer((DataFix)new ParticleUnflatteningFix(schema220));
/*      */     
/* 1455 */     Schema schema221 = paramDataFixerBuilder.addSchema(3818, 5, net.minecraft.util.datafix.schemas.V3818_5::new);
/* 1456 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackComponentizationFix(schema221));
/*      */     
/* 1458 */     Schema schema222 = paramDataFixerBuilder.addSchema(3818, 6, SAME_NAMESPACED);
/* 1459 */     paramDataFixerBuilder.addFixer((DataFix)new AreaEffectCloudPotionFix(schema222));
/*      */     
/* 1461 */     Schema schema223 = paramDataFixerBuilder.addSchema(3820, SAME_NAMESPACED);
/* 1462 */     paramDataFixerBuilder.addFixer((DataFix)new PlayerHeadBlockProfileFix(schema223));
/* 1463 */     paramDataFixerBuilder.addFixer((DataFix)new LodestoneCompassComponentFix(schema223));
/*      */     
/* 1465 */     Schema schema224 = paramDataFixerBuilder.addSchema(3825, net.minecraft.util.datafix.schemas.V3825::new);
/* 1466 */     paramDataFixerBuilder.addFixer((DataFix)new ItemStackCustomNameToOverrideComponentFix(schema224));
/* 1467 */     paramDataFixerBuilder.addFixer((DataFix)new BannerEntityCustomNameToOverrideComponentFix(schema224));
/* 1468 */     paramDataFixerBuilder.addFixer((DataFix)new TrialSpawnerConfigFix(schema224));
/* 1469 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema224, "Added Ominous Item Spawner", References.ENTITY));
/*      */     
/* 1471 */     Schema schema225 = paramDataFixerBuilder.addSchema(3828, SAME_NAMESPACED);
/* 1472 */     paramDataFixerBuilder.addFixer((DataFix)new EmptyItemInVillagerTradeFix(schema225));
/*      */     
/* 1474 */     Schema schema226 = paramDataFixerBuilder.addSchema(3833, SAME_NAMESPACED);
/* 1475 */     paramDataFixerBuilder.addFixer((DataFix)new RemoveEmptyItemInBrushableBlockFix(schema226));
/*      */     
/* 1477 */     Schema schema227 = paramDataFixerBuilder.addSchema(3938, net.minecraft.util.datafix.schemas.V3938::new);
/* 1478 */     paramDataFixerBuilder.addFixer((DataFix)new ProjectileStoredWeaponFix(schema227));
/*      */     
/* 1480 */     Schema schema228 = paramDataFixerBuilder.addSchema(3939, SAME_NAMESPACED);
/* 1481 */     paramDataFixerBuilder.addFixer((DataFix)new FeatureFlagRemoveFix(schema228, "Remove 1.21 feature toggle", Set.of("minecraft:update_1_21")));
/*      */     
/* 1483 */     Schema schema229 = paramDataFixerBuilder.addSchema(3943, SAME_NAMESPACED);
/* 1484 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsMenuBlurrinessFix(schema229));
/*      */     
/* 1486 */     Schema schema230 = paramDataFixerBuilder.addSchema(3945, SAME_NAMESPACED);
/* 1487 */     paramDataFixerBuilder.addFixer((DataFix)new AttributeModifierIdFix(schema230));
/* 1488 */     paramDataFixerBuilder.addFixer((DataFix)new JukeboxTicksSinceSongStartedFix(schema230));
/*      */     
/* 1490 */     Schema schema231 = paramDataFixerBuilder.addSchema(4054, SAME_NAMESPACED);
/* 1491 */     paramDataFixerBuilder.addFixer((DataFix)new OminousBannerRarityFix(schema231));
/*      */     
/* 1493 */     Schema schema232 = paramDataFixerBuilder.addSchema(4055, SAME_NAMESPACED);
/* 1494 */     paramDataFixerBuilder.addFixer((DataFix)new AttributeIdPrefixFix(schema232));
/*      */     
/* 1496 */     Schema schema233 = paramDataFixerBuilder.addSchema(4057, SAME_NAMESPACED);
/* 1497 */     paramDataFixerBuilder.addFixer((DataFix)new CarvingStepRemoveFix(schema233));
/*      */     
/* 1499 */     Schema schema234 = paramDataFixerBuilder.addSchema(4059, net.minecraft.util.datafix.schemas.V4059::new);
/* 1500 */     paramDataFixerBuilder.addFixer((DataFix)new FoodToConsumableFix(schema234));
/*      */     
/* 1502 */     Schema schema235 = paramDataFixerBuilder.addSchema(4061, SAME_NAMESPACED);
/* 1503 */     paramDataFixerBuilder.addFixer((DataFix)new TrialSpawnerConfigInRegistryFix(schema235));
/*      */     
/* 1505 */     Schema schema236 = paramDataFixerBuilder.addSchema(4064, SAME_NAMESPACED);
/* 1506 */     paramDataFixerBuilder.addFixer((DataFix)new FireResistantToDamageResistantComponentFix(schema236));
/*      */     
/* 1508 */     Schema schema237 = paramDataFixerBuilder.addSchema(4067, net.minecraft.util.datafix.schemas.V4067::new);
/* 1509 */     paramDataFixerBuilder.addFixer((DataFix)new BoatSplitFix(schema237));
/* 1510 */     paramDataFixerBuilder.addFixer((DataFix)new FeatureFlagRemoveFix(schema237, "Remove Bundle experimental feature flag", Set.of("minecraft:bundle")));
/*      */     
/* 1512 */     Schema schema238 = paramDataFixerBuilder.addSchema(4068, SAME_NAMESPACED);
/* 1513 */     paramDataFixerBuilder.addFixer((DataFix)new LockComponentPredicateFix(schema238));
/* 1514 */     paramDataFixerBuilder.addFixer((DataFix)new ContainerBlockEntityLockPredicateFix(schema238));
/*      */     
/* 1516 */     Schema schema239 = paramDataFixerBuilder.addSchema(4070, net.minecraft.util.datafix.schemas.V4070::new);
/* 1517 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema239, "Added Pale Oak Boat and Pale Oak Chest Boat", References.ENTITY));
/*      */     
/* 1519 */     Schema schema240 = paramDataFixerBuilder.addSchema(4071, net.minecraft.util.datafix.schemas.V4071::new);
/* 1520 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema240, "Added Creaking", References.ENTITY));
/* 1521 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema240, "Added Creaking Heart", References.BLOCK_ENTITY));
/*      */     
/* 1523 */     Schema schema241 = paramDataFixerBuilder.addSchema(4081, SAME_NAMESPACED);
/* 1524 */     paramDataFixerBuilder.addFixer((DataFix)new EntitySalmonSizeFix(schema241));
/*      */     
/* 1526 */     Schema schema242 = paramDataFixerBuilder.addSchema(4173, SAME_NAMESPACED);
/* 1527 */     paramDataFixerBuilder.addFixer((DataFix)new EntityFieldsRenameFix(schema242, "Rename TNT Minecart fuse", "minecraft:tnt_minecart", 
/* 1528 */           Map.of("TNTFuse", "fuse")));
/*      */ 
/*      */     
/* 1531 */     Schema schema243 = paramDataFixerBuilder.addSchema(4175, SAME_NAMESPACED);
/* 1532 */     paramDataFixerBuilder.addFixer((DataFix)new EquippableAssetRenameFix(schema243));
/* 1533 */     paramDataFixerBuilder.addFixer((DataFix)new CustomModelDataExpandFix(schema243));
/*      */     
/* 1535 */     Schema schema244 = paramDataFixerBuilder.addSchema(4176, SAME_NAMESPACED);
/* 1536 */     paramDataFixerBuilder.addFixer((DataFix)new InvalidBlockEntityLockFix(schema244));
/* 1537 */     paramDataFixerBuilder.addFixer((DataFix)new InvalidLockComponentFix(schema244));
/*      */     
/* 1539 */     Schema schema245 = paramDataFixerBuilder.addSchema(4180, SAME_NAMESPACED);
/* 1540 */     paramDataFixerBuilder.addFixer((DataFix)new FeatureFlagRemoveFix(schema245, "Remove Winter Drop toggle", Set.of("minecraft:winter_drop")));
/*      */     
/* 1542 */     Schema schema246 = paramDataFixerBuilder.addSchema(4181, SAME_NAMESPACED);
/* 1543 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityFurnaceBurnTimeFix(schema246, "minecraft:furnace"));
/* 1544 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityFurnaceBurnTimeFix(schema246, "minecraft:smoker"));
/* 1545 */     paramDataFixerBuilder.addFixer((DataFix)new BlockEntityFurnaceBurnTimeFix(schema246, "minecraft:blast_furnace"));
/*      */ 
/*      */     
/* 1548 */     Schema schema247 = paramDataFixerBuilder.addSchema(4187, SAME_NAMESPACED);
/* 1549 */     paramDataFixerBuilder.addFixer((DataFix)new EntityAttributeBaseValueFix(schema247, "Villager follow range fix undo", "minecraft:villager", "minecraft:follow_range", paramDouble -> (paramDouble == 48.0D) ? 16.0D : paramDouble));
/* 1550 */     paramDataFixerBuilder.addFixer((DataFix)new EntityAttributeBaseValueFix(schema247, "Bee follow range fix", "minecraft:bee", "minecraft:follow_range", paramDouble -> (paramDouble == 48.0D) ? 16.0D : paramDouble));
/* 1551 */     paramDataFixerBuilder.addFixer((DataFix)new EntityAttributeBaseValueFix(schema247, "Allay follow range fix", "minecraft:allay", "minecraft:follow_range", paramDouble -> (paramDouble == 48.0D) ? 16.0D : paramDouble));
/* 1552 */     paramDataFixerBuilder.addFixer((DataFix)new EntityAttributeBaseValueFix(schema247, "Llama follow range fix", "minecraft:llama", "minecraft:follow_range", paramDouble -> (paramDouble == 40.0D) ? 16.0D : paramDouble));
/* 1553 */     paramDataFixerBuilder.addFixer((DataFix)new EntityAttributeBaseValueFix(schema247, "Piglin Brute follow range fix", "minecraft:piglin_brute", "minecraft:follow_range", paramDouble -> (paramDouble == 16.0D) ? 12.0D : paramDouble));
/* 1554 */     paramDataFixerBuilder.addFixer((DataFix)new EntityAttributeBaseValueFix(schema247, "Warden follow range fix", "minecraft:warden", "minecraft:follow_range", paramDouble -> (paramDouble == 16.0D) ? 24.0D : paramDouble));
/*      */     
/* 1556 */     Schema schema248 = paramDataFixerBuilder.addSchema(4290, net.minecraft.util.datafix.schemas.V4290::new);
/* 1557 */     paramDataFixerBuilder.addFixer((DataFix)new UnflattenTextComponentFix(schema248));
/*      */     
/* 1559 */     Schema schema249 = paramDataFixerBuilder.addSchema(4291, SAME_NAMESPACED);
/* 1560 */     paramDataFixerBuilder.addFixer((DataFix)new LegacyHoverEventFix(schema249));
/*      */     
/* 1562 */     paramDataFixerBuilder.addFixer((DataFix)new TextComponentStringifiedFlagsFix(schema249));
/*      */     
/* 1564 */     Schema schema250 = paramDataFixerBuilder.addSchema(4292, net.minecraft.util.datafix.schemas.V4292::new);
/* 1565 */     paramDataFixerBuilder.addFixer((DataFix)new TextComponentHoverAndClickEventFix(schema250));
/*      */     
/* 1567 */     Schema schema251 = paramDataFixerBuilder.addSchema(4293, SAME_NAMESPACED);
/* 1568 */     paramDataFixerBuilder.addFixer((DataFix)new DropChancesFormatFix(schema251));
/*      */     
/* 1570 */     Schema schema252 = paramDataFixerBuilder.addSchema(4294, SAME_NAMESPACED);
/* 1571 */     paramDataFixerBuilder.addFixer((DataFix)new BlockPropertyRenameAndFix(schema252, "CreakingHeartBlockStateFix", "minecraft:creaking_heart", "active", "creaking_heart_state", paramString -> paramString.equals("true") ? "awake" : "uprooted"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1577 */     Schema schema253 = paramDataFixerBuilder.addSchema(4295, SAME_NAMESPACED);
/*      */     
/* 1579 */     paramDataFixerBuilder.addFixer((DataFix)new BlendingDataFix(schema253));
/*      */     
/* 1581 */     Schema schema254 = paramDataFixerBuilder.addSchema(4296, SAME_NAMESPACED);
/* 1582 */     paramDataFixerBuilder.addFixer((DataFix)new AreaEffectCloudDurationScaleFix(schema254));
/*      */     
/* 1584 */     Schema schema255 = paramDataFixerBuilder.addSchema(4297, SAME_NAMESPACED);
/* 1585 */     paramDataFixerBuilder.addFixer((DataFix)new ForcedChunkToTicketFix(schema255));
/*      */     
/* 1587 */     Schema schema256 = paramDataFixerBuilder.addSchema(4299, SAME_NAMESPACED);
/* 1588 */     paramDataFixerBuilder.addFixer((DataFix)new EntitySpawnerItemVariantComponentFix(schema256));
/*      */     
/* 1590 */     Schema schema257 = paramDataFixerBuilder.addSchema(4300, net.minecraft.util.datafix.schemas.V4300::new);
/* 1591 */     paramDataFixerBuilder.addFixer((DataFix)new SaddleEquipmentSlotFix(schema257));
/*      */     
/* 1593 */     Schema schema258 = paramDataFixerBuilder.addSchema(4301, net.minecraft.util.datafix.schemas.V4301::new);
/* 1594 */     paramDataFixerBuilder.addFixer((DataFix)new EquipmentFormatFix(schema258));
/*      */     
/* 1596 */     Schema schema259 = paramDataFixerBuilder.addSchema(4302, net.minecraft.util.datafix.schemas.V4302::new);
/* 1597 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema259, "Added Test and Test Instance Block Entities", References.BLOCK_ENTITY));
/*      */     
/* 1599 */     Schema schema260 = paramDataFixerBuilder.addSchema(4303, SAME_NAMESPACED);
/* 1600 */     paramDataFixerBuilder.addFixer((DataFix)new EntityFallDistanceFloatToDoubleFix(schema260, References.ENTITY));
/* 1601 */     paramDataFixerBuilder.addFixer((DataFix)new EntityFallDistanceFloatToDoubleFix(schema260, References.PLAYER));
/*      */     
/* 1603 */     Schema schema261 = paramDataFixerBuilder.addSchema(4305, SAME_NAMESPACED);
/* 1604 */     paramDataFixerBuilder.addFixer((DataFix)new BlockPropertyRenameAndFix(schema261, "rename test block mode", "minecraft:test_block", "test_block_mode", "mode", paramString -> paramString));
/*      */     
/* 1606 */     Schema schema262 = paramDataFixerBuilder.addSchema(4306, net.minecraft.util.datafix.schemas.V4306::new);
/* 1607 */     paramDataFixerBuilder.addFixer((DataFix)new ThrownPotionSplitFix(schema262));
/*      */     
/* 1609 */     Schema schema263 = paramDataFixerBuilder.addSchema(4307, net.minecraft.util.datafix.schemas.V4307::new);
/* 1610 */     paramDataFixerBuilder.addFixer((DataFix)new TooltipDisplayComponentFix(schema263));
/*      */     
/* 1612 */     Schema schema264 = paramDataFixerBuilder.addSchema(4309, SAME_NAMESPACED);
/* 1613 */     paramDataFixerBuilder.addFixer((DataFix)new RaidRenamesDataFix(schema264));
/* 1614 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkTicketUnpackPosFix(schema264));
/*      */     
/* 1616 */     Schema schema265 = paramDataFixerBuilder.addSchema(4311, SAME_NAMESPACED);
/* 1617 */     paramDataFixerBuilder.addFixer((DataFix)new AdvancementsRenameFix(schema265, false, "Use lodestone category change", createRenamer("minecraft:nether/use_lodestone", "minecraft:adventure/use_lodestone")));
/*      */     
/* 1619 */     Schema schema266 = paramDataFixerBuilder.addSchema(4312, net.minecraft.util.datafix.schemas.V4312::new);
/* 1620 */     paramDataFixerBuilder.addFixer((DataFix)new PlayerEquipmentFix(schema266));
/*      */     
/* 1622 */     Schema schema267 = paramDataFixerBuilder.addSchema(4314, SAME_NAMESPACED);
/* 1623 */     paramDataFixerBuilder.addFixer((DataFix)new InlineBlockPosFormatFix(schema267));
/*      */     
/* 1625 */     Schema schema268 = paramDataFixerBuilder.addSchema(4420, net.minecraft.util.datafix.schemas.V4420::new);
/* 1626 */     paramDataFixerBuilder.addFixer((DataFix)new NamedEntityConvertUncheckedFix(schema268, "AreaEffectCloudCustomParticleFix", References.ENTITY, "minecraft:area_effect_cloud"));
/*      */     
/* 1628 */     Schema schema269 = paramDataFixerBuilder.addSchema(4421, net.minecraft.util.datafix.schemas.V4421::new);
/* 1629 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema269, "Added Happy Ghast", References.ENTITY));
/*      */     
/* 1631 */     Schema schema270 = paramDataFixerBuilder.addSchema(4424, SAME_NAMESPACED);
/* 1632 */     paramDataFixerBuilder.addFixer((DataFix)new FeatureFlagRemoveFix(schema270, "Remove Locator Bar experimental feature flag", Set.of("minecraft:locator_bar")));
/* 1633 */     paramDataFixerBuilder.addFixer((DataFix)new AddFieldFix(schema270, References.PLAYER, "style", paramDynamic -> paramDynamic.createString("minecraft:default"), new String[] { "locator_bar_icon" }));
/* 1634 */     paramDataFixerBuilder.addFixer((DataFix)new AddFieldFix(schema270, References.ENTITY, "style", paramDynamic -> paramDynamic.createString("minecraft:default"), new String[] { "locator_bar_icon" }));
/*      */     
/* 1636 */     Schema schema271 = paramDataFixerBuilder.addSchema(4531, net.minecraft.util.datafix.schemas.V4531::new);
/* 1637 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema271, "Added Copper Golem", References.ENTITY));
/*      */     
/* 1639 */     Schema schema272 = paramDataFixerBuilder.addSchema(4532, net.minecraft.util.datafix.schemas.V4532::new);
/* 1640 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema272, "Added Copper Golem Statue Block Entity", References.BLOCK_ENTITY));
/*      */     
/* 1642 */     Schema schema273 = paramDataFixerBuilder.addSchema(4533, net.minecraft.util.datafix.schemas.V4533::new);
/* 1643 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema273, "Added Shelf", References.BLOCK_ENTITY));
/*      */     
/* 1645 */     Schema schema274 = paramDataFixerBuilder.addSchema(4535, SAME_NAMESPACED);
/* 1646 */     paramDataFixerBuilder.addFixer((DataFix)new CopperGolemWeatherStateFix(schema274));
/*      */     
/* 1648 */     Schema schema275 = paramDataFixerBuilder.addSchema(4537, SAME_NAMESPACED);
/* 1649 */     paramDataFixerBuilder.addFixer((DataFix)new ChunkDeleteLightFix(schema275));
/*      */     
/* 1651 */     Schema schema276 = paramDataFixerBuilder.addSchema(4541, SAME_NAMESPACED);
/* 1652 */     paramDataFixerBuilder.addFixer(BlockRenameFix.create(schema276, "Rename chain to iron_chain", createRenamer("minecraft:chain", "minecraft:iron_chain")));
/* 1653 */     paramDataFixerBuilder.addFixer(ItemRenameFix.create(schema276, "Rename chain to iron_chain", createRenamer("minecraft:chain", "minecraft:iron_chain")));
/*      */     
/* 1655 */     Schema schema277 = paramDataFixerBuilder.addSchema(4543, net.minecraft.util.datafix.schemas.V4543::new);
/* 1656 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema277, "Added Mannequin", References.ENTITY));
/*      */     
/* 1658 */     Schema schema278 = paramDataFixerBuilder.addSchema(4544, SAME_NAMESPACED);
/* 1659 */     paramDataFixerBuilder.addFixer((DataFix)new LegacyWorldBorderFix(schema278));
/*      */     
/* 1661 */     Schema schema279 = paramDataFixerBuilder.addSchema(4548, SAME_NAMESPACED);
/* 1662 */     paramDataFixerBuilder.addFixer((DataFix)new WorldSpawnDataFix(schema279));
/* 1663 */     paramDataFixerBuilder.addFixer((DataFix)new PlayerRespawnDataFix(schema279));
/*      */     
/* 1665 */     Schema schema280 = paramDataFixerBuilder.addSchema(4648, net.minecraft.util.datafix.schemas.V4648::new);
/* 1666 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema280, "Added Nautilus and Zombie Nautilus", References.ENTITY));
/*      */     
/* 1668 */     Schema schema281 = paramDataFixerBuilder.addSchema(4649, SAME_NAMESPACED);
/* 1669 */     paramDataFixerBuilder.addFixer((DataFix)new TridentAnimationFix(schema281));
/*      */     
/* 1671 */     Schema schema282 = paramDataFixerBuilder.addSchema(4650, SAME_NAMESPACED);
/* 1672 */     paramDataFixerBuilder.addFixer((DataFix)new DebugProfileOverlayReferenceFix(schema282));
/*      */     
/* 1674 */     Schema schema283 = paramDataFixerBuilder.addSchema(4651, SAME_NAMESPACED);
/* 1675 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsGraphicsModeSplitFix(schema283, "cutoutLeaves", "false", "true", "true"));
/* 1676 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsGraphicsModeSplitFix(schema283, "weatherRadius", "5", "10", "10"));
/* 1677 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsGraphicsModeSplitFix(schema283, "vignette", "false", "true", "true"));
/* 1678 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsGraphicsModeSplitFix(schema283, "improvedTransparency", "false", "false", "true"));
/* 1679 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsSetGraphicsPresetToCustomFix(schema283));
/*      */     
/* 1681 */     Schema schema284 = paramDataFixerBuilder.addSchema(4656, net.minecraft.util.datafix.schemas.V4656::new);
/* 1682 */     paramDataFixerBuilder.addFixer((DataFix)new AddNewChoices(schema284, "Added Parched and Camel Husk", References.ENTITY));
/*      */     
/* 1684 */     Schema schema285 = paramDataFixerBuilder.addSchema(4657, SAME_NAMESPACED);
/* 1685 */     paramDataFixerBuilder.addFixer((DataFix)new WorldBorderWarningTimeFix(schema285));
/*      */     
/* 1687 */     Schema schema286 = paramDataFixerBuilder.addSchema(4658, SAME_NAMESPACED);
/* 1688 */     paramDataFixerBuilder.addFixer((DataFix)new GameRuleRegistryFix(schema286));
/*      */     
/* 1690 */     Schema schema287 = paramDataFixerBuilder.addSchema(4661, SAME_NAMESPACED);
/* 1691 */     paramDataFixerBuilder.addFixer((DataFix)new OptionsMusicToastFix(schema287, false));
/*      */   }
/*      */   
/*      */   private static UnaryOperator<String> createRenamerNoNamespace(Map<String, String> paramMap) {
/* 1695 */     return paramString -> (String)paramMap.getOrDefault(paramString, paramString);
/*      */   }
/*      */   
/*      */   private static UnaryOperator<String> createRenamer(Map<String, String> paramMap) {
/* 1699 */     return paramString -> (String)paramMap.getOrDefault(NamespacedSchema.ensureNamespaced(paramString), paramString);
/*      */   }
/*      */   
/*      */   private static UnaryOperator<String> createRenamer(String paramString1, String paramString2) {
/* 1703 */     return paramString3 -> Objects.equals(NamespacedSchema.ensureNamespaced(paramString3), paramString1) ? paramString2 : paramString3;
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\DataFixers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */