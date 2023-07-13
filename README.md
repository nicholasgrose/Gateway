# Gateway

A [Paper](https://papermc.io/) server plugin that adds [Discord](https://discord.com/)
-to-[Minecraft](https://www.minecraft.net/en-us) interactions and vice versa.
It has a number of extensions that can be enabled or disabled in its configuration, so you don't need to use them all.

## Available extensions:

- About Extension – Lets Discord users view the server version and have fun.
- Chat Extension – Adds cross-chat capabilities between Minecraft and Discord.
- IP Extension – Adds a way for Discord users to query for the server's IP address.
- List Extension – Lets Discord users list the names of online users.
- TPS Extension - Lets Discord users see how the server is doing by checking its TPS.
- Whitelist Extension – Lets Discord users add and remove people from the server whitelist.

## Installing the Plugin:

1. Install just like any other Paper plugin: plop the jar in your plugin's directory.
2. Start your server to create the default configuration in the `plugins/Gateway` directory.
3. Next, modify the configuration. You can do this in two ways:

   a. Modify the `plugins/Gateway/config.yaml` file by adding a Discord bot token and list of the text channels in which
   you wish to have the bot post and detect messages.
   You can reload from the file with `/gateway config reload` in-game.

   b. Use `/gateway config set bot.token TOKEN` and `/gateway config add bot.botChannels CHANNEL_NAME` to update the bot
   configuration.

   You can play with the other settings, too.
   Information about those settings is viewable in-game via `/gateway config help` and in the YAML comments.
4. Play!

## Discord Bot Tokens

The Discord bot token connects your Minecraft server to the bot in Discord.
Because this plugin actually runs the bot, it is impossible to have one bot shared by all.
DO NOT SHARE YOUR BOT TOKEN.
It is unique to the bot, and anyone with it will be able to control it.
Thus, you will need to create your own Discord bot.
This is an easy process, though:

1. Go to https://discord.com/developers/applications/.
2. Log in with your Discord account (or make one if you don't already have one).
   If you get navigated away from the page, after logging in, just go to the developer portal link again,
   and it should work.
3. Make sure the tab on the left side has 'Applications' highlighted and then, on the top right, click 'New
   Application'.
   You may then name your app and then click 'Create'.
4. We now must create our bot.
   Do this by clicking the 'Bot' tab on the left side and then hit 'Add Bot' on the top right.
   You will need to confirm this action.
5. Underneath your new bot's username, there will be a 'Token' field that says 'Click to Reveal Token'.
   Reveal the token and copy it into the bot token field of the plugin's configuration file.
   Again, DO NOT SHARE THIS TOKEN.
6. Since our bot intends to use a lot of features for Discord, we must enable its intents.
   Partway down the page, you will find a section titled 'Privileged Gateway Intents'.
   Turn the sliders to on for Presence Intent, Server
   Members Intent, and Message Content Intent.
7. From here on out, we are adding the bot to your server. Start by going to the 'OAuth2' tab on the left side.
8. There will be a section named 'OAuth2 URL Generator'.
   This creates the URL you need to add the bot to your server.
   First, go the 'Scopes' label within this section and click only 'bot'.
9. At the bottom of the 'Scopes' box, there will now be a URL copy this, as it will be what you use for adding the bot
   to your server.
10. Above the 'OAuth2 URL Generator' will be a section labeled 'Redirects'.
    Click 'Add Redirect' and paste your URL in.
    Save your changes with the pop-up now at the bottom of your screen.
11. Add your bot to the server using the link still in your clipboard.
12. Enjoy!

## Building the Plugin

If you are a developer and wish to be able to modify the plugin or contribute, you can clone this repository and use
[Gradle](https://gradle.org/) to build and test the plugin.
The following useful Gradle commands are available:

`gradlew build` - Builds the jar, and nothing more.

`gradlew runServer` - Builds the plugin and then runs it in a test server. Good for debugging.

`gradlew clean` - Deletes the build files for a clean build.

`gradlew runChecks` - Runs all automated code checks (formatting and static analysis).
