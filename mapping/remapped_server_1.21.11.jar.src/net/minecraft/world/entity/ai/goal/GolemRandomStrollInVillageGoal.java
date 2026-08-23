/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.level.entity.EntityAccess;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GolemRandomStrollInVillageGoal extends RandomStrollGoal {
/*     */   private static final int POI_SECTION_SCAN_RADIUS = 2;
/*     */   private static final int VILLAGER_SCAN_RADIUS = 32;
/*     */   
/*     */   public GolemRandomStrollInVillageGoal(PathfinderMob paramPathfinderMob, double paramDouble) {
/*  25 */     super(paramPathfinderMob, paramDouble, 240, false);
/*     */   }
/*     */   private static final int RANDOM_POS_XY_DISTANCE = 10; private static final int RANDOM_POS_Y_DISTANCE = 7;
/*     */   
/*     */   protected Vec3 getPosition() {
/*     */     Vec3 vec3;
/*  31 */     float f = (this.mob.level()).random.nextFloat();
/*  32 */     if ((this.mob.level()).random.nextFloat() < 0.3F) {
/*  33 */       return getPositionTowardsAnywhere();
/*     */     }
/*     */     
/*  36 */     if (f < 0.7F) {
/*  37 */       vec3 = getPositionTowardsVillagerWhoWantsGolem();
/*  38 */       if (vec3 == null) {
/*  39 */         vec3 = getPositionTowardsPoi();
/*     */       }
/*     */     } else {
/*  42 */       vec3 = getPositionTowardsPoi();
/*  43 */       if (vec3 == null) {
/*  44 */         vec3 = getPositionTowardsVillagerWhoWantsGolem();
/*     */       }
/*     */     } 
/*     */     
/*  48 */     return (vec3 == null) ? getPositionTowardsAnywhere() : vec3;
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsAnywhere() {
/*  52 */     return LandRandomPos.getPos(this.mob, 10, 7);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsVillagerWhoWantsGolem() {
/*  56 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/*  57 */     List<Villager> list = serverLevel.getEntities((EntityTypeTest)EntityType.VILLAGER, this.mob.getBoundingBox().inflate(32.0D), this::doesVillagerWantGolem);
/*  58 */     if (list.isEmpty()) {
/*  59 */       return null;
/*     */     }
/*  61 */     Villager villager = list.get((this.mob.level()).random.nextInt(list.size()));
/*  62 */     Vec3 vec3 = villager.position();
/*  63 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, vec3);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsPoi() {
/*  67 */     SectionPos sectionPos = getRandomVillageSection();
/*  68 */     if (sectionPos == null) {
/*  69 */       return null;
/*     */     }
/*     */     
/*  72 */     BlockPos blockPos = getRandomPoiWithinSection(sectionPos);
/*  73 */     if (blockPos == null)
/*     */     {
/*  75 */       return null;
/*     */     }
/*     */     
/*  78 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)blockPos));
/*     */   }
/*     */   
/*     */   private SectionPos getRandomVillageSection() {
/*  82 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/*     */ 
/*     */ 
/*     */     
/*  86 */     List<SectionPos> list = (List)SectionPos.cube(SectionPos.of((EntityAccess)this.mob), 2).filter(paramSectionPos -> (paramServerLevel.sectionsToVillage(paramSectionPos) == 0)).collect(Collectors.toList());
/*     */     
/*  88 */     if (list.isEmpty()) {
/*  89 */       return null;
/*     */     }
/*  91 */     return list.get(serverLevel.random.nextInt(list.size()));
/*     */   }
/*     */   
/*     */   private BlockPos getRandomPoiWithinSection(SectionPos paramSectionPos) {
/*  95 */     ServerLevel serverLevel = (ServerLevel)this.mob.level();
/*  96 */     PoiManager poiManager = serverLevel.getPoiManager();
/*     */ 
/*     */     
/*  99 */     List<BlockPos> list = (List)poiManager.getInRange(paramHolder -> true, paramSectionPos.center(), 8, PoiManager.Occupancy.IS_OCCUPIED).map(PoiRecord::getPos).collect(Collectors.toList());
/*     */     
/* 101 */     if (list.isEmpty()) {
/* 102 */       return null;
/*     */     }
/* 104 */     return list.get(serverLevel.random.nextInt(list.size()));
/*     */   }
/*     */   
/*     */   private boolean doesVillagerWantGolem(Villager paramVillager) {
/* 108 */     return paramVillager.wantsToSpawnGolem(this.mob.level().getGameTime());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\GolemRandomStrollInVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */