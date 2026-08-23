package com.CharunCore.server;

import com.CharunCore.server.console.ConsoleCommandHandler;
import com.CharunCore.server.plugin.Server;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.event.events.WeatherChangeEvent;
import com.CharunCore.server.plugin.scheduler.ServerScheduler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.*;
import com.CharunCore.server.world.entity.EndDragonFight;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.light.LightEngine;
import com.CharunCore.server.network.MinecraftFrameDecoder;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static volatile long worldAge = 0;
    public static volatile long dayTime  = 6000;
    public static volatile boolean isRaining = false;
    public static volatile boolean isThundering = false;
    /** 当前动画化降雨强度(0..1), 每 tick 向 rainTarget 平滑过渡, 实现雨起/停的渐变 */
    public static volatile double rainLevel = 0.0;
    public static volatile double rainTarget = 0.0;
    public static int difficulty = 1;
    public static final String SERVER_NAME = "CharunCore MC Server 1.21.11";
    public static volatile io.netty.channel.Channel serverChannel = null;
    public static volatile boolean RUNNING = true;
    private static int weatherTimer = 600 + (int)(Math.random() * 1200);
    private static int tickCount = 0;

    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "WorldScheduler");
                t.setDaemon(true);
                return t;
            });

    private static io.netty.channel.EventLoopGroup bossGroup;
    private static io.netty.channel.EventLoopGroup workerGroup;

    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        ServerConfig.load();
        difficulty = ServerConfig.difficulty;
        System.out.println("[配置] 端口=" + ServerConfig.serverPort
                + " online-mode=" + ServerConfig.onlineMode
                + " uuid-fix=" + ServerConfig.uuidFix
                + " 压缩阈值=" + ServerConfig.compressionThreshold
                + " 视距=" + ServerConfig.viewDistance
                + " MOTD=" + ServerConfig.motd);

        BlockStateHelper.init();
        BlockManager.init();
        LightEngine.init();
        PlayerDataManager.init();

        System.out.println(">>> NMS Registry 已加载");

        new File("world/region").mkdirs();
        new File("players").mkdirs();

        // 注册 JVM 关闭钩子，确保优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[系统] 正在关闭服务器...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
            System.out.println("[系统] 服务器已关闭");
        }));

        scheduler.scheduleAtFixedRate(() -> {
            worldAge += 1;
            dayTime   = (dayTime + 1) % 24000;
            tickCount++;

            weatherTimer--;
            if (weatherTimer <= 0) {
                boolean nextRaining = !isRaining;
                boolean nextThundering = nextRaining && Math.random() < 0.3;
                var weatherEvent = EventManager.INSTANCE.fire(
                        new WeatherChangeEvent(nextRaining, nextThundering));
                if (!weatherEvent.isCancelled()) {
                    isRaining = nextRaining;
                    isThundering = nextThundering;
                    rainTarget = nextRaining ? 1.0 : 0.0;
                    weatherTimer = nextRaining
                            ? 12000 + (int)(Math.random() * 12000)
                            : 12000 + (int)(Math.random() * 156000);
                } else {
                    weatherTimer = 600 + (int)(Math.random() * 1200);
                }
                for (NetworkHandler player : NetworkHandler.players.values()) {
                    if (player.ctx != null && player.ctx.channel().isActive()) {
                        player.sendPacket(player.ctx, 0x26, pb -> {
                            pb.writeByte((byte) (isRaining ? 1 : 2));
                            pb.writeFloat(0.0f);
                        });
                    }
                }
            }

            // 平滑降雨过渡: 每 tick 把 rainLevel 朝 rainTarget 线性逼近(约 8 秒完成),
            // 并广播 game_state_change reason 7(rain_level), 客户端据此做渐变渲染。
            if (Math.abs(rainLevel - rainTarget) > 0.002) {
                double step = 1.0 / 160.0; // 0.05s/tick * 160tick ≈ 8s
                if (rainLevel < rainTarget) rainLevel = Math.min(rainTarget, rainLevel + step);
                else rainLevel = Math.max(rainTarget, rainLevel - step);
                final float lvl = (float) rainLevel;
                for (NetworkHandler player : NetworkHandler.players.values()) {
                    if (player.ctx != null && player.ctx.channel().isActive()) {
                        player.sendPacket(player.ctx, 0x26, pb -> {
                            pb.writeByte((byte) 7);
                            pb.writeFloat(lvl);
                        });
                    }
                }
            }

            // BUG7: 服务器时钟(上面 worldAge/dayTime 推进 + 天气 lerp)独立成 50ms 节拍,
            // 绝不依赖下面的"重型世界 tick"。这样即使某次世界 tick 偶发较长(例如仍在
            // 处理玩家附近区块生成), 服务器时间也始终以正确速率推进, 不会出现"时间停滞/TPS≈0"。

            // 时间节拍与游戏 tick 解耦: 每 20 个时钟节拍(=1 游戏秒)向客户端广播一次时间包。
            if ((tickCount % 20) == 0) {
                for (NetworkHandler player : NetworkHandler.players.values()) {
                    if (player.ctx != null && player.ctx.channel().isActive()) {
                        player.sendPacket(player.ctx, 0x6F, pb -> {
                            pb.writeLong(worldAge);
                            pb.writeLong(dayTime);
                            pb.writeBoolean(true);
                        });
                    }
                }
            }
        }, 50, 50, TimeUnit.MILLISECONDS);

        // BUG7: 重型世界 tick 单独一个 50ms 节拍(WorldTickScheduler), 与时钟节拍分离。
        // 其自身可被 scheduleAtFixedRate 的背压机制"跳过/合并", 不会反过来冻结服务器时间。
        ScheduledExecutorService worldTickScheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "WorldTickScheduler");
                t.setDaemon(true);
                return t;
            });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            worldTickScheduler.shutdown();
            try {
                if (!worldTickScheduler.awaitTermination(5, TimeUnit.SECONDS))
                    worldTickScheduler.shutdownNow();
            } catch (InterruptedException e) { worldTickScheduler.shutdownNow(); }
        }));

        worldTickScheduler.scheduleAtFixedRate(() -> {
            // BUG7 彻底修复：本线程(WorldTickScheduler)在做任何区块读写时都禁止触发同步生成
            // (WorldManager 通过 isTickThread() 退化为缓存只读)。这保证世界 tick 无论何时都
            // 不会因"生成新区块"而卡死, TPS 与服务器时间不再冻结。
            WorldManager.markTickThread();
            try {
            EntityManager.tick();

            SpawnerSystem.tick();

            EndDragonFight.tick();

            FluidEngine.tick();

            ContainerStore.tick();

            RedstoneEngine.tick();

            RandomTickEngine.tick();

            ServerScheduler.INSTANCE.tick();

            for (NetworkHandler player : NetworkHandler.players.values()) {
                if (player.ctx != null && player.ctx.channel().isActive()) {
                    player.tickSurvival();
                }
            }
            } finally {
                WorldManager.unmarkTickThread();
            }
        }, 50, 50, TimeUnit.MILLISECONDS);

        System.out.println("[系统] 时间线程已启动");

        // 插件加载 (在注册表/配方等初始化完成后)
        try {
            Server.get().getPluginManager().loadPlugins(new File("plugins"));
        } catch (Throwable t) {
            System.err.println("[插件] 加载异常: " + t);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                Server.get().getPluginManager().disableAll()));

        // 启动服务器终端指令读取线程 (控制台可输入 /指令)
        startConsoleReader();

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new MinecraftFrameDecoder(), new NetworkHandler());
                    }
                });
        try {
            serverChannel = b.bind(ServerConfig.serverPort).sync().channel();
        } catch (Exception e) {
            System.err.println("[错误] 无法绑定端口 " + ServerConfig.serverPort + ": " + e.getMessage());
            stopServer();
            return;
        }
        System.out.println("[系统] 服务器已启动, 监听端口 " + ServerConfig.serverPort);
        serverChannel.closeFuture().sync();
        RUNNING = false;
        System.out.println("[系统] 服务器已停止");
    }

    /** 读取 System.in 的守护线程, 把每一行交给 ConsoleCommandHandler 执行。 */
    private static void startConsoleReader() {
        Thread t = new Thread(() -> {
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(System.in, java.nio.charset.StandardCharsets.UTF_8))) {
                System.out.println("[控制台] 已就绪, 输入 /help 查看可用指令 (也可省略前缀 /)");
                String line;
                while (RUNNING && (line = br.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;
                    ConsoleCommandHandler.execute(trimmed);
                }
            } catch (java.io.IOException e) {
                System.out.println("[控制台] 输入读取结束: " + e.getMessage());
            }
        });
        t.setName("ConsoleReader");
        t.setDaemon(true);
        t.start();
    }

    /** 由控制台 /stop 调用：关闭监听通道与所有线程池, 并退出 JVM。 */
    public static void stopServer() {
        System.out.println("[系统] 正在停止服务器...");
        RUNNING = false;
        if (serverChannel != null) {
            try { serverChannel.close().sync(); } catch (Exception ignored) {}
            serverChannel = null;
        }
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
        scheduler.shutdownNow();
        new Thread(() -> {
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            System.exit(0);
        }, "ShutdownExit").start();
    }
}
