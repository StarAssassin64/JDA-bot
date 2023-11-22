package com.StarAssassin64.JDA_Bot.Commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("welcome")) {
            // Runs /welcome command
            String username = event.getUser().getGlobalName();
            event.reply("Welcome to the server **" + username + "**!").setEphemeral(true).queue();
        } else if (command.equals("roles")) {
            // Runs /roles command
            event.deferReply().setEphemeral(true).queue();
            String roles = "";
            for (Role role : event.getGuild().getRoles()) {
                roles += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(roles).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
            List<CommandData> commandsData = new ArrayList<>();
            commandsData.add(Commands.slash("welcome", "get welcomed by the bot!"));
            commandsData.add(Commands.slash("roles", "display all roles on the server"));
            event.getGuild().updateCommands().addCommands(commandsData).queue();
    }
}
