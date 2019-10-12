package library.network.rpcprotocol

import java.io.Serializable

import library.network.rpcprotocol.ResponseType.ResponseType

object Response
{

    class Builder
    {
        private val response: Response = new Response

        def `type`(`type`: String): Response.Builder =
        {
            response.setType(`type`)
            this
        }

        def data(data: Any): Response.Builder =
        {
            response.setData(data)
            this
        }

        def build: Response =
        {
            response
        }
    }

}

class Response private extends Serializable
{
    private var `type`: String = null
    private var data: Any = null

    def getType: String =
    {
        `type`
    }

    private def setType(`type`: String)
    {
        this.`type` = `type`
    }

    def getData: Any =
    {
        data
    }

    private def setData(data: Any)
    {
        this.data = data
    }

    override def toString: String =
    {
        "Response{" + "setType='" + `type` + '\'' + ", getData='" + data + '\'' + '}'
    }
}
