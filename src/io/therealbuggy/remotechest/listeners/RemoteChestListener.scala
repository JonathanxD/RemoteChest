package io.therealbuggy.remotechest.listeners

import io.therealbuggy.remotechest.RemoteChest
import io.therealbuggy.remotechest.api.API
import org.bukkit.block.{BlockFace, Chest}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, Material, Location}
import org.bukkit.event.block.{BlockPlaceEvent, BlockBreakEvent, Action}
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventPriority, EventHandler, Listener}


/**
  * Created by jonathan on 30/12/15.
  */
class RemoteChestListener(remoteChestPlugin: RemoteChest) extends Listener {

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  def blockBreak(event: BlockBreakEvent): Unit = {
    if(event.getBlock.getType == Material.CHEST || event.getBlock.getType == Material.TRAPPED_CHEST) {
      if(remoteChestPlugin.obtainConfig.habilitarLimpeza) {
        remoteChestPlugin.obtainAPI.breakedChest(event.getBlock.getLocation)
      }
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  def blockPlace(event: BlockPlaceEvent): Unit = {
    val api: API = remoteChestPlugin.obtainAPI
    if(event.getBlock.getType == Material.CHEST) {
      if(api.isAItemChest(event.getItemInHand)) {

        val locationOption: Option[Location] = api.getLocationFromStack(event.getItemInHand)
        var location: Location = null

        if(locationOption.isDefined){
          location = locationOption.get
        }else{
          return
        }
        if(api.hasChest(event.getPlayer, location).isEmpty) {
          return
        }
        if(!event.getPlayer.isSneaking) {
          event.setCancelled(true)
          val ievent: PlayerInteractEvent = new PlayerInteractEvent(event.getPlayer, Action.RIGHT_CLICK_AIR, event.getItemInHand, null, null)
          Bukkit.getPluginManager.callEvent(ievent)

        }
      }
    }
  }



  @EventHandler(priority = EventPriority.HIGH)
  def clickListener(event: PlayerInteractEvent): Unit ={

    val handStack = event.getItem//event.getPlayer.getItemInHand
    val api: API = remoteChestPlugin.obtainAPI
    val player: Player = event.getPlayer

    if (handStack != null && handStack.getType != Material.AIR){

      if(event.getClickedBlock != null
        && (event.getClickedBlock.getType == Material.CHEST
        || event.getClickedBlock.getType == Material.TRAPPED_CHEST)) {

        if(event.isCancelled) {
          remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemNaoPodeDefinir(player)
          return
        }
        //Bloco clicado
        val clickedBlock = event.getClickedBlock
        val clickedBlockLocation = clickedBlock.getLocation
        if(handStack.getType == Material.CHEST){
          // Remover bau
          if(event.getAction == Action.LEFT_CLICK_BLOCK) {
            if(api.getChests(player) == 0){
              remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemSemMaisBaus(player)
              return
            }
            val locationOption: Option[Location] = api.hasChest(player, clickedBlockLocation)
            if(locationOption.isDefined) {
              api.removeChest(player, locationOption.get)
              remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemRemoveuBau(player)
              return
            }
            remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
          }

          if(event.getAction == Action.RIGHT_CLICK_BLOCK) {
            if(api.addChest(player, clickedBlockLocation, player.getItemInHand)) {
              remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemDefiniuBau(player)
              event.setCancelled(true)
              return
            }else{
              remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemExcedeuLimite(player)
              remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemRemoverBau(player)
              return
            }
          }
        }
      }

      if(api.isAItemChest(handStack)) {
        if(event.getAction == Action.RIGHT_CLICK_AIR) {
          val locationOption: Option[Location] = api.getLocationFromStack(handStack)
          var location: Location = null

          if(locationOption.isDefined){
            location = locationOption.get
          }else{
            remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
            return
          }

          if(api.hasChest(player, location).isEmpty) {
            remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
            return
          }

          val remoteChestBlock = location.getWorld.getBlockAt(location)
          remoteChestBlock.getState match {
            case chest: Chest =>
              //Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace
              val ievent: PlayerInteractEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), remoteChestBlock, BlockFace.NORTH)
              Bukkit.getPluginManager.callEvent(ievent)
              if(ievent.isCancelled) {
                remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
              }else{
                player.closeInventory()
                player.openInventory(chest.getInventory)
              }
              return
            case _ => remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
          }

        }
      }

    }
  }
}
