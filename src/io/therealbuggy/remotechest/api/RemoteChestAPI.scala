package io.therealbuggy.remotechest.api

import io.therealbuggy.remotechest.RemoteChest
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable
import scala.collection.JavaConverters._
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
      playerChests += player.getUniqueId -> mutable.Set.empty
    }

    var storeStack: ItemStack = null
    if(applyItemStack.getAmount > 1){
      storeStack = new ItemStack(applyItemStack.getType, applyItemStack.getAmount-1, applyItemStack.getDurability)
      storeStack.setItemMeta(applyItemStack.getItemMeta)
      storeStack.setData(applyItemStack.getData)
      storeStack.addEnchantments(applyItemStack.getEnchantments)
      applyItemStack.setAmount(1)
    }

    val itemMeta = applyItemStack.getItemMeta

    val locationString: String = Statics.remoteChestTag +
      " " + locationId.getWorld.getName +
      " " + String.valueOf(locationId.getBlockX) +
      " " + String.valueOf(locationId.getBlockY) +
      " " + String.valueOf(locationId.getBlockZ)

    val lore = mutable.MutableList[String]()
    lore += locationString

    itemMeta.setLore(lore.asJava)

    applyItemStack.setItemMeta(itemMeta)

    playerChests.get(player.getUniqueId).get.add(locationId)
    if(storeStack != null){
      val out: Map[Integer, ItemStack] = player.getInventory.addItem(storeStack).asScala.toMap
      if(out.nonEmpty){
        out.values.foreach((stack) => {
          player.getWorld.dropItemNaturally(player.getLocation.add(0, 1, 0), stack)
        })

      }
    }
    true
  }

}
