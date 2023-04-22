package me.astero.companions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import me.astero.companions.companiondata.PlayerData;
import me.astero.companions.companiondata.packets.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.astero.companions.api.PlaceholderAPI;
import me.astero.companions.command.ClearCompanionDataCommand;
import me.astero.companions.command.CompanionCoinCommand;
import me.astero.companions.command.CompanionCommand;
import me.astero.companions.command.ForceCompanionActiveCommand;
import me.astero.companions.command.ForceCompanionDeactiveCommand;
import me.astero.companions.command.ForceCompanionUpgradeCommand;
import me.astero.companions.command.GiveCompanionCommand;
import me.astero.companions.command.GiveCompanionItemCommand;
import me.astero.companions.command.RemoveCompanionCommand;
import me.astero.companions.command.TradeCompanionCommand;
import me.astero.companions.companiondata.Companions;
import me.astero.companions.companiondata.abilities.CustomAbilities;
import me.astero.companions.companiondata.abilities.PotionEffectAbility;
import me.astero.companions.companiondata.animations.Animation;
import me.astero.companions.currency.CompanionCoin;
import me.astero.companions.database.Database;
import me.astero.companions.database.VersionChecker;
import me.astero.companions.economy.EconomyHandler;
import me.astero.companions.filemanager.FileHandler;
import me.astero.companions.filemanager.FileManager;
import me.astero.companions.items.CompanionToken;
import me.astero.companions.listener.ChatListener;
import me.astero.companions.listener.PlayerListener;
import me.astero.companions.listener.VanishListener;
import me.astero.companions.listener.VehicleListener;
import me.astero.companions.listener.companions.CompanionCache;
import me.astero.companions.listener.companions.CompanionFollow;
import me.astero.companions.listener.companions.CompanionInteraction;
import me.astero.companions.listener.menu.MainMenuListener;
import me.astero.companions.listener.menu.OwnedMenuListener;
import me.astero.companions.listener.menu.PlayerDetailsMenuListener;
import me.astero.companions.listener.menu.ShopMenuListener;
import me.astero.companions.listener.menu.UpgradeMenuListener;
import me.astero.companions.util.CompanionUtil;
import me.astero.companions.util.FormatNumbers;


public class CompanionsPlugin extends JavaPlugin {
	

	@Getter private FileHandler fileHandler;
	@Getter private FileManager fileManager;
	@Getter private Companions companions;
	@Getter private CompanionUtil companionUtil;
	@Getter private PotionEffectAbility potionEffectAbility;
	@Getter private CustomAbilities customAbility;
	@Getter private Animation animation;
	@Getter private FormatNumbers formatNumbers;
	@Getter private Database database;
	@Getter private CompanionCoin companionCoin;
	@Getter private CompanionPacket companionPacket;

	private String source = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";

	@Override
	public void onEnable() 
	{
		System.out.println("\n" + ChatColor.GOLD + "Companions" + ChatColor.GRAY + " by Astero" + ChatColor.GOLD + " is loading up...\n");
		
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		animation = new Animation(this);
	
		fileManager = new FileManager(this);
		formatNumbers = new FormatNumbers();
		System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " YAML files are loaded up!");
		companionUtil = new CompanionUtil(this);
		fileHandler = new FileHandler(this);
		System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " Caching files is done!");
		
		companions = new Companions(this);
		potionEffectAbility = new PotionEffectAbility(this);
		customAbility = new CustomAbilities(this);
		

		new EconomyHandler(this);
		
		
		System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " Misc files are loaded up!");


		
		Bukkit.getPluginManager().registerEvents(new CompanionFollow(this), this);
		Bukkit.getPluginManager().registerEvents(new CompanionCache(this), this);
		Bukkit.getPluginManager().registerEvents(new OwnedMenuListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ShopMenuListener(this), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuListener(this), this);
		Bukkit.getPluginManager().registerEvents(new UpgradeMenuListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CompanionInteraction(this), this);
		Bukkit.getPluginManager().registerEvents(new CompanionToken(this), this);
		Bukkit.getPluginManager().registerEvents(customAbility, this);
		Bukkit.getPluginManager().registerEvents(new VanishListener(this), this);
		Bukkit.getPluginManager().registerEvents(new VehicleListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDetailsMenuListener(this), this);	
		System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " Event Listeners are loaded up!");
		
		getCommand("companions").setExecutor(new CompanionCommand(this));
		getCommand("givecompanion").setExecutor(new GiveCompanionCommand(this));
		getCommand("removecompanion").setExecutor(new RemoveCompanionCommand(this));
		getCommand("givecompanionitem").setExecutor(new GiveCompanionItemCommand(this));
		getCommand("clearcompaniondata").setExecutor(new ClearCompanionDataCommand(this));
		getCommand("forceupgrade").setExecutor(new ForceCompanionUpgradeCommand(this));
		getCommand("forceactive").setExecutor(new ForceCompanionActiveCommand(this));
		getCommand("tradecompanion").setExecutor(new TradeCompanionCommand(this));
		getCommand("forcedeactive").setExecutor(new ForceCompanionDeactiveCommand(this));
		getCommand("companioncoin").setExecutor(new CompanionCoinCommand(this));
		
		System.out.println(ChatColor.GOLD + ">" + ChatColor.GRAY + " Commands are loaded up!");
		
		System.out.println("\n" + ChatColor.GOLD + "              >--------------------------<");
		System.out.println(ChatColor.GOLD + "              A total of " + ChatColor.YELLOW + this.getFileHandler().getCompanionDetails().size() + ChatColor.GOLD + " Companions have");
		System.out.println(ChatColor.GOLD + "                    been loaded up." );
		System.out.println(ChatColor.GOLD + "              >--------------------------< \n");
		
		




		companionCoin = new CompanionCoin(this);
		
	    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
	    {
            new PlaceholderAPI().register();
           
	    }


		System.out.println(ChatColor.GOLD + "Companions" + ChatColor.GRAY + " by Astero" + ChatColor.GOLD + " has been sucessfully loaded up!\n");
		
		//VersionChecker vc = new VersionChecker(this);
		
		setupNMS();

		database = new Database(this, source);
		
		
		
	
		

		

	}
	
	@Override
	public void onDisable() {
		//int companionCount = 0;

		System.out.println(ChatColor.GOLD + "Companions" + ChatColor.GRAY + " is disabling and saving necessary files..");


		PreparedStatement p = null;
		Connection conn = null;

		if (getFileHandler().isDatabase()) {
			for (PlayerData pd : PlayerData.getPlayers().values()) {

				getCompanionUtil().saveCache(pd.getPlayer(), pd, p, conn);

				//pd.removeCompanion();
				//companionCount++;
				//System.out.println(pd.getActiveCompanionName());
			}


			database.close(conn, p, null);
		}
		
		//System.out.println(ChatColor.GOLD + "  >" + ChatColor.GRAY + " Removed " + ChatColor.YELLOW + companionCount + ChatColor.GRAY + " Companion(s)..\n");
		
		this.getFileManager().saveFile();
		
		database.onDisabled();
		
		System.out.println(ChatColor.GOLD + "Companions" + ChatColor.GRAY + " by Astero" + ChatColor.GOLD + " has been sucessfully disabled!\n");
	}
	
	public void saveActiveCompanion(String getCompanionName, Player player, PreparedStatement p, Connection conn) // method not in used
	{
		if(!getFileHandler().isDatabase())
			getFileManager().getCompanionsData().set("companions." + player.getUniqueId()
			+ ".active" , getCompanionName.toUpperCase());
		
		else
		{
			
					  
						
						try
						{
							
							conn = getDatabase().getHikari().getConnection();
							
							p = conn.prepareStatement("INSERT INTO `" + getDatabase().getTablePrefix() 
									+"active` (`UUID`,`name`,`companion`) VALUES (?,?,?)" + 
									"  ON DUPLICATE KEY UPDATE companion=\"" + getCompanionName.toUpperCase() + "\"");
							p.setString(1, player.getUniqueId().toString());
							p.setString(2, player.getName().toString());
							p.setString(3, getCompanionName.toUpperCase());
							//p.setString(4, player.getUniqueId().toString());
		
							p.execute();
							
							
						} 
						catch (SQLException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

		}
	}
	
	public void setupNMS()
	{
		String spigotVersion = null;
		
		try
		{
			spigotVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		}
		catch(ArrayIndexOutOfBoundsException versionNotFound) {}


		if(spigotVersion.equals("v1_19_R1"))
		{
			source = "com.mysql.cj.jdbc.MysqlDataSource";
			companionPacket = new CompanionPacket_1_19_R1(this);
		}
		else if(spigotVersion.equals("v1_18_R2") )
		{
			source = "com.mysql.cj.jdbc.MysqlDataSource";
			//companionPacket = new CompanionPacket_1_18_R2(this);
		}

		else if(spigotVersion.equals("v1_16_R3"))
		{
			companionPacket = new CompanionPacket_1_16_R3(this);
		}
		else if(spigotVersion.equals("v1_16_R2"))
		{
			companionPacket = new CompanionPacket_1_16_R2(this);
		}
		else if(spigotVersion.equals("v1_16_R1"))
		{
			companionPacket = new CompanionPacket_1_16(this);
		}
		else if(spigotVersion.equals("v1_15_R1"))
		{
			companionPacket = new CompanionPacket_1_15(this);
		}
		else if(spigotVersion.equals("v1_14_R1"))
		{
			companionPacket = new CompanionPacket_1_14_R1(this);
		}
		else if(spigotVersion.equals("v1_13_R2"))
		{
			companionPacket = new CompanionPacket_1_13_R2(this);
		}
		else if(spigotVersion.equals("v1_13_R1"))
		{
			companionPacket = new CompanionPacket_1_13_R1(this);
		}
		else if(spigotVersion.equals("v1_12_R1"))
		{
			companionPacket = new CompanionPacket_1_12_R1(this);
		}
		else if(spigotVersion.equals("v1_11_R1"))
		{
			companionPacket = new CompanionPacket_1_11_R1(this);
		}
		else if(spigotVersion.equals("v1_10_R1"))
		{
			companionPacket = new CompanionPacket_1_10_R1(this);
		}
		else if(spigotVersion.equals("v1_9_R2"))
		{
			companionPacket = new CompanionPacket_1_9_R2(this);
		}
		else if(spigotVersion.equals("v1_9_R1"))
		{
			companionPacket = new CompanionPacket_1_9_R1(this);
		}
		else if(spigotVersion.equals("v1_8_R3"))
		{
			companionPacket = new CompanionPacket_1_8_R3(this);
		}
		else if(spigotVersion.equals("v1_8_R2"))
		{
			companionPacket = new CompanionPacket_1_8_R2(this);
		}
		else if(spigotVersion.equals("v1_8_R1"))
		{
			companionPacket = new CompanionPacket_1_8_R1(this);
		}
		else
		{
			
			getLogger().log(Level.SEVERE, "ERROR! The plugin does not support this Spigot version. "
			+ "VERSION: " + spigotVersion + " - PLEASE CONTACT THE DEVELOPER TO FIX THIS ISSUE." );
			
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
	}
	

}
