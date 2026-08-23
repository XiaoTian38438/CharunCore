/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class EnchantingTableBlockEntity
/*     */   extends BlockEntity implements Nameable {
/*  20 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.enchant");
/*     */   
/*     */   public int time;
/*     */   public float flip;
/*     */   public float oFlip;
/*     */   public float flipT;
/*     */   public float flipA;
/*     */   public float open;
/*     */   public float oOpen;
/*     */   public float rot;
/*     */   public float oRot;
/*     */   public float tRot;
/*  32 */   private static final RandomSource RANDOM = RandomSource.create();
/*     */   private Component name;
/*     */   
/*     */   public EnchantingTableBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  36 */     super(BlockEntityType.ENCHANTING_TABLE, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  41 */     super.saveAdditional(paramValueOutput);
/*  42 */     paramValueOutput.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  47 */     super.loadAdditional(paramValueInput);
/*  48 */     this.name = parseCustomNameSafe(paramValueInput, "CustomName");
/*     */   }
/*     */   
/*     */   public static void bookAnimationTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, EnchantingTableBlockEntity paramEnchantingTableBlockEntity) {
/*  52 */     paramEnchantingTableBlockEntity.oOpen = paramEnchantingTableBlockEntity.open;
/*  53 */     paramEnchantingTableBlockEntity.oRot = paramEnchantingTableBlockEntity.rot;
/*     */     
/*  55 */     Player player = paramLevel.getNearestPlayer(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, 3.0D, false);
/*  56 */     if (player != null) {
/*  57 */       double d1 = player.getX() - paramBlockPos.getX() + 0.5D;
/*  58 */       double d2 = player.getZ() - paramBlockPos.getZ() + 0.5D;
/*     */       
/*  60 */       paramEnchantingTableBlockEntity.tRot = (float)Mth.atan2(d2, d1);
/*     */       
/*  62 */       paramEnchantingTableBlockEntity.open += 0.1F;
/*     */       
/*  64 */       if (paramEnchantingTableBlockEntity.open < 0.5F || RANDOM.nextInt(40) == 0) {
/*  65 */         float f = paramEnchantingTableBlockEntity.flipT;
/*     */         do {
/*  67 */           paramEnchantingTableBlockEntity.flipT += (RANDOM.nextInt(4) - RANDOM.nextInt(4));
/*  68 */         } while (f == paramEnchantingTableBlockEntity.flipT);
/*     */       } 
/*     */     } else {
/*  71 */       paramEnchantingTableBlockEntity.tRot += 0.02F;
/*  72 */       paramEnchantingTableBlockEntity.open -= 0.1F;
/*     */     } 
/*     */     
/*  75 */     while (paramEnchantingTableBlockEntity.rot >= 3.1415927F) {
/*  76 */       paramEnchantingTableBlockEntity.rot -= 6.2831855F;
/*     */     }
/*  78 */     while (paramEnchantingTableBlockEntity.rot < -3.1415927F) {
/*  79 */       paramEnchantingTableBlockEntity.rot += 6.2831855F;
/*     */     }
/*  81 */     while (paramEnchantingTableBlockEntity.tRot >= 3.1415927F) {
/*  82 */       paramEnchantingTableBlockEntity.tRot -= 6.2831855F;
/*     */     }
/*  84 */     while (paramEnchantingTableBlockEntity.tRot < -3.1415927F) {
/*  85 */       paramEnchantingTableBlockEntity.tRot += 6.2831855F;
/*     */     }
/*  87 */     float f1 = paramEnchantingTableBlockEntity.tRot - paramEnchantingTableBlockEntity.rot;
/*  88 */     while (f1 >= 3.1415927F) {
/*  89 */       f1 -= 6.2831855F;
/*     */     }
/*  91 */     while (f1 < -3.1415927F) {
/*  92 */       f1 += 6.2831855F;
/*     */     }
/*     */     
/*  95 */     paramEnchantingTableBlockEntity.rot += f1 * 0.4F;
/*     */     
/*  97 */     paramEnchantingTableBlockEntity.open = Mth.clamp(paramEnchantingTableBlockEntity.open, 0.0F, 1.0F);
/*     */     
/*  99 */     paramEnchantingTableBlockEntity.time++;
/* 100 */     paramEnchantingTableBlockEntity.oFlip = paramEnchantingTableBlockEntity.flip;
/*     */     
/* 102 */     float f2 = (paramEnchantingTableBlockEntity.flipT - paramEnchantingTableBlockEntity.flip) * 0.4F;
/* 103 */     float f3 = 0.2F;
/* 104 */     f2 = Mth.clamp(f2, -0.2F, 0.2F);
/* 105 */     paramEnchantingTableBlockEntity.flipA += (f2 - paramEnchantingTableBlockEntity.flipA) * 0.9F;
/*     */     
/* 107 */     paramEnchantingTableBlockEntity.flip += paramEnchantingTableBlockEntity.flipA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 112 */     if (this.name != null) {
/* 113 */       return this.name;
/*     */     }
/* 115 */     return DEFAULT_NAME;
/*     */   }
/*     */   
/*     */   public void setCustomName(Component paramComponent) {
/* 119 */     this.name = paramComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getCustomName() {
/* 124 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 129 */     super.applyImplicitComponents(paramDataComponentGetter);
/* 130 */     this.name = (Component)paramDataComponentGetter.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 135 */     super.collectImplicitComponents(paramBuilder);
/* 136 */     paramBuilder.set(DataComponents.CUSTOM_NAME, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 141 */     paramValueOutput.discard("CustomName");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\EnchantingTableBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */