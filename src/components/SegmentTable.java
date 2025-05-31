package components;

public class SegmentTable {
	protected final int TABLE_SIZE;
	protected int[] segmentTable;
	protected int CS;
	
	public SegmentTable() {
		TABLE_SIZE = 8;
		this.segmentTable = new int[TABLE_SIZE];
		this.CS = 0;
	}
	
	public int[] getSegmentTable() {
		return this.segmentTable;
	}
	
	public int getCS(){
		return this.CS;
	}
	
	public int getBase(int n) {
		return (this.segmentTable[n] >> 16) & 0xFFFF;
	}
	
	public int getSize(int n) {
		return (this.segmentTable[n] & 0xFFFF);
	}

	public void setSegmentTable(byte[] size, int MEMORY_SIZE) {
		int s = ((int)(size[0] << 8)|(int)(size[1]) & 0xFF);
		this.segmentTable[0] = s & 0xFFFF;
		this.segmentTable[1] = (s << 16) | ((MEMORY_SIZE-s) & 0xFFFF);  
	}

	

	
	public int LogicToPhysic(int logic_address) throws Exception {
		int Mask = 0x0000FFFF;
		int segment = (logic_address >> 16) & Mask;
		int offset = logic_address & Mask;

		offset = (offset << 16) >> 16;
		
		int base_address = (this.segmentTable[segment] >> 16) & Mask;
		int segment_size = this.segmentTable[segment] & Mask;
		int limit_segment = base_address + segment_size; 
		int fisic_address = base_address + offset;
		
		if((fisic_address <= limit_segment) && (fisic_address >= base_address)) 
			return fisic_address;
		else if(fisic_address > limit_segment)
			throw new Exception("Attempt to access a segment after the limit address");
		else {
				throw new Exception("Attempt to access a segment before the base address");
			}
		}
	}
