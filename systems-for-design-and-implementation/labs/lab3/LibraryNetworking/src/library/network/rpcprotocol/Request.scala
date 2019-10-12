package library.network.rpcprotocol

import java.io.Serializable

object Request
{
    class Builder
    {
        private val request: Request = new Request

        def `type`(`type`: String): Request.Builder =
        {
            request.setType(`type`)
            this
        }

        def data(data: Any): Request.Builder =
        {
            request.setData(data)
            this
        }

        def build: Request =
        {
            request
        }
    }
}

class Request private extends Serializable
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
        "Request{" + "getType='" + `type` + '\'' + ", getData='" + data + '\'' + '}'
    }
}
