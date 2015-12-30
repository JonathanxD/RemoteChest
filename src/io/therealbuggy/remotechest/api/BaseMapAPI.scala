package io.therealbuggy.remotechest.api

import io.therealbuggy.remotechest.util.LocationUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable
import scala.collection.JavaConverters._

/**
  * Created by jonathan on 30/12/15.
  */
abstract class BaseMapAPI extends API {
  val playerChests = mutable.Map[Player, mutable.Set[Location]]()

  override def getChests(player: Player): Int = {
    val option: Option[mutable.Set[Location]] = playerChests.get(player)
    if (option.isDefined) option.get.size else 0
  }

  override def getLocationFromStack(itemStack: ItemStack): Option[Location] = {
    val itemMeta = itemStack.getItemMeta
    val lore = itemMeta.getLore.asScala.toList
    lore.foreach((description) => {
      if(description.startsWith(Statics.remoteChestTag)) {
        return Option.apply(LocationUtil.getLocationAheadText(description, Statics.remoteChestTag))
      }
    })
    Option.empty
  }

  override def removeChest(player: Player, id: Location): Boolean = {
    val chests = getChests(player)
    if (chests > 0) {
      val option: Option[mutable.Set[Location]] = playerChests.get(player)
      option.get -= id

      if (chests - 1 <= 0) {
        // Remover do map para poupar memória
        playerChests.remove(player)
      }

      return true
    }
    false
  }

  override def isAItemChest(itemStack: ItemStack) : Boolean = {
    val itemMeta = itemStack.getItemMeta
    if(!itemMeta.hasLore) return false
    val lore = itemMeta.getLore.asScala.toList
    lore.foreach((description) => {
      if(description.startsWith(Statics.remoteChestTag)) {
        return true
      }
    })
    false
  }

  override def hasChest(player: Player, location: Location) : Option[Location] = {
    if (getChests(player) == 0) return Option.empty
    if (playerChests.get(player).get.contains(location)) {
      return Option.apply(location)
    }

    playerChests.get(player).get.foreach((chestLocation) => {
      if(chestLocation.getWorld.equals(location.getWorld)
        && chestLocation.getBlockX.equals(location.getBlockX)
        && chestLocation.getBlockY.equals(location.getBlockY)
        && chestLocation.getBlockZ.equals(location.getBlockZ)){
        return Option.apply(chestLocation)
      }
    })

    Option.empty
  }

  object Statics {
    val remoteChestTag: String = "[RemoteChest ID]"
  }
}
