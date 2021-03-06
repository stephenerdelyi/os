///////////////////
// Console: contains important output functions to the monitor or log file; also contains the error and crash functions
///////////////////
public class Console extends OS {
    WriteFile fileWriter = new WriteFile();

    //log - contains all important logic for logging to the monitor, log file, or both
    public void log(String echoStatement, String route) {
        if (config != null) {
            if (config.logOption != null) {
                if (config.logOption.equals("Log to File")) {
                    writeFileLog(echoStatement, route);
                } else if (config.logOption.equals("Log to Monitor")) {
                    writeConsoleLog(echoStatement, route);
                } else if (config.logOption.equals("Log to Both")) {
                    writeConsoleLog(echoStatement, route);
                    writeFileLog(echoStatement, route);
                }
            } else {
                //since config file name hasn't loaded yet, just output to console
                writeConsoleLog(echoStatement, route);
            }
        } else {
            //since config hasn't loaded yet, just output to console
            writeConsoleLog(echoStatement, route);
        }

        //after we write everything, if the log was fatal we should ask to crash
        if (route.equals("fatal")) {
            //if we allow fatal executions
            if (allowFatalExecution) {
                //prompt the user to continue
                console.writeConsoleLog("Continue execution? (Y/N)", "log");
                String name = System.console().readLine();
                //if the user does not want to continue, then crash
                if (!name.equals("y") && !name.equals("Y")) {
                    crash();
                }
            } else {
                crash();
            }
        }
    }

    //writeConsoleLog - writes a log message to the monitor only
    public void writeConsoleLog(String echoStatement, String route) {
        if (route.equals("log")) {
            System.out.println((char)27 + "[39m" + echoStatement);
        } else if (route.equals("warn")) {
            System.out.println((char)27 + "[39m");
            System.out.println((char)27 + "[36m" + "➤ WARNING: " + echoStatement);
            System.out.println((char)27 + "[39m");
        } else if (route.equals("fatal")) {
            System.out.println((char)27 + "[39m");
            System.out.println((char)27 + "[31m" + "✖ FATAL ERROR: " + echoStatement);
            System.out.println((char)27 + "[39m");
        }
    }

    //writeFileLog - writes a log message to the log file only
    public void writeFileLog(String echoStatement, String route) {
        if (route.equals("log")) {
            fileWriter.write(config.logFileName, true, echoStatement);
        } else if (route.equals("warn")) {
            fileWriter.write(config.logFileName, true, "➤ WARNING: " + echoStatement);
        } else if (route.equals("fatal")) {
            fileWriter.write(config.logFileName, true, "✖ FATAL ERROR: " + echoStatement);
        }
    }

    //log - parameter override for single int printout
    public void log(int echoStatement) {
        log(Integer.toString(echoStatement), "log");
    }

    //log - parameter override for cleaner log statement outside class
    public void log(String echoStatement) {
        log(echoStatement, "log");
    }

    //warn - writes a log message with a warning color
    public void warn(String echoStatement) {
        log(echoStatement, "warn");
    }

    //error - writes a log message with isFatal set to true - will crash program
    public void error(String echoStatement) {
        log(echoStatement, "fatal");
    }

    //printDiv - prints a divider to the monitor or log file
    public void printDiv() {
        log("=======================================================", "log");
    }

    //printNewline - prints a new line to the monitor or log file
    public void printNewline() {
        log(" ", "log");
    }

    //crash - crashes the program prematurely so that the files can not be processed after a syntax error is found
    private void crash() {
        System.exit(0);
    }
}
