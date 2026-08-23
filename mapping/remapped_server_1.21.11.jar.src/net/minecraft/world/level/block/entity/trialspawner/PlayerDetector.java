/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface PlayerDetector {
/*    */   public static final PlayerDetector NO_CREATIVE_PLAYERS;
/*    */   public static final PlayerDetector INCLUDING_CREATIVE_PLAYERS;
/*    */   public static final PlayerDetector SHEEP;
/*    */   
/*    */   static {
/* 25 */     NO_CREATIVE_PLAYERS = ((paramServerLevel, paramEntitySelector, paramBlockPos, paramDouble, paramBoolean) -> paramEntitySelector.getPlayers(paramServerLevel, ()).stream().filter(()).map(Entity::getUUID).toList());
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     INCLUDING_CREATIVE_PLAYERS = ((paramServerLevel, paramEntitySelector, paramBlockPos, paramDouble, paramBoolean) -> paramEntitySelector.getPlayers(paramServerLevel, ()).stream().filter(()).map(Entity::getUUID).toList());
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     SHEEP = ((paramServerLevel, paramEntitySelector, paramBlockPos, paramDouble, paramBoolean) -> {
/*    */         AABB aABB = (new AABB(paramBlockPos)).inflate(paramDouble);
/*    */         return paramEntitySelector.<Entity>getEntities(paramServerLevel, (EntityTypeTest<Entity, Entity>)EntityType.SHEEP, aABB, LivingEntity::isAlive).stream().filter(()).map(Entity::getUUID).toList();
/*    */       });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean inLineOfSight(Level paramLevel, Vec3 paramVec31, Vec3 paramVec32) {
/* 46 */     BlockHitResult blockHitResult = paramLevel.clip(new ClipContext(paramVec32, paramVec31, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 47 */     return (blockHitResult.getBlockPos().equals(BlockPos.containing((Position)paramVec31)) || blockHitResult.getType() == HitResult.Type.MISS);
/*    */   }
/*    */ 
/*    */   
/*    */   List<UUID> detect(ServerLevel paramServerLevel, EntitySelector paramEntitySelector, BlockPos paramBlockPos, double paramDouble, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   class null
/*    */     implements EntitySelector
/*    */   {
/*    */     public List<ServerPlayer> getPlayers(ServerLevel param1ServerLevel, Predicate<? super Player> param1Predicate) {
/* 58 */       return param1ServerLevel.getPlayers(param1Predicate);
/*    */     }
/*    */     
/*    */     public <T extends Entity> List<T> getEntities(ServerLevel param1ServerLevel, EntityTypeTest<Entity, T> param1EntityTypeTest, AABB param1AABB, Predicate<? super T> param1Predicate)
/*    */     {
/* 63 */       return param1ServerLevel.getEntities(param1EntityTypeTest, param1AABB, param1Predicate); } } public static interface EntitySelector { public static final EntitySelector SELECT_FROM_LEVEL = new EntitySelector() { public <T extends Entity> List<T> getEntities(ServerLevel param2ServerLevel, EntityTypeTest<Entity, T> param2EntityTypeTest, AABB param2AABB, Predicate<? super T> param2Predicate) { return param2ServerLevel.getEntities(param2EntityTypeTest, param2AABB, param2Predicate); }
/*    */          public List<ServerPlayer> getPlayers(ServerLevel param2ServerLevel, Predicate<? super Player> param2Predicate) {
/*    */           return param2ServerLevel.getPlayers(param2Predicate);
/*    */         } }
/*    */     ; List<? extends Player> getPlayers(ServerLevel param1ServerLevel, Predicate<? super Player> param1Predicate); <T extends Entity> List<T> getEntities(ServerLevel param1ServerLevel, EntityTypeTest<Entity, T> param1EntityTypeTest, AABB param1AABB, Predicate<? super T> param1Predicate); static EntitySelector onlySelectPlayer(Player param1Player) {
/* 68 */       return onlySelectPlayers(List.of(param1Player));
/*    */     }
/*    */     
/*    */     static EntitySelector onlySelectPlayers(final List<Player> players) {
/* 72 */       return new EntitySelector()
/*    */         {
/*    */           public List<Player> getPlayers(ServerLevel param2ServerLevel, Predicate<? super Player> param2Predicate) {
/* 75 */             return players.stream()
/* 76 */               .filter(param2Predicate)
/* 77 */               .toList();
/*    */           }
/*    */ 
/*    */           
/*    */           public <T extends Entity> List<T> getEntities(ServerLevel param2ServerLevel, EntityTypeTest<Entity, T> param2EntityTypeTest, AABB param2AABB, Predicate<? super T> param2Predicate)
/*    */           {
/* 83 */             Objects.requireNonNull(param2EntityTypeTest); return players.stream().map(param2EntityTypeTest::tryCast)
/* 84 */               .filter(Objects::nonNull)
/* 85 */               .filter(param2Predicate)
/* 86 */               .toList(); } }; } } class null implements EntitySelector { public <T extends Entity> List<T> getEntities(ServerLevel param1ServerLevel, EntityTypeTest<Entity, T> param1EntityTypeTest, AABB param1AABB, Predicate<? super T> param1Predicate) { Objects.requireNonNull(param1EntityTypeTest); return players.stream().map(param1EntityTypeTest::tryCast).filter(Objects::nonNull).filter(param1Predicate).toList(); }
/*    */ 
/*    */     
/*    */     public List<Player> getPlayers(ServerLevel param1ServerLevel, Predicate<? super Player> param1Predicate) {
/*    */       return players.stream().filter(param1Predicate).toList();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */