package com.CharunCore.server.plugin.event.events;

import com.CharunCore.server.plugin.event.Event;

public final class ServerListPingEvent extends Event {

    private String motd;
    private int maxPlayers;
    private final int onlinePlayers;
    private final String clientHost;

    public ServerListPingEvent(String motd, int maxPlayers, int onlinePlayers, String clientHost) {{
        this.motd = motd;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.clientHost = clientHost;
    }}

    public String getMotd() {{ return motd; }}
    public void setMotd(String motd) {{ this.motd = motd; }}
    public int getMaxPlayers() {{ return maxPlayers; }}
    public void setMaxPlayers(int maxPlayers) {{ this.maxPlayers = maxPlayers; }}
    public int getOnlinePlayers() {{ return onlinePlayers; }}
    public String getClientHost() {{ return clientHost; }}
}
