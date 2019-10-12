package library.network.rpcprotocol

import java.io.Reader
import java.util

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

class JsonSerialization private[rpcprotocol]
{
    private val gson: Gson = new Gson

    def toJSON[T](src: T, `type`: Class[T]): String =
    {
        gson.toJson(src, `type`)
    }

    def fromJSON[T](json: Reader, `type`: Class[T]): T =
    {
        gson.fromJson(json, `type`)
    }

    def fromJSON[T](json: String, `type`: Class[T]): T =
    {
        gson.fromJson(json, `type`)
    }

    def convertToCustomObject[T](`object`: Any, clazz: Class[T]): T =
    {
        val json: String = toJSON(`object`.asInstanceOf[LinkedTreeMap[_, _]], classOf[LinkedTreeMap[_, _]])
        fromJSON(json, clazz)
    }

    def convertToCustomObject[T](json: String, clazz: Class[T]): T =
    {
        fromJSON(json, clazz)
    }

    def convertToCustomList[T](`object`: Any, `type`: Class[T]): util.List[T] =
    {
        val json: String = toJSON(`object`.asInstanceOf[LinkedTreeMap[_, _]], classOf[LinkedTreeMap[_, _]])
        gson.fromJson(json, new ListOfJson[T](`type`))
    }

    def convertToCustomList[T](json: String, `type`: Class[T]): util.List[T] =
    {
        gson.fromJson(json, new ListOfJson[T](`type`))
    }
}