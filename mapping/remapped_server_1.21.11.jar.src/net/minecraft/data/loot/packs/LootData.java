/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public interface LootData
/*    */ {
/* 11 */   public static final Map<DyeColor, ItemLike> WOOL_ITEM_BY_DYE = Maps.newEnumMap(Map.ofEntries((Map.Entry<?, ?>[])new Map.Entry[] { 
/* 12 */           Map.entry(DyeColor.WHITE, Blocks.WHITE_WOOL), 
/* 13 */           Map.entry(DyeColor.ORANGE, Blocks.ORANGE_WOOL), 
/* 14 */           Map.entry(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL), 
/* 15 */           Map.entry(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL), 
/* 16 */           Map.entry(DyeColor.YELLOW, Blocks.YELLOW_WOOL), 
/* 17 */           Map.entry(DyeColor.LIME, Blocks.LIME_WOOL), 
/* 18 */           Map.entry(DyeColor.PINK, Blocks.PINK_WOOL), 
/* 19 */           Map.entry(DyeColor.GRAY, Blocks.GRAY_WOOL), 
/* 20 */           Map.entry(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL), 
/* 21 */           Map.entry(DyeColor.CYAN, Blocks.CYAN_WOOL), 
/* 22 */           Map.entry(DyeColor.PURPLE, Blocks.PURPLE_WOOL), 
/* 23 */           Map.entry(DyeColor.BLUE, Blocks.BLUE_WOOL), 
/* 24 */           Map.entry(DyeColor.BROWN, Blocks.BROWN_WOOL), 
/* 25 */           Map.entry(DyeColor.GREEN, Blocks.GREEN_WOOL), 
/* 26 */           Map.entry(DyeColor.RED, Blocks.RED_WOOL), 
/* 27 */           Map.entry(DyeColor.BLACK, Blocks.BLACK_WOOL) }));
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\loot\packs\LootData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */