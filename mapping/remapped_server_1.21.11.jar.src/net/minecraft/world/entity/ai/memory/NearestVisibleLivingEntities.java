/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestVisibleLivingEntities
/*    */ {
/* 22 */   private static final NearestVisibleLivingEntities EMPTY = new NearestVisibleLivingEntities();
/*    */   private final List<LivingEntity> nearbyEntities;
/*    */   private final Predicate<LivingEntity> lineOfSightTest;
/*    */   
/*    */   private NearestVisibleLivingEntities() {
/* 27 */     this.nearbyEntities = List.of();
/* 28 */     this.lineOfSightTest = (paramLivingEntity -> false);
/*    */   }
/*    */   
/*    */   public NearestVisibleLivingEntities(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, List<LivingEntity> paramList) {
/* 32 */     this.nearbyEntities = paramList;
/* 33 */     Object2BooleanOpenHashMap object2BooleanOpenHashMap = new Object2BooleanOpenHashMap(paramList.size());
/* 34 */     Predicate predicate = paramLivingEntity2 -> Sensor.isEntityTargetable(paramServerLevel, paramLivingEntity1, paramLivingEntity2);
/* 35 */     this.lineOfSightTest = (paramLivingEntity -> paramObject2BooleanOpenHashMap.computeIfAbsent(paramLivingEntity, paramPredicate));
/*    */   }
/*    */   
/*    */   public static NearestVisibleLivingEntities empty() {
/* 39 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public Optional<LivingEntity> findClosest(Predicate<LivingEntity> paramPredicate) {
/* 43 */     for (LivingEntity livingEntity : this.nearbyEntities) {
/* 44 */       if (paramPredicate.test(livingEntity) && this.lineOfSightTest.test(livingEntity)) {
/* 45 */         return Optional.of(livingEntity);
/*    */       }
/*    */     } 
/* 48 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterable<LivingEntity> findAll(Predicate<LivingEntity> paramPredicate) {
/* 57 */     return Iterables.filter(this.nearbyEntities, paramLivingEntity -> (paramPredicate.test(paramLivingEntity) && this.lineOfSightTest.test(paramLivingEntity)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<LivingEntity> find(Predicate<LivingEntity> paramPredicate) {
/* 67 */     return this.nearbyEntities.stream()
/* 68 */       .filter(paramLivingEntity -> (paramPredicate.test(paramLivingEntity) && this.lineOfSightTest.test(paramLivingEntity)));
/*    */   }
/*    */   
/*    */   public boolean contains(LivingEntity paramLivingEntity) {
/* 72 */     return (this.nearbyEntities.contains(paramLivingEntity) && this.lineOfSightTest.test(paramLivingEntity));
/*    */   }
/*    */   
/*    */   public boolean contains(Predicate<LivingEntity> paramPredicate) {
/* 76 */     for (LivingEntity livingEntity : this.nearbyEntities) {
/* 77 */       if (paramPredicate.test(livingEntity) && this.lineOfSightTest.test(livingEntity)) {
/* 78 */         return true;
/*    */       }
/*    */     } 
/* 81 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\memory\NearestVisibleLivingEntities.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */