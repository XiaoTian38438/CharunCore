/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class RaidRenamesDataFix extends DataFix {
/*    */   public RaidRenamesDataFix(Schema paramSchema) {
/* 12 */     super(paramSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     return fixTypeEverywhereTyped("RaidRenamesDataFix", getInputSchema().getType(References.SAVED_DATA_RAIDS), paramTyped -> paramTyped.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fix(Dynamic<?> paramDynamic) {
/* 23 */     return paramDynamic
/* 24 */       .renameAndFixField("Raids", "raids", paramDynamic -> paramDynamic.createList(paramDynamic.asStream().map(RaidRenamesDataFix::fixRaid)))
/*    */ 
/*    */       
/* 27 */       .renameField("Tick", "tick")
/* 28 */       .renameField("NextAvailableID", "next_id");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixRaid(Dynamic<?> paramDynamic) {
/* 32 */     return ExtraDataFixUtils.fixInlineBlockPos(paramDynamic, "CX", "CY", "CZ", "center")
/* 33 */       .renameField("Id", "id")
/* 34 */       .renameField("Started", "started")
/* 35 */       .renameField("Active", "active")
/* 36 */       .renameField("TicksActive", "ticks_active")
/* 37 */       .renameField("BadOmenLevel", "raid_omen_level")
/* 38 */       .renameField("GroupsSpawned", "groups_spawned")
/* 39 */       .renameField("PreRaidTicks", "cooldown_ticks")
/* 40 */       .renameField("PostRaidTicks", "post_raid_ticks")
/* 41 */       .renameField("TotalHealth", "total_health")
/* 42 */       .renameField("NumGroups", "group_count")
/* 43 */       .renameField("Status", "status")
/* 44 */       .renameField("HeroesOfTheVillage", "heroes_of_the_village");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\RaidRenamesDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */