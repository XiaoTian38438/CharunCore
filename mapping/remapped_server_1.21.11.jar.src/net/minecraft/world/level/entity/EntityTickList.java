/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class EntityTickList
/*    */ {
/* 13 */   private Int2ObjectMap<Entity> active = (Int2ObjectMap<Entity>)new Int2ObjectLinkedOpenHashMap();
/* 14 */   private Int2ObjectMap<Entity> passive = (Int2ObjectMap<Entity>)new Int2ObjectLinkedOpenHashMap();
/*    */   private Int2ObjectMap<Entity> iterated;
/*    */   
/*    */   private void ensureActiveIsNotIterated() {
/* 18 */     if (this.iterated == this.active) {
/* 19 */       this.passive.clear();
/* 20 */       for (ObjectIterator<Int2ObjectMap.Entry> objectIterator = Int2ObjectMaps.fastIterable(this.active).iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry entry = objectIterator.next();
/* 21 */         this.passive.put(entry.getIntKey(), entry.getValue()); }
/*    */       
/* 23 */       Int2ObjectMap<Entity> int2ObjectMap = this.active;
/* 24 */       this.active = this.passive;
/* 25 */       this.passive = int2ObjectMap;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Entity paramEntity) {
/* 30 */     ensureActiveIsNotIterated();
/* 31 */     this.active.put(paramEntity.getId(), paramEntity);
/*    */   }
/*    */   
/*    */   public void remove(Entity paramEntity) {
/* 35 */     ensureActiveIsNotIterated();
/* 36 */     this.active.remove(paramEntity.getId());
/*    */   }
/*    */   
/*    */   public boolean contains(Entity paramEntity) {
/* 40 */     return this.active.containsKey(paramEntity.getId());
/*    */   }
/*    */   
/*    */   public void forEach(Consumer<Entity> paramConsumer) {
/* 44 */     if (this.iterated != null)
/*    */     {
/* 46 */       throw new UnsupportedOperationException("Only one concurrent iteration supported");
/*    */     }
/*    */     
/* 49 */     this.iterated = this.active;
/*    */     
/*    */     try {
/* 52 */       for (ObjectIterator<Entity> objectIterator = this.active.values().iterator(); objectIterator.hasNext(); ) { Entity entity = objectIterator.next();
/* 53 */         paramConsumer.accept(entity); }
/*    */     
/*    */     } finally {
/* 56 */       this.iterated = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityTickList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */