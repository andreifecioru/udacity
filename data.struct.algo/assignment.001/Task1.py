"""
Read file into texts and calls.
It's ok if you don't understand how to read files.
"""
import csv
with open('texts.csv', 'r') as f:
    reader = csv.reader(f)
    texts = list(reader)

with open('calls.csv', 'r') as f:
    reader = csv.reader(f)
    calls = list(reader)


"""
TASK 1:
How many different telephone numbers are there in the records? 
Print a message:
"There are <count> different telephone numbers in the records."
"""


def process_records(acc_set, records):
    '''
    Accumulate telephone numbers in a set.

    Args:
        acc_set (set[str]): The set where tel. numbers are accumulated
        records (list[str]): The list of records to be processed. In all
                             cases, the first 2 elements are tel. numbers

    Returns:
        set[str]: The accumulated set.
    '''
    for record in records:
        if len(record) >= 2:
            acc_set.add(record[0])
            acc_set.add(record[1])
    return acc_set


# Second, accumulate the telephone numbers from "calls" into the same set
tel_no_set = process_records(
    # First, accumulate the telephone numbers from "texts" into an empty set
    process_records(set(), texts),
    calls)

print('There are {} different telephone numbers in the records.'.format(len(tel_no_set)))
