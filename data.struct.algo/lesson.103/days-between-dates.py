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
    if year % 4 == 0:
        return DAYS_IN_MONTHS_LEAP_YEAR
    return DAYS_IN_MONTHS

def days_in_year(year):
    return sum(days_in_months(year))

def days_till_eom(year, month, day):
    return days_in_months(year)[month - 1] - day

def days_till_eoy(year, month, day):
    return days_till_eom(year, month, day) + sum(days_in_months(year)[month:])

def days_since_soy(year, month, day):
    return sum(days_in_months(year)[:month - 1]) + day


def check_inputs(year1, month1, day1, year2, month2, day2):
    def check_day(year, month, day):
        return days_in_months(year)[month - 1] >= day

    if year1 > year2:
        return False
    
    if year1 == year2:
        if month1 > month2:
            return False
        if month1 == month2 and day1 > day2:
            return False

    return check_day(year1, month1, day1) and check_day(year2, month2, day2)


def daysBetweenDates(year1, month1, day1, year2, month2, day2):
    """
    Calculates the number of days between two dates.
    """

    if not check_inputs(year1, month1, day1, year2, month2, day2):
        raise Exception("Invalid inputs.")

    if year1 == year2:
        if month1 == month2:
            return day2 - day1
        return days_till_eom(month1) + sum(days_in_months(year1)[month1:month2]) + day2

    return sum(days_in_year(year) for year in range(year1 + 1, year2)) + \
        days_till_eoy(year1, month1, day1) + \
        days_since_soy(year2, month2, day2)

    
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
    
    print(daysBetweenDates(2012, 6, 29,
                            2013, 6, 31))
    
    print("Congratulations! Your daysBetweenDates")
    print("function is working correctly!")

    print(daysBetweenDates(1981, 6, 24, 2019, 4, 24))
    
