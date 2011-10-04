package me.luuxx.Dwarflater;

import java.util.logging.Logger;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Dwarflater extends JavaPlugin{
	
	public static Dwarflater plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public Property woords = new Property("plugins/Dwarflater/dwarven.txt", this);
	public static PermissionHandler permissionHandler;
	public boolean dwAuto = false;
	public final ServerChatPlayerListener playerListener = new ServerChatPlayerListener(this);


	@Override
	public void onDisable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");
		
	}
	
	@Override
	public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enablad");
		try
		{
			linkToPermissions();
			pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.High, this);
			woords.load();
			this.logger.info(pdfFile.getName() + "loaded woords");
		}
		catch(Exception e)
		{
			this.logger.info(pdfFile.getName() + "failed to loads woords");
			this.logger.info(e.getMessage());
		}

	}
	
	public void linkToPermissions(){
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (permissionsPlugin == null) {
			this.logger.info("Permission system not detected, defaulting to OP");
		    return;
		}

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		this.logger.info("Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] arg){
		readCommand((Player) sender, commandLable, arg);
		return false;
	}
	
	public void readCommand(Player player, String cmd, String[] arg){
		try
		{
			if(cmd.equalsIgnoreCase("dwarf") && !player.hasPermission("Dwarflater.Dwarf") && !Dwarflater.permissionHandler.has(player, "Dwarflater.Dwarf")){
				player.sendMessage("Dwarven is not in your dictionary. Learn it from your admins or do a self study");
				return ;
			}
			if(cmd.equalsIgnoreCase("dwarfauto") && !player.hasPermission("Dwarflater.DwarfAuto") && !Dwarflater.permissionHandler.has(player, "Dwarflater.DwarfAuto")){
				player.sendMessage("You do not have permission to use the dwarven transelate rune.");
				return ;
			}
			String newWords = "dwarf";
			if(cmd.equalsIgnoreCase("dwarf")){
				if(arg != null && arg.length != 0){
					newWords = TranselateWoord(arg[0]);
					for(int i =  1; i < arg.length; i++){
						newWords += " "+TranselateWoord(arg[i]);
					}
					if(!newWords.equals(""))
						player.chat(newWords);
					else
						return ;
					
				}
				else {
					player.sendMessage(ChatColor.RED + "/dwarf <string>");
				}
			}
			if(cmd.equalsIgnoreCase("dwarfauto")){
				this.dwAuto = !this.dwAuto;
				if(this.dwAuto)
					player.sendMessage(ChatColor.GOLD + "Dwarven transelate rune is enabled. Your words will automatically be transelated to dwarven");
				else
					player.sendMessage(ChatColor.GOLD + "Dwarven transelate rune is disebled. Your words won't be transelated to dwarven");
			}
		} catch(CommandException e){
			logger.info("Dwarven is not supported by the consele. go ingame or transelate by hand.");			
		}
		
	}
	public String TranselateWoord(String woord){
		String newwoord = "";
		try
		{

			if(woords.keyExists(woord.toLowerCase())){
				newwoord = woords.getString(woord.toLowerCase());
			}
			else
			{
				newwoord = woord;
			}
			return newwoord;

		}
		catch(Exception Nulle)
		{
			return woord;
		}			
		
	}

}
