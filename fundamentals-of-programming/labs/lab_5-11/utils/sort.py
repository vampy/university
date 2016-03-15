def search(lst, searchValue, key):
    for item in lst:
        method = getattr(item, key)
        if method() == searchValue:
            return item


def partition(l, left, right):
    pivot = l[left]
    i = left
    j = right
    while i != j:
        while l[j] >= pivot and i < j:
            j = j - 1
        l[i] = l[j]
        while l[i] <= pivot and i < j:
            i = i + 1
        l[j] = l[i]
    l[i] = pivot
    return i


def quickSortRec(l, left, right):
    # partition the list
    pos = partition(l, left, right)
    # order the left part
    if left < pos - 1: quickSortRec(l, left, pos - 1)
    # order the right part
    if pos + 1 < right: quickSortRec(l, pos + 1, right)


def gnomesort(lst):
    pos = 0
    while True:
        if pos == 0:
            pos += 1
        if pos >= len(lst):
            break
        if lst[pos] >= lst[pos - 1]:
            pos += 1
        else:
            lst[pos - 1], lst[pos] = lst[pos], lst[pos - 1]
            pos -= 1


def linear(iterable, search_value, key):
    """
    Perform a linear search on an iterable object
    Input:
        iterable - the search list
        search_value - the value to find
        key - the key to compare on
    Output:
        The list of items matching the search on
    """
    return_list = []

    for i in range(len(iterable)):
        method = getattr(iterable[i], key)
        if str(type(method)) == "<type 'method-wrapper'>" or str(
                type(method)) == "<type 'instancemethod'>":  # is a method
            attribute = method()
        else:  # is a simple attribute
            attribute = method
        if attribute == search_value:
            return_list.append(iterable[i])

    return return_list
    # print(linear(pers,1, key="getPersonId"))

# from repository.fileclient import FileClientRepository
#
# client_repository = FileClientRepository("clients_new.dat")
#
# all_clients = client_repository.select_all()
# quickSortRec(all_clients, 0, len(all_clients) - 1)
#
# for client in all_clients:
#     print(client)
