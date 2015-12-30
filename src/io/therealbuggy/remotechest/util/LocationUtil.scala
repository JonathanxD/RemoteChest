package io.therealbuggy.remotechest.util

import org.bukkit.{Bukkit, Location}

/**
  * Created by jonathan on 30/12/15.
  */
object LocationUtil {
  def getLocationAheadText(input: String, textStart: String): Location = {
    val text = input.substring(textStart.length+1, input.length)
    val texts = text.split(" ")
    new Location(Bukkit.getWorld(texts(0)), texts(1).toDouble, texts(2).toDouble, texts(3).toDouble)
  }

  def getLocationBehindText(input: String, textEnd: String): Location = {
    val in = input.indexOf(textEnd) -1
    val text = input.substring(0, in)
    val texts = text.split(" ")
    new Location(Bukkit.getWorld(texts(0)), texts(1).toDouble, texts(2).toDouble, texts(3).toDouble)
  }
}
