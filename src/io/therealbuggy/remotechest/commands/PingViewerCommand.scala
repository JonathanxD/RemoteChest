package io.therealbuggy.remotechest.commands

import io.therealbuggy.remotechest.RemoteChest
import org.bukkit.{ChatColor, Bukkit}
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player

/**
  * Created by jonathan on 20/12/15.
  */
class PingViewerCommand(plugin: RemoteChest) extends CommandExecutor{


  override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
    var outroJogador : Player = null
    if(strings.length > 0){
      outroJogador = Bukkit.getPlayer(strings(0))
      if(outroJogador != null && outroJogador.isOnline){
        plugin.obtainConfig.Mensagem.enviarMensagens(PingReflect.getPing(outroJogador), commandSender, Option.apply(outroJogador))
        return true
      }else{
        commandSender.sendMessage(ChatColor.RED + " Este jogador esta offline!")
        return true
      }
    }
    commandSender.sendMessage(ChatColor.RED + " Voce precisa informar um jogador!")
    true
  }
}
