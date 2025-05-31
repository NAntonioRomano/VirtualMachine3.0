package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class RND extends Mnemonic2 {

    public RND(VirtualMachine vm) {
        super(vm, "RND");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int valueB = B.getValue();
        A.setValue((int) (Math.random() * valueB));

    }

}
