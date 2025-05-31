package componentsSecondPart;

import components.VirtualMainMemory;

public class VirtualMainMemory2 extends VirtualMainMemory {

    public VirtualMainMemory2(int MEMORY_SIZE){
        this.MEMORY_SIZE = MEMORY_SIZE;
        this.memory = new byte[MEMORY_SIZE];
    }

    public void setMemorySize(int MEMORY_SIZE){
        this.MEMORY_SIZE = MEMORY_SIZE;
        this.memory = new byte[MEMORY_SIZE];
    }

    @Override
    public void setMemory(byte[] memory, byte[] size,int param_size) throws Exception {
        if(size.length == 2){
            super.setMemory(memory, size,0);
        }else{
            int program_size = 0;
            int CS = ((int)(size[0] << 8)|(int)(size[1]& 0xFF));
            int KS = ((int)(size[8] << 8)|(int)(size[9]& 0xFF));
            
            for(int i = 0; i < size.length - 2; i+=2){
                program_size += ((int)(size[i] << 8)|(int)(size[i+1]& 0xFF));
            }

            if(program_size > this.MEMORY_SIZE) {
                throw new Exception("Exceeded memory size");
            }else {
                System.arraycopy(memory, CS, this.memory , param_size, KS);
                System.arraycopy(memory, 0, this.memory, param_size + KS, CS);
            }
        }  
    }

}
 