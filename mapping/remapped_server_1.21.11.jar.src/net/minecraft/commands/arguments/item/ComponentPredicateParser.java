/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.parsing.packrat.Atom;
/*     */ import net.minecraft.util.parsing.packrat.Dictionary;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.ParseState;
/*     */ import net.minecraft.util.parsing.packrat.Rule;
/*     */ import net.minecraft.util.parsing.packrat.Scope;
/*     */ import net.minecraft.util.parsing.packrat.Term;
/*     */ import net.minecraft.util.parsing.packrat.commands.Grammar;
/*     */ import net.minecraft.util.parsing.packrat.commands.IdentifierParseRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;
/*     */ import net.minecraft.util.parsing.packrat.commands.TagParseRule;
/*     */ 
/*     */ 
/*     */ public class ComponentPredicateParser
/*     */ {
/*     */   public static <T, C, P> Grammar<List<T>> createGrammar(Context<T, C, P> paramContext) {
/*  34 */     Atom atom1 = Atom.of("top");
/*  35 */     Atom atom2 = Atom.of("type");
/*  36 */     Atom atom3 = Atom.of("any_type");
/*  37 */     Atom atom4 = Atom.of("element_type");
/*  38 */     Atom atom5 = Atom.of("tag_type");
/*  39 */     Atom atom6 = Atom.of("conditions");
/*  40 */     Atom atom7 = Atom.of("alternatives");
/*  41 */     Atom atom8 = Atom.of("term");
/*  42 */     Atom atom9 = Atom.of("negation");
/*  43 */     Atom atom10 = Atom.of("test");
/*  44 */     Atom atom11 = Atom.of("component_type");
/*  45 */     Atom atom12 = Atom.of("predicate_type");
/*  46 */     Atom atom13 = Atom.of("id");
/*  47 */     Atom atom14 = Atom.of("tag");
/*     */     
/*  49 */     Dictionary dictionary = new Dictionary();
/*     */     
/*  51 */     NamedRule<StringReader, Identifier> namedRule = dictionary.put(atom13, IdentifierParseRule.INSTANCE);
/*     */     
/*  53 */     NamedRule namedRule1 = dictionary.put(atom1, 
/*  54 */         Term.alternative(new Term[] {
/*  55 */             Term.sequence(new Term[] { dictionary.named(atom2), StringReaderTerms.character('['), Term.cut(), Term.optional(dictionary.named(atom6)), StringReaderTerms.character(']') }, ), dictionary
/*  56 */             .named(atom2)
/*     */           }, ), paramScope -> {
/*     */           ImmutableList.Builder builder = ImmutableList.builder();
/*     */           Objects.requireNonNull(builder);
/*     */           ((Optional)paramScope.getOrThrow(paramAtom1)).ifPresent(builder::add);
/*     */           List list = (List)paramScope.get(paramAtom2);
/*     */           if (list != null) {
/*     */             builder.addAll(list);
/*     */           }
/*     */           return (List)builder.build();
/*     */         });
/*  67 */     dictionary.put(atom2, Term.alternative(new Term[] { dictionary
/*  68 */             .named(atom4), 
/*  69 */             Term.sequence(new Term[] { StringReaderTerms.character('#'), Term.cut(), dictionary.named(atom5) }, ), dictionary
/*  70 */             .named(atom3) }, ), paramScope -> Optional.ofNullable(paramScope.getAny(new Atom[] { paramAtom1, paramAtom2 })));
/*     */ 
/*     */     
/*  73 */     dictionary.put(atom3, StringReaderTerms.character('*'), paramScope -> Unit.INSTANCE);
/*  74 */     dictionary.put(atom4, (Rule)new ElementLookupRule<>(namedRule, paramContext));
/*  75 */     dictionary.put(atom5, (Rule)new TagLookupRule<>(namedRule, paramContext));
/*     */     
/*  77 */     dictionary.put(atom6, 
/*  78 */         Term.sequence(new Term[] {
/*  79 */             dictionary.named(atom7), 
/*  80 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character(','), dictionary.named(atom6) }, ))
/*     */           }, ), paramScope -> {
/*     */           Object object = paramContext.anyOf((List)paramScope.getOrThrow(paramAtom1));
/*     */ 
/*     */ 
/*     */           
/*     */           return Optional.<List>ofNullable((List)paramScope.get(paramAtom2)).map(()).orElse(List.of(object));
/*     */         });
/*     */ 
/*     */     
/*  90 */     dictionary.put(atom7, 
/*  91 */         Term.sequence(new Term[] {
/*  92 */             dictionary.named(atom8), 
/*  93 */             Term.optional(Term.sequence(new Term[] { StringReaderTerms.character('|'), dictionary.named(atom7) }, ))
/*     */           }, ), paramScope -> {
/*     */           Object object = paramScope.getOrThrow(paramAtom1);
/*     */ 
/*     */ 
/*     */           
/*     */           return Optional.<List>ofNullable((List)paramScope.get(paramAtom2)).map(()).orElse(List.of(object));
/*     */         });
/*     */ 
/*     */     
/* 103 */     dictionary.put(atom8, 
/* 104 */         Term.alternative(new Term[] {
/* 105 */             dictionary.named(atom10), 
/* 106 */             Term.sequence(new Term[] { StringReaderTerms.character('!'), dictionary.named(atom9) }, )
/*     */           }, ), paramScope -> paramScope.getAnyOrThrow(new Atom[] { paramAtom1, paramAtom2 }));
/*     */ 
/*     */ 
/*     */     
/* 111 */     dictionary.put(atom9, dictionary
/* 112 */         .named(atom10), paramScope -> paramContext.negate(paramScope.getOrThrow(paramAtom)));
/*     */ 
/*     */ 
/*     */     
/* 116 */     dictionary.putComplex(atom10, 
/* 117 */         Term.alternative(new Term[] {
/* 118 */             Term.sequence(new Term[] { dictionary.named(atom11), StringReaderTerms.character('='), Term.cut(), dictionary.named(atom14)
/* 119 */               }, ), Term.sequence(new Term[] { dictionary.named(atom12), StringReaderTerms.character('~'), Term.cut(), dictionary.named(atom14) }, ), dictionary
/* 120 */             .named(atom11)
/*     */           }, ), paramParseState -> {
/*     */           Scope scope = paramParseState.scope();
/*     */           
/*     */           Object object = scope.get(paramAtom1);
/*     */           
/*     */           try {
/*     */             if (object != null) {
/*     */               Dynamic<?> dynamic1 = (Dynamic)scope.getOrThrow(paramAtom2);
/*     */               
/*     */               return paramContext.createPredicateTest((ImmutableStringReader)paramParseState.input(), object, dynamic1);
/*     */             } 
/*     */             
/*     */             Object object1 = scope.getOrThrow(paramAtom3);
/*     */             Dynamic<?> dynamic = (Dynamic)scope.get(paramAtom2);
/*     */             return (dynamic != null) ? paramContext.createComponentTest((ImmutableStringReader)paramParseState.input(), object1, dynamic) : paramContext.createComponentTest((ImmutableStringReader)paramParseState.input(), object1);
/* 136 */           } catch (CommandSyntaxException commandSyntaxException) {
/*     */             paramParseState.errorCollector().store(paramParseState.mark(), commandSyntaxException);
/*     */             
/*     */             return null;
/*     */           } 
/*     */         });
/*     */     
/* 143 */     dictionary.put(atom11, (Rule)new ComponentLookupRule<>(namedRule, paramContext));
/* 144 */     dictionary.put(atom12, (Rule)new PredicateLookupRule<>(namedRule, paramContext));
/* 145 */     dictionary.put(atom14, (Rule)new TagParseRule((DynamicOps)NbtOps.INSTANCE));
/*     */     
/* 147 */     return new Grammar(dictionary, namedRule1);
/*     */   }
/*     */   
/*     */   private static class ElementLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, T> {
/*     */     ElementLookupRule(NamedRule<StringReader, Identifier> param1NamedRule, ComponentPredicateParser.Context<T, C, P> param1Context) {
/* 152 */       super(param1NamedRule, param1Context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T validateElement(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws Exception {
/* 157 */       return (T)((ComponentPredicateParser.Context)this.context).forElementType(param1ImmutableStringReader, param1Identifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 162 */       return ((ComponentPredicateParser.Context)this.context).listElementTypes();
/*     */     }
/*     */   } public static interface Context<T, C, P> { T forElementType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listElementTypes(); T forTagType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listTagTypes(); C lookupComponentType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listComponentTypes(); T createComponentTest(ImmutableStringReader param1ImmutableStringReader, C param1C, Dynamic<?> param1Dynamic) throws CommandSyntaxException; T createComponentTest(ImmutableStringReader param1ImmutableStringReader, C param1C); P lookupPredicateType(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws CommandSyntaxException; Stream<Identifier> listPredicateTypes(); T createPredicateTest(ImmutableStringReader param1ImmutableStringReader, P param1P, Dynamic<?> param1Dynamic) throws CommandSyntaxException;
/*     */     T negate(T param1T);
/*     */     T anyOf(List<T> param1List); }
/*     */   private static class TagLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, T> { TagLookupRule(NamedRule<StringReader, Identifier> param1NamedRule, ComponentPredicateParser.Context<T, C, P> param1Context) {
/* 168 */       super(param1NamedRule, param1Context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T validateElement(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws Exception {
/* 173 */       return (T)((ComponentPredicateParser.Context)this.context).forTagType(param1ImmutableStringReader, param1Identifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 178 */       return ((ComponentPredicateParser.Context)this.context).listTagTypes();
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class ComponentLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, C> {
/*     */     ComponentLookupRule(NamedRule<StringReader, Identifier> param1NamedRule, ComponentPredicateParser.Context<T, C, P> param1Context) {
/* 184 */       super(param1NamedRule, param1Context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected C validateElement(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws Exception {
/* 189 */       return (C)((ComponentPredicateParser.Context)this.context).lookupComponentType(param1ImmutableStringReader, param1Identifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 194 */       return ((ComponentPredicateParser.Context)this.context).listComponentTypes();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PredicateLookupRule<T, C, P> extends ResourceLookupRule<Context<T, C, P>, P> {
/*     */     PredicateLookupRule(NamedRule<StringReader, Identifier> param1NamedRule, ComponentPredicateParser.Context<T, C, P> param1Context) {
/* 200 */       super(param1NamedRule, param1Context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected P validateElement(ImmutableStringReader param1ImmutableStringReader, Identifier param1Identifier) throws Exception {
/* 205 */       return (P)((ComponentPredicateParser.Context)this.context).lookupPredicateType(param1ImmutableStringReader, param1Identifier);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Identifier> possibleResources() {
/* 210 */       return ((ComponentPredicateParser.Context)this.context).listPredicateTypes();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */