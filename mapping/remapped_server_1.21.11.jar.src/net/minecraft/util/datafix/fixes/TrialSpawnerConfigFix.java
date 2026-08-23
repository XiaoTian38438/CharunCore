/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public class TrialSpawnerConfigFix
/*    */   extends NamedEntityWriteReadFix
/*    */ {
/*    */   public TrialSpawnerConfigFix(Schema paramSchema) {
/* 14 */     super(paramSchema, true, "Trial Spawner config tag fixer", References.BLOCK_ENTITY, "minecraft:trial_spawner");
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> moveToConfigTag(Dynamic<T> paramDynamic) {
/* 18 */     List<String> list = List.of("spawn_range", "total_mobs", "simultaneous_mobs", "total_mobs_added_per_player", "simultaneous_mobs_added_per_player", "ticks_between_spawn", "spawn_potentials", "loot_tables_to_eject", "items_to_drop_when_ominous");
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
/* 30 */     HashMap<Object, Object> hashMap = new HashMap<>(list.size());
/* 31 */     for (String str : list) {
/* 32 */       Optional<Dynamic> optional = paramDynamic.get(str).get().result();
/* 33 */       if (optional.isPresent()) {
/* 34 */         hashMap.put(paramDynamic.createString(str), optional.get());
/* 35 */         paramDynamic = paramDynamic.remove(str);
/*    */       } 
/*    */     } 
/* 38 */     return hashMap.isEmpty() ? paramDynamic : paramDynamic.set("normal_config", paramDynamic.createMap(hashMap));
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> paramDynamic) {
/* 43 */     return moveToConfigTag(paramDynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\TrialSpawnerConfigFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */