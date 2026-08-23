/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public abstract class BanListEntry<T>
/*     */   extends StoredUserEntry<T>
/*     */ {
/*  14 */   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);
/*     */   
/*     */   public static final String EXPIRES_NEVER = "forever";
/*     */   protected final Date created;
/*     */   protected final String source;
/*     */   protected final Date expires;
/*     */   protected final String reason;
/*     */   
/*     */   public BanListEntry(T paramT, Date paramDate1, String paramString1, Date paramDate2, String paramString2) {
/*  23 */     super(paramT);
/*  24 */     this.created = (paramDate1 == null) ? new Date() : paramDate1;
/*  25 */     this.source = (paramString1 == null) ? "(Unknown)" : paramString1;
/*  26 */     this.expires = paramDate2;
/*  27 */     this.reason = paramString2;
/*     */   }
/*     */   
/*     */   protected BanListEntry(T paramT, JsonObject paramJsonObject) {
/*  31 */     super(paramT);
/*     */     Date date1, date2;
/*     */     try {
/*  34 */       date1 = paramJsonObject.has("created") ? DATE_FORMAT.parse(paramJsonObject.get("created").getAsString()) : new Date();
/*  35 */     } catch (ParseException null) {
/*  36 */       date1 = new Date();
/*     */     } 
/*  38 */     this.created = date1;
/*  39 */     this.source = paramJsonObject.has("source") ? paramJsonObject.get("source").getAsString() : "(Unknown)";
/*     */     
/*     */     try {
/*  42 */       date2 = paramJsonObject.has("expires") ? DATE_FORMAT.parse(paramJsonObject.get("expires").getAsString()) : null;
/*  43 */     } catch (ParseException parseException) {
/*  44 */       date2 = null;
/*     */     } 
/*  46 */     this.expires = date2;
/*  47 */     this.reason = paramJsonObject.has("reason") ? paramJsonObject.get("reason").getAsString() : null;
/*     */   }
/*     */   
/*     */   public Date getCreated() {
/*  51 */     return this.created;
/*     */   }
/*     */   
/*     */   public String getSource() {
/*  55 */     return this.source;
/*     */   }
/*     */   
/*     */   public Date getExpires() {
/*  59 */     return this.expires;
/*     */   }
/*     */   
/*     */   public String getReason() {
/*  63 */     return this.reason;
/*     */   }
/*     */   
/*     */   public Component getReasonMessage() {
/*  67 */     String str = getReason();
/*  68 */     return (str == null) ? 
/*  69 */       (Component)Component.translatable("multiplayer.disconnect.banned.reason.default") : 
/*  70 */       (Component)Component.literal(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Component getDisplayName();
/*     */   
/*     */   boolean hasExpired() {
/*  77 */     if (this.expires == null) {
/*  78 */       return false;
/*     */     }
/*  80 */     return this.expires.before(new Date());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void serialize(JsonObject paramJsonObject) {
/*  85 */     paramJsonObject.addProperty("created", DATE_FORMAT.format(this.created));
/*  86 */     paramJsonObject.addProperty("source", this.source);
/*  87 */     paramJsonObject.addProperty("expires", (this.expires == null) ? "forever" : DATE_FORMAT.format(this.expires));
/*  88 */     paramJsonObject.addProperty("reason", this.reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  93 */     if (this == paramObject) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/*  97 */       return false;
/*     */     }
/*  99 */     BanListEntry banListEntry = (BanListEntry)paramObject;
/*     */ 
/*     */     
/* 102 */     return (Objects.equals(this.source, banListEntry.source) && 
/* 103 */       Objects.equals(this.expires, banListEntry.expires) && 
/* 104 */       Objects.equals(this.reason, banListEntry.reason) && 
/* 105 */       Objects.equals(getUser(), banListEntry.getUser()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\BanListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */