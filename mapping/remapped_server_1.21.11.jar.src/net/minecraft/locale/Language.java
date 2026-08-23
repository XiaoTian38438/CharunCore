/*     */ package net.minecraft.locale;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Language
/*     */ {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  32 */   private static final Gson GSON = new Gson();
/*     */ 
/*     */   
/*  35 */   private static final Pattern UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
/*     */   public static final String DEFAULT = "en_us";
/*  37 */   private static volatile Language instance = loadDefault();
/*     */   
/*     */   private static Language loadDefault() {
/*  40 */     DeprecatedTranslationsInfo deprecatedTranslationsInfo = DeprecatedTranslationsInfo.loadFromDefaultResource();
/*     */     
/*  42 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  43 */     Objects.requireNonNull(hashMap); BiConsumer<String, String> biConsumer = hashMap::put;
/*  44 */     parseTranslations(biConsumer, "/assets/minecraft/lang/en_us.json");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     deprecatedTranslationsInfo.applyToMap((Map)hashMap);
/*     */     
/*  52 */     final Map<Object, Object> storage = Map.copyOf(hashMap);
/*  53 */     return new Language()
/*     */       {
/*     */         public String getOrDefault(String param1String1, String param1String2) {
/*  56 */           return (String)storage.getOrDefault(param1String1, param1String2);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean has(String param1String) {
/*  61 */           return storage.containsKey(param1String);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDefaultRightToLeft() {
/*  66 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public FormattedCharSequence getVisualOrder(FormattedText param1FormattedText) {
/*  72 */           return param1FormattedCharSink -> param1FormattedText.visit((), Style.EMPTY).isPresent();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static void parseTranslations(BiConsumer<String, String> paramBiConsumer, String paramString) {
/*     */     
/*  80 */     try { InputStream inputStream = Language.class.getResourceAsStream(paramString); 
/*  81 */       try { loadFromJson(inputStream, paramBiConsumer);
/*  82 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonParseException iOException)
/*  83 */     { LOGGER.error("Couldn't read strings from {}", paramString, iOException); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void loadFromJson(InputStream paramInputStream, BiConsumer<String, String> paramBiConsumer) {
/*  88 */     JsonObject jsonObject = (JsonObject)GSON.fromJson(new InputStreamReader(paramInputStream, StandardCharsets.UTF_8), JsonObject.class);
/*  89 */     for (Map.Entry entry : jsonObject.entrySet()) {
/*  90 */       String str = UNSUPPORTED_FORMAT_PATTERN.matcher(GsonHelper.convertToString((JsonElement)entry.getValue(), (String)entry.getKey())).replaceAll("%$1s");
/*  91 */       paramBiConsumer.accept((String)entry.getKey(), str);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Language getInstance() {
/*  96 */     return instance;
/*     */   }
/*     */   
/*     */   public static void inject(Language paramLanguage) {
/* 100 */     instance = paramLanguage;
/*     */   }
/*     */   
/*     */   public String getOrDefault(String paramString) {
/* 104 */     return getOrDefault(paramString, paramString);
/*     */   }
/*     */   
/*     */   public abstract String getOrDefault(String paramString1, String paramString2);
/*     */   
/*     */   public abstract boolean has(String paramString);
/*     */   
/*     */   public abstract boolean isDefaultRightToLeft();
/*     */   
/*     */   public abstract FormattedCharSequence getVisualOrder(FormattedText paramFormattedText);
/*     */   
/*     */   public List<FormattedCharSequence> getVisualOrder(List<FormattedText> paramList) {
/* 116 */     return (List<FormattedCharSequence>)paramList.stream().map(this::getVisualOrder).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\locale\Language.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */