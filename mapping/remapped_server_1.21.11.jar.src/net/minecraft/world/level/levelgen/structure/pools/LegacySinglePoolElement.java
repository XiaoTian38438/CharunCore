/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class LegacySinglePoolElement extends SinglePoolElement {
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec(), (App)overrideLiquidSettingsCodec()).apply((Applicative)paramInstance, LegacySinglePoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<LegacySinglePoolElement> CODEC;
/*    */ 
/*    */   
/*    */   protected LegacySinglePoolElement(Either<Identifier, StructureTemplate> paramEither, Holder<StructureProcessorList> paramHolder, StructureTemplatePool.Projection paramProjection, Optional<LiquidSettings> paramOptional) {
/* 31 */     super(paramEither, paramHolder, paramProjection, paramOptional);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructurePlaceSettings getSettings(Rotation paramRotation, BoundingBox paramBoundingBox, LiquidSettings paramLiquidSettings, boolean paramBoolean) {
/* 36 */     StructurePlaceSettings structurePlaceSettings = super.getSettings(paramRotation, paramBoundingBox, paramLiquidSettings, paramBoolean);
/* 37 */     structurePlaceSettings.popProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 38 */     structurePlaceSettings.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 39 */     return structurePlaceSettings;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 44 */     return StructurePoolElementType.LEGACY;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "LegacySingle[" + String.valueOf(this.template) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pools\LegacySinglePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */