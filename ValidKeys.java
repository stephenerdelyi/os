///////////////////
// ValidKeys: holds all the valid keys or tokens for the configuration file and data input file (makes managing possible inputs easier)
///////////////////
public class ValidKeys extends OS {
    String[] configKeyDeclarations = {"Version/Phase","File Path","Monitor display time {msec}","Processor cycle time {msec}","Scanner cycle time {msec}","Hard drive cycle time {msec}","Keyboard cycle time {msec}","Memory cycle time {msec}","Projector cycle time {msec}","System memory {kbytes}|System memory {Mbytes}|System memory {Gbytes}","Memory block size {kbytes}|Memory block size {Mbytes}|Memory block size {Gbytes}","Projector quantity","Hard drive quantity","Log","Log File Path"}; //a string array of all valid config declarations for error checking
    String[] configTypeDeclarations = {"double","fileName","int","int","int","int","int","int","int","int","int","int","int","logOption","fileName"}; //a string array of datatypes that correspond to the entires in configKeyDeclarations used for error checking
    String[] inputMetaCodeDeclarations = {"S","A","P","M","O","I"}; //string (easier than char) array that contains all valid input file meta code values
    String[] inputDescriptorDeclarations = {"run","begin","allocate","monitor","hard drive","scanner","projector","block","keyboard","finish"}; //string array that contains all valid input file description values
    String[] processStateDeclarations = {"Start", "Ready", "Running", "Waiting", "Exit"};
}
