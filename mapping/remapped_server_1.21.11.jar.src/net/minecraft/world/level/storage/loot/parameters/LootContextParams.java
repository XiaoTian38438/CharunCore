/*    */ package net.minecraft.world.level.storage.loot.parameters;
/*    */ 
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LootContextParams
/*    */ {
/* 14 */   public static final ContextKey<Entity> THIS_ENTITY = ContextKey.vanilla("this_entity");
/*    */   
/* 16 */   public static final ContextKey<Entity> INTERACTING_ENTITY = ContextKey.vanilla("interacting_entity");
/*    */   
/* 18 */   public static final ContextKey<Entity> TARGET_ENTITY = ContextKey.vanilla("target_entity");
/*    */   
/* 20 */   public static final ContextKey<Player> LAST_DAMAGE_PLAYER = ContextKey.vanilla("last_damage_player");
/*    */   
/* 22 */   public static final ContextKey<DamageSource> DAMAGE_SOURCE = ContextKey.vanilla("damage_source");
/*    */   
/* 24 */   public static final ContextKey<Entity> ATTACKING_ENTITY = ContextKey.vanilla("attacking_entity");
/*    */   
/* 26 */   public static final ContextKey<Entity> DIRECT_ATTACKING_ENTITY = ContextKey.vanilla("direct_attacking_entity");
/*    */   
/* 28 */   public static final ContextKey<Vec3> ORIGIN = ContextKey.vanilla("origin");
/*    */   
/* 30 */   public static final ContextKey<BlockState> BLOCK_STATE = ContextKey.vanilla("block_state");
/*    */   
/* 32 */   public static final ContextKey<BlockEntity> BLOCK_ENTITY = ContextKey.vanilla("block_entity");
/*    */   
/* 34 */   public static final ContextKey<ItemStack> TOOL = ContextKey.vanilla("tool");
/*    */   
/* 36 */   public static final ContextKey<Float> EXPLOSION_RADIUS = ContextKey.vanilla("explosion_radius");
/*    */   
/* 38 */   public static final ContextKey<Integer> ENCHANTMENT_LEVEL = ContextKey.vanilla("enchantment_level");
/*    */   
/* 40 */   public static final ContextKey<Boolean> ENCHANTMENT_ACTIVE = ContextKey.vanilla("enchantment_active");
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParams.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */