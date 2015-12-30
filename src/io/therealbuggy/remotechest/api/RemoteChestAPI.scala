package io.therealbuggy.remotechest.api

import io.therealbuggy.remotechest.RemoteChest
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

/**
  * Created by jonathan on 30/12/15.
  */
class RemoteChestAPI(remoteChestPlugin: RemoteChest) extends BaseMapAPI{
  /**
    * O mesmo que o API faz, porém, neste caso, somente irá adicionar se houver slots disponiveis
    * @see API
    * @param player Jogador
    * @param locationId Localização
    * @param applyItemStack Item para aplicar as informações
    * @return Se foi adicionar com sucesso retorna true, caso contrario retorna false
    */
  override def addChest(player: Player, locationId: Location, applyItemStack: ItemStack): Boolean = {
    val chests: Int = getChests(player)
    if (chests >= remoteChestPlugin.obtainConfig.ConfigAPI.getMaximo(player)) {
      return false
    }

    if (chests == 0) {
      playerChests += player -> mutable.Set.empty
    }

    val itemMeta = applyItemStack.getItemMeta

    val locationString: String = Statics.remoteChestTag +
      " " + locationId.getWorld.getName +
      " " + String.valueOf(locationId.getBlockX) +
      " " + String.valueOf(locationId.getBlockY) +
      " " + String.valueOf(locationId.getBlockZ)

    itemMeta.getLore.set(0, locationString)
    applyItemStack.setItemMeta(itemMeta)

    playerChests.get(player).get.add(locationId)

    true
  }
}
