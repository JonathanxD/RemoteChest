package io.therealbuggy.remotechest

import java.io.File
import java.util.logging.Logger

import io.therealbuggy.remotechest.api.{RemoteChestAPI, API}
import io.therealbuggy.remotechest.configuration.Configuration
import io.therealbuggy.remotechest.listeners.RemoteChestListener
import io.therealbuggy.remotechest.saver.SaveLoad
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
  * Created by jonathan on 20/12/15.
  */
class RemoteChest extends JavaPlugin {

  private var logger: Logger = null
  private var config: Configuration = null
  private var api: API = null
  private var saveLoad: SaveLoad = null
  private var dataFile: File = null
  private var backupSaveLoad: SaveLoad = null
  private var backupFile: File = null

  override def onEnable(): Unit ={
    logger = getLogger
    logger.info("Carregando configuração...")
    saveDefaultConfig()
    logger.info("Configurando plugin...")
    dataFile = new File(getDataFolder, "data.dat")
    backupFile = new File(getDataFolder, "backup_data.dat")
    config = new Configuration(this, getConfig)
    api = new RemoteChestAPI(this)
    backupSaveLoad = new SaveLoad(api, backupFile)
    logger.info("Carregando chests...")
    saveLoad = new SaveLoad(api, dataFile)
    var loadedChests: Int = 0

    try{
       loadedChests = saveLoad.load()
    }catch {
      case x:Exception =>
        x.printStackTrace()
        logger.warning("Erro ao carregar o arquivo principal, tentando carregar o backup...")
        logger.info("Carregando o backup...")
        try{
          loadedChests = backupSaveLoad.load()
        }catch{
          case x2:Exception =>
            x2.printStackTrace()
            logger.severe("Erro ao carregar o backup! Resetando os arquivos...")
            try{
              dataFile.delete()
              backupFile.delete()
              saveLoad.load()
              backupSaveLoad.load()
            }catch{
              case ex:Exception =>
                ex.printStackTrace()
                logger.severe("Erro critico! Impossivel criar arquivos necessarios para o funcionamento do plugin!")
                this.getPluginLoader.disablePlugin(this)
                return
            }
        }
    }

    logger.info("Carregado "+loadedChests+" chests!")
    logger.info("Registrando listener...")
    Bukkit.getPluginManager.registerEvents(new RemoteChestListener(this), this)
    logger.info("Plugin habilitado!")

    Bukkit.getScheduler.scheduleSyncRepeatingTask(this, new Runnable {
      override def run(): Unit = {
        logger.info("Salvando chests...")
        backupSaveLoad.save()
        saveLoad.save()
        logger.info("Chests salvos!")
      }
    }, 0L, 15*60*20L)
  }

  override def onDisable() : Unit ={
    logger.info("Desabilitando...!")
    Bukkit.getScheduler.cancelTasks(this)
    logger.info("Salvando chests...")
    saveLoad.save()
    logger.info("Chests salvos!")
    config = null
    backupSaveLoad = null
    api = null
    dataFile = null
    logger.info("Desabilitado!")
    logger = null
  }

  def obatinLogger = logger
  def obtainConfig = config
  def obtainAPI = api
  def obtainSaver = saveLoad
  def obtainDataFile = dataFile
}
