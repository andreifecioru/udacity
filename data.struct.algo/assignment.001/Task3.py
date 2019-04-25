import re
from collections import namedtuple

FIXED_LINE_PATTERN = r'^\((?P<code>\d+)\)(\d+)$'
FIXED_LINE_REGEX = re.compile(FIXED_LINE_PATTERN)

MOBILE_PATTERN = r'^(?P<code>\d{4})(\d+)\s+(\d+)$'
MOBILE_REGEX = re.compile(MOBILE_PATTERN)

TELEMARK_PATTERN = r'^(?P<code>140)(\d+)$'
TELEMARK_REGEX = re.compile(TELEMARK_PATTERN)

PhoneNo = namedtuple('PhoneNo', ['code', 'number', 'type'])


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
TASK 3:
(080) is the area code for fixed line telephones in Bangalore.
Fixed line numbers include parentheses, so Bangalore numbers
have the form (080)xxxxxxx.)

Part A: Find all of the area codes and mobile prefixes called by people
in Bangalore.
 - Fixed lines start with an area code enclosed in brackets. The area
   codes vary in length but always begin with 0.
 - Mobile numbers have no parentheses, but have a space in the middle
   of the number to help readability. The prefix of a mobile number
   is its first four digits, and they always start with 7, 8 or 9.
 - Telemarketers' numbers have no parentheses or space, but they start
   with the area code 140.

Print the answer as part of a message:
"The numbers called by people in Bangalore have codes:"
 <list of codes>
The list of codes should be print out one per line in lexicographic order with no duplicates.

Part B: What percentage of calls from fixed lines in Bangalore are made
to fixed lines also in Bangalore? In other words, of all the calls made
from a number starting with "(080)", what percentage of these calls
were made to a number also starting with "(080)"?

Print the answer as a part of a message::
"<percentage> percent of calls from fixed lines in Bangalore are calls
to other fixed lines in Bangalore."
The percentage should have 2 decimal digits
"""


def parse_call_data(calls):
    '''
    Parse the raw call data as extracted from the input CSV file.

    Args:
        calls (list[str]): Raw call information from the input CSV file
    Returns
        list[PhoneNo]: List of PhoneNo instances.
    '''

    def parse_phone_no(phone_no):
        '''Helper: parse a phone number into an instance of PhoneNo.'''
        m = FIXED_LINE_REGEX.match(phone_no)
        if m:
            return PhoneNo(code=m.groupdict()['code'], number=phone_no, type='fixed_line')

        m = MOBILE_REGEX.match(phone_no)
        if m:
            return PhoneNo(code=m.groupdict()['code'], number=phone_no, type='mobile')

        m = TELEMARK_REGEX.match(phone_no)
        if m:
            return PhoneNo(code=m.groupdict()['code'], number=phone_no, type='telemarketer')

        print('Invalid phone number: {}'.format(phone_no))
        return None

    parsed_data = ((lambda data: (parse_phone_no(data[0]), parse_phone_no(data[1])))(call_data) 
                    for call_data in calls if len(call_data) >= 2)

    # filter-out invalid entries 
    # NOTE: we materialize the genex above into a list 
    # because we'll be making multiple passes through it
    return [data for data in parsed_data if data[0] and data[1]]


# parse the raw call data
parsed_calls = parse_call_data(calls) 

# perform queries on parsed data

# --------------- [PART A]-----------------
# get all the calls originating from Bangalore (fixed line)
calls_from_bangalore = [call_info for call_info in parsed_calls 
                        if call_info[0].type == 'fixed_line' and call_info[0].code == '080']

# accumulate the list pf codes into a set (avoid duplicates)
codes = set()
for call in calls_from_bangalore:
    codes.add(call[1].code)

# print the final output (also sort the codes lexicographically)
print('The numbers called by people in Bangalore have codes:')
for code in sorted(codes):
    print(code)

# --------------- [PART B]-----------------
def is_bangalore_call(call_info):
    return call_info[0].type == 'fixed_line' and call_info[0].code == '080' and \
           call_info[1].type == 'fixed_line' and call_info[1].code == '080'

# get all the calls from Bangalore to Bangalore
bangalore_calls = [call_info for call_info in parsed_calls if is_bangalore_call(call_info)]

# print the final output
print('{:2.2f} percent of calls from fixed lines in Bangalore are calls to other fixed lines in Bangalore.'
        .format(100 * len(bangalore_calls) / len(calls_from_bangalore)))

