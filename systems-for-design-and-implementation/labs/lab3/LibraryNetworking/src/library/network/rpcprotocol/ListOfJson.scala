package library.network.rpcprotocol

import java.lang.reflect.{ParameterizedType, Type}
import java.util

class ListOfJson[T](var wrapped: Class[_]) extends ParameterizedType
{
    def getActualTypeArguments: Array[Type] =
    {
        return Array[Type](wrapped)
    }

    def getRawType: Type =
    {
        return classOf[util.List[_]]
    }

    def getOwnerType: Type =
    {
        return null
    }
}
