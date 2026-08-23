/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class UpgradeChunk
/*     */ {
/*     */   private int sides;
/* 485 */   private final ChunkPalettedStorageFix.Section[] sections = new ChunkPalettedStorageFix.Section[16];
/*     */   
/*     */   private final Dynamic<?> level;
/*     */   private final int x;
/*     */   private final int z;
/* 490 */   private final Int2ObjectMap<Dynamic<?>> blockEntities = (Int2ObjectMap<Dynamic<?>>)new Int2ObjectLinkedOpenHashMap(16);
/*     */   
/*     */   public UpgradeChunk(Dynamic<?> paramDynamic) {
/* 493 */     this.level = paramDynamic;
/* 494 */     this.x = paramDynamic.get("xPos").asInt(0) << 4;
/* 495 */     this.z = paramDynamic.get("zPos").asInt(0) << 4;
/*     */     
/* 497 */     paramDynamic.get("TileEntities").asStreamOpt().ifSuccess(paramStream -> paramStream.forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 510 */     boolean bool = paramDynamic.get("convertedFromAlphaFormat").asBoolean(false);
/*     */     
/* 512 */     paramDynamic.get("Sections").asStreamOpt().ifSuccess(paramStream -> paramStream.forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 520 */     for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 521 */       if (section != null)
/*     */       {
/*     */ 
/*     */         
/* 525 */         for (ObjectIterator<Int2ObjectMap.Entry> objectIterator = section.toFix.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { IntListIterator<Integer> intListIterator; Int2ObjectMap.Entry entry = objectIterator.next();
/* 526 */           int i = section.y << 12;
/* 527 */           switch (entry.getIntKey()) {
/*     */             case 2:
/* 529 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 530 */                 j |= i;
/*     */                 
/* 532 */                 Dynamic<?> dynamic = getBlock(j);
/* 533 */                 if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(dynamic))) {
/* 534 */                   String str = ChunkPalettedStorageFix.getName(getBlock(relative(j, ChunkPalettedStorageFix.Direction.UP)));
/* 535 */                   if ("minecraft:snow".equals(str) || "minecraft:snow_layer".equals(str)) {
/* 536 */                     setBlock(j, ChunkPalettedStorageFix.MappingConstants.SNOWY_GRASS);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 3:
/* 543 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 544 */                 j |= i;
/*     */                 
/* 546 */                 Dynamic<?> dynamic = getBlock(j);
/* 547 */                 if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(dynamic))) {
/* 548 */                   String str = ChunkPalettedStorageFix.getName(getBlock(relative(j, ChunkPalettedStorageFix.Direction.UP)));
/* 549 */                   if ("minecraft:snow".equals(str) || "minecraft:snow_layer".equals(str)) {
/* 550 */                     setBlock(j, ChunkPalettedStorageFix.MappingConstants.SNOWY_PODZOL);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 110:
/* 557 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 558 */                 j |= i;
/*     */                 
/* 560 */                 Dynamic<?> dynamic = getBlock(j);
/* 561 */                 if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(dynamic))) {
/* 562 */                   String str = ChunkPalettedStorageFix.getName(getBlock(relative(j, ChunkPalettedStorageFix.Direction.UP)));
/* 563 */                   if ("minecraft:snow".equals(str) || "minecraft:snow_layer".equals(str)) {
/* 564 */                     setBlock(j, ChunkPalettedStorageFix.MappingConstants.SNOWY_MYCELIUM);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 25:
/* 571 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 572 */                 j |= i;
/* 573 */                 Dynamic<?> dynamic = removeBlockEntity(j);
/* 574 */                 if (dynamic != null) {
/* 575 */                   String str = Boolean.toString(dynamic.get("powered").asBoolean(false)) + Boolean.toString(dynamic.get("powered").asBoolean(false));
/* 576 */                   setBlock(j, ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.getOrDefault(str, ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.get("false0")));
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 26:
/* 582 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 583 */                 j |= i;
/* 584 */                 Dynamic<?> dynamic1 = getBlockEntity(j);
/* 585 */                 Dynamic<?> dynamic2 = getBlock(j);
/* 586 */                 if (dynamic1 != null) {
/* 587 */                   int k = dynamic1.get("color").asInt(0);
/* 588 */                   if (k != 14 && k >= 0 && k < 16) {
/* 589 */                     String str = ChunkPalettedStorageFix.getProperty(dynamic2, "facing") + ChunkPalettedStorageFix.getProperty(dynamic2, "facing") + ChunkPalettedStorageFix.getProperty(dynamic2, "occupied") + ChunkPalettedStorageFix.getProperty(dynamic2, "part");
/* 590 */                     if (ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.containsKey(str)) {
/* 591 */                       setBlock(j, ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.get(str));
/*     */                     }
/*     */                   } 
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 176:
/*     */             case 177:
/* 600 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 601 */                 j |= i;
/* 602 */                 Dynamic<?> dynamic1 = getBlockEntity(j);
/* 603 */                 Dynamic<?> dynamic2 = getBlock(j);
/* 604 */                 if (dynamic1 != null) {
/* 605 */                   int k = dynamic1.get("Base").asInt(0);
/* 606 */                   if (k != 15 && k >= 0 && k < 16) {
/* 607 */                     String str = ChunkPalettedStorageFix.getProperty(dynamic2, (entry.getIntKey() == 176) ? "rotation" : "facing") + "_" + ChunkPalettedStorageFix.getProperty(dynamic2, (entry.getIntKey() == 176) ? "rotation" : "facing");
/* 608 */                     if (ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.containsKey(str)) {
/* 609 */                       setBlock(j, ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.get(str));
/*     */                     }
/*     */                   } 
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 86:
/* 617 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 618 */                 j |= i;
/*     */                 
/* 620 */                 Dynamic<?> dynamic = getBlock(j);
/* 621 */                 if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(dynamic))) {
/* 622 */                   String str = ChunkPalettedStorageFix.getName(getBlock(relative(j, ChunkPalettedStorageFix.Direction.DOWN)));
/* 623 */                   if ("minecraft:grass_block".equals(str) || "minecraft:dirt".equals(str)) {
/* 624 */                     setBlock(j, ChunkPalettedStorageFix.MappingConstants.PUMPKIN);
/*     */                   }
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 140:
/* 631 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 632 */                 j |= i;
/* 633 */                 Dynamic<?> dynamic = removeBlockEntity(j);
/* 634 */                 if (dynamic != null) {
/* 635 */                   String str = dynamic.get("Item").asString("") + dynamic.get("Item").asString("");
/* 636 */                   setBlock(j, ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.getOrDefault(str, ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.get("minecraft:air0")));
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 144:
/* 642 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 643 */                 j |= i;
/* 644 */                 Dynamic<?> dynamic = getBlockEntity(j);
/* 645 */                 if (dynamic != null) {
/* 646 */                   String str3, str1 = String.valueOf(dynamic.get("SkullType").asInt(0));
/* 647 */                   String str2 = ChunkPalettedStorageFix.getProperty(getBlock(j), "facing");
/*     */                   
/* 649 */                   if ("up".equals(str2) || "down".equals(str2)) {
/* 650 */                     str3 = str1 + str1;
/*     */                   } else {
/* 652 */                     str3 = str1 + str1;
/*     */                   } 
/*     */                   
/* 655 */                   dynamic.remove("SkullType");
/* 656 */                   dynamic.remove("facing");
/* 657 */                   dynamic.remove("Rot");
/*     */                   
/* 659 */                   setBlock(j, ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.getOrDefault(str3, ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.get("0north")));
/*     */                 }  }
/*     */             
/*     */             
/*     */             case 64:
/*     */             case 71:
/*     */             case 193:
/*     */             case 194:
/*     */             case 195:
/*     */             case 196:
/*     */             case 197:
/* 670 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 671 */                 j |= i;
/*     */                 
/* 673 */                 Dynamic<?> dynamic = getBlock(j);
/* 674 */                 if (ChunkPalettedStorageFix.getName(dynamic).endsWith("_door")) {
/* 675 */                   Dynamic<?> dynamic1 = getBlock(j);
/* 676 */                   if ("lower".equals(ChunkPalettedStorageFix.getProperty(dynamic1, "half"))) {
/* 677 */                     int k = relative(j, ChunkPalettedStorageFix.Direction.UP);
/* 678 */                     Dynamic<?> dynamic2 = getBlock(k);
/* 679 */                     String str = ChunkPalettedStorageFix.getName(dynamic1);
/* 680 */                     if (str.equals(ChunkPalettedStorageFix.getName(dynamic2))) {
/* 681 */                       String str1 = ChunkPalettedStorageFix.getProperty(dynamic1, "facing");
/* 682 */                       String str2 = ChunkPalettedStorageFix.getProperty(dynamic1, "open");
/* 683 */                       String str3 = bool ? "left" : ChunkPalettedStorageFix.getProperty(dynamic2, "hinge");
/* 684 */                       String str4 = bool ? "false" : ChunkPalettedStorageFix.getProperty(dynamic2, "powered");
/* 685 */                       setBlock(j, ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(str + str + "lower" + str1 + str3 + str2));
/* 686 */                       setBlock(k, ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(str + str + "upper" + str1 + str3 + str2));
/*     */                     } 
/*     */                   } 
/*     */                 }  }
/*     */             
/*     */ 
/*     */             
/*     */             case 175:
/* 694 */               for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int j = ((Integer)intListIterator.next()).intValue();
/* 695 */                 j |= i;
/*     */                 
/* 697 */                 Dynamic<?> dynamic = getBlock(j);
/* 698 */                 if ("upper".equals(ChunkPalettedStorageFix.getProperty(dynamic, "half"))) {
/* 699 */                   Dynamic<?> dynamic1 = getBlock(relative(j, ChunkPalettedStorageFix.Direction.DOWN));
/* 700 */                   String str = ChunkPalettedStorageFix.getName(dynamic1);
/* 701 */                   switch (str) { case "minecraft:sunflower":
/* 702 */                       setBlock(j, ChunkPalettedStorageFix.MappingConstants.UPPER_SUNFLOWER);
/* 703 */                     case "minecraft:lilac": setBlock(j, ChunkPalettedStorageFix.MappingConstants.UPPER_LILAC);
/* 704 */                     case "minecraft:tall_grass": setBlock(j, ChunkPalettedStorageFix.MappingConstants.UPPER_TALL_GRASS);
/* 705 */                     case "minecraft:large_fern": setBlock(j, ChunkPalettedStorageFix.MappingConstants.UPPER_LARGE_FERN);
/* 706 */                     case "minecraft:rose_bush": setBlock(j, ChunkPalettedStorageFix.MappingConstants.UPPER_ROSE_BUSH);
/* 707 */                     case "minecraft:peony": setBlock(j, ChunkPalettedStorageFix.MappingConstants.UPPER_PEONY); }
/*     */                 
/*     */                 }  }
/*     */             
/*     */           }  }
/*     */       
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Dynamic<?> getBlockEntity(int paramInt) {
/* 719 */     return (Dynamic)this.blockEntities.get(paramInt);
/*     */   }
/*     */   
/*     */   private Dynamic<?> removeBlockEntity(int paramInt) {
/* 723 */     return (Dynamic)this.blockEntities.remove(paramInt);
/*     */   }
/*     */   
/*     */   public static int relative(int paramInt, ChunkPalettedStorageFix.Direction paramDirection) {
/* 727 */     switch (paramDirection.getAxis().ordinal()) { default: throw new MatchException(null, null);
/*     */       case 0:
/* 729 */         i = (paramInt & 0xF) + paramDirection.getAxisDirection().getStep();
/* 730 */         return (i < 0 || i > 15) ? -1 : (paramInt & 0xFFFFFFF0 | i);
/*     */       
/*     */       case 1:
/* 733 */         i = (paramInt >> 8) + paramDirection.getAxisDirection().getStep();
/* 734 */         return (i < 0 || i > 255) ? -1 : (paramInt & 0xFF | i << 8);
/*     */       case 2:
/*     */         break; }
/* 737 */      int i = (paramInt >> 4 & 0xF) + paramDirection.getAxisDirection().getStep();
/* 738 */     return (i < 0 || i > 15) ? -1 : (paramInt & 0xFFFFFF0F | i << 4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setBlock(int paramInt, Dynamic<?> paramDynamic) {
/* 744 */     if (paramInt < 0 || paramInt > 65535) {
/*     */       return;
/*     */     }
/*     */     
/* 748 */     ChunkPalettedStorageFix.Section section = getSection(paramInt);
/*     */     
/* 750 */     if (section == null) {
/*     */       return;
/*     */     }
/*     */     
/* 754 */     section.setBlock(paramInt & 0xFFF, paramDynamic);
/*     */   }
/*     */   
/*     */   private ChunkPalettedStorageFix.Section getSection(int paramInt) {
/* 758 */     int i = paramInt >> 12;
/* 759 */     return (i < this.sections.length) ? this.sections[i] : null;
/*     */   }
/*     */   
/*     */   public Dynamic<?> getBlock(int paramInt) {
/* 763 */     if (paramInt < 0 || paramInt > 65535) {
/* 764 */       return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */     }
/*     */     
/* 767 */     ChunkPalettedStorageFix.Section section = getSection(paramInt);
/*     */     
/* 769 */     if (section == null) {
/* 770 */       return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */     }
/*     */     
/* 773 */     return section.getBlock(paramInt & 0xFFF);
/*     */   }
/*     */   
/*     */   public Dynamic<?> write() {
/* 777 */     Dynamic<?> dynamic = this.level;
/* 778 */     if (this.blockEntities.isEmpty()) {
/* 779 */       dynamic = dynamic.remove("TileEntities");
/*     */     } else {
/* 781 */       dynamic = dynamic.set("TileEntities", dynamic.createList(this.blockEntities.values().stream()));
/*     */     } 
/*     */     
/* 784 */     Dynamic dynamic1 = dynamic.emptyMap();
/* 785 */     ArrayList<Dynamic<?>> arrayList = Lists.newArrayList();
/* 786 */     for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 787 */       if (section != null) {
/* 788 */         arrayList.add(section.write());
/* 789 */         dynamic1 = dynamic1.set(String.valueOf(section.y), dynamic1.createIntList(Arrays.stream(section.update.toIntArray())));
/*     */       } 
/*     */     } 
/*     */     
/* 793 */     Dynamic dynamic2 = dynamic.emptyMap();
/* 794 */     dynamic2 = dynamic2.set("Sides", dynamic2.createByte((byte)this.sides));
/* 795 */     dynamic2 = dynamic2.set("Indices", dynamic1);
/* 796 */     return dynamic.set("UpgradeData", dynamic2).set("Sections", dynamic2.createList(arrayList.stream()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$UpgradeChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */