package me.astero.companions.filemanager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.astero.companions.CompanionsPlugin;


public class FileManager {
	
	public File companions, messagesData, companionsData, customAbility;
	public YamlConfiguration modifyCompanions, modifyMessagesData, modifyCompanionsData, modifyCustomAbility;
	private CompanionsPlugin main;
	

	public FileManager(CompanionsPlugin main)
	{
		this.main = main;
		initiateFiles();
		scheduledSave();
	
	}
	
	// CUSTOM YML FILES
	
	public void initiateFiles()
	{
		try {
			initiateYAMLFiles();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public YamlConfiguration getCompanions() 
	{
		return modifyCompanions;
	}
	

	public File getCompanionsFile()
	{
		return companions;
	}
	
	public YamlConfiguration getCustomAbility() 
	{
		return modifyCustomAbility;
	}
	

	public File getCustomAbilityFIle()
	{
		return customAbility;
	}
	
	public YamlConfiguration getMessagesData() 
	{
		return modifyMessagesData;
	}
	

	public File getMessagesFile()
	{
		return messagesData;
	}
	
	public YamlConfiguration getCompanionsData() 
	{
		return modifyCompanionsData;
	}
	

	public File getCompanionsDataFile()
	{
		return companionsData;
	}
	
	
	

	public void initiateYAMLFiles() throws IOException
	{
		companions = new File(Bukkit.getServer().getPluginManager()
				.getPlugin("Companions").getDataFolder(), "companions.yml");
		
		messagesData = new File(Bukkit.getServer().getPluginManager()
				.getPlugin("Companions").getDataFolder(), "lang.yml");
		
		companionsData = new File(Bukkit.getServer().getPluginManager()
				.getPlugin("Companions").getDataFolder(), "companionsdata.yml");
		
		customAbility = new File(Bukkit.getServer().getPluginManager()
				.getPlugin("Companions").getDataFolder(), "customability.yml");
		
		if(!companionsData.exists()) // If companiondata.yml is not found.
		{
			companionsData.createNewFile();
		} 

		if(!companions.exists()) 
		{
			main.saveResource("companions.yml", false);
		} 
		
		if(!messagesData.exists()) // If lang.yml is not found.
		{
			main.saveResource("lang.yml", false);
		} 
		
		if(!customAbility.exists()) 
		{
			main.saveResource("customability.yml", false);
		} 
		
		
		modifyCompanions = YamlConfiguration.loadConfiguration(companions);		
		modifyCustomAbility = YamlConfiguration.loadConfiguration(customAbility);		
		modifyMessagesData = YamlConfiguration.loadConfiguration(messagesData);		
		modifyCompanionsData = YamlConfiguration.loadConfiguration(companionsData);		

	}
	
	public void saveFile()
	{
		try {
			modifyCompanionsData.save(companionsData);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
    public void reloadConfigs()
    {
    	if(!main.getFileHandler().isDatabase())
    		saveFile();
    	
    	else
    	{
	    	for(Player player : Bukkit.getOnlinePlayers())
	    	{
	    		main.getCompanionUtil().saveCache(player);
	    	}
    	}
    	
    	main.reloadConfig();
		modifyCompanions = YamlConfiguration.loadConfiguration(companions);		
		modifyCustomAbility = YamlConfiguration.loadConfiguration(customAbility);		
		modifyMessagesData = YamlConfiguration.loadConfiguration(messagesData);		
		modifyCompanionsData = YamlConfiguration.loadConfiguration(companionsData);		
		
		main.getCompanionUtil().setPrefix(main.getFileManager().getMessagesData().getString("messages.prefix"));
		
		main.getFileHandler().cache();
    }
    
    public void scheduledSave()
    {
    	if(!main.getConfig().getBoolean("database.use"))
    	{
	        Bukkit.getScheduler().runTaskTimerAsynchronously(main, new Runnable() 
	        {
	            @Override
	            public void run()
	            { 
	            	System.out.println(ChatColor.GOLD + "(Companions) >" + ChatColor.GRAY + " Saving files right now, it MIGHT lag momentarily.");
	            	
	            	
	            	saveFile();
	
	            	System.out.println(ChatColor.GOLD + "(Companions) >" + ChatColor.GRAY + " Files are successfully saved! :)");
	
	                    
	                
	            }
	        }, main.getConfig().getLong("settings.saveTime") * 1200, main.getConfig().getLong("settings.saveTime") * 1200);
    	}
    	else
    	{
	        Bukkit.getScheduler().runTaskTimer(main, new Runnable() 
	        {
	            @Override
	            public void run()
	            { 
	            	System.out.println(ChatColor.GOLD + "(Companions) >" + ChatColor.GRAY + " Saving files right now, it MIGHT lag momentarily.");
	            	
	            	
	            	for(Player player : Bukkit.getOnlinePlayers())
	            	{
	            		main.getCompanionUtil().saveCache(player);
	            	}
	
	            	System.out.println(ChatColor.GOLD + "(Companions) >" + ChatColor.GRAY + " Files are successfully saved! :)");
	
	                    
	                
	            }
	        }, main.getConfig().getLong("settings.saveTime") * 1200, main.getConfig().getLong("settings.saveTime") * 1200);
    	}
    }
	


}
