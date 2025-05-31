package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class JNZ extends Mnemonic1 {

    public JNZ(VirtualMachine vm) {
        super(vm, "JNZ");
    }

    @Override
    public void operate(Operand A) throws Exception {

        int cc = this.vm.getRegisters().getRegister(8);
        if ((cc & 0x40000000) == 0) { 
           // int address = A.getValue(); 
           // vm.getRegisters().setRegister(5, address);

           
            int address = this.vm.getRegisters().getRegister(0);
            address += A.getValue();
            this.vm.getRegisters().setRegister(5, address);

        } 

    }                               

}
