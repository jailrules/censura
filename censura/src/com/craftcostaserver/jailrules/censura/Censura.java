package com.craftcostaserver.jailrules.censura;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
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
			this.logger.info("[Censura] Error al cerrar la conexion a Base de Datos");
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
		if(commandLabel.equalsIgnoreCase("censura")){
			if(args.length==0){
				player.sendMessage(ChatColor.WHITE+ "Usa "+ChatColor.GOLD+"/censura Help "+ChatColor.WHITE+"para obtener ayuda");
			}else{
				if(args.length==1){
					if(args[0].equalsIgnoreCase("help")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Help ----");
						player.sendMessage(ChatColor.GOLD+"/cban "+ChatColor.WHITE+"Usa /censura ban Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/ctempban "+ChatColor.WHITE+"Usa /censura tempban Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cunban "+ChatColor.WHITE+"Usa /censura unban Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cmute "+ChatColor.WHITE+"Usa /censura mute Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/ctempmute "+ChatColor.WHITE+"Usa /censura tempmute Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cunmute "+ChatColor.WHITE+"Usa /censura unmute Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cwarn "+ChatColor.WHITE+"Usa /censura warn Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cjail "+ChatColor.WHITE+"Usa /censura jail Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cunjail "+ChatColor.WHITE+"Usa /censura unjail Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cjailmute "+ChatColor.WHITE+"Usa /censura jailmute Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/ckick "+ChatColor.WHITE+"Usa /censura kick Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/chistory "+ChatColor.WHITE+"Usa /censura history Para mas informacion");
						player.sendMessage(ChatColor.GOLD+"/cphistory "+ChatColor.WHITE+"Usa /censura phistory Para mas informacion");
					}
					
					if(args[0].equalsIgnoreCase("ban")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Ban Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cban "+ChatColor.WHITE+"para banear a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cban "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("tempban")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura TempBan Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/ctempban "+ChatColor.WHITE+"para banear temporalmente a un jugador");
						player.sendMessage(ChatColor.GOLD+"/ctempban "+ChatColor.WHITE+"<Usuario> <Duracion> <Razon>");
					}
					if(args[0].equalsIgnoreCase("unban")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura UnBan Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cunban "+ChatColor.WHITE+"para desbanear a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cunban "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("mute")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Mute Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cmute "+ChatColor.WHITE+"para mutear a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cmute "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("tempmute")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura TempMute Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/ctempmute "+ChatColor.WHITE+"para mutear temporalmente a un jugador");
						player.sendMessage(ChatColor.GOLD+"/ctempmute "+ChatColor.WHITE+"<Usuario> <Duracion> <Razon>");
					}
					if(args[0].equalsIgnoreCase("unmute")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura UnMute Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cunmute "+ChatColor.WHITE+"para desmutear a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cunmute "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("kick")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Kick Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/ckick "+ChatColor.WHITE+"para kickear a un jugador");
						player.sendMessage(ChatColor.GOLD+"/ckick "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("warn")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Warn Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cwarn "+ChatColor.WHITE+"para advertir a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cwarn "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("history")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura History Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/chistory "+ChatColor.WHITE+"para ver el historial de castigos aplicados");
						player.sendMessage(ChatColor.GOLD+"/chistory "+ChatColor.WHITE+"<pagina>");
					}
					if(args[0].equalsIgnoreCase("phistory")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura PlayerHistory Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cphistory "+ChatColor.WHITE+" para ver el historial de jugador");
						player.sendMessage(ChatColor.GOLD+"/chistory "+ChatColor.WHITE+"<Usuario>");
					}
					if(args[0].equalsIgnoreCase("jail")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Jail Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cjail "+ChatColor.WHITE+"para encarcelar a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cjail "+ChatColor.WHITE+"<Usuario> <Celda> <Duracion> <Razon>");
					}
					if(args[0].equalsIgnoreCase("unjail")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura UnJail Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cunjail "+ChatColor.WHITE+"para sacar de la carcel a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cunjail "+ChatColor.WHITE+"<Usuario> <Razon>");
					}
					if(args[0].equalsIgnoreCase("jailmute")){
						player.sendMessage(ChatColor.YELLOW+"---- Censura Jail & Mute Help ----");
						player.sendMessage("Usa "+ChatColor.GOLD+"/cjailmute "+ChatColor.WHITE+"para encarcelar y mutear a un jugador");
						player.sendMessage(ChatColor.GOLD+"/cjailmute "+ChatColor.WHITE+"<Usuario> <Celda> <Duracion> <Razon>");
					}
					
				}else{
					player.sendMessage(ChatColor.WHITE+ "Usa "+ChatColor.GOLD+"/censura Help "+ChatColor.WHITE+"para obtener ayuda");
				}
			}
			return true;
		}
		if(commandLabel.equalsIgnoreCase("cjail")){
			if(args.length<4){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				if(player.performCommand("jail "+ args[0] + " " + args[1] + " " +args[2])){
					for(int i=3;i<args.length;i++){
						aux+=" "+args[i];
					}
					player.sendMessage(ChatColor.RED + "Encarcelando a: "+ ChatColor.WHITE+ args[0]);
					player.sendMessage(ChatColor.RED + "Celda: "+ ChatColor.WHITE +args[1]);
					player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[2]);
					player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
					Date now = new Date();
					Timestamp timestamp = new Timestamp(now.getTime());
					
					String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
							+"VALUES (NOW(),'"+args[0]+"','jail','"+args[1]+"','"+args[2]+"',"+"0,'"+aux+"','"+sender.getName()+"')";
					if(insertar(sql)){
						player.sendMessage(ChatColor.RED+"[Censura] Datos guardados correctamente");
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo");
						return false;
					}
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
					player.sendMessage(ChatColor.RED + "Desmuteaste a: "+ ChatColor.WHITE+ args[0]);
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
			int totalPaginas=0;
			int paginaActual=0;
			if(args.length<1){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else if(args.length==1){
				Player obj=Bukkit.getPlayer(args[0]);
				if(obj.hasPlayedBefore()){
					try {
						//Realizar consulta
						PreparedStatement sql= con.prepareStatement("SELECT * FROM `censura` WHERE playerName=?;");
						sql.setString(1, obj.getName());
						ResultSet rs= sql.executeQuery();
						//calcular totalpaginas
						if(rs.next()){
							player.sendMessage(ChatColor.YELLOW+"---- Historial de castigos de "+args[0]+" ----"); 
							
							
							player.sendMessage(ChatColor.YELLOW+"---- Pagina 0/"+totalPaginas+" ----");
							player.sendMessage(ChatColor.WHITE+"Usa "+ChatColor.GOLD+"/cphistory"+ChatColor.WHITE+ " <jugador> <pagina> para navegar entre las paginas del historial");
						}
						else{
							
						}
						sql.close();
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else{
					player.sendMessage(ChatColor.RED+"[Censura] El jugador no existe.");
					return false;
				}
			}else if(args.length==2){
				if (isInteger(args[1])  ) {	
					player.sendMessage(ChatColor.YELLOW+"---- Historial de castigos de "+args[0]+" ----");
					//Realizar consulta
					//calcular totalPaginas y paginaActual
					player.sendMessage(ChatColor.YELLOW+"---- Pagina "+paginaActual+"/"+totalPaginas+" ----");
					player.sendMessage(ChatColor.WHITE+"Usa "+ChatColor.GOLD+"/cphistory"+ChatColor.WHITE+ " <jugador> <pagina> para navegar entre las paginas del historial");
				}else{
					player.sendMessage(ChatColor.RED + "El argumento debe ser un numero entero");
					return false;
				}
			}else{
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("chistory")){
			if(args.length==0){
				player.sendMessage(ChatColor.YELLOW+"---- Historial de castigos ----"); 
				//Shows first history page
				//how many records have to be shown per page
				
				
			}else if (args.length==1){
				if (isInteger(args[0])  ) {	
					//Check if the page is out of range else show page
				}else{
					player.sendMessage(ChatColor.RED + "El argumento debe ser un numero entero");
					return false;
				}
			}else{
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}
			return true;
		}
		
		return false;
	}
	
	public static ArrayList<String> consulta(ResultSet rs){
		ArrayList<String> cnsta=new ArrayList<>();
		try {
			while(rs.next()){
				cnsta.add(new String(rs.getString("dateTime")+" "+rs.getString("playerName")+" "+rs.getString("sanction")+" "+rs.getString("duration")+" "+rs.getString("reason")+rs.getString("Expired")+" "+rs.getString("authoredBy")));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public synchronized boolean insertar(String op){		
		try {
			Statement sttmt= con.createStatement();
			sttmt.executeUpdate(op);
			sttmt.close();
		} catch (SQLException e) {
			this.logger.info("[Censura] Error al intentar ejecutar la operacion de insertar en la base de datos");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static int contarFilas(ResultSet rs){
		return 1;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
}

