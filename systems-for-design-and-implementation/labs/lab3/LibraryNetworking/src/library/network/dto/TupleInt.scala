package library.network.dto

import java.io.Serializable

// TODO use scala tuples. sigh
class TupleInt(var first: Int, var second: Int) extends Serializable
{
    def getFirst: Int =
    {
        first
    }

    def setFirst(first: Int)
    {
        this.first = first
    }

    def getSecond: Int =
    {
        second
    }

    def setSecond(second: Int)
    {
        this.second = second
    }

    override def toString: String =
    {
        "TupleInt{" + "first=" + first + ", second=" + second + '}'
    }
}
