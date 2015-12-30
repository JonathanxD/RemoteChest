package io.therealbuggy.remotechest.saver

import java.io._
import java.util.UUID

import io.therealbuggy.remotechest.api.API
import org.bukkit.{Bukkit, Location}

import scala.collection.mutable
import util.control.Breaks._

/**
  * Created by jonathan on 30/12/15.
  */
class EndState extends Serializable{}

class SaveLoad(api: API, file: File) {
  def load(): Int ={
    this.synchronized {
      if(!file.exists()) {
        save()
        return 0
      }
      val objectInputStream: ObjectInputStream = new ObjectInputStream(new FileInputStream(file))
      val map : mutable.Map[UUID, mutable.Set[Location]] = mutable.Map[UUID, mutable.Set[Location]]()

      var chests: Int = 0
      var obj: AnyRef = objectInputStream.readObject()
      breakable {
        while(true) {
          obj match {
            case uuid: UUID =>
              val set: mutable.Set[Location] = mutable.Set.empty
              map += uuid -> set

              breakable {
                while (true) {
                  obj = objectInputStream.readObject()
                  obj match {
                    case world: String =>
                      val x: Int = objectInputStream.readInt()
                      val y: Int = objectInputStream.readInt()
                      val z: Int = objectInputStream.readInt()
                      set += new Location(Bukkit.getWorld(world), x.asInstanceOf[Double], y.asInstanceOf[Double], z.asInstanceOf[Double])
                      chests += 1
                    case _ =>
                      break
                  }
                }
              }
            case _ =>
              break
          }
        }
      }

      api.getChestsAsMap() ++= map
      objectInputStream.close()
      return chests
    }
    0
  }

  def save(): Unit ={
    this.synchronized {
      if(file.exists()) {
        file.delete()
        file.createNewFile()
      }
      val objectOutputStream: ObjectOutputStream = new ObjectOutputStream(new FileOutputStream(file))
      val map : mutable.Map[UUID, mutable.Set[Location]] = api.getChestsAsMap()
      map.foreach{
        case(uuid, locations) =>
          if(locations.nonEmpty){
            objectOutputStream.writeObject(uuid)
            locations.foreach((location) => {
              objectOutputStream.writeObject(location.getWorld.getName)
              objectOutputStream.writeInt(location.getBlockX)
              objectOutputStream.writeInt(location.getBlockY)
              objectOutputStream.writeInt(location.getBlockZ)
            })
          }
      }
      objectOutputStream.writeObject(new EndState)
      objectOutputStream.flush()
      objectOutputStream.close()
    }
  }

}
