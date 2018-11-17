///////////////////
// ValidKeys: holds all the valid keys or tokens for the configuration file and data input file (makes managing possible inputs easier)
///////////////////
public class ValidKeys extends OS {
    String[] configKeyDeclarations = {"Version/Phase","File Path","Monitor display time {msec}","Processor cycle time {msec}","Scanner cycle time {msec}","Hard drive cycle time {msec}","Keyboard cycle time {msec}","Memory cycle time {msec}","Projector cycle time {msec}","System memory {kbytes}|System memory {Mbytes}|System memory {Gbytes}","Memory block size {kbytes}|Memory block size {Mbytes}|Memory block size {Gbytes}","Projector quantity","Hard drive quantity","Log","Log File Path","Processor Quantum Number","CPU Scheduling Code"}; //a string array of all valid config declarations for error checking
    String[] configTypeDeclarations = {"double","fileName","int","int","int","int","int","int","int","int","int","int","int","logOption","fileName","int","schedulingCode"}; //a string array of datatypes that correspond to the entires in configKeyDeclarations used for error checking
    String[] configDefaultValueDeclarations = {"4.0","Test_4a.mdf","20","500","30","150","60","10","550","2048","128","4","2","Log to Both","logfile_1.lgf","1","FIFO"}; //string array of default config values
    String[] configDefaultAllowed = {"0","0","0","0","0","0","0","1","0","1","1","1","1","0","0","1","1"}; //1 if OS should allow default values to be written, 0 if it is required explicitly from config file
    String[] inputMetaCodeDeclarations = {"S","A","P","M","O","I"}; //string (easier than char) array that contains all valid input file meta code values
    String[] inputDescriptorDeclarations = {"run","begin","allocate","monitor","hard drive","scanner","projector","block","keyboard","finish"}; //string array that contains all valid input file description values
    String[] processStateDeclarations = {"Start", "Ready", "Running", "Waiting", "Exit"};
}
