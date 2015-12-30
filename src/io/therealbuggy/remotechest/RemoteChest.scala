package io.therealbuggy.remotechest

import java.util.logging.Logger

import io.therealbuggy.remotechest.commands.{PingViewerCommand, PingCommand}
import io.therealbuggy.remotechest.configuration.Configuration
import org.bukkit.plugin.java.JavaPlugin

/**
  * Created by jonathan on 20/12/15.
  */
class RemoteChest extends JavaPlugin {

  private var logger: Logger = null
  private var config: Configuration = null

  override def onEnable(): Unit ={
    saveDefaultConfig()
    logger = getLogger
    logger.info("Plugin habilitado")
    getCommand("pingv").setExecutor(new PingCommand(this))
    getCommand("remotechest").setExecutor(new PingViewerCommand(this))
    config = new Configuration(getConfig)
  }

  def obatinLogger = logger
  def obtainConfig = config
}
