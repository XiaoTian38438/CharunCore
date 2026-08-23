/*     */ package net.minecraft.util.monitoring.jmx;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MinecraftServerStatistics
/*     */   implements DynamicMBean
/*     */ {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private MinecraftServerStatistics(MinecraftServer paramMinecraftServer) {
/*  34 */     this
/*     */ 
/*     */       
/*  37 */       .attributeDescriptionByName = (Map<String, AttributeDescription>)Stream.<AttributeDescription>of(new AttributeDescription[] { new AttributeDescription("tickTimes", this::getTickTimes, "Historical tick times (ms)", long[].class), new AttributeDescription("averageTickTime", this::getAverageTickTime, "Current average tick time (ms)", long.class) }).collect(Collectors.toMap(paramAttributeDescription -> paramAttributeDescription.name, Function.identity()));
/*     */ 
/*     */     
/*  40 */     this.server = paramMinecraftServer;
/*     */ 
/*     */ 
/*     */     
/*  44 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = (MBeanAttributeInfo[])this.attributeDescriptionByName.values().stream().map(AttributeDescription::asMBeanAttributeInfo).toArray(paramInt -> new MBeanAttributeInfo[paramInt]);
/*     */     
/*  46 */     this.mBeanInfo = new MBeanInfo(MinecraftServerStatistics.class.getSimpleName(), "metrics for dedicated server", arrayOfMBeanAttributeInfo, null, null, new javax.management.MBeanNotificationInfo[0]);
/*     */   }
/*     */   private final MBeanInfo mBeanInfo; private final Map<String, AttributeDescription> attributeDescriptionByName;
/*     */   public static void registerJmxMonitoring(MinecraftServer paramMinecraftServer) {
/*     */     try {
/*  51 */       ManagementFactory.getPlatformMBeanServer().registerMBean(new MinecraftServerStatistics(paramMinecraftServer), new ObjectName("net.minecraft.server:type=Server"));
/*     */ 
/*     */     
/*     */     }
/*  55 */     catch (MalformedObjectNameException|javax.management.InstanceAlreadyExistsException|javax.management.MBeanRegistrationException|javax.management.NotCompliantMBeanException malformedObjectNameException) {
/*  56 */       LOGGER.warn("Failed to initialise server as JMX bean", malformedObjectNameException);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getAverageTickTime() {
/*  61 */     return this.server.getCurrentSmoothedTickTime();
/*     */   }
/*     */   
/*     */   private long[] getTickTimes() {
/*  65 */     return this.server.getTickTimesNanos();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String paramString) {
/*  70 */     AttributeDescription attributeDescription = this.attributeDescriptionByName.get(paramString);
/*  71 */     return (attributeDescription == null) ? 
/*  72 */       null : 
/*  73 */       attributeDescription.getter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(Attribute paramAttribute) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList getAttributes(String[] paramArrayOfString) {
/*  84 */     Objects.requireNonNull(this.attributeDescriptionByName);
/*     */ 
/*     */     
/*  87 */     List<Attribute> list = (List)Arrays.<String>stream(paramArrayOfString).map(this.attributeDescriptionByName::get).filter(Objects::nonNull).map(paramAttributeDescription -> new Attribute(paramAttributeDescription.name, paramAttributeDescription.getter.get())).collect(Collectors.toList());
/*  88 */     return new AttributeList(list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList setAttributes(AttributeList paramAttributeList) {
/*  94 */     return new AttributeList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString) {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MBeanInfo getMBeanInfo() {
/* 105 */     return this.mBeanInfo;
/*     */   }
/*     */   
/*     */   private static final class AttributeDescription {
/*     */     final String name;
/*     */     final Supplier<Object> getter;
/*     */     private final String description;
/*     */     private final Class<?> type;
/*     */     
/*     */     AttributeDescription(String param1String1, Supplier<Object> param1Supplier, String param1String2, Class<?> param1Class) {
/* 115 */       this.name = param1String1;
/* 116 */       this.getter = param1Supplier;
/* 117 */       this.description = param1String2;
/* 118 */       this.type = param1Class;
/*     */     }
/*     */     
/*     */     private MBeanAttributeInfo asMBeanAttributeInfo() {
/* 122 */       return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\monitoring\jmx\MinecraftServerStatistics.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */