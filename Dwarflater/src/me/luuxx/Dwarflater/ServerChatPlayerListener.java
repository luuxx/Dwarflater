package me.luuxx.Dwarflater;

import me.luuxx.Dwarflater.Dwarflater;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class ServerChatPlayerListener extends PlayerListener {
	public static Dwarflater plugin;
	
	public ServerChatPlayerListener(Dwarflater instance){
		plugin = instance;
	}
	
	public void onPlayerChat(PlayerChatEvent chat){
		if(plugin.dwAuto)
		{
			Player p = chat.getPlayer();
			String message = chat.getMessage();
			chat.setCancelled(true);
			String message_lower = message.toLowerCase();
			String[] woord_array = message_lower.split("\\s+");
			String newWords = "dwarf";
			if(woord_array != null && woord_array.length != 0){
				newWords = plugin.TranselateWoord(woord_array[0]);
				for(int i =  1; i < woord_array.length; i++){
					newWords += " "+plugin.TranselateWoord(woord_array[i]);
				}
				if(!newWords.equals("") && newWords.length() != 0)
					p.chat(newWords);
				else
					return ;
				
			}
			return ;
		}
		else
			return ;
	}
}
