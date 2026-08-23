# Vanilla Structure System 100% Replication — Design Spec

## [S1] Problem
Current structure system (15 generators) is entirely self-written and unfaithful to vanilla 1.21.11. 7 architectural defects: broken .nbt paths (3 generators silently no-op), single-chunk clipping (multi-block templates truncated), no block-state rotation, no per-structure salt (all structures land on same grid), wrong spacing/separation values, no tile-entity data (chests/spawners inert), inverted generation order (structures AFTER features vs vanilla BEFORE).

## [S2] Solution overview
Replicate vanilla's layered structure pipeline. Build foundation (Tiers 1-4) which unlocks all jigsaw-based structures (villages, pillager outpost, ancient city, trail ruins, trial chambers, bastion, ruined portal, end city), then incrementally add Beardifier (Tier 5) and procedural structure classes (Tier 6).

## [S3] Architecture (per vanilla ChunkGenerator pipeline)
1. `createStructures` — iterate StructureSets, gate by `isStructureChunk(state,x,z)` (RandomSpread grid with per-set salt), weighted-pick for multi-entry sets, `tryGenerateStructure` → `structure.generate` → store `StructureStart`.
2. `createReferences` — 17×17 neighbor scan, bump reference counts.
3. `applyBiomeDecoration` — structure pieces placed BEFORE features per `GenerationStep.Decoration` ordinal; then features.

## [S4] Tier 1 — Template subsystem (leaf of dependency tree)
- `StructureTemplate` — in-memory rep: size, palettes, entity infos. `load(CompoundTag)` (palette deser, block partition solid/dynamic/nbt, sort y,x,z), `getJigsaws`, `placeInWorld` (processor pipeline, rotation+mirror of block-state, waterlogging, edge shape update), `getBoundingBox`, `transform`/`calculateRelativePosition`.
- `StructurePlaceSettings` — mutable placement config (mirror/rotation/pivot/bbox/processors/liquidSettings/palette/knownShape).
- `StructureTemplateManager` — cache + loader. `getOrCreate(id)` → cache or load from `json/minecraft/structure/<path>.nbt` via NbtIo.readCompressed.
- `StructureProcessor` + `ProcessorRule` + `RuleTest` — start with `BlockIgnoreProcessor.STRUCTURE_BLOCK` + `JigsawReplacementProcessor` + `GravityProcessor` (TERRAIN_MATCHING). Rule-based processor for mossify/etc.

## [S5] Tier 2 — Jigsaw pool subsystem
- `StructurePoolElement` hierarchy: abstract + `SinglePoolElement` (NBT-loader element: getShuffledJigsawBlocks, getBoundingBox, place, getSettings), `LegacySinglePoolElement`, `EmptyPoolElement`, `ListPoolElement`, `FeaturePoolElement`.
- `StructureTemplatePool` — weighted bag + `Projection` enum (RIGID/TERRAIN_MATCHING) + `getRandomTemplate`/`getShuffledTemplates`. Load from `json/minecraft/worldgen/template_pool/*.json`.
- `JigsawJunction` — record (sourceX, sourceGroundY, sourceZ, deltaY, projection). Needed by placer + Beardifier.
- `PoolElementStructurePiece` — StructurePiece wrapper around a StructurePoolElement (position/rotation/groundLevelDelta/junctions).

## [S6] Tier 3 — Placement & start subsystem
- `StructurePlacement` (abstract) + `RandomSpreadStructurePlacement` (grid: spacing/separation/salt/spreadType LINEAR|TRIANGULAR, `setLargeFeatureWithSalt` seeding) + `ConcentricRingsStructurePlacement` (stronghold only, defer).
- `FrequencyReductionMethod` (DEFAULT/LEGACY_1/2/3) + `ExclusionZone`.
- `StructureSet` — record (structures weighted list + placement). Load from `json/minecraft/worldgen/structure_set/*.json`.
- `Structure` abstract + `JigsawStructure` — `GenerationContext`, `GenerationStub`, `generate(...)`, `findValidGenerationPoint` (biome validity check), `findGenerationPoint` delegates to `JigsawPlacement.addPieces`.
- `StructureStart` + `PiecesContainer` + `StructurePiecesBuilder` — per-chunk activation record, piece storage, serialization.
- `StructureManager` — bridges ChunkGenerator ↔ ChunkAccess for start storage/retrieval (per-section Map<Structure,StructureStart> + reference LongSets).

## [S7] Tier 4 — Wire into DensityRouterChunkGenerator
- Move biome creation BEFORE noise (vanilla order).
- Add `createStructures(cx,cz)` phase before `fillFromNoise` (decide starts).
- Add `createReferences` (17×17 scan).
- Restructure `applyBiomeDecoration`: structure pieces (per step ordinal) THEN features (per step ordinal), interleaved. Currently features+structures are a single flat pass.
- Add structure-NBT field to Chunk for StructureStart persistence.

## [S8] Tier 5 — Beardifier (deferred)
- `Beardifier.forStructuresInChunk` — inject into `NoiseChunk` (currently `constant(0)` at NoiseChunk.java:114-118). Computes terrain denting from structure pieces within 12 blocks. 5 TerrainAdjustment values (NONE/BURY/BEARD_THIN/BEARD_BOX/ENCAPSULATE). Optional — structures place without it but float/clip.

## [S9] Tier 6 — Procedural structure classes (deferred, per-structure)
Non-jigsaw Structure subclasses + their *Pieces.java (Stronghold ~90KB, NetherFortress ~95KB, OceanMonument ~116KB, WoodlandMansion ~62KB, Mineshaft ~50KB, EndCity, DesertPyramid, JungleTemple, SwampHut, Igloo, Shipwreck, OceanRuin, RuinedPortal, BuriedTreasure, NetherFossil). Each bespoke — build after jigsaw path complete.

## [S10] Seed determinism requirement
Every random draw (placement grid, pool template selection, rotation, jigsaw matching) MUST use vanilla's exact `WorldgenRandom` + `LegacyRandomSource` + `setLargeFeatureWithSalt`/`setLargeFeatureSeed` seeding or structures won't appear at vanilla coordinates.

## [S11] Data sources
- `.nbt` templates: `json/minecraft/structure/<path>.nbt` (2,404 files). Subdirs: village/{plains,desert,savanna,snowy,taiga}/, bastion/, ancient_city/, pillager_outpost/, shipwreck/, ruined_portal/, underwater_ruin/, igloo/, etc.
- configured structures: `json/minecraft/worldgen/structure/*.json` (34 files).
- structure sets: `json/minecraft/worldgen/structure_set/*.json` (20 files).
- template pools: `json/minecraft/worldgen/template_pool/*.json`.
- processor lists: `json/minecraft/worldgen/processor_list/*.json`.
- biome tags: `json/minecraft/tags/worldgen/biome/has_structure/*.json`.
