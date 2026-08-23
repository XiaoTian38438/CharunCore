/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityBrushableBlockFieldsRenameFix extends NamedEntityFix {
/*    */   public EntityBrushableBlockFieldsRenameFix(Schema paramSchema) {
/* 10 */     super(paramSchema, false, "EntityBrushableBlockFieldsRenameFix", References.BLOCK_ENTITY, "minecraft:brushable_block");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> paramDynamic) {
/* 14 */     return paramDynamic.renameField("loot_table", "LootTable").renameField("loot_table_seed", "LootTableSeed");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> paramTyped) {
/* 19 */     return paramTyped.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\EntityBrushableBlockFieldsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */