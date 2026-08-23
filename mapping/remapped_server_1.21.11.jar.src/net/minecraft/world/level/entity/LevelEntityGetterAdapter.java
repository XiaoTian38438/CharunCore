/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class LevelEntityGetterAdapter<T extends EntityAccess>
/*    */   implements LevelEntityGetter<T>
/*    */ {
/*    */   private final EntityLookup<T> visibleEntities;
/*    */   private final EntitySectionStorage<T> sectionStorage;
/*    */   
/*    */   public LevelEntityGetterAdapter(EntityLookup<T> paramEntityLookup, EntitySectionStorage<T> paramEntitySectionStorage) {
/* 15 */     this.visibleEntities = paramEntityLookup;
/* 16 */     this.sectionStorage = paramEntitySectionStorage;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(int paramInt) {
/* 21 */     return this.visibleEntities.getEntity(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(UUID paramUUID) {
/* 26 */     return this.visibleEntities.getEntity(paramUUID);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterable<T> getAll() {
/* 31 */     return this.visibleEntities.getAllEntities();
/*    */   }
/*    */ 
/*    */   
/*    */   public <U extends T> void get(EntityTypeTest<T, U> paramEntityTypeTest, AbortableIterationConsumer<U> paramAbortableIterationConsumer) {
/* 36 */     this.visibleEntities.getEntities(paramEntityTypeTest, paramAbortableIterationConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void get(AABB paramAABB, Consumer<T> paramConsumer) {
/* 41 */     this.sectionStorage.getEntities(paramAABB, AbortableIterationConsumer.forConsumer(paramConsumer));
/*    */   }
/*    */ 
/*    */   
/*    */   public <U extends T> void get(EntityTypeTest<T, U> paramEntityTypeTest, AABB paramAABB, AbortableIterationConsumer<U> paramAbortableIterationConsumer) {
/* 46 */     this.sectionStorage.getEntities(paramEntityTypeTest, paramAABB, paramAbortableIterationConsumer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\LevelEntityGetterAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */