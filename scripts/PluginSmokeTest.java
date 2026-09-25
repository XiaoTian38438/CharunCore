import com.CharunCore.server.plugin.Plugin;
import com.CharunCore.server.plugin.PluginManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * 插件链路冒烟测试: 打包示例插件 JAR -> PluginManager 实际加载 -> 验证 onEnable -> 卸载。
 * 运行: java -cp "target/classes;scripts" PluginSmokeTest
 */
public class PluginSmokeTest {

    public static void main(String[] args) throws Exception {
        Path pluginsDir = Path.of("target", "smoke-plugins");
        if (Files.exists(pluginsDir)) {
            Files.walk(pluginsDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
        Files.createDirectories(pluginsDir);
        Path classesDir = Path.of("target", "smoke-classes");
        if (Files.exists(classesDir)) {
            Files.walk(classesDir).sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
        Files.createDirectories(classesDir);

        int pass = 0, total = 0;
        String[][] examples = {
            {"EssentialsGUI", "examples/EssentialsGUI/EssentialsPlugin.java",
                "com/example/essentials/EssentialsPlugin.class"},
            {"InfoBoard", "examples/InfoBoard/InfoBoardPlugin.java",
                "com/example/infoboard/InfoBoardPlugin.class"},
            {"Guard", "examples/Guard/GuardPlugin.java",
                "com/example/guard/GuardPlugin.class"},
        };
        for (String[] ex : examples) {
            String name = ex[0];
            String source = ex[1];
            String entry = ex[2];
            // 1. 编译示例
            Process javac = new ProcessBuilder("javac", "-encoding", "UTF-8",
                    "-cp", "target/classes", "-d", classesDir.toString(), source)
                    .redirectErrorStream(true).start();
            javac.getInputStream().transferTo(System.out);
            if (javac.waitFor() != 0) { System.out.println("[FAIL] " + name + " 编译失败"); continue; }
            total++;
            // 2. 打 JAR (plugin.yml 在根 + classes 全目录: 覆盖匿名内部类/嵌套类)
            Path jarPath = pluginsDir.resolve(name + ".jar");
            Process jar = new ProcessBuilder("jar", "cf", jarPath.toString(),
                    "-C", classesDir.toString(), ".",
                    "-C", source.substring(0, source.lastIndexOf('/')), "plugin.yml")
                    .redirectErrorStream(true).start();
            jar.getInputStream().transferTo(System.out);
            if (jar.waitFor() != 0) { System.out.println("[FAIL] " + name + " 打包失败"); continue; }
            System.out.println("[OK] " + name + ".jar 已生成 (" + Files.size(jarPath) + " bytes)");
        }

        // 3. PluginManager 实际加载
        PluginManager pm = new PluginManager();
        pm.loadPlugins(pluginsDir.toFile());
        System.out.println("─────────────────────────────────");
        for (Plugin p : pm.getPlugins()) {
            String expect = p.getName();
            boolean enabled = p.isEnabled();
            System.out.println("[插件] " + expect + " enabled=" + enabled
                    + " dataFolder=" + p.getDataFolder());
            if (enabled) pass++;
        }
        System.out.println("─────────────────────────────────");

        // 4. 卸载(验证 disable 链路)
        pm.disableAll();
        boolean allDisabled = pm.getPlugins().isEmpty();
        System.out.println("[卸载] 全部卸载 " + (allDisabled ? "OK" : "FAIL"));

        System.out.println(pass + "/" + total + " 插件加载成功, 卸载 " + (allDisabled ? "OK" : "FAIL"));
        System.out.println(pass == total && allDisabled ? "SMOKE PASS" : "SMOKE FAIL");
        System.exit(pass == total && allDisabled ? 0 : 1);
    }
}
