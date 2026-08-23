/*    */ package net.minecraft.world.level.block.entity.trialspawner;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
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
/*    */ class null
/*    */   implements PlayerDetector.EntitySelector
/*    */ {
/*    */   public List<Player> getPlayers(ServerLevel paramServerLevel, Predicate<? super Player> paramPredicate) {
/* 75 */     return players.stream()
/* 76 */       .filter(paramPredicate)
/* 77 */       .toList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends Entity> List<T> getEntities(ServerLevel paramServerLevel, EntityTypeTest<Entity, T> paramEntityTypeTest, AABB paramAABB, Predicate<? super T> paramPredicate) {
/* 83 */     Objects.requireNonNull(paramEntityTypeTest); return players.stream().map(paramEntityTypeTest::tryCast)
/* 84 */       .filter(Objects::nonNull)
/* 85 */       .filter(paramPredicate)
/* 86 */       .toList();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\trialspawner\PlayerDetector$EntitySelector$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */