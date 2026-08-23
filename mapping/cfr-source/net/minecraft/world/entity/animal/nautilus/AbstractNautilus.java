/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.entity.animal.nautilus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.nautilus.NautilusAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class AbstractNautilus
extends TamableAnimal
implements HasCustomInventoryScreen,
PlayerRideableJumping {
    public static final int INVENTORY_SLOT_OFFSET = 500;
    public static final int INVENTORY_ROWS = 3;
    public static final int SMALL_RESTRICTION_RADIUS = 16;
    public static final int LARGE_RESTRICTION_RADIUS = 32;
    public static final int RESTRICTION_RADIUS_BUFFER = 8;
    private static final int EFFECT_DURATION = 60;
    private static final int EFFECT_REFRESH_RATE = 40;
    private static final double NAUTILUS_WATER_RESISTANCE = 0.9;
    private static final float IN_WATER_SPEED_MODIFIER = 0.011f;
    private static final float RIDDEN_SPEED_MODIFIER_IN_WATER = 0.0325f;
    private static final float RIDDEN_SPEED_MODIFIER_ON_LAND = 0.02f;
    private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
    private static final int DASH_COOLDOWN_TICKS = 40;
    private static final int DASH_MINIMUM_DURATION_TICKS = 5;
    private static final float DASH_MOMENTUM_IN_WATER = 1.2f;
    private static final float DASH_MOMENTUM_ON_LAND = 0.5f;
    private int dashCooldown = 0;
    protected float playerJumpPendingScale;
    protected SimpleContainer inventory;
    private static final double BUBBLE_SPREAD_FACTOR = 0.8;
    private static final double BUBBLE_DIRECTION_SCALE = 1.1;
    private static final double BUBBLE_Y_OFFSET = 0.25;
    private static final double BUBBLE_PROBABILITY_MULTIPLIER = 2.0;
    private static final float BUBBLE_PROBABILITY_MIN = 0.15f;
    private static final float BUBBLE_PROBABILITY_MAX = 1.0f;

    protected AbstractNautilus(EntityType<? extends AbstractNautilus> entityType, Level level) {
        super((EntityType<? extends TamableAnimal>)entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.011f, 0.0f, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
        this.createInventory();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return this.isTame() || this.isBaby() ? itemStack.is(ItemTags.NAUTILUS_FOOD) : itemStack.is(ItemTags.NAUTILUS_TAMING_ITEMS);
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (itemStack.is(ItemTags.NAUTILUS_BUCKET_FOOD)) {
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.WATER_BUCKET)));
        } else {
            super.usePlayerItem(player, interactionHand, itemStack);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 1.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.3f);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return 0.0f;
    }

    public static boolean checkNautilusSpawnRules(EntityType<? extends AbstractNautilus> entityType, LevelAccessor levelAccessor, EntitySpawnReason entitySpawnReason, BlockPos blockPos, RandomSource randomSource) {
        int n = levelAccessor.getSeaLevel();
        int n2 = n - 25;
        return blockPos.getY() >= n2 && blockPos.getY() <= n - 5 && levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER) && levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this);
    }

    @Override
    public boolean canUseSlot(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.SADDLE || equipmentSlot == EquipmentSlot.BODY) {
            return this.isAlive() && !this.isBaby() && this.isTame();
        }
        return super.canUseSlot(equipmentSlot);
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.BODY || equipmentSlot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(equipmentSlot);
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return !this.isVehicle();
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (this.isSaddled() && entity instanceof Player) {
            Player player = (Player)entity;
            return player;
        }
        return super.getControllingPassenger();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        float f = player.xxa;
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (player.zza != 0.0f) {
            float f4 = Mth.cos(player.getXRot() * ((float)Math.PI / 180));
            float f5 = -Mth.sin(player.getXRot() * ((float)Math.PI / 180));
            if (player.zza < 0.0f) {
                f4 *= -0.5f;
                f5 *= -0.5f;
            }
            f3 = f5;
            f2 = f4;
        }
        return new Vec3(f, f3, f2);
    }

    protected Vec2 getRiddenRotation(LivingEntity livingEntity) {
        return new Vec2(livingEntity.getXRot() * 0.5f, livingEntity.getYRot());
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        Vec2 vec2 = this.getRiddenRotation(player);
        float f = this.getYRot();
        float f2 = Mth.wrapDegrees(vec2.y - f);
        float f3 = 0.5f;
        this.setRot(f += f2 * 0.5f, vec2.x);
        this.yBodyRot = this.yHeadRot = f;
        this.yRotO = this.yHeadRot;
        if (this.isLocalInstanceAuthoritative()) {
            if (this.playerJumpPendingScale > 0.0f && !this.isJumping()) {
                this.executeRidersJump(this.playerJumpPendingScale, player);
            }
            this.playerJumpPendingScale = 0.0f;
        }
    }

    @Override
    protected void travelInWater(Vec3 vec3, double d, boolean bl, double d2) {
        float f = this.getSpeed();
        this.moveRelative(f, vec3);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return this.isInWater() ? 0.0325f * (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) : 0.02f * (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide()) {
            player.startRiding(this);
            if (!this.isVehicle()) {
                this.clearHome();
            }
        }
    }

    private int getNautilusRestrictionRadius() {
        if (!this.isBaby() && this.getItemBySlot(EquipmentSlot.SADDLE).isEmpty()) {
            return 32;
        }
        return 16;
    }

    protected void checkRestriction() {
        if (this.isLeashed() || this.isVehicle() || !this.isTame()) {
            return;
        }
        int n = this.getNautilusRestrictionRadius();
        if (this.hasHome() && this.getHomePosition().closerThan(this.blockPosition(), n + 8) && n == this.getHomeRadius()) {
            return;
        }
        this.setHomeTo(this.blockPosition(), n);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        this.checkRestriction();
        super.customServerAiStep(serverLevel);
    }

    private void applyEffects(Level level) {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player) {
            boolean bl;
            Player player = (Player)entity;
            boolean bl2 = player.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
            boolean bl3 = bl = level.getGameTime() % 40L == 0L;
            if (!bl2 || bl) {
                player.addEffect(new MobEffectInstance(MobEffects.BREATH_OF_THE_NAUTILUS, 60, 0, true, true, true));
            }
        }
    }

    private void spawnBubbles() {
        double d = this.getDeltaMovement().length();
        double d2 = Mth.clamp(d * 2.0, (double)0.15f, 1.0);
        if ((double)this.random.nextFloat() < d2) {
            float f = this.getYRot();
            float f2 = Mth.clamp(this.getXRot(), -10.0f, 10.0f);
            Vec3 vec3 = this.calculateViewVector(f2, f);
            double d3 = this.random.nextDouble() * 0.8 * (1.0 + d);
            double d4 = ((double)this.random.nextFloat() - 0.5) * d3;
            double d5 = ((double)this.random.nextFloat() - 0.5) * d3;
            double d6 = ((double)this.random.nextFloat() - 0.5) * d3;
            this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - vec3.x * 1.1, this.getY() - vec3.y + 0.25, this.getZ() - vec3.z * 1.1, d4, d5, d6);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.applyEffects(this.level());
        }
        if (this.isDashing() && this.dashCooldown < 35) {
            this.setDashing(false);
        }
        if (this.dashCooldown > 0) {
            --this.dashCooldown;
            if (this.dashCooldown == 0) {
                this.makeSound(this.getDashReadySound());
            }
        }
        if (this.isInWater()) {
            this.spawnBubbles();
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void onPlayerJump(int n) {
        if (!this.isSaddled() || this.dashCooldown > 0) {
            return;
        }
        this.playerJumpPendingScale = this.getPlayerJumpPendingScale(n);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DASH, false);
    }

    public boolean isDashing() {
        return this.entityData.get(DASH);
    }

    public void setDashing(boolean bl) {
        this.entityData.set(DASH, bl);
    }

    protected void executeRidersJump(float f, Player player) {
        this.addDeltaMovement(player.getLookAngle().scale((double)((this.isInWater() ? 1.2f : 0.5f) * f) * this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)this.getBlockSpeedFactor()));
        this.dashCooldown = 40;
        this.setDashing(true);
        this.needsSync = true;
    }

    @Override
    public void handleStartJump(int n) {
        this.makeSound(this.getDashSound());
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.setDashing(true);
    }

    @Override
    public int getJumpCooldown() {
        return this.dashCooldown;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (!this.firstTick && DASH.equals(entityDataAccessor)) {
            this.dashCooldown = this.dashCooldown == 0 ? 40 : this.dashCooldown;
        }
        super.onSyncedDataUpdated(entityDataAccessor);
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    protected @Nullable SoundEvent getDashSound() {
        return null;
    }

    protected @Nullable SoundEvent getDashReadySound() {
        return null;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        this.setPersistenceRequired();
        return super.interact(player, interactionHand);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (this.isBaby()) {
            return super.mobInteract(player, interactionHand);
        }
        if (this.isTame() && player.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.SUCCESS;
        }
        if (!itemStack.isEmpty()) {
            if (!this.level().isClientSide() && !this.isTame() && this.isFood(itemStack)) {
                this.usePlayerItem(player, interactionHand, itemStack);
                this.tryToTame(player);
                return InteractionResult.SUCCESS_SERVER;
            }
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
                this.heal(foodProperties != null ? (float)(2 * foodProperties.nutrition()) : 1.0f);
                this.usePlayerItem(player, interactionHand, itemStack);
                this.playEatingSound();
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, interactionHand);
            if (interactionResult.consumesAction()) {
                return interactionResult;
            }
        }
        if (this.isTame() && !player.isSecondaryUseActive() && !this.isFood(itemStack)) {
            this.doPlayerRide(player);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, interactionHand);
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }
        this.playEatingSound();
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return true;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        Entity entity;
        boolean bl = super.hurtServer(serverLevel, damageSource, f);
        if (bl && (entity = damageSource.getEntity()) instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            NautilusAi.setAngerTarget(serverLevel, this, livingEntity);
        }
        return bl;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance.getEffect() == MobEffects.POISON) {
            return false;
        }
        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomSource = serverLevelAccessor.getRandom();
        NautilusAi.initMemories(this, randomSource);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, spawnGroupData);
    }

    @Override
    protected Holder<SoundEvent> getEquipSound(EquipmentSlot equipmentSlot, ItemStack itemStack, Equippable equippable) {
        if (equipmentSlot == EquipmentSlot.SADDLE && this.isUnderWater()) {
            return SoundEvents.NAUTILUS_SADDLE_UNDERWATER_EQUIP;
        }
        if (equipmentSlot == EquipmentSlot.SADDLE) {
            return SoundEvents.NAUTILUS_SADDLE_EQUIP;
        }
        return super.getEquipSound(equipmentSlot, itemStack, equippable);
    }

    public final int getInventorySize() {
        return AbstractMountInventoryMenu.getInventorySize(this.getInventoryColumns());
    }

    protected void createInventory() {
        SimpleContainer simpleContainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simpleContainer != null) {
            int n = Math.min(simpleContainer.getContainerSize(), this.inventory.getContainerSize());
            for (int i = 0; i < n; ++i) {
                ItemStack itemStack = simpleContainer.getItem(i);
                if (itemStack.isEmpty()) continue;
                this.inventory.setItem(i, itemStack.copy());
            }
        }
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide() && (!this.isVehicle() || this.hasPassenger(player)) && this.isTame()) {
            player.openNautilusInventory(this, this.inventory);
        }
    }

    @Override
    public @Nullable SlotAccess getSlot(int n) {
        int n2 = n - 500;
        if (n2 >= 0 && n2 < this.inventory.getContainerSize()) {
            return this.inventory.getSlot(n2);
        }
        return super.getSlot(n);
    }

    public boolean hasInventoryChanged(Container container) {
        return this.inventory != container;
    }

    public int getInventoryColumns() {
        return 0;
    }

    protected boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }

    protected boolean isAggravated() {
        return this.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }
}

