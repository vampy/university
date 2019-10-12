package library.network.rpcprotocol

object ResponseType extends Enumeration
{
    type ResponseType = Value
    val OK, ERROR, GET_BOOKS_AVAILABLE, GET_BORROWED_BY, NEW_MESSAGE, SEARCH_TITLE, GET_ALL_LOANS, BORROW_BOOK, UPDATE_BOOK, UPDATE_LOAN, RETURN_BOOK = Value
}