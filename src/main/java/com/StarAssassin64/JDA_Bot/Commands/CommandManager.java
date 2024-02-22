package com.StarAssassin64.JDA_Bot.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import io.github.cdimascio.dotenv.Dotenv;
import java.awt.Color;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final Dotenv config = Dotenv.configure().load();
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("welcome")) {
            // Runs /welcome command
            String username = event.getUser().getGlobalName();
            event.reply("Welcome to the server **" + username + "**!").setEphemeral(true).queue();
        } else if (command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();

            String replyMessage = "";
            switch (type) {
                case "hug" -> {
                    replyMessage = "*hugs you*";
                }
                case "laugh" -> {
                    replyMessage = "*laughs*";
                }
                case "cry" -> {
                    replyMessage = "*cries*";
                }
            }
            event.reply(replyMessage).queue();
        } else if (command.equals("giverole")) {
            event.deferReply().setEphemeral(true).queue();
            Member member = event.getOption("user").getAsMember();
            Role role = event.getOption("role").getAsRole();
            Role admin = event.getGuild().getRoleById(config.get("ADMIN_ID"));

            if (event.getMember().getRoles().contains(admin)){
                event.getGuild().addRoleToMember(member, role).queue();
                event.getHook().sendMessage(member.getAsMention() + " has been given " + role.getAsMention()).queue();
            } else {
                event.getHook().sendMessage("You do not have permission to give a user a role").queue();
                System.out.println(event.getUser().getName() + " Tried to give " + member.getUser().getName() + " the role of " + role.getName());
            }
        } else if (command.equals("sendannouncment")) {
            OptionMapping ChannelOption = event.getOption("channel");
            Channel channel = ChannelOption.getAsChannel();
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();
            MessageChannel sendingChannel;
            event.deferReply().queue();
            Role admin = event.getGuild().getRoleById(config.get("ADMIN_ID"));
            EmbedBuilder sendingEmbed = new EmbedBuilder();

            if (channel != null){
                sendingChannel = ChannelOption.getAsChannel().asGuildMessageChannel();
            } else {
                sendingChannel = event.getChannel().asGuildMessageChannel();
            }

            if (event.getMember().getRoles().contains(admin)) {
                sendingEmbed.setAuthor(event.getMember().getEffectiveName(),config.get("INVITE_LINK"),event.getGuild().getBannerUrl());
                sendingEmbed.setTitle("Announcement");
                sendingEmbed.setDescription(message);
                sendingEmbed.setFooter("Bot created by: StarAssassin64");
                sendingEmbed.setColor(Color.RED);
                MessageEmbed embed = sendingEmbed.build();
                sendingChannel.sendMessageEmbeds(embed).queue();
                event.getHook().sendMessage("Announcment sent").queue();
            } else {
                event.getHook().sendMessage("You must be an admin to send an announcement through the bot").queue();

            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandsData = new ArrayList<>();
        commandsData.add(Commands.slash("welcome", "get welcomed by the bot!"));

        // Command: /sendAnnouncement <message> [channel]
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "The channel you want to send this message in", false)
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD);
        commandsData.add(Commands.slash("sendannouncment", "Send an announcement (Admin only)").addOptions(option1, option2));

        // Command: /emote [type]
        OptionData option3 = new OptionData(OptionType.STRING, "type", "The type of emotion to express", true)
                .addChoice("hug", "hug")
                .addChoice("laugh", "laugh")
                .addChoice("cry", "cry");
        commandsData.add(Commands.slash("emote", "Express your emotions through text").addOptions(option3));

        // Command: /giveRole <user> <role>
        OptionData option4 = new OptionData(OptionType.USER, "user", "The user you want to give the role to", true);
        OptionData option5 = new OptionData(OptionType.ROLE, "role", "The role you want to give the user", true);
        commandsData.add(Commands.slash("giverole", "Gives a user a role (Admin Only)").addOptions(option4, option5));

        event.getGuild().updateCommands().addCommands(commandsData).queue();
    }
}
