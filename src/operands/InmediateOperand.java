package operands;

import components.VirtualMachine;

public  class InmediateOperand extends Operand {
    
    public InmediateOperand(VirtualMachine vm) {
        super(vm);
    }
    
    @Override
    public int getValue() throws Exception {
        int result = ((this.data << 16) >> 16);
        return result; 
    }
    
    @Override
    public void setValue(int value) throws Exception {
        throw new Exception("Inmediate operand cannot be modified");
    }

    @Override
    public String toString() {
        return Integer.toString((this.data << 16)>>16);
    }
}