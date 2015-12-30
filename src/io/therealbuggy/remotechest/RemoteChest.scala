package io.therealbuggy.remotechest

import java.util.logging.Logger

import io.therealbuggy.remotechest.api.{RemoteChestAPI, API}
import io.therealbuggy.remotechest.configuration.Configuration
import io.therealbuggy.remotechest.listeners.RemoteChestListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
  * Created by jonathan on 20/12/15.
  */
class RemoteChest extends JavaPlugin {

  private var logger: Logger = null
  private var config: Configuration = null
  private var api: API = null

  override def onEnable(): Unit ={
    saveDefaultConfig()
    logger = getLogger
    config = new Configuration(this, getConfig)
    api = new RemoteChestAPI(this)
    Bukkit.getPluginManager.registerEvents(new RemoteChestListener(this), this)
    logger.info("Plugin habilitado")

  }

  def obatinLogger = logger
  def obtainConfig = config
  def obtainAPI = api
}
