/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class ServerExplosion
/*     */   implements Explosion
/*     */ {
/*  41 */   private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
/*     */   
/*     */   private static final int MAX_DROPS_PER_COMBINED_STACK = 16;
/*     */   
/*     */   private static final float LARGE_EXPLOSION_RADIUS = 2.0F;
/*     */   private final boolean fire;
/*     */   private final Explosion.BlockInteraction blockInteraction;
/*     */   private final ServerLevel level;
/*     */   private final Vec3 center;
/*     */   private final Entity source;
/*     */   private final float radius;
/*     */   private final DamageSource damageSource;
/*     */   private final ExplosionDamageCalculator damageCalculator;
/*  54 */   private final Map<Player, Vec3> hitPlayers = new HashMap<>();
/*     */   
/*     */   public ServerExplosion(ServerLevel paramServerLevel, Entity paramEntity, DamageSource paramDamageSource, ExplosionDamageCalculator paramExplosionDamageCalculator, Vec3 paramVec3, float paramFloat, boolean paramBoolean, Explosion.BlockInteraction paramBlockInteraction) {
/*  57 */     this.level = paramServerLevel;
/*  58 */     this.source = paramEntity;
/*  59 */     this.radius = paramFloat;
/*  60 */     this.center = paramVec3;
/*  61 */     this.fire = paramBoolean;
/*  62 */     this.blockInteraction = paramBlockInteraction;
/*  63 */     this.damageSource = (paramDamageSource == null) ? paramServerLevel.damageSources().explosion(this) : paramDamageSource;
/*  64 */     this.damageCalculator = (paramExplosionDamageCalculator == null) ? makeDamageCalculator(paramEntity) : paramExplosionDamageCalculator;
/*     */   }
/*     */   
/*     */   private ExplosionDamageCalculator makeDamageCalculator(Entity paramEntity) {
/*  68 */     return (paramEntity == null) ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(paramEntity);
/*     */   }
/*     */   
/*     */   public static float getSeenPercent(Vec3 paramVec3, Entity paramEntity) {
/*  72 */     AABB aABB = paramEntity.getBoundingBox();
/*  73 */     double d1 = 1.0D / ((aABB.maxX - aABB.minX) * 2.0D + 1.0D);
/*  74 */     double d2 = 1.0D / ((aABB.maxY - aABB.minY) * 2.0D + 1.0D);
/*  75 */     double d3 = 1.0D / ((aABB.maxZ - aABB.minZ) * 2.0D + 1.0D);
/*     */     
/*  77 */     double d4 = (1.0D - Math.floor(1.0D / d1) * d1) / 2.0D;
/*  78 */     double d5 = (1.0D - Math.floor(1.0D / d3) * d3) / 2.0D;
/*     */     
/*  80 */     if (d1 < 0.0D || d2 < 0.0D || d3 < 0.0D) {
/*  81 */       return 0.0F;
/*     */     }
/*  83 */     byte b1 = 0;
/*  84 */     byte b2 = 0; double d6;
/*  85 */     for (d6 = 0.0D; d6 <= 1.0D; d6 += d1) {
/*  86 */       double d; for (d = 0.0D; d <= 1.0D; d += d2) {
/*  87 */         double d7; for (d7 = 0.0D; d7 <= 1.0D; d7 += d3) {
/*  88 */           double d8 = Mth.lerp(d6, aABB.minX, aABB.maxX);
/*  89 */           double d9 = Mth.lerp(d, aABB.minY, aABB.maxY);
/*  90 */           double d10 = Mth.lerp(d7, aABB.minZ, aABB.maxZ);
/*  91 */           Vec3 vec3 = new Vec3(d8 + d4, d9, d10 + d5);
/*  92 */           if (paramEntity.level().clip(new ClipContext(vec3, paramVec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, paramEntity)).getType() == HitResult.Type.MISS) {
/*  93 */             b1++;
/*     */           }
/*  95 */           b2++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     return b1 / b2;
/*     */   }
/*     */ 
/*     */   
/*     */   public float radius() {
/* 105 */     return this.radius;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 center() {
/* 110 */     return this.center;
/*     */   }
/*     */   
/*     */   private List<BlockPos> calculateExplodedPositions() {
/* 114 */     HashSet<BlockPos> hashSet = new HashSet();
/*     */     
/* 116 */     byte b1 = 16;
/* 117 */     for (byte b2 = 0; b2 < 16; b2++) {
/* 118 */       for (byte b = 0; b < 16; b++) {
/* 119 */         for (byte b3 = 0; b3 < 16; b3++) {
/* 120 */           if (b2 == 0 || b2 == 15 || b == 0 || b == 15 || b3 == 0 || b3 == 15) {
/*     */ 
/*     */ 
/*     */             
/* 124 */             double d1 = (b2 / 15.0F * 2.0F - 1.0F);
/* 125 */             double d2 = (b / 15.0F * 2.0F - 1.0F);
/* 126 */             double d3 = (b3 / 15.0F * 2.0F - 1.0F);
/* 127 */             double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/*     */             
/* 129 */             d1 /= d4;
/* 130 */             d2 /= d4;
/* 131 */             d3 /= d4;
/*     */             
/* 133 */             float f1 = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
/* 134 */             double d5 = this.center.x;
/* 135 */             double d6 = this.center.y;
/* 136 */             double d7 = this.center.z;
/*     */             
/* 138 */             float f2 = 0.3F;
/* 139 */             while (f1 > 0.0F) {
/* 140 */               BlockPos blockPos = BlockPos.containing(d5, d6, d7);
/* 141 */               BlockState blockState = this.level.getBlockState(blockPos);
/* 142 */               FluidState fluidState = this.level.getFluidState(blockPos);
/*     */               
/* 144 */               if (!this.level.isInWorldBounds(blockPos)) {
/*     */                 break;
/*     */               }
/*     */               
/* 148 */               Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, (BlockGetter)this.level, blockPos, blockState, fluidState);
/* 149 */               if (optional.isPresent()) {
/* 150 */                 f1 -= (((Float)optional.get()).floatValue() + 0.3F) * 0.3F;
/*     */               }
/*     */               
/* 153 */               if (f1 > 0.0F && this.damageCalculator.shouldBlockExplode(this, (BlockGetter)this.level, blockPos, blockState, f1)) {
/* 154 */                 hashSet.add(blockPos);
/*     */               }
/*     */               
/* 157 */               d5 += d1 * 0.30000001192092896D;
/* 158 */               d6 += d2 * 0.30000001192092896D;
/* 159 */               d7 += d3 * 0.30000001192092896D;
/* 160 */               f1 -= 0.22500001F;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 166 */     return (List<BlockPos>)new ObjectArrayList(hashSet);
/*     */   }
/*     */   
/*     */   private void hurtEntities() {
/* 170 */     if (this.radius < 1.0E-5F) {
/*     */       return;
/*     */     }
/* 173 */     float f = this.radius * 2.0F;
/*     */     
/* 175 */     int i = Mth.floor(this.center.x - f - 1.0D);
/* 176 */     int j = Mth.floor(this.center.x + f + 1.0D);
/* 177 */     int k = Mth.floor(this.center.y - f - 1.0D);
/* 178 */     int m = Mth.floor(this.center.y + f + 1.0D);
/* 179 */     int n = Mth.floor(this.center.z - f - 1.0D);
/* 180 */     int i1 = Mth.floor(this.center.z + f + 1.0D);
/* 181 */     List list = this.level.getEntities(this.source, new AABB(i, k, n, j, m, i1));
/*     */     
/* 183 */     for (Entity entity : list) {
/* 184 */       if (entity.ignoreExplosion(this)) {
/*     */         continue;
/*     */       }
/* 187 */       double d1 = Math.sqrt(entity.distanceToSqr(this.center)) / f;
/* 188 */       if (d1 > 1.0D) {
/*     */         continue;
/*     */       }
/*     */       
/* 192 */       Vec3 vec31 = (entity instanceof net.minecraft.world.entity.item.PrimedTnt) ? entity.position() : entity.getEyePosition();
/* 193 */       Vec3 vec32 = vec31.subtract(this.center).normalize();
/*     */       
/* 195 */       boolean bool = this.damageCalculator.shouldDamageEntity(this, entity);
/* 196 */       float f1 = this.damageCalculator.getKnockbackMultiplier(entity);
/* 197 */       float f2 = (bool || f1 != 0.0F) ? getSeenPercent(this.center, entity) : 0.0F;
/*     */       
/* 199 */       if (bool) {
/* 200 */         entity.hurtServer(this.level, this.damageSource, this.damageCalculator.getEntityDamageAmount(this, entity, f2));
/*     */       }
/*     */       
/* 203 */       LivingEntity livingEntity = (LivingEntity)entity; double d2 = (entity instanceof LivingEntity) ? livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE) : 0.0D;
/*     */       
/* 205 */       double d3 = (1.0D - d1) * f2 * f1 * (1.0D - d2);
/* 206 */       Vec3 vec33 = vec32.scale(d3);
/* 207 */       entity.push(vec33);
/*     */       
/* 209 */       if (entity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile) { Projectile projectile = (Projectile)entity;
/* 210 */         projectile.setOwner(this.damageSource.getEntity()); }
/* 211 */       else if (entity instanceof Player) { Player player = (Player)entity;
/* 212 */         if (!player.isSpectator() && (!player.isCreative() || !(player.getAbilities()).flying)) {
/* 213 */           this.hitPlayers.put(player, vec33);
/*     */         } }
/*     */ 
/*     */       
/* 217 */       entity.onExplosionHit(this.source);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void interactWithBlocks(List<BlockPos> paramList) {
/* 222 */     ArrayList arrayList = new ArrayList();
/* 223 */     Util.shuffle(paramList, this.level.random);
/*     */     
/* 225 */     for (BlockPos blockPos : paramList) {
/* 226 */       this.level.getBlockState(blockPos).onExplosionHit(this.level, blockPos, this, (paramItemStack, paramBlockPos) -> addOrAppendStack(paramList, paramItemStack, paramBlockPos));
/*     */     }
/*     */     
/* 229 */     for (StackCollector stackCollector : arrayList) {
/* 230 */       Block.popResource((Level)this.level, stackCollector.pos, stackCollector.stack);
/*     */     }
/*     */   }
/*     */   
/*     */   private void createFire(List<BlockPos> paramList) {
/* 235 */     for (BlockPos blockPos : paramList) {
/* 236 */       if (this.level.random.nextInt(3) == 0 && this.level.getBlockState(blockPos).isAir() && this.level.getBlockState(blockPos.below()).isSolidRender()) {
/* 237 */         this.level.setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)this.level, blockPos));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int explode() {
/* 243 */     this.level.gameEvent(this.source, (Holder)GameEvent.EXPLODE, this.center);
/*     */     
/* 245 */     List<BlockPos> list = calculateExplodedPositions();
/* 246 */     hurtEntities();
/*     */     
/* 248 */     if (interactsWithBlocks()) {
/* 249 */       ProfilerFiller profilerFiller = Profiler.get();
/* 250 */       profilerFiller.push("explosion_blocks");
/* 251 */       interactWithBlocks(list);
/* 252 */       profilerFiller.pop();
/*     */     } 
/*     */     
/* 255 */     if (this.fire) {
/* 256 */       createFire(list);
/*     */     }
/* 258 */     return list.size();
/*     */   }
/*     */   
/*     */   private static class StackCollector {
/*     */     final BlockPos pos;
/*     */     ItemStack stack;
/*     */     
/*     */     StackCollector(BlockPos param1BlockPos, ItemStack param1ItemStack) {
/* 266 */       this.pos = param1BlockPos;
/* 267 */       this.stack = param1ItemStack;
/*     */     }
/*     */     
/*     */     public void tryMerge(ItemStack param1ItemStack) {
/* 271 */       if (ItemEntity.areMergable(this.stack, param1ItemStack)) {
/* 272 */         this.stack = ItemEntity.merge(this.stack, param1ItemStack, 16);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addOrAppendStack(List<StackCollector> paramList, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 278 */     for (StackCollector stackCollector : paramList) {
/* 279 */       stackCollector.tryMerge(paramItemStack);
/* 280 */       if (paramItemStack.isEmpty()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 286 */     paramList.add(new StackCollector(paramBlockPos, paramItemStack));
/*     */   }
/*     */   
/*     */   private boolean interactsWithBlocks() {
/* 290 */     return (this.blockInteraction != Explosion.BlockInteraction.KEEP);
/*     */   }
/*     */   
/*     */   public Map<Player, Vec3> getHitPlayers() {
/* 294 */     return this.hitPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerLevel level() {
/* 299 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getIndirectSourceEntity() {
/* 304 */     return Explosion.getIndirectSourceEntity(this.source);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getDirectSourceEntity() {
/* 309 */     return this.source;
/*     */   }
/*     */   
/*     */   public DamageSource getDamageSource() {
/* 313 */     return this.damageSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public Explosion.BlockInteraction getBlockInteraction() {
/* 318 */     return this.blockInteraction;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTriggerBlocks() {
/* 323 */     if (this.blockInteraction != Explosion.BlockInteraction.TRIGGER_BLOCK) {
/* 324 */       return false;
/*     */     }
/*     */     
/* 327 */     if (this.source != null && this.source.getType() == EntityType.BREEZE_WIND_CHARGE) {
/* 328 */       return ((Boolean)this.level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/*     */     }
/*     */     
/* 331 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldAffectBlocklikeEntities() {
/* 336 */     boolean bool = ((Boolean)this.level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/* 337 */     boolean bool1 = (this.source == null || (this.source.getType() != EntityType.BREEZE_WIND_CHARGE && this.source.getType() != EntityType.WIND_CHARGE)) ? true : false;
/* 338 */     if (bool) {
/* 339 */       return bool1;
/*     */     }
/* 341 */     return (this.blockInteraction.shouldAffectBlocklikeEntities() && bool1);
/*     */   }
/*     */   
/*     */   public boolean isSmall() {
/* 345 */     return (this.radius < 2.0F || !interactsWithBlocks());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ServerExplosion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */