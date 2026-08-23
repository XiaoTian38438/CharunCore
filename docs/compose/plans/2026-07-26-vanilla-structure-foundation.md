# Vanilla Structure Foundation (Tier 1-4) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use compose:subagent (recommended) or compose:execute to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the vanilla structure pipeline foundation so jigsaw-based structures (villages, pillager outpost, ancient city, etc.) generate faithfully at vanilla coordinates.

**Architecture:** Port vanilla's layered pipeline: `StructureTemplate` (.nbt load/place) → `StructureTemplatePool`/`SinglePoolElement` (jigsaw pools) → `JigsawPlacement` (graph expansion) → `RandomSpreadStructurePlacement`/`StructureSet` (positioning with per-set salt) → `Structure`/`JigsawStructure`/`StructureStart` (start generation + piece storage) → wire `createStructures`/`createReferences`/structure-piece-placement into `DensityRouterChunkGenerator` before features.

**Tech Stack:** Java 21, no NMS. Reuses project's existing NBT reader, `BlockStateHelper`, `Chunk`, `LegacyRandomSource`, `XoroshiroRandomSource`. Vanilla source at `mapping/cfr-source/net/minecraft/world/level/levelgen/structure/`.

## Global Constraints
- Java 21, package `com.yanrong.server.worldgen.structure2` (new package to avoid clashing with broken old `structure` package).
- No emoji, no comments unless user requests.
- Port vanilla classes verbatim from `mapping/cfr-source/...` — exact field names, exact constants, exact algorithm. The vanilla source IS the spec.
- Seed determinism: every RNG draw uses `LegacyRandomSource` + `setLargeFeatureWithSalt(seed, gridX, gridZ, salt)` / `setLargeFeatureSeed(seed, x, z)` matching vanilla `WorldgenRandom`. The project has `LegacyRandomSource` already.
- .nbt files at `json/minecraft/structure/<path>.nbt` (singular `structure` dir). 2,404 files.
- Configured structures: `json/minecraft/worldgen/structure/*.json` (34 files). Structure sets: `json/minecraft/worldgen/structure_set/*.json` (20 files). Template pools: `json/minecraft/worldgen/template_pool/*.json`. Processor lists: `json/minecraft/worldgen/processor_list/*.json`. Biome tags: `json/minecraft/tags/worldgen/biome/has_structure/*.json`.
- Compile check after every task: `mvn -o compile -DskipTests` → BUILD SUCCESS.
- MinY=-64, height=384, seaLevel=63 for overworld.

---

### Task 1: StructureTemplate — in-memory representation + .nbt loader

**Covers:** [S4]
**Vanilla source:** `mapping/cfr-source/net/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate.java` (881 lines — focus on `load` `:631-672`, `loadPalette` `:654-672`, `addToLists`/`buildInfoList` `:153-173`, `palettes` field `:150`).

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplate.java`
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureBlockInfo.java` (record: BlockPos, int blockStateId, CompoundTag nbt)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/Palette.java` (SimplePalette: BlockState→id map + `jigsaws()` filter)

**Interfaces:**
- Consumes: project's NBT reader (CompoundTag/ListTag), `BlockStateHelper.getDefault(name)` + `withProps(stateId, prop, value)`, `BlockStateHelper.getName(stateId)`.
- Produces: `StructureTemplate` with `Vec3i size`, `List<StructureBlockInfo> blocks` (sorted solid/dynamic/nbt), `load(CompoundTag)`, `getJigsaws()` returns `List<JigsawBlockInfo>`, `palettes`.

- [ ] **Step 1:** Port `StructureTemplate.load(CompoundTag)` — read `size` (3-int list), read `palettes` (single or multi) into `Palette`, read `blocks` (each: `pos` 3-int list, `state` int index into palette, `nbt` optional CompoundTag). Build `List<StructureBlockInfo>`.
- [ ] **Step 2:** Port `addToLists`/`buildInfoList` — partition blocks into solid/dynamic/nbt buckets, sort each by (y,x,z), concatenate (solid first).
- [ ] **Step 3:** Port `Palette.jigsaws()` — filter blocks where block name == `minecraft:jigsaw`, wrap each in `JigsawBlockInfo(pos, nbt{pool, name, target, final_state}, placementPriority)`.
- [ ] **Step 4:** Write `TestStructureTemplate.java` — load `json/minecraft/structure/igloo/top.nbt`, assert `size` = [3,5,3] (or actual), `blocks.size()` > 0, `getJigsaws().size()` == 1.
- [ ] **Step 5:** Run: `mvn -o test-compile -DskipTests -q; java -cp ... TestStructureTemplate` → PASS.
- [ ] **Step 6:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 2: StructurePlaceSettings + block-state rotation/mirror

**Covers:** [S4]
**Vanilla source:** `mapping/cfr-source/.../templatesystem/StructurePlaceSettings.java` (167 lines). Block rotation/mirror: `StructureTemplate.placeInWorld:273` calls `state.rotate(rotation).mirror(mirror)`.

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructurePlaceSettings.java` (mirror, rotation, pivot, processors, boundingBox, knownShape, liquidSettings)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/BlockTransform.java` — `rotate(int stateId, Rotation)`, `mirror(int stateId, Mirror)` using `BlockStateHelper.withProps` to flip `facing`/`axis`/`rotation` properties per vanilla `BlockState.rotate`/`mirror` logic.

**Interfaces:**
- Consumes: `BlockStateHelper.getProp(stateId, "facing"|"axis"|"rotation"|...)`, `BlockStateHelper.withProp`.
- Produces: `StructurePlaceSettings` builder; `BlockTransform.rotate/mirror` that correctly rotate stairs/doors/fences/logs/chests/etc.

- [ ] **Step 1:** Implement `BlockTransform.rotate(stateId, rotation)` — for blocks with `facing` property: cycle N→E→S→W per rotation steps; for `axis` (logs): x↔z on 90°/270°; for `rotation` (chests/signs): +90° per step mod 4. For blocks with no directional property: return unchanged.
- [ ] **Step 2:** Implement `BlockTransform.mirror(stateId, mirror)` — LEFT_RIGHT flips facing E↔W; FRONT_BACK flips N↔S.
- [ ] **Step 3:** Test: rotate `oak_stairs[facing=east]` by 90° → facing=south. Mirror `chest[facing=north]` LEFT_RIGHT → north (unchanged, since LR flips E/W). Verify via `BlockStateHelper.getName/getProp`.
- [ ] **Step 4:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 3: StructureTemplate.placeInWorld — stamp template into world

**Covers:** [S4]
**Vanilla source:** `StructureTemplate.placeInWorld` `:239-351`.

**Files:**
- Modify: `StructureTemplate.java` — add `placeInWorld(WorldGenLevel level, BlockPos pos, StructurePlaceSettings settings)`.

**Interfaces:**
- Consumes: `WorldGenLevel.setBlock(absX,y,absZ,stateId)`, `BlockTransform`, `StructurePlaceSettings`, project heightmap.
- Produces: working template placement with rotation/mirror applied to every block-state.

- [ ] **Step 1:** Implement `placeInWorld`: for each `StructureBlockInfo`, compute transformed position (`calculateRelativePosition` per rotation/mirror/pivot) and transformed block-state (rotate+mirror), call `level.setBlock`. Skip `structure_block` (jigsaw placeholder) blocks. Replace them with their `final_state` from the jigsaw nbt (or air if none).
- [ ] **Step 2:** Implement `calculateRelativePosition` — vanilla `:442-474` transform math (rotate pos around pivot, apply mirror).
- [ ] **Step 3:** Test: load `igloo/top.nbt`, place at world (0,64,0) with rotation 0, assert `level.getBlock(0,64,0)` is snow_block (or whatever top.nbt's first block is). Place same with rotation 90°, assert the structure is rotated (check a directional block's facing changed).
- [ ] **Step 4:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 4: StructureTemplateManager — cache + .nbt file loader

**Covers:** [S4]
**Vanilla source:** `mapping/cfr-source/.../templatesystem/StructureTemplateManager.java` (337 lines — focus on `getOrCreate` `:93-101`, `loadFromResource` `:129-132`, `load` `:242-257`).

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplateManager.java`

**Interfaces:**
- Consumes: project NBT reader (`NbtIo.readCompressed` equivalent — check if project has one; the old `StructureTemplateLoader` used Cloudburst NBT).
- Produces: `getOrCreate(ResourceLocation id)` → `StructureTemplate` (cached, lazy-loaded from `json/minecraft/structure/<id.path>.nbt`).

- [ ] **Step 1:** Implement `StructureTemplateManager.getOrCreate(id)` — cache `Map<ResourceLocation, StructureTemplate>`. On miss, load `.nbt` from `json/minecraft/structure/<namespace>/<path>.nbt`, decompress NBT, call `StructureTemplate.load`, cache, return.
- [ ] **Step 2:** Reuse the old `StructureTemplateLoader`'s NBT-decompression code (it works) but route through the new `StructureTemplate.load`.
- [ ] **Step 3:** Test: `manager.getOrCreate("minecraft:igloo/top")` returns non-null template with correct size. Call twice → same instance (cache hit).
- [ ] **Step 4:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 5: StructurePoolElement + StructureTemplatePool — jigsaw pools

**Covers:** [S5]
**Vanilla source:** `pools/StructurePoolElement.java`, `pools/SinglePoolElement.java` (175 lines), `pools/StructureTemplatePool.java` (142 lines), `pools/EmptyPoolElement.java`.

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructurePoolElement.java` (abstract: `projection`, `getShuffledJigsawBlocks`, `getBoundingBox`, `place`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/SinglePoolElement.java` (holds `ResourceLocation templateId` + processor list; `place` calls `template.placeInWorld`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureTemplatePool.java` (weighted bag: `List<StructurePoolElement> templates` expanded by weight, `Projection` enum, `getRandomTemplate(rand)`, `getShuffledTemplates(rand)`, `fallback` pool ref)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/TemplatePoolLoader.java` — load `json/minecraft/worldgen/template_pool/*.json` into `Map<ResourceLocation, StructureTemplatePool>`.

**Interfaces:**
- Consumes: `StructureTemplateManager.getOrCreate`, `StructurePlaceSettings`, `LegacyRandomSource`.
- Produces: `StructureTemplatePool.getRandomTemplate(rng)` → `StructurePoolElement`; pool registry from JSON.

- [ ] **Step 1:** Implement `StructureTemplatePool` — parse template_pool JSON: `{fallback, elements: [{element: {element_type, location, processors, projection}, weight}]}`. Expand each element weight times into a flat list. `getRandomTemplate(rng)` = `list.get(rng.nextInt(size))`.
- [ ] **Step 2:** Implement `SinglePoolElement` — `getShuffledJigsawBlocks(manager, pos, rotation, mirror)` loads template, extracts jigsaw blocks, transforms positions. `getBoundingBox(manager, pos, rotation)` = template size transformed. `place(level, manager, pos, settings)` calls `template.placeInWorld`.
- [ ] **Step 3:** Test: load `village/plains/town_centers` pool, assert it has multiple weighted elements (plains_fountain_01, plains_meeting_point_*). `getRandomTemplate` returns a non-null `SinglePoolElement`.
- [ ] **Step 4:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 6: PoolElementStructurePiece + JigsawJunction

**Covers:** [S5]
**Vanilla source:** `structure/PoolElementStructurePiece.java` (134 lines), `pools/JigsawJunction.java`.

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/PoolElementStructurePiece.java` (fields: `StructurePoolElement element`, `BlockPos position`, `int groundLevelDelta`, `Rotation rotation`, `List<JigsawJunction> junctions`, `BoundingBox boundingBox`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/JigsawJunction.java` (record: `int sourceX, int sourceGroundY, int sourceZ, int deltaY, Projection projection`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/BoundingBox.java` (record: `minX,minY,minZ,maxX,maxY,maxZ` + `intersects`, `inflate`)

**Interfaces:**
- Consumes: `StructurePoolElement.place`, `BoundingBox`.
- Produces: `PoolElementStructurePiece.postProcess(level, ...)` → delegates to `element.place`. `getJigsawBlocks` returns transformed jigsaw blocks.

- [ ] **Step 1:** Port `PoolElementStructurePiece` — `postProcess` calls `element.place(level, manager, position, settings with rotation+mirror)`. `getBoundingBox` returns stored box.
- [ ] **Step 2:** Port `BoundingBox` with `intersects(other)` and `inflate(n)`.
- [ ] **Step 3:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 7: JigsawPlacement.addPieces — the graph expansion engine

**Covers:** [S5]
**Vanilla source:** `pools/JigsawPlacement.java` (357 lines — `addPieces` `:70-112`, `Placer.tryPlacingChildren` `:194-315`). This is the hardest task.

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/JigsawPlacement.java`

**Interfaces:**
- Consumes: `StructureTemplatePool.getRandomTemplate`, `SinglePoolElement.getShuffledJigsawBlocks`, `PoolElementStructurePiece`, `BoundingBox`, `LegacyRandomSource`, project heightmap `WORLD_SURFACE_WG`.
- Produces: `JigsawPlacement.addPieces(ctx, startPool, startJigsawName, maxDepth, pos, ...)` → `Optional<List<PoolElementStructurePiece>>` (the village).

- [ ] **Step 1:** Port `JigsawBlockInfo` record + `JigsawBlock.canAttach(parent, child)` — vanilla `pools/JigsawBlock.java` + `JigsawBlocks.java`: the attachment rule (parent.target == child.name, parent.facing is opposite child.facing, etc.).
- [ ] **Step 2:** Port the root piece creation (`:80-109`): pick random rotation, resolve start pool, draw `startPool.getRandomTemplate(rng)`, build root `PoolElementStructurePiece` at `pos` projected to heightmap.
- [ ] **Step 3:** Port `Placer.tryPlacingChildren` (`:194-315`): for each parent jigsaw (shuffled), look up its pool, try each candidate template × each rotation, find attachable jigsaw pair, compute child position (align the two jigsaw blocks), compute child bounding box, collision-check via simple AABB overlap (defer VoxelShape — use bounding-box `intersects` against all existing pieces), if fits create child piece, register junctions, enqueue if depth+1 <= maxDepth.
- [ ] **Step 4:** Port the BFS frontier (`:132-139`): iterate `placing` priority queue until empty.
- [ ] **Step 5:** Test: `addPieces` with start pool `village/plains/town_centers`, maxDepth 6, pos (0,64,0). Assert result present, `pieces.size()` > 1 (village expanded). Print piece count + bounding box span.
- [ ] **Step 6:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 8: StructurePlacement + RandomSpreadStructurePlacement + StructureSet

**Covers:** [S6]
**Vanilla source:** `placement/StructurePlacement.java` (159), `placement/RandomSpreadStructurePlacement.java` (80), `StructureSet.java` (38).

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructurePlacement.java` (abstract: `locateOffset`, `frequencyReductionMethod`, `frequency`, `salt`, `exclusionZone`, `isStructureChunk(state,x,z)`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/RandomSpreadStructurePlacement.java` (`spacing`, `separation`, `spreadType`; `getPotentialStructureChunk(seed,x,z)` via `setLargeFeatureWithSalt`; `isPlacementChunk`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureSet.java` (record: `List<StructureSelectionEntry> structures`, `StructurePlacement placement`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureSetLoader.java` — load `json/minecraft/worldgen/structure_set/*.json`.

**Interfaces:**
- Consumes: `LegacyRandomSource.setLargeFeatureWithSalt(seed, gridX, gridZ, salt)` (project has this — verify in `LegacyRandomSource.java`).
- Produces: `StructureSet` registry; `RandomSpreadStructurePlacement.isStructureChunk(seed, chunkX, chunkZ)` → bool.

- [ ] **Step 1:** Port `RandomSpreadStructurePlacement.getPotentialStructureChunk(seed, x, z)` — `gridX=floorDiv(x,spacing)`, `gridZ=floorDiv(z,spacing)`, seed `WorldgenRandom` with `setLargeFeatureWithSalt(seed, gridX, gridZ, salt)`, `offsetX = rng.nextInt(spacing-separation)`, return `ChunkPos(gridX*spacing+offsetX, ...)`. `isPlacementChunk` = current chunk == potential chunk.
- [ ] **Step 2:** Port `FrequencyReductionMethod` (DEFAULT uses `setLargeFeatureWithSalt`+`nextFloat < freq`; LEGACY_3 for mineshaft uses `setLargeFeatureSeed`+`nextDouble < freq`). Defer LEGACY_1/2.
- [ ] **Step 3:** Port `StructureSetLoader` — parse `structure_set/*.json`: `{placement: {type, salt, spacing, separation, ...}, structures: [{structure: id, weight}]}`.
- [ ] **Step 4:** Test: load `villages.json` structure_set, assert spacing=34, separation=8, salt=10387312. `isStructureChunk(12345L, <a village chunk>, <a village chunk>)` → true for the correct grid cell.
- [ ] **Step 5:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 9: Structure + JigsawStructure + StructureStart

**Covers:** [S6]
**Vanilla source:** `Structure.java` (371 — `StructureSettings`, `GenerationContext`, `GenerationStub`, `generate`, `findValidGenerationPoint`), `structures/JigsawStructure.java` (134), `StructureStart.java` (145), `pieces/PiecesContainer.java`, `pieces/StructurePiecesBuilder.java`.

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/Structure.java` (abstract: `biomes` tag, `step`, `terrainAdaptation`, `findGenerationPoint(ctx)` abstract, `generate(...)` → `StructureStart`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/JigsawStructure.java` (fields: `startPool`, `size=maxDepth`, `startHeight`, `projectStartToHeightmap`, `useExpansionHack`, `maxDistanceFromCenter`; `findGenerationPoint` → `JigsawPlacement.addPieces`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureStart.java` (fields: `Structure`, `List<StructurePiece> pieces`, `ChunkPos`, `int references`; `placeInChunk(level, rng, writableArea, chunkPos)` → iterate pieces calling `postProcess`)
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureRegistry.java` — load `structure/*.json` into `Map<ResourceLocation, Structure>`. Parse `type=jigsaw` → `JigsawStructure` with start_pool/size/etc. Parse `type=mineshaft/stronghold/...` → defer (mark as not-yet-implemented).

**Interfaces:**
- Consumes: `JigsawPlacement.addPieces`, `TemplatePoolLoader`, biome tag resolution (`json/minecraft/tags/worldgen/biome/has_structure/*.json`), `BiomeManager.getBiome(x,y,z)`.
- Produces: `Structure.generate(seed, chunkPos, ...)` → `StructureStart` (or INVALID). `StructureStart.placeInChunk` stamps all pieces.

- [ ] **Step 1:** Port `JigsawStructure.findGenerationPoint` — pick start position (chunk center + heightmap), call `JigsawPlacement.addPieces(ctx, startPool, ..., maxDepth=size, pos, ...)`, return `GenerationStub` if pieces present.
- [ ] **Step 2:** Port `Structure.generate` — call `findGenerationPoint`, biome-validity check (sample biome at pos, check against structure's biomes tag), return `StructureStart` or INVALID.
- [ ] **Step 3:** Port `StructureStart.placeInChunk` — for each piece whose bounding box intersects the writable area (16×16×height), call `piece.postProcess(level, ...)`.
- [ ] **Step 4:** Port `StructureRegistry` — load all `structure/*.json`, build `JigsawStructure` for jigsaw types. Load biome tags into `Set<Integer>` per structure (resolve tag → biome IDs via `OverworldBiomeBuilder`).
- [ ] **Step 5:** Test: `StructureRegistry.get("minecraft:village_plains")` returns a `JigsawStructure` with start_pool=`minecraft:village/plains/town_centers`, size=6. Biome tag `#minecraft:has_structure/village_plains` resolves to a set containing plains (40).
- [ ] **Step 6:** `mvn -o compile -DskipTests` → BUILD SUCCESS.

### Task 10: StructureManager + wire into DensityRouterChunkGenerator

**Covers:** [S7]
**Vanilla source:** `ChunkGenerator.java:378-425` (createStructures), `:446-474` (createReferences), `:276-292` (applyBiomeDecoration structure loop). `world/level/StructureManager.java`.

**Files:**
- Create: `../../../src/main/java/com/CharunCore/server/worldgen/structure2/StructureManager.java` (per-chunk `Map<Structure, StructureStart>` + reference tracking; `setStart`, `getStartsForStep(step)`, `placeStructures(level, step, chunkX, chunkZ)`)
- Modify: `../../../src/main/java/com/CharunCore/server/worldgen/DensityRouterChunkGenerator.java` — add `createStructures(cx,cz)` call before features; restructure `generate()` to run structure-piece-placement per `GenerationStep` before features. Add structure-NBT persistence field to `Chunk`.

**Interfaces:**
- Consumes: `StructureSetLoader`, `StructureRegistry`, `RandomSpreadStructurePlacement.isStructureChunk`, `Structure.generate`, `StructureStart.placeInChunk`, `WorldGenLevel`.
- Produces: structures generating at vanilla coordinates with pieces placed before features.

- [ ] **Step 1:** Port `createStructures(cx, cz, seed)` — iterate all loaded `StructureSet`s, for each: `placement.isStructureChunk(seed, cx, cz)`? If yes: weighted-pick structure from set, `structure.generate(seed, chunkPos, ...)` → `StructureStart`, store in `StructureManager` for this chunk.
- [ ] **Step 2:** Port `createReferences` — for the center chunk, scan 17×17 neighbors' stored starts, for each start whose bounding box intersects center, bump reference count. (For MVP: store starts in a world-level `Map<ChunkPos, Map<Structure, StructureStart>>` cache.)
- [ ] **Step 3:** Port structure-piece placement in `applyBiomeDecoration` — for each `GenerationStep.Decoration` ordinal (0..10): first place structure pieces whose `step.ordinal()` == i (via `StructureStart.placeInChunk`), then features for step i. For MVP: structures use step `surface_structures` (4); place them before the existing feature pass.
- [ ] **Step 4:** Wire into `DensityRouterChunkGenerator.generate()` — call `createStructures` after biome creation (before surface), call `placeStructures(level, step, cx, cz)` before `placeFeatures`. Use the existing `WorldGenLevel` (from Stage 2) for absolute-coord writes so structures span chunks.
- [ ] **Step 5:** Retire the old `structure.StructureManager.generateStructures(...)` call (line 169-170) — remove it from `generate()`.
- [ ] **Step 6:** Test: generate a 5×5 chunk grid with seed 12345L, find a chunk that `createStructures` flags for a village (search the grid), assert `StructureManager` has a `StructureStart` for `village_plains`, and `placeStructures` wrote village blocks (assert some `structure2`-placed blocks exist: e.g. count `oak_planks` or `oak_log` blocks that a village house would contain).
- [ ] **Step 7:** `mvn -o compile -DskipTests` → BUILD SUCCESS. Run TestGen → 9 chunks generate without crash.

---

## Verification (end of plan)
- `mvn -o compile -DskipTests` → BUILD SUCCESS
- TestGen (9 chunks) generates without crash
- TestStructureTemplate loads `igloo/top.nbt` correctly
- TestStructurePool loads `village/plains/town_centers` pool with multiple elements
- TestJigsawPlacement produces a multi-piece village from `village/plains/town_centers` with maxDepth 6
- TestVillageGeneration: a 5×5 grid at seed 12345L produces at least one chunk with village_plains StructureStart and village blocks placed

## Notes
- This plan covers Tier 1-4 only. Tier 5 (Beardifier — `NoiseChunk.java:114` stub) and Tier 6 (procedural structures: Stronghold/Mineshaft/Monument/Mansion/Fortress) get separate plans after this foundation produces a working village.
- The old `structure` package (15 broken generators) is left in place but un-wired; delete after the new system is verified.
- VoxelShape collision (vanilla `Shapes.joinIsNotEmpty`) is simplified to bounding-box `intersects` for the jigsaw collision check — sufficient for MVP, revisit if villages overlap oddly.
