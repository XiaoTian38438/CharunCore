package com.CharunCore.server.world.entity;

import com.CharunCore.server.Main;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.ExplosionEngine;
import com.CharunCore.server.utils.BlockManager;
import java.util.Random;

public class MobEntity extends LivingEntity {
    public final String entityName;
    private static final Random aiRng = new Random();
    private int aiTimer = 0;
    private int attackCooldown = 0;
    private int fleeTimer = 0;
    private int jumpCooldown = 0;
    private int angeredTimer = 0;
    private int pathTimer = 0;
    private int[] pathStep = null;
    /** 苦力怕引信(原版 1.5 秒): -1=未点燃, >=0 表示 fuse 剩余 tick。 */
    private int fuseTimer = -1;
    public boolean baby = false;
    /** 繁殖: >0 表示处于发情期(爱心), 每 tick 自减。 */
    public int loveTimer = 0;
    /** 繁殖后冷却(不可再繁殖), 每 tick 自减。 */
    public int breedCooldown = 0;
    /** 幼体成长计时(仅 baby 用), 达到 BABY_MATURE 转为成年。 */
    public int babyAge = 0;
    /** 发情期时长(tick): 30 秒。 */
    private static final int LOVE_MAX = 600;
    /** 繁殖冷却(tick): 5 分钟, 期间不可再次繁殖。 */
    private static final int BREED_CD = 6000;
    /** 幼体成熟所需 tick: 原版 20 分钟(24000), 此处加速为 2 分钟便于体验。 */
    private static final int BABY_MATURE = 2400;
    /** 距离最近玩家 >32 格后累计的存活刻数, 用于随机消失判定。 */
    public int despawnCounter = 0;
    /** BUG8: 上次广播给客户端的"着火"状态, 仅在状态翻转时重发, 避免每 tick 刷包。 */
    private boolean lastFireBroadcast = false;
    /** 猪灵以物易物: 拾取金锭后倒计时(原版 120 tick=6 秒), 到 0 吐出战利品; -1=未拾取。 */
    private int barterTimer = -1;

    // ── 村民交易系统（仅 entityName=="villager" 使用）──────────────────────
    public String profession = null;      // 职业名（13 职业之一），null = 无业
    public int villagerLevel = 1;          // 1..5（novice..master）
    public int villagerXp = 0;             // 当前等级内累计经验
    public java.util.List<VillagerTrade> villagerTrades = null; // 可交易列表（懒初始化）
    private long lastRestockDay = -1;
    private int restockCount = 0;
    /** 每次升级所需累计经验（level→level+1），原版阈值。 */
    private static final int[] XP_TO_NEXT = {10, 70, 150, 250};

    public MobEntity(int id, String entityName, double x, double y, double z) {
        super(id, 0, x, y, z);
        this.entityName = entityName;
        switch (entityName) {
            case "zombie", "skeleton", "creeper", "blaze", "husk", "drowned",
                 "stray", "zombified_piglin", "zombie_villager" -> { this.maxHealth = 20.0f; }
            case "spider", "cave_spider" -> { this.maxHealth = 16.0f; }
            case "enderman" -> { this.maxHealth = 40.0f; }
            case "witch" -> { this.maxHealth = 26.0f; }
            case "slime", "magma_cube" -> { this.maxHealth = 16.0f; }
            case "ghast" -> { this.maxHealth = 10.0f; }
            case "wither_skeleton" -> { this.maxHealth = 20.0f; }
            case "cow", "sheep", "pig", "mooshroom" -> { this.maxHealth = 10.0f; }
            case "chicken" -> { this.maxHealth = 4.0f; }
            case "rabbit" -> { this.maxHealth = 3.0f; }
            case "squid" -> { this.maxHealth = 10.0f; }
            case "wolf" -> { this.maxHealth = 8.0f; }
            case "villager" -> { this.maxHealth = 20.0f; }
            default -> { this.maxHealth = 20.0f; }
        }
        this.health = this.maxHealth;
        if (entityName.equals("villager")) initVillager();
        switch (entityName) {
            case "chicken" -> { this.width = 0.4; this.height = 0.7; }
            case "cow", "sheep", "mooshroom" -> { this.width = 0.9; this.height = 1.3; }
            case "pig" -> { this.width = 0.9; this.height = 0.9; }
            case "rabbit" -> { this.width = 0.4; this.height = 0.5; }
            case "spider", "cave_spider" -> { this.width = 1.4; this.height = 0.9; }
            case "enderman" -> { this.width = 0.6; this.height = 2.9; }
            case "creeper" -> { this.width = 0.6; this.height = 1.7; }
            default -> { this.width = 0.6; this.height = 1.95; }
        }
    }

    public boolean isHostile() {
        boolean day = Main.dayTime < 12500 || Main.dayTime > 23000;
        return switch (entityName) {
            // 原版: 蜘蛛白天中立(光照充足时), 夜晚主动敌对
            case "spider", "cave_spider" -> !day;
            case "zombie", "skeleton", "creeper",
                 "witch", "slime", "blaze", "ghast", "magma_cube", "phantom",
                 "pillager", "vindicator", "ravager", "husk", "drowned", "stray",
                 "wither_skeleton", "zombie_villager", "silverfish", "endermite" -> true;
            default -> false;
        };
    }

    public boolean isPassive() {
        return switch (entityName) {
            case "cow", "sheep", "pig", "chicken", "rabbit", "mooshroom",
                 "squid", "bat", "horse", "villager" -> true;
            default -> false;
        };
    }

    public boolean isNeutral() {
        return switch (entityName) {
            case "wolf", "zombified_piglin", "enderman" -> true;
            default -> false;
        };
    }

    public boolean burnsInDaylight() {
        return switch (entityName) {
            case "zombie", "skeleton", "husk", "stray", "zombie_villager" -> true;
            default -> false;
        };
    }

    /** 是否可被繁殖的被动生物(不含村民/马, 二者有独立逻辑)。 */
    public boolean isBreedable() {
        return switch (entityName) {
            case "cow", "sheep", "pig", "chicken", "rabbit", "mooshroom" -> true;
            default -> false;
        };
    }

    /** 某食物是否能喂养该可繁殖生物(原版食物表)。 */
    public static boolean isBreedFood(String mob, String food) {
        return switch (mob) {
            case "cow", "sheep", "mooshroom" -> "wheat".equals(food);
            case "pig" -> "carrot".equals(food) || "potato".equals(food) || "beetroot".equals(food);
            case "chicken" -> "wheat_seeds".equals(food) || "pumpkin_seeds".equals(food)
                             || "melon_seeds".equals(food) || "beetroot_seeds".equals(food);
            case "rabbit" -> "dandelion".equals(food) || "carrot".equals(food) || "golden_carrot".equals(food);
            default -> false;
        };
    }

    /** 按生物类型恢复成年体型(幼体成长为成年时调用)。 */
    public void applyAdultSize() {
        switch (entityName) {
            case "chicken" -> { this.width = 0.4; this.height = 0.7; }
            case "cow", "sheep", "mooshroom" -> { this.width = 0.9; this.height = 1.3; }
            case "pig" -> { this.width = 0.9; this.height = 0.9; }
            case "rabbit" -> { this.width = 0.4; this.height = 0.5; }
            case "spider", "cave_spider" -> { this.width = 1.4; this.height = 0.9; }
            case "enderman" -> { this.width = 0.6; this.height = 2.9; }
            case "creeper" -> { this.width = 0.6; this.height = 1.7; }
            default -> { this.width = 0.6; this.height = 1.95; }
        }
    }

    private float getAttackDamage() {
        return switch (entityName) {
            case "zombie", "husk", "drowned", "zombie_villager" -> 3.0f;
            case "skeleton", "stray" -> 2.0f;
            case "creeper" -> 6.0f;
            case "spider" -> 2.0f;
            case "cave_spider" -> 2.0f;
            case "enderman" -> 7.0f;
            case "witch" -> 3.0f;
            case "slime" -> 2.0f;
            case "blaze" -> 6.0f;
            case "ghast" -> 17.0f;
            case "magma_cube" -> 4.0f;
            case "phantom" -> 3.0f;
            case "pillager" -> 5.0f;
            case "vindicator" -> 13.0f;
            case "ravager" -> 12.0f;
            case "wither_skeleton" -> 8.0f;
            default -> 2.0f;
        };
    }

    public int getXpDrop() {
        if (isPassive()) return 1 + aiRng.nextInt(3);
        // 原版经验表: 烈焰人 10, 其余常规敌对 5
        return switch (entityName) {
            case "blaze" -> 10;
            case "wither_skeleton" -> 5;
            default -> 5;
        };
    }

    @Override
    public void damage(float amount, String source) {
        super.damage(amount, source);
        if (deathTime == 0 && isPassive()) {
            fleeTimer = 60;
        }
        if (deathTime == 0 && isNeutral() && "player".equals(source)) {
            angeredTimer = 240; // 被玩家攻击后约 12 秒敌对
        }
        // 原版: 末影人受击 33% 概率瞬移(8-32 格)。同步由 EntityManager.sendMove 每 tick
        // 绝对坐标广播完成, 客户端会看到瞬移跳变。
        if (deathTime == 0 && entityName.equals("enderman") && "player".equals(source)) {
            if (aiRng.nextInt(3) == 0) {
                double ang = aiRng.nextDouble() * Math.PI * 2;
                double dist = 8.0 + aiRng.nextDouble() * 24.0;
                x = x + Math.cos(ang) * dist;
                z = z + Math.sin(ang) * dist;
                vx = 0; vz = 0; vy = 0;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (entityName.equals("villager")) villagerRestockIfDue();

        // 猪灵以物易物(barter): 非战斗状态下, 检测 4 格内金锭掉落物并拾取;
        // 拾取后 120 tick(6 秒)在脚下吐出随机战利品(原版行为, 非下界/非和平才以物易物)。
        if (entityName.equals("piglin") && barterTimer < 0 && deathTime == 0) {
            int goldId = com.CharunCore.server.utils.BlockManager.getItemIdByName("gold_ingot");
            if (goldId > 0) {
                for (Entity e : EntityManager.getAllEntities()) {
                    if (!(e instanceof ItemEntity item)) continue;
                    if (item.dim != this.dim || item.itemId != goldId) continue;
                    if (item.pickupDelay > 0) continue;
                    double dx = item.x - x, dy = item.y - y, dz = item.z - z;
                    if (dx * dx + dy * dy + dz * dz < 4.0 * 4.0) {
                        item.remove();
                        barterTimer = 120;
                        break;
                    }
                }
            }
        }
        if (entityName.equals("piglin") && barterTimer >= 0) {
            barterTimer--;
            if (barterTimer == 0) {
                String loot = rollBarterLoot();
                if (loot != null) {
                    int lootId = com.CharunCore.server.utils.BlockManager.getItemIdByName(loot);
                    if (lootId > 0) {
                        ItemEntity drop = new ItemEntity(
                            EntityManager.allocateId(), x, y + 0.5, z, lootId, 1);
                        drop.dim = this.dim;
                        drop.pickupDelay = 20;
                        EntityManager.addEntity(drop);
                    }
                }
            }
        }
        // 原版: 末影人接触水(或雨)持续受伤害。这里按水中每 tick 1 点(drown 源),
        // 40 血约 2 秒死亡, 与原版"末影人怕水"行为一致。
        if (deathTime == 0 && entityName.equals("enderman") && isInFluid("water")) {
            damage(1.0f, "drown");
        }

        if (deathTime > 0) return;
        if (attackCooldown > 0) attackCooldown--;
        if (jumpCooldown > 0) jumpCooldown--;
        if (angeredTimer > 0) angeredTimer--;

        NetworkHandler nearestPlayer = null;
        double nearestDistSq = 16.0 * 16.0;

        for (NetworkHandler player : NetworkHandler.players.values()) {
            // #41 修复: 原版创造/旁观玩家也会被怪物索敌追击(只是攻击判定不同)。
            // 曾跳过 gameMode 1/3 -> 创造模式测试时生物呆立不寻路。
            if (player.isDead || player.currentDim != this.dim) continue;
            double dx = player.x - x;
            double dy = player.y - y;
            double dz = player.z - z;
            double distSq = dx * dx + dy * dy + dz * dz;
            if (distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearestPlayer = player;
            }
        }

        if ((isHostile() || (isNeutral() && angeredTimer > 0)) && nearestPlayer != null) {
            double dx = nearestPlayer.x - x;
            double dz = nearestPlayer.z - z;
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist < 0.001) dist = 0.001;

            // 苦力怕 fuse: 靠近玩家(<2.5 格)点燃引信, 1.5 秒后爆炸(原版行为)。
            // 引信期间停止攻击逻辑, 持续靠近玩家。
            if (entityName.equals("creeper")) {
                if (fuseTimer < 0 && dist < 2.5 && Math.abs(nearestPlayer.y - y) < 2.0) {
                    fuseTimer = 30;
                    EntityManager.broadcastCreeperSwell(this, 1);
                    EntityManager.broadcastSoundAt(
                        this, "minecraft:entity.creeper.primed", 1.0f, 1.0f);
                }
                if (fuseTimer >= 0) {
                    fuseTimer--;
                    double speed = 0.20;
                    vx = (dx / dist) * speed;
                    vz = (dz / dist) * speed;
                    this.yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                    this.yaw = ((this.yaw % 360) + 360) % 360;
                    tryJumpObstacle();
                    if (fuseTimer == 0) {
                        EntityManager.broadcastCreeperSwell(this, -1);
                        explode(nearestPlayer);
                        return;
                    }
                    // 原版: 引信期间玩家逃出 6 格 → 中止爆炸
                    if (dist > 6.0) {
                        fuseTimer = -1;
                        EntityManager.broadcastCreeperSwell(this, -1);
                    }
                    return;
                }
            }

            // P4-3: 远程生物(骷髅/流浪者/凋零骷髅/女巫)在中距离发射射弹
            if (isRanged() && dist > 4.0 && dist < 18.0 && attackCooldown <= 0) {
                EntityManager.broadcastEntityAttack(this);
                shootAt(nearestPlayer);
                attackCooldown = 40; // 约 2 秒冷却
                aiTimer = 5;
            } else if (dist < 1.8 && Math.abs(nearestPlayer.y - y) < 2.5 && attackCooldown <= 0) {
                EntityManager.broadcastEntityAttack(this);
                float damage = getAttackDamage();
                nearestPlayer.damagePlayer(damage, "mob", x, z);
                if (entityName.equals("creeper")) {
                    explode(nearestPlayer);
                    return;
                }
                attackCooldown = 20;
                // 原版: 怪物近战攻击不击退玩家(击退仅来自玩家侧/爆炸等)
            } else {
                double speed = switch (entityName) {
                    case "spider", "cave_spider" -> 0.21;
                    case "enderman" -> 0.30;
                    case "creeper" -> 0.15;
                    default -> 0.18;
                };
                // 远程生物(骷髅等)保持交战距离: 玩家逼近(<5)时面向玩家倒退拉开距离(原版行为)
                if (isRanged() && dist < 5.0) {
                    vx = -(dx / dist) * speed;
                    vz = -(dz / dist) * speed;
                    this.yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                    this.yaw = ((this.yaw % 360) + 360) % 360;
                    tryJumpObstacle();
                } else {
                // A* 寻路（绕障）；失败则直线 homing
                pathTimer--;
                if (pathTimer <= 0) {
                    pathStep = Pathfinder.step(this.dim, x, y, z,
                            nearestPlayer.x, nearestPlayer.y, nearestPlayer.z);
                    pathTimer = 8;
                }
                double mvx, mvz;
                int up = 0;
                if (pathStep != null) {
                    mvx = pathStep[0]; mvz = pathStep[2]; up = pathStep[1];
                } else {
                    mvx = dx / dist; mvz = dz / dist;
                }
                double mlen = Math.sqrt(mvx * mvx + mvz * mvz);
                if (mlen < 0.001) { mvx = dx / dist; mvz = dz / dist; mlen = 1.0; }
                vx = (mvx / mlen) * speed;
                vz = (mvz / mlen) * speed;
                this.yaw = (float) Math.toDegrees(Math.atan2(-mvx, mvz));
                this.yaw = ((this.yaw % 360) + 360) % 360; // #41 归一化
                if (up > 0 && onGround && jumpCooldown <= 0) {
                    vy = 0.42; onGround = false; jumpCooldown = 10;
                } else {
                    tryJumpObstacle();
                }
                aiTimer = 5;
                }
            }
        } else if (fleeTimer > 0 && nearestPlayer != null) {
            fleeTimer--;
            double dx = x - nearestPlayer.x;
            double dz = z - nearestPlayer.z;
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist < 0.001) dist = 0.001;
            vx = (dx / dist) * 0.25;
            vz = (dz / dist) * 0.25;
            // 原版: 逃跑时应面朝移动方向(远离威胁)。dx,dz 为远离玩家方向(即速度方向),
            // 与追击/矿车一致用 atan2(-mvx, mvz), 而非朝向威胁源(差 180°, 导致倒着跑, #41)。
            this.yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
            this.yaw = ((this.yaw % 360) + 360) % 360; // #41 归一化, 防负角字节错误
            tryJumpObstacle();
        } else {
            if (fleeTimer > 0) fleeTimer--;
            aiTimer--;
            if (aiTimer <= 0) {
                // #41: 被动生物更活跃地游荡(原版每 5-20 tick 随机转向走动, 曾 40-120 tick 且 3/4 概率静止)。
                aiTimer = 10 + aiRng.nextInt(30);
                if (aiRng.nextInt(3) != 0) {
                    float wanderYaw = aiRng.nextFloat() * 360;
                    this.yaw = wanderYaw;
                    double speed = isPassive() ? 0.10 : 0.12;
                    vx = -Math.sin(Math.toRadians(wanderYaw)) * speed;
                    vz = Math.cos(Math.toRadians(wanderYaw)) * speed;
                } else {
                    vx = 0; vz = 0;
                }
            }
            if (vx != 0 || vz != 0) tryJumpObstacle();
        }

        if (burnsInDaylight() && dim == DimensionType.OVERWORLD) {
            long t = Main.dayTime;
            boolean day = t < 12000 || t > 23500;
            if (day && EntityManager.hasSkyAccess(this)) {
                fireTicks = Math.max(fireTicks, 100);
            }
        }

        // BUG8: 着火状态翻转时, 立即广播给客户端, 使燃烧中的怪物真正显示火焰。
        boolean onFire = fireTicks > 0;
        if (onFire != lastFireBroadcast) {
            lastFireBroadcast = onFire;
            EntityManager.broadcastEntityFire(this, onFire);
        }

        if (health <= 0 && deathTime == 0) {
            deathTime = 1;
            onDeath();
        }

        // ── 繁殖 / 幼体成长 (被动可繁殖生物) ──
        if (baby) {
            babyAge++;
            if (babyAge >= BABY_MATURE) {
                baby = false;
                babyAge = 0;
                applyAdultSize();
                EntityManager.broadcastBaby(this);
            }
        } else if (isBreedable()) {
            if (breedCooldown > 0) breedCooldown--;
            if (loveTimer > 0) {
                loveTimer--;
                if (loveTimer > 0 && breedCooldown <= 0) {
                    // 寻找附近同种且同样处于发情期、不在冷却的伴侣
                    for (Entity e
                            : EntityManager.getEntities().values()) {
                        if (!(e instanceof MobEntity o) || o == this) continue;
                        if (!o.entityName.equals(this.entityName) || o.baby) continue;
                        if (o.loveTimer <= 0 || o.breedCooldown > 0) continue;
                        double dx = o.x - x, dy = o.y - y, dz = o.z - z;
                        if (dx * dx + dy * dy + dz * dz > 4.0) continue; // 2 格内配对
                        EntityManager.spawnBaby(this, o);
                        this.breedCooldown = BREED_CD;
                        o.breedCooldown = BREED_CD;
                        this.loveTimer = 0;
                        o.loveTimer = 0;
                        break;
                    }
                }
            }
        }
    }

    private boolean isRanged() {
        return switch (entityName) {
            case "skeleton", "stray", "wither_skeleton", "witch" -> true;
            default -> false;
        };
    }

    /** P4-3: 朝目标发射射弹(骷髅射箭 / 女巫扔药水)。 */
    private void shootAt(NetworkHandler target) {
        double tx = target.x;
        double ty = target.y + 1.0;
        double tz = target.z;
        double dx = tx - x;
        double dy = ty - (y + 1.5);
        double dz = tz - z;
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 0.001) return;
        double speed = "witch".equals(entityName) ? 0.5 : 0.9;
        double vx = dx / len * speed;
        double vy = dy / len * speed + 0.12; // 略上抛补偿重力
        double vz = dz / len * speed;
        if ("witch".equals(entityName)) {
            WitchPotionEntity potion = new WitchPotionEntity(
                EntityManager.allocateId(), x, y + 1.5, z, vx, vy, vz, target);
            potion.dim = dim;
            EntityManager.addEntity(potion);
        } else {
            ArrowEntity arrow = new ArrowEntity(
                EntityManager.allocateId(), x, y + 1.5, z, vx, vy, vz, target);
            arrow.dim = dim;
            arrow.baseDamage = getAttackDamage();
            EntityManager.addEntity(arrow);
        }
    }

    private void tryJumpObstacle() {
        if (!onGround || jumpCooldown > 0) return;
        double nx = x + Math.signum(vx) * (width / 2.0 + 0.15);
        double nz = z + Math.signum(vz) * (width / 2.0 + 0.15);
        boolean blocked = (vx != 0 && isSolidAt(nx, y + 0.2, z))
                       || (vz != 0 && isSolidAt(x, y + 0.2, nz));
        if (blocked && !isSolidAt(x, y + 1.2, z) && !isSolidAt(x, y + 2.2, z)) {
            vy = 0.42;
            onGround = false;
            jumpCooldown = 10;
        }
    }

    private void explode(NetworkHandler target) {
        health = 0;
        deathTime = 1;
        ExplosionEngine.explode(dim, x, y, z, 3.0f, true);
        onDeath();
    }

    @Override
    protected void onDeath() {
        dropLoot();
        // P4-2/P4-4: 由 lastAttacker 决定经验来源, 改为生成可拾取经验球(不再是死代码, 也不会双重发放)。
        // lastAttacker 在 NetworkHandler.attackMob / ArrowEntity 命中时写入。
        NetworkHandler killer = lastAttacker;
        if (killer != null && !killer.isDead) {
            int xp = getXpDrop();
            if (xp > 0) EntityManager.spawnExperienceOrbs(x, y + 0.5, z, xp, dim.id);
        }
    }

    private void dropLoot() {
        java.util.List<String[]> drops = new java.util.ArrayList<>();
        boolean burned = fireTicks > 0;
        int looting = lootingLevel(); // P4-6: 读取击杀者抢夺等级

        switch (entityName) {
            case "zombie", "husk", "drowned", "zombie_villager" -> drops.add(new String[]{"rotten_flesh", "0", "2"});
            case "skeleton", "stray" -> { drops.add(new String[]{"bone", "0", "2"}); drops.add(new String[]{"arrow", "0", "2"}); }
            case "wither_skeleton" -> { drops.add(new String[]{"coal", "0", "1"}); drops.add(new String[]{"bone", "0", "2"}); }
            case "creeper" -> drops.add(new String[]{"gunpowder", "0", "2"});
            case "spider", "cave_spider" -> { drops.add(new String[]{"string", "0", "2"}); if (aiRng.nextInt(3) == 0) drops.add(new String[]{"spider_eye", "1", "1"}); }
            case "enderman" -> drops.add(new String[]{"ender_pearl", "0", "1"});
            case "witch" -> {
                // 原版: pool1 = 1-3 次 rolls 从 6 种材料随机 0-2 个; pool2 = redstone 0-3
                int rolls = 1 + aiRng.nextInt(3);
                String[] mats = {"glowstone_dust", "sugar", "spider_eye", "glass_bottle", "gunpowder", "stick"};
                for (int r = 0; r < rolls; r++) {
                    drops.add(new String[]{mats[aiRng.nextInt(mats.length)], "0", "2"});
                }
                drops.add(new String[]{"redstone", "0", "3"});
            }
            case "slime" -> drops.add(new String[]{"slime_ball", "0", "2"});
            case "blaze" -> drops.add(new String[]{"blaze_rod", "0", "1"});
            case "ghast" -> { drops.add(new String[]{"ghast_tear", "0", "1"}); drops.add(new String[]{"gunpowder", "0", "2"}); drops.add(new String[]{"music_disc_tears", "1", "1"}); }
            case "magma_cube" -> drops.add(new String[]{"magma_cream", "0", "1"});
            case "phantom" -> drops.add(new String[]{"phantom_membrane", "0", "1"});
            case "pillager", "vindicator" -> drops.add(new String[]{"emerald", "0", "1"});
            case "zombified_piglin" -> { drops.add(new String[]{"rotten_flesh", "0", "1"}); drops.add(new String[]{"gold_nugget", "0", "1"}); }
            case "cow", "mooshroom" -> { drops.add(new String[]{burned ? "cooked_beef" : "beef", "1", "3"}); drops.add(new String[]{"leather", "0", "2"}); }
            case "pig" -> drops.add(new String[]{burned ? "cooked_porkchop" : "porkchop", "1", "3"});
            case "sheep" -> { drops.add(new String[]{burned ? "cooked_mutton" : "mutton", "1", "2"}); drops.add(new String[]{"white_wool", "1", "1"}); }
            case "chicken" -> { drops.add(new String[]{burned ? "cooked_chicken" : "chicken", "1", "1"}); drops.add(new String[]{"feather", "0", "2"}); }
            case "rabbit" -> { drops.add(new String[]{burned ? "cooked_rabbit" : "rabbit", "0", "1"}); drops.add(new String[]{"rabbit_hide", "0", "1"}); }
            case "squid" -> drops.add(new String[]{"ink_sac", "1", "3"});
            case "wolf" -> { }
            default -> { }
        }

        // P4-6: 僵尸稀有掉落(铁/胡萝卜/土豆), 概率随抢夺提升
        if (entityName.equals("zombie")) {
            double chance = 0.025 + 0.01 * looting;
            if (aiRng.nextDouble() < chance) {
                String[] opt = {"iron_ingot", "carrot", "potato"};
                drops.add(new String[]{opt[aiRng.nextInt(opt.length)], "1", "1"});
            }
        }

        for (String[] d : drops) {
            int min = Integer.parseInt(d[1]);
            int max = Integer.parseInt(d[2]);
            int count = min + (max > min ? aiRng.nextInt(max - min + 1) : 0);
            if (looting > 0 && max > min) count += aiRng.nextInt(looting + 1); // 抢夺增加掉落数量
            if (count <= 0) continue;
            int itemId = BlockManager.getItemIdByName(d[0]);
            if (itemId <= 0) continue;
            ItemEntity drop = new ItemEntity(EntityManager.allocateId(), x, y + 0.3, z, itemId, count);
            drop.dim = this.dim;
            EntityManager.addEntity(drop);
        }
    }

    /** P4-6: 读取击杀者(玩家)的抢夺附魔等级。 */
    private int lootingLevel() {
        if (lastAttacker == null) return 0;
        return lastAttacker.getLootingLevel();
    }

    // ── 村民交易逻辑 ─────────────────────────────────────────────────────────

    /** 单条村民报价（携带运行时状态：已用次数/需求等）。 */
    public static final class VillagerTrade {
        public final int costAId, costACount;
        public final int costBId, costBCount; // 0 表示无第二成本
        public final int resultId, resultCount;
        public final int maxUses, xp;
        public final float priceMult;
        public final int level;               // 解锁该交易所需村民等级
        public int uses = 0;
        public int specialPrice = 0;          // 本实现无声誉折扣，恒 0
        public int demand = 0;                // 需求波动（成交 +1，补货回落）
        public VillagerTrade(int costAId, int costACount, int costBId, int costBCount,
                             int resultId, int resultCount, int maxUses, int xp,
                             float priceMult, int level) {
            this.costAId = costAId; this.costACount = costACount;
            this.costBId = costBId; this.costBCount = costBCount;
            this.resultId = resultId; this.resultCount = resultCount;
            this.maxUses = maxUses; this.xp = xp; this.priceMult = priceMult; this.level = level;
        }
        public boolean available() { return uses < maxUses; }
    }

    /** 懒初始化村民（构造时若注册表未就绪，可在打开交易时补调）。 */
    public void initVillager() {
        if (!entityName.equals("villager") || villagerTrades != null) return;
        java.util.List<String> profs = VillagerTrades.PROFESSIONS;
        this.profession = profs.get(new java.util.Random().nextInt(profs.size()));
        this.villagerLevel = 1;
        this.villagerXp = 0;
        buildTrades();
    }

    private void buildTrades() {
        villagerTrades = new java.util.ArrayList<>();
        if (profession == null || !VillagerTrades.isTradeProfession(profession)) return;
        for (int lvl = 1; lvl <= 5; lvl++) {
            for (VillagerTrades.Def d : VillagerTrades.offersFor(profession, lvl)) {
                int aId = BlockManager.getItemIdByName(d.aItem);
                int rId = BlockManager.getItemIdByName(d.resultItem);
                if (aId <= 0 || rId <= 0) continue;
                int bId = 0, bCount = 0;
                if (d.bItem != null) {
                    bId = BlockManager.getItemIdByName(d.bItem);
                    bCount = d.bCount;
                    if (bId <= 0) continue;
                }
                villagerTrades.add(new VillagerTrade(aId, d.aCount, bId, bCount,
                        rId, d.resultCount, d.maxUses, d.xp, d.priceMult, lvl));
            }
        }
    }

    /** 成交后增加村民经验并处理升级。 */
    public void addVillagerXp(int amount) {
        if (villagerLevel >= 5) return; // 满级不再升级
        villagerXp += amount;
        while (villagerLevel < 5 && villagerXp >= XP_TO_NEXT[villagerLevel - 1]) {
            villagerXp -= XP_TO_NEXT[villagerLevel - 1];
            villagerLevel++;
            restockCount = 0; // 升级重置当日补货次数（原版行为）
        }
        if (villagerLevel >= 5) villagerXp = 0;
    }

    /** 返回当前已解锁（等级达标）的报价列表。 */
    public java.util.List<VillagerTrade> unlockedTrades() {
        if (villagerTrades == null) initVillager();
        if (villagerTrades == null) return java.util.Collections.emptyList();
        java.util.List<VillagerTrade> out = new java.util.ArrayList<>();
        for (VillagerTrade t : villagerTrades) if (t.level <= villagerLevel) out.add(t);
        return out;
    }

    /** 每游戏日补货（重置 uses），并让需求缓慢回落。 */
    public void villagerRestockIfDue() {
        if (!entityName.equals("villager") || villagerTrades == null) return;
        long day = Main.dayTime / 24000L;
        if (lastRestockDay == day) return;
        lastRestockDay = day;
        restockCount = 0;
        for (VillagerTrade tr : villagerTrades) {
            tr.uses = 0;
            if (tr.demand > 0) tr.demand = Math.max(0, tr.demand - 1);
        }
    }

    /** 猪灵以物易方战利品表(原版 1.21.11 barter loot, 简化概率)。 */
    private static final String[] BARTER_LOOT = {
        "ender_pearl", "ender_pearl", "ender_pearl",       // 8.4%
        "string", "string",                                  // 6.8% x2
        "leather", "leather",
        "nether_quartz", "nether_quartz", "nether_quartz", "nether_quartz",
        "obsidian", "obsidian",
        "cry_obsidian", "cry_obsidian",
        "fire_charge", "fire_charge",
        "soul_sand", "soul_sand",
        "nether_brick", "nether_brick",
        "spectral_arrow",
        "glowstone_dust", "glowstone_dust", "glowstone_dust",
        "iron_nugget", "iron_nugget",
        "gold_nugget", "gold_nugget", "gold_nugget", "gold_nugget",
        "blackstone",
        "gravel",
        "water_breathing_potion",                             // 药水
        "fire_resistance_potion",
        "netherite_hoe",                                      // 罕见
    };
    private static String rollBarterLoot() {
        java.util.Random r = new java.util.Random();
        String name = BARTER_LOOT[r.nextInt(BARTER_LOOT.length)];
        if (name.endsWith("_potion")) return name;
        return name;
    }
}

