"""
Read file into texts and calls.
It's ok if you don't understand how to read files
"""
import csv
import re
from collections import namedtuple
from operator import itemgetter


with open('texts.csv', 'r') as f:
    reader = csv.reader(f)
    texts = list(reader)

with open('calls.csv', 'r') as f:
    reader = csv.reader(f)
    calls = list(reader)


TIME_INFO_PATTERN = r'^(?P<day>\d+)-(?P<month>\d+)-(?P<year>\d+)\s+(?P<hour>\d+):(?P<min>\d+):(?P<sec>\d+)$'
TIME_INFO_REGEX = re.compile(TIME_INFO_PATTERN)

TimeInfo = namedtuple('TimeInfo', ['day', 'month', 'year', 'hour', 'min', 'sec'])


"""
TASK 2: Which telephone number spent the longest time on the phone
during the period? Don't forget that time spent answering a call is
also time spent on the phone.
Print a message:
"<telephone number> spent the longest time, <total time> seconds, on the phone during 
September 2016.".
"""


def process_records(acc, records):
    '''
    Accumulate call durations per phone number into a dictionary 
    (key is phone no and value is the call duration).

    Args:
        acc (dict[str,int]): The dictionary accumulating call durations
        records (list[str]): Raw call information from the input CSV file
    '''

    def add_time(acc, phone_no, duration):
        '''Helper: keep adding duration to a phone no. participating in a call.'''
        if acc.get(phone_no):
            acc[phone_no] += int(duration)
        else:
            acc[phone_no] = int(duration)

    def is_sept_2016(time_of_call):
        ''' Helper: check to see of call is in Sept 2016'''
        m = TIME_INFO_REGEX.match(time_of_call)
        if m:
            info = TimeInfo(**m.groupdict())
            return info.year == '2016' and info.month == '09'

        return False

    for record in records:
        if len(record) >= 4:
            src, dst, time_of_call, duration = record
            if is_sept_2016(time_of_call):
                add_time(acc, src, duration)
                add_time(acc, dst, duration)

    return acc

# compute the desired result
result = max(
    ((phone_no, duration) for phone_no, duration in process_records({}, calls).items()), 
    key=itemgetter(1))

# ... and print it
print('{} spent the longest time, {} seconds, on the phone during September 2016.'.format(*result))


