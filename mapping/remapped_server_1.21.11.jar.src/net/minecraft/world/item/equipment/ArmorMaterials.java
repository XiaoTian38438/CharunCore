/*     */ package net.minecraft.world.item.equipment;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ 
/*     */ public interface ArmorMaterials
/*     */ {
/*     */   private static Map<ArmorType, Integer> makeDefense(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/*  11 */     return Maps.newEnumMap(Map.of(ArmorType.BOOTS, 
/*  12 */           Integer.valueOf(paramInt1), ArmorType.LEGGINGS, 
/*  13 */           Integer.valueOf(paramInt2), ArmorType.CHESTPLATE, 
/*  14 */           Integer.valueOf(paramInt3), ArmorType.HELMET, 
/*  15 */           Integer.valueOf(paramInt4), ArmorType.BODY, 
/*  16 */           Integer.valueOf(paramInt5)));
/*     */   }
/*     */ 
/*     */   
/*  20 */   public static final ArmorMaterial LEATHER = new ArmorMaterial(5, 
/*     */       
/*  22 */       makeDefense(1, 2, 3, 1, 3), 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ItemTags.REPAIRS_LEATHER_ARMOR, EquipmentAssets.LEATHER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static final ArmorMaterial COPPER = new ArmorMaterial(11, 
/*     */       
/*  32 */       makeDefense(1, 3, 4, 2, 4), 8, SoundEvents.ARMOR_EQUIP_COPPER, 0.0F, 0.0F, ItemTags.REPAIRS_COPPER_ARMOR, EquipmentAssets.COPPER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static final ArmorMaterial CHAINMAIL = new ArmorMaterial(15, 
/*     */       
/*  42 */       makeDefense(1, 4, 5, 2, 4), 12, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, ItemTags.REPAIRS_CHAIN_ARMOR, EquipmentAssets.CHAINMAIL);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final ArmorMaterial IRON = new ArmorMaterial(15, 
/*     */       
/*  52 */       makeDefense(2, 5, 6, 2, 5), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, EquipmentAssets.IRON);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final ArmorMaterial GOLD = new ArmorMaterial(7, 
/*     */       
/*  62 */       makeDefense(1, 3, 5, 2, 7), 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, ItemTags.REPAIRS_GOLD_ARMOR, EquipmentAssets.GOLD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final ArmorMaterial DIAMOND = new ArmorMaterial(33, 
/*     */       
/*  72 */       makeDefense(3, 6, 8, 3, 11), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, ItemTags.REPAIRS_DIAMOND_ARMOR, EquipmentAssets.DIAMOND);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final ArmorMaterial TURTLE_SCUTE = new ArmorMaterial(25, 
/*     */       
/*  83 */       makeDefense(2, 5, 6, 2, 5), 9, SoundEvents.ARMOR_EQUIP_TURTLE, 0.0F, 0.0F, ItemTags.REPAIRS_TURTLE_HELMET, EquipmentAssets.TURTLE_SCUTE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final ArmorMaterial NETHERITE = new ArmorMaterial(37, 
/*     */       
/*  93 */       makeDefense(3, 6, 8, 3, 19), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_NETHERITE_ARMOR, EquipmentAssets.NETHERITE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static final ArmorMaterial ARMADILLO_SCUTE = new ArmorMaterial(4, 
/*     */       
/* 103 */       makeDefense(3, 6, 8, 3, 11), 10, SoundEvents.ARMOR_EQUIP_WOLF, 0.0F, 0.0F, ItemTags.REPAIRS_WOLF_ARMOR, EquipmentAssets.ARMADILLO_SCUTE);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\equipment\ArmorMaterials.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */