/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*    */ import net.minecraft.world.entity.animal.frog.FrogAi;
/*    */ import net.minecraft.world.entity.animal.nautilus.NautilusAi;
/*    */ 
/*    */ public class SensorType<U extends Sensor<?>>
/*    */ {
/* 15 */   public static final SensorType<DummySensor> DUMMY = register("dummy", DummySensor::new);
/* 16 */   public static final SensorType<NearestItemSensor> NEAREST_ITEMS = register("nearest_items", NearestItemSensor::new);
/* 17 */   public static final SensorType<NearestLivingEntitySensor<LivingEntity>> NEAREST_LIVING_ENTITIES = register("nearest_living_entities", NearestLivingEntitySensor::new);
/* 18 */   public static final SensorType<PlayerSensor> NEAREST_PLAYERS = register("nearest_players", PlayerSensor::new);
/* 19 */   public static final SensorType<NearestBedSensor> NEAREST_BED = register("nearest_bed", NearestBedSensor::new);
/* 20 */   public static final SensorType<HurtBySensor> HURT_BY = register("hurt_by", HurtBySensor::new);
/* 21 */   public static final SensorType<VillagerHostilesSensor> VILLAGER_HOSTILES = register("villager_hostiles", VillagerHostilesSensor::new);
/* 22 */   public static final SensorType<VillagerBabiesSensor> VILLAGER_BABIES = register("villager_babies", VillagerBabiesSensor::new);
/* 23 */   public static final SensorType<SecondaryPoiSensor> SECONDARY_POIS = register("secondary_pois", SecondaryPoiSensor::new);
/* 24 */   public static final SensorType<GolemSensor> GOLEM_DETECTED = register("golem_detected", GolemSensor::new);
/* 25 */   public static final SensorType<MobSensor<Armadillo>> ARMADILLO_SCARE_DETECTED = register("armadillo_scare_detected", () -> new MobSensor<>(5, Armadillo::isScaredBy, Armadillo::canStayRolledUp, MemoryModuleType.DANGER_DETECTED_RECENTLY, 80));
/* 26 */   public static final SensorType<PiglinSpecificSensor> PIGLIN_SPECIFIC_SENSOR = register("piglin_specific_sensor", PiglinSpecificSensor::new);
/* 27 */   public static final SensorType<PiglinBruteSpecificSensor> PIGLIN_BRUTE_SPECIFIC_SENSOR = register("piglin_brute_specific_sensor", PiglinBruteSpecificSensor::new);
/* 28 */   public static final SensorType<HoglinSpecificSensor> HOGLIN_SPECIFIC_SENSOR = register("hoglin_specific_sensor", HoglinSpecificSensor::new);
/* 29 */   public static final SensorType<AdultSensor> NEAREST_ADULT = register("nearest_adult", AdultSensor::new);
/* 30 */   public static final SensorType<AdultSensor> NEAREST_ADULT_ANY_TYPE = register("nearest_adult_any_type", AdultSensorAnyType::new);
/* 31 */   public static final SensorType<AxolotlAttackablesSensor> AXOLOTL_ATTACKABLES = register("axolotl_attackables", AxolotlAttackablesSensor::new);
/* 32 */   public static final SensorType<TemptingSensor> FOOD_TEMPTATIONS = register("food_temptations", TemptingSensor::forAnimal);
/* 33 */   public static final SensorType<TemptingSensor> FROG_TEMPTATIONS = register("frog_temptations", () -> new TemptingSensor(FrogAi.getTemptations()));
/* 34 */   public static final SensorType<TemptingSensor> NAUTILUS_TEMPTATIONS = register("nautilus_temptations", () -> new TemptingSensor(NautilusAi.getTemptations()));
/* 35 */   public static final SensorType<FrogAttackablesSensor> FROG_ATTACKABLES = register("frog_attackables", FrogAttackablesSensor::new);
/* 36 */   public static final SensorType<IsInWaterSensor> IS_IN_WATER = register("is_in_water", IsInWaterSensor::new);
/* 37 */   public static final SensorType<WardenEntitySensor> WARDEN_ENTITY_SENSOR = register("warden_entity_sensor", WardenEntitySensor::new);
/* 38 */   public static final SensorType<BreezeAttackEntitySensor> BREEZE_ATTACK_ENTITY_SENSOR = register("breeze_attack_entity_sensor", BreezeAttackEntitySensor::new);
/*    */   
/*    */   private final Supplier<U> factory;
/*    */   
/*    */   private SensorType(Supplier<U> paramSupplier) {
/* 43 */     this.factory = paramSupplier;
/*    */   }
/*    */   
/*    */   public U create() {
/* 47 */     return this.factory.get();
/*    */   }
/*    */   
/*    */   private static <U extends Sensor<?>> SensorType<U> register(String paramString, Supplier<U> paramSupplier) {
/* 51 */     return (SensorType<U>)Registry.register((Registry)BuiltInRegistries.SENSOR_TYPE, Identifier.withDefaultNamespace(paramString), new SensorType<>(paramSupplier));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\SensorType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */