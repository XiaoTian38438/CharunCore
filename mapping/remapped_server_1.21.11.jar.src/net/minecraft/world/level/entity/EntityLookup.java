/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntityLookup<T extends EntityAccess>
/*    */ {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 18 */   private final Int2ObjectMap<T> byId = (Int2ObjectMap<T>)new Int2ObjectLinkedOpenHashMap();
/* 19 */   private final Map<UUID, T> byUuid = Maps.newHashMap();
/*    */   
/*    */   public <U extends T> void getEntities(EntityTypeTest<T, U> paramEntityTypeTest, AbortableIterationConsumer<U> paramAbortableIterationConsumer) {
/* 22 */     for (ObjectIterator<EntityAccess> objectIterator = this.byId.values().iterator(); objectIterator.hasNext(); ) { EntityAccess entityAccess1 = objectIterator.next();
/* 23 */       EntityAccess entityAccess2 = (EntityAccess)paramEntityTypeTest.tryCast((T)entityAccess1);
/* 24 */       if (entityAccess2 != null && 
/* 25 */         paramAbortableIterationConsumer.accept(entityAccess2).shouldAbort()) {
/*    */         return;
/*    */       } }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterable<T> getAllEntities() {
/* 33 */     return Iterables.unmodifiableIterable((Iterable)this.byId.values());
/*    */   }
/*    */   
/*    */   public void add(T paramT) {
/* 37 */     UUID uUID = paramT.getUUID();
/* 38 */     if (this.byUuid.containsKey(uUID)) {
/* 39 */       LOGGER.warn("Duplicate entity UUID {}: {}", uUID, paramT);
/*    */       return;
/*    */     } 
/* 42 */     this.byUuid.put(uUID, paramT);
/* 43 */     this.byId.put(paramT.getId(), paramT);
/*    */   }
/*    */   
/*    */   public void remove(T paramT) {
/* 47 */     this.byUuid.remove(paramT.getUUID());
/* 48 */     this.byId.remove(paramT.getId());
/*    */   }
/*    */   
/*    */   public T getEntity(int paramInt) {
/* 52 */     return (T)this.byId.get(paramInt);
/*    */   }
/*    */   
/*    */   public T getEntity(UUID paramUUID) {
/* 56 */     return this.byUuid.get(paramUUID);
/*    */   }
/*    */   
/*    */   public int count() {
/* 60 */     return this.byUuid.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */