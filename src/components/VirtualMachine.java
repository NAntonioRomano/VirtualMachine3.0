package components;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map; // Importación añadida para usar Map

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
    protected boolean breakpoint = true;
    protected boolean disassembler = true;
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

    //INICIA BLOQUE DISASSEMBLER

    private int extractOperandValue(byte[] codeSegment, int startIndex, int type) {
        int value = 0;
        for (int i = 0; i < type; i++) {
            value = (value << 8) | (codeSegment[startIndex + i] & 0xFF);
        }
        return value;
    }

    private void printHexBytesAndPad(byte[] codeSegment, int startIndex, int count, int totalWidth) {
        for (int i = 0; i < count; i++) {
            System.out.print(String.format("%02X ", codeSegment[startIndex + i]));
        }
        for (int i = count; i < totalWidth; i++) {
            System.out.print("   ");
        }
    }

    private int handleNoOperandInstruction(int opcode, int instructionStartMemoryPos, int instructionByte) {
        System.out.print(String.format("[%04X] %02X ", instructionStartMemoryPos, instructionByte));
        printHexBytesAndPad(null, 0, 0, 8);
        System.out.println("| " + this.mnemonics0.get(opcode).toString());
        return 1;
    }

    private int handleSingleOperandInstruction(int opcode, int instructionStartMemoryPos, int instructionByte, byte[] codeSegment, int currentIndex) {
        int typeOpA = (instructionByte >> 6) & 0x3;
        int operandAValue = extractOperandValue(codeSegment, currentIndex + 1, typeOpA);
        this.operandA[typeOpA].setData(operandAValue);

        System.out.print(String.format("[%04X] %02X ", instructionStartMemoryPos, instructionByte));
        printHexBytesAndPad(codeSegment, currentIndex + 1, typeOpA, 8);
        System.out.println("| " + this.mnemonics1.get(opcode).toString(this.operandA[typeOpA]));
        return typeOpA + 1;
    }

    private int handleDoubleOperandInstruction(int opcode, int instructionStartMemoryPos, int instructionByte, byte[] codeSegment, int currentIndex) {
        int typeOpA = (instructionByte >> 4) & 0x3;
        int typeOpB = (instructionByte >> 6) & 0x3;

        int operandBValue = extractOperandValue(codeSegment, currentIndex + 1, typeOpB);
        this.operandB[typeOpB].setData(operandBValue);

        int operandAValue = extractOperandValue(codeSegment, currentIndex + 1 + typeOpB, typeOpA);
        this.operandA[typeOpA].setData(operandAValue);

        System.out.print(String.format("[%04X] %02X ", instructionStartMemoryPos, instructionByte));
        printHexBytesAndPad(codeSegment, currentIndex + 1, typeOpA + typeOpB, 8);
        System.out.println("| " + this.mnemonics2.get(opcode).toString(this.operandA[typeOpA], this.operandB[typeOpB]));
        return typeOpA + typeOpB + 1;
    }
	
    public void disassembler(byte[] offset) throws Exception {
        int index = 0;
        int instructionStartMemoryPos = 0;
        int memoryIndex = this.segTable.LogicToPhysic(this.registers.getRegister(0));
        int codeSegmentBase = this.segTable.getBase(this.registers.getRegister(0) >> 16);
        byte[] codeSegment = new byte[this.segTable.getSize(this.registers.getRegister(0) >> 16)];

        System.arraycopy(this.virtualMemory.getMemory(), codeSegmentBase, codeSegment, 0, this.segTable.getSize(this.registers.getRegister(0) >> 16));

        System.out.println("\n------------------DISASSEMBLER------------------\n");

        while (index < codeSegment.length) {
            memoryIndex += index - instructionStartMemoryPos;
            int instructionByte = codeSegment[index] & 0xFF;
            int opcode = this.getOpcode(instructionByte);
            int operandCount = this.cantOP(instructionByte);
            instructionStartMemoryPos = memoryIndex;


            if (operandCount == 0) {
                index += handleNoOperandInstruction(opcode, instructionStartMemoryPos, instructionByte);
            } else if (operandCount == 1) {
                index += handleSingleOperandInstruction(opcode, instructionStartMemoryPos, instructionByte, codeSegment, index);
            } else {
                index += handleDoubleOperandInstruction(opcode, instructionStartMemoryPos, instructionByte, codeSegment, index);
            }

        }
        System.out.println("\n-------------------------------------------------\n");
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
            //System.out.println("[DEBUG] " + mnemonics0.get(codop).toString());
            mnemonics0.get(codop).operate();

        }else if(cantop == 1 && codop >= 0x00 && codop <= 0x0D) {
            //System.out.println("[DEBUG] " + mnemonics1.get(codop).toString(B));
            mnemonics1.get(codop).operate(B);

        }else if(cantop == 2 && codop >= 0x10 && codop <= 0x1E) {
            //System.out.println("[DEBUG] " + mnemonics2.get(codop).toString(A,B));
            mnemonics2.get(codop).operate(A, B);

        }else {
            throw new Exception("Invalid code operation");
        }
        
    }
    //FIN BLOQUE DE EJECUCION DEL PROGRAMA  
}



