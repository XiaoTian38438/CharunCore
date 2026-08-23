/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface EntitySelector
/*    */ {
/* 55 */   public static final EntitySelector SELECT_FROM_LEVEL = new EntitySelector()
/*    */     {
/*    */       public List<ServerPlayer> getPlayers(ServerLevel param2ServerLevel, Predicate<? super Player> param2Predicate) {
/* 58 */         return param2ServerLevel.getPlayers(param2Predicate);
/*    */       }
/*    */ 
/*    */       
/*    */       public <T extends Entity> List<T> getEntities(ServerLevel param2ServerLevel, EntityTypeTest<Entity, T> param2EntityTypeTest, AABB param2AABB, Predicate<? super T> param2Predicate) {
/* 63 */         return param2ServerLevel.getEntities(param2EntityTypeTest, param2AABB, param2Predicate);
/*    */       }
/*    */     };
/*    */   List<? extends Player> getPlayers(ServerLevel paramServerLevel, Predicate<? super Player> paramPredicate);
/*    */   static EntitySelector onlySelectPlayer(Player paramPlayer) {
/* 68 */     return onlySelectPlayers(List.of(paramPlayer));
/*    */   }
/*    */   <T extends Entity> List<T> getEntities(ServerLevel paramServerLevel, EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate);
/*    */   static EntitySelector onlySelectPlayers(final List<Player> players) {
/* 72 */     return new EntitySelector()
/*    */       {
/*    */         public List<Player> getPlayers(ServerLevel param2ServerLevel, Predicate<? super Player> param2Predicate) {
/* 75 */           return players.stream()
/* 76 */             .filter(param2Predicate)
/* 77 */             .toList();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public <T extends Entity> List<T> getEntities(ServerLevel param2ServerLevel, EntityTypeTest<Entity, T> param2EntityTypeTest, AABB param2AABB, Predicate<? super T> param2Predicate) {
/* 83 */           Objects.requireNonNull(param2EntityTypeTest); return players.stream().map(param2EntityTypeTest::tryCast)
/* 84 */             .filter(Objects::nonNull)
/* 85 */             .filter(param2Predicate)
/* 86 */             .toList();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */