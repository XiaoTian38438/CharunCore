/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
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
/*     */ public abstract class BaseSpawner
/*     */ {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String SPAWN_DATA_TAG = "SpawnData";
/*     */   
/*     */   private static final int EVENT_SPAWN = 1;
/*     */   
/*     */   private static final int DEFAULT_SPAWN_DELAY = 20;
/*     */   
/*     */   private static final int DEFAULT_MIN_SPAWN_DELAY = 200;
/*     */   private static final int DEFAULT_MAX_SPAWN_DELAY = 800;
/*     */   private static final int DEFAULT_SPAWN_COUNT = 4;
/*     */   private static final int DEFAULT_MAX_NEARBY_ENTITIES = 6;
/*     */   private static final int DEFAULT_REQUIRED_PLAYER_RANGE = 16;
/*     */   private static final int DEFAULT_SPAWN_RANGE = 4;
/*  49 */   private int spawnDelay = 20;
/*  50 */   private WeightedList<SpawnData> spawnPotentials = WeightedList.of();
/*     */   private SpawnData nextSpawnData;
/*     */   private double spin;
/*     */   private double oSpin;
/*  54 */   private int minSpawnDelay = 200;
/*  55 */   private int maxSpawnDelay = 800;
/*  56 */   private int spawnCount = 4;
/*     */   private Entity displayEntity;
/*  58 */   private int maxNearbyEntities = 6;
/*  59 */   private int requiredPlayerRange = 16;
/*  60 */   private int spawnRange = 4;
/*     */   
/*     */   public void setEntityId(EntityType<?> paramEntityType, Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/*  63 */     getOrCreateNextSpawnData(paramLevel, paramRandomSource, paramBlockPos).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(paramEntityType).toString());
/*     */   }
/*     */   
/*     */   private boolean isNearPlayer(Level paramLevel, BlockPos paramBlockPos) {
/*  67 */     return paramLevel.hasNearbyAlivePlayer(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, this.requiredPlayerRange);
/*     */   }
/*     */   
/*     */   public void clientTick(Level paramLevel, BlockPos paramBlockPos) {
/*  71 */     if (!isNearPlayer(paramLevel, paramBlockPos)) {
/*  72 */       this.oSpin = this.spin;
/*  73 */     } else if (this.displayEntity != null) {
/*  74 */       RandomSource randomSource = paramLevel.getRandom();
/*  75 */       double d1 = paramBlockPos.getX() + randomSource.nextDouble();
/*  76 */       double d2 = paramBlockPos.getY() + randomSource.nextDouble();
/*  77 */       double d3 = paramBlockPos.getZ() + randomSource.nextDouble();
/*  78 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*  79 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.FLAME, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */       
/*  81 */       if (this.spawnDelay > 0) {
/*  82 */         this.spawnDelay--;
/*     */       }
/*  84 */       this.oSpin = this.spin;
/*  85 */       this.spin = (this.spin + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
/*     */     } 
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
/*     */   public void serverTick(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: aload_2
/*     */     //   3: invokevirtual isNearPlayer : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z
/*     */     //   6: ifeq -> 16
/*     */     //   9: aload_1
/*     */     //   10: invokevirtual isSpawnerBlockEnabled : ()Z
/*     */     //   13: ifne -> 17
/*     */     //   16: return
/*     */     //   17: aload_0
/*     */     //   18: getfield spawnDelay : I
/*     */     //   21: iconst_m1
/*     */     //   22: if_icmpne -> 31
/*     */     //   25: aload_0
/*     */     //   26: aload_1
/*     */     //   27: aload_2
/*     */     //   28: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   31: aload_0
/*     */     //   32: getfield spawnDelay : I
/*     */     //   35: ifle -> 49
/*     */     //   38: aload_0
/*     */     //   39: dup
/*     */     //   40: getfield spawnDelay : I
/*     */     //   43: iconst_1
/*     */     //   44: isub
/*     */     //   45: putfield spawnDelay : I
/*     */     //   48: return
/*     */     //   49: iconst_0
/*     */     //   50: istore_3
/*     */     //   51: aload_1
/*     */     //   52: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   55: astore #4
/*     */     //   57: aload_0
/*     */     //   58: aload_1
/*     */     //   59: aload #4
/*     */     //   61: aload_2
/*     */     //   62: invokevirtual getOrCreateNextSpawnData : (Lnet/minecraft/world/level/Level;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/SpawnData;
/*     */     //   65: astore #5
/*     */     //   67: iconst_0
/*     */     //   68: istore #6
/*     */     //   70: iload #6
/*     */     //   72: aload_0
/*     */     //   73: getfield spawnCount : I
/*     */     //   76: if_icmpge -> 733
/*     */     //   79: new net/minecraft/util/ProblemReporter$ScopedCollector
/*     */     //   82: dup
/*     */     //   83: aload_0
/*     */     //   84: <illegal opcode> get : (Lnet/minecraft/world/level/BaseSpawner;)Lnet/minecraft/util/ProblemReporter$PathElement;
/*     */     //   89: getstatic net/minecraft/world/level/BaseSpawner.LOGGER : Lorg/slf4j/Logger;
/*     */     //   92: invokespecial <init> : (Lnet/minecraft/util/ProblemReporter$PathElement;Lorg/slf4j/Logger;)V
/*     */     //   95: astore #7
/*     */     //   97: aload #7
/*     */     //   99: aload_1
/*     */     //   100: invokevirtual registryAccess : ()Lnet/minecraft/core/RegistryAccess;
/*     */     //   103: aload #5
/*     */     //   105: invokevirtual getEntityToSpawn : ()Lnet/minecraft/nbt/CompoundTag;
/*     */     //   108: invokestatic create : (Lnet/minecraft/util/ProblemReporter;Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/storage/ValueInput;
/*     */     //   111: astore #8
/*     */     //   113: aload #8
/*     */     //   115: invokestatic by : (Lnet/minecraft/world/level/storage/ValueInput;)Ljava/util/Optional;
/*     */     //   118: astore #9
/*     */     //   120: aload #9
/*     */     //   122: invokevirtual isEmpty : ()Z
/*     */     //   125: ifeq -> 140
/*     */     //   128: aload_0
/*     */     //   129: aload_1
/*     */     //   130: aload_2
/*     */     //   131: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   134: aload #7
/*     */     //   136: invokevirtual close : ()V
/*     */     //   139: return
/*     */     //   140: aload #8
/*     */     //   142: ldc_w 'Pos'
/*     */     //   145: getstatic net/minecraft/world/phys/Vec3.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   148: invokeinterface read : (Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
/*     */     //   153: aload_0
/*     */     //   154: aload_2
/*     */     //   155: aload #4
/*     */     //   157: <illegal opcode> get : (Lnet/minecraft/world/level/BaseSpawner;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Ljava/util/function/Supplier;
/*     */     //   162: invokevirtual orElseGet : (Ljava/util/function/Supplier;)Ljava/lang/Object;
/*     */     //   165: checkcast net/minecraft/world/phys/Vec3
/*     */     //   168: astore #10
/*     */     //   170: aload_1
/*     */     //   171: aload #9
/*     */     //   173: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   176: checkcast net/minecraft/world/entity/EntityType
/*     */     //   179: aload #10
/*     */     //   181: getfield x : D
/*     */     //   184: aload #10
/*     */     //   186: getfield y : D
/*     */     //   189: aload #10
/*     */     //   191: getfield z : D
/*     */     //   194: invokevirtual getSpawnAABB : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */     //   197: invokevirtual noCollision : (Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   200: ifne -> 211
/*     */     //   203: aload #7
/*     */     //   205: invokevirtual close : ()V
/*     */     //   208: goto -> 727
/*     */     //   211: aload #10
/*     */     //   213: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   216: astore #11
/*     */     //   218: aload #5
/*     */     //   220: invokevirtual getCustomSpawnRules : ()Ljava/util/Optional;
/*     */     //   223: invokevirtual isPresent : ()Z
/*     */     //   226: ifeq -> 299
/*     */     //   229: aload #9
/*     */     //   231: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   234: checkcast net/minecraft/world/entity/EntityType
/*     */     //   237: invokevirtual getCategory : ()Lnet/minecraft/world/entity/MobCategory;
/*     */     //   240: invokevirtual isFriendly : ()Z
/*     */     //   243: ifne -> 264
/*     */     //   246: aload_1
/*     */     //   247: invokevirtual getDifficulty : ()Lnet/minecraft/world/Difficulty;
/*     */     //   250: getstatic net/minecraft/world/Difficulty.PEACEFUL : Lnet/minecraft/world/Difficulty;
/*     */     //   253: if_acmpne -> 264
/*     */     //   256: aload #7
/*     */     //   258: invokevirtual close : ()V
/*     */     //   261: goto -> 727
/*     */     //   264: aload #5
/*     */     //   266: invokevirtual getCustomSpawnRules : ()Ljava/util/Optional;
/*     */     //   269: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   272: checkcast net/minecraft/world/level/SpawnData$CustomSpawnRules
/*     */     //   275: astore #12
/*     */     //   277: aload #12
/*     */     //   279: aload #11
/*     */     //   281: aload_1
/*     */     //   282: invokevirtual isValidPosition : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/server/level/ServerLevel;)Z
/*     */     //   285: ifne -> 296
/*     */     //   288: aload #7
/*     */     //   290: invokevirtual close : ()V
/*     */     //   293: goto -> 727
/*     */     //   296: goto -> 331
/*     */     //   299: aload #9
/*     */     //   301: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   304: checkcast net/minecraft/world/entity/EntityType
/*     */     //   307: aload_1
/*     */     //   308: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   311: aload #11
/*     */     //   313: aload_1
/*     */     //   314: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   317: invokestatic checkSpawnRules : (Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z
/*     */     //   320: ifne -> 331
/*     */     //   323: aload #7
/*     */     //   325: invokevirtual close : ()V
/*     */     //   328: goto -> 727
/*     */     //   331: aload #8
/*     */     //   333: aload_1
/*     */     //   334: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   337: aload #10
/*     */     //   339: <illegal opcode> process : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/entity/EntityProcessor;
/*     */     //   344: invokestatic loadEntityRecursive : (Lnet/minecraft/world/level/storage/ValueInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/EntityProcessor;)Lnet/minecraft/world/entity/Entity;
/*     */     //   347: astore #12
/*     */     //   349: aload #12
/*     */     //   351: ifnonnull -> 366
/*     */     //   354: aload_0
/*     */     //   355: aload_1
/*     */     //   356: aload_2
/*     */     //   357: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   360: aload #7
/*     */     //   362: invokevirtual close : ()V
/*     */     //   365: return
/*     */     //   366: aload_1
/*     */     //   367: aload #12
/*     */     //   369: invokevirtual getClass : ()Ljava/lang/Class;
/*     */     //   372: invokestatic forExactClass : (Ljava/lang/Class;)Lnet/minecraft/world/level/entity/EntityTypeTest;
/*     */     //   375: new net/minecraft/world/phys/AABB
/*     */     //   378: dup
/*     */     //   379: aload_2
/*     */     //   380: invokevirtual getX : ()I
/*     */     //   383: i2d
/*     */     //   384: aload_2
/*     */     //   385: invokevirtual getY : ()I
/*     */     //   388: i2d
/*     */     //   389: aload_2
/*     */     //   390: invokevirtual getZ : ()I
/*     */     //   393: i2d
/*     */     //   394: aload_2
/*     */     //   395: invokevirtual getX : ()I
/*     */     //   398: iconst_1
/*     */     //   399: iadd
/*     */     //   400: i2d
/*     */     //   401: aload_2
/*     */     //   402: invokevirtual getY : ()I
/*     */     //   405: iconst_1
/*     */     //   406: iadd
/*     */     //   407: i2d
/*     */     //   408: aload_2
/*     */     //   409: invokevirtual getZ : ()I
/*     */     //   412: iconst_1
/*     */     //   413: iadd
/*     */     //   414: i2d
/*     */     //   415: invokespecial <init> : (DDDDDD)V
/*     */     //   418: aload_0
/*     */     //   419: getfield spawnRange : I
/*     */     //   422: i2d
/*     */     //   423: invokevirtual inflate : (D)Lnet/minecraft/world/phys/AABB;
/*     */     //   426: getstatic net/minecraft/world/entity/EntitySelector.NO_SPECTATORS : Ljava/util/function/Predicate;
/*     */     //   429: invokevirtual getEntities : (Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */     //   432: invokeinterface size : ()I
/*     */     //   437: istore #13
/*     */     //   439: iload #13
/*     */     //   441: aload_0
/*     */     //   442: getfield maxNearbyEntities : I
/*     */     //   445: if_icmplt -> 460
/*     */     //   448: aload_0
/*     */     //   449: aload_1
/*     */     //   450: aload_2
/*     */     //   451: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   454: aload #7
/*     */     //   456: invokevirtual close : ()V
/*     */     //   459: return
/*     */     //   460: aload #12
/*     */     //   462: aload #12
/*     */     //   464: invokevirtual getX : ()D
/*     */     //   467: aload #12
/*     */     //   469: invokevirtual getY : ()D
/*     */     //   472: aload #12
/*     */     //   474: invokevirtual getZ : ()D
/*     */     //   477: aload #4
/*     */     //   479: invokeinterface nextFloat : ()F
/*     */     //   484: ldc_w 360.0
/*     */     //   487: fmul
/*     */     //   488: fconst_0
/*     */     //   489: invokevirtual snapTo : (DDDFF)V
/*     */     //   492: aload #12
/*     */     //   494: instanceof net/minecraft/world/entity/Mob
/*     */     //   497: ifeq -> 638
/*     */     //   500: aload #12
/*     */     //   502: checkcast net/minecraft/world/entity/Mob
/*     */     //   505: astore #14
/*     */     //   507: aload #5
/*     */     //   509: invokevirtual getCustomSpawnRules : ()Ljava/util/Optional;
/*     */     //   512: invokevirtual isEmpty : ()Z
/*     */     //   515: ifeq -> 538
/*     */     //   518: aload #14
/*     */     //   520: aload_1
/*     */     //   521: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   524: invokevirtual checkSpawnRules : (Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;)Z
/*     */     //   527: ifne -> 538
/*     */     //   530: aload #7
/*     */     //   532: invokevirtual close : ()V
/*     */     //   535: goto -> 727
/*     */     //   538: aload #14
/*     */     //   540: aload_1
/*     */     //   541: invokevirtual checkSpawnObstruction : (Lnet/minecraft/world/level/LevelReader;)Z
/*     */     //   544: ifne -> 555
/*     */     //   547: aload #7
/*     */     //   549: invokevirtual close : ()V
/*     */     //   552: goto -> 727
/*     */     //   555: aload #5
/*     */     //   557: invokevirtual getEntityToSpawn : ()Lnet/minecraft/nbt/CompoundTag;
/*     */     //   560: invokevirtual size : ()I
/*     */     //   563: iconst_1
/*     */     //   564: if_icmpne -> 587
/*     */     //   567: aload #5
/*     */     //   569: invokevirtual getEntityToSpawn : ()Lnet/minecraft/nbt/CompoundTag;
/*     */     //   572: ldc 'id'
/*     */     //   574: invokevirtual getString : (Ljava/lang/String;)Ljava/util/Optional;
/*     */     //   577: invokevirtual isPresent : ()Z
/*     */     //   580: ifeq -> 587
/*     */     //   583: iconst_1
/*     */     //   584: goto -> 588
/*     */     //   587: iconst_0
/*     */     //   588: istore #15
/*     */     //   590: iload #15
/*     */     //   592: ifeq -> 618
/*     */     //   595: aload #12
/*     */     //   597: checkcast net/minecraft/world/entity/Mob
/*     */     //   600: aload_1
/*     */     //   601: aload_1
/*     */     //   602: aload #12
/*     */     //   604: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   607: invokevirtual getCurrentDifficultyAt : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/DifficultyInstance;
/*     */     //   610: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   613: aconst_null
/*     */     //   614: invokevirtual finalizeSpawn : (Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;
/*     */     //   617: pop
/*     */     //   618: aload #5
/*     */     //   620: invokevirtual getEquipment : ()Ljava/util/Optional;
/*     */     //   623: aload #14
/*     */     //   625: dup
/*     */     //   626: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   629: pop
/*     */     //   630: <illegal opcode> accept : (Lnet/minecraft/world/entity/Mob;)Ljava/util/function/Consumer;
/*     */     //   635: invokevirtual ifPresent : (Ljava/util/function/Consumer;)V
/*     */     //   638: aload_1
/*     */     //   639: aload #12
/*     */     //   641: invokevirtual tryAddFreshEntityWithPassengers : (Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   644: ifne -> 659
/*     */     //   647: aload_0
/*     */     //   648: aload_1
/*     */     //   649: aload_2
/*     */     //   650: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   653: aload #7
/*     */     //   655: invokevirtual close : ()V
/*     */     //   658: return
/*     */     //   659: aload_1
/*     */     //   660: sipush #2004
/*     */     //   663: aload_2
/*     */     //   664: iconst_0
/*     */     //   665: invokevirtual levelEvent : (ILnet/minecraft/core/BlockPos;I)V
/*     */     //   668: aload_1
/*     */     //   669: aload #12
/*     */     //   671: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_PLACE : Lnet/minecraft/core/Holder$Reference;
/*     */     //   674: aload #11
/*     */     //   676: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   679: aload #12
/*     */     //   681: instanceof net/minecraft/world/entity/Mob
/*     */     //   684: ifeq -> 695
/*     */     //   687: aload #12
/*     */     //   689: checkcast net/minecraft/world/entity/Mob
/*     */     //   692: invokevirtual spawnAnim : ()V
/*     */     //   695: iconst_1
/*     */     //   696: istore_3
/*     */     //   697: aload #7
/*     */     //   699: invokevirtual close : ()V
/*     */     //   702: goto -> 727
/*     */     //   705: astore #8
/*     */     //   707: aload #7
/*     */     //   709: invokevirtual close : ()V
/*     */     //   712: goto -> 724
/*     */     //   715: astore #9
/*     */     //   717: aload #8
/*     */     //   719: aload #9
/*     */     //   721: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   724: aload #8
/*     */     //   726: athrow
/*     */     //   727: iinc #6, 1
/*     */     //   730: goto -> 70
/*     */     //   733: iload_3
/*     */     //   734: ifeq -> 743
/*     */     //   737: aload_0
/*     */     //   738: aload_1
/*     */     //   739: aload_2
/*     */     //   740: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   743: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #90	-> 0
/*     */     //   #91	-> 16
/*     */     //   #94	-> 17
/*     */     //   #95	-> 25
/*     */     //   #98	-> 31
/*     */     //   #99	-> 38
/*     */     //   #100	-> 48
/*     */     //   #103	-> 49
/*     */     //   #105	-> 51
/*     */     //   #106	-> 57
/*     */     //   #107	-> 67
/*     */     //   #108	-> 79
/*     */     //   #109	-> 97
/*     */     //   #110	-> 113
/*     */     //   #111	-> 120
/*     */     //   #112	-> 128
/*     */     //   #185	-> 134
/*     */     //   #113	-> 139
/*     */     //   #116	-> 140
/*     */     //   #122	-> 170
/*     */     //   #185	-> 203
/*     */     //   #123	-> 208
/*     */     //   #126	-> 211
/*     */     //   #127	-> 218
/*     */     //   #128	-> 229
/*     */     //   #185	-> 256
/*     */     //   #129	-> 261
/*     */     //   #132	-> 264
/*     */     //   #133	-> 277
/*     */     //   #185	-> 288
/*     */     //   #134	-> 293
/*     */     //   #136	-> 296
/*     */     //   #137	-> 299
/*     */     //   #185	-> 323
/*     */     //   #138	-> 328
/*     */     //   #142	-> 331
/*     */     //   #146	-> 349
/*     */     //   #147	-> 354
/*     */     //   #185	-> 360
/*     */     //   #148	-> 365
/*     */     //   #151	-> 366
/*     */     //   #152	-> 439
/*     */     //   #153	-> 448
/*     */     //   #185	-> 454
/*     */     //   #154	-> 459
/*     */     //   #157	-> 460
/*     */     //   #158	-> 492
/*     */     //   #159	-> 507
/*     */     //   #185	-> 530
/*     */     //   #160	-> 535
/*     */     //   #162	-> 538
/*     */     //   #185	-> 547
/*     */     //   #163	-> 552
/*     */     //   #166	-> 555
/*     */     //   #167	-> 590
/*     */     //   #168	-> 595
/*     */     //   #171	-> 618
/*     */     //   #174	-> 638
/*     */     //   #175	-> 647
/*     */     //   #185	-> 653
/*     */     //   #176	-> 658
/*     */     //   #179	-> 659
/*     */     //   #180	-> 668
/*     */     //   #181	-> 679
/*     */     //   #182	-> 687
/*     */     //   #184	-> 695
/*     */     //   #185	-> 697
/*     */     //   #108	-> 705
/*     */     //   #107	-> 727
/*     */     //   #188	-> 733
/*     */     //   #189	-> 737
/*     */     //   #191	-> 743
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   97	134	705	java/lang/Throwable
/*     */     //   140	203	705	java/lang/Throwable
/*     */     //   211	256	705	java/lang/Throwable
/*     */     //   264	288	705	java/lang/Throwable
/*     */     //   296	323	705	java/lang/Throwable
/*     */     //   331	360	705	java/lang/Throwable
/*     */     //   366	454	705	java/lang/Throwable
/*     */     //   460	530	705	java/lang/Throwable
/*     */     //   538	547	705	java/lang/Throwable
/*     */     //   555	653	705	java/lang/Throwable
/*     */     //   659	697	705	java/lang/Throwable
/*     */     //   707	712	715	java/lang/Throwable
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
/*     */   private void delay(Level paramLevel, BlockPos paramBlockPos) {
/* 194 */     RandomSource randomSource = paramLevel.random;
/* 195 */     if (this.maxSpawnDelay <= this.minSpawnDelay) {
/* 196 */       this.spawnDelay = this.minSpawnDelay;
/*     */     } else {
/* 198 */       this.spawnDelay = this.minSpawnDelay + randomSource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
/*     */     } 
/*     */     
/* 201 */     this.spawnPotentials.getRandom(randomSource).ifPresent(paramSpawnData -> setNextSpawnData(paramLevel, paramBlockPos, paramSpawnData));
/*     */     
/* 203 */     broadcastEvent(paramLevel, paramBlockPos, 1);
/*     */   }
/*     */   
/*     */   public void load(Level paramLevel, BlockPos paramBlockPos, ValueInput paramValueInput) {
/* 207 */     this.spawnDelay = paramValueInput.getShortOr("Delay", (short)20);
/*     */     
/* 209 */     paramValueInput.read("SpawnData", SpawnData.CODEC)
/* 210 */       .ifPresent(paramSpawnData -> setNextSpawnData(paramLevel, paramBlockPos, paramSpawnData));
/*     */     
/* 212 */     this
/* 213 */       .spawnPotentials = paramValueInput.read("SpawnPotentials", SpawnData.LIST_CODEC).orElseGet(() -> WeightedList.of((this.nextSpawnData != null) ? this.nextSpawnData : new SpawnData()));
/*     */     
/* 215 */     this.minSpawnDelay = paramValueInput.getIntOr("MinSpawnDelay", 200);
/* 216 */     this.maxSpawnDelay = paramValueInput.getIntOr("MaxSpawnDelay", 800);
/* 217 */     this.spawnCount = paramValueInput.getIntOr("SpawnCount", 4);
/*     */     
/* 219 */     this.maxNearbyEntities = paramValueInput.getIntOr("MaxNearbyEntities", 6);
/* 220 */     this.requiredPlayerRange = paramValueInput.getIntOr("RequiredPlayerRange", 16);
/*     */     
/* 222 */     this.spawnRange = paramValueInput.getIntOr("SpawnRange", 4);
/*     */     
/* 224 */     this.displayEntity = null;
/*     */   }
/*     */   
/*     */   public void save(ValueOutput paramValueOutput) {
/* 228 */     paramValueOutput.putShort("Delay", (short)this.spawnDelay);
/* 229 */     paramValueOutput.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
/* 230 */     paramValueOutput.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
/* 231 */     paramValueOutput.putShort("SpawnCount", (short)this.spawnCount);
/* 232 */     paramValueOutput.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
/* 233 */     paramValueOutput.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
/* 234 */     paramValueOutput.putShort("SpawnRange", (short)this.spawnRange);
/* 235 */     paramValueOutput.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
/* 236 */     paramValueOutput.store("SpawnPotentials", SpawnData.LIST_CODEC, this.spawnPotentials);
/*     */   }
/*     */   
/*     */   public Entity getOrCreateDisplayEntity(Level paramLevel, BlockPos paramBlockPos) {
/* 240 */     if (this.displayEntity == null) {
/* 241 */       CompoundTag compoundTag = getOrCreateNextSpawnData(paramLevel, paramLevel.getRandom(), paramBlockPos).getEntityToSpawn();
/* 242 */       if (compoundTag.getString("id").isEmpty()) {
/* 243 */         return null;
/*     */       }
/* 245 */       this.displayEntity = EntityType.loadEntityRecursive(compoundTag, paramLevel, EntitySpawnReason.SPAWNER, EntityProcessor.NOP);
/* 246 */       if (compoundTag.size() != 1 || this.displayEntity instanceof net.minecraft.world.entity.Mob);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     return this.displayEntity;
/*     */   }
/*     */   
/*     */   public boolean onEventTriggered(Level paramLevel, int paramInt) {
/* 256 */     if (paramInt == 1) {
/* 257 */       if (paramLevel.isClientSide()) {
/* 258 */         this.spawnDelay = this.minSpawnDelay;
/*     */       }
/* 260 */       return true;
/*     */     } 
/* 262 */     return false;
/*     */   }
/*     */   
/*     */   protected void setNextSpawnData(Level paramLevel, BlockPos paramBlockPos, SpawnData paramSpawnData) {
/* 266 */     this.nextSpawnData = paramSpawnData;
/*     */   }
/*     */   
/*     */   private SpawnData getOrCreateNextSpawnData(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 270 */     if (this.nextSpawnData != null) {
/* 271 */       return this.nextSpawnData;
/*     */     }
/* 273 */     setNextSpawnData(paramLevel, paramBlockPos, this.spawnPotentials.getRandom(paramRandomSource).orElseGet(SpawnData::new));
/* 274 */     return this.nextSpawnData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSpin() {
/* 280 */     return this.spin;
/*     */   }
/*     */   
/*     */   public double getOSpin() {
/* 284 */     return this.oSpin;
/*     */   }
/*     */   
/*     */   public abstract void broadcastEvent(Level paramLevel, BlockPos paramBlockPos, int paramInt);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\BaseSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */