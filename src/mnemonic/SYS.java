package mnemonic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.Scanner.*;

import components.VirtualMachine;
import operands.*;

public class SYS extends Mnemonic1 {
    
    public SYS(VirtualMachine vm) {
        super(vm, "SYS");
    }
    
    @Override
    public void operate(Operand A) throws Exception {
        int ECX = vm.getRegisters().getRegister(12);
        int EAX = vm.getRegisters().getRegister(10);
        int type = A.getValue();

        switch (type) {
            case 1:
                this.READ((ECX & 0xFF),((ECX >> 8) & 0xFF), (EAX & 0xFF));
                break;
            case 2:
                this.WRITE((ECX & 0xFF),((ECX >> 8) & 0xFF), (EAX & 0xFF));
                break;
            case 3:
                this.STRINGREAD((ECX & 0xFFFF));
                break;
            case 4: 
                this.STRINGWRITE();
                break;
            case 7:
                this.CLEARSCREEN();
                break;
            case 0xF:
                if(this.vm.getVmi_file() != null){
                    this.BREAKPOINT();
                }
            default:
                break;
        }



    }
    
    //FUNCIONES COMUNES
    private void writeInMemory(int value, int CH, int logic_address) throws Exception {

        int address = this.vm.getSegTable().LogicToPhysic(logic_address);
        this.vm.getVirtualMemory().writeNbytes(address, CH, value);
    }
    
    private int readFromMemory(int CH, int logic_address) throws Exception {
        int address = this.vm.getSegTable().LogicToPhysic(logic_address);
        return this.vm.getVirtualMemory().readNbytes(address, CH);

    }
    //FIN FUNCIONES COMUNES

    //INICIO BLOQUE READ (SYS 1)

    private void READ(int CL,int CH, int AL) throws Exception {
        int value;
        int address = this.vm.getRegisters().getRegister(13);
        Scanner input = new Scanner(System.in);

        for(int i = 0; i < CL; i++){
            try{
                System.out.print("["+ String.format("%04X",this.vm.getSegTable().LogicToPhysic(address)) + "]: ");
                String data = input.nextLine();
                value = convertInt(data,AL);
                writeInMemory(value, CH, address);
                address += CH;
            }catch(Exception e){
                throw e;
            }
        }
        
    }

    private int convertInt(String data, int AL) throws Exception {
        int value = 0;

        //CONVIERTE A HEXA SI LO PERMITE  AL
        if((AL & 0b01000) != 0){
            value = Integer.parseInt(data,16);
        }
        //CONVIERTE A BINARIO SI LO PERMITE  AL
        else if((AL & 0b10000) != 0){
            value = Integer.parseInt(data,2);
        }
        //CONVIERTE A OCTAL SI LO PERMITE AL
        else if((AL & 0b00100) != 0){
            value = Integer.parseInt(data, 8);
        }
        //CONVIERTE UN CARECTER A DECIMAL SI LO PERMITE AL
        else if(data.length() == 1 && (AL & 0b00010) != 0){
            value = (int)data.charAt(0);
        }
        //CONVIERTE A DECIMAL SI LO PERMITE AL
        else if((AL & 0b00001) != 0){
            value = Integer.parseInt(data, 10);
        
        }else{
            throw new Exception("Invalid conversion type for AL register.");
        }

        return value;
    }

    //FIN BLOQUE READ (SYS 1)

    
    //INICIO BLOQUE WRITE (SYS 2)

    private void WRITE(int CL, int CH, int AL) throws Exception {
        int address = this.vm.getRegisters().getRegister(13);

        for (int i = 0; i < CL; i++) {
            try {
                int value = readFromMemory(CH, address);
                String output = convertToString(value, AL);
                System.out.println("["+ String.format("%04X",this.vm.getSegTable().LogicToPhysic(address)) + "] " + output);
                address += CH;
            } catch (Exception e) {
                throw e;
            }
        }
        System.out.println();
    }

    private String convertToString(int value, int AL) throws Exception {
        String result = "";


        // CONVIERTE A HEXA SI LO PERMITE EL AL
        if ((AL & 0b01000) != 0) {
            result = String.format("0x%08X", value);
        }
        // CONVIERTE A BINARIO SI LO PERMITE EL AL
        if ((AL & 0b10000) != 0) {
            if (result.length() > 0) 
                result += " ";
            result += "0b" + Integer.toBinaryString(value);
        }
        // CONVIERTE A OCTAL SI LO PERMITE EL AL
        if ((AL & 0b00100) != 0) {
            if (result.length() > 0) 
                result += " ";
            result += "0o" + Integer.toOctalString(value);
        }
        // CONVIERTE A CHAR SI LO PERMITE EL AL
        if ((AL & 0b00010) != 0) {
            if (result.length() > 0) 
                result += " ";
            
            char character = (char) value;
            if (character >= 32 && character <= 126) {
                result += character;
            } else {
                result += ".";  
            }
        }
        // CONVIERTE A DECIMAL SI LO PERMITE EL AL
        if ((AL & 0b00001) != 0) {
            if (result.length() > 0) 
                result += " ";
            result += Integer.toString(value);
        }

        if (result.length() == 0) {
            throw new Exception("Invalid conversion type for AL register.");
        }

        return result;
    }
    //FIN BLOQUE WRITE (SYS 2)

    //INICIO BLOQUE STRINGREAD (SYS 3)

    private void STRINGREAD(int CX) throws Exception{
        int address = this.vm.getRegisters().getRegister(13);
        Scanner input = new Scanner(System.in);
        String data = input.nextLine();

        if(CX != -1 && data.length() > CX){
            data = data.substring(0, CX);
        }

        data += "\0";


        for (int i = 0; i < data.length();i++){
            char c = data.charAt(i);
            int value = (int) c;
            this.writeInMemory(value, 1, address);
            address++;
        }

    }
    //FIN BLOQUE STRINGREAD (SYS 3)

    //INICIO BLOQUE STRINGWRITE (SYS 4)
    private void STRINGWRITE() throws Exception{
        int address = this.vm.getRegisters().getRegister(13);
        int value = readFromMemory(1,address);
        String data = "";
        
        
        while (value != 0x00) {
            data += this.convertToString(value,0x02);
            address++;
            value = readFromMemory(1,address);
        }

        System.out.println("["+ String.format("%04X",this.vm.getSegTable().LogicToPhysic(this.vm.getRegisters().getRegister(13))) + "] " + data);
        
    }
    //FIN BLOQUE STRINGWRITE (SYS 4)

    //INICIO BLOQUE CLEARSCREEEN (SYS 7)
    private void CLEARSCREEN() throws Exception{
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    //FIN BLOQUE CLEARSCREEN (SYS 7)

    //INICIO BLOQUE BREAKPOINT (SYS 0xF)
    public void BREAKPOINT() throws Exception{
        
        FileOutputStream file =  new  FileOutputStream(this.vm.getVmi_file());

        this.createHeader(file);
        this.copyBytes(file, this.vm.getRegisters().getRegs());
        this.copyBytes(file, this.vm.getSegTable().getSegmentTable());
        file.write(this.vm.getVirtualMemory().getMemory());

        file.close();

        Scanner input = new Scanner(System.in);
        String next = input.nextLine();

        if(next.equals("q")){
            this.vm.getRegisters().setRegister(5,-1);
        }else if(next.isEmpty()){
            this.vm.setBreakpoint(true);
        }else if(next.equals("g")){

        }else{
            throw new Exception("Invalid command. Use 'q' to quit, 'g' to continue, or press Enter to repeat.");
        }

        

    }

    private void createHeader(FileOutputStream  file) throws Exception{
        int memory = this.vm.getVirtualMemory().getMemorySize();
        byte[] indentifier = {'V','M','I','2','5'};
        byte[] version = {1};
        byte[] memory_size = {(byte)((memory >> 8) & 0xFF),(byte)(memory & 0xFF)};

        file.write(indentifier);
        file.write(version);
        file.write(memory_size);
    }

    private void copyBytes(FileOutputStream file, int[] data) throws Exception{
        for(int i = 0; i < data.length; i++){
            byte[] bytes = intToByteArray(data[i]);
            file.write(bytes);
        }
    }
            
    private byte[] intToByteArray(int i) {
        byte[] bytes = new byte[4];

        for(int j = 3; j >=0; j--){
            bytes[j] = (byte)(i & 0xFF);
            i = i >> 8;
        }

        return bytes;
    }

    //FIN BLOQUE BREAKPOINT (SYS 0xF)
}
