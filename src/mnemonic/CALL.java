package mnemonic;

import components.VirtualMachine;
import operands.InmediateOperand;
import operands.Operand;

public class CALL extends Mnemonic1 {

    public CALL(VirtualMachine vm) {
        super(vm, "CALL");
    }

    @Override
    public void operate(Operand A) throws Exception {
        //PUSH IP
        Operand IP = new InmediateOperand(this.vm);
        Mnemonic1 m = new PUSH(this.vm);
        IP.setData(this.vm.getRegisters().getRegister(5));
        m.operate(IP);

        //MOV <IP>,<CS>
        int logic_address = this.vm.getRegisters().getRegister(0);

        //ADD <IP>,<A.getValue>
        logic_address += A.getValue();
        this.vm.getRegisters().setRegister(5, logic_address);
    
    }
    
}
