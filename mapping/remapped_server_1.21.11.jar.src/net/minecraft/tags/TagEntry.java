/*     */ package net.minecraft.tags;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class TagEntry {
/*     */   static {
/*  15 */     FULL_CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("id").forGetter(TagEntry::elementOrTag), (App)Codec.BOOL.optionalFieldOf("required", Boolean.valueOf(true)).forGetter(())).apply((Applicative)paramInstance, TagEntry::new));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  20 */     CODEC = Codec.either(ExtraCodecs.TAG_OR_ELEMENT_ID, FULL_CODEC).xmap(paramEither -> (TagEntry)paramEither.map((), ()), paramTagEntry -> paramTagEntry.required ? Either.left(paramTagEntry.elementOrTag()) : Either.right(paramTagEntry));
/*     */   }
/*     */   
/*     */   private static final Codec<TagEntry> FULL_CODEC;
/*     */   public static final Codec<TagEntry> CODEC;
/*     */   private final Identifier id;
/*     */   private final boolean tag;
/*     */   private final boolean required;
/*     */   
/*     */   private TagEntry(Identifier paramIdentifier, boolean paramBoolean1, boolean paramBoolean2) {
/*  30 */     this.id = paramIdentifier;
/*  31 */     this.tag = paramBoolean1;
/*  32 */     this.required = paramBoolean2;
/*     */   }
/*     */   
/*     */   private TagEntry(ExtraCodecs.TagOrElementLocation paramTagOrElementLocation, boolean paramBoolean) {
/*  36 */     this.id = paramTagOrElementLocation.id();
/*  37 */     this.tag = paramTagOrElementLocation.tag();
/*  38 */     this.required = paramBoolean;
/*     */   }
/*     */   
/*     */   private ExtraCodecs.TagOrElementLocation elementOrTag() {
/*  42 */     return new ExtraCodecs.TagOrElementLocation(this.id, this.tag);
/*     */   }
/*     */   
/*     */   public static TagEntry element(Identifier paramIdentifier) {
/*  46 */     return new TagEntry(paramIdentifier, false, true);
/*     */   }
/*     */   
/*     */   public static TagEntry optionalElement(Identifier paramIdentifier) {
/*  50 */     return new TagEntry(paramIdentifier, false, false);
/*     */   }
/*     */   
/*     */   public static TagEntry tag(Identifier paramIdentifier) {
/*  54 */     return new TagEntry(paramIdentifier, true, true);
/*     */   }
/*     */   
/*     */   public static TagEntry optionalTag(Identifier paramIdentifier) {
/*  58 */     return new TagEntry(paramIdentifier, true, false);
/*     */   }
/*     */   
/*     */   public <T> boolean build(Lookup<T> paramLookup, Consumer<T> paramConsumer) {
/*  62 */     if (this.tag) {
/*  63 */       Collection<T> collection = paramLookup.tag(this.id);
/*  64 */       if (collection == null) {
/*  65 */         return !this.required;
/*     */       }
/*  67 */       collection.forEach(paramConsumer);
/*     */     } else {
/*  69 */       T t = paramLookup.element(this.id, this.required);
/*  70 */       if (t == null) {
/*  71 */         return !this.required;
/*     */       }
/*  73 */       paramConsumer.accept(t);
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public void visitRequiredDependencies(Consumer<Identifier> paramConsumer) {
/*  79 */     if (this.tag && this.required) {
/*  80 */       paramConsumer.accept(this.id);
/*     */     }
/*     */   }
/*     */   
/*     */   public void visitOptionalDependencies(Consumer<Identifier> paramConsumer) {
/*  85 */     if (this.tag && !this.required) {
/*  86 */       paramConsumer.accept(this.id);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean verifyIfPresent(Predicate<Identifier> paramPredicate1, Predicate<Identifier> paramPredicate2) {
/*  91 */     return (!this.required || (this.tag ? paramPredicate2 : paramPredicate1).test(this.id));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     StringBuilder stringBuilder = new StringBuilder();
/*  97 */     if (this.tag) {
/*  98 */       stringBuilder.append('#');
/*     */     }
/* 100 */     stringBuilder.append(this.id);
/* 101 */     if (!this.required) {
/* 102 */       stringBuilder.append('?');
/*     */     }
/* 104 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public static interface Lookup<T> {
/*     */     T element(Identifier param1Identifier, boolean param1Boolean);
/*     */     
/*     */     Collection<T> tag(Identifier param1Identifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\TagEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */