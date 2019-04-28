#!/usr/bin/env python

DAYS_IN_MONTHS = [
    31, # Jan
    28, # Feb
    31, # Mar
    30, # Apr
    31, # May
    30, # Jun
    31, # Jul
    31, # Aug
    30, # Sep
    31, # Oct
    30, # Nov
    31, # Dec
]

DAYS_IN_MONTHS_LEAP_YEAR = [
    31, # Jan
    29, # Feb
    31, # Mar
    30, # Apr
    31, # May
    30, # Jun
    31, # Jul
    31, # Aug
    30, # Sep
    31, # Oct
    30, # Nov
    31, # Dec
]

def days_in_months(year):
    if year % 4 == 0 and not year % 100 == 0:
        return DAYS_IN_MONTHS_LEAP_YEAR
    return DAYS_IN_MONTHS


def increment_date(year, month, day):
    days_in_current_month = days_in_months(year)[month - 1]

    if day < days_in_current_month:
        day += 1
    elif month < 12:
        day = 1
        month += 1
    else:
        day = 1
        month = 1
        year += 1
    return (year, month, day)


def is_date_before(year1, month1, day1, year2, month2, day2):
    if year1 > year2:
        return False
    
    if year1 == year2:
        if month1 > month2:
            return False
        if month1 == month2 and day1 >= day2:
            return False
    return True


def check_inputs(year1, month1, day1, year2, month2, day2):
    def check_day(year, month, day):
        return days_in_months(year)[month - 1] >= day

    return check_day(year1, month1, day1) and \
        check_day(year2, month2, day2) and \
        not is_date_before(year2, month2, day2, year1, month1, day1)


def daysBetweenDates(year1, month1, day1, year2, month2, day2):
    """
    Calculates the number of days between two dates.
    """

    if not check_inputs(year1, month1, day1, year2, month2, day2):
        raise Exception("Invalid inputs.")

    day_count = 0
    date = (year1, month1, day1)
    while is_date_before(*date, year2, month2, day2):
        date = increment_date(*date)
        day_count += 1

    return day_count


if __name__ == '__main__':
    # test same day
    assert(daysBetweenDates(2017, 12, 30,
                            2017, 12, 30) == 0)
    # test adjacent days
    assert(daysBetweenDates(2017, 12, 30, 
                            2017, 12, 31) == 1)

    # test new year
    assert(daysBetweenDates(2017, 12, 30, 
                            2018, 1,  1)  == 2)
    # test full year difference
    assert(daysBetweenDates(2012, 6, 29,
                            2013, 6, 29)  == 365)
    
    
    print("Congratulations! Your daysBetweenDates")
    print("function is working correctly!")

    print(daysBetweenDates(1900, 1, 1, 1999, 12, 31))
    