package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class JNN extends Mnemonic1 {

    public JNN(VirtualMachine vm) {
        super(vm, "JNN");
    }

    @Override
    public void operate(Operand A) throws Exception {

        int cc = this.vm.getRegisters().getRegister(8);
        if ((cc & 0x80000000) == 0) { 
            //int address = A.getValue(); 
            //vm.getRegisters().setRegister(5, address);

            
            int address = this.vm.getRegisters().getRegister(0);
            address += A.getValue();
            this.vm.getRegisters().setRegister(5, address);

        }
    }

}
