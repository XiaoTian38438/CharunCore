/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*    */ 
/*    */ public class FossilFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Identifier.CODEC.listOf().fieldOf("fossil_structures").forGetter(()), (App)Identifier.CODEC.listOf().fieldOf("overlay_structures").forGetter(()), (App)StructureProcessorType.LIST_CODEC.fieldOf("fossil_processors").forGetter(()), (App)StructureProcessorType.LIST_CODEC.fieldOf("overlay_processors").forGetter(()), (App)Codec.intRange(0, 7).fieldOf("max_empty_corners_allowed").forGetter(())).apply((Applicative)paramInstance, FossilFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FossilFeatureConfiguration> CODEC;
/*    */   
/*    */   public final List<Identifier> fossilStructures;
/*    */   
/*    */   public final List<Identifier> overlayStructures;
/*    */   
/*    */   public final Holder<StructureProcessorList> fossilProcessors;
/*    */   public final Holder<StructureProcessorList> overlayProcessors;
/*    */   public final int maxEmptyCornersAllowed;
/*    */   
/*    */   public FossilFeatureConfiguration(List<Identifier> paramList1, List<Identifier> paramList2, Holder<StructureProcessorList> paramHolder1, Holder<StructureProcessorList> paramHolder2, int paramInt) {
/* 29 */     if (paramList1.isEmpty()) {
/* 30 */       throw new IllegalArgumentException("Fossil structure lists need at least one entry");
/*    */     }
/* 32 */     if (paramList1.size() != paramList2.size()) {
/* 33 */       throw new IllegalArgumentException("Fossil structure lists must be equal lengths");
/*    */     }
/* 35 */     this.fossilStructures = paramList1;
/* 36 */     this.overlayStructures = paramList2;
/* 37 */     this.fossilProcessors = paramHolder1;
/* 38 */     this.overlayProcessors = paramHolder2;
/* 39 */     this.maxEmptyCornersAllowed = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\FossilFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */