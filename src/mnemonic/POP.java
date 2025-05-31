package mnemonic;

import components.VirtualMachine;
import operands.Operand;
import exceptions.*;

public class POP extends Mnemonic1{

    


    public POP(VirtualMachine vm) {
        super(vm, "POP");
    }

    @Override
    public void operate(Operand A) throws Exception {
        int SP = this.vm.getRegisters().getRegister(6);
        int logic_address;
        
        if (SP == -1 || SP > (this.vm.getRegisters().getRegister(3) + this.vm.getSegTable().getSize(SP >> 16))) {
            throw new StackUnderflowException("Stack Underflow Exception");
        }
        A.setValue(this.vm.getVirtualMemory().read4bytes(this.vm.getSegTable().LogicToPhysic(SP)));
        
        SP += 4;
        this.vm.getRegisters().setRegister(6,SP);

    }

    
    
}
