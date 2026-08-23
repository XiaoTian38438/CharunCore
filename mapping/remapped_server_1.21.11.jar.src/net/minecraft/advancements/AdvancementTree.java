/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class AdvancementTree
/*     */ {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  20 */   private final Map<Identifier, AdvancementNode> nodes = (Map<Identifier, AdvancementNode>)new Object2ObjectOpenHashMap();
/*  21 */   private final Set<AdvancementNode> roots = (Set<AdvancementNode>)new ObjectLinkedOpenHashSet();
/*  22 */   private final Set<AdvancementNode> tasks = (Set<AdvancementNode>)new ObjectLinkedOpenHashSet();
/*     */   private Listener listener;
/*     */   
/*     */   private void remove(AdvancementNode paramAdvancementNode) {
/*  26 */     for (AdvancementNode advancementNode : paramAdvancementNode.children()) {
/*  27 */       remove(advancementNode);
/*     */     }
/*     */     
/*  30 */     LOGGER.info("Forgot about advancement {}", paramAdvancementNode.holder());
/*  31 */     this.nodes.remove(paramAdvancementNode.holder().id());
/*  32 */     if (paramAdvancementNode.parent() == null) {
/*  33 */       this.roots.remove(paramAdvancementNode);
/*  34 */       if (this.listener != null) {
/*  35 */         this.listener.onRemoveAdvancementRoot(paramAdvancementNode);
/*     */       }
/*     */     } else {
/*  38 */       this.tasks.remove(paramAdvancementNode);
/*  39 */       if (this.listener != null) {
/*  40 */         this.listener.onRemoveAdvancementTask(paramAdvancementNode);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(Set<Identifier> paramSet) {
/*  46 */     for (Identifier identifier : paramSet) {
/*  47 */       AdvancementNode advancementNode = this.nodes.get(identifier);
/*  48 */       if (advancementNode == null) {
/*  49 */         LOGGER.warn("Told to remove advancement {} but I don't know what that is", identifier); continue;
/*     */       } 
/*  51 */       remove(advancementNode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(Collection<AdvancementHolder> paramCollection) {
/*  57 */     ArrayList<AdvancementHolder> arrayList = new ArrayList<>(paramCollection);
/*  58 */     while (!arrayList.isEmpty()) {
/*  59 */       if (!arrayList.removeIf(this::tryInsert)) {
/*  60 */         LOGGER.error("Couldn't load advancements: {}", arrayList);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  65 */     LOGGER.info("Loaded {} advancements", Integer.valueOf(this.nodes.size()));
/*     */   }
/*     */   
/*     */   private boolean tryInsert(AdvancementHolder paramAdvancementHolder) {
/*  69 */     Optional<Identifier> optional = paramAdvancementHolder.value().parent();
/*  70 */     Objects.requireNonNull(this.nodes); AdvancementNode advancementNode1 = optional.<AdvancementNode>map(this.nodes::get).orElse(null);
/*  71 */     if (advancementNode1 == null && optional.isPresent()) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     AdvancementNode advancementNode2 = new AdvancementNode(paramAdvancementHolder, advancementNode1);
/*  76 */     if (advancementNode1 != null) {
/*  77 */       advancementNode1.addChild(advancementNode2);
/*     */     }
/*     */     
/*  80 */     this.nodes.put(paramAdvancementHolder.id(), advancementNode2);
/*  81 */     if (advancementNode1 == null) {
/*  82 */       this.roots.add(advancementNode2);
/*  83 */       if (this.listener != null) {
/*  84 */         this.listener.onAddAdvancementRoot(advancementNode2);
/*     */       }
/*     */     } else {
/*  87 */       this.tasks.add(advancementNode2);
/*  88 */       if (this.listener != null) {
/*  89 */         this.listener.onAddAdvancementTask(advancementNode2);
/*     */       }
/*     */     } 
/*     */     
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  97 */     this.nodes.clear();
/*  98 */     this.roots.clear();
/*  99 */     this.tasks.clear();
/* 100 */     if (this.listener != null) {
/* 101 */       this.listener.onAdvancementsCleared();
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterable<AdvancementNode> roots() {
/* 106 */     return this.roots;
/*     */   }
/*     */   
/*     */   public Collection<AdvancementNode> nodes() {
/* 110 */     return this.nodes.values();
/*     */   }
/*     */   
/*     */   public AdvancementNode get(Identifier paramIdentifier) {
/* 114 */     return this.nodes.get(paramIdentifier);
/*     */   }
/*     */   
/*     */   public AdvancementNode get(AdvancementHolder paramAdvancementHolder) {
/* 118 */     return this.nodes.get(paramAdvancementHolder.id());
/*     */   }
/*     */   
/*     */   public void setListener(Listener paramListener) {
/* 122 */     this.listener = paramListener;
/* 123 */     if (paramListener != null) {
/* 124 */       for (AdvancementNode advancementNode : this.roots) {
/* 125 */         paramListener.onAddAdvancementRoot(advancementNode);
/*     */       }
/* 127 */       for (AdvancementNode advancementNode : this.tasks)
/* 128 */         paramListener.onAddAdvancementTask(advancementNode); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void onAddAdvancementRoot(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onRemoveAdvancementRoot(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onAddAdvancementTask(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onRemoveAdvancementTask(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onAdvancementsCleared();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\AdvancementTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */