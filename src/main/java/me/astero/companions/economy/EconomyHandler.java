package me.astero.companions.economy;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.astero.companions.CompanionsPlugin;
import net.milkbowl.vault.economy.Economy;

public class EconomyHandler {
	
	private CompanionsPlugin main;
    public static Economy econ = null;
	
	public EconomyHandler(CompanionsPlugin main)
	{
		this.main = main;
		
        if (!setupEconomy())
        {
        	main.getFileHandler().setVault(false);
        	System.out.println("\n" +ChatColor.GOLD + "(!) " + ChatColor.YELLOW + "Vault and or an Economy plugin is not found,");
        	System.out.println(ChatColor.YELLOW + "    therefore economy will not be used!"); 
        	System.out.println(ChatColor.YELLOW +  "    (It is recommended to use Vault &");
        	System.out.println(ChatColor.YELLOW + "    an Economy plugin for the best experience.)\n");
            return;
        }
        else
        {
        	main.getFileHandler().setVault(true);
        	System.out.println("\n" + ChatColor.GOLD + "(!) " + ChatColor.YELLOW + "Vault and an Economy plugin has been found, the plugin will be using them.\n");
        }
	}
	
    public boolean setupEconomy() {
        if (main.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
        
    }
    
    public static Economy getEconomy() {
        return econ;
    }

}
