import math

def isPrime(n):
    # Validate number
    if n <= 1:
        return False
    
    limit = int(math.sqrt(n)) + 1
    for i in range(2, limit):
        if n % i == 0:
            return False
    
    # The number is prime
    return True

print isPrime(int(raw_input("Check if number is prime: ")))