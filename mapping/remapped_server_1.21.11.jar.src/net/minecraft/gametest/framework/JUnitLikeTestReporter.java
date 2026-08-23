/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import java.io.File;
/*    */ import java.time.Instant;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class JUnitLikeTestReporter
/*    */   implements TestReporter {
/*    */   private final Document document;
/*    */   private final Element testSuite;
/*    */   private final Stopwatch stopwatch;
/*    */   private final File destination;
/*    */   
/*    */   public JUnitLikeTestReporter(File paramFile) throws ParserConfigurationException {
/* 26 */     this.destination = paramFile;
/* 27 */     this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/* 28 */     this.testSuite = this.document.createElement("testsuite");
/* 29 */     Element element = this.document.createElement("testsuite");
/* 30 */     element.appendChild(this.testSuite);
/* 31 */     this.document.appendChild(element);
/*    */     
/* 33 */     this.testSuite.setAttribute("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
/*    */     
/* 35 */     this.stopwatch = Stopwatch.createStarted();
/*    */   }
/*    */   
/*    */   private Element createTestCase(GameTestInfo paramGameTestInfo, String paramString) {
/* 39 */     Element element = this.document.createElement("testcase");
/* 40 */     element.setAttribute("name", paramString);
/* 41 */     element.setAttribute("classname", paramGameTestInfo.getStructure().toString());
/* 42 */     element.setAttribute("time", String.valueOf(paramGameTestInfo.getRunTime() / 1000.0D));
/* 43 */     this.testSuite.appendChild(element);
/* 44 */     return element;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTestFailed(GameTestInfo paramGameTestInfo) {
/* 49 */     String str1 = paramGameTestInfo.id().toString();
/* 50 */     String str2 = paramGameTestInfo.getError().getMessage();
/*    */     
/* 52 */     Element element1 = this.document.createElement(paramGameTestInfo.isRequired() ? "failure" : "skipped");
/* 53 */     element1.setAttribute("message", "(" + paramGameTestInfo.getTestBlockPos().toShortString() + ") " + str2);
/*    */     
/* 55 */     Element element2 = createTestCase(paramGameTestInfo, str1);
/* 56 */     element2.appendChild(element1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTestSuccess(GameTestInfo paramGameTestInfo) {
/* 61 */     String str = paramGameTestInfo.id().toString();
/* 62 */     createTestCase(paramGameTestInfo, str);
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish() {
/* 67 */     this.stopwatch.stop();
/* 68 */     this.testSuite.setAttribute("time", String.valueOf(this.stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0D));
/*    */     
/*    */     try {
/* 71 */       save(this.destination);
/* 72 */     } catch (TransformerException transformerException) {
/* 73 */       throw new Error("Couldn't save test report", transformerException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void save(File paramFile) throws TransformerException {
/* 78 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 79 */     Transformer transformer = transformerFactory.newTransformer();
/* 80 */     DOMSource dOMSource = new DOMSource(this.document);
/* 81 */     StreamResult streamResult = new StreamResult(paramFile);
/* 82 */     transformer.transform(dOMSource, streamResult);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\JUnitLikeTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */