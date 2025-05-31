package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class JMP extends Mnemonic1 {


    public JMP(VirtualMachine vm) {
        super(vm, "JMP");
    }

    @Override
    public void operate(Operand A) throws Exception {
        //int address = A.getValue(); 
        //vm.getRegisters().setRegister(5, address); 


        int address = this.vm.getRegisters().getRegister(0);
        address += A.getValue();
        this.vm.getRegisters().setRegister(5, address);
    }
}   