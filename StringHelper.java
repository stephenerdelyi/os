import java.util.Random;
import java.io.File;

///////////////////
// StringHelper: contains many helper fuctions for strings used throughout portions of the program
///////////////////
public class StringHelper extends OS {
    //splitOnDelimeter - split the input string into an array based on the delimiter
    public String[] splitOnDelimeter(String inputString, String delimiter) {
        String[] data = inputString.split(delimiter);
        return data;
    }

    //substringIsInString - check to see if the given sub string exists within the input string
    public boolean substringIsInString(String inputString, String subString) {
        if (inputString.toLowerCase().contains(subString.toLowerCase())) {
            return true;
        }
        return false;
    }

    //findTokenIndexInArray - loops through the input array to see if the token to find exists within it
    public int findTokenIndexInArray(String[] inputArray, String tokenToFind) {
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i].contains("|")) {
                String[] splitSubArray = splitOnDelimeter(inputArray[i], "\\|");
                for (int j = 0; j < splitSubArray.length; j++) {
                    if (splitSubArray[j].equals(tokenToFind)) {
                        return i;
                    }
                }
            } else {
                if (inputArray[i].equals(tokenToFind)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public String returnOrdinal(int inputNum) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (inputNum % 100) {
            case 11:
            case 12:
            case 13:
                return inputNum + "th";
            default:
                return inputNum + sufixes[inputNum % 10];
        }
    }

    //removeBadWhitespace - removes all white space except for that which descriptor tokens are found
    public String removeBadWhitespace(String inputString, String[] validKeys) {
        //first remove all white space from the line
        inputString = inputString.replaceAll("[\\s]*","");

        //loop through all valid keys
        for (int i = 0; i < validKeys.length; i++) {
            //if the valid key even contains a space
            if (substringIsInString(validKeys[i], " ")) {
                String keyWithSpace = validKeys[i];
                String keyWithoutSpace = validKeys[i].replaceAll(" ", "");
                //if the key is present in the string without spaces
                if (substringIsInString(inputString, keyWithoutSpace)) {
                    //replace with spaces
                    inputString = inputString.replaceAll(keyWithoutSpace, keyWithSpace);
                }
            }
        }

        return inputString;
    }

    public String makeFakeHex() {
        String returnString = "0x";
        Random rand = new Random();

        for (int i = 0; i < 8; i++) {
            int randomN = rand.nextInt(15);

            if (randomN == 10) {
                returnString = returnString + "A";
            } else if (randomN == 11) {
                returnString = returnString + "B";
            } else if (randomN == 12) {
                returnString = returnString + "C";
            } else if (randomN == 13) {
                returnString = returnString + "D";
            } else if (randomN == 14) {
                returnString = returnString + "E";
            } else if (randomN == 15) {
                returnString = returnString + "F";
            } else {
                returnString = returnString + Integer.toString(randomN);
            }
        }

        return returnString;
    }
}
