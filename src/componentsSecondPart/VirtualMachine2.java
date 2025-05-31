package componentsSecondPart;

import java.util.HashMap;

import components.Registers;
import components.SegmentTable;
import components.VirtualMachine;
import components.VirtualMainMemory;
import mnemonic.*;
import operands.*;

public class VirtualMachine2 extends VirtualMachine {


    public VirtualMachine2(VirtualMainMemory memory){
        this.virtualMemory = memory;
		this.segTable = new SegmentTable2();
		this.registers = new Registers();
		this.mnemonics0 = new HashMap<>();
		this.mnemonics1 = new HashMap<>();
		this.mnemonics2 = new HashMap<>();
        this.vmi_file = null;

		this.setMnemonics();
		this.setOperando();
    }

    @Override
    protected void setMnemonics(){
        super.setMnemonics();
        
        //NUEVOS MNEMOMICOS

        //0 OPERANDOS
        this.mnemonics0.put(0x0E, new RET(this));

        //1 OPERANDO
        this.mnemonics1.put(0x0B, new PUSH(this));
        this.mnemonics1.put(0x0C, new POP(this));
        this.mnemonics1.put(0x0D, new CALL(this));

    }

    //BLOQUE VALIDACIONES
    @Override
    public void validateExtension(String file_path) throws Exception {
        try{
            super.validateExtension(file_path);
        }catch(Exception e){
            if(e.getMessage().equalsIgnoreCase("Invalid extension")){
                if(!file_path.toLowerCase().endsWith(".vmi")){
                    throw e;
                }
            }else
                throw e;
        }
    }

    @Override
    protected void validateVersion(byte[] version) throws Exception {
        try {
            super.validateVersion(version);
        } catch (Exception e) {
            if(version[0] != 2) {
                throw e;
            }
        }
    }

    @Override
    protected void validateHeader(byte[] header) throws Exception {
        try{
            super.validateHeader(header);
        }catch (Exception e){
            byte[] expected = {'V','M','I','2','5'};
		
            for(int i = 0;i < 5;i++) {
                if(header[i] != expected[i]) {
                    throw e;
                }
                
            }
        }
    }

    //FIN BLOQUE VALIDACIONES

    //BLOQUE CARGA DE MEMORIA
    public void startMemory(String file, byte[] allbytes, int param_size) throws Exception {

        byte version = allbytes[5];

        if(file.endsWith(".vmi")){

            byte[] registers = new byte[64];
            byte[] segmentTable = new byte[32];
            byte[] memory = new byte[allbytes.length - 104];
            byte[] size = new byte[2];

            System.arraycopy(allbytes,6,size,0,2);
            System.arraycopy(allbytes,8, registers,0, 64);
            System.arraycopy(allbytes,72, segmentTable,0, 32);
            System.arraycopy(allbytes,104, memory,0,memory.length);

            this.registers.loadRegisters(registers);
            this.segTable.loadSegmentTable(segmentTable);
            this.virtualMemory.copyMemory(memory);

            
        }else{
            if (version == 1){
                super.startMemory(allbytes);
            } else {
                byte[] segments = new byte[12];
                byte[] memory = new byte[allbytes.length - 18];

                System.arraycopy(allbytes,6, segments,0, 12);
                System.arraycopy(allbytes,18, memory, 0, memory.length);

                this.segTable.setSegmentTable(segments, param_size);
                this.registers.initRegisters(segments);
                this.virtualMemory.setMemory(memory, segments, param_size);
            }
            
        }
    }

    public void initStack(int argc,int param_size) throws Exception{
        this.registers.setRegister(6, -1);

        Operand SP = new InmediateOperand(this);
        Mnemonic1 m = new PUSH(this);
        //PUSH *argv
        SP.setData(param_size);
        m.operate(SP);
        //PUSH argc
        SP.setData(argc);
        m.operate(SP);
        //PUSH RET(-1)
        SP.setData(-1);
        m.operate(SP);
    }

	
    public int getParamSize(String[] params){
        int param_size = 0;
        if(params == null)
            return 0;

        for(int i = 0; i < params.length; i++){
            for(int j = 0;j<params[i].length();j++)
                param_size ++;
            param_size++;
        }
        return param_size;
    }  

    public void loadParams(String[] params){
        int fisic_address = -1;

        for(int i = 0; i < params.length; i++){
            for(int j = 0; j<params[i].length(); j++){
                fisic_address++;
                this.virtualMemory.writeByte(fisic_address, (int)params[i].charAt(j));
            }
            fisic_address++;
            this.virtualMemory.writeByte(fisic_address, 0x00);    
        }

        //ARGV
        int acumulator = 0;
        fisic_address++;
        for(int i = 0; i < params.length; i++){
            int pointer = acumulator;
            this.virtualMemory.write4bytes(fisic_address, pointer);
            acumulator += params[i].length() + 1;
            fisic_address += 4;
        }
    }

}
