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
TASK 4:
The telephone company want to identify numbers that might be doing
telephone marketing. Create a set of possible telemarketers:
these are numbers that make outgoing calls but never send texts,
receive texts or receive incoming calls.

Print a message:
"These numbers could be telemarketers: "
<list of numbers>
The list of numbers should be print out one per line in lexicographic order with no duplicates.
"""


# accumulate into a set all the numbers that issue calls 
phone_call_issuers = set([record[0] for record in calls if record])

# also accumulate into another set all the numbers that are NOT telemarketers
# (i.e.) those who send/receive texts or receive incoming calls
not_telemarketers = set()

# get all the source/target phone numbers in "texts"
for record in texts:
    if len(record) >= 2:
        not_telemarketers.add(record[0])
        not_telemarketers.add(record[1])

# also get all the numbers from calls that are on the receiving end of a call
for record in calls:
    not_telemarketers.add(record[1])


# compute the difference between the 2 sets
suspected_telemarketers = phone_call_issuers - not_telemarketers
print("These numbers could be telemarketers:")
for phone_no in sorted(suspected_telemarketers):
    print(phone_no)


