package library.network.rpcprotocol

object RequestType extends Enumeration
{
    type RequestType = Value
    val LOGIN, LOGOUT, SEND_MESSAGE, GET_BOOKS_AVAILABLE, GET_BORROWED_BY, GET_ALL_LOANS, SEARCH_TITLE, BORROW_BOOK, RETURN_BOOK = Value
}
