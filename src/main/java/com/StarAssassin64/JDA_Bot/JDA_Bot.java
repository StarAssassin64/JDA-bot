package com.StarAssassin64.JDA_Bot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class JDA_Bot {
    private ShardManager shardManager;

    public JDA_Bot() throws LoginException {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("OTcwODE3NTAzOTgwODQ3MTc1.Gz7sLI.gDDzcdk7DelDJ-qUgnMcbQZJsm3JPTyqxYElZ4");
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening("commands"));
        builder.build();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            JDA_Bot bot = new JDA_Bot();
        } catch (LoginException e) {
            System.out.println("Error | Login token not valid");
        }
    }
}
