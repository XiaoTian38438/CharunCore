/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityTooltipInfo
/*     */ {
/*     */   public static final MapCodec<EntityTooltipInfo> CODEC;
/*     */   public final EntityType<?> type;
/*     */   public final UUID uuid;
/*     */   public final Optional<Component> name;
/*     */   private List<Component> linesCache;
/*     */   
/*     */   static {
/*  71 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(()), (App)UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(()), (App)ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(())).apply((Applicative)paramInstance, EntityTooltipInfo::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityTooltipInfo(EntityType<?> paramEntityType, UUID paramUUID, Component paramComponent) {
/*  84 */     this(paramEntityType, paramUUID, Optional.ofNullable(paramComponent));
/*     */   }
/*     */   
/*     */   public EntityTooltipInfo(EntityType<?> paramEntityType, UUID paramUUID, Optional<Component> paramOptional) {
/*  88 */     this.type = paramEntityType;
/*  89 */     this.uuid = paramUUID;
/*  90 */     this.name = paramOptional;
/*     */   }
/*     */   
/*     */   public List<Component> getTooltipLines() {
/*  94 */     if (this.linesCache == null) {
/*  95 */       this.linesCache = new ArrayList<>();
/*  96 */       Objects.requireNonNull(this.linesCache); this.name.ifPresent(this.linesCache::add);
/*  97 */       this.linesCache.add(Component.translatable("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
/*  98 */       this.linesCache.add(Component.literal(this.uuid.toString()));
/*     */     } 
/* 100 */     return this.linesCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 105 */     if (this == paramObject) {
/* 106 */       return true;
/*     */     }
/* 108 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     EntityTooltipInfo entityTooltipInfo = (EntityTooltipInfo)paramObject;
/* 113 */     return (this.type.equals(entityTooltipInfo.type) && this.uuid.equals(entityTooltipInfo.uuid) && this.name.equals(entityTooltipInfo.name));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     int i = this.type.hashCode();
/* 119 */     i = 31 * i + this.uuid.hashCode();
/* 120 */     i = 31 * i + this.name.hashCode();
/* 121 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\HoverEvent$EntityTooltipInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */