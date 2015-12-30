package io.therealbuggy.remotechest.util

/**
  * Created by jonathan on 20/12/15.
  */
object ConfigurationUtil {

  def replaceColors(text: String) : String = {
    text.replaceAll("(&([a-fk-or0-9]))", "§$2")
  }

}
