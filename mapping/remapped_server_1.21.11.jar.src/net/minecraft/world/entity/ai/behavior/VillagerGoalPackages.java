/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerGoalPackages
/*     */ {
/*     */   private static final float STROLL_SPEED_MODIFIER = 0.4F;
/*     */   public static final int INTERACT_DIST_SQR = 5;
/*     */   public static final int INTERACT_WALKUP_DIST = 2;
/*     */   public static final float INTERACT_SPEED_MODIFIER = 0.5F;
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getCorePackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/*  46 */     return ImmutableList.of(
/*  47 */         Pair.of(Integer.valueOf(0), new Swim<>(0.8F)), 
/*  48 */         Pair.of(Integer.valueOf(0), InteractWithDoor.create()), 
/*  49 */         Pair.of(Integer.valueOf(0), new LookAtTargetSink(45, 90)), 
/*  50 */         Pair.of(Integer.valueOf(0), new VillagerPanicTrigger()), 
/*  51 */         Pair.of(Integer.valueOf(0), WakeUp.create()), 
/*  52 */         Pair.of(Integer.valueOf(0), ReactToBell.create()), 
/*  53 */         Pair.of(Integer.valueOf(0), SetRaidStatus.create()), 
/*  54 */         Pair.of(Integer.valueOf(0), ValidateNearbyPoi.create(((VillagerProfession)paramHolder.value()).heldJobSite(), MemoryModuleType.JOB_SITE)), 
/*  55 */         Pair.of(Integer.valueOf(0), ValidateNearbyPoi.create(((VillagerProfession)paramHolder.value()).acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE)), 
/*  56 */         Pair.of(Integer.valueOf(1), new MoveToTargetSink()), 
/*  57 */         Pair.of(Integer.valueOf(2), PoiCompetitorScan.create()), 
/*  58 */         Pair.of(Integer.valueOf(3), new LookAndFollowTradingPlayerSink(paramFloat)), (Object[])new Pair[] {
/*  59 */           Pair.of(Integer.valueOf(5), GoToWantedItem.create(paramFloat, false, 4)), 
/*     */           
/*  61 */           Pair.of(Integer.valueOf(6), AcquirePoi.create(((VillagerProfession)paramHolder.value()).acquirableJobSite(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty(), (paramServerLevel, paramBlockPos) -> true)), 
/*  62 */           Pair.of(Integer.valueOf(7), new GoToPotentialJobSite(paramFloat)), 
/*  63 */           Pair.of(Integer.valueOf(8), YieldJobSite.create(paramFloat)), 
/*  64 */           Pair.of(Integer.valueOf(10), AcquirePoi.create(paramHolder -> paramHolder.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of(Byte.valueOf((byte)14)), VillagerGoalPackages::validateBedPoi)), 
/*  65 */           Pair.of(Integer.valueOf(10), AcquirePoi.create(paramHolder -> paramHolder.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of(Byte.valueOf((byte)14)))), 
/*  66 */           Pair.of(Integer.valueOf(10), AssignProfessionFromJobSite.create()), 
/*  67 */           Pair.of(Integer.valueOf(10), ResetProfession.create())
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean validateBedPoi(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/*  72 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos);
/*  73 */     return (blockState.is(BlockTags.BEDS) && !((Boolean)blockState.getValue((Property)BedBlock.OCCUPIED)).booleanValue());
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getWorkPackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/*     */     WorkAtPoi workAtPoi;
/*  78 */     if (paramHolder.is(VillagerProfession.FARMER)) {
/*  79 */       workAtPoi = new WorkAtComposter();
/*     */     } else {
/*  81 */       workAtPoi = new WorkAtPoi();
/*     */     } 
/*     */     
/*  84 */     return ImmutableList.of(
/*  85 */         getMinimalLookBehavior(), 
/*  86 */         Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/*  87 */               Pair.of(workAtPoi, Integer.valueOf(7)), 
/*  88 */               Pair.of(StrollAroundPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 4), Integer.valueOf(2)), 
/*  89 */               Pair.of(StrollToPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), Integer.valueOf(5)), 
/*  90 */               Pair.of(StrollToPoiList.create(MemoryModuleType.SECONDARY_JOB_SITE, paramFloat, 1, 6, MemoryModuleType.JOB_SITE), Integer.valueOf(5)), 
/*  91 */               Pair.of(new HarvestFarmland(), Integer.valueOf(paramHolder.is(VillagerProfession.FARMER) ? 2 : 5)), 
/*  92 */               Pair.of(new UseBonemeal(), Integer.valueOf(paramHolder.is(VillagerProfession.FARMER) ? 4 : 7))))), 
/*     */         
/*  94 */         Pair.of(Integer.valueOf(10), new ShowTradesToPlayer(400, 1600)), 
/*  95 */         Pair.of(Integer.valueOf(10), SetLookAndInteract.create(EntityType.PLAYER, 4)), 
/*  96 */         Pair.of(Integer.valueOf(2), SetWalkTargetFromBlockMemory.create(MemoryModuleType.JOB_SITE, paramFloat, 9, 100, 1200)), 
/*  97 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/*  98 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPlayPackage(float paramFloat) {
/* 103 */     return ImmutableList.of(
/* 104 */         Pair.of(Integer.valueOf(0), new MoveToTargetSink(80, 120)), 
/* 105 */         getFullLookBehavior(), 
/* 106 */         Pair.of(Integer.valueOf(5), PlayTagWithOtherKids.create()), 
/* 107 */         Pair.of(Integer.valueOf(5), new RunOne<>(
/* 108 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */ 
/*     */             
/* 112 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 113 */               Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, paramFloat, 2), Integer.valueOf(2)), 
/* 114 */               Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, paramFloat, 2), Integer.valueOf(1)), 
/* 115 */               Pair.of(VillageBoundRandomStroll.create(paramFloat), Integer.valueOf(1)), 
/* 116 */               Pair.of(SetWalkTargetFromLookTarget.create(paramFloat, 2), Integer.valueOf(1)), 
/* 117 */               Pair.of(new JumpOnBed(paramFloat), Integer.valueOf(2)), 
/* 118 */               Pair.of(new DoNothing(20, 40), Integer.valueOf(2))))), 
/*     */ 
/*     */         
/* 121 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRestPackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 126 */     return ImmutableList.of(
/* 127 */         Pair.of(Integer.valueOf(2), SetWalkTargetFromBlockMemory.create(MemoryModuleType.HOME, paramFloat, 1, 150, 1200)), 
/* 128 */         Pair.of(Integer.valueOf(3), ValidateNearbyPoi.create(paramHolder -> paramHolder.is(PoiTypes.HOME), MemoryModuleType.HOME)), 
/* 129 */         Pair.of(Integer.valueOf(3), new SleepInBed()), 
/* 130 */         Pair.of(Integer.valueOf(5), new RunOne<>(
/* 131 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */ 
/*     */             
/* 135 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 136 */               Pair.of(SetClosestHomeAsWalkTarget.create(paramFloat), Integer.valueOf(1)), 
/* 137 */               Pair.of(InsideBrownianWalk.create(paramFloat), Integer.valueOf(4)), 
/* 138 */               Pair.of(GoToClosestVillage.create(paramFloat, 4), Integer.valueOf(2)), 
/* 139 */               Pair.of(new DoNothing(20, 40), Integer.valueOf(2))))), 
/*     */ 
/*     */         
/* 142 */         getMinimalLookBehavior(), 
/* 143 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getMeetPackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 148 */     return ImmutableList.of(
/* 149 */         Pair.of(Integer.valueOf(2), TriggerGate.triggerOneShuffled((List<Pair<? extends Trigger<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 150 */               Pair.of(StrollAroundPoi.create(MemoryModuleType.MEETING_POINT, 0.4F, 40), Integer.valueOf(2)), 
/* 151 */               Pair.of(SocializeAtBell.create(), Integer.valueOf(2))))), 
/*     */         
/* 153 */         Pair.of(Integer.valueOf(10), new ShowTradesToPlayer(400, 1600)), 
/* 154 */         Pair.of(Integer.valueOf(10), SetLookAndInteract.create(EntityType.PLAYER, 4)), 
/* 155 */         Pair.of(Integer.valueOf(2), SetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, paramFloat, 6, 100, 200)), 
/* 156 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/* 157 */         Pair.of(Integer.valueOf(3), ValidateNearbyPoi.create(paramHolder -> paramHolder.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT)), 
/* 158 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 159 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 160 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */ 
/*     */             
/* 163 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 164 */               Pair.of(new TradeWithVillager(), Integer.valueOf(1))))), 
/*     */ 
/*     */         
/* 167 */         getFullLookBehavior(), 
/* 168 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getIdlePackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 173 */     return ImmutableList.of(
/* 174 */         Pair.of(Integer.valueOf(2), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 175 */               Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, paramFloat, 2), Integer.valueOf(2)), 
/* 176 */               Pair.of(InteractWith.of(EntityType.VILLAGER, 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, paramFloat, 2), Integer.valueOf(1)), 
/* 177 */               Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, paramFloat, 2), Integer.valueOf(1)), 
/* 178 */               Pair.of(VillageBoundRandomStroll.create(paramFloat), Integer.valueOf(1)), 
/* 179 */               Pair.of(SetWalkTargetFromLookTarget.create(paramFloat, 2), Integer.valueOf(1)), 
/* 180 */               Pair.of(new JumpOnBed(paramFloat), Integer.valueOf(1)), 
/* 181 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))), 
/*     */         
/* 183 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/* 184 */         Pair.of(Integer.valueOf(3), SetLookAndInteract.create(EntityType.PLAYER, 4)), 
/* 185 */         Pair.of(Integer.valueOf(3), new ShowTradesToPlayer(400, 1600)), 
/* 186 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 187 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 188 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */ 
/*     */             
/* 191 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 192 */               Pair.of(new TradeWithVillager(), Integer.valueOf(1))))), 
/*     */ 
/*     */         
/* 195 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 196 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 197 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.BREED_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */ 
/*     */             
/* 200 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 201 */               Pair.of(new VillagerMakeLove(), Integer.valueOf(1))))), 
/*     */ 
/*     */         
/* 204 */         getFullLookBehavior(), 
/* 205 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPanicPackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 210 */     float f = paramFloat * 1.5F;
/*     */     
/* 212 */     return ImmutableList.of(
/* 213 */         Pair.of(Integer.valueOf(0), VillagerCalmDown.create()), 
/* 214 */         Pair.of(Integer.valueOf(1), SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)), 
/* 215 */         Pair.of(Integer.valueOf(1), SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)), 
/* 216 */         Pair.of(Integer.valueOf(3), VillageBoundRandomStroll.create(f, 2, 2)), 
/* 217 */         getMinimalLookBehavior());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPreRaidPackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 222 */     return ImmutableList.of(
/* 223 */         Pair.of(Integer.valueOf(0), RingBell.create()), 
/* 224 */         Pair.of(Integer.valueOf(0), TriggerGate.triggerOneShuffled((List<Pair<? extends Trigger<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 225 */               Pair.of(SetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, paramFloat * 1.5F, 2, 150, 200), Integer.valueOf(6)), 
/* 226 */               Pair.of(VillageBoundRandomStroll.create(paramFloat * 1.5F), Integer.valueOf(2))))), 
/*     */         
/* 228 */         getMinimalLookBehavior(), 
/* 229 */         Pair.of(Integer.valueOf(99), ResetRaidStatus.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRaidPackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 234 */     return ImmutableList.of(
/* 235 */         Pair.of(Integer.valueOf(0), BehaviorBuilder.sequence(
/* 236 */             BehaviorBuilder.triggerIf(VillagerGoalPackages::raidExistsAndNotVictory), 
/* 237 */             TriggerGate.triggerOneShuffled((List<Pair<? extends Trigger<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 238 */                 Pair.of(MoveToSkySeeingSpot.create(paramFloat), Integer.valueOf(5)), 
/* 239 */                 Pair.of(VillageBoundRandomStroll.create(paramFloat * 1.1F), Integer.valueOf(2)))))), 
/*     */ 
/*     */         
/* 242 */         Pair.of(Integer.valueOf(0), new CelebrateVillagersSurvivedRaid(600, 600)), 
/* 243 */         Pair.of(Integer.valueOf(2), BehaviorBuilder.sequence(
/* 244 */             BehaviorBuilder.triggerIf(VillagerGoalPackages::raidExistsAndActive), 
/* 245 */             LocateHidingPlace.create(24, paramFloat * 1.4F, 1))), 
/*     */         
/* 247 */         getMinimalLookBehavior(), 
/* 248 */         Pair.of(Integer.valueOf(99), ResetRaidStatus.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getHidePackage(Holder<VillagerProfession> paramHolder, float paramFloat) {
/* 253 */     byte b = 2;
/* 254 */     return ImmutableList.of(
/* 255 */         Pair.of(Integer.valueOf(0), SetHiddenState.create(15, 3)), 
/* 256 */         Pair.of(Integer.valueOf(1), LocateHidingPlace.create(32, paramFloat * 1.25F, 2)), 
/* 257 */         getMinimalLookBehavior());
/*     */   }
/*     */ 
/*     */   
/*     */   private static Pair<Integer, BehaviorControl<LivingEntity>> getFullLookBehavior() {
/* 262 */     return Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 263 */             Pair.of(SetEntityLookTarget.create(EntityType.CAT, 8.0F), Integer.valueOf(8)), 
/* 264 */             Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), Integer.valueOf(2)), 
/* 265 */             Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), Integer.valueOf(2)), 
/* 266 */             Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 8.0F), Integer.valueOf(1)), 
/* 267 */             Pair.of(SetEntityLookTarget.create(MobCategory.WATER_CREATURE, 8.0F), Integer.valueOf(1)), 
/*     */             
/* 269 */             Pair.of(SetEntityLookTarget.create(MobCategory.AXOLOTLS, 8.0F), Integer.valueOf(1)), 
/* 270 */             Pair.of(SetEntityLookTarget.create(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), Integer.valueOf(1)), 
/* 271 */             Pair.of(SetEntityLookTarget.create(MobCategory.WATER_AMBIENT, 8.0F), Integer.valueOf(1)), 
/* 272 */             Pair.of(SetEntityLookTarget.create(MobCategory.MONSTER, 8.0F), Integer.valueOf(1)), 
/* 273 */             Pair.of(new DoNothing(30, 60), Integer.valueOf(2)))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Pair<Integer, BehaviorControl<LivingEntity>> getMinimalLookBehavior() {
/* 278 */     return Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 279 */             Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), Integer.valueOf(2)), 
/* 280 */             Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), Integer.valueOf(2)), 
/* 281 */             Pair.of(new DoNothing(30, 60), Integer.valueOf(8)))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean raidExistsAndActive(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 286 */     Raid raid = paramServerLevel.getRaidAt(paramLivingEntity.blockPosition());
/* 287 */     return (raid != null && raid.isActive() && !raid.isVictory() && !raid.isLoss());
/*     */   }
/*     */   
/*     */   private static boolean raidExistsAndNotVictory(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 291 */     Raid raid = paramServerLevel.getRaidAt(paramLivingEntity.blockPosition());
/* 292 */     return (raid != null && raid.isVictory());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\VillagerGoalPackages.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */