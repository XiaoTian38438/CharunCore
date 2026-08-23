/*    */ package net.minecraft.util;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class IdentifierPattern {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)ExtraCodecs.PATTERN.optionalFieldOf("namespace").forGetter(()), (App)ExtraCodecs.PATTERN.optionalFieldOf("path").forGetter(())).apply((Applicative)paramInstance, IdentifierPattern::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<IdentifierPattern> CODEC;
/*    */   private final Optional<Pattern> namespacePattern;
/*    */   private final Predicate<String> namespacePredicate;
/*    */   private final Optional<Pattern> pathPattern;
/*    */   private final Predicate<String> pathPredicate;
/*    */   private final Predicate<Identifier> locationPredicate;
/*    */   
/*    */   private IdentifierPattern(Optional<Pattern> paramOptional1, Optional<Pattern> paramOptional2) {
/* 24 */     this.namespacePattern = paramOptional1;
/* 25 */     this.namespacePredicate = paramOptional1.<Predicate<String>>map(Pattern::asPredicate).orElse(paramString -> true);
/* 26 */     this.pathPattern = paramOptional2;
/* 27 */     this.pathPredicate = paramOptional2.<Predicate<String>>map(Pattern::asPredicate).orElse(paramString -> true);
/* 28 */     this.locationPredicate = (paramIdentifier -> (this.namespacePredicate.test(paramIdentifier.getNamespace()) && this.pathPredicate.test(paramIdentifier.getPath())));
/*    */   }
/*    */   
/*    */   public Predicate<String> namespacePredicate() {
/* 32 */     return this.namespacePredicate;
/*    */   }
/*    */   
/*    */   public Predicate<String> pathPredicate() {
/* 36 */     return this.pathPredicate;
/*    */   }
/*    */   
/*    */   public Predicate<Identifier> locationPredicate() {
/* 40 */     return this.locationPredicate;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\IdentifierPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */