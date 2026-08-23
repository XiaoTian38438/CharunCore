/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class EndCityPieces {
/*     */   static EndCityPiece addPiece(StructureTemplateManager paramStructureTemplateManager, EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, String paramString, Rotation paramRotation, boolean paramBoolean) {
/*  36 */     EndCityPiece endCityPiece = new EndCityPiece(paramStructureTemplateManager, paramString, paramEndCityPiece.templatePosition(), paramRotation, paramBoolean);
/*  37 */     BlockPos blockPos = paramEndCityPiece.template().calculateConnectedPosition(paramEndCityPiece.placeSettings(), paramBlockPos, endCityPiece.placeSettings(), BlockPos.ZERO);
/*  38 */     endCityPiece.move(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*     */     
/*  40 */     return endCityPiece;
/*     */   }
/*     */   private static final int MAX_GEN_DEPTH = 8;
/*     */   
/*     */   public static class EndCityPiece extends TemplateStructurePiece { public EndCityPiece(StructureTemplateManager param1StructureTemplateManager, String param1String, BlockPos param1BlockPos, Rotation param1Rotation, boolean param1Boolean) {
/*  45 */       super(StructurePieceType.END_CITY_PIECE, 0, param1StructureTemplateManager, makeIdentifier(param1String), param1String, makeSettings(param1Boolean, param1Rotation), param1BlockPos);
/*     */     }
/*     */     
/*     */     public EndCityPiece(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag) {
/*  49 */       super(StructurePieceType.END_CITY_PIECE, param1CompoundTag, param1StructureTemplateManager, param1Identifier -> makeSettings(param1CompoundTag.getBooleanOr("OW", false), param1CompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */     }
/*     */     
/*     */     private static StructurePlaceSettings makeSettings(boolean param1Boolean, Rotation param1Rotation) {
/*  53 */       BlockIgnoreProcessor blockIgnoreProcessor = param1Boolean ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/*  54 */       return (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor((StructureProcessor)blockIgnoreProcessor).setRotation(param1Rotation);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Identifier makeTemplateLocation() {
/*  59 */       return makeIdentifier(this.templateName);
/*     */     }
/*     */     
/*     */     private static Identifier makeIdentifier(String param1String) {
/*  63 */       return Identifier.withDefaultNamespace("end_city/" + param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*  68 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*     */       
/*  70 */       param1CompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*  71 */       param1CompoundTag.putBoolean("OW", (this.placeSettings.getProcessors().get(0) == BlockIgnoreProcessor.STRUCTURE_BLOCK));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String param1String, BlockPos param1BlockPos, ServerLevelAccessor param1ServerLevelAccessor, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  76 */       if (param1String.startsWith("Chest")) {
/*  77 */         BlockPos blockPos = param1BlockPos.below();
/*     */         
/*  79 */         if (param1BoundingBox.isInside((Vec3i)blockPos)) {
/*  80 */           RandomizableContainer.setBlockEntityLootTable((BlockGetter)param1ServerLevelAccessor, param1RandomSource, blockPos, BuiltInLootTables.END_CITY_TREASURE);
/*     */         }
/*  82 */       } else if (param1BoundingBox.isInside((Vec3i)param1BlockPos) && Level.isInSpawnableBounds(param1BlockPos)) {
/*  83 */         if (param1String.startsWith("Sentry")) {
/*  84 */           Shulker shulker = (Shulker)EntityType.SHULKER.create((Level)param1ServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE);
/*  85 */           if (shulker != null) {
/*  86 */             shulker.setPos(param1BlockPos.getX() + 0.5D, param1BlockPos.getY(), param1BlockPos.getZ() + 0.5D);
/*  87 */             param1ServerLevelAccessor.addFreshEntity((Entity)shulker);
/*     */           } 
/*  89 */         } else if (param1String.startsWith("Elytra")) {
/*  90 */           ItemFrame itemFrame = new ItemFrame((Level)param1ServerLevelAccessor.getLevel(), param1BlockPos, this.placeSettings.getRotation().rotate(Direction.SOUTH));
/*  91 */           itemFrame.setItem(new ItemStack((ItemLike)Items.ELYTRA), false);
/*  92 */           param1ServerLevelAccessor.addFreshEntity((Entity)itemFrame);
/*     */         } 
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startHouseTower(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, List<StructurePiece> paramList, RandomSource paramRandomSource) {
/* 107 */     FAT_TOWER_GENERATOR.init();
/* 108 */     HOUSE_TOWER_GENERATOR.init();
/* 109 */     TOWER_BRIDGE_GENERATOR.init();
/* 110 */     TOWER_GENERATOR.init();
/*     */     
/* 112 */     EndCityPiece endCityPiece = addHelper(paramList, new EndCityPiece(paramStructureTemplateManager, "base_floor", paramBlockPos, paramRotation, true));
/* 113 */     endCityPiece = addHelper(paramList, addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 0, -1), "second_floor_1", paramRotation, false));
/* 114 */     endCityPiece = addHelper(paramList, addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 4, -1), "third_floor_1", paramRotation, false));
/* 115 */     endCityPiece = addHelper(paramList, addPiece(paramStructureTemplateManager, endCityPiece, new BlockPos(-1, 8, -1), "third_roof", paramRotation, true));
/*     */     
/* 117 */     recursiveChildren(paramStructureTemplateManager, TOWER_GENERATOR, 1, endCityPiece, null, paramList, paramRandomSource);
/*     */   }
/*     */   
/*     */   static EndCityPiece addHelper(List<StructurePiece> paramList, EndCityPiece paramEndCityPiece) {
/* 121 */     paramList.add(paramEndCityPiece);
/* 122 */     return paramEndCityPiece;
/*     */   }
/*     */   
/*     */   static boolean recursiveChildren(StructureTemplateManager paramStructureTemplateManager, SectionGenerator paramSectionGenerator, int paramInt, EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, List<StructurePiece> paramList, RandomSource paramRandomSource) {
/* 126 */     if (paramInt > 8) {
/* 127 */       return false;
/*     */     }
/*     */     
/* 130 */     ArrayList<StructurePiece> arrayList = Lists.newArrayList();
/* 131 */     if (paramSectionGenerator.generate(paramStructureTemplateManager, paramInt, paramEndCityPiece, paramBlockPos, arrayList, paramRandomSource)) {
/*     */       
/* 133 */       boolean bool = false;
/* 134 */       int i = paramRandomSource.nextInt();
/* 135 */       for (StructurePiece structurePiece1 : arrayList) {
/* 136 */         structurePiece1.setGenDepth(i);
/* 137 */         StructurePiece structurePiece2 = StructurePiece.findCollisionPiece(paramList, structurePiece1.getBoundingBox());
/* 138 */         if (structurePiece2 != null && structurePiece2.getGenDepth() != paramEndCityPiece.getGenDepth()) {
/* 139 */           bool = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 143 */       if (!bool) {
/* 144 */         paramList.addAll(arrayList);
/* 145 */         return true;
/*     */       } 
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */   
/* 151 */   static final SectionGenerator HOUSE_TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager param1StructureTemplateManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, RandomSource param1RandomSource) {
/* 158 */         if (param1Int > 8) {
/* 159 */           return false;
/*     */         }
/*     */         
/* 162 */         Rotation rotation = param1EndCityPiece.placeSettings().getRotation();
/* 163 */         EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, param1EndCityPiece, param1BlockPos, "base_floor", rotation, true));
/*     */         
/* 165 */         int i = param1RandomSource.nextInt(3);
/* 166 */         if (i == 0) {
/* 167 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-1, 4, -1), "base_roof", rotation, true));
/* 168 */         } else if (i == 1) {
/* 169 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 170 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-1, 8, -1), "second_roof", rotation, false));
/*     */           
/* 172 */           EndCityPieces.recursiveChildren(param1StructureTemplateManager, EndCityPieces.TOWER_GENERATOR, param1Int + 1, endCityPiece, null, param1List, param1RandomSource);
/* 173 */         } else if (i == 2) {
/* 174 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 175 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-1, 4, -1), "third_floor_2", rotation, false));
/* 176 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
/*     */           
/* 178 */           EndCityPieces.recursiveChildren(param1StructureTemplateManager, EndCityPieces.TOWER_GENERATOR, param1Int + 1, endCityPiece, null, param1List, param1RandomSource);
/*     */         } 
/* 180 */         return true;
/*     */       }
/*     */     }; private static interface SectionGenerator {
/*     */     void init(); boolean generate(StructureTemplateManager param1StructureTemplateManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, RandomSource param1RandomSource); }
/* 184 */   static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList((Object[])new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   static final SectionGenerator TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager param1StructureTemplateManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, RandomSource param1RandomSource) {
/* 198 */         Rotation rotation = param1EndCityPiece.placeSettings().getRotation();
/* 199 */         EndCityPieces.EndCityPiece endCityPiece1 = param1EndCityPiece;
/* 200 */         endCityPiece1 = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece1, new BlockPos(3 + param1RandomSource.nextInt(2), -3, 3 + param1RandomSource.nextInt(2)), "tower_base", rotation, true));
/* 201 */         endCityPiece1 = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece1, new BlockPos(0, 7, 0), "tower_piece", rotation, true));
/*     */         
/* 203 */         EndCityPieces.EndCityPiece endCityPiece2 = (param1RandomSource.nextInt(3) == 0) ? endCityPiece1 : null;
/*     */         
/* 205 */         int i = 1 + param1RandomSource.nextInt(3);
/* 206 */         for (byte b = 0; b < i; b++) {
/* 207 */           endCityPiece1 = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece1, new BlockPos(0, 4, 0), "tower_piece", rotation, true));
/* 208 */           if (b < i - 1 && param1RandomSource.nextBoolean()) {
/* 209 */             endCityPiece2 = endCityPiece1;
/*     */           }
/*     */         } 
/*     */         
/* 213 */         if (endCityPiece2 != null) {
/* 214 */           for (Tuple<Rotation, BlockPos> tuple : EndCityPieces.TOWER_BRIDGES) {
/* 215 */             if (param1RandomSource.nextBoolean()) {
/*     */               
/* 217 */               EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece2, (BlockPos)tuple.getB(), "bridge_end", rotation.getRotated((Rotation)tuple.getA()), true));
/* 218 */               EndCityPieces.recursiveChildren(param1StructureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, param1Int + 1, endCityPiece, null, param1List, param1RandomSource);
/*     */             } 
/*     */           } 
/*     */           
/* 222 */           endCityPiece1 = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece1, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */         }
/* 224 */         else if (param1Int == 7) {
/* 225 */           endCityPiece1 = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece1, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */         } else {
/* 227 */           return EndCityPieces.recursiveChildren(param1StructureTemplateManager, EndCityPieces.FAT_TOWER_GENERATOR, param1Int + 1, endCityPiece1, null, param1List, param1RandomSource);
/*     */         } 
/*     */         
/* 230 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 234 */   static final SectionGenerator TOWER_BRIDGE_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public boolean shipCreated;
/*     */       
/*     */       public void init() {
/* 239 */         this.shipCreated = false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager param1StructureTemplateManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, RandomSource param1RandomSource) {
/* 244 */         Rotation rotation = param1EndCityPiece.placeSettings().getRotation();
/* 245 */         int i = param1RandomSource.nextInt(4) + 1;
/*     */         
/* 247 */         EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, param1EndCityPiece, new BlockPos(0, 0, -4), "bridge_piece", rotation, true));
/* 248 */         endCityPiece.setGenDepth(-1);
/* 249 */         byte b1 = 0;
/* 250 */         for (byte b2 = 0; b2 < i; b2++) {
/* 251 */           if (param1RandomSource.nextBoolean()) {
/* 252 */             endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(0, b1, -4), "bridge_piece", rotation, true));
/* 253 */             b1 = 0;
/*     */           } else {
/* 255 */             if (param1RandomSource.nextBoolean()) {
/* 256 */               endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(0, b1, -4), "bridge_steep_stairs", rotation, true));
/*     */             } else {
/* 258 */               endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(0, b1, -8), "bridge_gentle_stairs", rotation, true));
/*     */             } 
/* 260 */             b1 = 4;
/*     */           } 
/*     */         } 
/*     */         
/* 264 */         if (this.shipCreated || param1RandomSource.nextInt(10 - param1Int) != 0) {
/* 265 */           if (!EndCityPieces.recursiveChildren(param1StructureTemplateManager, EndCityPieces.HOUSE_TOWER_GENERATOR, param1Int + 1, endCityPiece, new BlockPos(-3, b1 + 1, -11), param1List, param1RandomSource)) {
/* 266 */             return false;
/*     */           }
/*     */         } else {
/*     */           
/* 270 */           EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-8 + param1RandomSource.nextInt(8), b1, -70 + param1RandomSource.nextInt(10)), "ship", rotation, true));
/* 271 */           this.shipCreated = true;
/*     */         } 
/*     */ 
/*     */         
/* 275 */         endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(4, b1, 0), "bridge_end", rotation.getRotated(Rotation.CLOCKWISE_180), true));
/* 276 */         endCityPiece.setGenDepth(-1);
/*     */         
/* 278 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 282 */   static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList((Object[])new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   static final SectionGenerator FAT_TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureTemplateManager param1StructureTemplateManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, RandomSource param1RandomSource) {
/* 297 */         Rotation rotation = param1EndCityPiece.placeSettings().getRotation();
/*     */         
/* 299 */         EndCityPieces.EndCityPiece endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, param1EndCityPiece, new BlockPos(-3, 4, -3), "fat_tower_base", rotation, true));
/* 300 */         endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(0, 4, 0), "fat_tower_middle", rotation, true));
/* 301 */         for (byte b = 0; b < 2 && 
/* 302 */           param1RandomSource.nextInt(3) != 0; b++) {
/*     */ 
/*     */           
/* 305 */           endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(0, 8, 0), "fat_tower_middle", rotation, true));
/*     */           
/* 307 */           for (Tuple<Rotation, BlockPos> tuple : EndCityPieces.FAT_TOWER_BRIDGES) {
/* 308 */             if (param1RandomSource.nextBoolean()) {
/*     */               
/* 310 */               EndCityPieces.EndCityPiece endCityPiece1 = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, (BlockPos)tuple.getB(), "bridge_end", rotation.getRotated((Rotation)tuple.getA()), true));
/* 311 */               EndCityPieces.recursiveChildren(param1StructureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, param1Int + 1, endCityPiece1, null, param1List, param1RandomSource);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 316 */         endCityPiece = EndCityPieces.addHelper(param1List, EndCityPieces.addPiece(param1StructureTemplateManager, endCityPiece, new BlockPos(-2, 8, -2), "fat_tower_top", rotation, true));
/* 317 */         return true;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */