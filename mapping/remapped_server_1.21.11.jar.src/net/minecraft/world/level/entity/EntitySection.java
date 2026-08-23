/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import net.minecraft.util.ClassInstanceMultiMap;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntitySection<T extends EntityAccess>
/*    */ {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ClassInstanceMultiMap<T> storage;
/*    */   private Visibility chunkStatus;
/*    */   
/*    */   public EntitySection(Class<T> paramClass, Visibility paramVisibility) {
/* 20 */     this.chunkStatus = paramVisibility;
/* 21 */     this.storage = new ClassInstanceMultiMap(paramClass);
/*    */   }
/*    */   
/*    */   public void add(T paramT) {
/* 25 */     this.storage.add(paramT);
/*    */   }
/*    */   
/*    */   public boolean remove(T paramT) {
/* 29 */     return this.storage.remove(paramT);
/*    */   }
/*    */   
/*    */   public AbortableIterationConsumer.Continuation getEntities(AABB paramAABB, AbortableIterationConsumer<T> paramAbortableIterationConsumer) {
/* 33 */     for (EntityAccess entityAccess : this.storage) {
/* 34 */       if (entityAccess.getBoundingBox().intersects(paramAABB) && 
/* 35 */         paramAbortableIterationConsumer.accept(entityAccess).shouldAbort()) {
/* 36 */         return AbortableIterationConsumer.Continuation.ABORT;
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */   }
/*    */   
/*    */   public <U extends T> AbortableIterationConsumer.Continuation getEntities(EntityTypeTest<T, U> paramEntityTypeTest, AABB paramAABB, AbortableIterationConsumer<? super U> paramAbortableIterationConsumer) {
/* 44 */     Collection collection = this.storage.find(paramEntityTypeTest.getBaseClass());
/* 45 */     if (collection.isEmpty()) {
/* 46 */       return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */     }
/* 48 */     for (EntityAccess entityAccess1 : collection) {
/* 49 */       EntityAccess entityAccess2 = (EntityAccess)paramEntityTypeTest.tryCast((T)entityAccess1);
/* 50 */       if (entityAccess2 != null && entityAccess1.getBoundingBox().intersects(paramAABB) && 
/* 51 */         paramAbortableIterationConsumer.accept(entityAccess2).shouldAbort()) {
/* 52 */         return AbortableIterationConsumer.Continuation.ABORT;
/*    */       }
/*    */     } 
/*    */     
/* 56 */     return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 60 */     return this.storage.isEmpty();
/*    */   }
/*    */   
/*    */   public Stream<T> getEntities() {
/* 64 */     return this.storage.stream();
/*    */   }
/*    */   
/*    */   public Visibility getStatus() {
/* 68 */     return this.chunkStatus;
/*    */   }
/*    */   
/*    */   public Visibility updateChunkStatus(Visibility paramVisibility) {
/* 72 */     Visibility visibility = this.chunkStatus;
/* 73 */     this.chunkStatus = paramVisibility;
/* 74 */     return visibility;
/*    */   }
/*    */   
/*    */   @VisibleForDebug
/*    */   public int size() {
/* 79 */     return this.storage.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntitySection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */