package com.craftcostaserver.jailrules.censura;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private static Connection con=null;
	@Override
	public void onEnable(){
		PluginDescriptionFile pdffile=this.getDescription();
		this.logger.info(pdffile.getName() + " Version " + pdffile.getVersion() + "Ha sido habilitado.");
		openConnection();
		if(!tableExists()){
			//create table 
			String table="CREATE TABLE censura ("
					+ "dateTime DATETIME NOT NULL,"
					+ "playerName VARCHAR(17) NOT NULL,"
					+ "sanction VARCHAR(15) NOT NULL,"
					+ "jail      VARCHAR(5) NOT NULL,"
					+ "duration VARCHAR(5) NOT NULL,"
					+ "expired BOOLEAN NOT NULL,"
					+ "reason VARCHAR(30) NOT NULL,"
					+ "authoredBy VARCHAR(17) NOT NULL,PRIMARY KEY (`dateTime`))";
			try {
				Statement sttmt=con.createStatement();
				sttmt.executeUpdate(table);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info("[Censura] An error occured on Table Creation");
				e.printStackTrace();
			}
			logger.info("[Censura] Table Censura created!!!");
		}
	}
	
	@Override
	public void onDisable(){
		try {
			if(con!=null && !con.isClosed()){
					con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PluginDescriptionFile pdffile=this.getDescription();
		this.logger.info(pdffile.getName() + " Se ha deshabilitado.");
	}
	
	public synchronized static void openConnection(){
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/prueba","root","");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public synchronized static boolean tableExists(){
		try {
			DatabaseMetaData dbm=con.getMetaData();
			ResultSet tables= dbm.getTables(null, null, "censura", null);
			if(tables.next()){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Check if table exists
		return false;
		

				
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
				for(int i=1;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("mute "+ args[0])){
					player.sendMessage(ChatColor.RED + "Muteaste a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("ctempmute")){
			if(args.length<3){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=2;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("tempmute "+ args[0] + " " + args[1] + " " + aux)){
					player.sendMessage(ChatColor.RED + "Muteaste temporalmente a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE+ args[1]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cunmute")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=1;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("unmute "+ args[0])){
					player.sendMessage(ChatColor.RED + "Muteaste temporalmente a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cwarn")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				for(int i=1;i<args.length;i++){
					aux+=args[i]+" ";
				}
				if(player.performCommand("warn "+ args[0]+ " " +aux)){
					player.sendMessage(ChatColor.RED + "Advertiste a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
					return false;
				}
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("cjailmute")){
			if(args.length<4){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				if(player.performCommand("jail "+ args[0] + " " + args[1] + " " +args[2]) && player.performCommand("tempmute "+ args[0] + " " +args[2]+ " " + aux)){
					for(int i=3;i<args.length;i++){
						aux+=args[i]+" ";
					}
					player.sendMessage(ChatColor.RED + "Encarcelando y muteando a: "+ ChatColor.WHITE+ args[0]);
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
		
		if(commandLabel.equalsIgnoreCase("cphistory")){
			if(args.length<2){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				player.sendMessage(ChatColor.RED + "Muteaste temporalmente a: "+ ChatColor.WHITE+ args[0]);
				player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("chistory")){
			if(args.length==0){
				//Shows first history page
				//how many records have to be shown per page
				
			}else if (args.length==1){
				if (isInteger(args[0])  ) {	
					//Check if the page is out of range else show page
				}
			}else{
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}
			return true;
		}
		
		return false;
	}
	
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
}

