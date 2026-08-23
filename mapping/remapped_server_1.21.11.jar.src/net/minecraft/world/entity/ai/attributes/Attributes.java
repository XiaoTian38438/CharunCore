/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Attributes
/*    */ {
/*    */   public static final double DEFAULT_ATTACK_SPEED = 4.0D;
/* 14 */   public static final Holder<Attribute> ARMOR = register("armor", (new RangedAttribute("attribute.name.armor", 0.0D, 0.0D, 30.0D)).setSyncable(true));
/* 15 */   public static final Holder<Attribute> ARMOR_TOUGHNESS = register("armor_toughness", (new RangedAttribute("attribute.name.armor_toughness", 0.0D, 0.0D, 20.0D)).setSyncable(true));
/* 16 */   public static final Holder<Attribute> ATTACK_DAMAGE = register("attack_damage", new RangedAttribute("attribute.name.attack_damage", 2.0D, 0.0D, 2048.0D));
/* 17 */   public static final Holder<Attribute> ATTACK_KNOCKBACK = register("attack_knockback", new RangedAttribute("attribute.name.attack_knockback", 0.0D, 0.0D, 5.0D));
/* 18 */   public static final Holder<Attribute> ATTACK_SPEED = register("attack_speed", (new RangedAttribute("attribute.name.attack_speed", 4.0D, 0.0D, 1024.0D)).setSyncable(true));
/* 19 */   public static final Holder<Attribute> BLOCK_BREAK_SPEED = register("block_break_speed", (new RangedAttribute("attribute.name.block_break_speed", 1.0D, 0.0D, 1024.0D)).setSyncable(true));
/* 20 */   public static final Holder<Attribute> BLOCK_INTERACTION_RANGE = register("block_interaction_range", (new RangedAttribute("attribute.name.block_interaction_range", 4.5D, 0.0D, 64.0D)).setSyncable(true));
/* 21 */   public static final Holder<Attribute> BURNING_TIME = register("burning_time", (new RangedAttribute("attribute.name.burning_time", 1.0D, 0.0D, 1024.0D)).setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE));
/* 22 */   public static final Holder<Attribute> CAMERA_DISTANCE = register("camera_distance", (new RangedAttribute("attribute.name.camera_distance", 4.0D, 0.0D, 32.0D)).setSyncable(true));
/* 23 */   public static final Holder<Attribute> EXPLOSION_KNOCKBACK_RESISTANCE = register("explosion_knockback_resistance", (new RangedAttribute("attribute.name.explosion_knockback_resistance", 0.0D, 0.0D, 1.0D)).setSyncable(true));
/* 24 */   public static final Holder<Attribute> ENTITY_INTERACTION_RANGE = register("entity_interaction_range", (new RangedAttribute("attribute.name.entity_interaction_range", 3.0D, 0.0D, 64.0D)).setSyncable(true));
/* 25 */   public static final Holder<Attribute> FALL_DAMAGE_MULTIPLIER = register("fall_damage_multiplier", (new RangedAttribute("attribute.name.fall_damage_multiplier", 1.0D, 0.0D, 100.0D)).setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE));
/* 26 */   public static final Holder<Attribute> FLYING_SPEED = register("flying_speed", (new RangedAttribute("attribute.name.flying_speed", 0.4D, 0.0D, 1024.0D)).setSyncable(true));
/* 27 */   public static final Holder<Attribute> FOLLOW_RANGE = register("follow_range", new RangedAttribute("attribute.name.follow_range", 32.0D, 0.0D, 2048.0D));
/* 28 */   public static final Holder<Attribute> GRAVITY = register("gravity", (new RangedAttribute("attribute.name.gravity", 0.08D, -1.0D, 1.0D)).setSyncable(true).setSentiment(Attribute.Sentiment.NEUTRAL));
/* 29 */   public static final Holder<Attribute> JUMP_STRENGTH = register("jump_strength", (new RangedAttribute("attribute.name.jump_strength", 0.41999998688697815D, 0.0D, 32.0D)).setSyncable(true));
/* 30 */   public static final Holder<Attribute> KNOCKBACK_RESISTANCE = register("knockback_resistance", new RangedAttribute("attribute.name.knockback_resistance", 0.0D, 0.0D, 1.0D));
/* 31 */   public static final Holder<Attribute> LUCK = register("luck", (new RangedAttribute("attribute.name.luck", 0.0D, -1024.0D, 1024.0D)).setSyncable(true));
/* 32 */   public static final Holder<Attribute> MAX_ABSORPTION = register("max_absorption", (new RangedAttribute("attribute.name.max_absorption", 0.0D, 0.0D, 2048.0D)).setSyncable(true));
/* 33 */   public static final Holder<Attribute> MAX_HEALTH = register("max_health", (new RangedAttribute("attribute.name.max_health", 20.0D, 1.0D, 1024.0D)).setSyncable(true));
/* 34 */   public static final Holder<Attribute> MINING_EFFICIENCY = register("mining_efficiency", (new RangedAttribute("attribute.name.mining_efficiency", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
/* 35 */   public static final Holder<Attribute> MOVEMENT_EFFICIENCY = register("movement_efficiency", (new RangedAttribute("attribute.name.movement_efficiency", 0.0D, 0.0D, 1.0D)).setSyncable(true));
/* 36 */   public static final Holder<Attribute> MOVEMENT_SPEED = register("movement_speed", (new RangedAttribute("attribute.name.movement_speed", 0.7D, 0.0D, 1024.0D)).setSyncable(true));
/* 37 */   public static final Holder<Attribute> OXYGEN_BONUS = register("oxygen_bonus", (new RangedAttribute("attribute.name.oxygen_bonus", 0.0D, 0.0D, 1024.0D)).setSyncable(true));
/* 38 */   public static final Holder<Attribute> SAFE_FALL_DISTANCE = register("safe_fall_distance", (new RangedAttribute("attribute.name.safe_fall_distance", 3.0D, -1024.0D, 1024.0D)).setSyncable(true));
/* 39 */   public static final Holder<Attribute> SCALE = register("scale", (new RangedAttribute("attribute.name.scale", 1.0D, 0.0625D, 16.0D)).setSyncable(true).setSentiment(Attribute.Sentiment.NEUTRAL));
/* 40 */   public static final Holder<Attribute> SNEAKING_SPEED = register("sneaking_speed", (new RangedAttribute("attribute.name.sneaking_speed", 0.3D, 0.0D, 1.0D)).setSyncable(true));
/* 41 */   public static final Holder<Attribute> SPAWN_REINFORCEMENTS_CHANCE = register("spawn_reinforcements", new RangedAttribute("attribute.name.spawn_reinforcements", 0.0D, 0.0D, 1.0D));
/* 42 */   public static final Holder<Attribute> STEP_HEIGHT = register("step_height", (new RangedAttribute("attribute.name.step_height", 0.6D, 0.0D, 10.0D)).setSyncable(true));
/* 43 */   public static final Holder<Attribute> SUBMERGED_MINING_SPEED = register("submerged_mining_speed", (new RangedAttribute("attribute.name.submerged_mining_speed", 0.2D, 0.0D, 20.0D)).setSyncable(true));
/* 44 */   public static final Holder<Attribute> SWEEPING_DAMAGE_RATIO = register("sweeping_damage_ratio", (new RangedAttribute("attribute.name.sweeping_damage_ratio", 0.0D, 0.0D, 1.0D)).setSyncable(true));
/* 45 */   public static final Holder<Attribute> TEMPT_RANGE = register("tempt_range", new RangedAttribute("attribute.name.tempt_range", 10.0D, 0.0D, 2048.0D));
/* 46 */   public static final Holder<Attribute> WATER_MOVEMENT_EFFICIENCY = register("water_movement_efficiency", (new RangedAttribute("attribute.name.water_movement_efficiency", 0.0D, 0.0D, 1.0D)).setSyncable(true));
/* 47 */   public static final Holder<Attribute> WAYPOINT_TRANSMIT_RANGE = register("waypoint_transmit_range", (new RangedAttribute("attribute.name.waypoint_transmit_range", 0.0D, 0.0D, 6.0E7D)).setSentiment(Attribute.Sentiment.NEUTRAL));
/* 48 */   public static final Holder<Attribute> WAYPOINT_RECEIVE_RANGE = register("waypoint_receive_range", (new RangedAttribute("attribute.name.waypoint_receive_range", 0.0D, 0.0D, 6.0E7D)).setSentiment(Attribute.Sentiment.NEUTRAL));
/*    */   
/*    */   private static Holder<Attribute> register(String paramString, Attribute paramAttribute) {
/* 51 */     return (Holder<Attribute>)Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Identifier.withDefaultNamespace(paramString), paramAttribute);
/*    */   }
/*    */   
/*    */   public static Holder<Attribute> bootstrap(Registry<Attribute> paramRegistry) {
/* 55 */     return MAX_HEALTH;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\attributes\Attributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */