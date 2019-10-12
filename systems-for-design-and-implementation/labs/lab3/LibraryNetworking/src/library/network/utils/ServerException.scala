package library.network.utils

// https://stackoverflow.com/questions/10925268/define-your-own-exceptions-with-overloaded-constructors-in-scala
class ServerException(message: String = null, cause: Throwable = null) extends Exception(message, cause)
{
}
