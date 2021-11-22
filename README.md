# Gateway

A Paper server plugin that adds Discord-to-Minecraft interactions and vice versa. It has a number of extensions that can
be enabled or disabled in its configuration, so you don't need to use them all.

Available extensions:

- Chat Extension - Adds cross-chat capability between Minecraft and Discord.
- Whitelist Extension - Let's Discord users add and removes people from the whitelist.
- List Extension - Let's Discord users list the names of online users.
- About Extension - Let's Discord users view the plugin version and have fun.

## Installing the Plugin:

1. Install just like any other Paper plugin: plop the jar in your plugin's directory.
2. After you install the plugin, you will need to add a configuration file for it. You can get a sample configuration
   file from examples/Gateway.yaml in this repository. Place this configuration file inside your server's plugins
   directory in a new directory called 'Gateway' (i.e., {SERVER_FOLDER}/plugins/Gateway/Gateway.yaml).
3. Next, modify the configuration file by adding a Discord bot token and list of the text channels in which you wish to
   have the bot respond detect commands and/or post messages. You can play with the other settings, too. Those have
   further elaboration in the example yaml.
4. Play!

## Discord Bot Tokens

The Discord bot token connects your Minecraft server to the bot in Discord. Because this plugin actually runs the bot,
it is impossible to have one bot shared by all. DO NOT SHARE YOUR BOT TOKEN. It is unique to the bot and anyone with it
will be able to control it. Thus, you will need to create your own Discord bot. This is an easy process, though:

1. Go to https://discord.com/developers/applications/.
2. Log in with your Discord account (or make one, if you don't already have one). If you get navigated away from the
   page, after logging in, just go to the developer portal link again, and it should work.
3. Make sure the tab on the left side has 'Applications' highlighted and then, on the top right, click 'New
   Application'. You may then name your app and then click 'Create'.
4. We now must create our bot. Do this by clicking the 'Bot' tab on the left side and then hit 'Add Bot' on the top
   right. You will need to confirm this action.
5. Underneath your new bot's username, there will be a 'Token' field that says 'Click to Reveal Token'. Reveal the token
   and copy it into the bot token field of the plugin's configuration file. Again, DO NOT SHARE THIS TOKEN.
6. From here on out, we are adding the bot to your server. Start by going to the 'OAuth2' tab on the left side.
7. There will be a section named 'OAuth2 URL Generator'. This creates the URL you need to add the bot to your server.
   First, go the 'Scopes' label within this section and click only 'bot'.
8. At the bottom of the 'Scopes' box, there will now be a URL copy this, as it will be what you use for adding the bot
   to your server.
9. Above the 'OAuth2 URL Generator' will be a section labelled 'Redirects'. Click 'Add Redirect' and paste your URL in.
   Save your changes with the pop-up now at the bottom of your screen.
10. Add your bot to the server using the link still in your clipboard.
11. Enjoy!

## Building the Plugin

If you are a developer and wish to be able to modify the plugin or contribute, you can clone this repository and use
gradle to build and test the plugin. The following gradle commands are available:

`gradlew build` - Builds the jar, and nothing more.

`gradlew runServer` - Builds the plugin and then runs it in a test server. Good for debugging.

`gradlew clean` - Deletes the build files for a clean build.
