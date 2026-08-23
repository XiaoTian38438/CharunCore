/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundBundlePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundMoveMinecartPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
/*     */ import net.minecraft.network.protocol.game.VecDeltaCodec;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerEntity
/*     */ {
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   private static final int TOLERANCE_LEVEL_ROTATION = 1;
/*     */ 
/*     */   
/*     */   private static final double TOLERANCE_LEVEL_POSITION = 7.62939453125E-6D;
/*     */ 
/*     */   
/*     */   public static final int FORCED_POS_UPDATE_PERIOD = 60;
/*     */ 
/*     */   
/*     */   private static final int FORCED_TELEPORT_PERIOD = 400;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Entity entity;
/*     */   
/*     */   private final int updateInterval;
/*     */   
/*     */   private final boolean trackDelta;
/*     */   
/*     */   private final Synchronizer synchronizer;
/*     */   
/*  78 */   private final VecDeltaCodec positionCodec = new VecDeltaCodec();
/*     */   private byte lastSentYRot;
/*     */   private byte lastSentXRot;
/*     */   private byte lastSentYHeadRot;
/*     */   private Vec3 lastSentMovement;
/*     */   private int tickCount;
/*     */   private int teleportDelay;
/*  85 */   private List<Entity> lastPassengers = Collections.emptyList();
/*     */   
/*     */   private boolean wasRiding;
/*     */   
/*     */   private boolean wasOnGround;
/*     */   private List<SynchedEntityData.DataValue<?>> trackedDataValues;
/*     */   
/*     */   public ServerEntity(ServerLevel paramServerLevel, Entity paramEntity, int paramInt, boolean paramBoolean, Synchronizer paramSynchronizer) {
/*  93 */     this.level = paramServerLevel;
/*  94 */     this.synchronizer = paramSynchronizer;
/*  95 */     this.entity = paramEntity;
/*  96 */     this.updateInterval = paramInt;
/*  97 */     this.trackDelta = paramBoolean;
/*     */ 
/*     */     
/* 100 */     this.positionCodec.setBase(paramEntity.trackingPosition());
/*     */     
/* 102 */     this.lastSentMovement = paramEntity.getDeltaMovement();
/*     */     
/* 104 */     this.lastSentYRot = Mth.packDegrees(paramEntity.getYRot());
/* 105 */     this.lastSentXRot = Mth.packDegrees(paramEntity.getXRot());
/* 106 */     this.lastSentYHeadRot = Mth.packDegrees(paramEntity.getYHeadRot());
/* 107 */     this.wasOnGround = paramEntity.onGround();
/*     */     
/* 109 */     this.trackedDataValues = paramEntity.getEntityData().getNonDefaultValues();
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
/*     */   public void sendChanges() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   4: invokevirtual updateDataBeforeSync : ()V
/*     */     //   7: aload_0
/*     */     //   8: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   11: invokevirtual getPassengers : ()Ljava/util/List;
/*     */     //   14: astore_1
/*     */     //   15: aload_1
/*     */     //   16: aload_0
/*     */     //   17: getfield lastPassengers : Ljava/util/List;
/*     */     //   20: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   25: ifne -> 60
/*     */     //   28: aload_0
/*     */     //   29: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   32: new net/minecraft/network/protocol/game/ClientboundSetPassengersPacket
/*     */     //   35: dup
/*     */     //   36: aload_0
/*     */     //   37: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   40: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;)V
/*     */     //   43: aload_0
/*     */     //   44: aload_1
/*     */     //   45: <illegal opcode> test : (Lnet/minecraft/server/level/ServerEntity;Ljava/util/List;)Ljava/util/function/Predicate;
/*     */     //   50: invokeinterface sendToTrackingPlayersFiltered : (Lnet/minecraft/network/protocol/Packet;Ljava/util/function/Predicate;)V
/*     */     //   55: aload_0
/*     */     //   56: aload_1
/*     */     //   57: putfield lastPassengers : Ljava/util/List;
/*     */     //   60: aload_0
/*     */     //   61: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   64: astore_3
/*     */     //   65: aload_3
/*     */     //   66: instanceof net/minecraft/world/entity/decoration/ItemFrame
/*     */     //   69: ifeq -> 207
/*     */     //   72: aload_3
/*     */     //   73: checkcast net/minecraft/world/entity/decoration/ItemFrame
/*     */     //   76: astore_2
/*     */     //   77: aload_0
/*     */     //   78: getfield tickCount : I
/*     */     //   81: bipush #10
/*     */     //   83: irem
/*     */     //   84: ifne -> 207
/*     */     //   87: aload_2
/*     */     //   88: invokevirtual getItem : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   91: astore_3
/*     */     //   92: aload_3
/*     */     //   93: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*     */     //   96: instanceof net/minecraft/world/item/MapItem
/*     */     //   99: ifeq -> 203
/*     */     //   102: aload_3
/*     */     //   103: getstatic net/minecraft/core/component/DataComponents.MAP_ID : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   106: invokevirtual get : (Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;
/*     */     //   109: checkcast net/minecraft/world/level/saveddata/maps/MapId
/*     */     //   112: astore #4
/*     */     //   114: aload #4
/*     */     //   116: aload_0
/*     */     //   117: getfield level : Lnet/minecraft/server/level/ServerLevel;
/*     */     //   120: invokestatic getSavedData : (Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;
/*     */     //   123: astore #5
/*     */     //   125: aload #5
/*     */     //   127: ifnull -> 203
/*     */     //   130: aload_0
/*     */     //   131: getfield level : Lnet/minecraft/server/level/ServerLevel;
/*     */     //   134: invokevirtual players : ()Ljava/util/List;
/*     */     //   137: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   142: astore #6
/*     */     //   144: aload #6
/*     */     //   146: invokeinterface hasNext : ()Z
/*     */     //   151: ifeq -> 203
/*     */     //   154: aload #6
/*     */     //   156: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   161: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   164: astore #7
/*     */     //   166: aload #5
/*     */     //   168: aload #7
/*     */     //   170: aload_3
/*     */     //   171: invokevirtual tickCarriedBy : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   174: aload #5
/*     */     //   176: aload #4
/*     */     //   178: aload #7
/*     */     //   180: invokevirtual getUpdatePacket : (Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/network/protocol/Packet;
/*     */     //   183: astore #8
/*     */     //   185: aload #8
/*     */     //   187: ifnull -> 200
/*     */     //   190: aload #7
/*     */     //   192: getfield connection : Lnet/minecraft/server/network/ServerGamePacketListenerImpl;
/*     */     //   195: aload #8
/*     */     //   197: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   200: goto -> 144
/*     */     //   203: aload_0
/*     */     //   204: invokevirtual sendDirtyEntityData : ()V
/*     */     //   207: aload_0
/*     */     //   208: getfield tickCount : I
/*     */     //   211: aload_0
/*     */     //   212: getfield updateInterval : I
/*     */     //   215: irem
/*     */     //   216: ifeq -> 242
/*     */     //   219: aload_0
/*     */     //   220: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   223: getfield needsSync : Z
/*     */     //   226: ifne -> 242
/*     */     //   229: aload_0
/*     */     //   230: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   233: invokevirtual getEntityData : ()Lnet/minecraft/network/syncher/SynchedEntityData;
/*     */     //   236: invokevirtual isDirty : ()Z
/*     */     //   239: ifeq -> 1144
/*     */     //   242: aload_0
/*     */     //   243: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   246: invokevirtual getYRot : ()F
/*     */     //   249: invokestatic packDegrees : (F)B
/*     */     //   252: istore_2
/*     */     //   253: aload_0
/*     */     //   254: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   257: invokevirtual getXRot : ()F
/*     */     //   260: invokestatic packDegrees : (F)B
/*     */     //   263: istore_3
/*     */     //   264: iload_2
/*     */     //   265: aload_0
/*     */     //   266: getfield lastSentYRot : B
/*     */     //   269: isub
/*     */     //   270: invokestatic abs : (I)I
/*     */     //   273: iconst_1
/*     */     //   274: if_icmpge -> 290
/*     */     //   277: iload_3
/*     */     //   278: aload_0
/*     */     //   279: getfield lastSentXRot : B
/*     */     //   282: isub
/*     */     //   283: invokestatic abs : (I)I
/*     */     //   286: iconst_1
/*     */     //   287: if_icmplt -> 294
/*     */     //   290: iconst_1
/*     */     //   291: goto -> 295
/*     */     //   294: iconst_0
/*     */     //   295: istore #4
/*     */     //   297: aload_0
/*     */     //   298: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   301: invokevirtual isPassenger : ()Z
/*     */     //   304: ifeq -> 380
/*     */     //   307: iload #4
/*     */     //   309: ifeq -> 354
/*     */     //   312: aload_0
/*     */     //   313: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   316: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Rot
/*     */     //   319: dup
/*     */     //   320: aload_0
/*     */     //   321: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   324: invokevirtual getId : ()I
/*     */     //   327: iload_2
/*     */     //   328: iload_3
/*     */     //   329: aload_0
/*     */     //   330: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   333: invokevirtual onGround : ()Z
/*     */     //   336: invokespecial <init> : (IBBZ)V
/*     */     //   339: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   344: aload_0
/*     */     //   345: iload_2
/*     */     //   346: putfield lastSentYRot : B
/*     */     //   349: aload_0
/*     */     //   350: iload_3
/*     */     //   351: putfield lastSentXRot : B
/*     */     //   354: aload_0
/*     */     //   355: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   358: aload_0
/*     */     //   359: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   362: invokevirtual trackingPosition : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   365: invokevirtual setBase : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   368: aload_0
/*     */     //   369: invokevirtual sendDirtyEntityData : ()V
/*     */     //   372: aload_0
/*     */     //   373: iconst_1
/*     */     //   374: putfield wasRiding : Z
/*     */     //   377: goto -> 1082
/*     */     //   380: aload_0
/*     */     //   381: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   384: astore #7
/*     */     //   386: aload #7
/*     */     //   388: instanceof net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   391: ifeq -> 436
/*     */     //   394: aload #7
/*     */     //   396: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   399: astore #5
/*     */     //   401: aload #5
/*     */     //   403: invokevirtual getBehavior : ()Lnet/minecraft/world/entity/vehicle/minecart/MinecartBehavior;
/*     */     //   406: astore #7
/*     */     //   408: aload #7
/*     */     //   410: instanceof net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   413: ifeq -> 436
/*     */     //   416: aload #7
/*     */     //   418: checkcast net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   421: astore #6
/*     */     //   423: aload_0
/*     */     //   424: aload #6
/*     */     //   426: iload_2
/*     */     //   427: iload_3
/*     */     //   428: iload #4
/*     */     //   430: invokevirtual handleMinecartPosRot : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;BBZ)V
/*     */     //   433: goto -> 1082
/*     */     //   436: aload_0
/*     */     //   437: dup
/*     */     //   438: getfield teleportDelay : I
/*     */     //   441: iconst_1
/*     */     //   442: iadd
/*     */     //   443: putfield teleportDelay : I
/*     */     //   446: aload_0
/*     */     //   447: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   450: invokevirtual trackingPosition : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   453: astore #7
/*     */     //   455: aload_0
/*     */     //   456: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   459: aload #7
/*     */     //   461: invokevirtual delta : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   464: invokevirtual lengthSqr : ()D
/*     */     //   467: ldc2_w 7.62939453125E-6
/*     */     //   470: dcmpl
/*     */     //   471: iflt -> 478
/*     */     //   474: iconst_1
/*     */     //   475: goto -> 479
/*     */     //   478: iconst_0
/*     */     //   479: istore #8
/*     */     //   481: aconst_null
/*     */     //   482: astore #9
/*     */     //   484: iload #8
/*     */     //   486: ifne -> 499
/*     */     //   489: aload_0
/*     */     //   490: getfield tickCount : I
/*     */     //   493: bipush #60
/*     */     //   495: irem
/*     */     //   496: ifne -> 503
/*     */     //   499: iconst_1
/*     */     //   500: goto -> 504
/*     */     //   503: iconst_0
/*     */     //   504: istore #10
/*     */     //   506: iconst_0
/*     */     //   507: istore #11
/*     */     //   509: iconst_0
/*     */     //   510: istore #12
/*     */     //   512: aload_0
/*     */     //   513: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   516: aload #7
/*     */     //   518: invokevirtual encodeX : (Lnet/minecraft/world/phys/Vec3;)J
/*     */     //   521: lstore #13
/*     */     //   523: aload_0
/*     */     //   524: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   527: aload #7
/*     */     //   529: invokevirtual encodeY : (Lnet/minecraft/world/phys/Vec3;)J
/*     */     //   532: lstore #15
/*     */     //   534: aload_0
/*     */     //   535: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   538: aload #7
/*     */     //   540: invokevirtual encodeZ : (Lnet/minecraft/world/phys/Vec3;)J
/*     */     //   543: lstore #17
/*     */     //   545: lload #13
/*     */     //   547: ldc2_w -32768
/*     */     //   550: lcmp
/*     */     //   551: iflt -> 599
/*     */     //   554: lload #13
/*     */     //   556: ldc2_w 32767
/*     */     //   559: lcmp
/*     */     //   560: ifgt -> 599
/*     */     //   563: lload #15
/*     */     //   565: ldc2_w -32768
/*     */     //   568: lcmp
/*     */     //   569: iflt -> 599
/*     */     //   572: lload #15
/*     */     //   574: ldc2_w 32767
/*     */     //   577: lcmp
/*     */     //   578: ifgt -> 599
/*     */     //   581: lload #17
/*     */     //   583: ldc2_w -32768
/*     */     //   586: lcmp
/*     */     //   587: iflt -> 599
/*     */     //   590: lload #17
/*     */     //   592: ldc2_w 32767
/*     */     //   595: lcmp
/*     */     //   596: ifle -> 603
/*     */     //   599: iconst_1
/*     */     //   600: goto -> 604
/*     */     //   603: iconst_0
/*     */     //   604: istore #19
/*     */     //   606: aload_0
/*     */     //   607: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   610: invokevirtual getRequiresPrecisePosition : ()Z
/*     */     //   613: ifne -> 652
/*     */     //   616: iload #19
/*     */     //   618: ifne -> 652
/*     */     //   621: aload_0
/*     */     //   622: getfield teleportDelay : I
/*     */     //   625: sipush #400
/*     */     //   628: if_icmpgt -> 652
/*     */     //   631: aload_0
/*     */     //   632: getfield wasRiding : Z
/*     */     //   635: ifne -> 652
/*     */     //   638: aload_0
/*     */     //   639: getfield wasOnGround : Z
/*     */     //   642: aload_0
/*     */     //   643: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   646: invokevirtual onGround : ()Z
/*     */     //   649: if_icmpeq -> 686
/*     */     //   652: aload_0
/*     */     //   653: aload_0
/*     */     //   654: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   657: invokevirtual onGround : ()Z
/*     */     //   660: putfield wasOnGround : Z
/*     */     //   663: aload_0
/*     */     //   664: iconst_0
/*     */     //   665: putfield teleportDelay : I
/*     */     //   668: aload_0
/*     */     //   669: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   672: invokestatic of : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;
/*     */     //   675: astore #9
/*     */     //   677: iconst_1
/*     */     //   678: istore #11
/*     */     //   680: iconst_1
/*     */     //   681: istore #12
/*     */     //   683: goto -> 831
/*     */     //   686: iload #10
/*     */     //   688: ifeq -> 696
/*     */     //   691: iload #4
/*     */     //   693: ifne -> 706
/*     */     //   696: aload_0
/*     */     //   697: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   700: instanceof net/minecraft/world/entity/projectile/arrow/AbstractArrow
/*     */     //   703: ifeq -> 752
/*     */     //   706: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$PosRot
/*     */     //   709: dup
/*     */     //   710: aload_0
/*     */     //   711: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   714: invokevirtual getId : ()I
/*     */     //   717: lload #13
/*     */     //   719: l2i
/*     */     //   720: i2s
/*     */     //   721: lload #15
/*     */     //   723: l2i
/*     */     //   724: i2s
/*     */     //   725: lload #17
/*     */     //   727: l2i
/*     */     //   728: i2s
/*     */     //   729: iload_2
/*     */     //   730: iload_3
/*     */     //   731: aload_0
/*     */     //   732: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   735: invokevirtual onGround : ()Z
/*     */     //   738: invokespecial <init> : (ISSSBBZ)V
/*     */     //   741: astore #9
/*     */     //   743: iconst_1
/*     */     //   744: istore #11
/*     */     //   746: iconst_1
/*     */     //   747: istore #12
/*     */     //   749: goto -> 831
/*     */     //   752: iload #10
/*     */     //   754: ifeq -> 798
/*     */     //   757: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Pos
/*     */     //   760: dup
/*     */     //   761: aload_0
/*     */     //   762: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   765: invokevirtual getId : ()I
/*     */     //   768: lload #13
/*     */     //   770: l2i
/*     */     //   771: i2s
/*     */     //   772: lload #15
/*     */     //   774: l2i
/*     */     //   775: i2s
/*     */     //   776: lload #17
/*     */     //   778: l2i
/*     */     //   779: i2s
/*     */     //   780: aload_0
/*     */     //   781: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   784: invokevirtual onGround : ()Z
/*     */     //   787: invokespecial <init> : (ISSSZ)V
/*     */     //   790: astore #9
/*     */     //   792: iconst_1
/*     */     //   793: istore #11
/*     */     //   795: goto -> 831
/*     */     //   798: iload #4
/*     */     //   800: ifeq -> 831
/*     */     //   803: new net/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Rot
/*     */     //   806: dup
/*     */     //   807: aload_0
/*     */     //   808: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   811: invokevirtual getId : ()I
/*     */     //   814: iload_2
/*     */     //   815: iload_3
/*     */     //   816: aload_0
/*     */     //   817: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   820: invokevirtual onGround : ()Z
/*     */     //   823: invokespecial <init> : (IBBZ)V
/*     */     //   826: astore #9
/*     */     //   828: iconst_1
/*     */     //   829: istore #12
/*     */     //   831: aload_0
/*     */     //   832: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   835: getfield needsSync : Z
/*     */     //   838: ifne -> 871
/*     */     //   841: aload_0
/*     */     //   842: getfield trackDelta : Z
/*     */     //   845: ifne -> 871
/*     */     //   848: aload_0
/*     */     //   849: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   852: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   855: ifeq -> 1028
/*     */     //   858: aload_0
/*     */     //   859: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   862: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   865: invokevirtual isFallFlying : ()Z
/*     */     //   868: ifeq -> 1028
/*     */     //   871: aload_0
/*     */     //   872: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   875: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   878: astore #20
/*     */     //   880: aload #20
/*     */     //   882: aload_0
/*     */     //   883: getfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   886: invokevirtual distanceToSqr : (Lnet/minecraft/world/phys/Vec3;)D
/*     */     //   889: dstore #21
/*     */     //   891: dload #21
/*     */     //   893: ldc2_w 1.0E-7
/*     */     //   896: dcmpl
/*     */     //   897: ifgt -> 917
/*     */     //   900: dload #21
/*     */     //   902: dconst_0
/*     */     //   903: dcmpl
/*     */     //   904: ifle -> 1028
/*     */     //   907: aload #20
/*     */     //   909: invokevirtual lengthSqr : ()D
/*     */     //   912: dconst_0
/*     */     //   913: dcmpl
/*     */     //   914: ifne -> 1028
/*     */     //   917: aload_0
/*     */     //   918: aload #20
/*     */     //   920: putfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   923: aload_0
/*     */     //   924: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   927: astore #24
/*     */     //   929: aload #24
/*     */     //   931: instanceof net/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile
/*     */     //   934: ifeq -> 1001
/*     */     //   937: aload #24
/*     */     //   939: checkcast net/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile
/*     */     //   942: astore #23
/*     */     //   944: aload_0
/*     */     //   945: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   948: new net/minecraft/network/protocol/game/ClientboundBundlePacket
/*     */     //   951: dup
/*     */     //   952: new net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket
/*     */     //   955: dup
/*     */     //   956: aload_0
/*     */     //   957: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   960: invokevirtual getId : ()I
/*     */     //   963: aload_0
/*     */     //   964: getfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   967: invokespecial <init> : (ILnet/minecraft/world/phys/Vec3;)V
/*     */     //   970: new net/minecraft/network/protocol/game/ClientboundProjectilePowerPacket
/*     */     //   973: dup
/*     */     //   974: aload #23
/*     */     //   976: invokevirtual getId : ()I
/*     */     //   979: aload #23
/*     */     //   981: getfield accelerationPower : D
/*     */     //   984: invokespecial <init> : (ID)V
/*     */     //   987: invokestatic of : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;
/*     */     //   990: invokespecial <init> : (Ljava/lang/Iterable;)V
/*     */     //   993: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   998: goto -> 1028
/*     */     //   1001: aload_0
/*     */     //   1002: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1005: new net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket
/*     */     //   1008: dup
/*     */     //   1009: aload_0
/*     */     //   1010: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1013: invokevirtual getId : ()I
/*     */     //   1016: aload_0
/*     */     //   1017: getfield lastSentMovement : Lnet/minecraft/world/phys/Vec3;
/*     */     //   1020: invokespecial <init> : (ILnet/minecraft/world/phys/Vec3;)V
/*     */     //   1023: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1028: aload #9
/*     */     //   1030: ifnull -> 1044
/*     */     //   1033: aload_0
/*     */     //   1034: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1037: aload #9
/*     */     //   1039: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1044: aload_0
/*     */     //   1045: invokevirtual sendDirtyEntityData : ()V
/*     */     //   1048: iload #11
/*     */     //   1050: ifeq -> 1062
/*     */     //   1053: aload_0
/*     */     //   1054: getfield positionCodec : Lnet/minecraft/network/protocol/game/VecDeltaCodec;
/*     */     //   1057: aload #7
/*     */     //   1059: invokevirtual setBase : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   1062: iload #12
/*     */     //   1064: ifeq -> 1077
/*     */     //   1067: aload_0
/*     */     //   1068: iload_2
/*     */     //   1069: putfield lastSentYRot : B
/*     */     //   1072: aload_0
/*     */     //   1073: iload_3
/*     */     //   1074: putfield lastSentXRot : B
/*     */     //   1077: aload_0
/*     */     //   1078: iconst_0
/*     */     //   1079: putfield wasRiding : Z
/*     */     //   1082: aload_0
/*     */     //   1083: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1086: invokevirtual getYHeadRot : ()F
/*     */     //   1089: invokestatic packDegrees : (F)B
/*     */     //   1092: istore #5
/*     */     //   1094: iload #5
/*     */     //   1096: aload_0
/*     */     //   1097: getfield lastSentYHeadRot : B
/*     */     //   1100: isub
/*     */     //   1101: invokestatic abs : (I)I
/*     */     //   1104: iconst_1
/*     */     //   1105: if_icmplt -> 1136
/*     */     //   1108: aload_0
/*     */     //   1109: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1112: new net/minecraft/network/protocol/game/ClientboundRotateHeadPacket
/*     */     //   1115: dup
/*     */     //   1116: aload_0
/*     */     //   1117: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1120: iload #5
/*     */     //   1122: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;B)V
/*     */     //   1125: invokeinterface sendToTrackingPlayers : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1130: aload_0
/*     */     //   1131: iload #5
/*     */     //   1133: putfield lastSentYHeadRot : B
/*     */     //   1136: aload_0
/*     */     //   1137: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1140: iconst_0
/*     */     //   1141: putfield needsSync : Z
/*     */     //   1144: aload_0
/*     */     //   1145: dup
/*     */     //   1146: getfield tickCount : I
/*     */     //   1149: iconst_1
/*     */     //   1150: iadd
/*     */     //   1151: putfield tickCount : I
/*     */     //   1154: aload_0
/*     */     //   1155: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1158: getfield hurtMarked : Z
/*     */     //   1161: ifeq -> 1192
/*     */     //   1164: aload_0
/*     */     //   1165: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1168: iconst_0
/*     */     //   1169: putfield hurtMarked : Z
/*     */     //   1172: aload_0
/*     */     //   1173: getfield synchronizer : Lnet/minecraft/server/level/ServerEntity$Synchronizer;
/*     */     //   1176: new net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket
/*     */     //   1179: dup
/*     */     //   1180: aload_0
/*     */     //   1181: getfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   1184: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;)V
/*     */     //   1187: invokeinterface sendToTrackingPlayersAndSelf : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   1192: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #113	-> 0
/*     */     //   #115	-> 7
/*     */     //   #116	-> 15
/*     */     //   #117	-> 28
/*     */     //   #121	-> 55
/*     */     //   #124	-> 60
/*     */     //   #125	-> 87
/*     */     //   #127	-> 92
/*     */     //   #128	-> 102
/*     */     //   #129	-> 114
/*     */     //   #130	-> 125
/*     */     //   #131	-> 130
/*     */     //   #132	-> 166
/*     */     //   #134	-> 174
/*     */     //   #135	-> 185
/*     */     //   #136	-> 190
/*     */     //   #138	-> 200
/*     */     //   #142	-> 203
/*     */     //   #147	-> 207
/*     */     //   #148	-> 242
/*     */     //   #149	-> 253
/*     */     //   #150	-> 264
/*     */     //   #152	-> 297
/*     */     //   #154	-> 307
/*     */     //   #155	-> 312
/*     */     //   #156	-> 344
/*     */     //   #157	-> 349
/*     */     //   #160	-> 354
/*     */     //   #162	-> 368
/*     */     //   #164	-> 372
/*     */     //   #165	-> 380
/*     */     //   #167	-> 423
/*     */     //   #169	-> 436
/*     */     //   #170	-> 446
/*     */     //   #173	-> 455
/*     */     //   #175	-> 481
/*     */     //   #177	-> 484
/*     */     //   #178	-> 506
/*     */     //   #179	-> 509
/*     */     //   #181	-> 512
/*     */     //   #182	-> 523
/*     */     //   #183	-> 534
/*     */     //   #184	-> 545
/*     */     //   #185	-> 606
/*     */     //   #186	-> 652
/*     */     //   #187	-> 663
/*     */     //   #188	-> 668
/*     */     //   #189	-> 677
/*     */     //   #190	-> 680
/*     */     //   #192	-> 686
/*     */     //   #193	-> 706
/*     */     //   #194	-> 743
/*     */     //   #195	-> 746
/*     */     //   #196	-> 752
/*     */     //   #197	-> 757
/*     */     //   #198	-> 792
/*     */     //   #199	-> 798
/*     */     //   #200	-> 803
/*     */     //   #201	-> 828
/*     */     //   #205	-> 831
/*     */     //   #206	-> 871
/*     */     //   #207	-> 880
/*     */     //   #209	-> 891
/*     */     //   #210	-> 917
/*     */     //   #211	-> 923
/*     */     //   #212	-> 944
/*     */     //   #213	-> 960
/*     */     //   #214	-> 976
/*     */     //   #212	-> 987
/*     */     //   #216	-> 1001
/*     */     //   #221	-> 1028
/*     */     //   #222	-> 1033
/*     */     //   #225	-> 1044
/*     */     //   #227	-> 1048
/*     */     //   #228	-> 1053
/*     */     //   #230	-> 1062
/*     */     //   #231	-> 1067
/*     */     //   #232	-> 1072
/*     */     //   #235	-> 1077
/*     */     //   #238	-> 1082
/*     */     //   #239	-> 1094
/*     */     //   #240	-> 1108
/*     */     //   #241	-> 1130
/*     */     //   #243	-> 1136
/*     */     //   #246	-> 1144
/*     */     //   #247	-> 1154
/*     */     //   #248	-> 1164
/*     */     //   #249	-> 1172
/*     */     //   #251	-> 1192
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
/*     */   private void handleMinecartPosRot(NewMinecartBehavior paramNewMinecartBehavior, byte paramByte1, byte paramByte2, boolean paramBoolean) {
/* 254 */     sendDirtyEntityData();
/*     */     
/* 256 */     if (paramNewMinecartBehavior.lerpSteps.isEmpty()) {
/* 257 */       Vec3 vec31 = this.entity.getDeltaMovement();
/* 258 */       double d = vec31.distanceToSqr(this.lastSentMovement);
/* 259 */       Vec3 vec32 = this.entity.trackingPosition();
/* 260 */       boolean bool1 = (this.positionCodec.delta(vec32).lengthSqr() >= 7.62939453125E-6D) ? true : false;
/* 261 */       boolean bool2 = (bool1 || this.tickCount % 60 == 0) ? true : false;
/*     */       
/* 263 */       if (bool2 || paramBoolean || d > 1.0E-7D) {
/* 264 */         this.synchronizer.sendToTrackingPlayers((Packet<? super ClientGamePacketListener>)new ClientboundMoveMinecartPacket(this.entity.getId(), List.of(new NewMinecartBehavior.MinecartStep(this.entity.position(), this.entity.getDeltaMovement(), this.entity.getYRot(), this.entity.getXRot(), 1.0F))));
/*     */       }
/*     */     } else {
/* 267 */       this.synchronizer.sendToTrackingPlayers((Packet<? super ClientGamePacketListener>)new ClientboundMoveMinecartPacket(this.entity.getId(), List.copyOf(paramNewMinecartBehavior.lerpSteps)));
/* 268 */       paramNewMinecartBehavior.lerpSteps.clear();
/*     */     } 
/*     */     
/* 271 */     this.lastSentYRot = paramByte1;
/* 272 */     this.lastSentXRot = paramByte2;
/* 273 */     this.positionCodec.setBase(this.entity.position());
/*     */   }
/*     */   
/*     */   public void removePairing(ServerPlayer paramServerPlayer) {
/* 277 */     this.entity.stopSeenByPlayer(paramServerPlayer);
/* 278 */     paramServerPlayer.connection.send((Packet)new ClientboundRemoveEntitiesPacket(new int[] { this.entity.getId() }));
/*     */   }
/*     */   
/*     */   public void addPairing(ServerPlayer paramServerPlayer) {
/* 282 */     ArrayList arrayList = new ArrayList();
/* 283 */     Objects.requireNonNull(arrayList); sendPairingData(paramServerPlayer, arrayList::add);
/* 284 */     paramServerPlayer.connection.send((Packet)new ClientboundBundlePacket(arrayList));
/* 285 */     this.entity.startSeenByPlayer(paramServerPlayer);
/*     */   }
/*     */   
/*     */   public void sendPairingData(ServerPlayer paramServerPlayer, Consumer<Packet<ClientGamePacketListener>> paramConsumer) {
/* 289 */     this.entity.updateDataBeforeSync();
/* 290 */     if (this.entity.isRemoved()) {
/* 291 */       LOGGER.warn("Fetching packet for removed entity {}", this.entity);
/*     */     }
/* 293 */     Packet<ClientGamePacketListener> packet = this.entity.getAddEntityPacket(this);
/* 294 */     paramConsumer.accept(packet);
/*     */     
/* 296 */     if (this.trackedDataValues != null) {
/* 297 */       paramConsumer.accept(new ClientboundSetEntityDataPacket(this.entity.getId(), this.trackedDataValues));
/*     */     }
/*     */     
/* 300 */     Entity entity = this.entity; if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 301 */       Collection collection = livingEntity.getAttributes().getSyncableAttributes();
/* 302 */       if (!collection.isEmpty()) {
/* 303 */         paramConsumer.accept(new ClientboundUpdateAttributesPacket(this.entity.getId(), collection));
/*     */       } }
/*     */ 
/*     */     
/* 307 */     entity = this.entity; if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 308 */       ArrayList<Pair> arrayList = Lists.newArrayList();
/* 309 */       for (EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
/* 310 */         ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
/* 311 */         if (!itemStack.isEmpty()) {
/* 312 */           arrayList.add(Pair.of(equipmentSlot, itemStack.copy()));
/*     */         }
/*     */       } 
/* 315 */       if (!arrayList.isEmpty()) {
/* 316 */         paramConsumer.accept(new ClientboundSetEquipmentPacket(this.entity.getId(), arrayList));
/*     */       } }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     if (!this.entity.getPassengers().isEmpty()) {
/* 323 */       paramConsumer.accept(new ClientboundSetPassengersPacket(this.entity));
/*     */     }
/* 325 */     if (this.entity.isPassenger()) {
/* 326 */       paramConsumer.accept(new ClientboundSetPassengersPacket(this.entity.getVehicle()));
/*     */     }
/*     */ 
/*     */     
/* 330 */     entity = this.entity; if (entity instanceof Leashable) { Leashable leashable = (Leashable)entity;
/* 331 */       if (leashable.isLeashed()) {
/* 332 */         paramConsumer.accept(new ClientboundSetEntityLinkPacket(this.entity, leashable.getLeashHolder()));
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public Vec3 getPositionBase() {
/* 338 */     return this.positionCodec.getBase();
/*     */   }
/*     */   
/*     */   public Vec3 getLastSentMovement() {
/* 342 */     return this.lastSentMovement;
/*     */   }
/*     */   
/*     */   public float getLastSentXRot() {
/* 346 */     return Mth.unpackDegrees(this.lastSentXRot);
/*     */   }
/*     */   
/*     */   public float getLastSentYRot() {
/* 350 */     return Mth.unpackDegrees(this.lastSentYRot);
/*     */   }
/*     */   
/*     */   public float getLastSentYHeadRot() {
/* 354 */     return Mth.unpackDegrees(this.lastSentYHeadRot);
/*     */   }
/*     */   
/*     */   private void sendDirtyEntityData() {
/* 358 */     SynchedEntityData synchedEntityData = this.entity.getEntityData();
/* 359 */     List list = synchedEntityData.packDirty();
/* 360 */     if (list != null) {
/* 361 */       this.trackedDataValues = synchedEntityData.getNonDefaultValues();
/* 362 */       this.synchronizer.sendToTrackingPlayersAndSelf((Packet<? super ClientGamePacketListener>)new ClientboundSetEntityDataPacket(this.entity.getId(), list));
/*     */     } 
/*     */     
/* 365 */     if (this.entity instanceof LivingEntity) {
/* 366 */       Set set = ((LivingEntity)this.entity).getAttributes().getAttributesToSync();
/*     */       
/* 368 */       if (!set.isEmpty()) {
/* 369 */         this.synchronizer.sendToTrackingPlayersAndSelf((Packet<? super ClientGamePacketListener>)new ClientboundUpdateAttributesPacket(this.entity.getId(), set));
/*     */       }
/*     */       
/* 372 */       set.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Synchronizer {
/*     */     void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> param1Packet);
/*     */     
/*     */     void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> param1Packet);
/*     */     
/*     */     void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> param1Packet, Predicate<ServerPlayer> param1Predicate);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ServerEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */