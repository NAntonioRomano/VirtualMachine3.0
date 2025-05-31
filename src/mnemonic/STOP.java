package mnemonic;
import components.VirtualMachine;
public  class STOP extends Mnemonic0 {

    public STOP(VirtualMachine vm) {
        super(vm, "STOP");
    }

    @Override
    public void operate() throws Exception {
        this.vm.getRegisters().setRegister(5, -1);
    }
}