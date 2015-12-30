package io.therealbuggy.remotechest.configuration

import io.therealbuggy.remotechest.RemoteChest
import io.therealbuggy.remotechest.util.{ConfigurationUtil, BooleanUtil}
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import scala.collection.JavaConverters._

/**
  * Created by jonathan on 20/12/15.
  */
object Secoes {
  val secaoPrincipal = "remotechest"
  val quantidadePorJogador = secaoPrincipal+".quantidadePorJogador"
  val ilimitadoParaOps = secaoPrincipal+".ilimitadoParaOps"
  val ilimitadoParaPermissoes = secaoPrincipal+".ilimitadoParaPermissoes"
  val secaoMensagens = secaoPrincipal+".mensagens"
  val mensagemDefiniuBau = secaoMensagens+".definiuBau"
  val mensagemExcedeuLimite = secaoMensagens+".excedeuLimite"
  val mensagemRemoveuBau = secaoMensagens+".removeuBau"
  val mensagemRemoverBau = secaoMensagens+".removerBau"
  val mensagemNaoPodeDefinir = secaoMensagens+".naoPodeDefinir"
  val mensagemBauNaoEncontrado = secaoMensagens+".bauNaoEncontrado"
}

class Configuration(remoteChestPlugin: RemoteChest, fileConfiguration: FileConfiguration) {


  val quantidadePorJogador: Int = fileConfiguration.getInt(Secoes.quantidadePorJogador)
  val ilimitadoParaOps: Boolean = BooleanUtil.booleanFromString(fileConfiguration.getString(Secoes.ilimitadoParaOps))
  val ilimitadoParaPermissoes: List[String] = fileConfiguration.getStringList(Secoes.ilimitadoParaPermissoes).asScala.toList

  object Mensagem {
    val mensagemDefiniuBau = fileConfiguration.getString(Secoes.mensagemDefiniuBau)

    val mensagemExcedeuLimite = fileConfiguration.getString(Secoes.mensagemExcedeuLimite)
    val mensagemRemoveuBau = fileConfiguration.getString(Secoes.mensagemRemoveuBau)
    val mensagemRemoverBau = fileConfiguration.getString(Secoes.mensagemRemoverBau)

    val mensagemNaoPodeDefinir = fileConfiguration.getString(Secoes.mensagemNaoPodeDefinir)
    val mensagemBauNaoEncontrado = fileConfiguration.getString(Secoes.mensagemBauNaoEncontrado)

    def enviarMensagemDefiniuBau(player: Player): Unit = {
      var mensagem = ConfigurationUtil.replaceColors(mensagemDefiniuBau)
      mensagem.replace("$baus", String.valueOf(remoteChestPlugin.obtainAPI.getChests(player)))
      mensagem = mensagem.replace("$maximo", String.valueOf(ConfigAPI.getMaximo(player)))
      player.sendMessage(mensagem)
    }

    def enviarMensagemRemoveuBau(player: Player): Unit = {
      var mensagem = ConfigurationUtil.replaceColors(mensagemRemoveuBau)
      mensagem.replace("$baus", String.valueOf(remoteChestPlugin.obtainAPI.getChests(player)))
      mensagem = mensagem.replace("$maximo", String.valueOf(ConfigAPI.getMaximo(player)))
      player.sendMessage(mensagem)
    }

    def enviarMensagemExcedeuLimite(player: Player): Unit = {
      val mensagem = ConfigurationUtil.replaceColors(mensagemExcedeuLimite)
      player.sendMessage(mensagem)
    }

    def enviarMensagemRemoverBau(player: Player): Unit = {
      val mensagem = ConfigurationUtil.replaceColors(mensagemRemoverBau)
      player.sendMessage(mensagem)
    }

    def enviarMensagemNaoPodeDefinir(player: Player): Unit = {
      val mensagem = ConfigurationUtil.replaceColors(mensagemNaoPodeDefinir)
      player.sendMessage(mensagem)
    }

    def enviarMensagemBauNaoEncontrado(player: Player): Unit = {
      val mensagem = ConfigurationUtil.replaceColors(mensagemBauNaoEncontrado)
      player.sendMessage(mensagem)
    }



  }

  object ConfigAPI {
    def getMaximo(player: Player) : Int = {
      if(ilimitadoParaOps && player.isOp) {
        return Integer.MAX_VALUE
      }

      ilimitadoParaPermissoes.foreach((permissao) => {
        if (player.hasPermission(permissao)) {
          return Integer.MAX_VALUE
        }
      })

      quantidadePorJogador
    }
  }
}
