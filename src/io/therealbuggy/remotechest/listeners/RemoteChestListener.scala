package io.therealbuggy.remotechest.listeners

import io.therealbuggy.remotechest.RemoteChest
import io.therealbuggy.remotechest.api.API
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.{Material, Location}
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}


/**
  * Created by jonathan on 30/12/15.
  */
class RemoteChestListener(remoteChestPlugin: RemoteChest) extends Listener {
  @EventHandler
  def clickListener(event: PlayerInteractEvent): Unit ={
    val handStack = event.getPlayer.getItemInHand
    val api: API = remoteChestPlugin.obtainAPI
    val player: Player = event.getPlayer

    if (handStack != null && handStack.getType != Material.AIR){

      if(event.getClickedBlock != null
        && event.getClickedBlock.getType == Material.CHEST) {

        //Bloco clicado
        val clickedBlock = event.getClickedBlock
        val clickedBlockLocation = clickedBlock.getLocation

        // Remover bau
        if(event.getAction == Action.LEFT_CLICK_BLOCK) {
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
            return
          }
          remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemNaoPodeDefinir(player)
        }
      }

      if(api.isAItemChest(handStack)) {
        if(event.getAction == Action.RIGHT_CLICK_AIR) {
          val locationOption: Option[Location] = api.getLocationFromStack(handStack)
          var location: Location = null

          if(locationOption.isDefined){
            location = locationOption.get
            remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
          }else{
            return
          }

          if(api.hasChest(player, location).isEmpty) {
            remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
            return
          }

          val remoteChestBlock = location.getWorld.getBlockAt(location)
          remoteChestBlock.getState match {
            case chest: Chest =>
              player.openInventory(chest.getInventory);
            case _ => remoteChestPlugin.obtainConfig.Mensagem.enviarMensagemBauNaoEncontrado(player)
          }

        }
      }

    }
  }
}
