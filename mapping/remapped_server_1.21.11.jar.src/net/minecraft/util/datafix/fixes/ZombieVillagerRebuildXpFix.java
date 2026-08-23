/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ZombieVillagerRebuildXpFix extends NamedEntityFix {
/*    */   public ZombieVillagerRebuildXpFix(Schema paramSchema, boolean paramBoolean) {
/* 11 */     super(paramSchema, paramBoolean, "Zombie Villager XP rebuild", References.ENTITY, "minecraft:zombie_villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 16 */     return paramTyped.update(DSL.remainderFinder(), paramDynamic -> {
/*    */           Optional optional = paramDynamic.get("Xp").asNumber().result();
/*    */           if (optional.isEmpty()) {
/*    */             int i = paramDynamic.get("VillagerData").get("level").asInt(1);
/*    */             return paramDynamic.set("Xp", paramDynamic.createInt(VillagerRebuildLevelAndXpFix.getMinXpPerLevel(i)));
/*    */           } 
/*    */           return paramDynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ZombieVillagerRebuildXpFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */