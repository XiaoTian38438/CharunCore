/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SlotRanges
/*     */ {
/*     */   private static final List<SlotRange> SLOTS;
/*     */   
/*     */   static {
/*  23 */     SLOTS = (List<SlotRange>)Util.make(new ArrayList(), paramArrayList -> {
/*     */           addSingleSlot(paramArrayList, "contents", 0);
/*     */           addSlotRange(paramArrayList, "container.", 0, 54);
/*     */           addSlotRange(paramArrayList, "hotbar.", 0, 9);
/*     */           addSlotRange(paramArrayList, "inventory.", 9, 27);
/*     */           addSlotRange(paramArrayList, "enderchest.", 200, 27);
/*     */           addSlotRange(paramArrayList, "villager.", 300, 8);
/*     */           addSlotRange(paramArrayList, "horse.", 500, 15);
/*     */           int i = EquipmentSlot.MAINHAND.getIndex(98);
/*     */           int j = EquipmentSlot.OFFHAND.getIndex(98);
/*     */           addSingleSlot(paramArrayList, "weapon", i);
/*     */           addSingleSlot(paramArrayList, "weapon.mainhand", i);
/*     */           addSingleSlot(paramArrayList, "weapon.offhand", j);
/*     */           addSlots(paramArrayList, "weapon.*", new int[] { i, j });
/*     */           i = EquipmentSlot.HEAD.getIndex(100);
/*     */           j = EquipmentSlot.CHEST.getIndex(100);
/*     */           int k = EquipmentSlot.LEGS.getIndex(100);
/*     */           int m = EquipmentSlot.FEET.getIndex(100);
/*     */           int n = EquipmentSlot.BODY.getIndex(105);
/*     */           addSingleSlot(paramArrayList, "armor.head", i);
/*     */           addSingleSlot(paramArrayList, "armor.chest", j);
/*     */           addSingleSlot(paramArrayList, "armor.legs", k);
/*     */           addSingleSlot(paramArrayList, "armor.feet", m);
/*     */           addSingleSlot(paramArrayList, "armor.body", n);
/*     */           addSlots(paramArrayList, "armor.*", new int[] { i, j, k, m, n });
/*     */           addSingleSlot(paramArrayList, "saddle", EquipmentSlot.SADDLE.getIndex(106));
/*     */           addSingleSlot(paramArrayList, "horse.chest", 499);
/*     */           addSingleSlot(paramArrayList, "player.cursor", 499);
/*     */           addSlotRange(paramArrayList, "player.crafting.", 500, 4);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Function<String, SlotRange> NAME_LOOKUP;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final Codec<SlotRange> CODEC = StringRepresentable.fromValues(() -> (SlotRange[])SLOTS.toArray(()));
/*     */   static {
/*  66 */     NAME_LOOKUP = StringRepresentable.createNameLookup((StringRepresentable[])SLOTS.toArray(paramInt -> new SlotRange[paramInt]));
/*     */   }
/*     */   private static SlotRange create(String paramString, int paramInt) {
/*  69 */     return SlotRange.of(paramString, IntLists.singleton(paramInt));
/*     */   }
/*     */   
/*     */   private static SlotRange create(String paramString, IntList paramIntList) {
/*  73 */     return SlotRange.of(paramString, IntLists.unmodifiable(paramIntList));
/*     */   }
/*     */   
/*     */   private static SlotRange create(String paramString, int... paramVarArgs) {
/*  77 */     return SlotRange.of(paramString, IntList.of(paramVarArgs));
/*     */   }
/*     */   
/*     */   private static void addSingleSlot(List<SlotRange> paramList, String paramString, int paramInt) {
/*  81 */     paramList.add(create(paramString, paramInt));
/*     */   }
/*     */   
/*     */   private static void addSlotRange(List<SlotRange> paramList, String paramString, int paramInt1, int paramInt2) {
/*  85 */     IntArrayList intArrayList = new IntArrayList(paramInt2);
/*  86 */     for (byte b = 0; b < paramInt2; b++) {
/*  87 */       int i = paramInt1 + b;
/*  88 */       paramList.add(create(paramString + paramString, i));
/*  89 */       intArrayList.add(i);
/*     */     } 
/*  91 */     paramList.add(create(paramString + "*", (IntList)intArrayList));
/*     */   }
/*     */   
/*     */   private static void addSlots(List<SlotRange> paramList, String paramString, int... paramVarArgs) {
/*  95 */     paramList.add(create(paramString, paramVarArgs));
/*     */   }
/*     */   
/*     */   public static SlotRange nameToIds(String paramString) {
/*  99 */     return NAME_LOOKUP.apply(paramString);
/*     */   }
/*     */   
/*     */   public static Stream<String> allNames() {
/* 103 */     return SLOTS.stream().map(StringRepresentable::getSerializedName);
/*     */   }
/*     */   
/*     */   public static Stream<String> singleSlotNames() {
/* 107 */     return SLOTS.stream().filter(paramSlotRange -> (paramSlotRange.size() == 1)).map(StringRepresentable::getSerializedName);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\SlotRanges.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */