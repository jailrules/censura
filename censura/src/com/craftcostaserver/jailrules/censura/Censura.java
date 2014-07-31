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
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;


public class Censura extends JavaPlugin{
	public static Censura plugin;
	public IEssentials ess;
	public final Logger logger = Logger.getLogger("Minecraft");
	private static Connection con=null;
	@Override
	public void onEnable(){
		PluginDescriptionFile pdffile=this.getDescription();
		this.logger.info(pdffile.getName() + " Version " + pdffile.getVersion() + "Ha sido habilitado.");

		ess = (IEssentials) getServer().getPluginManager().getPlugin("Essentials");

		if(ess != null){
			System.out.print("Essentials found");
		}
		else{
			System.out.print("Essentials not found");
		}
		openConnection();
		if(!tableExists()){
			//create table 
			String table="CREATE TABLE censura ("
					+ "dateTime BIGINT NOT NULL,"
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
				try{					
					if(usuarioExiste(args[0])){
						if(player.performCommand("jail "+ args[0] + " " + args[1] + " " +args[2])){
							for(int i=3;i<args.length;i++){
								aux+=" "+args[i];
							}
							player.sendMessage(ChatColor.RED + "Encarcelando a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Celda: "+ ChatColor.WHITE +args[1]);
							player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[2]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							System.currentTimeMillis();
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','jail','"+args[1]+"','"+args[2]+"',"+"0,'"+aux+"','"+sender.getName()+"')";
							if(!insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					User user=ess.getUserMap().getUser(args[0]);
					if(usuarioExiste(args[0])){
						if(user.isJailed()){
							if(player.performCommand("unjail "+ args[0])){
								for(int i=1;i<args.length;i++){
									aux+=args[i]+" ";
								}
								player.sendMessage(ChatColor.RED + "Desencarcelando a: "+ ChatColor.WHITE+ args[0]);
								player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
								String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
										+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','unjail','NC','NC',"+"1,'"+aux+"','"+sender.getName()+"')";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
									return false;
								}
								sql="UPDATE `censura` SET `expired`=1 WHERE `sanction`='jail' AND `playerName`='"+args[0]+"'";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al actualizar castigo anterior.");
									return false;
								}
							}else{
								player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED+"[Censura] Usuario no esta encarcelado!!");
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					if(usuarioExiste(args[0])){
						for(int i=1;i<args.length;i++){
							aux+=args[i]+" ";
						}
						if(player.performCommand("kick "+ args[0] + " " + aux)){
							player.sendMessage(ChatColor.RED + "Kickeando a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','kick','NC','NC',"+"1,'"+aux+"','"+sender.getName()+"')";
							if(insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Datos guardados correctamente.");
							}else{
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					if(usuarioExiste(args[0])){
						for(int i=2;i<args.length;i++){
							aux+=args[i]+" ";
						}
						if(player.performCommand("tempban "+ args[0] + " " + args[1] + " " +aux)){
							player.sendMessage(ChatColor.RED + "Baneando temporalmente a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[1]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','tempban','NC','"+args[1]+"',"+"0,'"+aux+"','"+sender.getName()+"')";
							if(!insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					if(usuarioExiste(args[0])){
						for(int i=1;i<args.length;i++){
							aux+=args[i]+" ";
						}
						if(player.performCommand("ban "+ args[0] + " " + aux)){
							player.sendMessage(ChatColor.RED + "Baneando a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','ban','NC','NC',"+"0,'"+aux+"','"+sender.getName()+"')";
							if(!insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					User user=ess.getUserMap().getUser(args[0]);
					if(usuarioExiste(args[0])){
						if(user.isBanned()){
							for(int i=3;i<args.length;i++){
								aux+=args[i]+" ";
							}
							if(player.performCommand("unban "+ args[0])){
								player.sendMessage(ChatColor.RED + "Desbaneando a: "+ ChatColor.WHITE+ args[0]);
								player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
								String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
										+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','unban','NC','NC',"+"1,'"+aux+"','"+sender.getName()+"')";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
									return false;
								}
								//ACTUALIZAR FILAS ANTERIORES DEL MISMO JUGADOR
								sql="UPDATE `censura` SET `expired`=1 WHERE `sanction`='ban' AND `playerName`='"+args[0]+"'";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al actualizar castigo anterior.");
									return false;
								}
								sql="UPDATE `censura` SET `expired`=1 WHERE `sanction`='tempban' AND `playerName`='"+args[0]+"'";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al actualizar castigo temporal anterior.");
									return false;
								}
							}else{
								player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] El usuario no esta baneado!");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					User user=ess.getUserMap().getUser(args[0]);
					if(usuarioExiste(args[0])){	
						if(!user.isMuted()){
							for(int i=1;i<args.length;i++){
								aux+=args[i]+" ";
							}
							if(player.performCommand("mute "+ args[0])){
								player.sendMessage(ChatColor.RED + "Muteaste a: "+ ChatColor.WHITE+ args[0]);
								player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
								String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
										+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','mute','NC','NC',"+"0,'"+aux+"','"+sender.getName()+"')";
								if(insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Datos guardados correctamente.");
								}else{
									player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
									return false;
								}
							}else{
								player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
								return false;
							}
						}else{
							player.sendMessage("[Censura] El Usuario ya esta muteado!!");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch (Exception e) {
					e.printStackTrace();
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
				try{
					if(usuarioExiste(args[0])){
						for(int i=2;i<args.length;i++){
							aux+=args[i]+" ";
						}
						if(player.performCommand("mute "+ args[0] + " " +args[1])){
							player.sendMessage(ChatColor.RED + "Muteaste temporalmente a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE+ args[1]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','tempmute','NC','"+args[1]+"',"+"0,'"+aux+"','"+sender.getName()+"')";
							if(insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Datos guardados correctamente.");
							}else{
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage("[Censura] Usuario no existe");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					User user=ess.getUserMap().getUser(args[0]);
					if(usuarioExiste(args[0])){
						if(user.isMuted()){
							for(int i=1;i<args.length;i++){
								aux+=args[i]+" ";
							}
							if(player.performCommand("mute "+ args[0])){
								player.sendMessage(ChatColor.RED + "Desmuteaste a: "+ ChatColor.WHITE+ args[0]);
								player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
								String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
										+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','unmute','NC','NC',"+"1,'"+aux+"','"+sender.getName()+"')";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
									return false;
								}
								sql="UPDATE `censura` SET `expired`=1 WHERE `sanction`='mute' AND `playerName`='"+args[0]+"'";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al actualizar castigo anterior.");
									return false;
								}
								sql="UPDATE `censura` SET `expired`=1 WHERE `sanction`='tempmute' AND `playerName`='"+args[0]+"'";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al actualizar castigo anterior.");
									return false;
								}
							}else{
								player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED+"[Censura] Usuario no esta muteado!");
							return false;
						}
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Usuario no existe!");
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
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
				try{
					if(usuarioExiste(args[0])){
						for(int i=1;i<args.length;i++){
							aux+=args[i]+" ";
						}
						if(player.performCommand("warn "+ args[0]+ " " +aux)){
							player.sendMessage(ChatColor.RED + "Advertiste a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','warn','NC','NC',"+"0,'"+aux+"','"+sender.getName()+"')";
							if(!insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Usuario no existe");
						return false;
					}
				}catch (Exception e) {
					e.printStackTrace();
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
				try{
					if(usuarioExiste(args[0])){
						if(player.performCommand("jail "+ args[0] + " " + args[1] + " " +args[2]) && player.performCommand("mute "+ args[0] + " " +args[2])){
							for(int i=3;i<args.length;i++){
								aux+=args[i]+" ";
							}
							player.sendMessage(ChatColor.RED + "Encarcelando y muteando a: "+ ChatColor.WHITE+ args[0]);
							player.sendMessage(ChatColor.RED + "Celda: "+ ChatColor.WHITE +args[1]);
							player.sendMessage(ChatColor.RED + "Duracion: "+ ChatColor.WHITE +args[2]);
							player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
							String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
									+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','jailmute','"+args[1]+"','"+args[2]+"',"+"0,'"+aux+"','"+sender.getName()+"')";
							if(!insertar(sql)){
								player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
							return false;
						}
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Usuario no existe");
						return false;
					}
				}catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
		}

		if(commandLabel.equalsIgnoreCase("cunjailmute")){
			if(args.length<1){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				try{
					if(usuarioExiste(args[0])){
						User user=ess.getUserMap().getUser(args[0]);
						if (user.isMuted() && user.isJailed()){
							if(player.performCommand("unjail "+ args[0]) && player.performCommand("mute "+ args[0])){
								for(int i=1;i<args.length;i++){
									aux+=args[i]+" ";
								}
								player.sendMessage(ChatColor.RED + "Desencarcelando y desmuteando a: "+ ChatColor.WHITE+ args[0]);
								player.sendMessage(ChatColor.RED + "Razon: "+ ChatColor.WHITE +aux);
								String sql="INSERT INTO `censura` (`dateTime`,`playerName`,`sanction`,`jail`,`duration`,`expired`,`reason`,`authoredBy`) "
										+"VALUES ("+System.currentTimeMillis()+",'"+args[0]+"','unjailmute','NC','NC',"+"1,'"+aux+"','"+sender.getName()+"')";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al guardar el castigo.");
									return false;
								}
								sql="UPDATE `censura` SET `expired`=1 WHERE `sanction`='jailmute' AND `playerName`='"+args[0]+"'";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"[Censura] Error al actualizar castigo anterior.");
									return false;
								}
							}else{
								player.sendMessage(ChatColor.RED + "[Censura] Argumentos incorrectos!! Revisa los datos introducidos.");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED+"[Censura] Usuario no esta encarcelado y/o muteado");
							return false;
						}
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Usuario no existe");
						return false;
					}
				}catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
		}

		if(commandLabel.equalsIgnoreCase("cphistory")){
			int lineas=0;
			int totalPaginas=0;
			int paginaActual=0;
			ArrayList<String> res= new ArrayList<String>();
			if(args.length<1){
				player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
				return false;
			}else{
				if(usuarioExiste(args[0])){
					try{
						PreparedStatement sql= con.prepareStatement("SELECT * FROM `censura` WHERE playerName=? ORDER BY `censura`.`dateTime` DESC");
						sql.setString(1, args[0]);
						ResultSet rs= sql.executeQuery();
						res=consulta(rs);
						lineas=res.size();
						if(lineas==0){
							totalPaginas=0;
						}else{
							totalPaginas=((int)lineas/5)+1;
						}
						sql.close();
						rs.close();
					}catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(totalPaginas==0){
						player.sendMessage("[Censura] El usuario no tiene ningun castigo.");
						return false;
					}else{
						if(args.length==1){
							player.sendMessage(ChatColor.YELLOW+"------------- Historial de castigos de "+args[0]+" ---------"); 
							int j=0;
							while(j<res.size() && j<5){
								player.sendMessage(res.get(j));
								j++;
							}
							player.sendMessage(ChatColor.YELLOW+"-------------------     Pagina 1/"+totalPaginas+"    ------------------");
							player.sendMessage(ChatColor.WHITE+"Usa "+ChatColor.GOLD+"/cphistory"+ChatColor.WHITE+ " <jugador> <pagina> para navegar entre las paginas del historial");
						}else if(args.length==2){
							if(isInteger(args[1])){
								if(Integer.parseInt(args[1])>totalPaginas || Integer.parseInt(args[1])<1){
									player.sendMessage(ChatColor.RED+"[Censura] Pagina fuera de rango");
									return false;
								}else{
									paginaActual=Integer.parseInt(args[1]);
									player.sendMessage(ChatColor.YELLOW+"------------- Historial de castigos de "+args[0]+" ---------"); 
									int j=(paginaActual-1)*5;
									while(j<res.size() && j<(paginaActual-1)*5+5){
										player.sendMessage(res.get(j));
										j++;
									}
									player.sendMessage(ChatColor.YELLOW+"-------------------     Pagina "+paginaActual+"/"+totalPaginas+"    ------------------");
									player.sendMessage(ChatColor.WHITE+"Usa "+ChatColor.GOLD+"/cphistory"+ChatColor.WHITE+ " <jugador> <pagina> para navegar entre las paginas del historial");
								}

							}else{
								player.sendMessage(ChatColor.RED+"[Censura] Segundo Argumento incorrecto!! Debe ser un numero de pagina valido");
								return false;
							}
						}else{
							player.sendMessage(ChatColor.RED+"[Censura] Demasiados argumentos!!");
							return false;
						}

					}

				}else{
					player.sendMessage(ChatColor.RED+"[Censura] Usuario no existe");
					return false;
				}
			} 
			return true;
		}

		if(commandLabel.equalsIgnoreCase("cppurge")){
			if(player.hasPermission("censura.*")){
				if(args.length<1){
					player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
					return false;
				}else{
					User user=ess.getUserMap().getUser(args[0]);
					if(usuarioExiste(args[0])){
						if(user.isMuted()){
							player.performCommand("mute "+args[0]);
						}
						if(user.isBanned()){
							player.performCommand("unban "+args[0]);
						}
						if(user.isJailed()){
							player.performCommand("unjail "+args[0]);
						}
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Usuario no existe");
						return false;
					}
					String sql="UPDATE `censura` SET `expired`=1 WHERE `playerName`='"+args[0]+"'";
					if(!insertar(sql)){
						player.sendMessage(ChatColor.RED+"Error al intentar eliminar los castigos del usuario");
					}
					sql="DELETE FROM `censura` WHERE `playerName`='"+args[0]+"'";
					if(!insertar(sql)){
						player.sendMessage(ChatColor.RED+"Error al intentar eliminar los castigos del usuario");
					}
				}
			}else{
				player.sendMessage(ChatColor.RED+"[censura] Usted no tiene permiso para usar ese comando!");
			}
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("cpurge")){
			if(player.hasPermission("censura.*")){
				if(args.length<1){
					player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
					return false;
				}else{
					if(isInteger(args[0])){
						try{
							//CALCULAR TIMESTAMP LIMITE
							int days= Integer.parseInt(args[0]);
							player.sendMessage("dias: "+days);
							if (days>0){
								long date= System.currentTimeMillis()- (days*24*60*60*1000);
								
								String sql="DELETE FROM `censura` WHERE `dateTime` BETWEEN "+date+" AND "+0+" AND `expired`=1";
								if(!insertar(sql)){
									player.sendMessage(ChatColor.RED+"Error al intentar vaciar la tabla");
								}
							}else{
								player.sendMessage(ChatColor.RED+"[Censura] Error el numero de dias debe ser 1 o superior!!");
								return false;
							}
							
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(args[0].equalsIgnoreCase("all")){
						try{
							String sql="TRUNCATE TABLE `censura`";
							if(!insertar(sql)){
								player.sendMessage(ChatColor.RED+"Error al intentar vaciar la tabla");
							}
						}catch(Exception e){
							e.printStackTrace();
							
						}
					}else{
						player.sendMessage(ChatColor.RED+"[censura] Argumento incorrecto!!");
						return false;
					}
				}
			}else{
				player.sendMessage(ChatColor.RED+"[censura] Usted no tiene permiso para usar ese comando!");
			}
			return true;
		}


		if(commandLabel.equalsIgnoreCase("chistory")){
			int totalPaginas=0;
			int paginaActual=0;
			int lineas=0;
			ArrayList<String> res=null;
			try{
				//Realizar consulta
				PreparedStatement sql= con.prepareStatement("SELECT * FROM `censura` ORDER BY `censura`.`dateTime` DESC");
				ResultSet rs= sql.executeQuery();
				res=consulta(rs);
				//calcular totalpaginas, lineas, paginaactual=0;
				lineas=res.size();
				if(lineas==0){
					totalPaginas=0;
				}else{
					totalPaginas=((int)lineas/5)+1;
				}
				sql.close();
				rs.close();
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				player.sendMessage(ChatColor.RED+"[Censura] Error al realizar la consulta del historial.");
			}
			if(totalPaginas==0){
				player.sendMessage(ChatColor.RED+"[Censura] No existen castigos aplicados.");
				return false;
			}else{
				if(args.length==0){
					player.sendMessage(ChatColor.YELLOW+"----------------   Historial de castigos   ---------------");
					player.sendMessage(ChatColor.YELLOW+"Fecha  "
							+ChatColor.AQUA+"Jugador  "
							+ChatColor.GOLD+"Sancion  "
							+ChatColor.GREEN+"Expira en  "
							+ChatColor.RED+"Razon  "
							+ChatColor.YELLOW+"Sancionador "); 
					//Shows first history page
					int j=0;
					while(j<res.size() && j<5){
						player.sendMessage(res.get(j));
						j++;
					}
					player.sendMessage(ChatColor.YELLOW+"-------------------     Pagina 1/"+totalPaginas+"    ------------------");
					player.sendMessage(ChatColor.WHITE+"Usa "+ChatColor.GOLD+"/chistory"+ChatColor.WHITE+ " <pagina> para navegar entre las paginas.");
				}else if (args.length==1){
					if(isInteger(args[0])){
						if(Integer.parseInt(args[0])>totalPaginas || Integer.parseInt(args[0])<1){
							player.sendMessage(ChatColor.RED+"[Censura] Pagina fuera de rango");
							return false;
						}else{
							paginaActual=Integer.parseInt(args[0]);
							player.sendMessage(ChatColor.YELLOW+"----------------   Historial de castigos   ---------------"); 
							int j=(paginaActual-1)*5;
							while(j<res.size() && j<(paginaActual-1)*5+5){
								player.sendMessage(res.get(j));
								j++;
							}
							player.sendMessage(ChatColor.YELLOW+"-------------------     Pagina "+paginaActual+"/"+totalPaginas+"    ------------------");
							player.sendMessage(ChatColor.WHITE+"Usa "+ChatColor.GOLD+"/chistory"+ChatColor.WHITE+ " <pagina> para navegar entre las paginas.");
						}						
					}else{
						player.sendMessage(ChatColor.RED+"[Censura] Primer Argumento incorrecto!! Debe ser un numero de pagina valido");
						return false;
					}
				}else{
					player.sendMessage(ChatColor.RED + "[Censura] Numero de argumentos incorrecto!!");
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean usuarioExiste(String playerName){
		if(ess.getUserMap().getUser(playerName)!=null){
			return true;
		}
		else{
			return false;
		}
	}

	public ArrayList<String> consulta(ResultSet rs){
		ArrayList<String> cnsta=new ArrayList<String>();
		try {
			while(rs.next()){
				String datetime=rs.getString("dateTime");
				long date= Long.parseLong(datetime);
				String name=rs.getString("playerName");
				String sanc=rs.getString("sanction");
				String duration=rs.getString("duration");
				
				if(duration.equalsIgnoreCase("NC")){
					duration="nunca";
				}else{
					long remaining=remainingTime(duration, datetime);
					if(remaining>System.currentTimeMillis()){
						duration=restante(remaining,System.currentTimeMillis());
					}else{
						duration="expirado";
					}
				}
				String razon=rs.getString("reason");
				String author=rs.getString("authoredBy");
				
				String s=ChatColor.YELLOW+String.format("%1$TD %1$TR", new Timestamp(date))
						+" "+ChatColor.AQUA+name
						+" "+ChatColor.GOLD+sanc
						+" "+ChatColor.GREEN+duration
						+" "+ChatColor.RED+razon
						+" "+ChatColor.YELLOW+author;
				System.out.println(s);
				cnsta.add(s);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.logger.info(ChatColor.RED+"[Censura] Error en consulta a ArrayList.");
			e.printStackTrace();
		}		
		return cnsta;
	}
	public synchronized boolean insertar(String op){		
		try {
			Statement sttmt= con.createStatement();
			sttmt.executeUpdate(op);
			sttmt.close();
		} catch (SQLException e) {
			this.logger.info(ChatColor.RED+"[Censura] Error al intentar ejecutar la operacion en la base de datos");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private String restante(long previsto, long actual){
		long resto= previsto-actual;
		int dias,horas,minutos,segundos;
		dias=(int)resto/(24*3600*1000);
		resto=resto-(dias*24*3600*1000);
		horas=(int)resto/(3600*1000);
		resto=resto-(horas*3600*1000);
		minutos=(int)resto/(60*1000);
		resto=resto-(minutos*60*1000);
		segundos=(int)resto/1000;
		return ""+dias+"d"+horas+"h"+minutos+"m"+segundos+"s";
	}
	private long remainingTime(String duration, String dateTime) {
	    List<String> prueba = new ArrayList<String>();
	    long finaltime=0;
	    int duracion=0;
	    Matcher match = Pattern.compile("[0-9]+|[a-z]+|[A-Z]+").matcher(duration);
	    while (match.find()) {
	        prueba.add(match.group());
	    }
	    switch (prueba.get(1).toLowerCase()) {
		case "seconds":
		case "s":
			duracion=Integer.parseInt(prueba.get(0))*1000;
			
			break;
		case "minutes":
		case "m":
			duracion=Integer.parseInt(prueba.get(0))*1000*60;
			break;
		case "hours":
		case "h":
			duracion=Integer.parseInt(prueba.get(0))*1000*3600;
			break;
		case "days":
		case "d":
			duracion=Integer.parseInt(prueba.get(0))*1000*3600*24;
			break;
		case "weeks":
		case "w":
			duracion=Integer.parseInt(prueba.get(0))*1000*3600*24*7;
			break;
		case "months":
		case "mo":
			duracion=Integer.parseInt(prueba.get(0))*1000*3600*24*30;
			break;
		case "years":
		case "y":
			duracion=Integer.parseInt(prueba.get(0))*1000*3600*24*365;
			break;
		default:
			break;
		}
	    finaltime=Long.parseLong(dateTime)+duracion;
	    return finaltime;
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
