package components;



public class VirtualMainMemory {
	protected int MEMORY_SIZE;
	protected byte[] memory;
	
	public VirtualMainMemory() {
		this.MEMORY_SIZE = 16384;
		this.memory = new byte[MEMORY_SIZE];
	}
	
	public int getMemorySize(){
		return MEMORY_SIZE;
	}

	public byte[] getMemory(){
		return this.memory;
	}

	public int readByte(int fisic_address) {
		return (this.memory[fisic_address]) & 0xFF;
	}
	
	public void writeByte(int fisic_address, int data) {
		byte newdata = (byte) data;
		this.memory[fisic_address] = newdata;
	}

	public int read4bytes(int fisic_address){
		int block = 0;
		for(int i = 0; i < 4; i++){
			block = (block << 8) | readByte(fisic_address + i);
		}

		return block;
	}
	

	public void write4bytes(int fisic_address,int data){
		for(int i=3; i>=0 ; i--){
			writeByte(fisic_address+i,data);
			data = (data >> 8);
		}
	}

	public int readNbytes(int fisic_address, int size){
		int block = 0;
		for(int i = 0; i < size; i++){
			block = (block << 8) | readByte(fisic_address + i);
		}
		return block;

	}

	public void writeNbytes(int fisic_address, int size, int data){
		for(int i=size-1; i>=0 ; i--){
			writeByte(fisic_address+i,data);
			data = (data >> 8);
		}
	}

	public void setMemory(byte[] memory, byte[] size, int param_size) throws Exception {
		int s = ((int)(size[0] << 8)|(int)(size[1]) & 0xFF);
		if(s > this.MEMORY_SIZE) {
			throw new Exception("Exceeded memory size");
		}else {
			System.arraycopy(memory, 0, this.memory, 0, memory.length);
		}
	}

	public void copyMemory(byte[] memory) throws Exception{
		if(memory.length > this.MEMORY_SIZE) {
			throw new Exception("Exceeded memory size");
		}else {
			System.arraycopy(memory, 0, this.memory, 0, memory.length);
		}
	}
	
} 
