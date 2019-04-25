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
TASK 0:
What is the first record of texts and what is the last record of calls?
Print messages:
"First record of texts, <incoming number> texts <answering number> at time <time>"
"Last record of calls, <incoming number> calls <answering number> at time <time>, lasting <during> seconds"
"""


if texts:
    # Select the 1st text record
    text = texts[0]
    if (len(text) >= 3):
        print('First record of texts, {} texts {} at time {}'.format(*text))

if calls:
    # Select the last call record
    call = calls[-1]
    if (len(call) >= 4):
        print('Last record of calls, {} calls {} at time {}, lasting {} seconds.'.format(*call))

