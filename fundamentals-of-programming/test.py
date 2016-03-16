def is_prime(n):
    if n < 2:
        return False

    for i in range(2, n / 2 + 1):
        if n % i == 0:
            return False

    return True


def primes_iterative(li):
    counter = 0
    for item in li:
        if is_prime(item):
            counter += 1

    return counter


def primes_recursive(li):
    len_li = len(li)

    # base case
    if len_li == 1:
        return int(is_prime(li[0]))

    middle = int(len_li / 2)

    # left hand side
    return primes_recursive(li[:middle]) + \
           primes_recursive(li[middle:])
    # right hand side


print(is_prime(5))
list_of_primes = range(10)
print("Number of primes iterative: ", primes_iterative(list_of_primes))
print("Number of primes recursive: ", primes_recursive(list_of_primes))
