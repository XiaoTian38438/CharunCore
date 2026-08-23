/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ScheduledTickAccess;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.EntityBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.material.MapColor;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*      */ public abstract class BlockStateBase
/*      */   extends StateHolder<Block, BlockState>
/*      */ {
/*  820 */   private static final Direction[] DIRECTIONS = Direction.values(); private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES; static {
/*  821 */     EMPTY_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], paramArrayOfVoxelShape -> Arrays.fill((Object[])paramArrayOfVoxelShape, Shapes.empty()));
/*  822 */     FULL_BLOCK_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], paramArrayOfVoxelShape -> Arrays.fill((Object[])paramArrayOfVoxelShape, Shapes.block()));
/*      */   }
/*      */   private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES;
/*      */   private final int lightEmission;
/*      */   private final boolean useShapeForLightOcclusion;
/*      */   private final boolean isAir;
/*      */   private final boolean ignitedByLava;
/*      */   @Deprecated
/*      */   private final boolean liquid;
/*      */   @Deprecated
/*      */   private boolean legacySolid;
/*      */   private final PushReaction pushReaction;
/*      */   private final MapColor mapColor;
/*      */   private final float destroySpeed;
/*      */   private final boolean requiresCorrectToolForDrops;
/*      */   private final boolean canOcclude;
/*      */   private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */   private final BlockBehaviour.StatePredicate isSuffocating;
/*      */   private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */   private final BlockBehaviour.StatePredicate hasPostProcess;
/*      */   private final BlockBehaviour.StatePredicate emissiveRendering;
/*      */   private final BlockBehaviour.OffsetFunction offsetFunction;
/*      */   private final boolean spawnTerrainParticles;
/*      */   private final NoteBlockInstrument instrument;
/*      */   private final boolean replaceable;
/*      */   private Cache cache;
/*  848 */   private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
/*      */   private boolean isRandomlyTicking;
/*      */   private boolean solidRender;
/*      */   private VoxelShape occlusionShape;
/*      */   private VoxelShape[] occlusionShapesByFace;
/*      */   private boolean propagatesSkylightDown;
/*      */   private int lightBlock;
/*      */   
/*      */   protected BlockStateBase(Block paramBlock, Reference2ObjectArrayMap<Property<?>, Comparable<?>> paramReference2ObjectArrayMap, MapCodec<BlockState> paramMapCodec) {
/*  857 */     super(paramBlock, paramReference2ObjectArrayMap, paramMapCodec);
/*  858 */     BlockBehaviour.Properties properties = paramBlock.properties;
/*      */     
/*  860 */     this.lightEmission = properties.lightEmission.applyAsInt(asState());
/*  861 */     this.useShapeForLightOcclusion = paramBlock.useShapeForLightOcclusion(asState());
/*  862 */     this.isAir = properties.isAir;
/*  863 */     this.ignitedByLava = properties.ignitedByLava;
/*  864 */     this.liquid = properties.liquid;
/*  865 */     this.pushReaction = properties.pushReaction;
/*  866 */     this.mapColor = properties.mapColor.apply(asState());
/*  867 */     this.destroySpeed = properties.destroyTime;
/*  868 */     this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
/*  869 */     this.canOcclude = properties.canOcclude;
/*  870 */     this.isRedstoneConductor = properties.isRedstoneConductor;
/*  871 */     this.isSuffocating = properties.isSuffocating;
/*  872 */     this.isViewBlocking = properties.isViewBlocking;
/*  873 */     this.hasPostProcess = properties.hasPostProcess;
/*  874 */     this.emissiveRendering = properties.emissiveRendering;
/*  875 */     this.offsetFunction = properties.offsetFunction;
/*  876 */     this.spawnTerrainParticles = properties.spawnTerrainParticles;
/*  877 */     this.instrument = properties.instrument;
/*  878 */     this.replaceable = properties.replaceable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean calculateSolid() {
/*  887 */     if (this.owner.properties.forceSolidOn) {
/*  888 */       return true;
/*      */     }
/*  890 */     if (this.owner.properties.forceSolidOff) {
/*  891 */       return false;
/*      */     }
/*  893 */     if (this.cache == null) {
/*  894 */       return false;
/*      */     }
/*  896 */     VoxelShape voxelShape = this.cache.collisionShape;
/*  897 */     if (voxelShape.isEmpty()) {
/*  898 */       return false;
/*      */     }
/*  900 */     AABB aABB = voxelShape.bounds();
/*  901 */     if (aABB.getSize() >= 0.7291666666666666D) {
/*  902 */       return true;
/*      */     }
/*  904 */     if (aABB.getYsize() >= 1.0D) {
/*  905 */       return true;
/*      */     }
/*  907 */     return false;
/*      */   }
/*      */   
/*      */   public void initCache() {
/*  911 */     this.fluidState = this.owner.getFluidState(asState());
/*  912 */     this.isRandomlyTicking = this.owner.isRandomlyTicking(asState());
/*  913 */     if (!getBlock().hasDynamicShape()) {
/*  914 */       this.cache = new Cache(asState());
/*      */     }
/*  916 */     this.legacySolid = calculateSolid();
/*      */     
/*  918 */     this.occlusionShape = this.canOcclude ? this.owner.getOcclusionShape(asState()) : Shapes.empty();
/*  919 */     this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
/*      */     
/*  921 */     if (this.occlusionShape.isEmpty()) {
/*  922 */       this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
/*  923 */     } else if (this.solidRender) {
/*  924 */       this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
/*      */     } else {
/*  926 */       this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];
/*  927 */       for (Direction direction : DIRECTIONS) {
/*  928 */         this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction);
/*      */       }
/*      */     } 
/*      */     
/*  932 */     this.propagatesSkylightDown = this.owner.propagatesSkylightDown(asState());
/*  933 */     this.lightBlock = this.owner.getLightBlock(asState());
/*      */   }
/*      */   
/*      */   public Block getBlock() {
/*  937 */     return this.owner;
/*      */   }
/*      */   
/*      */   public Holder<Block> getBlockHolder() {
/*  941 */     return (Holder<Block>)this.owner.builtInRegistryHolder();
/*      */   }
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
/*      */   @Deprecated
/*      */   public boolean blocksMotion() {
/*  957 */     Block block = getBlock();
/*  958 */     return (block != Blocks.COBWEB && block != Blocks.BAMBOO_SAPLING && isSolid());
/*      */   }
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
/*      */   @Deprecated
/*      */   public boolean isSolid() {
/*  976 */     return this.legacySolid;
/*      */   }
/*      */   
/*      */   public boolean isValidSpawn(BlockGetter paramBlockGetter, BlockPos paramBlockPos, EntityType<?> paramEntityType) {
/*  980 */     return (getBlock()).properties.isValidSpawn.test(asState(), paramBlockGetter, paramBlockPos, paramEntityType);
/*      */   }
/*      */   
/*      */   public boolean propagatesSkylightDown() {
/*  984 */     return this.propagatesSkylightDown;
/*      */   }
/*      */   
/*      */   public int getLightBlock() {
/*  988 */     return this.lightBlock;
/*      */   }
/*      */   
/*      */   public VoxelShape getFaceOcclusionShape(Direction paramDirection) {
/*  992 */     return this.occlusionShapesByFace[paramDirection.ordinal()];
/*      */   }
/*      */   
/*      */   public VoxelShape getOcclusionShape() {
/*  996 */     return this.occlusionShape;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasLargeCollisionShape() {
/* 1001 */     return (this.cache == null || this.cache.largeCollisionShape);
/*      */   }
/*      */   
/*      */   public boolean useShapeForLightOcclusion() {
/* 1005 */     return this.useShapeForLightOcclusion;
/*      */   }
/*      */   
/*      */   public int getLightEmission() {
/* 1009 */     return this.lightEmission;
/*      */   }
/*      */   
/*      */   public boolean isAir() {
/* 1013 */     return this.isAir;
/*      */   }
/*      */   
/*      */   public boolean ignitedByLava() {
/* 1017 */     return this.ignitedByLava;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean liquid() {
/* 1023 */     return this.liquid;
/*      */   }
/*      */   
/*      */   public MapColor getMapColor(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1027 */     return this.mapColor;
/*      */   }
/*      */   
/*      */   public BlockState rotate(Rotation paramRotation) {
/* 1031 */     return getBlock().rotate(asState(), paramRotation);
/*      */   }
/*      */   
/*      */   public BlockState mirror(Mirror paramMirror) {
/* 1035 */     return getBlock().mirror(asState(), paramMirror);
/*      */   }
/*      */   
/*      */   public RenderShape getRenderShape() {
/* 1039 */     return getBlock().getRenderShape(asState());
/*      */   }
/*      */   
/*      */   public boolean emissiveRendering(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1043 */     return this.emissiveRendering.test(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public float getShadeBrightness(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1047 */     return getBlock().getShadeBrightness(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public boolean isRedstoneConductor(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1051 */     return this.isRedstoneConductor.test(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public boolean isSignalSource() {
/* 1055 */     return getBlock().isSignalSource(asState());
/*      */   }
/*      */   
/*      */   public int getSignal(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 1059 */     return getBlock().getSignal(asState(), paramBlockGetter, paramBlockPos, paramDirection);
/*      */   }
/*      */   
/*      */   public boolean hasAnalogOutputSignal() {
/* 1063 */     return getBlock().hasAnalogOutputSignal(asState());
/*      */   }
/*      */   
/*      */   public int getAnalogOutputSignal(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 1067 */     return getBlock().getAnalogOutputSignal(asState(), paramLevel, paramBlockPos, paramDirection);
/*      */   }
/*      */   
/*      */   public float getDestroySpeed(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1071 */     return this.destroySpeed;
/*      */   }
/*      */   
/*      */   public float getDestroyProgress(Player paramPlayer, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1075 */     return getBlock().getDestroyProgress(asState(), paramPlayer, paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public int getDirectSignal(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 1079 */     return getBlock().getDirectSignal(asState(), paramBlockGetter, paramBlockPos, paramDirection);
/*      */   }
/*      */   
/*      */   public PushReaction getPistonPushReaction() {
/* 1083 */     return this.pushReaction;
/*      */   }
/*      */   
/*      */   public boolean isSolidRender() {
/* 1087 */     return this.solidRender;
/*      */   }
/*      */   
/*      */   public boolean canOcclude() {
/* 1091 */     return this.canOcclude;
/*      */   }
/*      */   
/*      */   public boolean skipRendering(BlockState paramBlockState, Direction paramDirection) {
/* 1095 */     return getBlock().skipRendering(asState(), paramBlockState, paramDirection);
/*      */   }
/*      */   
/*      */   public VoxelShape getShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1099 */     return getShape(paramBlockGetter, paramBlockPos, CollisionContext.empty());
/*      */   }
/*      */   
/*      */   public VoxelShape getShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 1103 */     return getBlock().getShape(asState(), paramBlockGetter, paramBlockPos, paramCollisionContext);
/*      */   }
/*      */   
/*      */   public VoxelShape getCollisionShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1107 */     if (this.cache != null) {
/* 1108 */       return this.cache.collisionShape;
/*      */     }
/* 1110 */     return getCollisionShape(paramBlockGetter, paramBlockPos, CollisionContext.empty());
/*      */   }
/*      */   
/*      */   public VoxelShape getCollisionShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 1114 */     return getBlock().getCollisionShape(asState(), paramBlockGetter, paramBlockPos, paramCollisionContext);
/*      */   }
/*      */   
/*      */   public VoxelShape getEntityInsideCollisionShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/* 1118 */     return getBlock().getEntityInsideCollisionShape(asState(), paramBlockGetter, paramBlockPos, paramEntity);
/*      */   }
/*      */   
/*      */   public VoxelShape getBlockSupportShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1122 */     return getBlock().getBlockSupportShape(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public VoxelShape getVisualShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 1126 */     return getBlock().getVisualShape(asState(), paramBlockGetter, paramBlockPos, paramCollisionContext);
/*      */   }
/*      */   
/*      */   public VoxelShape getInteractionShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1130 */     return getBlock().getInteractionShape(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public final boolean entityCanStandOn(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/* 1134 */     return entityCanStandOnFace(paramBlockGetter, paramBlockPos, paramEntity, Direction.UP);
/*      */   }
/*      */   
/*      */   public final boolean entityCanStandOnFace(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity, Direction paramDirection) {
/* 1138 */     return Block.isFaceFull(getCollisionShape(paramBlockGetter, paramBlockPos, CollisionContext.of(paramEntity)), paramDirection);
/*      */   }
/*      */   
/*      */   public Vec3 getOffset(BlockPos paramBlockPos) {
/* 1142 */     BlockBehaviour.OffsetFunction offsetFunction = this.offsetFunction;
/* 1143 */     if (offsetFunction != null) {
/* 1144 */       return offsetFunction.evaluate(asState(), paramBlockPos);
/*      */     }
/* 1146 */     return Vec3.ZERO;
/*      */   }
/*      */   
/*      */   public boolean hasOffsetFunction() {
/* 1150 */     return (this.offsetFunction != null);
/*      */   }
/*      */   
/*      */   public boolean triggerEvent(Level paramLevel, BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 1154 */     return getBlock().triggerEvent(asState(), paramLevel, paramBlockPos, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleNeighborChanged(Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 1162 */     getBlock().neighborChanged(asState(), paramLevel, paramBlockPos, paramBlock, paramOrientation, paramBoolean);
/*      */   }
/*      */   
/*      */   public final void updateNeighbourShapes(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, @UpdateFlags int paramInt) {
/* 1166 */     updateNeighbourShapes(paramLevelAccessor, paramBlockPos, paramInt, 512);
/*      */   }
/*      */   
/*      */   public final void updateNeighbourShapes(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, @UpdateFlags int paramInt1, int paramInt2) {
/* 1170 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 1171 */     for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/* 1172 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction);
/* 1173 */       paramLevelAccessor.neighborShapeChanged(direction.getOpposite(), (BlockPos)mutableBlockPos, paramBlockPos, asState(), paramInt1, paramInt2);
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void updateIndirectNeighbourShapes(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, @UpdateFlags int paramInt) {
/* 1178 */     updateIndirectNeighbourShapes(paramLevelAccessor, paramBlockPos, paramInt, 512);
/*      */   }
/*      */   
/*      */   public void updateIndirectNeighbourShapes(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, @UpdateFlags int paramInt1, int paramInt2) {
/* 1182 */     getBlock().updateIndirectNeighbourShapes(asState(), paramLevelAccessor, paramBlockPos, paramInt1, paramInt2);
/*      */   }
/*      */   
/*      */   public void onPlace(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 1186 */     getBlock().onPlace(asState(), paramLevel, paramBlockPos, paramBlockState, paramBoolean);
/*      */   }
/*      */   
/*      */   public void affectNeighborsAfterRemoval(ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 1190 */     getBlock().affectNeighborsAfterRemoval(asState(), paramServerLevel, paramBlockPos, paramBoolean);
/*      */   }
/*      */   
/*      */   public void onExplosionHit(ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 1194 */     getBlock().onExplosionHit(asState(), paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*      */   }
/*      */   
/*      */   public void tick(ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 1198 */     getBlock().tick(asState(), paramServerLevel, paramBlockPos, paramRandomSource);
/*      */   }
/*      */   
/*      */   public void randomTick(ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 1202 */     getBlock().randomTick(asState(), paramServerLevel, paramBlockPos, paramRandomSource);
/*      */   }
/*      */   
/*      */   public void entityInside(Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 1206 */     getBlock().entityInside(asState(), paramLevel, paramBlockPos, paramEntity, paramInsideBlockEffectApplier, paramBoolean);
/*      */   }
/*      */   
/*      */   public void spawnAfterBreak(ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 1210 */     getBlock().spawnAfterBreak(asState(), paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/*      */   }
/*      */   
/*      */   public List<ItemStack> getDrops(LootParams.Builder paramBuilder) {
/* 1214 */     return getBlock().getDrops(asState(), paramBuilder);
/*      */   }
/*      */   
/*      */   public InteractionResult useItemOn(ItemStack paramItemStack, Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 1218 */     return getBlock().useItemOn(paramItemStack, asState(), paramLevel, paramBlockHitResult.getBlockPos(), paramPlayer, paramInteractionHand, paramBlockHitResult);
/*      */   }
/*      */   
/*      */   public InteractionResult useWithoutItem(Level paramLevel, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 1222 */     return getBlock().useWithoutItem(asState(), paramLevel, paramBlockHitResult.getBlockPos(), paramPlayer, paramBlockHitResult);
/*      */   }
/*      */   
/*      */   public void attack(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 1226 */     getBlock().attack(asState(), paramLevel, paramBlockPos, paramPlayer);
/*      */   }
/*      */   
/*      */   public boolean isSuffocating(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1230 */     return this.isSuffocating.test(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public boolean isViewBlocking(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1234 */     return this.isViewBlocking.test(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public BlockState updateShape(LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState, RandomSource paramRandomSource) {
/* 1238 */     return getBlock().updateShape(asState(), paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState, paramRandomSource);
/*      */   }
/*      */   
/*      */   public boolean isPathfindable(PathComputationType paramPathComputationType) {
/* 1242 */     return getBlock().isPathfindable(asState(), paramPathComputationType);
/*      */   }
/*      */   
/*      */   public boolean canBeReplaced(BlockPlaceContext paramBlockPlaceContext) {
/* 1246 */     return getBlock().canBeReplaced(asState(), paramBlockPlaceContext);
/*      */   }
/*      */   
/*      */   public boolean canBeReplaced(Fluid paramFluid) {
/* 1250 */     return getBlock().canBeReplaced(asState(), paramFluid);
/*      */   }
/*      */   
/*      */   public boolean canBeReplaced() {
/* 1254 */     return this.replaceable;
/*      */   }
/*      */   
/*      */   public boolean canSurvive(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 1258 */     return getBlock().canSurvive(asState(), paramLevelReader, paramBlockPos);
/*      */   }
/*      */   
/*      */   public boolean hasPostProcess(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1262 */     return this.hasPostProcess.test(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public MenuProvider getMenuProvider(Level paramLevel, BlockPos paramBlockPos) {
/* 1266 */     return getBlock().getMenuProvider(asState(), paramLevel, paramBlockPos);
/*      */   }
/*      */   
/*      */   public boolean is(TagKey<Block> paramTagKey) {
/* 1270 */     return getBlock().builtInRegistryHolder().is(paramTagKey);
/*      */   }
/*      */   
/*      */   public boolean is(TagKey<Block> paramTagKey, Predicate<BlockStateBase> paramPredicate) {
/* 1274 */     return (is(paramTagKey) && paramPredicate.test(this));
/*      */   }
/*      */   
/*      */   public boolean is(HolderSet<Block> paramHolderSet) {
/* 1278 */     return paramHolderSet.contains((Holder)getBlock().builtInRegistryHolder());
/*      */   }
/*      */   
/*      */   public boolean is(Holder<Block> paramHolder) {
/* 1282 */     return is((Block)paramHolder.value());
/*      */   }
/*      */   
/*      */   public Stream<TagKey<Block>> getTags() {
/* 1286 */     return getBlock().builtInRegistryHolder().tags();
/*      */   }
/*      */   
/*      */   public boolean hasBlockEntity() {
/* 1290 */     return getBlock() instanceof EntityBlock;
/*      */   }
/*      */   
/*      */   public boolean shouldChangedStateKeepBlockEntity(BlockState paramBlockState) {
/* 1294 */     return getBlock().shouldChangedStateKeepBlockEntity(paramBlockState);
/*      */   }
/*      */   
/*      */   public <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockEntityType<T> paramBlockEntityType) {
/* 1298 */     if (getBlock() instanceof EntityBlock) {
/* 1299 */       return ((EntityBlock)getBlock()).getTicker(paramLevel, asState(), paramBlockEntityType);
/*      */     }
/* 1301 */     return null;
/*      */   }
/*      */   
/*      */   public boolean is(Block paramBlock) {
/* 1305 */     return (getBlock() == paramBlock);
/*      */   }
/*      */   
/*      */   public boolean is(ResourceKey<Block> paramResourceKey) {
/* 1309 */     return getBlock().builtInRegistryHolder().is(paramResourceKey);
/*      */   }
/*      */   
/*      */   public FluidState getFluidState() {
/* 1313 */     return this.fluidState;
/*      */   }
/*      */   
/*      */   public boolean isRandomlyTicking() {
/* 1317 */     return this.isRandomlyTicking;
/*      */   }
/*      */   
/*      */   public long getSeed(BlockPos paramBlockPos) {
/* 1321 */     return getBlock().getSeed(asState(), paramBlockPos);
/*      */   }
/*      */   
/*      */   public SoundType getSoundType() {
/* 1325 */     return getBlock().getSoundType(asState());
/*      */   }
/*      */   
/*      */   public void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 1329 */     getBlock().onProjectileHit(paramLevel, paramBlockState, paramBlockHitResult, paramProjectile);
/*      */   }
/*      */   
/*      */   public boolean isFaceSturdy(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 1333 */     return isFaceSturdy(paramBlockGetter, paramBlockPos, paramDirection, SupportType.FULL);
/*      */   }
/*      */   
/*      */   public boolean isFaceSturdy(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection, SupportType paramSupportType) {
/* 1337 */     if (this.cache != null) {
/* 1338 */       return this.cache.isFaceSturdy(paramDirection, paramSupportType);
/*      */     }
/* 1340 */     return paramSupportType.isSupporting(asState(), paramBlockGetter, paramBlockPos, paramDirection);
/*      */   }
/*      */   
/*      */   public boolean isCollisionShapeFullBlock(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 1344 */     if (this.cache != null) {
/* 1345 */       return this.cache.isCollisionShapeFullBlock;
/*      */     }
/* 1347 */     return getBlock().isCollisionShapeFullBlock(asState(), paramBlockGetter, paramBlockPos);
/*      */   }
/*      */   
/*      */   public ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, boolean paramBoolean) {
/* 1351 */     return getBlock().getCloneItemStack(paramLevelReader, paramBlockPos, asState(), paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean requiresCorrectToolForDrops() {
/* 1357 */     return this.requiresCorrectToolForDrops;
/*      */   }
/*      */   
/*      */   public boolean shouldSpawnTerrainParticles() {
/* 1361 */     return this.spawnTerrainParticles;
/*      */   }
/*      */   
/*      */   public NoteBlockInstrument instrument() {
/* 1365 */     return this.instrument;
/*      */   }
/*      */   protected abstract BlockState asState();
/*      */   
/* 1369 */   private static final class Cache { private static final Direction[] DIRECTIONS = Direction.values();
/* 1370 */     private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
/*      */     protected final VoxelShape collisionShape;
/*      */     protected final boolean largeCollisionShape;
/*      */     private final boolean[] faceSturdy;
/*      */     protected final boolean isCollisionShapeFullBlock;
/*      */     
/*      */     Cache(BlockState param2BlockState) {
/* 1377 */       Block block = param2BlockState.getBlock();
/*      */       
/* 1379 */       this.collisionShape = block.getCollisionShape(param2BlockState, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1380 */       if (!this.collisionShape.isEmpty() && param2BlockState.hasOffsetFunction()) {
/* 1381 */         throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) }));
/*      */       }
/* 1383 */       this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(param2Axis -> (this.collisionShape.min(param2Axis) < 0.0D || this.collisionShape.max(param2Axis) > 1.0D));
/* 1384 */       this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1385 */       for (Direction direction : DIRECTIONS) {
/* 1386 */         for (SupportType supportType : SupportType.values()) {
/* 1387 */           this.faceSturdy[getFaceSupportIndex(direction, supportType)] = supportType.isSupporting(param2BlockState, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction);
/*      */         }
/*      */       } 
/* 1390 */       this.isCollisionShapeFullBlock = Block.isShapeFullBlock(param2BlockState.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(Direction param2Direction, SupportType param2SupportType) {
/* 1394 */       return this.faceSturdy[getFaceSupportIndex(param2Direction, param2SupportType)];
/*      */     }
/*      */     
/*      */     private static int getFaceSupportIndex(Direction param2Direction, SupportType param2SupportType) {
/* 1398 */       return param2Direction.ordinal() * SUPPORT_TYPE_COUNT + param2SupportType.ordinal();
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\BlockBehaviour$BlockStateBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */