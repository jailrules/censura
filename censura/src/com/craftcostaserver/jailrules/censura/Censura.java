package com.craftcostaserver.jailrules.censura;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Censura extends JavaPlugin{
	public static Censura plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	
	@Override
	public void onEnable(){
		PluginDescriptionFile pdffile=this.getDescription();
		this.logger.info(pdffile.getName() + " Version " + pdffile.getVersion() + "Ha sido habilitado.");
		
	}
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdffile=this.getDescription();
		this.logger.info(pdffile.getName() + " Se ha deshabilitado.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
		Player player= (Player) sender;
		String aux="";
		if(commandLabel.equalsIgnoreCase("cjail")){
			if(args.length<4){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				if(player.performCommand("jail "+ args[0] + " " + args[1] + " " +args[2])){
					for(int i=3;i<args.length;i++){
						aux+=args[i]+" ";
					}
					player.sendMessage(ChatColor.RED + "Encarcelando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Celda: "+ ChatColor.WHITE +args[1]);
					player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[2]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cunjail")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				if(player.performCommand("unjail "+ args[0])){
					for(int i=1;i<args.length;i++){
						aux+=args[i]+" ";
					}
					player.sendMessage(ChatColor.RED + "Desencarcelando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("ckick")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=1;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("kick "+ args[0] + " " + aux)){
					player.sendMessage(ChatColor.RED + "Kickeando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("ctempban")){
			if(args.length<3){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=2;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("tempban "+ args[0] + " " + args[1] + " " +aux)){
					player.sendMessage(ChatColor.RED + "Baneando temporalmente a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[1]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cban")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=1;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("ban "+ args[0] + " " + aux)){
					player.sendMessage(ChatColor.RED + "Baneando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cunban")){
			if(args.length<4){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=3;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("unban "+ args[0])){
					player.sendMessage(ChatColor.RED + "Desbaneando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cmute")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				if(player.performCommand("jail "+ args[0] + " " + args[1] + " " +args[2])){
					for(int i=3;i<args.length;i++){
						aux+=args[i]+" ";
					}
					player.sendMessage(ChatColor.RED + "Encarcelando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Celda: "+ ChatColor.WHITE +args[1]);
					player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[2]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		return false;
	}
}

