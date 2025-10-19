import threading
import random
import time


def create_threads(threads_quantity: int):
    for i in range(threads_quantity):
        thread = threading.Thread(target=foo(numbers=numbers, index=i))
        thread.start
        thread.join


def foo(numbers, index):
    rand_number = random.randint(0, 10)
    print(rand_number)
    time.sleep(rand_number)
    numbers[index] = rand_number


def sum(list):
    sum = 0
    for i in range(len(list)):
        sum += list[i]
    return sum


threads_quantity = int(input())
numbers = [0] * threads_quantity
create_threads(threads_quantity)
print(sum(numbers))
