package components;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import componentsSecondPart.SegmentTable2;
import mnemonic.*;
import operands.*;
import utils.utilities;



public class VirtualMachine {
    protected VirtualMainMemory virtualMemory;
    protected SegmentTable2 segTable;
    protected Registers registers;
    protected HashMap<Integer,Mnemonic0> mnemonics0;
    protected HashMap<Integer,Mnemonic1> mnemonics1;
    protected HashMap<Integer,Mnemonic2> mnemonics2;
    protected Operand[] operandA;
    protected Operand[] operandB;
    protected boolean breakpoint = false;
    protected boolean disassembler = false;
    protected String vmi_file;


    // BLOQUE CONSTRUCTORES GETTERS Y SETTERS
    
    public VirtualMachine() {
        this.virtualMemory = new VirtualMainMemory();
        this.segTable = new SegmentTable2();
        this.registers = new Registers();
        this.mnemonics0 = new HashMap<>();
        this.mnemonics1 = new HashMap<>();
        this.mnemonics2 = new HashMap<>();
        vmi_file = null;

        this.setMnemonics();
        this.setOperando();
    }
    

    protected void setMnemonics() {
        mnemonics0.put(0x0F, new STOP(this));

        mnemonics1.put(0x00, new SYS(this));
        mnemonics1.put(0x01, new JMP(this));
        mnemonics1.put(0x02, new JZ(this));
        mnemonics1.put(0x03, new JP(this));
        mnemonics1.put(0x04, new JN(this));
        mnemonics1.put(0x05, new JNZ(this));
        mnemonics1.put(0x06, new JNP(this));
        mnemonics1.put(0x07, new JNN(this));
        mnemonics1.put(0x08, new NOT(this));

        mnemonics2.put(0x10, new MOV(this));
        mnemonics2.put(0x11, new ADD(this));
        mnemonics2.put(0x12, new SUB(this));
        mnemonics2.put(0x13, new SWAP(this));
        mnemonics2.put(0x14, new MUL(this));
        mnemonics2.put(0x15, new DIV(this));
        mnemonics2.put(0x16, new CMP(this));
        mnemonics2.put(0x17, new SHL(this));
        mnemonics2.put(0x18, new SHR(this));
        mnemonics2.put(0x19, new AND(this));
        mnemonics2.put(0x1A, new OR(this));
        mnemonics2.put(0x1B, new XOR(this));
        mnemonics2.put(0x1C, new LDL(this));
        mnemonics2.put(0x1D, new LDH(this));
        mnemonics2.put(0x1E, new RND(this));
    }

    protected void setOperando() {
        this.operandA = new Operand[4];
        this.operandB = new Operand[4];

        this.operandA[0] = null;
        this.operandA[1] = new RegisterOperand(this); 
        this.operandA[2] = new InmediateOperand(this);
        this.operandA[3] = new MemoryOperand(this);

        this.operandB[0] = null;
        this.operandB[1] = new RegisterOperand(this);
        this.operandB[2] = new InmediateOperand(this);
        this.operandB[3] = new MemoryOperand(this);
    }

    public VirtualMainMemory getVirtualMemory() {
        return virtualMemory;
    }

    public SegmentTable2 getSegTable() { // Cambiado a SegmentTable2
        return segTable;
    }

    public Registers getRegisters() {
        return registers;
    }

    public void setBreakpoint(boolean breakpoint) {
        this.breakpoint = breakpoint;
    }

    public HashMap<Integer, Mnemonic0> getMnemonics0() {
        return mnemonics0;
    }


    public HashMap<Integer, Mnemonic1> getMnemonics1() {
        return mnemonics1;
    }


    public HashMap<Integer, Mnemonic2> getMnemonics2() {
        return mnemonics2;
    }

    public String getVmi_file(){
        return vmi_file;
    }

    public void setVmi_file(String vmi_file){
        this.vmi_file = vmi_file;
    }

    public void setDisassembler(boolean disassembler) {
        this.disassembler = disassembler;
    }

    public boolean getDissasembler(){
        return this.disassembler;
    }

    //FIN BLOQUE CONSTRUCTORES GETTERS Y SETTERS
    

    //BLOQUE DE VALIDACIONES


    public void verify(byte[] allbytes) throws Exception{
        try {
            
            byte[] header = new byte[5];
            byte[] version = new byte[1];

            
            
            System.arraycopy(allbytes,0,header,0, 5);
            
            validateHeader(header);
            
            System.arraycopy(allbytes, 5, version, 0, 1);
            
            validateVersion(version);
            
            
            
        } catch (Exception e) {
            throw e;
        }
        
    }

    protected void validateVersion(byte[] version) throws Exception{
        if(version[0] != 1) {
            throw new Exception("Invalid version");
        }
        
    }

    protected void validateHeader (byte[] header) throws Exception{
        byte[] expected = {'V','M','X','2','5'};
        
        for(int i = 0;i < 5;i++) {
            if(header[i] != expected[i]) {
                throw new Exception("Invalid Header");
            }
                
        }
    }

    public void validateExtension(String file_path) throws Exception {
        Path path = Paths.get(file_path);
        
        if(!file_path.toLowerCase().endsWith(".vmx")){
            throw new Exception("Invalid extension");
        }
        
        if(!Files.exists(path)) {
            throw new Exception("File not found");
        }
        
        
    }

    // FIN BLOQUE VALIDACIONES


    //BLOQUE DE CARGA DE PROGRAMA EN MEMORIA PRINCIPAL
    
    public void startMemory(byte[] allbytes) throws Exception {
        byte[] size = new byte[2]; 
        byte[] memory = new byte[allbytes.length - 8];
        
        System.arraycopy(allbytes,6,size,0,2);
        System.arraycopy(allbytes,8,memory,0,allbytes.length - 8);
        
        this.virtualMemory.setMemory(memory, size,0);
        this.segTable.setSegmentTable(size, this.virtualMemory.getMemorySize());
        this.registers.loadRegisters(utilities.defaultRegisters);
    }

    //FIN BLOQUE CARGA DE PROGRAMA EN MEMORIA PRINCIPAL

    // INICIA BLOQUE DISASSEMBLER

    private int extractOperand(byte[] code, int start, int type) {
        int value = 0;
        for (int i = 0; i < type; i++) {
            value = (value << 8) | (code[start + i] & 0xFF);
        }
        return value;
    }

    private void printHexAndPad(byte[] code, int start, int count, int width) {
        for (int i = 0; i < count; i++) {
            System.out.print(String.format("%02X ", code[start + i] & 0xFF));
        }
        for (int i = count; i < width; i++) {
            System.out.print("   ");
        }
    }

    private int handleNoOp(int opcode, int memPos, int instructionByte) {
        System.out.print(String.format("[%04X] %02X ", memPos, instructionByte));
        printHexAndPad(null, 0, 0, 8);
        System.out.println("| " + this.mnemonics0.get(opcode).toString());
        return 1;
    }

    private int handleSingleOp(int opcode, int memPos, int instructionByte, byte[] code, int currentIdx) {
        int opAType = (instructionByte >> 6) & 0x3;
        if (currentIdx + 1 + opAType > code.length) { 
            throw new IllegalArgumentException("Insufficient bytes for single operand instruction.");
        }
        int opAValue = extractOperand(code, currentIdx + 1, opAType);
        this.operandA[opAType].setData(opAValue);

        System.out.print(String.format("[%04X] %02X ", memPos, instructionByte));
        printHexAndPad(code, currentIdx + 1, opAType, 8);
        System.out.println("| " + this.mnemonics1.get(opcode).toString(this.operandA[opAType]));
        return opAType + 1;
    }

    private int handleDoubleOp(int opcode, int memPos, int instructionByte, byte[] code, int currentIdx) {
        int opAType = (instructionByte >> 4) & 0x3;
        int opBType = (instructionByte >> 6) & 0x3;

        if (currentIdx + 1 + opBType + opAType > code.length) { 
            throw new IllegalArgumentException("Insufficient bytes for double operand instruction.");
        }

        int opBValue = extractOperand(code, currentIdx + 1, opBType);
        this.operandB[opBType].setData(opBValue);

        int opAValue = extractOperand(code, currentIdx + 1 + opBType, opAType);
        this.operandA[opAType].setData(opAValue);

        System.out.print(String.format("[%04X] %02X ", memPos, instructionByte));
        printHexAndPad(code, currentIdx + 1, opAType + opBType, 8);
        System.out.println("| " + this.mnemonics2.get(opcode).toString(this.operandA[opAType], this.operandB[opBType]));
        return opAType + opBType + 1;
    }

    private int handleString(byte[] code, int startIdx, int memPos) {
        StringBuilder hex = new StringBuilder();
        StringBuilder ascii = new StringBuilder();
        int currentLen = 0;
        int originalLen = 0;

        while (startIdx + originalLen < code.length && code[startIdx + originalLen] != 0x00) {
            originalLen++;
        }
        if (startIdx + originalLen < code.length) {
            originalLen++; 
        }

        int bytesToProcess = Math.min(originalLen, code.length - startIdx);

        for (int i = 0; i < bytesToProcess; i++) {
            byte b = code[startIdx + i];
            
            if (currentLen < 6) {
                hex.append(String.format("%02X ", b & 0xFF));
            } else if (currentLen == 6 && originalLen > 7) {
                hex.append(".. ");
            }
            
            if (b >= 32 && b <= 126) {
                ascii.append((char) b);
            } else {
                ascii.append(".");
            }
            currentLen++;
        }
        
        String hexPart = hex.toString().trim();
        int padding = Math.max(0, 8 * 3 - hexPart.length());
        
        System.out.print(String.format("[%04X] %s", memPos, hexPart));
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.println("| \"" + ascii.toString() + "\"");

        return bytesToProcess;
    }

    public void disassembleCodeSegment(int offset) throws Exception {
        int index = 0;
        int codeSegLogicalBase = this.segTable.getBase(this.registers.getRegister(0) >> 16); 
        int currentLogicalOffset = this.segTable.getSize(this.registers.getRegister(0) >> 16);

        int codeSegPhysicalBase = this.segTable.LogicToPhysic(this.registers.getRegister(0));
        byte[] codeSegment = new byte[this.segTable.getSize(this.registers.getRegister(0) >> 16)];

        System.arraycopy(this.virtualMemory.getMemory(), codeSegPhysicalBase, codeSegment, 0, this.segTable.getSize(this.registers.getRegister(0) >> 16));


        int entryPointLogicalAddr = offset; 

        while (index < codeSegment.length) {
            int currentMemPos = codeSegLogicalBase + index;

            if (currentMemPos == entryPointLogicalAddr) {
                System.out.print(">");
            } else {
                System.out.print(" ");
            }

            int instructionByte = codeSegment[index] & 0xFF;
            int opcode = this.getOpcode(instructionByte);
            int operandCount = this.cantOP(instructionByte);
            
            int bytesAdvanced = 0;

            try {
                if (this.mnemonics0.containsKey(opcode)) {
                    bytesAdvanced = handleNoOp(opcode, currentMemPos, instructionByte);
                } else if (this.mnemonics1.containsKey(opcode)) {
                    bytesAdvanced = handleSingleOp(opcode, currentMemPos, instructionByte, codeSegment, index);
                } else if (this.mnemonics2.containsKey(opcode)) {
                    bytesAdvanced = handleDoubleOp(opcode, currentMemPos, instructionByte, codeSegment, index);
                } else {
                    int nullByteIdx = -1;
                    for (int i = index; i < codeSegment.length && i < index + 32; i++) {
                        if (codeSegment[i] == 0x00) {
                            nullByteIdx = i;
                            break;
                        }
                    }
                    
                    if (nullByteIdx != -1) {
                        bytesAdvanced = handleString(codeSegment, index, currentMemPos);
                    } else {
                        System.out.print(String.format("[%04X] %02X ", currentMemPos, instructionByte));
                        printHexAndPad(null, 0, 0, 8);
                        System.out.println("| DB " + String.format("%02X", instructionByte));
                        bytesAdvanced = 1;
                    }
                }
            } catch (IllegalArgumentException e) {
                int nullByteIdx = -1;
                for (int i = index; i < codeSegment.length && i < index + 32; i++) {
                    if (codeSegment[i] == 0x00) {
                        nullByteIdx = i;
                        break;
                    }
                }
                
                if (nullByteIdx != -1) {
                    bytesAdvanced = handleString(codeSegment, index, currentMemPos);
                } else {
                    System.out.print(String.format("[%04X] %02X ", currentMemPos, instructionByte));
                    printHexAndPad(null, 0, 0, 8);
                    System.out.println("| DB " + String.format("%02X", instructionByte));
                    bytesAdvanced = 1;
                }
            }
            index += bytesAdvanced;
        }
    }

    public void disassembleConstantSegment() throws Exception {
        int ksLogicalAddr = this.registers.getRegister(4); 
        
        int segmentSel = ksLogicalAddr >> 16;
        int initialOffset = ksLogicalAddr & 0x0000FFFF;

        int ksPhysicalBase = this.segTable.LogicToPhysic(ksLogicalAddr);
        int ksSize = this.segTable.getSize(segmentSel);

        byte[] ksSegment = new byte[ksSize - initialOffset];
        System.arraycopy(this.virtualMemory.getMemory(), ksPhysicalBase + initialOffset, ksSegment, 0, ksSize - initialOffset);


        int index = 0;
        int currentLogicalMemPos = ksLogicalAddr;

        while (index < ksSegment.length) {
            int currentByte = ksSegment[index] & 0xFF;
            
            int nullByteIdx = -1;

            for (int i = index; i < ksSegment.length && i < index + 256; i++) {
                if (ksSegment[i] == 0x00) {
                    nullByteIdx = i;
                    break;
                }
            }

            if (nullByteIdx != -1) {
                int stringLen = (nullByteIdx - index) + 1; 
                if (index + stringLen <= ksSegment.length) {
                    index += handleString(ksSegment, index, currentLogicalMemPos);
                    currentLogicalMemPos = ksLogicalAddr + index;
                } else {

                    System.out.print(String.format("  [%04X] %02X ", currentLogicalMemPos, currentByte));
                    printHexAndPad(null, 0, 0, 8);
                    System.out.println("| DB " + String.format("%02X", currentByte));
                    index++;
                    currentLogicalMemPos++;
                }
            } else {
                System.out.print(String.format("  [%04X] %02X ", currentLogicalMemPos, currentByte));
                printHexAndPad(null, 0, 0, 8);
                System.out.println("| DB " + String.format("%02X", currentByte));
                index++;
                currentLogicalMemPos++;
            }
        }

    }

    public void disassembler(byte[] offset) throws Exception{
        int offset_entry_point = ((int)(offset[0] << 8)|(int)(offset[1]) & 0xFF);
        System.out.println("\n----------------------DISASSEMBLER--------------------\n");
        disassembleConstantSegment();
        disassembleCodeSegment(offset_entry_point);
        System.out.println("\n------------------------------------------------------\n");
    }


    //FIN BLOQUE DISASSEMBLER

    //BLOQUE DE EJECUCION DEL PROGRAMA

    public void execute() throws Exception {
        Operand A,B;
        
        int instruction;
        int codop;
        int limitcodesegment = this.segTable.LogicToPhysic(this.registers.getRegister(0)) + this.segTable.getSize(this.registers.getRegister(0) >> 16);

        while(this.registers.getRegister(5) != -1 && this.segTable.LogicToPhysic(this.registers.getRegister(5)) < limitcodesegment) {
            
            instruction = this.getInstruction();
            codop = this.getOpcode(instruction);

            A = this.getOperand(((instruction >> 4) & 0x3), this.operandA, this.getData(instruction, ((instruction >> 6) & 0x3)));
            B = this.getOperand(((instruction >> 6) & 0x3), this.operandB, this.getData(instruction,0));
            this.addIP(instruction);
            if(this.breakpoint){
                this.Operation(codop, A, B, this.cantOP(instruction));
                this.breakpoint = false;
                SYS sys = new SYS(this);
                sys.BREAKPOINT();
            }else{
                this.Operation(codop, A, B, this.cantOP(instruction));
            } 
        }

    }

    protected int getInstruction() throws Exception {
        int instruction;
    
        instruction = this.virtualMemory.readByte(this.segTable.LogicToPhysic(this.registers.getRegister(5)));

        return instruction;
    }

    protected int cantOP(int register){
        int cant;

        if((register & 0b11100000) == 0) {
            cant = 0;
        }else if((register & 0b00010000) == 0) {
            cant = 1;
        }else {
            cant = 2;
        }
        return cant;
        
    }


    protected void addIP(int register){
        int cant =(((register >> 6) & 0x3) + ((register >> 4) & 0x3));
        cant = cant + 1;
        this.registers.add(5, cant);
    }


    protected int getData(int register, int offset) throws Exception {
        int tipo;
        if(offset > 0){
            tipo = (register >> 4) & 0x3;
        }else{
            tipo = (register >> 6) & 0x3;
        }
        
        int opA = 0;
        
        for(int i = 1; i <= tipo; i++) {
            opA = ((opA << 8) | this.virtualMemory.readByte(this.segTable.LogicToPhysic(this.registers.getRegister(5) + offset + i)));
        }
        return opA;
    }

    protected int getOpcode(int register) {
        return (register & 0x1F);
    } 

    protected Operand getOperand(int type, Operand[] operands, int data) {
        Operand operand = null;
        if(type > 0 && type < 4){
            operand = operands[type];
            operand.setData(data);
        }
        return operand;
    }

    protected void Operation(int codop, Operand A, Operand B, int cantop) throws Exception{
        if(cantop == 0 && (codop == 0x0F || codop == 0x0E)) {
            mnemonics0.get(codop).operate();

        }else if(cantop == 1 && codop >= 0x00 && codop <= 0x0D) {
            mnemonics1.get(codop).operate(B);

        }else if(cantop == 2 && codop >= 0x10 && codop <= 0x1E) {
            mnemonics2.get(codop).operate(A, B);

        }else {
            throw new Exception("Invalid code operation");
        }
        
    }
    //FIN BLOQUE DE EJECUCION DEL PROGRAMA  
}



