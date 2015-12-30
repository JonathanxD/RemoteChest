package io.therealbuggy.remotechest.api

import java.util.UUID

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

/**
  * Created by jonathan on 30/12/15.
  */
trait API {
  /**
    * Obter os baus de um certo jogador
    * @param player Jogador
    * @return Numero de baus
    */
  def getChests(player: Player) : Int

  /**
    * Adicionar um chest
    * @param player Jogador
    * @param locationId Localização
    * @param applyItemStack Item para aplicar as informações
    * @return Se foi adicionar com sucesso retorna true, caso contrario retorna false
    */
  def addChest(player: Player, locationId: Location, applyItemStack: ItemStack) : Boolean

  /**
    * Remove um bau
    * @param player Jogador
    * @param id Localizacao
    * @return True se foi removido, false caso contrario
    */
  def removeChest(player: Player, id: Location) : Boolean

  /**
    * Obtem a informação de localização de uma stack
    * @param itemStack Stack
    * @return Localizacao
    */
  def getLocationFromStack(itemStack: ItemStack) : Option[Location]

  /**
    * Verifica se a itemstack é uma informação de bau
    * @param itemStack Stack
    * @return Se for retorna true, caso contrario retorna false
    */
  def isAItemChest(itemStack: ItemStack) : Boolean

  /**
    * Verifica as coordenadas do Location (não só o location em si)
    * E retorna uma Option das localizações do MAPA e não da informada (só da informada se for exatamente igual ao do MAPA),
    * pois a localização informada pode ser levemente diferente da do mapa (compara mundo, e coordenadas dos blocks)
    * @param player Jogador
    * @param location Localização
    * @return Optional da localização
    */
  def hasChest(player: Player, location: Location) : Option[Location]


  /**
    * Método que deve ser chamado quando um bau for quebrado
    * A implementação deste método deve verificar se há algum baú naquela localização, utilizando loops
    * @param location Localização do bau
    */
  def breakedChest(location: Location) : Unit

  def getChestsAsMap(): mutable.Map[UUID, mutable.Set[Location]]
}
