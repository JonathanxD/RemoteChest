package io.therealbuggy.remotechest.configuration

import io.therealbuggy.remotechest.util.{ConfigurationUtil, BooleanUtil}
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import scala.collection.JavaConverters._

import scala.collection.immutable.TreeMap

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

class Configuration(fileConfiguration: FileConfiguration) {


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
      //mensagem.replace("$baus", API.getBaus(player))
      mensagem = mensagem.replace("$maximo", String.valueOf(ConfigAPI.getMaximo(player)));
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
