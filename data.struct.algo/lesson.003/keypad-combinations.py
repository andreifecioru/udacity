#!/usr/bin/env python
def get_characters(num):
    if num == 2:
        return "abc"
    elif num == 3:
        return "def"
    elif num == 4:
        return "ghi"
    elif num == 5:
        return "jkl"
    elif num == 6:
        return "mno"
    elif num == 7:
        return "pqrs"
    elif num == 8:
        return "tuv"
    elif num == 9:
        return "wxyz"
    else:
        return ""


def keypad(num):
    digits = str(num)

    if len(digits) == 1:
        return list(get_characters(int(digits[0]))) or ['']

    first_digit_letters = list(get_characters(int(digits[0])))
    if first_digit_letters:
        ret_val = []
        result = keypad(int(digits[1:]))
        for letter in first_digit_letters:
            for combination in result:
                ret_val.append(letter + combination)
        return ret_val
    else:
        return keypad(int(digits[1:]))
    

def test_keypad(input, expected_output):
    if sorted(keypad(input)) == expected_output:
        print("Yay. We got it right.")
    else:
        print("Oops! That was incorrect.")
        print(sorted(keypad(input)))


if __name__ == '__main__':
    # Base case: list with empty string
    input = 0
    expected_output = [""]
    test_keypad(input, expected_output)

    # Example case
    input = 23
    expected_output = sorted(["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"])
    test_keypad(input, expected_output)

    # Example case
    input = 32
    expected_output = sorted(["da", "db", "dc", "ea", "eb", "ec", "fa", "fb", "fc"])
    test_keypad(input, expected_output)

    # Example case
    input = 8
    expected_output = sorted(["t", "u", "v"])
    test_keypad(input, expected_output)

    input = 354
    expected_output = sorted(["djg", "ejg", "fjg", "dkg", "ekg", "fkg", "dlg", "elg", "flg", "djh", "ejh", "fjh", "dkh", "ekh", "fkh", "dlh", "elh", "flh", "dji", "eji", "fji", "dki", "eki", "fki", "dli", "eli", "fli"])
    test_keypad(input, expected_output)
