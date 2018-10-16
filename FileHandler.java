///////////////////
// FileHandler: handles all file processing (excluding reading/writing)
///////////////////
public class FileHandler extends OS {
    ReadFile fileReader = new ReadFile();
    WriteFile fileWriter = new WriteFile();

    //verifyConfigFile - verify the configuration file
    public boolean verifyConfigFile() {
        fileReader.read(config.fileName); //actually read the config file and store it in the class's string var
        String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n"); //array of strings containing each line read in from the config file
        String[] configTokenHistory = new String[validKeys.configKeyDeclarations.length]; //string array that is used to determine if there are too many/few assignments of a given config declaration
        System.arraycopy(validKeys.configKeyDeclarations, 0, configTokenHistory, 0, validKeys.configKeyDeclarations.length); //make a second copy of configKeyDeclarations tocd configTokenHistory since we need two working arrays to accomplish error checking
        int lineNum = 0; //used for error logging

        //verify the config file extension is valid
        String fileExtension = stringHelper.splitOnDelimeter(config.fileName, "\\.")[1];
        if (!fileExtension.equals("conf")) {
            console.error("Config file does not end in .conf");
        }

        //loop through each line in configuration file
        for (int i = 0; i < splitByLineBreak.length; i++) {
            lineNum++;
            //if the current line is not a comment
            if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], ":[\\s]*"); //array of 2 cells - 1st is the left hand side of a config line (key), 2nd is the right hand side of a config line (value)
                int returnedIndex = stringHelper.findTokenIndexInArray(validKeys.configKeyDeclarations, splitByColon[0]); //the index of the token found in configKeyDeclarations (= -1 if not found in array)

                //if the token is not invalid
                if (returnedIndex != -1) {
                    //if the token has already been used before
                    if (configTokenHistory[returnedIndex].equals("0")) {
                        console.error("Duplicate parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[0] + "\"");
                    } else {
                        //remove the token from the history array so that it can't be used twice
                        configTokenHistory[returnedIndex] = "0";
                    }
                    //if it is of type ______, try to parse it & verify the validity of the value
                    if (validKeys.configTypeDeclarations[returnedIndex].equals("double")) {
                        try {
                            Double.parseDouble(splitByColon[1]);
                        } catch (Exception e) {
                            console.error("There was an error parsing the double provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                        }
                    } else if (validKeys.configTypeDeclarations[returnedIndex].equals("int")) {
                        try {
                            Integer.parseInt(splitByColon[1]);
                        } catch (Exception e) {
                            console.error("There was an error parsing the int provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                        }
                    } else if (validKeys.configTypeDeclarations[returnedIndex].equals("fileName")) {
                        if (!stringHelper.substringIsInString(splitByColon[1], ".")) {
                            console.error("There was an error parsing the filename provided in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                        } else {
                            if (splitByColon[0].equals("Log File Path")) {
                                //verify the log file extension is valid
                                String logFileExtension = stringHelper.splitOnDelimeter(splitByColon[1], "\\.")[1];
                                if (!logFileExtension.equals("lgf")) {
                                    console.error("Log file name does not end in .lgf");
                                }
                            } else if (splitByColon[0].equals("File Path")) {
                                //verify the input file extension is valid
                                String inputFileExtension = stringHelper.splitOnDelimeter(splitByColon[1], "\\.")[1];
                                if (!inputFileExtension.equals("mdf")) {
                                    console.error("Input file does not end in .mdf");
                                }
                            }
                        }
                    } else if (validKeys.configTypeDeclarations[returnedIndex].equals("logOption")) {
                        if (!splitByColon[1].equals("Log to Both") && !splitByColon[1].equals("Log to Monitor") && !splitByColon[1].equals("Log to File")) {
                            console.error("There was an error parsing the log option provided in " + config.fileName + " on line " + lineNum + " (invalid option specified): \n  \"" + splitByColon[1] + "\" next to declaration \"" + splitByColon[0] + "\"");
                        }
                    }
                } else {
                    console.error("Invalid parameter declaration in " + config.fileName + " on line " + lineNum + ": \n  \"" + splitByColon[0] + "\"");
                }
            }
        }

        //make sure all the tokens were used
        for (int i = 0; i < configTokenHistory.length - 1; i++) {
            if (!configTokenHistory[i].equals("0")) {
                console.error("Missing parameter declaration in " + config.fileName + ": \n  \"" + configTokenHistory[i] + "\"");
            }
        }

        return true;
    }
    //loadConfigFile - load the verified configuration file into the system
    public void loadConfigFile() {
        String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n"); //array of strings containing each line read in from the config file

        //loop through each line in the file
        for (int i = 0; i < splitByLineBreak.length - 1; i++) {
            //if line is not a comment
            if (stringHelper.substringIsInString(splitByLineBreak[i], ":")) {
                String[] splitByColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], ":[\\s]*"); //array of 2 cells - 1st is the left hand side of a config line (key), 2nd is the right hand side of a config line (value)

                //make begin and end count for 0 time
                config.times.put("begin", 0);
                config.times.put("finish", 0);

                //check to find which config value should be filled
                if (splitByColon[0].equals("Version/Phase")) {
                    config.version = Double.parseDouble(splitByColon[1]);
                } else if (splitByColon[0].equals("File Path")) {
                    config.inputFileName = splitByColon[1];
                } else if (splitByColon[0].equals("Monitor display time {msec}")) {
                    config.times.put("monitor", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Processor cycle time {msec}")) {
                    config.times.put("run", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Scanner cycle time {msec}")) {
                    config.times.put("scanner", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Hard drive cycle time {msec}")) {
                    config.times.put("hard drive", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Keyboard cycle time {msec}")) {
                    config.times.put("keyboard", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Memory cycle time {msec}")) {
                    config.times.put("allocate", Integer.parseInt(splitByColon[1]));
                    config.times.put("block", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Projector cycle time {msec}")) {
                    config.times.put("projector", Integer.parseInt(splitByColon[1]));
                } else if (splitByColon[0].equals("Log")) {
                    config.logOption = splitByColon[1];
                } else if (splitByColon[0].equals("Log File Path")) {
                    config.logFileName = splitByColon[1];
                } else if (splitByColon[0].equals("System memory {kbytes}")) {
                    config.systemMemory = Integer.parseInt(splitByColon[1]);
                } else if (splitByColon[0].equals("System memory {Mbytes}")) {
                    config.systemMemory = Integer.parseInt(splitByColon[1]) * 1000;
                } else if (splitByColon[0].equals("System memory {Gbytes}")) {
                    config.systemMemory = Integer.parseInt(splitByColon[1]) * 1000000;
                } else if (splitByColon[0].equals("Memory block size {kbytes}")) {
                    config.blockSize = Integer.parseInt(splitByColon[1]);
                } else if (splitByColon[0].equals("Memory block size {Mbytes}")) {
                    config.blockSize = Integer.parseInt(splitByColon[1]) * 1000;
                } else if (splitByColon[0].equals("Memory block size {Gbytes}")) {
                    config.blockSize = Integer.parseInt(splitByColon[1]) * 1000000;
                } else if (splitByColon[0].equals("Projector quantity")) {
                    config.projectorQuantity = Integer.parseInt(splitByColon[1]);
                } else if (splitByColon[0].equals("Hard drive quantity")) {
                    config.hardDriveQuantity = Integer.parseInt(splitByColon[1]);
                } else {
                    //should never reach this after verifying the file
                    console.error("Invalid parameter declaration in " + config.fileName + ": \n  \"" + splitByColon[0] + "\"");
                }
            }
        }

        //make the log file
        fileWriter.createFile(config.logFileName);
        //set the max block size for the memory allocator
        memoryBlock.setMaxBlockSize(config.systemMemory);
        //initialize the semaphore's number of available permits
        locks.projector.init(config.projectorQuantity);
        locks.hardDrive.init(config.hardDriveQuantity);

        //tell the user if there will be no further monitor/file output
        if (config.logOption.equals("Log to File")) {
            console.writeConsoleLog("✓ Output from this point on will show only in the log file", false);
        } else if (config.logOption.equals("Log to Monitor")) {
            console.writeFileLog("✓ Output from this point on will show only in the monitor window (log to monitor statement is in config file)", false);
        }
    }

    //verifyInputFile - verify the input file
    public boolean verifyInputFile() {
        fileReader.read(config.inputFileName); //actually read the input file and store it in the class's string var
        String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n");
        int lineNum = 0;

        //make sure the file is not empty
        if (splitByLineBreak.length <= 1) {
            console.error("Input file is empty");
        }

        for (int i = 0; i < splitByLineBreak.length; i++) {
            lineNum++;
            if (stringHelper.substringIsInString(splitByLineBreak[i], "{")) {
                //pre-process the line data to fix white space issues
                splitByLineBreak[i] = stringHelper.removeBadWhitespace(splitByLineBreak[i], validKeys.inputDescriptorDeclarations);
                String[] splitBySemiColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\;|\\.");

                for (int j = 0; j < splitBySemiColon.length; j++) {
                    //check to make sure the string matches the general regex pattern
                    if (!splitBySemiColon[j].matches("^[A-Z][\\s]*\\{[a-z]+(\\ *[a-z]*)\\}[\\s]*\\d+")) {
                        console.error("Syntax error in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \"" + splitBySemiColon[j] + "\"");
                    }

                    //define the items based off of the resulting string
                    String metaCode = splitBySemiColon[j].substring(0,1);
                    String numCycles = splitBySemiColon[j].replaceAll("\\D+","");
                    String descriptor = splitBySemiColon[j];
                    descriptor = descriptor.substring(descriptor.indexOf("{") + 1);
                    descriptor = descriptor.substring(0, descriptor.indexOf("}"));

                    //check the values individually
                    if (stringHelper.findTokenIndexInArray(validKeys.inputMetaCodeDeclarations, metaCode) == -1) {
                        console.error("Invalid meta code in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \""+ metaCode + "\" in \"" + splitBySemiColon[j] + "\"");
                    }
                    if (stringHelper.findTokenIndexInArray(validKeys.inputDescriptorDeclarations, descriptor) == -1) {
                        console.error("Invalid descriptor value in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n  \"" + descriptor + "\" in \"" + splitBySemiColon[j] + "\"");
                    }
                    try {
                        Integer.parseInt(numCycles);
                    } catch (Exception e) {
                        console.error("There was an error parsing the numCycles int in \"" + config.inputFileName + "\" on line " + lineNum + " for process:\n\"" + splitBySemiColon[j] + "\"");
                    }
                }
            }
        }

        return true;
    }

    //loadInputFile - load the verified input file into the system
    public void loadInputFile() {
        String[] splitByLineBreak = stringHelper.splitOnDelimeter(fileReader.lastReadFile, "\\\n");

        for (int i = 0; i < splitByLineBreak.length; i++) {
            if (stringHelper.substringIsInString(splitByLineBreak[i], "{")) {
                //pre-process the line data to fix white space issues
                splitByLineBreak[i] = stringHelper.removeBadWhitespace(splitByLineBreak[i], validKeys.inputDescriptorDeclarations);
                String[] splitBySemiColon = stringHelper.splitOnDelimeter(splitByLineBreak[i], "\\;|\\.");

                for (int j = 0; j < splitBySemiColon.length; j++) {
                    //define the items based off of the resulting string
                    char metaCode = splitBySemiColon[j].substring(0,1).charAt(0);
                    int numCycles = Integer.parseInt(splitBySemiColon[j].replaceAll("\\D+",""));
                    String descriptor = splitBySemiColon[j];
                    descriptor = descriptor.substring(descriptor.indexOf("{") + 1);
                    descriptor = descriptor.substring(0, descriptor.indexOf("}"));
                    //make a new (temp) task and add the data to it
                    Task newTask = new Task();
                    newTask.code = metaCode;
                    newTask.numCycles = numCycles;
                    newTask.description = descriptor;
                    //add the temp task to the task list
                    taskQueue.enqueue(newTask);
                }
            }
        }
    }
}
