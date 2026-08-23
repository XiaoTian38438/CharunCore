/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class ProjectileWeaponItem extends Item {
/*     */   public static final Predicate<ItemStack> ARROW_ONLY;
/*     */   public static final Predicate<ItemStack> ARROW_OR_FIREWORK;
/*     */   
/*     */   static {
/*  21 */     ARROW_ONLY = (paramItemStack -> paramItemStack.is(ItemTags.ARROWS));
/*  22 */     ARROW_OR_FIREWORK = ARROW_ONLY.or(paramItemStack -> paramItemStack.is(Items.FIREWORK_ROCKET));
/*     */   }
/*     */   public ProjectileWeaponItem(Item.Properties paramProperties) {
/*  25 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> getSupportedHeldProjectiles() {
/*  30 */     return getAllSupportedProjectiles();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack getHeldProjectile(LivingEntity paramLivingEntity, Predicate<ItemStack> paramPredicate) {
/*  37 */     if (paramPredicate.test(paramLivingEntity.getItemInHand(InteractionHand.OFF_HAND))) {
/*  38 */       return paramLivingEntity.getItemInHand(InteractionHand.OFF_HAND);
/*     */     }
/*  40 */     if (paramPredicate.test(paramLivingEntity.getItemInHand(InteractionHand.MAIN_HAND))) {
/*  41 */       return paramLivingEntity.getItemInHand(InteractionHand.MAIN_HAND);
/*     */     }
/*  43 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shoot(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, InteractionHand paramInteractionHand, ItemStack paramItemStack, List<ItemStack> paramList, float paramFloat1, float paramFloat2, boolean paramBoolean, LivingEntity paramLivingEntity2) {
/*  49 */     float f1 = EnchantmentHelper.processProjectileSpread(paramServerLevel, paramItemStack, (Entity)paramLivingEntity1, 0.0F);
/*  50 */     float f2 = (paramList.size() == 1) ? 0.0F : (2.0F * f1 / (paramList.size() - 1));
/*  51 */     float f3 = ((paramList.size() - 1) % 2) * f2 / 2.0F;
/*  52 */     float f4 = 1.0F;
/*  53 */     for (byte b = 0; b < paramList.size(); b++) {
/*  54 */       ItemStack itemStack = paramList.get(b);
/*     */       
/*  56 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/*  60 */         float f = f3 + f4 * ((b + 1) / 2) * f2;
/*  61 */         f4 = -f4;
/*     */         
/*  63 */         byte b1 = b;
/*     */         
/*  65 */         Projectile.spawnProjectile(
/*  66 */             createProjectile((Level)paramServerLevel, paramLivingEntity1, paramItemStack, itemStack, paramBoolean), paramServerLevel, itemStack, paramProjectile -> shootProjectile(paramLivingEntity1, paramProjectile, paramInt, paramFloat1, paramFloat2, paramFloat3, paramLivingEntity2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         paramItemStack.hurtAndBreak(getDurabilityUse(itemStack), paramLivingEntity1, paramInteractionHand.asEquipmentSlot());
/*  72 */         if (paramItemStack.isEmpty())
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getDurabilityUse(ItemStack paramItemStack) {
/*  79 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Projectile createProjectile(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack1, ItemStack paramItemStack2, boolean paramBoolean) {
/*  85 */     Item item = paramItemStack2.getItem(); ArrowItem arrowItem2 = (ArrowItem)item, arrowItem1 = (item instanceof ArrowItem) ? arrowItem2 : (ArrowItem)Items.ARROW;
/*  86 */     AbstractArrow abstractArrow = arrowItem1.createArrow(paramLevel, paramItemStack2, paramLivingEntity, paramItemStack1);
/*  87 */     if (paramBoolean) {
/*  88 */       abstractArrow.setCritArrow(true);
/*     */     }
/*  90 */     return (Projectile)abstractArrow;
/*     */   }
/*     */   
/*     */   protected static List<ItemStack> draw(ItemStack paramItemStack1, ItemStack paramItemStack2, LivingEntity paramLivingEntity) {
/*  94 */     if (paramItemStack2.isEmpty()) {
/*  95 */       return List.of();
/*     */     }
/*     */ 
/*     */     
/*  99 */     Level level = paramLivingEntity.level(); ServerLevel serverLevel = (ServerLevel)level; byte b1 = (level instanceof ServerLevel) ? EnchantmentHelper.processProjectileCount(serverLevel, paramItemStack1, (Entity)paramLivingEntity, 1) : 1;
/* 100 */     ArrayList<ItemStack> arrayList = new ArrayList(b1);
/*     */     
/* 102 */     ItemStack itemStack = paramItemStack2.copy();
/* 103 */     for (byte b2 = 0; b2 < b1; b2++) {
/* 104 */       ItemStack itemStack1 = useAmmo(paramItemStack1, (b2 == 0) ? paramItemStack2 : itemStack, paramLivingEntity, (b2 > 0));
/* 105 */       if (!itemStack1.isEmpty()) {
/* 106 */         arrayList.add(itemStack1);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     return arrayList;
/*     */   }
/*     */   
/*     */   protected static ItemStack useAmmo(ItemStack paramItemStack1, ItemStack paramItemStack2, LivingEntity paramLivingEntity, boolean paramBoolean) {
/*     */     // Byte code:
/*     */     //   0: iload_3
/*     */     //   1: ifne -> 43
/*     */     //   4: aload_2
/*     */     //   5: invokevirtual hasInfiniteMaterials : ()Z
/*     */     //   8: ifne -> 43
/*     */     //   11: aload_2
/*     */     //   12: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   15: astore #6
/*     */     //   17: aload #6
/*     */     //   19: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   22: ifeq -> 43
/*     */     //   25: aload #6
/*     */     //   27: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   30: astore #5
/*     */     //   32: aload #5
/*     */     //   34: aload_0
/*     */     //   35: aload_1
/*     */     //   36: iconst_1
/*     */     //   37: invokestatic processAmmoUse : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I
/*     */     //   40: goto -> 44
/*     */     //   43: iconst_0
/*     */     //   44: istore #4
/*     */     //   46: iload #4
/*     */     //   48: aload_1
/*     */     //   49: invokevirtual getCount : ()I
/*     */     //   52: if_icmple -> 59
/*     */     //   55: getstatic net/minecraft/world/item/ItemStack.EMPTY : Lnet/minecraft/world/item/ItemStack;
/*     */     //   58: areturn
/*     */     //   59: iload #4
/*     */     //   61: ifne -> 86
/*     */     //   64: aload_1
/*     */     //   65: iconst_1
/*     */     //   66: invokevirtual copyWithCount : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   69: astore #5
/*     */     //   71: aload #5
/*     */     //   73: getstatic net/minecraft/core/component/DataComponents.INTANGIBLE_PROJECTILE : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   76: getstatic net/minecraft/util/Unit.INSTANCE : Lnet/minecraft/util/Unit;
/*     */     //   79: invokevirtual set : (Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   82: pop
/*     */     //   83: aload #5
/*     */     //   85: areturn
/*     */     //   86: aload_1
/*     */     //   87: iload #4
/*     */     //   89: invokevirtual split : (I)Lnet/minecraft/world/item/ItemStack;
/*     */     //   92: astore #5
/*     */     //   94: aload_1
/*     */     //   95: invokevirtual isEmpty : ()Z
/*     */     //   98: ifeq -> 123
/*     */     //   101: aload_2
/*     */     //   102: instanceof net/minecraft/world/entity/player/Player
/*     */     //   105: ifeq -> 123
/*     */     //   108: aload_2
/*     */     //   109: checkcast net/minecraft/world/entity/player/Player
/*     */     //   112: astore #6
/*     */     //   114: aload #6
/*     */     //   116: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   119: aload_1
/*     */     //   120: invokevirtual removeItem : (Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   123: aload #5
/*     */     //   125: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     //   #116	-> 46
/*     */     //   #117	-> 55
/*     */     //   #119	-> 59
/*     */     //   #120	-> 64
/*     */     //   #121	-> 71
/*     */     //   #122	-> 83
/*     */     //   #124	-> 86
/*     */     //   #125	-> 94
/*     */     //   #127	-> 114
/*     */     //   #129	-> 123
/*     */   }
/*     */   
/*     */   public abstract Predicate<ItemStack> getAllSupportedProjectiles();
/*     */   
/*     */   public abstract int getDefaultProjectileRange();
/*     */   
/*     */   protected abstract void shootProjectile(LivingEntity paramLivingEntity1, Projectile paramProjectile, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, LivingEntity paramLivingEntity2);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ProjectileWeaponItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */